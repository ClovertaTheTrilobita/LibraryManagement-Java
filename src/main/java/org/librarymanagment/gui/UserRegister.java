package org.librarymanagment.gui;

import org.librarymanagment.componen.BackgroundPanel;
import org.librarymanagment.userMethod.Register;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class UserRegister {
    JFrame jf = new JFrame("注册");

    final int WIDTH = 500;
    final int HEIGHT = 400;

    // 组装视图
    public void init() throws Exception {
        // 设置窗口的属性
        jf.setResizable(false);
        // 插入图标
        jf.setIconImage(new ImageIcon(getClass().getResource("/images/library.png")).getImage());
        // 设置窗口内容，插入背景图片
        BackgroundPanel bgPanel = new BackgroundPanel(ImageIO.read(new File(getClass().getResource("/images/register.JPG").getPath())));
        jf.setSize(WIDTH, HEIGHT);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        bgPanel.setBounds(0, 0, WIDTH, HEIGHT);

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

        // 组装邮箱号
        Box eBox = Box.createHorizontalBox();
        JLabel eLabel = new JLabel("邮箱号：");
        JTextField eField = new JTextField(15);

        eBox.add(eLabel);
        eBox.add(Box.createHorizontalStrut(20));
        eBox.add(eField);

        // 组装手机号
        Box tBox = Box.createHorizontalBox();
        JLabel tLabel = new JLabel("手机号：");
        JTextField tField = new JTextField(11);

        tBox.add(tLabel);
        tBox.add(Box.createHorizontalStrut(20));
        tBox.add(tField);

        // 组装性别
        Box gBox = Box.createHorizontalBox();
        JLabel gLabel = new JLabel("性    别：");
        JRadioButton maleBth = new JRadioButton("男", true);
        JRadioButton femaleBth = new JRadioButton("女", false);

        // 实现单选的效果
        ButtonGroup bg = new ButtonGroup();
        bg.add(maleBth);
        bg.add(femaleBth);

        gBox.add(gLabel);
        gBox.add(Box.createHorizontalStrut(20));
        gBox.add(maleBth);
        gBox.add(femaleBth);
        gBox.add(Box.createHorizontalStrut(120));

        // 组装按钮
        Box btnBox = Box.createHorizontalBox();
        JButton registerButton = new JButton("注册");
        JButton backButton = new JButton("返回登录页面");

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userName = uField.getText().trim();
                String password = new String(pField.getPassword()).trim();
                String email = eField.getText().trim();
                String phone = tField.getText().trim();
                int gender = maleBth.isSelected() ? 1 : 0;

                // 输入验证
                if (userName.isEmpty() || password.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                    JOptionPane.showMessageDialog(jf, "所有字段均不能为空");
                    return;
                }
                if (!email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                    JOptionPane.showMessageDialog(jf, "邮箱格式不正确");
                    return;
                }
                if (!phone.matches("\\d{11}")) {
                    JOptionPane.showMessageDialog(jf, "电话必须为11位数字");
                    return;
                }

                // 调用 UserManagement.addAdmin
                String result = Register.UserRegister(userName, password, email, phone, gender);
                if ("success".equals(result)) {
                    JOptionPane.showMessageDialog(jf, "注册成功,即将返回登陆页面");

                    try {
                        new UserLogin().init();
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                    jf.dispose();
                } else if ("username_exists".equals(result)) {
                    JOptionPane.showMessageDialog(jf, "该用户名已存在，请选择其他用户名");
                } else if ("phone_exists".equals(result)) {
                    JOptionPane.showMessageDialog(jf, "该手机号已被注册，请使用其他手机号");
                } else if ("email_exists".equals(result)) {
                    JOptionPane.showMessageDialog(jf, "该邮箱已被注册，请使用其他邮箱");
                } else {
                    JOptionPane.showMessageDialog(jf, "注册失败，请稍后重试");
                }
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(jf, "跳转到登陆页面");
                try {
                    new UserLogin().init();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                jf.dispose();
            }

        });
        btnBox.add(registerButton);
        btnBox.add(Box.createHorizontalStrut(100));
        btnBox.add(backButton);

        vBox.add(Box.createVerticalStrut(40));
        vBox.add(uBox);
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(pBox);
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(eBox);
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(tBox);
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(gBox);
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(btnBox);

        bgPanel.add(vBox);
        jf.add(bgPanel);
        jf.setLocationRelativeTo(null);
        jf.setVisible(true);
    }

    public static void main(String[] args) {
        try {
            new UserRegister().init();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}