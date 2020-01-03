package casia.isiteam.zhihu_event.model;

import java.util.Date;

public class EventInfo {

	private int eid;			//事件Id
	private String name;		//事件名称
	private Date start_time;	//关注开始时间
	private Date end_time;		//关注开始时间
	private int language;		//'语种：0-中英；1-泰；2-越；3-老挝'
	private int category;		//专题分类
	private int location;		//事件爆发地
	private String description;	//摘要
	private String pic;			//图片URL
	private int pub_level;		//公开级别：0-仅自己，1-部门，2-单位
	private int type;			//类型：0-已发生，1-预埋
	private int site_type;		//预警分类：0-站内弹窗，1-短信
	private int status;			//站点选择：0-全网数据，1-特定站点
	private int dep_id;			//状态：1-正常，0-删除
	private int user_id;
	private Date create_time;
	private Date last_modify;
	private String keywords;
	private int support_level;
	private int hot_sensitive;
	private int is_cyc;
	private String dt_eid;
	private int dt_userid;
	private int city_level;
	private int city_area;
	private int damage_level;
	public int getEid() {
		return eid;
	}
	public void setEid(int eid) {
		this.eid = eid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
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
	public int getLanguage() {
		return language;
	}
	public void setLanguage(int language) {
		this.language = language;
	}
	public int getCategory() {
		return category;
	}
	public void setCategory(int category) {
		this.category = category;
	}
	public int getLocation() {
		return location;
	}
	public void setLocation(int location) {
		this.location = location;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public int getPub_level() {
		return pub_level;
	}
	public void setPub_level(int pub_level) {
		this.pub_level = pub_level;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getSite_type() {
		return site_type;
	}
	public void setSite_type(int site_type) {
		this.site_type = site_type;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getDep_id() {
		return dep_id;
	}
	public void setDep_id(int dep_id) {
		this.dep_id = dep_id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public Date getLast_modify() {
		return last_modify;
	}
	public void setLast_modify(Date last_modify) {
		this.last_modify = last_modify;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public int getSupport_level() {
		return support_level;
	}
	public void setSupport_level(int support_level) {
		this.support_level = support_level;
	}
	public int getHot_sensitive() {
		return hot_sensitive;
	}
	public void setHot_sensitive(int hot_sensitive) {
		this.hot_sensitive = hot_sensitive;
	}
	public int getIs_cyc() {
		return is_cyc;
	}
	public void setIs_cyc(int is_cyc) {
		this.is_cyc = is_cyc;
	}
	public String getDt_eid() {
		return dt_eid;
	}
	public void setDt_eid(String dt_eid) {
		this.dt_eid = dt_eid;
	}
	public int getDt_userid() {
		return dt_userid;
	}
	public void setDt_userid(int dt_userid) {
		this.dt_userid = dt_userid;
	}
	public int getCity_level() {
		return city_level;
	}
	public void setCity_level(int city_level) {
		this.city_level = city_level;
	}
	public int getCity_area() {
		return city_area;
	}
	public void setCity_area(int city_area) {
		this.city_area = city_area;
	}
	public int getDamage_level() {
		return damage_level;
	}
	public void setDamage_level(int damage_level) {
		this.damage_level = damage_level;
	}
	
	
	
	
	
	
}
