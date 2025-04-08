package org.librarymanagment.gui;

import org.librarymanagment.componen.UserBorrowManageComponent;
import org.librarymanagment.componen.UserBookSerchComponent;
import org.librarymanagment.componen.UserSpaceManageComponent;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class SystemGui {
    JFrame jf = new JFrame();
    final int WIDTH = 900;
    final int HEIGHT = 800;

    // 构造函数，接收用户名并设置窗口标题
    public SystemGui(String userName) {
        jf.setTitle("海大图书馆：" + userName + "，欢迎您");
    }

    //组装视图
    public void init() throws Exception {
        //由此插入图标
        jf.setIconImage(new ImageIcon(getClass().getResource("/images/library.png")).getImage());
        jf.setSize(WIDTH, HEIGHT);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //设置菜单栏
        JMenuBar jmb = new JMenuBar();
        JMenu jMenu = new JMenu("设置");
        JMenuItem m1 = new JMenuItem("切换账号");
        JMenuItem m2 = new JMenuItem("退出系统");
        m1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new UserLogin().init();
                    jf.dispose();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
        m2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        jMenu.add(m1);
        jMenu.add(m2);
        jmb.add(jMenu);
        jf.setJMenuBar(jmb);

        //设置分割面板
        JSplitPane sp = new JSplitPane();

        //支持连续布局
        sp.setContinuousLayout(true);
        sp.setDividerLocation(150);
        sp.setDividerSize(7);

        //设置左侧内容
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("系统管理");
        DefaultMutableTreeNode userSpace = new DefaultMutableTreeNode("用户空间");
        DefaultMutableTreeNode bookManage = new DefaultMutableTreeNode("搜索图书");
        DefaultMutableTreeNode borrowManage = new DefaultMutableTreeNode("借阅管理");
        DefaultMutableTreeNode statisticsManage = new DefaultMutableTreeNode("统计分析");

        root.add(userSpace);
        root.add(bookManage);
        root.add(borrowManage);
        root.add(statisticsManage);

        Color color = new Color(203, 220, 217);
        JTree tree = new JTree(root);
        MyRenderer myRenderer = new MyRenderer();
        myRenderer.setBackgroundNonSelectionColor(color);
        myRenderer.setBackgroundSelectionColor(new Color(140, 140, 140));
        tree.setCellRenderer(myRenderer);

        tree.setBackground(color);

        //设置当前tree默认选中图书管理
        tree.setSelectionRow(2);
        tree.addTreeSelectionListener(new TreeSelectionListener() {

            //当条目选中变化后，这个方法会执行
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                //得到当前选中的节点对象
                Object lastPathComponent = e.getNewLeadSelectionPath().getLastPathComponent();

                if(userSpace.equals(lastPathComponent)) {
                    sp.setRightComponent(new UserSpaceManageComponent());
                    sp.setDividerLocation(150);
                }
                if(bookManage.equals(lastPathComponent)) {
                    sp.setRightComponent(new UserBookSerchComponent());
                    sp.setDividerLocation(150);
                }
                if(borrowManage.equals(lastPathComponent)) {
                    sp.setRightComponent(new UserBorrowManageComponent());
                    sp.setDividerLocation(150);
                }
                if(statisticsManage.equals(lastPathComponent)) {
                    sp.setRightComponent(new JLabel("统计分析您的喜好..."));
                    sp.setDividerLocation(150);

                }

            }
        });






        sp.setRightComponent(new UserBookSerchComponent());

        sp.setLeftComponent(tree);
        jf.add(sp);


        jf.setLocationRelativeTo(null);
        jf.setVisible(true);

        //获取登录用户的用户名

    }

    public static void main(String[] args) {
        try {
            new SystemGui("默认用户名").init(); // 这里可以传入默认用户名，实际使用中会从登录页面获取
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private class MyRenderer extends DefaultTreeCellRenderer {
        private ImageIcon rootIcon = null;
        private ImageIcon userSpaceIcon = null;
        private ImageIcon bookManageIcon = null;
        private ImageIcon borrowManageIcon = null;
        private ImageIcon statisticsManageIcon = null;

        public MyRenderer () {
            rootIcon = new ImageIcon((getClass().getResource("/images/root.png").getPath()));
            userSpaceIcon = new ImageIcon((getClass().getResource("/images/userSpace.png").getPath()));
            bookManageIcon = new ImageIcon((getClass().getResource("/images/bookManage.png").getPath()));
            borrowManageIcon = new ImageIcon((getClass().getResource("/images/borrowManage.png").getPath()));
            statisticsManageIcon = new ImageIcon((getClass().getResource("/images/statisticsManage.png").getPath()));
        }
        //当绘制树的每个节点时，都会调用这个方法
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

            ImageIcon image = null;
            switch(row){
                case 0:
                    image = rootIcon;
                    break;
                case 1:
                    image = userSpaceIcon;
                    break;
                case 2:
                    image = bookManageIcon;
                    break;
                case 3:
                    image = borrowManageIcon;
                    break;
                case 4:
                    image = statisticsManageIcon;
                    break;
            }

            this.setIcon(image);
            return this;

        }
    }
}