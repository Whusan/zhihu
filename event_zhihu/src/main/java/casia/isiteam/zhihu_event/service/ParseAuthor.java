package casia.isiteam.zhihu_event.service;

import java.sql.Timestamp;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import casia.isiteam.zhihu_event.model.Forum;
import casia.isiteam.zhihu_event.model.Users;
import casia.isiteam.zhihu_event.util.MD5Util;


/**
 * 处理作者实体类
 * @author wd
 * @version jdk 1.7
 * @date 2018年7月20日
 */
public class ParseAuthor {

	public Users getAuthor(Forum forum) {
		Users users=new Users();
		
		//获取fhid
		users.setFhid(2);
		
		//获取作者
		String username=forum.getAuthor();
		if (StringUtils.isNotBlank(username)) {
			users.setUsername(username.trim());
		} else {
			return null;
		}
		
		//获取作者链接
		String author_url=forum.getAuthorUrl();
		if (StringUtils.isNotBlank(author_url)) {
			users.setAuthor_url(author_url.trim());
		} else {
			return null;
		}
		
		//urlmd5
		users.setUrlmd5(MD5Util.md5(author_url));
		
		//inserttime
		Date date = new Date();  
		Timestamp timestamp = new Timestamp(date.getTime());
		users.setInserttime(timestamp);
		
		return users;
	}
}
