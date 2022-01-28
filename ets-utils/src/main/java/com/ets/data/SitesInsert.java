package com.ets.data;

import com.ets.pojo.Sites;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class SitesInsert {
    public static void insertSitesData(ArrayList<Sites> list) {
        Connection connection = JDBCUtils.getConnection();
        PreparedStatement pstmt = null;
        try {
            for (Sites site : list) {
                String sql = "insert into sites(id,name,type,level,parent_id,created,updated,site_id) values(?,?,?,?,?,?,?,?)";
                pstmt = connection.prepareStatement(sql);
                pstmt.setInt(1,site.getId());
                pstmt.setString(2, site.getName());
                pstmt.setString(3, site.getType());
                pstmt.setInt(4, site.getLevel());
                pstmt.setString(5, site.getParent_id());
                pstmt.setDate(6, site.getCreated());
                pstmt.setDate(7, site.getUpdated());
                pstmt.setString(8, site.getSite_id());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            //注意要释放资源
            JDBCUtils.release(null,null,connection);
        }
    }

}
