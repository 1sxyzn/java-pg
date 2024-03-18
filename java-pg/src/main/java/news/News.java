package news;

public class News{
	private int aid;
	private String title;
	private String img;
	private String date;
	private String content;
	
	// getter
	public int getAid() {return aid;}
	public String getTitle() {return title;}
	public String getImg() {return img;}
	public String getDate() {return date;}
	public String getContent() {return content;}
	
	// setter
	public void setAid(int aid) {this.aid = aid;}
	public void setTitle(String title) {this.title = title;}
	public void setImg(String img) {this.img = img;}
	public void setDate(String date) {this.date = date;}
	public void setContent(String content) {this.content = content;}
}