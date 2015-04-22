package com.java.ui.componentc;

import com.java.ui.util.UIResourceManager;
import com.java.ui.util.UIUtil;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicLabelUI;
import javax.swing.plaf.basic.BasicTextFieldUI;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.text.JTextComponent;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;
import java.util.EventObject;

public class CTreeUI extends BasicTreeUI {
    public class CTreeCellEditor extends DefaultTreeCellEditor {
        protected class EditorComponent extends
                DefaultTreeCellEditor.DefaultTextField {
            private static final long serialVersionUID = 4975204501015290943L;
            private final Color SELECTION_COLOR = UIResourceManager
                    .getColor("TextSelectionColor");

            public EditorComponent(Border border) {
                super(null);
                setUI(new BasicTextFieldUI());
                setBorder(border);
                setMargin(new Insets(0, 0, 0, 0));
                setSelectionColor(this.SELECTION_COLOR);
                setCaretColor(Color.BLACK);
                setOpaque(false);
            }

            @Deprecated
            public void updateUI() {
            }
        }

        public CTreeCellEditor(JTree tree, DefaultTreeCellRenderer renderer) {
            super(tree, renderer);
        }

        public CTreeCellEditor(JTree tree, DefaultTreeCellRenderer renderer,
                               TreeCellEditor editor) {
            super(tree, renderer, editor);
        }

        protected TreeCellEditor createTreeCellEditor() {
            Border EDITOR_BORDER = UIResourceManager
                    .getBorder("TreeEditorBorder");

            DefaultCellEditor editor = new DefaultCellEditor(
                    new EditorComponent(EDITOR_BORDER)) {
                private static final long serialVersionUID = -4245514780481293601L;

                public boolean shouldSelectCell(EventObject event) {
                    boolean retValue = super.shouldSelectCell(event);
                    return retValue;
                }
            };
            editor.setClickCountToStart(1);
            return editor;
        }

        public Component getTreeCellEditorComponent(JTree tree, Object value,
                                                    boolean isSelected, boolean expanded, boolean leaf, int row) {
            Component editorContainer = super.getTreeCellEditorComponent(tree,
                    value, isSelected, expanded, leaf, row);
            JComponent editorComponent = (JComponent) this.editingComponent;
            editorComponent.setFont(tree.getFont());
            editorComponent.setForeground(tree.getForeground());
            editorComponent.setBackground(tree.getBackground());

            if (((editorComponent instanceof JTextComponent))
                    && ((tree instanceof JCTree))) {
                ((JTextComponent) editorComponent)
                        .setSelectedTextColor(((JCTree) tree)
                                .getSelectionForeground());
            }

            return editorContainer;
        }
    }

    public static class CTreeCellRenderer extends DefaultTreeCellRenderer {
        private static final long serialVersionUID = -615681618460114673L;
        private final Image BG_IMAGE = UIResourceManager
                .getImage("SelectedItemBackgroundImage");

        private final Image BG_IMAGE_DISABLED = UIResourceManager
                .getImage("SelectedItemDisabledBackgroundImage");
        private JTree tree;

        public CTreeCellRenderer() {
            setUI(new BasicLabelUI() {
                protected void installDefaults(JLabel c) {
                }
            });
            setBorder(UIResourceManager.getBorder("TreeRendererBorder"));
            setOpaque(false);
            setOpenIcon(CTreeUI.NODE_ICON);
            setClosedIcon(CTreeUI.NODE_ICON);
            setLeafIcon(CTreeUI.NODE_ICON);
        }

        private int getSelectedBGStart() {
            if (getText() == null) {
                return -1;
            }

            Icon currentIcon = getIcon();
            return currentIcon == null ? 0 : currentIcon.getIconWidth()
                    + getInsets().left + Math.max(getIconTextGap() - 2, 1);
        }

        public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                      boolean sel, boolean expanded, boolean leaf, int row,
                                                      boolean hasFocus) {
            this.tree = tree;
            super.getTreeCellRendererComponent(tree, value, sel, expanded,
                    leaf, row, hasFocus);

            if ((tree instanceof JCTree)) {
                JCTree cTree = (JCTree) tree;
                Icon icon = null;

                if (leaf) {
                    icon = getLeafIcon();
                } else if (expanded) {
                    icon = getOpenIcon();
                } else {
                    icon = getClosedIcon();
                }

                if ((!cTree.isEnabled()) && (icon != null)) {
                    icon = new ImageIcon(UIUtil.toBufferedImage(
                            ((ImageIcon) icon).getImage(), 0.5F, this));
                }

                setEnabled(true);
                setFont(tree.getFont());
                setForeground(cTree.isEnabled() ? cTree.getForeground()
                        : sel ? cTree.getSelectionForeground() : cTree
                        .getDisabledForeground());
                setIconTextGap(sel ? 5 : 4);
                setIcon(icon);
            }

            return this;
        }

        public void paint(Graphics g) {
            int imageOffset = getSelectedBGStart();
            Rectangle rect = null;

            if ((this.selected) && (imageOffset >= 0)) {
                if (getComponentOrientation().isLeftToRight()) {
                    rect = new Rectangle(imageOffset, 0, getWidth()
                            - imageOffset, getHeight());
                } else {
                    rect = new Rectangle(0, 0, getWidth() - imageOffset,
                            getHeight());
                }

                Image image = (this.tree != null) && (!this.tree.isEnabled()) ? this.BG_IMAGE_DISABLED
                        : this.BG_IMAGE;
                UIUtil.paintImage(g, image, new Insets(1, 1, 1, 1), rect, this);
            }

            paintComponent(g);
        }

        @Deprecated
        public void updateUI() {
        }
    }

    private static final Color LINE_COLOR = UIResourceManager
            .getColor("TreeLineColor");

    private static final Icon NODE_ICON = UIResourceManager
            .getIcon("TreeNodeDefaultIcon");

    public static ComponentUI createUI(JComponent c) {
        return new CTreeUI();
    }

    protected TreeCellEditor createDefaultCellEditor() {
        if ((this.currentCellRenderer != null)
                && ((this.currentCellRenderer instanceof DefaultTreeCellRenderer))) {
            return new CTreeCellEditor(this.tree,
                    (DefaultTreeCellRenderer) this.currentCellRenderer);
        }

        return new CTreeCellEditor(this.tree, null);
    }

    protected TreeCellRenderer createDefaultCellRenderer() {
        return new CTreeCellRenderer();
    }

    protected void installDefaults() {
        this.largeModel = ((this.tree.isLargeModel()) && (this.tree
                .getRowHeight() > 0));
        setLeftChildIndent(4);
        setRightChildIndent(12);
    }

    private void paintBackground(Graphics g, JComponent c) {
        if ((c instanceof JCTree)) {
            JCTree tree = (JCTree) c;
            UIUtil.paintBackground(g, c, tree.getBackground(),
                    tree.getBackground(), tree.getImage(), tree.isImageOnly(),
                    tree.getAlpha(), tree.getVisibleInsets());
        }
    }

    protected void paintHorizontalLine(Graphics g, JComponent c, int y,
                                       int left, int right) {
        g.setColor(LINE_COLOR);
        drawDashedHorizontalLine(g, y, left, right);
    }

    protected void paintVerticalLine(Graphics g, JComponent c, int x, int top,
                                     int bottom) {
        g.setColor(LINE_COLOR);
        drawDashedVerticalLine(g, x, top, bottom);
    }

    public void update(Graphics g, JComponent c) {
        paintBackground(g, c);
        super.update(g, c);
    }
}