package org.librarymanagment.userMethod;

import org.librarymanagment.database.DataBase;

public class Login {

    public static boolean[] userLogin(String userName, String password) {
        // 第一个元素表示登录是否成功，第二个元素表示是否为管理员
        boolean[] result = new boolean[2];
        result[0] = DataBase.UserListDB.UserLogin(userName, password, result);
        return result;
    }
}