package org.librarymanagment.userMethod;

import java.util.Vector;

// 定义一个用户类，用于存储用户信息
class User {
    private String username;
    private String password;
    private String email;
    private String phone;

    public User(String username, String password, String email, String phone) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

public class UserSpace {
    private User user;

    public UserSpace() {
        // 初始化一个用户对象，模拟从数据库中获取用户信息
        this.user = new User("用户名", "密码1", "email1@example.com", "12345678901");
    }

    // 获取用户信息的方法
    public Vector<Vector> getUser() {
        Vector<Vector> userData = new Vector<>();
        Vector<String> userRow = new Vector<>();
        userRow.add(user.getUsername());
        userRow.add(user.getPassword());
        userRow.add(user.getEmail());
        userRow.add(user.getPhone());
        userData.add(userRow);
        return userData;
    }

    // 修改后的 update 方法，接收修改的字段和新值，返回更新结果
    public boolean update(String field, String newValue) {
        try {
            switch (field) {
                case "用户名":
                    user.setUsername(newValue);
                    break;
                case "密码":
                    user.setPassword(newValue);
                    break;
                case "邮箱":
                    user.setEmail(newValue);
                    break;
                case "手机号":
                    user.setPhone(newValue);
                    break;
                default:
                    return false;
            }
            System.out.println("更新 " + field + " 为: " + newValue);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 新增的 update 方法，接收整个表格数据并更新数据库
    public boolean update(Vector<Vector> tableData) {
        try {
            for (Vector<String> row : tableData) {
                String field = row.get(0);
                String newValue = row.get(1);
                if (!update(field, newValue)) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}