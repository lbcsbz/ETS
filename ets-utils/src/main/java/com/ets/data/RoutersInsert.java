package com.ets.data;

import com.ets.pojo.Routers;
import com.ets.pojo.Sites;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class RoutersInsert {
    public static void insertRoutersData(ArrayList<Routers> list) {
        Connection connection = JDBCUtils.getConnection();
        PreparedStatement pstmt = null;
        try {
            for (Routers router : list) {
                String sql = "insert into routers(id,name,router_id,origin_site,origin_site_id,target_site,target_site_id,plan_origin_time,plan_target_time,created,updated) values(?,?,?,?,?,?,?,?,?,?,?)";
                pstmt = connection.prepareStatement(sql);
                pstmt.setInt(1,router.getId());
                pstmt.setString(2, router.getName());
                pstmt.setString(3, router.getRouter_id());
                pstmt.setString(4, router.getOrigin_site());
                pstmt.setString(5, router.getOrigin_site_id());
                pstmt.setString(6, router.getTarget_site());
                pstmt.setString(7, router.getTarget_site_id());
                pstmt.setString(8, router.getPlan_origin_time());
                pstmt.setString(9, router.getPlan_target_time());
                pstmt.setDate(10, router.getCreated());
                pstmt.setDate(11, router.getUpdated());
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
