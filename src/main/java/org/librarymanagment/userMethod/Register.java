package org.librarymanagment.userMethod;

import org.librarymanagment.database.DataBase;
import org.librarymanagment.database.User;

import javax.swing.*;

public class Register {
    // 注册新账号
    public static String UserRegister(String userName, String password, String email, String phone, int gender) {
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

        // 检查用户名是否已存在
        if (userDB.isUserNameExists(userName)) {
            return "username_exists";
        }
        // 检查邮箱是否已存在
        if (userDB.isEmailExists(email)) {
            return "email_exists";
        }
        // 检查手机号是否已存在
        if (userDB.isPhoneExists(phone)) {
            return "phone_exists";
        }

        // 执行插入操作
        boolean success = userDB.addUser(user);

        // 根据插入结果返回相应信息
        if (success) {
            return "success";
        } else {
            return "register_failed";
        }
    }
}