package casia.isiteam.zhihu_event.model;

import java.util.Date;

public class EventKeywords {

	private int kid;
	private int eid;
	private String keywords;
	private int deleted;
	private String not_keywords;
	private Date start_time;	//关注开始时间
	private Date end_time;		//关注开始时间
	
	public Date getStart_time() {
		return start_time;
	}
	public void setStart_time(Date start_time) {
		this.start_time = start_time;
	}
	public Date getEnd_time() {
		return end_time;
	}
	public void setEnd_time(Date end_time) {
		this.end_time = end_time;
	}
	public int getKid() {
		return kid;
	}
	public void setKid(int kid) {
		this.kid = kid;
	}
	public int getEid() {
		return eid;
	}
	public void setEid(int eid) {
		this.eid = eid;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public int getDeleted() {
		return deleted;
	}
	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}
	public String getNot_keywords() {
		return not_keywords;
	}
	public void setNot_keywords(String not_keywords) {
		this.not_keywords = not_keywords;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + deleted;
		result = prime * result + eid;
		result = prime * result + ((keywords == null) ? 0 : keywords.hashCode());
		result = prime * result + kid;
		result = prime * result + ((not_keywords == null) ? 0 : not_keywords.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EventKeywords other = (EventKeywords) obj;
		if (deleted != other.deleted)
			return false;
		if (eid != other.eid)
			return false;
		if (keywords == null) {
			if (other.keywords != null)
				return false;
		} else if (!keywords.equals(other.keywords))
			return false;
		if (kid != other.kid)
			return false;
		if (not_keywords == null) {
			if (other.not_keywords != null)
				return false;
		} else if (!not_keywords.equals(other.not_keywords))
			return false;
		return true;
	}
	
}
