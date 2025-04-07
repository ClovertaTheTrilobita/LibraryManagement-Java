package org.librarymanagment.gui;

import org.librarymanagment.componen.BackgroundPanel;
import org.librarymanagment.userMethod.Login;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class UserLogin {

    JFrame jf = new JFrame("海大图书馆");

    final int WIDE = 500;
    final int HIGH = 300;

    // 组装视图
    public void init() throws Exception {
        // 设置窗口相关属性

        jf.setResizable(false);
        //由此插入图标
        jf.setIconImage(new ImageIcon(getClass().getResource("/images/library.png")).getImage());
        jf.setSize(WIDE, HIGH);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setLocationRelativeTo(null);

        // 设置窗口内容，由此插入背景图片
        BackgroundPanel bgPanel = new BackgroundPanel(ImageIO.read(new File(getClass().getResource("/images/library.JPG").getPath())));

        // 组装登录相关的元素
        Box vBox = Box.createVerticalBox();

        // 组装用户名
        Box uBox = Box.createHorizontalBox();
        JLabel uLabel = new JLabel("用户名：");
        JTextField uField = new JTextField(15);

        uBox.add(uLabel);
        uBox.add(Box.createHorizontalStrut(20));
        uBox.add(uField);

        // 组装密码
        Box pBox = Box.createHorizontalBox();
        JLabel pLabel = new JLabel("密    码：");
        JPasswordField pField = new JPasswordField(15);

        pBox.add(pLabel);
        pBox.add(Box.createHorizontalStrut(20));
        pBox.add(pField);

        // 组装按钮
        Box btnBox = Box.createHorizontalBox();
        JButton loginButton = new JButton("登录");
        JButton registerButton = new JButton("没有账号？去注册");
        // 设置按钮无边框


        //登录按钮绑定事件：验证登录
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 获取用户输入的数据
                String userName = uField.getText();
                String password = new String(pField.getPassword());

                // 调用 Login 类的 userLogin 方法进行验证
                if (Login.userLogin(userName, password)) {
                    JOptionPane.showMessageDialog(jf, "登录成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                    // 进行后续操作，打开主界面
                    try {
                        new SystemGui().init();
                        jf.dispose();
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(jf, "用户名或密码错误！", "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        //注册按钮绑定事件跳转到注册页面
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(jf , "跳转到注册页面");
                try {
                    new UserRegister().init();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                //当前界面消失
                jf.dispose();
            }
        });
        btnBox.add(loginButton);
        btnBox.add(Box.createHorizontalStrut(100));
        btnBox.add(registerButton);

        vBox.add(Box.createVerticalStrut(40));
        vBox.add(uBox);
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(pBox);
        vBox.add(Box.createVerticalStrut(40));
        vBox.add(btnBox);

        bgPanel.add(vBox);
        jf.add(bgPanel);
        jf.setVisible(true);
    }


    // 客户端程序入口
    public static void main(String[] args) {
        try {
            UserLogin app = new UserLogin();
            app.init();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "程序初始化失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
}