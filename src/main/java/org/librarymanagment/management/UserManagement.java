package org.librarymanagment.management;

import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;

import org.librarymanagment.database.BorrowInfo;
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
    public static List<BorrowInfo> getBorrowRecords(int userId, boolean unreturnedOnly) {
        DataBase db = new DataBase();
        DataBase.BorrowedBookManagement borrowDB = db.new BorrowedBookManagement();
        return borrowDB.getBorrowHistoryByUserIdWithBookName(userId).stream()
                .filter(info -> !unreturnedOnly || info.getReturnTime() == null)
                .collect(Collectors.toList());
    }
}