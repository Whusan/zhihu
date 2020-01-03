package casia.isiteam.zhihu_event.action;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import casia.isiteam.zhihu_event.dao.ReadSite;
import casia.isiteam.zhihu_event.model.EventKeywords;

public class MainTask {

	private static ReadSite readSite = new ReadSite();
	private static Logger logger = Logger.getLogger(MainTask.class);
	
	public static void main(String[] args) {
		
		
		PropertyConfigurator.configureAndWatch("conf/log4j.properties");
		ArrayList<EventKeywords> hostList = readSite.getKeywords();
		new Thread(new CheckTask(hostList)).start();
		while (true) {
			try {
				ExecutorService executorService = Executors.newFixedThreadPool(30);
				ArrayList<EventKeywords> hostList1 = readSite.getKeywords();

				for (EventKeywords eventKeywords : hostList1) {
						executorService.execute(new ZhihuTaskNew(eventKeywords));
						executorService.execute(new ZhihuTaskOld(eventKeywords));
				}
				executorService.shutdown();
				executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
				logger.info("知乎一轮采集完成--------休息20分钟继续");
				Thread.sleep(1000 * 60 * 20);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
