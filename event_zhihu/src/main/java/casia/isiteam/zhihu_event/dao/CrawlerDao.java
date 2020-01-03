package casia.isiteam.zhihu_event.dao;


import org.apache.log4j.Logger;

import casia.isiteam.zhihu_event.model.EventKeywords;
import casia.isiteam.zhihu_event.model.Forum;
import casia.isiteam.zhihu_event.model.Users;
import casia.isiteam.zhihu_event.util.JdbcUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class CrawlerDao {
    private static Logger logger = Logger.getLogger(CrawlerDao.class);

    /**
     * 解析到的详情页内容插入到数据库
     * @param list 详情页的实体类集合
     * @return
     */
    public int addForum(ArrayList<Forum> list) {
        PreparedStatement ps;
        Connection conn = null;
        int[] result = null;
        String sql = "INSERT ignore INTO forum_threads (title, content, url, site, pubtime, genus, urlmd5, author, nation_category, source_type,reviewcount , replycount,fbid,intime) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,now());";
        try {
            conn = JdbcUtil.getConn(SystemConstant.db_caiji);
            ps = conn.prepareStatement(sql);
            conn.setAutoCommit(true);
            for (Forum forum : list) {
                ps.setString(1, forum.getTitle());
                ps.setString(2, forum.getContent());
                ps.setString(3, forum.getUrl());
                ps.setString(4, forum.getSite());
                if(forum.getPubtime()==null){
                    ps.setTimestamp(5, null);
                }else {
                    ps.setTimestamp(5, new Timestamp(forum.getPubtime().getTime()));
                }

                ps.setInt(6, forum.getGenus());
                ps.setString(7, forum.getUrlMd5());
                ps.setString(8, forum.getAuthor());
                ps.setInt(9, forum.getNationCategory());
                ps.setInt(10, forum.getSourceType());
                ps.setInt(11,forum.getReviewcount());
                ps.setInt(12,forum.getReplycount());
                ps.setInt(13,forum.getFbid());
                ps.addBatch();
            }
            result = ps.executeBatch();
        } catch (Exception e) {
            try {
                if (conn != null)
                    conn.rollback();
            } catch (SQLException e1) {
                logger.error("插入数据库发生异常\n堆栈信息：" + e);
            }
        } finally {
            CloseHandlder.close(null, null, conn);
        }
        if (result == null) {
            return 0;
        } else {
            int count = 0;
            for(int x: result){
                if(x==1){
                    count++;
                }
            }
            return count;
        }
    }
    
    /**
     * 解析到的详情页内容插入到数据库
     * @param forum  详情页的实体类
     * @return
     */
    public int addForum(Forum forum,EventKeywords eventKeywords) {
        PreparedStatement ps;
        Connection conn = null;
        int[] result = null;
        String sql = "INSERT ignore INTO forum_threads (title, content, url, site, pubtime, genus, urlmd5, author, nation_category, source_type,reviewcount , replycount,intime,fuid,keywords,keywords_id,lastreplyauthor,lastreplytime) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,now(),?,?,?,?,?);";
        try {
            conn = JdbcUtil.getConn(SystemConstant.db_caiji);
            ps = conn.prepareStatement(sql);
            conn.setAutoCommit(true);
            
                ps.setString(1, forum.getTitle());
                ps.setString(2, forum.getContent());
                ps.setString(3, forum.getUrl());
                ps.setString(4, forum.getSite());
                if(forum.getPubtime()==null){
                    ps.setTimestamp(5, null);
                }else {
                    ps.setTimestamp(5, new Timestamp(forum.getPubtime().getTime()));
                }

                ps.setInt(6, forum.getGenus());
                ps.setString(7, forum.getUrlMd5());
                ps.setString(8, forum.getAuthor());
                ps.setInt(9, forum.getNationCategory());
                ps.setInt(10, forum.getSourceType());
                ps.setInt(11,forum.getReviewcount());
                ps.setInt(12,forum.getReplycount());
                ps.setInt(13,forum.getFuid());
                ps.setString(14,eventKeywords.getKeywords());
                ps.setInt(15,eventKeywords.getKid());
                ps.setString(16, forum.getLastreplyauthor());
                if(forum.getLastreplytime()!=null) {
                ps.setTimestamp(17, new Timestamp(forum.getLastreplytime().getTime()));
                }else {
                ps.setTimestamp(17, null);
                }
                ps.addBatch();
            
            result = ps.executeBatch();
        } catch (Exception e) {
        	e.printStackTrace();
            try {
                if (conn != null)
                    conn.rollback();
            } catch (SQLException e1) {
                logger.error("插入数据库发生异常\n堆栈信息：" + e);
            }
        } finally {
            CloseHandlder.close(null, null, conn);
        }
        if (result == null) {
            return 0;
        } else {
            int count = 0;
            for(int x: result){
                if(x==1){
                    count++;
                }
            }
            return count;
        }
    }
    
	 public Forum existForum(String md5) {
		//String sql = "select * from news_schema where host_name='阿波罗新闻网' or host_name='越南共和国中央政府门户网站' or host_name='华夏文摘' or host_name='万维读者网' or host='www.bannedbook.org';";
       String sql = "select * from forum_threads where urlmd5='"+md5+"'";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Forum forum=null;
		try {
			conn = JdbcUtil.getConn(SystemConstant.db_caiji);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				forum=new Forum();
				forum.setTitle(rs.getString("title"));
				forum.setContent(rs.getString("content"));
			}

		} catch (SQLException e) {
			logger.error("从数据库中获取site对象异常\n堆栈信息：" + e);
		} finally {
			CloseHandlder.close(rs, ps, conn);
		}
		return forum;
	}
    
    /**
     * 解析到的详情页内容插入到数据库（此方法是作者为匿名的版本，即没有fuid）
     * @param forum
     * @return
     */
    public int addForum2(Forum forum,EventKeywords eventKeywords) {
        PreparedStatement ps;
        Connection conn = null;
        int[] result = null;
        String sql = "INSERT ignore INTO forum_threads (title, content, url, site, pubtime, genus, urlmd5, author, nation_category, source_type,reviewcount , replycount,fbid,intime,keywords,keywords_id) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,now(),?,?);";
        try {
            conn = JdbcUtil.getConn(SystemConstant.db_caiji);
            ps = conn.prepareStatement(sql);
            conn.setAutoCommit(true);
            
                ps.setString(1, forum.getTitle());
                ps.setString(2, forum.getContent());
                ps.setString(3, forum.getUrl());
                ps.setString(4, forum.getSite());
                if(forum.getPubtime()==null){
                    ps.setTimestamp(5, null);
                }else {
                    ps.setTimestamp(5, new Timestamp(forum.getPubtime().getTime()));
                }

                ps.setInt(6, forum.getGenus());
                ps.setString(7, forum.getUrlMd5());
                ps.setString(8, forum.getAuthor());
                ps.setInt(9, forum.getNationCategory());
                ps.setInt(10, forum.getSourceType());
                ps.setInt(11,forum.getReviewcount());
                ps.setInt(12,forum.getReplycount());
                ps.setInt(13,forum.getFbid());
                ps.setString(14, eventKeywords.getKeywords());
                ps.setInt(15,eventKeywords.getKid());
                ps.addBatch();
            
            result = ps.executeBatch();
        } catch (Exception e) {
        	e.printStackTrace();
            try {
                if (conn != null)
                    conn.rollback();
            } catch (SQLException e1) {
                logger.error("插入数据库发生异常\n堆栈信息：" + e);
            }
        } finally {
            CloseHandlder.close(null, null, conn);
        }
        if (result == null) {
            return 0;
        } else {
            int count = 0;
            for(int x: result){
                if(x==1){
                    count++;
                }
            }
            return count;
        }
    }
   
    /**
     * 把作者信息入库
     * @param users
     * @return
     */
    public int addUsers(Users users) {
        PreparedStatement ps;
        Connection conn = null;
        int[] result = null;
        String sql = "INSERT ignore INTO forum_users (fhid, username, indexurl, urlmd5, inserttime) VALUES (?,?,?,?,?);";
        try {
            conn = JdbcUtil.getConn(SystemConstant.user_caiji);
            ps = conn.prepareStatement(sql);
            	conn.setAutoCommit(true);
                ps.setLong(1, users.getFhid());
                ps.setString(2, users.getUsername());
                ps.setString(3, users.getAuthor_url());
                ps.setString(4, users.getUrlmd5());
                ps.setTimestamp(5, users.getInserttime());
                ps.addBatch();
                result = ps.executeBatch();
        } catch (Exception e) {
        	e.printStackTrace();
            try {
                if (conn != null)
                    conn.rollback();
            } catch (SQLException e1) {
                logger.error("插入数据库发生异常\n堆栈信息：" + e);
            }
        } finally {
            CloseHandlder.close(null, null, conn);
        } 
        if (result == null) {
            return 0;
        } else {
            int count = 0;
            for(int x: result){
                if(x==1){
                    count++;
                }
            }
            return count;
        }   
    }
    
    
