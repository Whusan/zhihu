package casia.isiteam.zhihu_event.util;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 随机获取一个UserAgent
 * @author wd
 * @version jdk 1.7
 * @date 2018年7月20日
 */
public class UserAgent {
	
	public static String[] userAgents =  new String[]{
			"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36 SE 2.X MetaSr 1.0",
			"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.167 Safari/537.36",
			"Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:62.0) Gecko/20100101 Firefox/62.0",
			"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3534.4 Safari/537.36",
			"Mozilla/5.0 (MSIE 10.0; Windows NT 6.1; Trident/5.0)",
			"Opera/9.80 (Windows NT 6.0) Presto/2.12.388 Version/12.14",
			"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_3) AppleWebKit/537.75.14 (KHTML, like Gecko) Version/7.0.3 Safari/7046A194A",
			"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:33.0) Gecko/20120101 Firefox/33.0",
			"Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.73 Safari/537.36 OPR/34.0.2036.25",
			"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Safari/537.36 Edge/13.10586",
			"Googlebot/2.1 (+http://www.googlebot.com/bot.html)",
			"Mozilla/5.0 (compatible; bingbot/2.0; +http://www.bing.com/bingbot.htm)"
			
	};
	
	
	public static String getUserAgent(){
		int index = userAgents.length-1;
		String result = userAgents[index];
		int index2 = new Integer(Double.toString(Math.rint(Math.random())).replace(".0", ""));
		if(index2>=0 && index2<=index){
			result = userAgents[index2];
		}
		return result;
	}
	
	public static void main(String[] args) throws IOException {
		String url ="http://search.sina.com.cn/?c=blog&q=%B1%B1%BE%A9+%B7%BF%BC%DB&range=article&by=all&sort=time";
		String html = Jsoup.connect(url).userAgent("Googlebot/2.1 (+http://www.googlebot.com/bot.html)").get().html();
		Elements select = Jsoup.parse(html).select("div[id='result'] >div>div h2 a");
		for (Element element : select) {
			System.out.println(element.text());
		}
	}

}
