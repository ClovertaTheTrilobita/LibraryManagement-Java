package org.librarymanagment.management;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
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
        JFrame frame = createBaseFrame("添加管理员", 300, 250);
        JPanel panel = createFormPanel();

        // 添加输入字段
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        // 使用新方法添加带标签的输入框
        addLabeledField(panel, "用户名:", usernameField);
        addLabeledField(panel, "密码:", passwordField);

        JPanel buttonPanel = createButtonPanel();
        addActionButton(buttonPanel, "添加", e -> {
            // 正确获取输入内容
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if(username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "用户名和密码不能为空");
                return;
            }

            if(UserManagement.addAdmin(username, password)) {
                JOptionPane.showMessageDialog(frame, "管理员添加成功");
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "添加失败，用户名可能已存在");
            }
        });
        addBackButton(buttonPanel, frame);

        frame.add(panel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        centerFrame(frame);
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

            if(bookName.isEmpty() || author.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "书名和作者不能为空");
                return;
            }

            if(BookManagement.addBook(bookName, author, location)) {
                JOptionPane.showMessageDialog(frame, "图书添加成功");
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "添加失败，请检查输入");
            }
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
            String input = bookIdField.getText().trim();

            if(input.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "请输入图书ID");
                return;
            }

            try {
                int bookId = Integer.parseInt(input);
                if(BookManagement.deleteBook(bookId)) {
                    JOptionPane.showMessageDialog(frame, "删除成功");
                    frame.dispose();
                } else {
                    JOptionPane.showMessageDialog(frame, "删除失败，图书可能不存在或已被借出");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "请输入有效的数字ID");
            }
        });
        addBackButton(buttonPanel, frame);

        frame.add(panel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        centerFrame(frame);
    }

    private static void showBookUpdateMenu() {
        JFrame frame = createBaseFrame("修改图书信息", 400, 400);
        JPanel panel = createFormPanel();

        // 输入组件直接引用
        JTextField bookIdField = new JTextField();
        JComboBox<String> fieldCombo = new JComboBox<>(new String[]{"书名", "作者", "位置"});
        JTextField newValueField = new JTextField();

        addLabeledField(panel, "图书ID:", bookIdField);
        addLabeledField(panel, "选择修改项:", fieldCombo);
        addLabeledField(panel, "新内容:", newValueField);

        JPanel buttonPanel = createButtonPanel();
        addActionButton(buttonPanel, "修改", e -> {
            try {
                int bookId = Integer.parseInt(bookIdField.getText().trim());
                String field = switch(fieldCombo.getSelectedIndex()) {
                    case 0 -> "book_name";
                    case 1 -> "author";
                    case 2 -> "location";
                    default -> "";
                };
                String newValue = newValueField.getText().trim();

                if(BookManagement.updateBookInfo(bookId, field, newValue)) {
                    JOptionPane.showMessageDialog(frame, "修改成功");
                    frame.dispose();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "请输入有效的图书ID");
            }
        });
        addBackButton(buttonPanel, frame);

        frame.add(panel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        centerFrame(frame);
    }
    private static void showBookQueryMenu() {
        JFrame frame = createBaseFrame("图书查询", 350, 300);
        JPanel panel = new JPanel(new GridLayout(4, 1));

        // 查询类型选择
        JComboBox<String> searchType = new JComboBox<>(new String[]{
                "按ID查询",
                "按书名查询",
                "按入库日期查询(yyyy-MM-dd)"
        });

        // 关键词输入
        JTextField keywordField = new JTextField();

        panel.add(new JLabel("选择查询方式:"));
        panel.add(searchType);
        panel.add(new JLabel("输入查询内容:"));
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
            if (!results.isEmpty()) {
                JOptionPane.showMessageDialog(frame,
                        String.join("\n\n", results),
                        "查询结果",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame,
                        "未找到匹配记录",
                        "提示",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        frame.add(panel, BorderLayout.CENTER);
        frame.add(searchBtn, BorderLayout.SOUTH);
        centerFrame(frame);
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
