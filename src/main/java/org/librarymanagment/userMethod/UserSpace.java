package org.librarymanagment.userMethod;

import org.librarymanagment.database.DataBase;
import org.librarymanagment.database.User;
import javax.swing.JOptionPane;
import java.util.Vector;

public class UserSpace {
    private DataBase db;
    private DataBase.UserListDB userDB;
    private User user;
    private String userName;

    public UserSpace(String userName) {
        this.userName = userName;
        db = new DataBase();
        userDB = db.new UserListDB();
        user = userDB.getUserByUserName(userName);
    }

    // 获取用户信息
    public Vector<Vector> getUser() {
        Vector<Vector> userData = new Vector<>();
        if (user != null) {
            Vector<String> userRow = new Vector<>();
            userRow.add(user.getUserName());
            userRow.add(user.getPassword());
            userRow.add(user.getEmail());
            userRow.add(user.getPhone());
            userData.add(userRow);
        }
        return userData;
    }

    // 修改用户信息
    public boolean update(String field, String newValue) {
        try {
            switch (field) {
                case "用户名":
                    if (userDB.isUserNameExists(newValue)) {
                        JOptionPane.showMessageDialog(null, "该用户名已被使用，请选择其他用户名。");
                        return false;
                    }
                    user.setUserName(newValue);
                    this.userName = newValue; // 更新当前保存的用户名
                    break;
                case "密码":
                    user.setPassword(newValue);
                    break;
                case "邮箱":
                    if (userDB.isEmailExists(newValue)) {
                        JOptionPane.showMessageDialog(null, "该邮箱已被使用，请选择其他邮箱。");
                        return false;
                    }
                    user.setEmail(newValue);
                    break;
                case "手机号":
                    if (userDB.isPhoneExists(newValue)) {
                        JOptionPane.showMessageDialog(null, "该手机号码已被使用，请选择其他手机号码。");
                        return false;
                    }
                    user.setPhone(newValue);
                    break;
                default:
                    return false;
            }
            System.out.println("更新 " + field + " 为: " + newValue);
            return userDB.updateUser(user.getUserId(), user);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 更新整个表格数据
    public boolean update(Vector<Vector> tableData) {
        boolean hasError = false;
        for (Vector<String> row : tableData) {
            String field = row.get(0);
            String newValue = row.get(1);
            if (!update(field, newValue)) {
                hasError = true;
                break;
            }
        }
        if (!hasError) {
            // 更新用户对象
            user = userDB.getUserByUserName(userName);
        }
        return!hasError;
    }

    // 获取当前用户名
    public String getUserName() {
        return userName;
    }
}