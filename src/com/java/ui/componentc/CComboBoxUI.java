package com.java.ui.componentc;

import com.java.ui.util.UIResourceManager;
import com.java.ui.util.UIUtil;
import sun.swing.DefaultLookup;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class CComboBoxUI extends BasicComboBoxUI {
    private static class BorderlessTextField extends JTextField {
        private static final long serialVersionUID = -330778040758821755L;

        public BorderlessTextField(String value, int n) {
            super(n);
            setUI(new BasicTextFieldUI());
        }

        public void setBorder(Border b) {
            if (!(b instanceof BasicComboBoxEditor.UIResource)) {
                super.setBorder(b);
            }
        }

        public void setText(String s) {
            if (getText().equals(s)) {
                return;
            }

            super.setText(s);
            setSelectionStart(0);
            setSelectionEnd(0);
        }

        @Deprecated
        public void updateUI() {
        }
    }

    private class CComboBoxEditor extends BasicComboBoxEditor.UIResource {
        private CComboBoxEditor() {
        }

        protected JTextField createEditorComponent() {
            JTextField editor = new CComboBoxUI.BorderlessTextField("", 9);
            editor.setBorder(null);
            return editor;
        }
    }

    private class CComboPopup extends BasicComboPopup {
        private static final long serialVersionUID = 7897660999228190162L;
        private JComboBox combo;
        private final Border VIEWPORT_BORDER = new EmptyBorder(0, 0, 0, 1);

        public CComboPopup(JComboBox combo) {
            super(combo);
            setUI(new BasicPopupMenuUI() {
                public void installDefaults() {
                }

                protected void uninstallDefaults() {
                }
            });
            this.combo = combo;
            setBorder(new EmptyBorder(0, 0, 0, 0));
            setOpaque(false);
        }

        protected void configureList() {
            super.configureList();
            this.list.setOpaque(false);
        }

        protected void configureScroller() {
            super.configureScroller();
            this.scroller.getVerticalScrollBar().setOpaque(false);
            this.scroller.getViewport().setOpaque(false);
            this.scroller.setOpaque(false);
            this.scroller.setBorder(UIResourceManager
                    .getBorder("ComboBoxPopupBorder"));
        }

        protected JList createList() {
            JList list = new JList(this.comboBox.getModel()) {
                private static final long serialVersionUID = -3870496246576971437L;

                public void processMouseEvent(MouseEvent e) {
                    if ((e.getModifiers() & Toolkit.getDefaultToolkit()
                            .getMenuShortcutKeyMask()) != 0) {
                        Toolkit toolkit = Toolkit.getDefaultToolkit();
                        e = new MouseEvent((Component) e.getSource(),
                                e.getID(), e.getWhen(), e.getModifiers()
                                ^ toolkit.getMenuShortcutKeyMask(),
                                e.getX(), e.getY(), e.getXOnScreen(),
                                e.getYOnScreen(), e.getClickCount(),
                                e.isPopupTrigger(), 0);
                    }

                    super.processMouseEvent(e);
                }

                @Deprecated
                public void updateUI() {
                }
            };
            list.setUI(new BasicListUI() {
                protected void installDefaults() {
                    this.list.setLayout(null);
                }

                protected void uninstallDefaults() {
                    if ((this.list.getTransferHandler() instanceof UIResource)) {
                        this.list.setTransferHandler(null);
                    }
                }
            });
            return list;
        }

        protected JScrollPane createScroller() {
            JScrollPane sp = new JScrollPane(this.list, 20, 31) {
                private static final long serialVersionUID = -2214894633749404512L;

                public JScrollBar createVerticalScrollBar() {
                    JScrollPane.ScrollBar vBar = new JScrollPane.ScrollBar(1) {
                        private static final long serialVersionUID = 397439195886258194L;

                        public void updateUI() {
                        }
                    };
                    vBar.setUI(new CScrollBarUI());
                    vBar.setBorder(null);
                    return vBar;
                }

                @Deprecated
                public void updateUI() {
                }
            };
            sp.setUI(new BasicScrollPaneUI() {
                protected void installDefaults(JScrollPane scrollpane) {
                }

                protected void uninstallDefaults(JScrollPane scrollpane) {
                }

                public void update(Graphics g, JComponent c) {
                    g.setColor(c.getBackground());
                    g.fillRect(0, 0, c.getWidth(), c.getHeight());
                    paint(g, c);
                }
            });
            sp.setHorizontalScrollBar(null);
            return sp;
        }

        public void setVisible(boolean visible) {
            super.setVisible(visible);

            if (visible) {
                if ((this.combo instanceof JCComboBox)) {
                    JCComboBox cbox = (JCComboBox) this.combo;
                    float alpha = cbox.isImageOnly() ? 0.0F : cbox.getAlpha();
                    Color oldBg = cbox.getBackground();
                    this.scroller.setBackground(new Color(oldBg.getRed(), oldBg
                            .getGreen(), oldBg.getBlue(), Math
                            .round(255.0F * alpha)));
                } else {
                    this.scroller.setBackground(this.combo.getBackground());
                }

                this.scroller
                        .setViewportBorder(this.scroller.getVerticalScrollBar()
                                .isVisible() ? this.VIEWPORT_BORDER : null);
            } else if ((this.combo instanceof JCComboBox)) {
                ((JCComboBox) this.combo).resetBorder();
            }
        }

        public void show() {
            if (((this.combo instanceof JCComboBox))
                    && (!((JCComboBox) this.combo).isEditableAll())) {
                return;
            }

            super.show();
        }

        @Deprecated
        public void updateUI() {
            setUI(getUI());
        }
    }

    private static final Color DISABLED_BG = UIResourceManager
            .getColor("TextDisabledBackground");
    private static final Color NON_EDITABLE_BG = UIResourceManager
            .getColor("TextNonEditableBackground");
    private static final Image RENDERER_BORDER_DISABLED_IMAGE = UIResourceManager
            .getImage("ComboBoxRendererBorderDisabledImage");

    private static final Image RENDERER_BORDER_IMAGE = UIResourceManager
            .getImage("ComboBoxRendererBorderImage");

    public static ComponentUI createUI(JComponent c) {
        return new CComboBoxUI();
    }

    protected Image buttonImage;

    protected Image buttonRolloverImage;

    public void changeButtonBorder(boolean mouseIn) {
        ((JCButton) this.arrowButton)
                .setImage(mouseIn ? this.buttonRolloverImage : this.buttonImage);
    }

    public void configureArrowButton() {
        super.configureArrowButton();

        if (this.arrowButton != null) {
            this.arrowButton.setFocusable(false);
        }
    }

    protected JButton createArrowButton() {
        JCButton button = new JCButton();
        button.setName("ComboBox.arrowButton");
        button.setImage(this.buttonImage = UIResourceManager
                .getImage("ComboBoxButtonImage"));
        button.setDisabledImage(button.getImage());
        button.setRolloverImage(this.buttonRolloverImage = UIResourceManager
                .getImage("ComboBoxButtonRolloverImage"));
        button.setPressedImage(UIResourceManager
                .getImage("ComboBoxButtonPressedImage"));
        button.setIcon(UIResourceManager.getIcon("ComboBoxButtonIcon"));
        button.setDisabledIcon(UIResourceManager
                .getIcon("ComboBoxButtonDisabledIcon"));
        button.setImageInsets(1, 2, 1, 1);
        return button;
    }

    protected ComboBoxEditor createEditor() {
        ComboBoxEditor editor = new CComboBoxEditor();
        JTextField field = (JTextField) editor.getEditorComponent();
        field.setBorder(UIResourceManager.getBorder("ComboBoxEditorBorder"));
        field.setSelectionColor(UIResourceManager
                .getColor("TextSelectionColor"));
        field.setSelectedTextColor(UIResourceManager
                .getColor("TextSelectionForeground"));
        field.setOpaque(false);
        field.setBackground(UIResourceManager.getWhiteColor());
        field.setForeground(Color.BLACK);
        field.setCaretColor(Color.BLACK);
        field.setMargin(new Insets(0, 0, 0, 0));
        field.setFont(UIUtil.getDefaultFont());

        if ((this.comboBox instanceof JCComboBox)) {
            field.setDisabledTextColor(((JCComboBox) this.comboBox)
                    .getDisabledTextColor());
        }

        return editor;
    }

    protected ComboPopup createPopup() {
        return new CComboPopup(this.comboBox);
    }

    public JButton getArrowButton() {
        return this.arrowButton;
    }

    public ComboPopup getPopup() {
        return this.popup;
    }

    protected void installDefaults() {
    }

    public void paint(Graphics g, JComponent c) {
        this.hasFocus = this.comboBox.hasFocus();
        Rectangle r = rectangleForCurrentValue();
        paintCurrentValueBackground(g, r, this.hasFocus);

        if (!this.comboBox.isEditable()) {
            paintCurrentValue(g, r, this.hasFocus);
        }
    }

    public void paintCurrentValue(Graphics g, Rectangle bounds, boolean hasFocus) {
        ListCellRenderer renderer = this.comboBox.getRenderer();
        Component c;

        if ((hasFocus) && (!isPopupVisible(this.comboBox))) {
            c = renderer.getListCellRendererComponent(this.listBox,
                    this.comboBox.getSelectedItem(), -1, true, false);
        } else {
            c = renderer.getListCellRendererComponent(this.listBox,
                    this.comboBox.getSelectedItem(), -1, false, false);
        }

        ((JComponent) c).setOpaque(false);
        ((JComponent) c).setBorder(CComboBoxRenderer.SELECTED_BORDER);
        c.setFont(this.comboBox.getFont());

        if ((this.comboBox instanceof JCComboBox)) {
            c.setForeground(this.comboBox.isEnabled() ? this.comboBox
                    .getForeground() : ((JCComboBox) this.comboBox)
                    .getDisabledTextColor());
        } else {
            c.setForeground(this.comboBox.isEnabled() ? this.comboBox
                    .getForeground() : DefaultLookup.getColor(this.comboBox,
                    this, "ComboBox.disabledForeground", null));
        }

        this.currentValuePane.paintComponent(g, c, this.comboBox, bounds.x,
                bounds.y, bounds.width, bounds.height, c instanceof JPanel);
    }

    public void paintCurrentValueBackground(Graphics g, Rectangle bounds,
                                            boolean hasFocus) {
        if ((this.comboBox instanceof JCComboBox)) {
            JCComboBox ccombox = (JCComboBox) this.comboBox;
            float alpha = ccombox.getAlpha();
            Image image = ccombox.getImage();
            boolean imageOnly = ccombox.isImageOnly();

            if ((alpha > 0.0D) && ((!imageOnly) || (image != null))) {
                UIUtil.paintBackground(g, ccombox,
                        ccombox.isEditableAll() ? ccombox.getBackground()
                                : NON_EDITABLE_BG, DISABLED_BG, image,
                        imageOnly, alpha, ccombox.getVisibleInsets());

                if ((!ccombox.isEnabled()) || (ccombox.isEditableAll())) {
                    paintRendererBorder(g, bounds);
                }
            }
        } else {
            super.paintCurrentValueBackground(g, bounds, hasFocus);
        }
    }

    private void paintRendererBorder(Graphics g, Rectangle bounds) {
        Image image = this.comboBox.isEnabled() ? RENDERER_BORDER_IMAGE
                : RENDERER_BORDER_DISABLED_IMAGE;
        Insets imageInsets = new Insets(2, 2, 0, 0);
        Rectangle paintRect = new Rectangle(bounds.x, bounds.y, bounds.width
                + this.arrowButton.getWidth(), bounds.height);
        UIUtil.paintImage(g, image, imageInsets, paintRect, this.comboBox);
    }

    protected void selectNextPossibleValue() {
        if (((this.comboBox instanceof JCComboBox))
                && (!((JCComboBox) this.comboBox).isEditableAll())) {
            return;
        }

        super.selectNextPossibleValue();
    }

    protected void selectPreviousPossibleValue() {
        if (((this.comboBox instanceof JCComboBox))
                && (!((JCComboBox) this.comboBox).isEditableAll())) {
            return;
        }

        super.selectPreviousPossibleValue();
    }

    protected void uninstallDefaults() {
    }
}