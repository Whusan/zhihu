package casia.isiteam.zhihu_event.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SystemConstant {

	public static int batchSize = 1000;

	public static final String FORUM = "forum_schema";
	public static final String NEWS = "news_schema";

	//server端启动的服务名
	public static String server_name_news="news_dist";
	public static String server_name_forum="forum_dist";

	
	//服务端存储的处理的最后一个id
	public static String last_IDFile_news="news.idcursor";
	public static String last_IDFile_forum="forum.idcursor";
	
	//客户端接收到ids后存储文件名
	public static String client_IDsFile_news="news_client_ids_file.ids";
	public static String client_IDsFile_forum="forum_client_ids_file.ids";

	

	public static String user_caiji = "user_caiji";
	//采集库
	public static String db_caiji = "db_caiji";
	public static String db_data = "db_data";

	//代理ip
	public static String proxy_ip=null;
	//代理端口
	public static int proxy_port = 1984;
	

	
	static{
		InputStream inStream = null;
		try {
//			inStream = new SystemConstant().getClass().getResourceAsStream("/conf"+ File.separator+"server.properties");
			inStream = new FileInputStream("conf/server.properties");
			Properties pro = new Properties();
			pro.load(inStream);

			proxy_ip = pro.getProperty("proxy_ip");
			proxy_port = Integer.parseInt(pro.getProperty("proxy_port").trim());
			
			
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				if (inStream != null)
					inStream.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}
	
	
}
