package com.java.ui.componentc;

import javax.swing.*;
import java.util.Vector;

public class JCScrollCheckBoxList extends JCScrollList {
    private static final long serialVersionUID = -6886223334688958453L;

    public JCScrollCheckBoxList() {
        this(new JCCheckBoxList());
    }

    public JCScrollCheckBoxList(JCCheckBoxList list) {
        super(list);
    }

    public JCScrollCheckBoxList(ListModel dataModel) {
        this(new JCCheckBoxList(dataModel));
    }

    public JCScrollCheckBoxList(Vector<?> listData) {
        this(new JCCheckBoxList(listData));
    }

    public JCCheckBoxList getList() {
        return (JCCheckBoxList) this.list;
    }
}