package casia.isiteam.zhihu_event.dao;

import java.io.Closeable;
import java.io.IOException;
import java.sql.*;

/**
 * 关闭数据库Connection,Statement,ResultSet
 * @author dell
 *
 */
public class CloseHandlder {
    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                closeable = null;
            }
        }
    }
    
    /**
     * 关闭数据库Connection,Statement,ResultSet
     * @param rs
     * @param st
     * @param conn
     */
    public static void close(ResultSet rs, Statement st, Connection conn) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            rs = null;
        }
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            st = null;
        }
        if (conn != null) {
            try {
                boolean autoCommit = conn.getAutoCommit();
                if (!autoCommit) {
                    conn.commit();
                }
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            conn = null;
        }
    }
    
    /**
     * 关闭数据库Connection,PreparedStatement,ResultSet
     * @param rs
     * @param ps
     * @param conn
     */
    public static void close(ResultSet rs, PreparedStatement ps, Connection conn) {
    	if (rs != null) {
    		try {
    			rs.close();
    		} catch (SQLException e) {
    			e.printStackTrace();
    		}
    		rs = null;
    	}
    	if (ps != null) {
    		try {
    			ps.close();
    		} catch (SQLException e) {
    			e.printStackTrace();
    		}
    		ps = null;
    	}
    	if (conn != null) {
    		try {
    			boolean autoCommit = conn.getAutoCommit();
    			if (!autoCommit) {
    				conn.commit();
    			}
    			conn.close();
    		} catch (SQLException e) {
    			e.printStackTrace();
    		}
    		conn = null;
    	}
    }
}
