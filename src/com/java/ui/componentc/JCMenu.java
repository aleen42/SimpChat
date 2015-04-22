package com.java.ui.componentc;

import com.java.ui.util.UIUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.beans.PropertyChangeListener;

public class JCMenu extends JMenu {
    private static final long serialVersionUID = -4247570496135633786L;
    private Point customMenuLocation;
    private Icon disabledIcon;
    private Color disabledTextColor;
    private JPopupMenu popupMenu;
    private int preferredHeight;
    private Color selectedForeground;
    private boolean showWhenRollover;

    public JCMenu() {
        this("");
    }

    public JCMenu(Action a) {
        this();
        setAction(a);
    }

    public JCMenu(String s) {
        super(s);
        setUI(new CMenuUI());
        setOpaque(false);
        setFont(UIUtil.getDefaultFont());
        setForeground(new Color(0, 20, 35));
        setBackground(Color.GRAY);
        setIconTextGap(0);
        setRolloverEnabled(true);
        setBorderPainted(false);
        setBorder(new EmptyBorder(0, 0, 0, 0));
        setFocusPainted(false);
        setDelay(200);
        setMargin(new Insets(0, 0, 0, 0));
        this.selectedForeground = new Color(253, 253, 253);
        this.disabledTextColor = new Color(127, 137, 144);
        this.preferredHeight = 22;
        this.showWhenRollover = true;
    }

    public Component add(Component c) {
        ensurePopupMenuCreated();
        this.popupMenu.add(c);
        return c;
    }

    public Component add(Component c, int index) {
        ensurePopupMenuCreated();
        this.popupMenu.add(c, index);
        return c;
    }

    public JMenuItem add(JMenuItem menuItem) {
        ensurePopupMenuCreated();
        return this.popupMenu.add(menuItem);
    }

    public JMenuItem add(String s) {
        return add(new JCMenuItem(s));
    }

    public void addSeparator() {
        ensurePopupMenuCreated();
        this.popupMenu.addSeparator();
    }

    public void applyComponentOrientation(ComponentOrientation o) {
        super.applyComponentOrientation(o);

        if (this.popupMenu != null) {
            int ncomponents = getMenuComponentCount();

            for (int i = 0; i < ncomponents; i++) {
                getMenuComponent(i).applyComponentOrientation(o);
            }

            this.popupMenu.setComponentOrientation(o);
        }
    }

    protected JMenuItem createActionComponent(Action a) {
        JCMenuItem mi = new JCMenuItem() {
            private static final long serialVersionUID = -5940148878356449223L;

            protected PropertyChangeListener createActionPropertyChangeListener(
                    Action a) {
                PropertyChangeListener pcl = JCMenu.this
                        .createActionChangeListener(this);
                pcl = pcl == null ? super.createActionPropertyChangeListener(a)
                        : pcl;
                return pcl;
            }
        };
        return mi;
    }

    private void ensurePopupMenuCreated() {
        if (this.popupMenu == null) {
            this.popupMenu = new JCPopupMenu();
            this.popupMenu.setInvoker(this);
            this.popupListener = createWinListener(this.popupMenu);
        }
    }

    public Icon getDisabledIcon() {
        return this.disabledIcon;
    }

    public Color getDisabledTextColor() {
        return this.disabledTextColor;
    }

    public Component getMenuComponent(int n) {
        return this.popupMenu != null ? this.popupMenu.getComponent(n) : null;
    }

    public int getMenuComponentCount() {
        return this.popupMenu != null ? this.popupMenu.getComponentCount() : 0;
    }

    public Component[] getMenuComponents() {
        return this.popupMenu != null ? this.popupMenu.getComponents()
                : new Component[0];
    }

    public JPopupMenu getPopupMenu() {
        ensurePopupMenuCreated();
        return this.popupMenu;
    }

    protected Point getPopupMenuOrigin() {
        int x = 0;
        int y = 0;
        JPopupMenu popup = getPopupMenu();
        Dimension size = getSize();
        Dimension popupSize = popup.getSize();
        popupSize = popupSize.width == 0 ? popup.getPreferredSize() : popupSize;
        Point position = getLocationOnScreen();
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        GraphicsConfiguration gc = getGraphicsConfiguration();
        Rectangle screenBounds = new Rectangle(toolkit.getScreenSize());
        GraphicsDevice[] gds = GraphicsEnvironment
                .getLocalGraphicsEnvironment().getScreenDevices();
        Container parent = getParent();

        for (GraphicsDevice gd : gds) {
            if (gd.getType() != 0)
                continue;
            GraphicsConfiguration dgc = gd.getDefaultConfiguration();

            if (!dgc.getBounds().contains(position))
                continue;
            gc = dgc;
            break;
        }

        if (gc != null) {
            screenBounds = gc.getBounds();
            Insets screenInsets = toolkit.getScreenInsets(gc);
            screenBounds.width -= Math.abs(screenInsets.left
                    + screenInsets.right);
            screenBounds.height -= Math.abs(screenInsets.top
                    + screenInsets.bottom);
            position.x -= Math.abs(screenInsets.left);
            position.y -= Math.abs(screenInsets.top);
        }

        if ((parent instanceof JPopupMenu)) {
            int xOffset = 0;
            int yOffset = -popup.getInsets().top;
            x = size.width + xOffset;
            y = yOffset;

            if ((position.x + x + popupSize.width >= screenBounds.width
                    + screenBounds.x)
                    && (screenBounds.width - size.width < 2 * (position.x - screenBounds.x))) {
                x = -(xOffset + popupSize.width);
            }

            if ((position.y + y + popupSize.height >= screenBounds.height
                    + screenBounds.y)
                    && (screenBounds.height - size.height < 2 * (position.y - screenBounds.y))) {
                y = size.height - yOffset - popupSize.height;
            }
        } else {
            int xOffset = 0;
            int yOffset = 0;
            x = xOffset;
            y = size.height + yOffset;

            if ((position.x + x + popupSize.width >= screenBounds.width
                    + screenBounds.x)
                    && (screenBounds.width - size.width < 2 * (position.x - screenBounds.x))) {
                x = size.width - xOffset - popupSize.width;
            }

            if ((position.y + y + popupSize.height >= screenBounds.height)
                    && (screenBounds.height - size.height < 2 * (position.y - screenBounds.y))) {
                y = -(yOffset + popupSize.height);
            }
        }

        return new Point(x, y);
    }

