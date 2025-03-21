package org.librarymanagment.database;

import com.mysql.jdbc.Driver;
import java.util.Properties;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.*;

public class DataBase {
    // 连接数据库URL格式为：jdbc协议:数据库子协议:主机:端口/连接的数据库名
    // 和HTTP协议类似
    private static String url = "jdbc:mysql://localhost:3306/library_management";
    private static String user = "root";// 用户名
    private static String password = "root";// 密码

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //第一种方法：使用驱动程序去连接
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public static void closeResources(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if(rs != null) rs.close();
            if(stmt != null) stmt.close();
            if(conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
