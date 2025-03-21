package org.librarymanagment.database;

import com.mysql.jdbc.Driver;
import java.util.Properties;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.*;

import java.sql.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class DataBase {
    // 数据库连接配置
    private static final String URL = "jdbc:mysql://localhost:3306/library_managemtent?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

    static {
        try {
            // 自动加载驱动（JDBC 4.0+可以省略，但显式声明更可靠）
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC驱动未找到", e);
        }
    }

    // 获取数据库连接
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // 查询分页图书信息
    /**
     * @param pageNumber: (int)第几页
      * @return List<Book>
     * @author cloverta
     * @description 获取图书信息，传入值为第几页，返回值为一个由Book对象组成的List，一页十本书。
     * @date 2025/3/21 17:45
     */
    public List<Book> fetchBooks(int pageNumber) {
        List<Book> books = new ArrayList<>();
        // 参数校验
        if(pageNumber < 1) {
            throw new IllegalArgumentException("页码不能小于1");
        }

        // 计算偏移量（从0开始）
        int offset = (pageNumber - 1) * 10;
        String sql = "SELECT book_id, book_name, author, location, is_borrowed, storage_time "
                + "FROM book LIMIT 10 OFFSET ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 设置分页参数
            pstmt.setInt(1, offset);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Book book = new Book();
                    book.setId(rs.getInt("book_id"));
                    book.setName(rs.getString("book_name"));
                    book.setAuthor(rs.getString("author"));
                    book.setLocation(rs.getString("location"));
                    book.setIsBorrowed(rs.getInt("is_borrowed"));
                    book.setStorageTime(rs.getTimestamp("storage_time"));
                    books.add(book);
                }
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return books;
    }

    // 异常处理
    private void handleSQLException(SQLException e) {
        System.err.println("数据库操作异常：");
        System.err.println("错误代码: " + e.getErrorCode());
        System.err.println("SQL状态: " + e.getSQLState());
        System.err.println("错误信息: " + e.getMessage());
        e.printStackTrace();
    }

    // 建议添加获取总页数的方法
    public int getTotalPages() {
        String sql = "SELECT COUNT(*) FROM book";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if(rs.next()) {
                int total = rs.getInt(1);
                return (int) Math.ceil(total / 10.0);
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return 0;
    }

    // 使用示例
    public static void main(String[] args) throws SQLException {
        DataBase db = new DataBase();
    }
}
