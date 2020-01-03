package casia.isiteam.zhihu_event.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 对URL的一些操作集（提取主机、域名、相对URL转绝对URL）
 * 
 * @author jf.rong Modify 2015-08-22
 * @author LiWei
 * @author PengX
 *
 */
public class DomainUtil {
	private static Logger log = LoggerFactory.getLogger(DomainUtil.class);
	/**
	 * 国际域名后缀
	 */
	private static HashSet<String> internationalSuffix;

	/**
	 * 国家域名后缀
	 */
	private static HashSet<String> countrySuffix;
	
	private static HashSet<String> completeSuffic;

	private static Pattern p = Pattern.compile("([\\w.-]+)");

	static {
		internationalSuffix = new HashSet<String>();
		countrySuffix = new HashSet<String>();
		completeSuffic = new HashSet<String>();

		try {
			
			//BufferedReader br = new BufferedReader(new InputStreamReader(DomainUtil.class.getClassLoader().getResourceAsStream("domain_suffix.suffix")));
			BufferedReader br = new BufferedReader(new FileReader(new File("conf/domain_suffix.suffix")));
			String line = null;
			while ((line = br.readLine()) != null) {
				if (line.equalsIgnoreCase("[International]")) {
					while ((line = br.readLine()) != null && !line.equalsIgnoreCase("[/International]")) {
						line = line.replaceAll("[\\s.]+", "");
						if (!line.equals("")) {
							internationalSuffix.add(line);
						}
					}
				}

				if (line.equalsIgnoreCase("[Country]")) {
					while ((line = br.readLine()) != null && !line.equalsIgnoreCase("[/Country]")) {
						line = line.replaceAll("[\\s.]+", "");
						if (!line.equals("")) {
							countrySuffix.add(line);
						}
					}
				}
				if (line.equalsIgnoreCase("[Complete]")) {
					while ((line = br.readLine()) != null && !line.equalsIgnoreCase("[/Complete]")) {
						line = line.replaceAll("[\\s]+", "");
						if (!line.equals("")) {
							completeSuffic.add(line);
						}
					}
				}
			}

			br.close();
		} catch (FileNotFoundException e) {
			log.error(ExceptionUtils.getStackTrace(e));
		} catch (IOException e) {
			log.error(ExceptionUtils.getStackTrace(e));
		}

	}

	/**
	 * 从URL中提取域名，如http://news.163.com/xxx/xxx.html --> 163.com
	 * 
	 * @param url
	 *            输入的URL
	 * @return String 
	 */
	public static String getDomain(String url) {
		String host = getHost(url);
		
		if(host == null){
			return host;
		}
		if (host.matches("\\d+(\\.\\d+){3,}")) {
			return host;
		}

		String[] segments = host.split("\\.");

		if (segments.length < 3) {
			return host;
		}
		
		String lastSegment = segments[segments.length - 1];
		String secondLastSegment = segments[segments.length - 2];
		
		String twoSegment = secondLastSegment + "." + lastSegment;
		
		if(!completeSuffic.contains(twoSegment)){
			if ((internationalSuffix.contains(lastSegment) || countrySuffix.contains(lastSegment))
					&& !internationalSuffix.contains(secondLastSegment)) {
				return secondLastSegment + "." + lastSegment;
			}
		}
		

		return segments[segments.length - 3] + "." + secondLastSegment + "." + lastSegment;
	}

	/**
	 * 从URL中提取主机，如http://news.163.com/xxx/xxx.html --> news.163.com
	 * 
	 * @param url
	 *            输入的URL
	 * @return String 主机地址
	 */
	public static String getHost(String url) {
		if (url == null || url.trim().isEmpty()) {
			return null;
		}
		try {
			url = URLDecoder.decode(url, "UTF-8");
			url = url.toLowerCase();
			url = url.replaceAll("#", "");
			url = url.replaceAll("!", "");
			if (url.contains("?")) {
				url = url.substring(0, url.indexOf("?"));
			}
		} catch (Exception e) {
			//
		}
		url = url.replaceAll("^https?://", "");
		Matcher matcher = p.matcher(url);
		matcher.find();
		return matcher.group(1);
	}