//    /**
//     * 频道信息入库
//     * @param boards
//     * @return
//     */
//    public int addBoards(Boards boards) {
//        PreparedStatement ps;
//        Connection conn = null;
//        int[] result = null;
//        String sql = "INSERT ignore INTO forum_boards (board_name, board_url, board_name_md5, board_url_md5,site,host,insert_time,update_time) VALUES (?,?,?,?,?,?,?,?);";
//        try {
//            conn = JdbcUtil.getConn(SystemConstant.db_caiji);
//            ps = conn.prepareStatement(sql);
//            	conn.setAutoCommit(true);
//            	ps.setString(1, boards.getBoard_name());
//                ps.setString(2, boards.getBoard_url());
//                ps.setString(3, boards.getBoard_name_md5());
//                ps.setString(4, boards.getBoard_url_md5());
//                ps.setString(5, boards.getSite());
//                ps.setString(6, boards.getHost());
//                ps.setTimestamp(7, boards.getInserttime());
//                ps.setTimestamp(8, boards.getUpdate_time());
//                ps.addBatch();
//                
//                result = ps.executeBatch();
//        } catch (Exception e) {
//        	e.printStackTrace();
//            try {
//                if (conn != null)
//                    conn.rollback();
//            } catch (SQLException e1) {
//                logger.error("插入数据库发生异常\n堆栈信息：" + e);
//            }
//        } finally {
//            CloseHandlder.close(null, null, conn);
//        } 
//        if (result == null) {
//            return 0;
//        } else {
//            int count = 0;
//            for(int x: result){
//                if(x==1){
//                    count++;
//                }
//            }
//            return count;
//        }   
//    }
    
  
 
    /**
     * 根据md5查询指定用户的ID
     * @param users
     * @return
     */
    public Users getUsersId(Users users) {
    	String sql = "select fuid from forum_users where urlmd5='"+users.getUrlmd5()+"'";
    	Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JdbcUtil.getConn(SystemConstant.user_caiji);
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {                  
            	users.setFuid(rs.getInt("fuid"));
            }

        } catch (SQLException e) {
            logger.error("从数据库中获取待下载对象异常\n堆栈信息："+e);
        }finally {
            CloseHandlder.close(rs, ps, conn);
        }
		return users;   
    }
    
