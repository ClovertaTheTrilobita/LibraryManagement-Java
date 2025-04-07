package org.librarymanagment.userMethod;

import org.librarymanagment.database.DataBase;
import org.librarymanagment.database.User;

import javax.swing.*;

public class Register {
    //注册新账号
    public static boolean UserRegister(String userName, String password, String email, String phone, int gender) {
        // 创建 User 对象，isAdmin 固定为 0
        User user = new User(
                0,          // userId 由数据库自增
                userName,
                password,
                email,
                phone,
                gender,
                0           // is_admin=0 表示普通用户
        );
        // 调用数据库操作类
        DataBase db = new DataBase();
        DataBase.UserListDB userDB = db.new UserListDB();
        boolean success = userDB.addUser(user);

        // 错误处理
        if (!success) {
            JOptionPane.showMessageDialog(null, "注册用户失败，用户名或邮箱可能重复");
        } else {
            JOptionPane.showMessageDialog(null, "用户注册成功");
        }
        return success;
    }
}
