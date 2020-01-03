package casia.isiteam.zhihu_event.model;

public class UrlType {

	private int id;
	private int type;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public UrlType(int id, int type) {
		super();
		this.id = id;
		this.type = type;
	}
	public UrlType() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
