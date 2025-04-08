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

    private JTable table;
    private Vector<String> titles;
    private Vector<Vector> tableData;
    private TableModel tableModel;
    private boolean isEditable = false; // 控制表格是否可编辑的标志位
    private Vector<Vector> originalData; // 保存原始数据，用于在更新失败时恢复
    private int selectedRow = -1; // 保存选中的行

    public UserSpaceManageComponent() {
        // 垂直布局
        super(BoxLayout.Y_AXIS);
        // 组装视图

        JPanel btnPanel = new JPanel();
        // 设置面板的首选大小
        btnPanel.setMaximumSize(new Dimension(WIDTH, 40));
        btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JButton updateBtn = new JButton("修改");
        // 为修改按钮添加点击事件监听器
        updateBtn.addActionListener(e -> {
            if (selectedRow != -1) {
                isEditable = true;
                originalData = new Vector<>(tableData); // 保存原始数据
                String field = (String) tableModel.getValueAt(selectedRow, 0);
                String currentValue = (String) tableModel.getValueAt(selectedRow, 1);
                String newValue = JOptionPane.showInputDialog(null, "请输入修改后的 " + field + " 数据", currentValue);
                if (newValue != null) {
                    tableModel.setValueAt(newValue, selectedRow, 1);
                    updateUserInfo(field, newValue);
                    UserSpace userSpace = new UserSpace();
                    boolean updateSuccess = userSpace.update(tableData); // 调用更新方法并获取更新结果
                    if (updateSuccess) {
                        requestData(); // 更新成功，重新获取数据并展示
                    } else {
                        // 更新失败，恢复原始数据
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

        // 组装表格
        titles = new Vector<>();
        titles.add("信息类别");
        titles.add("用户信息");

        tableData = new Vector<>();

        tableModel = new DefaultTableModel(tableData, titles) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // 只允许编辑“用户信息”列且在可编辑状态下，同时是选中的行
                return column == 1 && isEditable && row == selectedRow;
            }
        };

        // 禁止编辑
        table = new JTable(tableModel);
        // 设置只能选中一行
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectedRow = table.getSelectedRow();
            }
        });

        // 获取列标题的渲染器
        TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();
        Component headerComponent = headerRenderer.getTableCellRendererComponent(table, "", false, false, 0, 0);
        Color headerBackground = headerComponent.getBackground();

        // 自定义单元格渲染器，设置行背景色和列标题背景色相同
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

        // 设置表格所有列的渲染器
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }

        // 添加表格模型监听器
        tableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    int row = e.getFirstRow();
                    int column = e.getColumn();
                    if (column == 1 && isEditable) {
                        String field = (String) tableModel.getValueAt(row, 0);
                        String newValue = (String) tableModel.getValueAt(row, 1);
                        updateUserInfo(field, newValue);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);

        this.add(scrollPane);

        // 调用方法展示数据
        requestData();
    }

    public void requestData() {
        UserSpace userSpace = new UserSpace();
        // 假设 getUser 方法返回一个包含用户信息的 Vector
        Vector<Vector> userData = userSpace.getUser();
        if (userData != null && !userData.isEmpty()) {
            // 清空原表格数据
            tableData.clear();

            Vector<String> user = userData.get(0);
            // 添加用户名行
            Vector<String> usernameRow = new Vector<>();
            usernameRow.add("用户名");
            usernameRow.add(user.get(0));
            tableData.add(usernameRow);

            // 添加密码行
            Vector<String> passwordRow = new Vector<>();
            passwordRow.add("密码");
            passwordRow.add(user.get(1));
            tableData.add(passwordRow);

            // 添加邮箱行
            Vector<String> emailRow = new Vector<>();
            emailRow.add("邮箱");
            emailRow.add(user.get(2));
            tableData.add(emailRow);

            // 添加手机号行
            Vector<String> phoneRow = new Vector<>();
            phoneRow.add("手机号");
            phoneRow.add(user.get(3));
            tableData.add(phoneRow);

            // 更新表格模型
            DefaultTableModel dtm = (DefaultTableModel) tableModel;
            dtm.setDataVector(tableData, titles);
            dtm.fireTableDataChanged();
        }
    }

    private void updateUserInfo(String field, String newValue) {
        UserSpace userSpace = new UserSpace();
        // 调用 UserSpace 类的 update 方法，传递修改的字段和新值
        userSpace.update(field, newValue);
    }
}