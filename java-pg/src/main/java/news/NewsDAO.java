package news;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;
import java.util.*;

public class NewsDAO{
	final String JDBC_DRIVER = "org.h2.Driver";
	final String JDBC_URL = "jdbc:h2:tcp://localhost/~/news";
	
	public Connection open() {
		Connection conn = null;
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(JDBC_URL, "news", "news");
		} catch(Exception e) {e.printStackTrace();}
		return conn;
	}
	
	public List<News> getAll() throws Exception{
		Connection conn = open();
		List<News> newsList = new ArrayList<>();
		
		String sql = "SELECT AID, TITLE, PARSEDATETIME(DATE, 'yyyy-MM-dd hh:mm:ss') AS CDATE FROM NEWS";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		
		try(conn; pstmt; rs){
			while(rs.next()) {
				News n = new News();
				n.setAid(rs.getInt("AID"));
				n.setTitle(rs.getString("TITLE"));
				n.setDate(rs.getString("CDATE"));
				newsList.add(n);
			}
			return newsList;
		}
	}
	
	public News getNews(int aid) throws SQLException{
		Connection conn = open();
		News n = new News();
		
		String sql = "SELECT AID, TITLE, IMG, PARSEDATETIME(DATE, 'yyyy-MM-dd hh:mm:ss') AS CDATE, CONTENT FROM NEWS WHERE AID=?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, aid);
		ResultSet rs = pstmt.executeQuery();
		rs.next();

		try(conn; pstmt; rs){
			n.setAid(rs.getInt("AID"));
			n.setTitle(rs.getString("TITLE"));
			n.setImg(rs.getString("IMG"));
			n.setDate(rs.getString("CDATE"));
			n.setContent(rs.getString("CONTENT"));
			pstmt.executeQuery();
			return n;
		}
	}
	
	public void addNews(News n) throws Exception{
		Connection conn = open();
		
		String sql = "INSERT INTO NEWS(TITLE, IMG, DATE, CONTENT) VALUES(?, ?, CURRENT_TIMESTAMP(), ?)";
		PreparedStatement pstmt = conn.prepareStatement(sql);

		try(conn; pstmt){
			pstmt.setString(1, n.getTitle());
			pstmt.setString(2, n.getImg());
			pstmt.setString(3, n.getContent());
			pstmt.executeUpdate();
		}
	}
	
	public void delNews(int aid) throws SQLException{
		Connection conn = open();
		
		String sql = "DELETE FROM NEWS WHERE AID=?";
		PreparedStatement pstmt = conn.prepareStatement(sql);

		try(conn; pstmt){
			pstmt.setInt(1, aid);
			if(pstmt.executeUpdate()==0) {
				throw new SQLException("DB ERROR");
			}
		}
	}
}