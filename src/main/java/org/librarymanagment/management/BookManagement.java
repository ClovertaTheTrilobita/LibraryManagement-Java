package org.librarymanagment.management;

import org.librarymanagment.database.DataBase;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class BookManagement {
    // 添加图书
    public static boolean addBook(String bookName, String author, String location) {
        String sql = "INSERT INTO book(book_name, author, location, is_borrowed, storage_time) VALUES(?, ?, ?, 0, NOW())";
        try (Connection conn = DataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, bookName);
            pstmt.setString(2, author);
            pstmt.setString(3, location);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "添加失败: " + e.getMessage());
            return false;
        }
    }

    // 删除图书（需检查借阅状态）
    public static boolean deleteBook(int bookId) {
        // 检查是否被借出
        String checkSql = "SELECT is_borrowed FROM book WHERE book_id = ?";
        String deleteSql = "DELETE FROM book WHERE book_id = ?";

        try (Connection conn = DataBase.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql);
             PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {

            checkStmt.setInt(1, bookId);
            ResultSet rs = checkStmt.executeQuery();

            if(rs.next() && rs.getInt("is_borrowed") == 1) {
                JOptionPane.showMessageDialog(null, "图书已被借出，无法删除");
                return false;
            }

            deleteStmt.setInt(1, bookId);
            return deleteStmt.executeUpdate() > 0;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "删除失败: " + e.getMessage());
            return false;
        }
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
    public static boolean updateBookInfo(int bookId, String field, String newValue) {
        String sql = String.format("UPDATE book SET %s = ? WHERE book_id = ?", field);
        try (Connection conn = DataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newValue);
            pstmt.setInt(2, bookId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "更新失败: " + e.getMessage());
            return false;
        }
    }
}
