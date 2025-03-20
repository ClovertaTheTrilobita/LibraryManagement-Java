package org.librarymanagment.management;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class Index {
    private static final Color BG_COLOR = new Color(240, 240, 240);
    private static final Font TITLE_FONT = new Font("微软雅黑", Font.BOLD, 18);
    private static final Font BUTTON_FONT = new Font("宋体", Font.PLAIN, 14);
    private static final Dimension BUTTON_SIZE = new Dimension(200, 40);
    private static final Dimension FIELD_SIZE = new Dimension(250, 30);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> showAdminMenu("admin"));
    }

    public static void showAdminMenu(String username) {
        JFrame frame = createBaseFrame("图书管理系统管理员界面", 400, 400);

        JPanel mainPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(BG_COLOR);

        addStyledLabel(mainPanel, "欢迎, " + username, TITLE_FONT);
        addStyledLabel(mainPanel, "您有权限进行以下操作:", BUTTON_FONT);

        addMenuButton(mainPanel, "添加其他管理员", e -> showUserAddMenu());
        addMenuButton(mainPanel, "添加新图书", e -> showBookAddMenu());
        addMenuButton(mainPanel, "删除指定图书", e -> showBookDeleteMenu());
        addMenuButton(mainPanel, "查询图书信息", e -> showBookQueryMenu());
        addMenuButton(mainPanel, "查询借还书记录", e -> showBorrowManageMenu());
        addMenuButton(mainPanel, "退出", e -> frame.dispose());

        frame.add(mainPanel);
        centerFrame(frame);
    }

    private static void showUserAddMenu() {
        JFrame frame = createBaseFrame("添加图书", 300, 300);
        JPanel panel = createFormPanel();

        addFormField(panel, "书名:", new JTextField());
        addFormField(panel, "作者:", new JTextField());
        addFormField(panel, "位置:", new JTextField());

        JPanel buttonPanel = createButtonPanel();
        addActionButton(buttonPanel, "添加", e -> {
            String bookName = ((JTextField)panel.getComponent(1)).getText();
            String author = ((JTextField)panel.getComponent(3)).getText();
            String location = ((JTextField)panel.getComponent(5)).getText();

            if(BookManagement.addBook(bookName, author, location)) {
                JOptionPane.showMessageDialog(frame, "添加成功");
            }
        });
        addBackButton(buttonPanel, frame);

        frame.add(panel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        centerFrame(frame);
    }

    private static void showBookAddMenu() {
        JFrame frame = createBaseFrame("添加图书", 300, 250);
        JPanel panel = createFormPanel();

        addFormField(panel, "书名:", new JTextField());
        addFormField(panel, "作者:", new JTextField());

        JPanel buttonPanel = createButtonPanel();
        addActionButton(buttonPanel, "添加", e -> {
            // 添加图书逻辑
            JOptionPane.showMessageDialog(frame, "图书添加成功！");
            frame.dispose();
        });
        addBackButton(buttonPanel, frame);

        frame.add(panel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        centerFrame(frame);
    }

    private static void showBookDeleteMenu() {
        JFrame frame = createBaseFrame("删除指定图书", 300, 250);
        JPanel panel = createFormPanel();

        addFormField(panel, "图书ID:", new JTextField());

        JPanel buttonPanel = createButtonPanel();
        addActionButton(buttonPanel, "删除", e -> {
            // 删除图书逻辑
            JOptionPane.showMessageDialog(frame, "图书删除成功！");
            frame.dispose();
        });
        addBackButton(buttonPanel, frame);

        frame.add(panel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        centerFrame(frame);
    }

    private static void showBookQueryMenu() {
        JFrame frame = createBaseFrame("查询图书信息", 350, 300);
        JPanel panel = new JPanel(new GridLayout(4, 1));

        JComboBox<String> searchType = new JComboBox<>(new String[]{"按ID查询", "按书名查询", "按入库日期查询"});
        JTextField keywordField = new JTextField();

        panel.add(new JLabel("选择查询方式:"));
        panel.add(searchType);
        panel.add(new JLabel("输入关键词:"));
        panel.add(keywordField);

        JButton searchBtn = new JButton("查询");
        searchBtn.addActionListener(e -> {
            String type = switch(searchType.getSelectedIndex()) {
                case 0 -> "id";
                case 1 -> "name";
                case 2 -> "time";
                default -> "name";
            };

            List<String> results = BookManagement.searchBooks(type, keywordField.getText());
            if (results != null && !results.isEmpty()) {
                JOptionPane.showMessageDialog(frame, String.join("\n\n", results.toArray(new String[0])));
            } else {
                JOptionPane.showMessageDialog(frame, "未找到相关记录");
            }
        });

        frame.add(panel, BorderLayout.CENTER);
        frame.add(searchBtn, BorderLayout.SOUTH);
        centerFrame(frame);
    }

    private static void showBorrowManageMenu() {
        JFrame frame = createBaseFrame("查询借还书记录", 300, 250);
        JPanel panel = createFormPanel();

        addFormField(panel, "图书ID:", new JTextField());
        addFormField(panel, "借阅者ID:", new JTextField());

        JPanel buttonPanel = createButtonPanel();
        addActionButton(buttonPanel, "查询", e -> {
            // 查询借还书记录逻辑
            JOptionPane.showMessageDialog(frame, "查询结果：\n（此处应显示查询结果）");
            frame.dispose();
        });
        addBackButton(buttonPanel, frame);

        frame.add(panel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        centerFrame(frame);
    }



    private static JFrame createBaseFrame(String title, int width, int height) {
        JFrame frame = new JFrame(title);
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        return frame;
    }

    private static JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(BG_COLOR);
        return panel;
    }

    private static JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(BG_COLOR);
        return panel;
    }

    private static void addFormField(JPanel panel, String label, JComponent field) {
        field.setPreferredSize(FIELD_SIZE);
        JPanel fieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fieldPanel.setBackground(BG_COLOR);
        fieldPanel.add(new JLabel(label));
        fieldPanel.add(field);
        panel.add(fieldPanel);
    }

    private static void addMenuButton(JPanel panel, String text, java.awt.event.ActionListener listener) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setPreferredSize(BUTTON_SIZE);
        button.addActionListener(listener);
        panel.add(button);
    }

    private static void addStyledLabel(JPanel panel, String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label);
    }

    private static void addActionButton(JPanel panel, String text, java.awt.event.ActionListener listener) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.addActionListener(listener);
        panel.add(button);
    }

    private static void addBackButton(JPanel panel, JFrame frame) {
        JButton button = new JButton("返回");
        button.setFont(BUTTON_FONT);
        button.addActionListener(e -> frame.dispose());
        panel.add(button);
    }

    private static void centerFrame(JFrame frame) {
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}