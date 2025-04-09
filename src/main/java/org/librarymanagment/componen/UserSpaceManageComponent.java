package org.librarymanagment.componen;

import org.librarymanagment.userMethod.UserSpace;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.Vector;

public class UserSpaceManageComponent extends Box {
    final int WIDTH = 800;
    final int HEIGHT = 600;
    private String userName;
    private UserSpace userSpace;
    private UserNameChangeListener listener;

    private JTable table;
    private Vector<String> titles;
    private Vector<Vector> tableData;
    private TableModel tableModel;
    private boolean isEditable = false;
    private Vector<Vector> originalData;
    private int selectedRow = -1;

    public UserSpaceManageComponent(String userName, UserNameChangeListener listener) {
        super(BoxLayout.Y_AXIS);
        this.userName = userName;
        this.listener = listener;
        userSpace = new UserSpace(userName);

        JPanel btnPanel = new JPanel();
        btnPanel.setMaximumSize(new Dimension(WIDTH, 40));
        btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JButton updateBtn = new JButton("修改");
        updateBtn.addActionListener(e -> {
            if (selectedRow != -1) {
                isEditable = true;
                originalData = new Vector<>(tableData);
                String field = (String) tableModel.getValueAt(selectedRow, 0);
                String currentValue = (String) tableModel.getValueAt(selectedRow, 1);
                String newValue = JOptionPane.showInputDialog(null, "请输入修改后的 " + field + " 数据", currentValue);
                if (newValue != null) {
                    tableModel.setValueAt(newValue, selectedRow, 1);
                    Vector<Vector> singleRowData = new Vector<>();
                    singleRowData.add(tableData.get(selectedRow));
                    boolean updateSuccess = userSpace.update(singleRowData);
                    if (updateSuccess) {
                        JOptionPane.showMessageDialog(null, "修改成功！");
                        if ("用户名".equals(field)) {
                            // 通知监听器用户名已更改
                            if (listener != null) {
                                listener.onUserNameChanged(userSpace.getUserName());
                            }
                        }
                        requestData();
                    } else {
                        tableData = new Vector<>(originalData);
                        ((DefaultTableModel) tableModel).setDataVector(tableData, titles);
                        ((DefaultTableModel) tableModel).fireTableDataChanged();
                        JOptionPane.showMessageDialog(null, "数据更新失败，请重试。");
                    }
                }
                isEditable = false;
            } else {
                JOptionPane.showMessageDialog(null, "请先选中一行数据");
            }
        });

        btnPanel.add(updateBtn);
        this.add(btnPanel);

        titles = new Vector<>();
        titles.add("信息类别");
        titles.add("用户信息");

        tableData = new Vector<>();

        tableModel = new DefaultTableModel(tableData, titles) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1 && isEditable && row == selectedRow;
            }
        };

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectedRow = table.getSelectedRow();
            }
        });

        TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();
        Component headerComponent = headerRenderer.getTableCellRendererComponent(table, "", false, false, 0, 0);
        Color headerBackground = headerComponent.getBackground();

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(headerBackground);
                }
                return c;
            }
        };

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }

        tableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    int row = e.getFirstRow();
                    int column = e.getColumn();
                    if (column == 1 && isEditable) {
                        String field = (String) tableModel.getValueAt(row, 0);
                        String newValue = (String) tableModel.getValueAt(row, 1);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        this.add(scrollPane);

        requestData();
    }

    public void requestData() {
        Vector<Vector> userData = userSpace.getUser();
        if (userData != null && !userData.isEmpty()) {
            tableData.clear();
            Vector<String> user = userData.get(0);
            Vector<String> usernameRow = new Vector<>();
            usernameRow.add("用户名");
            usernameRow.add(user.get(0));
            tableData.add(usernameRow);

            Vector<String> passwordRow = new Vector<>();
            passwordRow.add("密码");
            passwordRow.add(user.get(1));
            tableData.add(passwordRow);

            Vector<String> emailRow = new Vector<>();
            emailRow.add("邮箱");
            emailRow.add(user.get(2));
            tableData.add(emailRow);

            Vector<String> phoneRow = new Vector<>();
            phoneRow.add("手机号");
            phoneRow.add(user.get(3));
            tableData.add(phoneRow);

            DefaultTableModel dtm = (DefaultTableModel) tableModel;
            dtm.setDataVector(tableData, titles);
            dtm.fireTableDataChanged();
        }
    }

    private void updateUserInfo(String field, String newValue) {
        userSpace.update(field, newValue);
    }
}