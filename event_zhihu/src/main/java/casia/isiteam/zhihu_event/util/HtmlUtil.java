package casia.isiteam.zhihu_event.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringEscapeUtils;

public class HtmlUtil {

	public static String stripHtml(String content) {

		// content = content.replaceAll("<img.*?\"\\s*>", "");

		content = content.replaceAll("<p class=\"mbn\">\\s+<a[\\s\\S]*?<\\/p>", "");
		// <p>段落替换为换行
		content = content.replaceAll("<p.*?>", "<br>");
		content = content.replaceAll("<tr.*?>", "<br>");
		// content = content.replaceAll("<img.*?\"\\s*>","$1");

		// 把空两行改为空一行
		content = content.replaceAll("(<br\\s*/?>(&nbsp;)*)+", "<br>");

		content = content.replaceAll("\\s{2,}", "");
		// <br><br/>替换为换行
		content = content.replaceAll("<br\\s*/?>", "\r\n");
		// 去掉其它的<>之间的东西
		// content = content.replaceAll("\\<.*?>", "");
		// 去掉多个换行
		// content = content.replaceAll("(\r?\n(\\s*\r?\n)+)", "\r\n");
		// 去掉style
		content = content.replaceAll("<span style=\"display:none\">[\\s\\S]*?<\\/span>", "");
		// 去掉干扰数据
		content = content.replaceAll("<font class=\"jammer\">[\\s\\S]*?<\\/font>", "");
		// 去掉广告
		content = content.replaceAll("<div class=\"attach_nopermission[\\s\\S]*?<\\/div>", "");
		content = content.replaceAll("<span class=\"atips_close\"[\\s\\S]*?<\\/span>", "");
		content = content.replaceAll("<div class=\"a_pr\"[\\s\\S]*?<\\/div>", "");
		
		// 去掉图片信息
		content = content.replaceAll("<div class=\"tip[\\s\\S]*?<\\/div>", "");
		content = content.replaceAll("<p class=\"mbn\">[\\s\\S]*?<\\/p>", "");
		// 对特殊字符进行转义
		content = StringEscapeUtils.unescapeHtml4(content.trim());
		return content;
	}

	public static String delHTMLTag(String htmlStr) {
		String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
		String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
		String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式

		htmlStr = stripHtml(htmlStr);
		Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
		Matcher m_script = p_script.matcher(htmlStr);
		htmlStr = m_script.replaceAll(""); // 过滤script标签

		Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
		Matcher m_style = p_style.matcher(htmlStr);
		htmlStr = m_style.replaceAll(""); // 过滤style标签

		Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
		Matcher m_html = p_html.matcher(htmlStr);
		// Matcher m_html=p_html.matcher("<(?!img)[^>");

		// htmlStr= m_html.replaceAll(""); //过滤html标签
		htmlStr = htmlStr.replaceAll("<(?!img)[^>]*>", "");
		//htmlStr = htmlStr.replaceAll("<(?)[^>]*>", "");
		htmlStr = htmlStr.replaceAll("[\\t\\f]", "");
		//htmlStr = htmlStr.replaceAll("(\r?\n(\\s*\r?\n)+)", "\r\n");

		//处理图片
		htmlStr = disposeImg(htmlStr.trim());
		
		htmlStr = htmlStr.replaceAll("(\r?\n(\\s*\r?\n)+)", "\r\n");
		return htmlStr.trim(); // 返回文本字符串
	}


	public static List<String>  getImgSrc(String content) {
		String img = "";
		Pattern p_image;
		Matcher m_image;
		List<String> pics = new ArrayList<String>();
		String regEx_img = "<img.*\\s*=\\s*(.*?)[^>]*?>";
		p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
		m_image = p_image.matcher(content);
		while (m_image.find()) {
			img = img + "," + m_image.group();
			// Matcher m = Pattern.compile("src=\"?(.*?)(\"|>|\\s+)").matcher(img); //匹配src
			Matcher m=null;
			String group =null;
			m = Pattern.compile("src\\s*=\"\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
			while (m.find()) {
				group = m.group(1);
			}
			if(group==null||group.contains("static/image/common/none.gif")||group.contains("pagespeed_static")) {
				m = Pattern.compile("[a-zA-Z]+file\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
				if (m.find()) {
					group = m.group(1);
					pics.add(group);
					return pics;
				}else {
					m = Pattern.compile("\\bfile\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
					if (m.find()) {
						group = m.group(1);
						pics.add(group);
						return pics;
					}
				}
				
			}else{
				pics.add(group);
				return pics;
			}
		}
		return pics;
	}

	public static String disposeImg(String content) {

		String reg = "<img.*?\"\\s*>";// 匹配只有三个字母的单词

		// 将规则封装成对象。
		Pattern p = Pattern.compile(reg);

		// 让正则对象和要作用的字符串相关联。获取匹配器对象。
		Matcher m = p.matcher(content);
		while (m.find()) {
			String group = m.group();
			List<String> imgSrcList = getImgSrc(group);
			if(imgSrcList.size()>0) {
			String imgSrc=imgSrcList.get(0);
			content = content.replace(group, "\r\n[img:" + imgSrc + "]\r\n");
			}
		}
		return content;
	}

	
	public static void main(String[] args) {

		 String stripHtml = delHTMLTag("<p>这得根据“学霸”这个概念来说的。</p><p>人的能力，不仅仅局限于学习能力。肢体协调、力量、反应都是。</p><p>而“学霸”的定义，是超越“读书机器”，而是在 记忆、逻辑、创造等一系列方面中都有上佳表现的存在。</p><p>那么，在游戏中，根据不同的情况，学霸的表现也可能更强。</p><p>比如在一款战略类游戏中，学霸会更快的掌握游戏的含义（比如星际，最本质的概念，就是把采到的资源转换成战力拆了对方的基地）。但在反应速度、应激行为等，仍然需要操练。</p><p>在更简单的策略中（比如剪刀石头布），玩家会很快到达游戏的瓶颈期。</p><p>因而，学霸会更快的上手相当部分游戏。但在相当部分下，并不比普通人强太多（因为经由训练可以达到同等的高度，同理那是游戏本身的高度）。</p><p>毕竟，围棋象棋这些，本质上，也是游戏。</p>");
		 System.out.println(stripHtml);
	}
}
