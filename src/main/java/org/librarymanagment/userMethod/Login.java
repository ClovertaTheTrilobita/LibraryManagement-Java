package org.librarymanagment.userMethod;

import org.librarymanagment.database.DataBase;

public class Login {

    public static boolean userLogin(String userName, String password) {
        // 调用数据库验证方法
        return DataBase.UserListDB.UserLogin(userName, password);
    }
}
