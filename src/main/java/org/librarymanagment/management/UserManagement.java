package org.librarymanagment.management;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import org.librarymanagment.database.DataBase;
import org.librarymanagment.database.User;

public class UserManagement {
    // 添加管理员
    public static boolean addAdmin(String userName, String password, String email,
                                   String phone, int gender) {
        // 创建 User 对象，isAdmin 固定为 1
        User user = new User(
                0,          // userId 由数据库自增
                userName,
                password,
                email,
                phone,
                gender,
                1           // is_admin=1 表示管理员
        );

        // 调用数据库操作类
        DataBase db = new DataBase();
        DataBase.UserListDB userDB = db.new UserListDB();
        boolean success = userDB.addUser(user);

        // 错误处理
        if (!success) {
            JOptionPane.showMessageDialog(null, "添加管理员失败，用户名或邮箱可能重复");
        } else {
            JOptionPane.showMessageDialog(null, "管理员添加成功");
        }
        return success;
    }


    // 获取借阅记录
//    public static List<String> getBorrowRecords(int userId, boolean unreturnedOnly) {
//        List<String> records = new ArrayList<>();
//        String sql = "SELECT b.book_name, bl.borrow_time, bl.return_time " +
//                "FROM borrow_list bl " +
//                "JOIN book b ON bl.book_id = b.book_id " +
//                "WHERE bl.user_id = ?" +
//                (unreturnedOnly ? " AND bl.return_time IS NULL" : "");
//
//        try (Connection conn = DataBase.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setInt(1, userId);
//            ResultSet rs = pstmt.executeQuery();
//
//            while(rs.next()) {
//                String record = String.format(
//                        "📖 书名: %s\n⏰ 借阅时间: %s\n🔁 归还状态: %s",
//                        rs.getString("book_name"),
//                        rs.getTimestamp("borrow_time"),
//                        rs.getTimestamp("return_time") == null ? "❌ 未归还" : "✅ 已归还"
//                );
//                records.add(record);
//            }
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "查询失败: " + e.getMessage());
//        }
//        return records;
//    }
//
//    // 获取图书借阅情况
//    public static String getBookBorrowStatus(String keyword) {
//        String sql = "SELECT b.book_name, COUNT(bl.borrow_id) AS total_borrow, " +
//                "SUM(CASE WHEN bl.return_time IS NULL THEN 1 ELSE 0 END) AS unreturned " +
//                "FROM book b LEFT JOIN borrow_list bl ON b.book_id = bl.book_id " +
//                "WHERE b.book_id = ? OR b.book_name LIKE ? " +
//                "GROUP BY b.book_id";
//
//        try (Connection conn = DataBase.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//
//            try {
//                int bookId = Integer.parseInt(keyword);
//                pstmt.setInt(1, bookId);
//                pstmt.setString(2, "%"+keyword+"%");
//            } catch (NumberFormatException e) {
//                pstmt.setInt(1, -1);
//                pstmt.setString(2, "%"+keyword+"%");
//            }
//
//            ResultSet rs = pstmt.executeQuery();
//            if(rs.next()) {
//                return String.format(
//                        "📚 书名: %s\n🔢 总借阅次数: %d\n🚫 未归还数量: %d",
//                        rs.getString("book_name"),
//                        rs.getInt("total_borrow"),
//                        rs.getInt("unreturned")
//                );
//            }
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "查询失败: " + e.getMessage());
//        }
//        return "未找到相关记录";
//    }
}