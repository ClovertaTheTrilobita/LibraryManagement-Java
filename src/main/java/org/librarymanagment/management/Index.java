package org.librarymanagment.management;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Index {
    private static String username;

    public static void main(String[] args) {
        // 假设初始用户名为 "admin"
        username = "admin";
        showAdminMenu(username);
    }

    public static void showAdminMenu(String username) {
        Index.username = username;
        JFrame frame = new JFrame("图书管理系统管理员界面");
        frame.setSize(600, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        frame.add(new JLabel("欢迎, " + username));
        frame.add(new JLabel("您有权限进行以下操作:"));

        JButton addAdminButton = new JButton("添加其他管理员");
        addAdminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showUserAddMenu();
            }
        });
        frame.add(addAdminButton);

        JButton addBookButton = new JButton("添加新图书");
        addBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showBookAddMenu();
            }
        });
        frame.add(addBookButton);

        JButton deleteBookButton = new JButton("删除指定图书");
        deleteBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showBookDeleteMenu();
            }
        });
        frame.add(deleteBookButton);

        JButton queryBookButton = new JButton("查询图书信息");
        queryBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showBookQueryMenu();
            }
        });
        frame.add(queryBookButton);

        JButton queryBorrowButton = new JButton("查询借还书记录");
        queryBorrowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showBorrowManageMenu();
            }
        });
        frame.add(queryBorrowButton);

        JButton exitButton = new JButton("退出");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        frame.add(exitButton);

        frame.pack();
        frame.setVisible(true);
    }

    private static void showUserAddMenu() {
        JFrame frame = new JFrame("添加管理员");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.add(new JLabel("管理员信息"));

        JTextField usernameField = new JTextField();
        frame.add(new JLabel("用户名:"));
        frame.add(usernameField);

        JTextField passwordField = new JTextField();
        frame.add(new JLabel("密码:"));
        frame.add(passwordField);

        JButton addButton = new JButton("添加");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText().trim();
                String password = passwordField.getText().trim();
                addAdmin(username, password);
                frame.dispose();
            }
        });
        frame.add(addButton);

        JButton backButton = new JButton("返回");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        frame.add(backButton);

        frame.pack();
        frame.setVisible(true);
    }

    private static void showBookAddMenu() {
        JFrame frame = new JFrame("添加图书");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.add(new JLabel("图书信息"));

        JTextField titleField = new JTextField();
        frame.add(new JLabel("书名:"));
        frame.add(titleField);

        JTextField authorField = new JTextField();
        frame.add(new JLabel("作者:"));
        frame.add(authorField);

        JButton addButton = new JButton("添加");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = titleField.getText().trim();
                String author = authorField.getText().trim();
                addBook(title, author);
                frame.dispose();
            }
        });
        frame.add(addButton);

        JButton backButton = new JButton("返回");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        frame.add(backButton);

        frame.pack();
        frame.setVisible(true);
    }

    private static void showBookDeleteMenu() {
        JFrame frame = new JFrame("删除图书");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.add(new JLabel("删除图书信息"));

        JButton deleteButton = new JButton("删除");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 删除图书逻辑
                frame.dispose();
            }
        });
        frame.add(deleteButton);

        JButton backButton = new JButton("返回");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        frame.add(backButton);

        frame.pack();
        frame.setVisible(true);
    }

    private static void showBookQueryMenu() {
        JFrame frame = new JFrame("查询图书信息");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.add(new JLabel("查询图书信息"));

        JButton backButton = new JButton("返回");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        frame.add(backButton);

        frame.pack();
        frame.setVisible(true);
    }

    private static void showBorrowManageMenu() {
        JFrame frame = new JFrame("查询借还书记录");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.add(new JLabel("查询借还书记录"));

        JButton backButton = new JButton("返回");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        frame.add(backButton);

        frame.pack();
        frame.setVisible(true);
    }

    private static void addAdmin(String username, String password) {
        // 添加管理员逻辑
        System.out.println("管理员 " + username + " 已添加");
    }

    private static void addBook(String title, String author) {
        // 添加图书逻辑
        System.out.println("图书 " + title + " 作者 " + author + " 已添加");
    }
}