    public int getPreferredHeight() {
        return this.preferredHeight;
    }

    public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize();

        if (this.preferredHeight > 0) {
            size.height = this.preferredHeight;
        }

        return size;
    }

    public Color getSelectedForeground() {
        return this.selectedForeground;
    }

    public MenuElement[] getSubElements() {
        return new MenuElement[]{this.popupMenu == null ? null
                : this.popupMenu};
    }

    public JMenuItem insert(Action a, int pos) {
        if (pos < 0) {
            throw new IllegalArgumentException("index less than zero.");
        }

        ensurePopupMenuCreated();
        JCMenuItem mi = new JCMenuItem(a);
        this.popupMenu.insert(mi, pos);
        return mi;
    }

    public JMenuItem insert(JMenuItem mi, int pos) {
        if (pos < 0) {
            throw new IllegalArgumentException("index less than zero.");
        }

        ensurePopupMenuCreated();
        this.popupMenu.insert(mi, pos);
        return mi;
    }

    public void insert(String s, int pos) {
        if (pos < 0) {
            throw new IllegalArgumentException("index less than zero.");
        }

        ensurePopupMenuCreated();
        this.popupMenu.insert(new JCMenuItem(s), pos);
    }

    public void insertSeparator(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("index less than zero.");
        }

        ensurePopupMenuCreated();
        this.popupMenu.insert(new JCPopupMenu.Separator(), index);
    }

    public boolean isPopupMenuVisible() {
        ensurePopupMenuCreated();
        return this.popupMenu.isVisible();
    }

    public boolean isShowWhenRollover() {
        return this.showWhenRollover;
    }

    public void remove(Component c) {
        if (this.popupMenu != null) {
            this.popupMenu.remove(c);
        }
    }

    public void remove(int pos) {
        if (pos < 0) {
            throw new IllegalArgumentException("index less than zero.");
        }

        if (pos > getItemCount()) {
            throw new IllegalArgumentException(
                    "index greater than the number of items.");
        }

        if (this.popupMenu != null) {
            this.popupMenu.remove(pos);
        }
    }

    public void remove(JMenuItem item) {
        if (this.popupMenu != null) {
            this.popupMenu.remove(item);
        }
    }

    public void removeAll() {
        if (this.popupMenu != null) {
            this.popupMenu.removeAll();
        }
    }

    public void setComponentOrientation(ComponentOrientation o) {
        super.setComponentOrientation(o);

        if (this.popupMenu != null) {
            this.popupMenu.setComponentOrientation(o);
        }
    }

    public void setDisabledIcon(Icon disabledIcon) {
        super.setDisabledIcon(disabledIcon);
        this.disabledIcon = disabledIcon;
    }

    public void setDisabledTextColor(Color disabledTextColor) {
        this.disabledTextColor = disabledTextColor;

        if (!isEnabled()) {
            repaint();
        }
    }

    public void setMenuLocation(int x, int y) {
        this.customMenuLocation = new Point(x, y);

        if (this.popupMenu != null) {
            this.popupMenu.setLocation(x, y);
        }
    }

    public void setPopupMenuVisible(boolean b) {
        if ((b != isPopupMenuVisible()) && ((isEnabled()) || (!b))
                && (getMenuComponentCount() > 0)) {
            ensurePopupMenuCreated();

            if ((b) && (isShowing())) {
                Point p = this.customMenuLocation;

                if (p == null) {
                    p = getPopupMenuOrigin();
                }

                getPopupMenu().show(this, p.x, p.y);
            } else {
                getPopupMenu().setVisible(false);
            }
        }
    }

    public void setPreferredHeight(int preferredHeight) {
        this.preferredHeight = preferredHeight;
        revalidate();
    }

    public void setSelectedForeground(Color selectedForeground) {
        this.selectedForeground = selectedForeground;
        repaint();
    }

    public void setShowWhenRollover(boolean showWhenRollover) {
        this.showWhenRollover = showWhenRollover;
    }

    @Deprecated
    public void updateUI() {
        if (this.popupMenu != null) {
            this.popupMenu.updateUI();
        }
    }
}