//    /**
//     * 根据md5查询指定频道的ID
//     * @param boards
//     * @return
//     */
//    public Boards getBoardsId(Boards boards) {
//    	String sql = "select auto_id from forum_boards where board_url_md5='"+boards.getBoard_url_md5()+"'";
//    	Connection conn = null;
//        PreparedStatement ps = null;
//        ResultSet rs = null;
//        try {
//            conn = JdbcUtil.getConn(SystemConstant.db_caiji);
//            ps = conn.prepareStatement(sql);
//            rs = ps.executeQuery();
//
//            while (rs.next()) {                  
//            	boards.setAuto_id(rs.getInt("auto_id"));
//            }
//
//        } catch (SQLException e) {
//            logger.error("从数据库中获取待下载对象异常\n堆栈信息："+e);
//        }finally {
//            CloseHandlder.close(rs, ps, conn);
//        }
//		return boards;   
//    }
    
    
    public static void main(String[] args) {
    	Users users = new Users();
		users.setFhid(1);
		users.setUsername("zhangsan");
		users.setAuthor_url("aaaaaaaaaaaa");
		users.setUrlmd5("95081bc1444cebe2151843ba0cb3a8fd");
		Date date = new Date();  
		Timestamp timestamp = new Timestamp(date.getTime());
		users.setInserttime(timestamp);
		CrawlerDao crawlerDao = new CrawlerDao();
		int addUsers = crawlerDao.addUsers(users);
		System.out.println(addUsers);
    	
	}
}
