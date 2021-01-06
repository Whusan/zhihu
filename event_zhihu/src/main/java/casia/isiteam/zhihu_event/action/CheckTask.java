package casia.isiteam.zhihu_event.action;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import casia.isiteam.zhihu_event.dao.ReadSite;
import casia.isiteam.zhihu_event.model.EventKeywords;

public class CheckTask implements Runnable {

	ArrayList<EventKeywords> hostList = new ArrayList<EventKeywords>();
	private int num=0;
	CheckTask(ArrayList<EventKeywords> hostList) {
		this.hostList = hostList;
	}

	CheckTask() {
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		ReadSite readSite = new ReadSite();
		ExecutorService executorService = Executors.newFixedThreadPool(50);
		while (true) {
			try {
				// 对库中正常的站点进行统计
				ArrayList<EventKeywords> normalList = readSite.getKeywords();
				// 把得到的集合与已经开始采集的集合求差集，得到新增的站点
				normalList.removeAll(hostList);

				// 如何对新增站点进行遍历
				for (EventKeywords eventKeywords : normalList) {
						// 获取新增站点的匹配规则
						hostList.add(eventKeywords);
						executorService.execute(new ZhihuTaskNew(eventKeywords));
						executorService.execute(new ZhihuTaskOld(eventKeywords));
				}
				Thread.sleep(1000 * 60 * 1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
