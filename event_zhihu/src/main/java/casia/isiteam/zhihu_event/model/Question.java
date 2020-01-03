package casia.isiteam.zhihu_event.model;

import java.sql.Timestamp;
import java.util.Date;

public class Question {
	private long auto_id;					//ID
	private String question;				//问题标题
	private Date question_time;
	private String question_describe;		//问题描述
	private int replycount;					//回复量
	private String content;					//内容
	private int viewcount;					//浏览量
	private int attenttioncount;			//关注量
	private String keywords;				//关键词
	private String host;
	private String url;
	private String url_md5;
	private int type;
	private Timestamp insert_time;
	
	
	
	public Date getQuestion_time() {
		return question_time;
	}
	public void setQuestion_time(Date question_time) {
		this.question_time = question_time;
	}
	public long getAuto_id() {
		return auto_id;
	}
	public void setAuto_id(long auto_id) {
		this.auto_id = auto_id;
	}
	
	public String getQuestion_describe() {
		return question_describe;
	}
	public void setQuestion_describe(String question_describe) {
		this.question_describe = question_describe;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getViewcount() {
		return viewcount;
	}
	public void setViewcount(int viewcount) {
		this.viewcount = viewcount;
	}
	public int getAttenttioncount() {
		return attenttioncount;
	}
	public void setAttenttioncount(int attenttioncount) {
		this.attenttioncount = attenttioncount;
	}
	public int getReplycount() {
		return replycount;
	}
	public void setReplycount(int replycount) {
		this.replycount = replycount;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUrl_md5() {
		return url_md5;
	}
	public void setUrl_md5(String url_md5) {
		this.url_md5 = url_md5;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Timestamp getInsert_time() {
		return insert_time;
	}
	public void setInsert_time(Timestamp insert_time) {
		this.insert_time = insert_time;
	}
	
	
}
