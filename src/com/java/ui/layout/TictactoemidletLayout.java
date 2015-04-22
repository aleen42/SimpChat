package com.java.ui.layout;

import java.awt.*;
import java.io.Serializable;

public class TictactoemidletLayout implements LayoutManager2, Serializable {
    public static final String CENTER = "Center";
    public static final String EAST = "East";
    public static final String NORTH = "North";
    public static final String NORTH_EAST = "North-East";
    public static final String NORTH_WEST = "North-West";
    private static final long serialVersionUID = 8146653535954456852L;
    public static final String SOUTH = "South";
    public static final String SOUTH_EAST = "South-East";
    public static final String SOUTH_WEST = "South-West";
    public static final String WEST = "West";
    private Component center;
    private Component east;
    private int hgap;
    private Component north;
    private Component northEast;
    private Component northWest;
    private Component south;
    private Component southEast;
    private Component southWest;
    private int vgap;
    private Component west;

    public TictactoemidletLayout() {
        this(0, 0);
    }

    public TictactoemidletLayout(int hgap, int vgap) {
        this.hgap = hgap;
        this.vgap = vgap;
    }

    public void addLayoutComponent(Component comp, Object constraints) {
        synchronized (comp.getTreeLock()) {
            if ((constraints == null) || ((constraints instanceof String))) {
                addLayoutComponent((String) constraints, comp);
            } else {
                throw new IllegalArgumentException(
                        "cannot add to layout: constraint must be a string (or null)");
            }
        }
    }

    @Deprecated
    public void addLayoutComponent(String name, Component comp) {
        synchronized (comp.getTreeLock()) {
            if (name == null) {
                name = "Center";
            }

            if ("Center".equals(name)) {
                this.center = comp;
            } else if ("North".equals(name)) {
                this.north = comp;
            } else if ("South".equals(name)) {
                this.south = comp;
            } else if ("East".equals(name)) {
                this.east = comp;
            } else if ("West".equals(name)) {
                this.west = comp;
            } else if ("North-West".equals(name)) {
                this.northWest = comp;
            } else if ("North-East".equals(name)) {
                this.northEast = comp;
            } else if ("South-West".equals(name)) {
                this.southWest = comp;
            } else if ("South-East".equals(name)) {
                this.southEast = comp;
            } else {
                throw new IllegalArgumentException(
                        "cannot add to layout: unknown constraint: " + name);
            }
        }
    }

    private Component getChild(String key) {
        Component result = null;

        if (key == "North") {
            result = this.north;
        } else if (key == "South") {
            result = this.south;
        } else if (key == "West") {
            result = this.west;
        } else if (key == "East") {
            result = this.east;
        } else if (key == "Center") {
            result = this.center;
        } else if (key == "North-West") {
            result = this.northWest;
        } else if (key == "North-East") {
            result = this.northEast;
        } else if (key == "South-West") {
            result = this.southWest;
        } else if (key == "South-East") {
            result = this.southEast;
        }

        if ((result != null) && (!result.isVisible())) {
            result = null;
        }

        return result;
    }

    public int getHgap() {
        return this.hgap;
    }

    public float getLayoutAlignmentX(Container parent) {
        return 0.5F;
    }

    public float getLayoutAlignmentY(Container parent) {
        return 0.5F;
    }

    public int getVgap() {
        return this.vgap;
    }

    public void invalidateLayout(Container target) {
    }

    public void layoutContainer(Container target) {
        synchronized (target.getTreeLock()) {
            Insets insets = target.getInsets();
            int top = insets.top;
            int bottom = target.getHeight() - insets.bottom;
            int left = insets.left;
            int right = target.getWidth() - insets.right;
            Component north = getChild("North");
            Component south = getChild("South");
            Component east = getChild("East");
            Component west = getChild("West");
            Component center = getChild("Center");
            Component northWest = getChild("North-West");
            Component northEast = getChild("North-East");
            Component southWest = getChild("South-West");
            Component southEast = getChild("South-East");

            if (north != null) {
                top += north.getPreferredSize().height + this.vgap;
            }

            if (south != null) {
                bottom -= south.getPreferredSize().height + this.vgap;
            }

            if (east != null) {
                right -= east.getPreferredSize().width + this.hgap;
            }

            if (west != null) {
                left += west.getPreferredSize().width + this.hgap;
            }

            if (north != null) {
                north.setBounds(left, insets.top, right - left,
                        north.getPreferredSize().height);
            }

            if (south != null) {
                south.setBounds(left, bottom + this.vgap, right - left,
                        south.getPreferredSize().height);
            }

            if (west != null) {
                west.setBounds(insets.left, top, west.getPreferredSize().width,
                        bottom - top);
            }

            if (east != null) {
                east.setBounds(right + this.hgap, top,
                        east.getPreferredSize().width, bottom - top);
            }

            if (center != null) {
                center.setBounds(left, top, right - left, bottom - top);
            }

            if (northWest != null) {
                northWest.setBounds(insets.left, insets.top, west == null ? 0
                        : west.getWidth(),
                        north == null ? 0 : north.getHeight());
            }

            if (northEast != null) {
                northEast.setBounds(right + this.hgap, insets.top,
                        east == null ? 0 : east.getWidth(), north == null ? 0
                        : north.getHeight());
            }

            if (southWest != null) {
                southWest.setBounds(insets.left, bottom + this.vgap,
                        west == null ? 0 : west.getWidth(), south == null ? 0
                        : south.getHeight());
            }

            if (southEast != null) {
                southEast.setBounds(right + this.hgap, bottom + this.vgap,
                        east == null ? 0 : east.getWidth(), south == null ? 0
                        : south.getHeight());
            }
        }
    }

