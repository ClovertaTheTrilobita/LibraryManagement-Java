package org.librarymanagment.componen;

import org.librarymanagment.userMethod.BookBorrow;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.Vector;

public class UserBorrowManageComponent extends Box {
    final int WIDTH = 800;
    final int HEIGHT = 600;

    private JTable table;
    private Vector<String> titles;
    private Vector<Vector> tableData;
    private DefaultTableModel tableModel;

    private BookBorrow bookBorrow;
    private String userName;

    public UserBorrowManageComponent(String userName) {
        super(BoxLayout.Y_AXIS);
        this.userName = userName;
        bookBorrow = new BookBorrow();

        // 组装视图
        JPanel btnPanel = new JPanel();
        btnPanel.setMaximumSize(new Dimension(WIDTH, 40));
        btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JButton returnBtn = new JButton("还书");
        returnBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    int bookId = (int) tableData.get(selectedRow).get(0);
                    boolean success = bookBorrow.returnBook(userName, bookId);
                    if (success) {
                        JOptionPane.showMessageDialog(null, "还书成功");
                        requestData();
                    } else {
                        JOptionPane.showMessageDialog(null, "还书失败");
                    }
                }
            }
        });

        btnPanel.add(returnBtn);
        this.add(btnPanel);

        // 组装表格
        String[] ts = {"书籍ID", "书名", "借阅时间", "还书时间", "借阅状态"};
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

        JScrollPane scrollPane = new JScrollPane(table);
        this.add(scrollPane);

        requestData();
    }

    public void requestData() {
        tableData.clear();
        tableData.addAll(bookBorrow.getBorrowedBooks(userName));
        tableModel.fireTableDataChanged();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("用户借阅管理");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(800, 600);
                frame.add(new UserBorrowManageComponent("testUser"));
                frame.setVisible(true);
            }
        });
    }
}