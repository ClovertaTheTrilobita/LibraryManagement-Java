package org.librarymanagment.database;

import com.mysql.jdbc.Driver;
import java.util.Properties;
import java.sql.Connection;
import java.sql.SQLException;

public class DataBase {
    // 连接数据库URL格式为：jdbc协议:数据库子协议:主机:端口/连接的数据库名
    // 和HTTP协议类似
    private static String url = "jdbc:mysql://localhost:3306/mydb";
    private static String user = "root";// 用户名
    private static String password = "root";// 密码

    //第一种方法：使用驱动程序去连接
    public static void main(String[] args) throws SQLException {
        //1.创建驱动程序类对象：
        //new不能是接口，而是实现类；实现类(要导包)在：com.mysql.jdbc.Driver里；
        Driver driver = new com.mysql.jdbc.Driver(); //新版本
        //Driver driver = new org.gjt.mm.mysql.Driver(); //旧版本

        //设置用户名和密码
        Properties props = new Properties();
        props.setProperty("user", user);
        props.setProperty("password", password);

        //2.连接数据库，返回连接对象
        Connection conn = driver.connect(url, props);
        System.out.println(conn);
        //输出：com.mysql.jdbc.JDBC4Connection@ba4d54，表明连接成功
    }
}
