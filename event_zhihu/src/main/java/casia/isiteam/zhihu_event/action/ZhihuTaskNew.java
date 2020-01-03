package casia.isiteam.zhihu_event.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import org.apache.log4j.Logger;
import casia.isiteam.zhihu_event.dao.CrawlerDao;
import casia.isiteam.zhihu_event.dao.ReadSite;
import casia.isiteam.zhihu_event.model.EventInfo;
import casia.isiteam.zhihu_event.model.EventKeywords;
import casia.isiteam.zhihu_event.model.Forum;
import casia.isiteam.zhihu_event.model.UrlType;
import casia.isiteam.zhihu_event.model.Users;
import casia.isiteam.zhihu_event.model.ZhihuListStatus;
import casia.isiteam.zhihu_event.service.ParseAnswer;
import casia.isiteam.zhihu_event.service.ParseArticle;
import casia.isiteam.zhihu_event.service.ParseAuthor;
import casia.isiteam.zhihu_event.service.ParseQuestion;
import casia.isiteam.zhihu_event.service.ParseZhihuList;
import casia.isiteam.zhihu_event.util.MD5Util;

public class ZhihuTaskNew implements Runnable {

	private EventKeywords eventKeywords = null;
	private Random r = new Random();
	private CrawlerDao crawlerDao = new CrawlerDao();
	private ReadSite readSite = new ReadSite();
	private ParseAuthor author = new ParseAuthor();					//解析作者
	private ParseZhihuList zhihuList=new ParseZhihuList();		//解析列表
	private ParseQuestion question=new ParseQuestion();			//解析问题
	private ParseAnswer answer=new ParseAnswer();				//解析答案
	private ParseArticle  article=new ParseArticle();			//解析文章
	private static Logger logger = Logger.getLogger(ZhihuTaskNew.class);
	public Date startTime = null;

	public ZhihuTaskNew(EventKeywords eventKeywords) {
		this.eventKeywords = eventKeywords;
	}

	public ZhihuTaskNew() {
	}

	public void run() {

		logger.info("知乎" + "\t最新数据\t采集开始\t关键词:" + eventKeywords.getKeywords());
		try {
			int i = 1;
			while (true) {
//				String url = hostRule.getChannelUrl()
//						.replace("@@@@", convertUrl(eventKeywords.getKeywords(), hostRule.getCharset()))
//						.replace("####", i + "");
				
				String url="https://www.zhihu.com/api/v4/search_v3?t=general&q="+convertUrl(eventKeywords.getKeywords(), "UTF-8")+"&correction=1&offset="+(i-1)*20+"&limit=20&show_all_topics=0&time_zone=a_day";
				
				//解析列表URL
				ZhihuListStatus listStatus = zhihuList.getQuestionsList(url);
				ArrayList<UrlType> urlList = listStatus.getUrlList();
				ArrayList<Forum> forumList=new ArrayList<Forum>();
				int success = 0;
				int existCount = 0;
				//遍历列表url
				if (urlList != null && urlList.size() != 0) {
					for (UrlType urltype : urlList) {
						//如果是问答类型
						if(urltype.getType()==1) {
							//判断是否存在
							String link="https://www.zhihu.com/question/"+urltype.getId();
							Forum f = crawlerDao.existForum(MD5Util.md5(link));
							if (f != null) {
								existCount++;
								continue;
							}
							//获取问题
							Forum forum=question.getContent(urltype.getId());
							//获取答案
							ArrayList<Forum> onePageList = answer.getAnswersOnePage(urltype.getId());
							
							if(forum!=null&&onePageList!=null) {
								forumList.add(forum);
								forumList.addAll(onePageList);
							}
						}
						//如果是文章类型
						else if(urltype.getType()==2) {
							//判断是否存在
							String link="https://zhuanlan.zhihu.com/p/"+urltype.getId();
							Forum f = crawlerDao.existForum(MD5Util.md5(link));
							if (f != null) {
								existCount++;
								continue;
							}
							//获取文章
							Forum forum = article.getArticle(urltype.getId());
							if(forum!=null) {
								forumList.add(forum);
							}
						}
					}
					
					//遍历内容集合
					for (Forum forum : forumList) {
						
						if (forum != null) {
							// 处理作者
							Users users = author.getAuthor(forum);
							// 此处判断一下是为了避免个别帖子的作者为匿名的，然后就会导致获取不到作者链接，从而导致入库的时候出错
							if (users != null) {
								crawlerDao.addUsers(users); // 入库
								users = crawlerDao.getUsersId(users);
								forum.setFuid(users.getFuid());
								// 入库有作者的
								if (crawlerDao.addForum(forum,eventKeywords) == 1) {
									success++;
								}
							} else {
								// 入库没uid的
								if (crawlerDao.addForum2(forum,eventKeywords) == 1) {
									success++;
								}
							}
						}
					}
						
				} else {
					// 结束采集
					break;
				}
				logger.info("知乎" + "\t最新数据\t第" + i + "页\t关键词: " + eventKeywords.getKeywords()
						+ "\t共有 " + urlList.size() + "\t实采 " + success + "\t存在 " + existCount);

				if (!listStatus.isIs_end()) {
					i++;
				} else {
					break;
				}
				Thread.sleep(r.nextInt(2000) + 2000);
			}
			logger.info("知乎" + "\t最新数据\t结束采集\t关键词: " + eventKeywords.getKeywords());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean judgeKeywords(EventKeywords eventKeywords) {

		try {
			// 如果关键词状态为1,或者关键词变更 -结束采集
			EventKeywords keywords = readSite.getKeywordsByKid(eventKeywords.getKid());
			if (keywords.getDeleted() == 1 || !eventKeywords.getKeywords().equals(keywords.getKeywords())) {
				return false;
			}
			EventInfo eventInfo = readSite.getEventByEid(eventKeywords.getEid());
			Date now = new Date();
			// 如果开始时间>当前时间
			// 结束时间<当前时间
			// 关键词的状态为0 结束采集
			if (eventInfo.getStart_time().after(now) || eventInfo.getEnd_time().before(now)
					|| eventInfo.getStatus() != 1) {
				return false;
			}
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}

	private String convertUrl(String keywords, String charset) {
		try {
			String encode = java.net.URLEncoder.encode(keywords, charset);
			return encode;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static Date getDate() {
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR_OF_DAY, -12);
		date = calendar.getTime();
		return date;
	}
}
