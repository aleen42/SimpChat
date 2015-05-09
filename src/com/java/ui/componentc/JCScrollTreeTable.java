package com.java.ui.componentc;

import com.java.ui.component.treetable.CommonTreeTableModel;
import com.java.ui.componentc.treetable.JCTreeTable;

import javax.swing.tree.DefaultMutableTreeNode;

public class JCScrollTreeTable extends JCScrollTable {
    private static final long serialVersionUID = 6786205172777947771L;
    private JCTreeTable treeTable;

    public JCScrollTreeTable(CommonTreeTableModel model) {
        this(new JCTreeTable(model));
    }

    public JCScrollTreeTable(DefaultMutableTreeNode root, String[] columnsName,
                             Class<?>[] columnsClass, String[] getMethodsName,
                             String[] setMethodsName) {
        this(new JCTreeTable(root, columnsName, columnsClass, getMethodsName,
                setMethodsName));
    }

    public JCScrollTreeTable(DefaultMutableTreeNode root, String[] columnsName,
                             Class<?>[] columnsClass, String[] getMethodsName,
                             String[] setMethodsName, boolean asksAllowsChildren) {
        this(new JCTreeTable(root, columnsName, columnsClass, getMethodsName,
                setMethodsName, asksAllowsChildren));
    }

    public JCScrollTreeTable(JCTreeTable treeTable) {
        super(treeTable);
        this.treeTable = treeTable;
    }

    public JCTreeTable getTreeTable() {
        return this.treeTable;
    }
}