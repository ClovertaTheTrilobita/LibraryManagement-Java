package org.librarymanagment.componen;

import org.librarymanagment.database.Book;
import org.librarymanagment.database.DataBase;
import org.librarymanagment.database.User;
import org.librarymanagment.userMethod.BookSearch;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Vector;

public class UserBookSerchComponent extends Box {
    final int WIDTH = 800;
    final int HEIGHT = 600;

    private JTable table;
    private Vector<String> titles;
    private Vector<Vector> tableData;
    private TableModel tableModel;

    private JTextField searchField;
    private JComboBox<String> searchTypeComboBox;
    private JTextField pageNumberField;

    private BookSearch bookSearch;
    private DataBase dataBase = new DataBase();
    private DataBase.BorrowedBookManagement borrowedBookManagement = dataBase.new BorrowedBookManagement();
    private DataBase.UserListDB userListDB = dataBase.new UserListDB();
    private String currentUsername;

    public UserBookSerchComponent(String username) {
        super(BoxLayout.Y_AXIS);
        this.currentUsername = username;

        // 搜索面板
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout());

        searchField = new JTextField(20);
        String[] searchTypes = {"按编号", "按书名", "按作者", "按位置", "按入库日期范围"};
        searchTypeComboBox = new JComboBox<>(searchTypes);
        pageNumberField = new JTextField(5);
        pageNumberField.setText("1");

        JButton searchBtn = new JButton("搜索");
        JButton addBtn = new JButton("借阅");

        searchPanel.add(new JLabel("搜索内容:"));
        searchPanel.add(searchField);
        searchPanel.add(new JLabel("搜索类型:"));
        searchPanel.add(searchTypeComboBox);
        searchPanel.add(new JLabel("页码:"));
        searchPanel.add(pageNumberField);
        searchPanel.add(searchBtn);
        searchPanel.add(addBtn);

        this.add(searchPanel);

        // 使用 Box.createVerticalStrut 减少间距
        this.add(Box.createVerticalStrut(0));

        // 表格面板
        String[] ts = {"编号", "书名", "作者", "位置", "是否被借阅"};
        titles = new Vector<>();
        for (String title : ts) {
            titles.add(title);
        }

        tableData = new Vector<>();

        tableModel = new DefaultTableModel(tableData, titles);
        table = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(table);

        this.add(scrollPane);

        bookSearch = new BookSearch();

        searchBtn.addActionListener(e -> requestData());

        addBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int bookId = (int) table.getValueAt(selectedRow, 0);
                User user = userListDB.getUserByUserName(currentUsername);
                if (user != null) {
                    int userId = user.getUserId();
                    boolean success = borrowedBookManagement.borrowBook(bookId, userId);
                    if (success) {
                        JOptionPane.showMessageDialog(this, "借阅成功");
                        requestData(); // 刷新表格数据
                    } else {
                        JOptionPane.showMessageDialog(this, "借阅失败");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "未找到用户信息");
                }
            } else {
                JOptionPane.showMessageDialog(this, "请选择一本书");
            }
        });
    }

    public void requestData() {
        String searchText = searchField.getText();
        String searchType = (String) searchTypeComboBox.getSelectedItem();
        int pageNumber = 1;
        try {
            pageNumber = Integer.parseInt(pageNumberField.getText());
        } catch (NumberFormatException e) {
            // 处理无效页码输入
            JOptionPane.showMessageDialog(this, "请输入有效的页码", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<String> formattedBooks = null;
        switch (searchType) {
            case "按编号":
                try {
                    int bookId = Integer.parseInt(searchText);
                    formattedBooks = bookSearch.searchBooks("id", String.valueOf(bookId), null, null, pageNumber);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "请输入有效的编号", "错误", JOptionPane.ERROR_MESSAGE);
                }
                break;
            case "按书名":
                formattedBooks = bookSearch.searchBooks("name", searchText, null, null, pageNumber);
                break;
            case "按作者":
                formattedBooks = bookSearch.searchBooks("author", searchText, null, null, pageNumber);
                break;
            case "按位置":
                formattedBooks = bookSearch.searchBooks("location", searchText, null, null, pageNumber);
                break;
            case "按入库日期范围":
                String[] dates = searchText.split(" - ");
                if (dates.length == 2) {
                    try {
                        Timestamp startDate = Timestamp.valueOf(dates[0]);
                        Timestamp endDate = Timestamp.valueOf(dates[1]);
                        formattedBooks = bookSearch.searchBooks("time", "", startDate, endDate, pageNumber);
                    } catch (IllegalArgumentException e) {
                        JOptionPane.showMessageDialog(this, "请输入有效的日期范围（格式：yyyy-MM-dd HH:mm:ss - yyyy-MM-dd HH:mm:ss）", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "请输入有效的日期范围（格式：yyyy-MM-dd HH:mm:ss - yyyy-MM-dd HH:mm:ss）", "错误", JOptionPane.ERROR_MESSAGE);
                }
                break;
        }

        tableData.clear();

        if (formattedBooks != null) {
            for (String formattedBook : formattedBooks) {
                String[] parts = formattedBook.split("\n");
                Vector<Object> row = new Vector<>();
                for (String part : parts) {
                    String[] keyValue = part.split(": ");
                    if (keyValue.length == 2) {
                        if (keyValue[0].equals("ID")) {
                            row.add(Integer.parseInt(keyValue[1]));
                        } else if (keyValue[0].equals("入库时间")) {
                            row.add(Timestamp.valueOf(keyValue[1]));
                        } else {
                            row.add(keyValue[1]);
                        }
                    }
                }
                tableData.add(row);
            }
        }

        ((DefaultTableModel) tableModel).fireTableDataChanged();
    }
}