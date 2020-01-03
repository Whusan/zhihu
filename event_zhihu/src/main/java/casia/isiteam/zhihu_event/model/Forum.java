package casia.isiteam.zhihu_event.model;

import java.sql.Timestamp;
import java.util.Date;

/**
 * 解析到的内容实体类
 * @author wd
 *
 */
public class Forum {

	private long id;				//内容ID
    private String title;			//标题
    private String content;			//正文
    private int fuid;				//对应此作者在作者表的ID
    private String author;			//作者
    private String authorUrl;		//作者的URL
	private Date pubtime;			//发布时时间
    private String pic;				//图片链接
    private int reviewcount;		//浏览量
    private int replycount;			//回复量
    private String lastreplyauthor; //最后回复的用户
    private Date lastreplytime;		//最后回复的时间
    private int fbid;				//对应频道ID
    private int genus;				//种类
    private String site;			//站点site
    private String url;				//帖子的URL
    private Date intime;			//插入时间
    private String urlMd5;			//URL的MD5值
    private String keywords;		//关键词
    private int sengineid;
    private int keywordsId;			//关键词ID
    private Timestamp modifytime;	//修改时间
    private int status;				//状态
    private String snapshot;		//快照		
    private String ip;				//IP
    private int forumTotal;			
    private int pictureTotal;
    private int fhid;				//对应host表的ID
    private String sourceId;		
    private int nationCategory;
    private int sourceType;
    private String channelName;		//频道名称
    private String channelUrl;		//频道URL

    
    @Override
	public String toString() {
		return "Forum [id=" + id + ", title=" + title + ", content=" + content + ", fuid=" + fuid + ", author=" + author
				+ ", authorUrl=" + authorUrl + ", pubtime=" + pubtime + ", pic=" + pic + ", reviewcount=" + reviewcount
				+ ", replycount=" + replycount + ", lastreplyauthor=" + lastreplyauthor + ", lastreplytime="
				+ lastreplytime + ", fbid=" + fbid + ", genus=" + genus + ", site=" + site + ", url=" + url
				+ ", intime=" + intime + ", urlMd5=" + urlMd5 + ", keywords=" + keywords + ", sengineid=" + sengineid
				+ ", keywordsId=" + keywordsId + ", modifytime=" + modifytime + ", status=" + status + ", snapshot="
				+ snapshot + ", ip=" + ip + ", forumTotal=" + forumTotal + ", pictureTotal=" + pictureTotal + ", fhid="
				+ fhid + ", sourceId=" + sourceId + ", nationCategory=" + nationCategory + ", sourceType=" + sourceType
				+ ", channelName=" + channelName + ", channelUrl=" + channelUrl + "]";
	}

	public String getAuthorUrl() {
		return authorUrl;
	}

	public void setAuthorUrl(String authorUrl) {
		this.authorUrl = authorUrl;
	}

    
    public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getChannelUrl() {
		return channelUrl;
	}

	public void setChannelUrl(String channelUrl) {
		this.channelUrl = channelUrl;
	}

	public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getFuid() {
        return fuid;
    }

    public void setFuid(int fuid) {
        this.fuid = fuid;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getPubtime() {
        return pubtime;
    }

    public void setPubtime(Date pubtime) {
        this.pubtime = pubtime;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getReviewcount() {
        return reviewcount;
    }

    public void setReviewcount(int reviewcount) {
        this.reviewcount = reviewcount;
    }

    public int getReplycount() {
        return replycount;
    }

    public void setReplycount(int replycount) {
        this.replycount = replycount;
    }

    public String getLastreplyauthor() {
        return lastreplyauthor;
    }

    public void setLastreplyauthor(String lastreplyauthor) {
        this.lastreplyauthor = lastreplyauthor;
    }

    public Date getLastreplytime() {
        return lastreplytime;
    }

    public void setLastreplytime(Date lastreplytime) {
        this.lastreplytime = lastreplytime;
    }

    public int getFbid() {
        return fbid;
    }

    public void setFbid(int fbid) {
        this.fbid = fbid;
    }

    public int getGenus() {
        return genus;
    }

    public void setGenus(int genus) {
        this.genus = genus;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getIntime() {
        return intime;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }

    public String getUrlMd5() {
        return urlMd5;
    }

    public void setUrlMd5(String urlMd5) {
        this.urlMd5 = urlMd5;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public int getSengineid() {
        return sengineid;
    }

    public void setSengineid(int sengineid) {
        this.sengineid = sengineid;
    }

    public int getKeywordsId() {
        return keywordsId;
    }

    public void setKeywordsId(int keywordsId) {
        this.keywordsId = keywordsId;
    }

    public Timestamp getModifytime() {
        return modifytime;
    }

    public void setModifytime(Timestamp modifytime) {
        this.modifytime = modifytime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(String snapshot) {
        this.snapshot = snapshot;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getForumTotal() {
        return forumTotal;
    }

    public void setForumTotal(int forumTotal) {
        this.forumTotal = forumTotal;
    }

    public int getPictureTotal() {
        return pictureTotal;
    }

    public void setPictureTotal(int pictureTotal) {
        this.pictureTotal = pictureTotal;
    }

    public int getFhid() {
        return fhid;
    }

    public void setFhid(int fhid) {
        this.fhid = fhid;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public int getNationCategory() {
        return nationCategory;
    }

    public void setNationCategory(int nationCategory) {
        this.nationCategory = nationCategory;
    }

    public int getSourceType() {
        return sourceType;
    }

    public void setSourceType(int sourceType) {
        this.sourceType = sourceType;
    }
}
