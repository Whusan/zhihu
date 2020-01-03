package casia.isiteam.zhihu_event.model;

import java.sql.Timestamp;

/**
 * 作者信息实体类
 * @author wd
 * @version jdk 1.7
 * @date 2018年7月20日
 */
public class Users {
	
	@Override
	public String toString() {
		return "Users [fuid=" + fuid + ", fhid=" + fhid + ", " + (username != null ? "username=" + username + ", " : "")
				+ (author_url != null ? "author_url=" + author_url + ", " : "")
				+ (urlmd5 != null ? "urlmd5=" + urlmd5 + ", " : "")
				+ (inserttime != null ? "inserttime=" + inserttime : "") + "]";
	}
	private int fuid;
	private int fhid;
	private String username;
	private String author_url;
	private String urlmd5;
	private Timestamp inserttime;
	public int getFuid() {
		return fuid;
	}
	public void setFuid(int fuid) {
		this.fuid = fuid;
	}
	public int getFhid() {
		return fhid;
	}
	public void setFhid(int fhid) {
		this.fhid = fhid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getAuthor_url() {
		return author_url;
	}
	public void setAuthor_url(String author_url) {
		this.author_url = author_url;
	}
	public String getUrlmd5() {
		return urlmd5;
	}
	public void setUrlmd5(String urlmd5) {
		this.urlmd5 = urlmd5;
	}
	public Timestamp getInserttime() {
		return inserttime;
	}
	public void setInserttime(Timestamp inserttime) {
		this.inserttime = inserttime;
	}
	
}
