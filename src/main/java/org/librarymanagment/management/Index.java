package org.librarymanagment.management;

import org.librarymanagment.database.DataBase;

import org.librarymanagment.database.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

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
        JFrame frame = new JFrame("添加管理员");
        frame.setSize(400, 300);
        JPanel panel = new JPanel(new GridLayout(6, 2));

        // 输入字段
        JTextField userNameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JTextField emailField = new JTextField();
        JTextField phoneField = new JTextField();
        JComboBox<String> genderCombo = new JComboBox<>(new String[]{"女", "男"});
        JButton submitButton = new JButton("提交");

        // 添加组件到面板
        panel.add(new JLabel("用户名:"));
        panel.add(userNameField);
        panel.add(new JLabel("密码:"));
        panel.add(passwordField);
        panel.add(new JLabel("邮箱:"));
        panel.add(emailField);
        panel.add(new JLabel("电话:"));
        panel.add(phoneField);
        panel.add(new JLabel("性别:"));
        panel.add(genderCombo);
        panel.add(new JLabel()); // 占位
        panel.add(submitButton);

        // 提交按钮事件
        submitButton.addActionListener(e -> {
            String userName = userNameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            int gender = genderCombo.getSelectedIndex(); // 0: 女, 1: 男

            // 输入验证
            if (userName.isEmpty() || password.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "所有字段均不能为空");
                return;
            }
            if (!email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                JOptionPane.showMessageDialog(frame, "邮箱格式不正确");
                return;
            }
            if (!phone.matches("\\d{11}")) {
                JOptionPane.showMessageDialog(frame, "电话必须为11位数字");
                return;
            }

            // 调用 UserManagement.addAdmin
            boolean success = UserManagement.addAdmin(userName, password, email, phone, gender);
            if (success) {
                frame.dispose();
            }
        });

        frame.add(panel);
        frame.setVisible(true);
    }

    private static void showBookAddMenu() {
        JFrame frame = createBaseFrame("添加图书", 300, 350);
        JPanel panel = createFormPanel();

        // 直接持有组件引用
        JTextField bookNameField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField locationField = new JTextField();

        addLabeledField(panel, "书名:", bookNameField);
        addLabeledField(panel, "作者:", authorField);
        addLabeledField(panel, "位置:", locationField);

        JPanel buttonPanel = createButtonPanel();
        addActionButton(buttonPanel, "添加", e -> {
            String bookName = bookNameField.getText().trim();
            String author = authorField.getText().trim();
            String location = locationField.getText().trim();

            if (bookName.isEmpty() || author.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "书名和作者不能为空");
                return;
            }

            if (BookManagement.addBook(bookName, author, location)) {
                JOptionPane.showMessageDialog(frame, "图书添加成功");
                frame.dispose();
            }
            // 错误提示已由 BookManagement 处理
        });
        addBackButton(buttonPanel, frame);

        frame.add(panel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        centerFrame(frame);
    }

    private static void showBookDeleteMenu() {
        JFrame frame = createBaseFrame("删除图书", 300, 200);
        JPanel panel = createFormPanel();

        // 直接持有组件引用
        JTextField bookIdField = new JTextField();
        addLabeledField(panel, "图书ID:", bookIdField);

        JPanel buttonPanel = createButtonPanel();
        addActionButton(buttonPanel, "删除", e -> {
            String bookIdStr = bookIdField.getText().trim();

            if (bookIdStr.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "图书ID不能为空");
                return;
            }

            try {
                int bookId = Integer.parseInt(bookIdStr);
                // 调用修改后的 BookManagement.deleteBook
                if (BookManagement.deleteBook(bookId)) {
                    frame.dispose(); // 关闭窗口
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "图书ID必须为数字");
            }
        });
        addBackButton(buttonPanel, frame);

        frame.add(panel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        centerFrame(frame);
    }

    private static void showBookUpdateMenu() {
        JFrame frame = new JFrame("更新图书信息");
        frame.setSize(400, 400);
        JPanel panel = new JPanel(new GridLayout(7, 2));

        // 输入字段
        JTextField bookIdField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField locationField = new JTextField();
        JComboBox<String> isBorrowedCombo = new JComboBox<>(new String[]{"未借出", "已借出"});
        JTextField storageTimeField = new JTextField(); // 格式：yyyy-MM-dd HH:mm:ss
        JButton submitButton = new JButton("提交");

        // 添加组件到面板
        panel.add(new JLabel("书籍ID:"));
        panel.add(bookIdField);
        panel.add(new JLabel("书名:"));
        panel.add(nameField);
        panel.add(new JLabel("作者:"));
        panel.add(authorField);
        panel.add(new JLabel("位置:"));
        panel.add(locationField);
        panel.add(new JLabel("借阅状态:"));
        panel.add(isBorrowedCombo);
        panel.add(new JLabel("入库时间 (格式: yyyy-MM-dd HH:mm:ss):"));
        panel.add(storageTimeField);
        panel.add(new JLabel()); // 占位
        panel.add(submitButton);

        // 提交按钮事件
        submitButton.addActionListener(e -> {
            try {
                int bookId = Integer.parseInt(bookIdField.getText().trim());
                String name = nameField.getText().trim();
                String author = authorField.getText().trim();
                String location = locationField.getText().trim();
                int isBorrowed = isBorrowedCombo.getSelectedIndex(); // 0: 未借出, 1: 已借出
                Timestamp storageTime = Timestamp.valueOf(storageTimeField.getText().trim());

                // 输入验证（示例，可根据需求扩展）
                if (name.isEmpty() || author.isEmpty() || location.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "书名、作者、位置不能为空");
                    return;
                }

                // 调用 BookManagement.updateBookInfo
                boolean success = BookManagement.updateBookInfo(
                        bookId, name, author, location, isBorrowed, storageTime
                );
                if (success) {
                    frame.dispose();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "书籍ID必须为数字");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(frame, "入库时间格式错误，正确格式：yyyy-MM-dd HH:mm:ss");
            }
        });

        frame.add(panel);
        frame.setVisible(true);
    }

    private static void showBookQueryMenu() {
        JFrame frame = createBaseFrame("图书查询", 400, 200);
        JPanel mainPanel = new JPanel(new BorderLayout());

        // 查询类型选择面板
        JPanel typePanel = new JPanel(new FlowLayout());
        JComboBox<String> searchTypeCombo = new JComboBox<>(
                new String[]{"按ID查询", "按书名查询", "按作者查询", "按位置查询", "按入库日期查询"}
        );
        typePanel.add(new JLabel("查询类型："));
        typePanel.add(searchTypeCombo);

        // 页码输入面板
        JPanel pagePanel = new JPanel(new FlowLayout());
        JTextField pageField = new JTextField("1", 5);
        pagePanel.add(new JLabel("页码："));
        pagePanel.add(pageField);

        // 结果展示区
        JTextArea resultArea = new JTextArea(8, 30);
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        // 操作按钮
        JButton searchBtn = new JButton("查询");
        JButton prevBtn = new JButton("上一页");
        JButton nextBtn = new JButton("下一页");

        // 查询按钮事件
        searchBtn.addActionListener(e -> {
            int searchType = searchTypeCombo.getSelectedIndex();
            int pageNumber;

            try {
                pageNumber = Integer.parseInt(pageField.getText());
                if (pageNumber < 1) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "页码必须为大于0的整数");
                return;
            }

            switch (searchType) {
                case 4: // 入库日期查询
                    showDateInputDialog(frame, pageNumber, resultArea);
                    break;
                default:
                    handleCommonSearch(searchTypeCombo, pageNumber, resultArea);
            }
        });

        // 分页按钮事件
        prevBtn.addActionListener(e -> {
            int current = Integer.parseInt(pageField.getText());
            if (current > 1) pageField.setText(String.valueOf(current - 1));
            searchBtn.doClick();
        });

        nextBtn.addActionListener(e -> {
            int current = Integer.parseInt(pageField.getText());
            pageField.setText(String.valueOf(current + 1));
            searchBtn.doClick();
        });

        // 按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(prevBtn);
        buttonPanel.add(searchBtn);
        buttonPanel.add(nextBtn);

        // 布局组装
        mainPanel.add(typePanel, BorderLayout.NORTH);
        mainPanel.add(pagePanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(mainPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        centerFrame(frame);
    }

    // 显示日期输入对话框
    private static void showDateInputDialog(Component parent, int pageNumber, JTextArea resultArea) {
        JDialog dialog = new JDialog((Frame) null, "日期范围查询", true);
        dialog.setLayout(new GridLayout(3, 2, 10, 10));
        dialog.setSize(300, 150);

        JTextField startField = new JTextField();
        JTextField endField = new JTextField();

        dialog.add(new JLabel("开始日期 (yyyy-MM-dd HH:mm:ss):"));
        dialog.add(startField);
        dialog.add(new JLabel("结束日期 (yyyy-MM-dd HH:mm:ss):"));
        dialog.add(endField);

        JButton confirmBtn = new JButton("确定");
        JButton cancelBtn = new JButton("取消");

        confirmBtn.addActionListener(e -> {
            try {
                Timestamp start = Timestamp.valueOf(startField.getText().trim());
                Timestamp end = Timestamp.valueOf(endField.getText().trim());

                List<Book> books = new DataBase().new BookDB()
                        .searchBooksByStorageDate(start, end, pageNumber);

                resultArea.setText(formatBookResults(books));
                dialog.dispose();
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(dialog, "日期格式错误，请使用：yyyy-MM-dd HH:mm:ss");
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        JPanel btnPanel = new JPanel();
        btnPanel.add(confirmBtn);
        btnPanel.add(cancelBtn);

        dialog.add(btnPanel);
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    // 处理常规查询
    private static void handleCommonSearch(JComboBox<String> combo, int pageNumber, JTextArea resultArea) {
        String searchType = switch (combo.getSelectedIndex()) {
            case 0 -> "id";
            case 1 -> "name";
            case 2 -> "author";
            case 3 -> "location";
            default -> "name";
        };

        String keyword = JOptionPane.showInputDialog("请输入搜索关键字：");
        if (keyword == null || keyword.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "搜索内容不能为空");
            return;
        }

        List<Book> books;
        DataBase.BookDB bookDB = new DataBase().new BookDB();
        switch (searchType) {
            case "id":
                books = bookDB.searchBooksById(Integer.parseInt(keyword), pageNumber);
                break;
            case "name":
                books = bookDB.searchBooksByName(keyword, pageNumber);
                break;
            case "author":
                books = bookDB.searchBooksByAuthor(keyword, pageNumber);
                break;
            case "location":
                books = bookDB.searchBooksByLocation(keyword, pageNumber);
                break;
            default:
                books = new ArrayList<>();
        }

        resultArea.setText(formatBookResults(books));
    }

    // 格式化查询结果
    private static String formatBookResults(List<Book> books) {
        if (books.isEmpty()) return "未找到匹配记录";

        StringBuilder sb = new StringBuilder();
        for (Book book : books) {
            sb.append(String.format(
                    "ID: %d\n书名: %s\n作者: %s\n位置: %s\n状态: %s\n入库时间: %s\n\n",
                    book.getId(),
                    book.getName(),
                    book.getAuthor(),
                    book.getLocation(),
                    book.getIsBorrowed() == 1 ? "已借出" : "在馆",
                    book.getStorageTime()
            ));
        }
        return sb.toString().trim();
    }

    private static void showBorrowManageMenu() {
        JFrame frame = createBaseFrame("借阅记录管理", 400, 300);
        JPanel panel = createFormPanel();

        // 直接引用组件
        JComboBox<String> searchType = new JComboBox<>(new String[]{
                "按用户ID查询所有记录",
                "按用户ID查询未归还记录",
                "按图书ID/名称查询"
        });
        JTextField inputField = new JTextField();

        addLabeledField(panel, "查询类型:", searchType);
        addLabeledField(panel, "输入内容:", inputField);

        JPanel buttonPanel = createButtonPanel();
        addActionButton(buttonPanel, "查询", e -> {
            try {
                int type = searchType.getSelectedIndex();
                String keyword = inputField.getText().trim();

                if(keyword.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "请输入查询内容");
                    return;
                }

                // 查询逻辑
                List<String> results = new ArrayList<>();
                switch(type) {
                    case 0:
                    case 1:
                        int userId = Integer.parseInt(keyword);
                        results = UserManagement.getBorrowRecords(userId, type == 1);
                        break;
                    case 2:
                        String status = UserManagement.getBookBorrowStatus(keyword);
                        results.add(status);
                        break;
                }

                if(!results.isEmpty()) {
                    new ResultDialog(frame, "查询结果", results);
                } else {
                    JOptionPane.showMessageDialog(frame, "未找到相关记录");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "用户ID必须是数字");
            }
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
        JPanel fieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fieldPanel.setBackground(BG_COLOR);
        field.setPreferredSize(FIELD_SIZE);
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

    // 添加带标签的输入组件
    private static void addLabeledField(JPanel panel, String label, JComponent field) {
        JPanel fieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fieldPanel.setBackground(BG_COLOR);
        field.setPreferredSize(FIELD_SIZE);
        fieldPanel.add(new JLabel(label));
        fieldPanel.add(field);
        panel.add(fieldPanel);
    }
}

// 专用结果展示对话框
 class ResultDialog extends JDialog {
    public ResultDialog(JFrame parent, String title, List<String> results) {
        super(parent, title, true);
        setSize(500, 400);
        setMinimumSize(new Dimension(400, 300)); // 设置最小大小

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("宋体", Font.PLAIN, 14));
        textArea.setText(String.join("\n\n", results));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeBtn = new JButton("关闭");
        closeBtn.addActionListener(e -> dispose());
        buttonPanel.add(closeBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(parent);
    }

    public void showDialog() {
        setVisible(true);
    }
}
