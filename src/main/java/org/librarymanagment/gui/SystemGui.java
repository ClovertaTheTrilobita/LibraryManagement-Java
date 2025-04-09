package org.librarymanagment.gui;

import org.librarymanagment.componen.UserBorrowManageComponent;
import org.librarymanagment.componen.UserBookSerchComponent;
import org.librarymanagment.componen.UserNameChangeListener;
import org.librarymanagment.componen.UserSpaceManageComponent;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class SystemGui implements UserNameChangeListener {
    JFrame jf = new JFrame();
    final int WIDTH = 900;
    final int HEIGHT = 600;
    private String userName;

    public SystemGui(String userName) {
        this.userName = userName;
        jf.setTitle("海大图书馆：" + userName + "，欢迎您");
    }

    public void init() throws Exception {
        jf.setIconImage(new ImageIcon(getClass().getResource("/images/library.png")).getImage());
        jf.setSize(WIDTH, HEIGHT);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setResizable(false);

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

        JSplitPane sp = new JSplitPane();
        sp.setContinuousLayout(true);
        sp.setDividerLocation(150);
        sp.setDividerSize(7);

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
        tree.setSelectionRow(2);
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                Object lastPathComponent = e.getNewLeadSelectionPath().getLastPathComponent();

                if (userSpace.equals(lastPathComponent)) {
                    sp.setRightComponent(new UserSpaceManageComponent(userName, SystemGui.this));
                    sp.setDividerLocation(150);
                }
                if (bookManage.equals(lastPathComponent)) {
                    sp.setRightComponent(new UserBookSerchComponent(userName));
                    sp.setDividerLocation(150);
                }
                if (borrowManage.equals(lastPathComponent)) {
                    sp.setRightComponent(new UserBorrowManageComponent(userName));
                    sp.setDividerLocation(150);
                }
                if (statisticsManage.equals(lastPathComponent)) {
                    sp.setRightComponent(new JLabel("统计分析您的喜好..."));
                    sp.setDividerLocation(150);
                }
            }
        });

        sp.setRightComponent(new UserBookSerchComponent(userName));
        sp.setLeftComponent(tree);
        jf.add(sp);

        jf.setLocationRelativeTo(null);
        jf.setVisible(true);
    }

    public static void main(String[] args) {
        try {
            new SystemGui("默认用户名").init();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUserNameChanged(String newUserName) {
        this.userName = newUserName;
        jf.setTitle("海大图书馆：" + newUserName + "，欢迎您");
    }

    private class MyRenderer extends DefaultTreeCellRenderer {
        private ImageIcon rootIcon = null;
        private ImageIcon userSpaceIcon = null;
        private ImageIcon bookManageIcon = null;
        private ImageIcon borrowManageIcon = null;
        private ImageIcon statisticsManageIcon = null;

        public MyRenderer() {
            rootIcon = new ImageIcon((getClass().getResource("/images/root.png").getPath()));
            userSpaceIcon = new ImageIcon((getClass().getResource("/images/userSpace.png").getPath()));
            bookManageIcon = new ImageIcon((getClass().getResource("/images/bookManage.png").getPath()));
            borrowManageIcon = new ImageIcon((getClass().getResource("/images/borrowManage.png").getPath()));
            statisticsManageIcon = new ImageIcon((getClass().getResource("/images/statisticsManage.png").getPath()));
        }

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

            ImageIcon image = null;
            switch (row) {
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