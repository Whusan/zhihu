package casia.isiteam.zhihu_event.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import casia.isiteam.zhihu_event.model.Forum;
import casia.isiteam.zhihu_event.util.DateUtil;
import casia.isiteam.zhihu_event.util.DomainUtil;
import casia.isiteam.zhihu_event.util.HtmlUtil;
import casia.isiteam.zhihu_event.util.MD5Util;
import casia.isiteam.zhihu_event.util.StringUtil;
import casia.isiteam.zhihu_event.util.TimeUtil;

public class ParseArticle {

	private JsoupDownloader jd = new JsoupDownloader();
	private TimeUtil tu2 = new TimeUtil();
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	//解析文章
	public Forum getArticle(int id) {
		
		String url="https://zhuanlan.zhihu.com/p/"+id;
		String html = jd.download(url);
		Document doc = Jsoup.parse(html);
		
		//标题
		String title = doc.select("header > h1").text();
		
		//时间
		String pubtime = doc.select("article > div.ContentItem-time").text();
		pubtime = tu2.parseDateTime(pubtime, null, null, null, null);
		Date time = new Date();
		try {
			time = formatter.parse(pubtime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(time.after(new Date())) {
			return null;
		}
		
		//作者
		String author = doc.select("a.UserLink-link").text();
		
		//作者URL
		String author_url = doc.select("a.UserLink-link").attr("href");
		author_url=DomainUtil.getAbsoluteURL(author_url, url);
		
		//正文
		String content =doc.select("article > div> div.RichText,ztext,Post-RichText").toString();
		content = HtmlUtil.delHTMLTag(content);
		
		if(StringUtil.isEmpty(title)||StringUtil.isEmpty(content)||StringUtil.isEmpty(author)||StringUtil.isEmpty(author_url)||StringUtil.isEmpty(time.toString())) {
			return null;
		}
		Forum forum=new Forum();
		forum.setTitle(title);
		forum.setContent(content);
		forum.setAuthor(author);
		forum.setPubtime(time);
		forum.setSite("www.zhihu.com");
		forum.setUrl(url);
		forum.setUrlMd5(MD5Util.md5(url));
		forum.setAuthorUrl(author_url);
		forum.setSourceType(2);
		forum.setNationCategory(2);
		forum.setReplycount(0);
		forum.setReviewcount(0);
		
		return forum;
	}
	
	public static void main(String[] args) {
		ParseArticle par=new ParseArticle();
		Forum article = par.getArticle(69902433);
		System.out.println(article);
	}
}
