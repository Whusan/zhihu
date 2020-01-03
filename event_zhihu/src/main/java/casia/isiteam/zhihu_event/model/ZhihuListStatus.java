package casia.isiteam.zhihu_event.model;

import java.util.ArrayList;

public class ZhihuListStatus {

	private ArrayList<UrlType> urlList;
	private boolean is_end;
	public ArrayList<UrlType> getUrlList() {
		return urlList;
	}
	public void setUrlList(ArrayList<UrlType> urlList) {
		this.urlList = urlList;
	}
	public boolean isIs_end() {
		return is_end;
	}
	public void setIs_end(boolean is_end) {
		this.is_end = is_end;
	}
}
