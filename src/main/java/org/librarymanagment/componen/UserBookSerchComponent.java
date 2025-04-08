package org.librarymanagment.componen;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.Vector;

public class UserBookSerchComponent extends Box {
    final int WIDTH = 800;
    final int HEIGHT = 600;

    private JTable table;
    private Vector<String> titles;
    private Vector<Vector> tableData;
    private TableModel tableModel;


    public UserBookSerchComponent() {
        //垂直布局
        super(BoxLayout.Y_AXIS);
        //组装视图

        JPanel btnPanel = new JPanel();
        //设置面板的首选大小
        btnPanel.setMaximumSize(new Dimension(WIDTH,40));
        btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JButton addBtn = new JButton("借阅");
        JButton searchBtn = new JButton("搜索");

        btnPanel.add(addBtn);
        btnPanel.add(searchBtn);

        this.add(btnPanel);

        //组装表格
        String[] ts = {"编号","书名","作者","所在地","入库时间"};
        titles = new Vector<>();
        for (String title : ts){
            titles.add(title);
        }

        tableData = new Vector<>();


        tableModel = new DefaultTableModel(tableData,titles);
        //禁止编辑
        table = new JTable(tableModel){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        //设置只能选中一行
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // 禁止表头拖动
        table.getTableHeader().setReorderingAllowed(false);


        JScrollPane scrollPane = new JScrollPane(table);

        this.add(scrollPane);


    }

    public void requestData(){
        //获取数据
        //设置表格数据
    }
}