    public Dimension maximumLayoutSize(Container target) {
        return new Dimension(2147483647, 2147483647);
    }

    public Dimension minimumLayoutSize(Container target) {
        synchronized (target.getTreeLock()) {
            Dimension dim = new Dimension(0, 0);
            Component c;
            if ((c = getChild("East")) != null) {
                Dimension d = c.getMinimumSize();
                dim.width += d.width + this.hgap;
                dim.height = Math.max(d.height, dim.height);
            }

            if ((c = getChild("West")) != null) {
                Dimension d = c.getMinimumSize();
                dim.width += d.width + this.hgap;
                dim.height = Math.max(d.height, dim.height);
            }

            if ((c = getChild("Center")) != null) {
                Dimension d = c.getMinimumSize();
                dim.width += d.width;
                dim.height = Math.max(d.height, dim.height);
            }

            if ((c = getChild("North")) != null) {
                Dimension d = c.getMinimumSize();
                dim.width = Math.max(d.width, dim.width);
                dim.height += d.height + this.vgap;
            }

            if ((c = getChild("South")) != null) {
                Dimension d = c.getMinimumSize();
                dim.width = Math.max(d.width, dim.width);
                dim.height += d.height + this.vgap;
            }

            Insets insets = target.getInsets();
            dim.width += insets.left + insets.right;
            dim.height += insets.top + insets.bottom;

            return dim;
        }
    }

    public Dimension preferredLayoutSize(Container target) {
        synchronized (target.getTreeLock()) {
            Dimension dim = new Dimension(0, 0);
            Component c;
            if ((c = getChild("East")) != null) {
                Dimension d = c.getPreferredSize();
                dim.width += d.width + this.hgap;
                dim.height = Math.max(d.height, dim.height);
            }

            if ((c = getChild("West")) != null) {
                Dimension d = c.getPreferredSize();
                dim.width += d.width + this.hgap;
                dim.height = Math.max(d.height, dim.height);
            }

            if ((c = getChild("Center")) != null) {
                Dimension d = c.getPreferredSize();
                dim.width += d.width;
                dim.height = Math.max(d.height, dim.height);
            }

            if ((c = getChild("North")) != null) {
                Dimension d = c.getPreferredSize();
                dim.width = Math.max(d.width, dim.width);
                dim.height += d.height + this.vgap;
            }

            if ((c = getChild("South")) != null) {
                Dimension d = c.getPreferredSize();
                dim.width = Math.max(d.width, dim.width);
                dim.height += d.height + this.vgap;
            }

            Insets insets = target.getInsets();
            dim.width += insets.left + insets.right;
            dim.height += insets.top + insets.bottom;

            return dim;
        }
    }

    public void removeLayoutComponent(Component comp) {
        synchronized (comp.getTreeLock()) {
            if (comp == this.center) {
                this.center = null;
            } else if (comp == this.north) {
                this.north = null;
            } else if (comp == this.south) {
                this.south = null;
            } else if (comp == this.east) {
                this.east = null;
            } else if (comp == this.west) {
                this.west = null;
            } else if (comp == this.northWest) {
                this.northWest = null;
            } else if (comp == this.northEast) {
                this.northEast = null;
            } else if (comp == this.southWest) {
                this.southWest = null;
            } else if (comp == this.southEast) {
                this.southEast = null;
            }
        }
    }

    public void setHgap(int hgap) {
        this.hgap = hgap;
    }

    public void setVgap(int vgap) {
        this.vgap = vgap;
    }

    public String toString() {
        return getClass().getName() + "[hgap=" + this.hgap + ",vgap="
                + this.vgap + "]";
    }
}