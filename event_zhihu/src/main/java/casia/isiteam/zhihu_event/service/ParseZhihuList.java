package casia.isiteam.zhihu_event.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.PropertyConfigurator;
import org.json.JSONArray;
import org.json.JSONObject;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import casia.isiteam.zhihu_event.model.UrlType;
import casia.isiteam.zhihu_event.model.ZhihuListStatus;
import casia.isiteam.zhihu_event.util.UserAgent;

public class ParseZhihuList {

	public static void main(String[] args) {
		PropertyConfigurator.configureAndWatch("conf/log4j.properties");
		ParseZhihuList pz=new ParseZhihuList();
		String url = "https://www.zhihu.com/api/v4/search_v3?t=general&q=%E8%8A%B1%E8%8A%B1%E8%8A%B1%E8%8A%B1%E8%AE%B8%E5%B5%A9%E8%AE%B8%E5%B5%A9&correction=1&offset=105&limit=10&show_all_topics=0&search_hash_id=44bb3b799c6c89ca35b12afa40d2eba9";
		pz.getQuestionsList(url);
	}

	public ZhihuListStatus getQuestionsList(String url) {
		String html = null;
		ZhihuListStatus listStatus =new ZhihuListStatus();
		ArrayList<UrlType> urlList=new ArrayList<UrlType>();
		try {
			Map<String, String> httpHeader=new HashMap<String, String>();
			httpHeader.put("user-agent", UserAgent.getUserAgent());
			html = HttpClientDownloader.httpGet(url, httpHeader);
			JSONObject jsonObject = new JSONObject(html);
			
			//判断是不是最后一页
			ReadContext rc = JsonPath.parse(jsonObject.toString());
			boolean is_end = rc.read("$.paging.is_end");
			
			JSONArray array = jsonObject.getJSONArray("data");
			for (int i = 0; i < array.length(); i++) {
				JSONObject o = array.getJSONObject(i);
				//获取类型
				String type = (String) o.get("type");
				if(type.equals("search_section")||type.equals("relevant_query")||type.equals("knowledge_ad")) {
					continue;
				}
				
				ReadContext context = JsonPath.parse(o.toString());
				//内容也类型
				String contentType = null;
				try {
					contentType=context.read("$.object.type");
				} catch (Exception e) {
					System.out.println(url);
					return listStatus;
				}
				
				if(contentType.equals("answer")) {
					String id = context.read("$.object.question.id");
					//link="https://www.zhihu.com/question/"+link;
					urlList.add(new UrlType(Integer.parseInt(id),1));
					//System.out.println(id);
				}else if(contentType.equals("article")) {
					String id = context.read("$.object.id");
					//link="https://zhuanlan.zhihu.com/p/"+link;
					urlList.add(new UrlType(Integer.parseInt(id),2));
					//System.out.println(id);
				}
			}
			listStatus.setUrlList(urlList);
			listStatus.setIs_end(is_end);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return listStatus;
	}
}
