package casia.isiteam.zhihu_event.service;

import java.io.IOException;
import org.jsoup.Jsoup;

import casia.isiteam.zhihu_event.util.UserAgent;

/**
 * jsoup下载器
 * 
 * @author wd
 * @version jdk 1.7
 * @date 2018年7月20日
 */

public class JsoupDownloader {

	public String download(String url) {
		
			String path=url.replace("https://www.zhihu.com", "");
			int count = 3;
			while (count > 0) {
				count--;
				String html = "";
				try {
					html = Jsoup.connect(url)
							.header(":path", path)
							.header(":authority", "www.zhihu.com")
							.header(":method", "GET")
							.header(":scheme", "https")
							.header("Connection", "keep-alive")
							.header("Accept",
									"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
							.header("Accept-Encoding", "gzip, deflate").header("Accept-Language", "zh-CN,zh;q=0.9")
							.userAgent(UserAgent.getUserAgent())
							.timeout(30000).get().html();

					if (html != null && !html.equals("") && !html.equals("null")) {
						return html;
					}
				} catch (IOException e) {
					// e.printStackTrace();
				}
			} 
		return "";
	}

	public static void main(String[] args) {

		JsoupDownloader jd = new JsoupDownloader();
		String download = jd.download("http://fgw.guang-an.gov.cn:8090/");
		System.out.println(download);
	}
}
