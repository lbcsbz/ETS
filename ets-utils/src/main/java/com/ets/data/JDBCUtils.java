package com.ets.data;


import org.apache.commons.dbcp2.BasicDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCUtils {
    private static DataSource dataSource;

    static {
        try {
            InputStream is = JDBCUtils.class.getClassLoader().getResourceAsStream("dbcpconfig.properties");
            Properties properties = new Properties();
            properties.load(is);
            dataSource = BasicDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static DataSource getDataSource() {
        return dataSource;
    }

    public static Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public static void release(ResultSet rs, PreparedStatement pstmt, Connection conn){
        try {
            if(rs!=null) rs.close();
            if(pstmt!=null) pstmt.close();
            if(conn!=null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
