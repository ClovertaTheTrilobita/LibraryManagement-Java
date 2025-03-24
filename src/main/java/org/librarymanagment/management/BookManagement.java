package org.librarymanagment.management;

import org.librarymanagment.database.DataBase;
import org.librarymanagment.database.Book;

import java.sql.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class BookManagement {
    // 添加图书
    public static boolean addBook(String bookName, String author, String location) {
        // 创建 Book 对象并填充数据
        Book book = new Book(
                0,                  // book_id 由数据库自增
                bookName,
                author,
                location,
                0,// is_borrowed 默认未借出
                new Timestamp(System.currentTimeMillis()),  // 当前时间戳
                null, // borrowedTime（未被借出）
                null  // returnTime（未被借出）
        );

        // 调用数据库操作类
        DataBase db = new DataBase();
        DataBase.BookDB bookDB = db.new BookDB();
        boolean success = bookDB.addBook(book);

        // 错误处理
        if (!success) {
            JOptionPane.showMessageDialog(null, "添加失败，请检查输入");
        }
        return success;
    }

    // 删除图书（需检查借阅状态）
    public static boolean deleteBook(int bookId) {
        // 检查是否被借出
        DataBase db = new DataBase();
        DataBase.BookDB bookDB = db.new BookDB();
        boolean success = bookDB.deleteBook(bookId);

        // 错误处理
        if (!success) {
            JOptionPane.showMessageDialog(null, "删除失败，请检查图书ID是否存在");
        } else {
            JOptionPane.showMessageDialog(null, "删除成功");
        }
        return success;
    }

    // 多条件查询图书
    public static List<String> searchBooks(String searchType, String keyword) {
        List<String> results = new ArrayList<>();
        String sql = "";

        switch(searchType) {
            case "id":
                sql = "SELECT * FROM book WHERE book_id = ?";
                break;
            case "name":
                sql = "SELECT * FROM book WHERE book_name LIKE ?";
                break;
            case "time":
                sql = "SELECT * FROM book WHERE DATE(storage_time) = ?";
                break;
        }

        try (Connection conn = DataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if(searchType.equals("id")) {
                pstmt.setInt(1, Integer.parseInt(keyword));
            } else {
                pstmt.setString(1, searchType.equals("name") ? "%"+keyword+"%" : keyword);
            }

            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                String status = rs.getInt("is_borrowed") == 0 ? "在馆" : "已借出";
                String info = String.format(
                        "ID: %d\n书名: %s\n作者: %s\n状态: %s\n位置: %s\n入库时间: %s",
                        rs.getInt("book_id"),
                        rs.getString("book_name"),
                        rs.getString("author"),
                        status,
                        rs.getString("location"),
                        rs.getTimestamp("storage_time")
                );
                results.add(info);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "查询失败: " + e.getMessage());
        }
        return results;
    }

    // 更新图书信息
    public static boolean updateBookInfo(int bookId, String name, String author,
                                         String location, int isBorrowed,
                                         Timestamp storageTime) {
        // 创建 Book 对象（仅填充需要更新的字段）
        Book book = new Book(
                bookId,       // 虽然 SQL 中不更新 book_id，但需要传递以避免构造函数缺失字段
                name,
                author,
                location,
                isBorrowed,
                storageTime,
                null,         // borrowedTime（未在更新中使用，设为 null）
                null          // returnTime（未在更新中使用，设为 null）
        );

        // 调用数据库操作类
        DataBase db = new DataBase();
        DataBase.BookDB bookDB = db.new BookDB();
        boolean success = bookDB.updateBook(bookId, book);

        // 错误处理
        if (!success) {
            JOptionPane.showMessageDialog(null, "更新失败，请检查书籍ID是否存在");
        } else {
            JOptionPane.showMessageDialog(null, "图书信息更新成功");
        }
        return success;
    }
}
}
