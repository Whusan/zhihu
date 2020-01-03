package casia.isiteam.zhihu_event.model;

public class Answer {
	
	private String author;			//作者
	private String author_url;		//作者链接
	private int voteup_count;		//赞同数
	private int comment_count;		//评论数
	private String content;			//内容
	private String question;		//问题
	private String question_url;	//问题链接
	private String question_url_md5;//md5
	private String answer_url;		//答案链接
	private String answer_url_md5;	//回答链接的md5

	
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getQuestion_url() {
		return question_url;
	}
	public void setQuestion_url(String question_url) {
		this.question_url = question_url;
	}
	public String getQuestion_url_md5() {
		return question_url_md5;
	}
	public void setQuestion_url_md5(String question_url_md5) {
		this.question_url_md5 = question_url_md5;
	}
	public String getAnswer_url() {
		return answer_url;
	}
	public void setAnswer_url(String answer_url) {
		this.answer_url = answer_url;
	}
	public String getAnswer_url_md5() {
		return answer_url_md5;
	}
	public void setAnswer_url_md5(String answer_url_md5) {
		this.answer_url_md5 = answer_url_md5;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getAuthor_url() {
		return author_url;
	}
	public void setAuthor_url(String author_url) {
		this.author_url = author_url;
	}
	public int getVoteup_count() {
		return voteup_count;
	}
	public void setVoteup_count(int voteup_count) {
		this.voteup_count = voteup_count;
	}
	public int getComment_count() {
		return comment_count;
	}
	public void setComment_count(int comment_count) {
		this.comment_count = comment_count;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
}
