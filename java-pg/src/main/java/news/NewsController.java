package news;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

import org.apache.commons.beanutils.BeanUtils;

@WebServlet(urlPatterns = "/news.nhn")
@MultipartConfig(maxFileSize=1024*1024*2, location="C:/Code/java/jsp-web/img")
public class NewsController extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	private NewsDAO dao;
	private ServletContext sc;
	
	private final String START_PAGE = "news/newsList.jsp";
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		dao = new NewsDAO();
		sc = getServletContext();
	}
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String action = request.getParameter("action");
		dao = new NewsDAO();
		
		Method m;
		String view = null;
		
		if(action == null) action = "listNews";
		
		try {
			m = this.getClass().getMethod(action, HttpServletRequest.class);
			view = (String)m.invoke(this, request);
		} catch(NoSuchMethodException e) {
			e.printStackTrace();
			sc.log("요청 action 없음");
			request.setAttribute("error", "action 파라미터 에러");
			view = START_PAGE;
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		if(view.startsWith("redirect:/")) {
			String rview = view.substring("redirect:/".length());
			response.sendRedirect(rview);
		}
		else {
			RequestDispatcher dispatcher = request.getRequestDispatcher(view);
			dispatcher.forward(request, response);
		}
	}
	
	public String addNews(HttpServletRequest request) {
		News n = new News();
		try {
			Part part = request.getPart("file");
			String fileName = getFilename(part);
			if(fileName != null && !fileName.isEmpty()) part.write(fileName);
			BeanUtils.populate(n, request.getParameterMap());
			n.setImg("/img/"+fileName);
			dao.addNews(n);
		}catch(Exception e) {
			e.printStackTrace();
			sc.log("뉴스 등록 에러");
			request.setAttribute("error", "뉴스 등록 실패");
			return listNews(request);
		}
		return "redirect:/news.nhn?action=listNews";
	}
	
	public String deleteNews(HttpServletRequest request) {
    	int aid = Integer.parseInt(request.getParameter("aid"));
		try {
			dao.delNews(aid);
		} catch (SQLException e) {
			e.printStackTrace();
			sc.log("뉴스 삭제 에러");
			request.setAttribute("error", "뉴스 삭제 실패");
			return listNews(request);
		}
		return "redirect:/news.nhn?action=listNews";
	}

	public String listNews(HttpServletRequest request) {
    	List<News> list;
		try {
			list = dao.getAll();
	    	request.setAttribute("newslist", list);
		} catch (Exception e) {
			e.printStackTrace();
			sc.log("뉴스 목록 조회 에러");
			request.setAttribute("error", "뉴스 목록 조회 실패");
		}
    	return "news/newsList.jsp";
    }
    
    public String getNews(HttpServletRequest request) {
        int aid = Integer.parseInt(request.getParameter("aid"));
        try {
			News n = dao.getNews(aid);
			request.setAttribute("news", n);
		} catch (SQLException e) {
			e.printStackTrace();
			sc.log("뉴스 조회 에러");
			request.setAttribute("error", "뉴스 조회 실패");
		}
    	return "news/newsView.jsp";
    }

	private String getFilename(Part part) {
        String fileName = null; 
        String header = part.getHeader("content-disposition");
        System.out.println("Header => "+header);
        int start = header.indexOf("filename=");
        fileName = header.substring(start+10,header.length()-1);        
        sc.log("파일명:"+fileName);        
        return fileName; 
	}
}
