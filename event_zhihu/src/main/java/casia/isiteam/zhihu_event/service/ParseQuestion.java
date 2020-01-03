package casia.isiteam.zhihu_event.service;

import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import casia.isiteam.zhihu_event.model.Forum;
import casia.isiteam.zhihu_event.util.DateUtil;
import casia.isiteam.zhihu_event.util.MD5Util;
import casia.isiteam.zhihu_event.util.StringUtil;

public class ParseQuestion {

	private JsoupDownloader jd = new JsoupDownloader();

	public Forum getContent(int id) {

		// int downLoadPage = UrlReptile.downLoadPage(url);
		// if(downLoadPage!=200){
		// System.out.println(url+"状态码为"+downLoadPage);
		// return;
		// }
		
		String url="https://www.zhihu.com/question/"+id;

		String download = jd.download(url);
		Document doc = Jsoup.parse(download);

		// 问题
		String question = doc.select("h1.QuestionHeader-title").get(0).text();
		//System.out.println("问题 :" + question);

		// 问题创建时间
		String quetion_time = doc.select("meta[itemProp='dateCreated']").attr("content");
		Date dateTime = DateUtil.dateTime(quetion_time);
		if(dateTime.after(new Date())) {
			return null;
		}
		//System.out.println("提问时间 :" + dateTime);

		// 问题描述
		String question_describe = doc.select("div.QuestionRichText span").get(0).text();
		//System.out.println("问题描述 :" + question_describe);
		if(StringUtil.isEmpty(question_describe)) {
			question_describe="无问题描述";
		}
		// 回复量
		String replycount="0";
		try {
		replycount = doc.select("h4.List-headerText").get(0).text().replaceAll("\\D", "");
		//System.out.println("回复量 :" + replycount);
		} catch (Exception e) {
		}
		// 关注量
		String attenttioncount = doc.select("strong.NumberBoard-itemValue").get(0).text().replaceAll("\\D", "");
		//System.out.println("关注量 :" + attenttioncount);

		// 浏览量
		String viewcount = doc.select("strong.NumberBoard-itemValue").get(1).text().replaceAll("\\D", "");
		//System.out.println("浏览量 :" + viewcount);

		// 关键词
		String keywords = doc.select(".QuestionHeader-topics div#null-toggle").text().replaceAll(" ", "-");
		//System.out.println("关键词 :" + keywords);
		
		if(StringUtil.isEmpty(question)||StringUtil.isEmpty(question_describe)||StringUtil.isEmpty(dateTime.toString())) {
			return null;
		}
		
		Forum forum=new Forum();
		forum.setTitle(question);
		forum.setContent(question_describe);
		forum.setAuthor(null);
		forum.setPubtime(dateTime);
		forum.setSite("www.zhihu.com");
		forum.setUrl(url);
		forum.setUrlMd5(MD5Util.md5(url));
		forum.setAuthorUrl(null);
		forum.setSourceType(2);
		forum.setNationCategory(2);
		forum.setReplycount(Integer.parseInt(replycount));
		forum.setReviewcount(Integer.parseInt(viewcount));
		return forum;

		// 回答
		//ArrayList<Answer> answers = getAnswers(url, Integer.parseInt(replycount));
		//String content = JSONArray.fromObject(answers).toString();
	}
}