	/**
	 * 
	 * @param srcURL
	 *            需要转换的URL
	 * @param baseURL
	 *            srcURL所在的URL
	 * @return String URL绝对地址
	 */
	public static String getAbsoluteURL(String srcURL, String baseURL) {
		if (baseURL == null || srcURL == null) {
			return srcURL;
		}
		if (srcURL.matches("(?i)mailto:.*"))
			return null;
		if (srcURL.startsWith("//")) {
			if (baseURL.startsWith("https://")) {
				return "https:" + srcURL;
			} else {
				return "http:" + srcURL;
			}
		}
		srcURL = srcURL.replaceAll("^(?:\"|\\./|\"\\./)|\"$", "");

		if (srcURL.toLowerCase().startsWith("http://") || srcURL.toLowerCase().startsWith("https://"))
			return trimURL(srcURL, "extra");
		else if (srcURL.matches("(?i)javascript:.*"))
			return null;

		String absoluteURL = null;

		String base_host = baseURL.replaceAll("(https?://[^/]+).*", "$1");
		String base_app = baseURL.replaceAll("https?://[^/]+(/(?:[^/\\?]+/)*)?.*", "$1");
		if (!base_app.startsWith("/")) {
			base_app = "/" + base_app;
		}

		if (srcURL.startsWith("/")) {
			absoluteURL = base_host + srcURL;
		} 
		else if (srcURL.startsWith("../")) {
			while (srcURL.startsWith("../")) {
				srcURL = srcURL.substring(3);
				base_app = base_app.replaceAll("[^/]+/$", "");
			}

			absoluteURL = base_host + base_app + srcURL;
		}
		else if(srcURL.startsWith("?")){
			if(baseURL.indexOf("?") > 0){
				absoluteURL = baseURL.substring(0, baseURL.indexOf("?")) + srcURL;
			}
			else{
				absoluteURL = baseURL + srcURL;
			}
		}
		else {
			absoluteURL = base_host + base_app + srcURL;
		}

		return trimURL(absoluteURL, "extra;sid;fpage");
	}


	/**
	 * 去除URL中不需要的参数信息
	 * @param url
	 * @param params
	 * @return  String
	 */
	public static String trimURL(String url, String params){
		if(url == null || params == null){
			return url;
		}
		StringBuilder urlBuilder = new StringBuilder();
		String[] urls = url.split("[&?]");
		List<String> trimList = new ArrayList<String>();
		for(String trim: params.split(";")){
			trimList.add(trim);
		}
		
		for(int i = 0; i < urls.length; i++){
			if(!trimList.contains(urls[i].split("=")[0])){
				if(i == 0){
					urlBuilder.append(urls[i] + "?");
				}
				else{
					urlBuilder.append(urls[i] + "&");
				}
			}
		}
		if(urlBuilder.toString().endsWith("&") || urlBuilder.toString().endsWith("?")){
			urlBuilder.delete(urlBuilder.length() - 1, urlBuilder.length());
		}
		
		return urlBuilder.toString();
	}
	
	
	public static void main(String[] args) {
		System.out.println(DomainUtil.getAbsoluteURL("/p/5894825162?pid=122183383683&cid=0#122183383683", "http://tieba.baidu.com/f/search/res?isnew=1&kw=&qw=%BE%A9%B6%AB&un=&rn=10&pn=0&sd=&ed=&sm=1&only_thread=1"));
		String url = "https://www3.nhk.or.jp/news/html/20180910/k10011621811000.html";
		System.out.println(url);
		System.out.println(DomainUtil.getHost(url));
		System.out.println(DomainUtil.getDomain(url));
	}
}
