package casia.isiteam.zhihu_event.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import casia.isiteam.zhihu_event.model.Forum;
import casia.isiteam.zhihu_event.util.HtmlUtil;
import casia.isiteam.zhihu_event.util.MD5Util;
import casia.isiteam.zhihu_event.util.StringUtil;
import casia.isiteam.zhihu_event.util.UserAgent;

public class ParseAnswer {

	/**
	 * 解析全部的答案
	 * 
	 * @param id
	 *            问题的ID
	 * @param replycount
	 *            回复量
	 * @return
	 */
	public ArrayList<Forum> getAnswersAll(int id, int replycount) {

		ArrayList<Forum> list = new ArrayList<Forum>();
		String url="https://www.zhihu.com/question/"+id;
		//int id = Integer.parseInt(url.substring(url.indexOf("question") + 9));
		for (int x = 0; x <= replycount / 20; x++) {
			String link = "https://www.zhihu.com/api/v4/questions/" + id + "/answers?include=data%5B%2A%5D.is_normal%2Cadmin_closed_comment%2Creward_info%2Cis_collapsed%2Cannotation_action%2Cannotation_detail%2Ccollapse_reason%2Cis_sticky%2Ccollapsed_by%2Csuggest_edit%2Ccomment_count%2Ccan_comment%2Ccontent%2Ceditable_content%2Cvoteup_count%2Creshipment_settings%2Ccomment_permission%2Ccreated_time%2Cupdated_time%2Creview_info%2Crelevant_info%2Cquestion%2Cexcerpt%2Crelationship.is_authorized%2Cis_author%2Cvoting%2Cis_thanked%2Cis_nothelp%3Bdata%5B%2A%5D.mark_infos%5B%2A%5D.url%3Bdata%5B%2A%5D.author.follower_count%2Cbadge%5B%2A%5D.topics&limit=20&offset=" + x * 20
					+ "&sort_by=default";
			Map<String, String> httpHeader=new HashMap<String, String>();
			httpHeader.put("user-agent", UserAgent.getUserAgent());
			String html = null;
			try {
				html = HttpClientDownloader.httpGet(link, httpHeader);
				JSONObject jsonObject = new JSONObject(html);
				org.json.JSONArray array = jsonObject.getJSONArray("data");

				for (int i = 0; i < array.length(); i++) {

					JSONObject o = array.getJSONObject(i);
					Forum forum = new Forum();
					// 问题
					String question = new JSONObject(o.get("question").toString()).getString("title");
					// 作者
					String author = new JSONObject(o.get("author").toString()).getString("name");
					// 作者URL
					String author_url = "https://www.zhihu.com/people/"
							+ new JSONObject(o.get("author").toString()).getString("url_token");
					// 回答时间
					String pubtime = (String) o.get("created_time");
					Date time = parseTimestamp(pubtime+"");
					// 赞同量
					int voteup_count = (Integer) o.get("voteup_count");
					// 评论量
					int comment_count = (Integer) o.get("comment_count");
					// 内容
					String content = HtmlUtil.delHTMLTag((String) o.get("content"));

					// 答案链接
					String answer_url = (String) o.get("url");
					answer_url = answer_url.substring(answer_url.lastIndexOf("/"));
					answer_url = url + "/answer" + answer_url;
	
					forum.setTitle(question);
					forum.setContent(content);
					forum.setAuthor(author);
					forum.setPubtime(time);
					forum.setSite("www.zhihu.com");
					forum.setUrl(answer_url);
					forum.setUrlMd5(MD5Util.md5(answer_url));
					forum.setAuthorUrl(author_url);
					forum.setSourceType(2);
					forum.setNationCategory(2);
					forum.setReplycount(comment_count);
					forum.setReviewcount(0);
					list.add(forum);
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return list;
	}

	/**
	 * 解析一页的答案
	 * 
	 * @param id
	 *            问题的ID
	 * @param replycount
	 *            回复量
	 * @return
	 */
	public ArrayList<Forum> getAnswersOnePage(int id) {

		ArrayList<Forum> list = new ArrayList<Forum>();
		String url="https://www.zhihu.com/question/"+id;
		//int id = Integer.parseInt(url.substring(url.indexOf("question") + 9));
		String link = "https://www.zhihu.com/api/v4/questions/" + id + "/answers?include=data%5B%2A%5D.is_normal%2Cadmin_closed_comment%2Creward_info%2Cis_collapsed%2Cannotation_action%2Cannotation_detail%2Ccollapse_reason%2Cis_sticky%2Ccollapsed_by%2Csuggest_edit%2Ccomment_count%2Ccan_comment%2Ccontent%2Ceditable_content%2Cvoteup_count%2Creshipment_settings%2Ccomment_permission%2Ccreated_time%2Cupdated_time%2Creview_info%2Crelevant_info%2Cquestion%2Cexcerpt%2Crelationship.is_authorized%2Cis_author%2Cvoting%2Cis_thanked%2Cis_nothelp%3Bdata%5B%2A%5D.mark_infos%5B%2A%5D.url%3Bdata%5B%2A%5D.author.follower_count%2Cbadge%5B%2A%5D.topics&limit=20&offset=0&sort_by=default";
		Map<String, String> httpHeader=new HashMap<String, String>();
		httpHeader.put("user-agent", UserAgent.getUserAgent());
		String html = null;
		try {
			html = HttpClientDownloader.httpGet(link, httpHeader);

			JSONObject jsonObject = new JSONObject(html);
			JSONArray array = jsonObject.getJSONArray("data");

			for (int i = 0; i < array.length(); i++) {

				// Answer answer = new Answer();
				Forum forum = new Forum();

				JSONObject o = array.getJSONObject(i);

				// 问题
				String question = new JSONObject(o.get("question").toString()).getString("title");
				// 作者
				String author = new JSONObject(o.get("author").toString()).getString("name");

				// 回答时间
				int pubtime = (int) o.get("created_time");
				Date time = parseTimestamp(pubtime+"");
				// 作者URL
				String author_url =  new JSONObject(o.get("author").toString()).getString("url_token");
				if(!StringUtil.isEmpty(author_url)) {
					author_url="https://www.zhihu.com/people/"+author_url;
				}
				// 赞同量
				int voteup_count = (Integer) o.get("voteup_count");
				// 评论量
				int comment_count = (Integer) o.get("comment_count");
				// 内容
				String content = HtmlUtil.delHTMLTag((String) o.get("content"));

				// 答案链接
				String answer_url = (String) o.get("url");
				answer_url = answer_url.substring(answer_url.lastIndexOf("/"));
				answer_url = url + "/answer" + answer_url;

				forum.setTitle(question);
				forum.setContent(content);
				forum.setAuthor(author);
				forum.setPubtime(time);
				forum.setSite("www.zhihu.com");
				forum.setUrl(answer_url);
				forum.setUrlMd5(MD5Util.md5(answer_url));
				forum.setAuthorUrl(author_url);
				forum.setSourceType(2);
				forum.setNationCategory(2);
				forum.setReplycount(comment_count);
				forum.setReviewcount(0);
				list.add(forum);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	public static Date parseTimestamp(String timestamp) {

		long parseLong = Long.parseLong(timestamp);
		Date date = null;
		if (timestamp.length() == 10) {
			date = new Date(parseLong * 1000);
		} else if (timestamp.length() == 13) {
			date = new Date(parseLong);
		}
		return date;
	}

	public static void main(String[] args) {
		
		ParseAnswer pa=new ParseAnswer();
		ArrayList<Forum> forums = pa.getAnswersOnePage(272494886);
		for (Forum forum : forums) {
			System.out.println(forum);
		}
	}

}
