package casia.isiteam.zhihu_event.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import casia.isiteam.zhihu_event.model.EventInfo;
import casia.isiteam.zhihu_event.model.EventKeywords;
import casia.isiteam.zhihu_event.util.JdbcUtil;


/**
 * 查询数据库
 * @author wd
 *
 */
public class ReadSite {

	private static Logger logger = Logger.getLogger(ReadSite.class);

	/**
	 * 查询事件
	 * @return
	 */
	public ArrayList<EventInfo> getEvent() {
		//String sql = "select * from news_schema where host_name='阿波罗新闻网' or host_name='越南共和国中央政府门户网站' or host_name='华夏文摘' or host_name='万维读者网' or host='www.bannedbook.org';";
		String sql = "select * from event_info where start_time < NOW() and end_time > NOW() and `status`=1";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<EventInfo> list = new ArrayList<EventInfo>();
		try {
			conn = JdbcUtil.getConn(SystemConstant.db_data);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				EventInfo eventInfo = new EventInfo();
				eventInfo.setEid(rs.getInt("eid"));
				eventInfo.setName(rs.getString("name"));
				eventInfo.setStart_time(rs.getTimestamp("start_time"));
				eventInfo.setEnd_time(rs.getTimestamp("end_time"));
				list.add(eventInfo);
			}
		} catch (SQLException e) {
			logger.error("从数据库中获取site对象异常\n堆栈信息：" + e);
		} finally {
			CloseHandlder.close(rs, ps, conn);
		}
		return list;
	}
	
	/**
	 * 查询事件
	 * @return
	 */
	public EventInfo getEventByEid(int eid) {
		//String sql = "select * from news_schema where host_name='阿波罗新闻网' or host_name='越南共和国中央政府门户网站' or host_name='华夏文摘' or host_name='万维读者网' or host='www.bannedbook.org';";
		String sql = "select * from event_info where eid="+eid+"";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		EventInfo eventInfo=null;
		try {
			conn = JdbcUtil.getConn(SystemConstant.db_data);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				eventInfo = new EventInfo();
				eventInfo.setEid(rs.getInt("eid"));
				eventInfo.setName(rs.getString("name"));
				eventInfo.setStart_time(rs.getTimestamp("start_time"));
				eventInfo.setEnd_time(rs.getTimestamp("end_time"));
				eventInfo.setStatus(rs.getInt("status"));
			}
		} catch (SQLException e) {
			logger.error("从数据库中获取site对象异常\n堆栈信息：" + e);
		} finally {
			CloseHandlder.close(rs, ps, conn);
		}
		return eventInfo;
	}
	
	/**
	 * 根据id查询关键词
	 * @return
	 */
	public ArrayList<EventKeywords> getKeywordsByEid(int eid) {
		//String sql = "select * from news_schema where host_name='阿波罗新闻网' or host_name='越南共和国中央政府门户网站' or host_name='华夏文摘' or host_name='万维读者网' or host='www.bannedbook.org';";
		String sql = "select * from event_keywords where eid="+eid+" and deleted =0";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<EventKeywords> list = new ArrayList<EventKeywords>();
		try {
			conn = JdbcUtil.getConn(SystemConstant.db_data);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				EventKeywords eventKeywords = new EventKeywords();
				eventKeywords.setKid(rs.getInt("kid"));
				eventKeywords.setEid(rs.getInt("eid"));
				eventKeywords.setKeywords(rs.getString("keywords"));
				eventKeywords.setDeleted(rs.getInt("deleted"));
				list.add(eventKeywords);
			}
		} catch (SQLException e) {
			logger.error("从数据库中获取site对象异常\n堆栈信息：" + e);
		} finally {
			CloseHandlder.close(rs, ps, conn);
		}
		return list;
	}
	
	/**
	 * 根据id查询关键词
	 * @return
	 */
	public EventKeywords getKeywordsByKid(int kid) {
		//String sql = "select * from news_schema where host_name='阿波罗新闻网' or host_name='越南共和国中央政府门户网站' or host_name='华夏文摘' or host_name='万维读者网' or host='www.bannedbook.org';";
		String sql = "select * from event_keywords where kid="+kid+"";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		EventKeywords eventKeywords = null;
		try {
			conn = JdbcUtil.getConn(SystemConstant.db_data);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				eventKeywords=new EventKeywords();
				eventKeywords.setKid(rs.getInt("kid"));
				eventKeywords.setEid(rs.getInt("eid"));
				eventKeywords.setKeywords(rs.getString("keywords"));
				eventKeywords.setDeleted(rs.getInt("deleted"));
			}
		} catch (SQLException e) {
			logger.error("从数据库中获取site对象异常\n堆栈信息：" + e);
		} finally {
			CloseHandlder.close(rs, ps, conn);
		}
		return eventKeywords;
	}
	
	
	/**
	 * 查询关键词
	 * @return
	 */
	public ArrayList<EventKeywords> getKeywords() {
		//String sql = "select * from news_schema where host_name='阿波罗新闻网' or host_name='越南共和国中央政府门户网站' or host_name='华夏文摘' or host_name='万维读者网' or host='www.bannedbook.org';";
		String sql = "select k.*,i.start_time,i.end_time from event_keywords k,event_info i where i.start_time < NOW() and i.end_time > NOW() and i.`status`=1 and i.eid=k.eid and k.deleted=0;";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<EventKeywords> list = new ArrayList<EventKeywords>();
		try {
			conn = JdbcUtil.getConn(SystemConstant.db_data);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				EventKeywords eventKeywords = new EventKeywords();
				eventKeywords.setKid(rs.getInt("kid"));
				eventKeywords.setEid(rs.getInt("eid"));
				eventKeywords.setKeywords(rs.getString("keywords"));
				eventKeywords.setDeleted(rs.getInt("deleted"));
				eventKeywords.setStart_time(rs.getTimestamp("start_time"));
				eventKeywords.setEnd_time(rs.getTimestamp("end_time"));
				list.add(eventKeywords);
			}
		} catch (SQLException e) {
			logger.error("从数据库中获取site对象异常\n堆栈信息：" + e);
		} finally {
			CloseHandlder.close(rs, ps, conn);
		}
		return list;
	}
	
}
