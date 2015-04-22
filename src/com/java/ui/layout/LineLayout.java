package com.java.ui.layout;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LineLayout implements LayoutManager2, Serializable {
    public static final int CENTER = 0;
    public static final String END = "End";
    public static final String END_FILL = "EedFill";
    public static final int HORIZONTAL = 0;
    public static final int LEADING = 10;
    public static final String MIDDLE = "Middle";
    public static final String MIDDLE_FILL = "MiddleFill";
    private static final long serialVersionUID = -2870834002278815219L;
    public static final String START = "Start";
    public static final String START_FILL = "StartFill";
    public static final int TRAILING = 11;
    public static final int VERTICAL = 1;
    private int alignment;
    private int bottomGap;
    private List<Component> endComponents;
    private List<Component> fillComponents;
    private int gap;
    private int leftGap;
    private Component middleComponent;
    private int orientation;
    private int position;
    private int rightGap;
    private List<Component> startComponents;
    private int topGap;

    public LineLayout() {
        this(0);
    }

    public LineLayout(int orientation) {
        this(5, 0, 10, orientation);
    }

    public LineLayout(int gap, int alignment, int position, int orientation) {
        this(gap, gap, gap, gap, gap, alignment, position, orientation);
    }

    public LineLayout(int gap, int leftGap, int rightGap, int topGap,
                      int bottomGap, int alignment, int position, int orientation) {
        this.gap = gap;
        this.leftGap = leftGap;
        this.rightGap = rightGap;
        this.topGap = topGap;
        this.bottomGap = bottomGap;
        this.alignment = alignment;
        this.position = position;
        this.orientation = orientation;
        this.startComponents = new ArrayList<Component>();
        this.endComponents = new ArrayList<Component>();
        this.fillComponents = new ArrayList<Component>();
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
            if (((name == null) || ("Start".equals(name)))
                    && (!this.startComponents.contains(comp))) {
                this.startComponents.add(comp);
            } else if (("StartFill".equals(name))
                    && (!this.startComponents.contains(comp))) {
                this.startComponents.add(comp);
                this.fillComponents.add(comp);
            } else if (("End".equals(name))
                    && (!this.endComponents.contains(comp))) {
                this.endComponents.add(comp);
            } else if (("EedFill".equals(name))
                    && (!this.endComponents.contains(comp))) {
                this.endComponents.add(comp);
                this.fillComponents.add(comp);
            } else if (("Middle".equals(name))
                    && (this.middleComponent != comp)) {
                this.middleComponent = comp;
            } else if (("MiddleFill".equals(name))
                    && (this.middleComponent != comp)) {
                this.middleComponent = comp;
                this.fillComponents.add(comp);
            }
        }
    }

    public int getAlignment() {
        return this.alignment;
    }

    public int getBottomGap() {
        return this.bottomGap;
    }

    public int getGap() {
        return this.gap;
    }

    public float getLayoutAlignmentX(Container target) {
        float alignmentX = 0.5F;

        if (this.orientation == 0) {
            switch (this.alignment) {
                case 10:
                    alignmentX = 0.0F;
                    break;
                case 11:
                    alignmentX = 1.0F;
            }

        }

        return alignmentX;
    }

    public float getLayoutAlignmentY(Container target) {
        float alignmentY = 0.5F;

        if (this.orientation == 1) {
            switch (this.alignment) {
                case 10:
                    alignmentY = 0.0F;
                    break;
                case 11:
                    alignmentY = 1.0F;
            }

        }

        return alignmentY;
    }

    public int getLeftGap() {
        return this.leftGap;
    }

    public int getOrientation() {
        return this.orientation;
    }

    public int getPosition() {
        return this.position;
    }

    public int getRightGap() {
        return this.rightGap;
    }

    public int getTopGap() {
        return this.topGap;
    }

    private int getX(Component component, int maxWidth, Insets targetInsets,
                     int targetWidth) {
        int x = targetInsets.left + this.leftGap;

        if (!this.fillComponents.contains(component)) {
            switch (this.alignment) {
                case 10:
                    break;
                case 11:
                    x += maxWidth - component.getWidth();
                    break;
                default:
                    x += (maxWidth - component.getWidth()) / 2;
            }

            switch (this.position) {
                case 11:
                    x += targetWidth - targetInsets.left - targetInsets.right
                            - this.leftGap - this.rightGap - maxWidth;
                    break;
                case 0:
                    x += (targetWidth - targetInsets.left - targetInsets.right
                            - this.leftGap - this.rightGap - maxWidth) / 2;
            }

        }

        return x;
    }

    private int getY(Component component, int maxHeight, Insets targetInsets,
                     int targetHeight) {
        int y = targetInsets.top + this.topGap;

        if (!this.fillComponents.contains(component)) {
            switch (this.alignment) {
                case 10:
                    break;
                case 11:
                    y += maxHeight - component.getHeight();
                    break;
                default:
                    y += (maxHeight - component.getHeight()) / 2;
            }

            switch (this.position) {
                case 11:
                    y += targetHeight - targetInsets.top - targetInsets.bottom
                            - this.topGap - this.bottomGap - maxHeight;
                    break;
                case 0:
                    y += (targetHeight - targetInsets.top - targetInsets.bottom
                            - this.topGap - this.bottomGap - maxHeight) / 2;
            }

        }

        return y;
    }

    public void invalidateLayout(Container target) {
    }

    public void layoutContainer(Container target) {
        if (this.orientation == 1) {
            layoutContainerV(target);
        } else {
            layoutContainerH(target);
        }
    }

    private void layoutContainerH(Container target) {
        int maxHeight = 0;

        Insets insets = target.getInsets();

        for (Component component : target.getComponents()) {
            if (!component.isVisible())
                continue;
            Dimension size = component.getPreferredSize();
            int height;

            if (this.fillComponents.contains(component)) {
                height = target.getHeight() - insets.top - insets.bottom
                        - this.topGap - this.bottomGap;
            } else {
                height = size.height;

                if (height > maxHeight) {
                    maxHeight = height;
                }
            }

            component.setSize(size.width, height);
        }

        moveComponentsH(target, maxHeight);
    }

    private void layoutContainerV(Container target) {
        int maxWidth = 0;

        Insets insets = target.getInsets();

        for (Component component : target.getComponents()) {
            if (!component.isVisible())
                continue;
            Dimension size = component.getPreferredSize();
            int width;

            if (this.fillComponents.contains(component)) {
                width = target.getWidth() - insets.left - insets.right
                        - this.leftGap - this.rightGap;
            } else {
                width = size.width;

                if (width > maxWidth) {
                    maxWidth = width;
                }
            }

            component.setSize(width, size.height);
        }

        moveComponentsV(target, maxWidth);
    }

    public Dimension maximumLayoutSize(Container target) {
        return new Dimension(2147483647, 2147483647);
    }

    public Dimension minimumLayoutSize(Container target) {
        synchronized (target.getTreeLock()) {
            int width = 0;
            int height = 0;
            int count = 0;

            Insets insets = target.getInsets();

            for (Component component : target.getComponents()) {
                if (!component.isVisible())
                    continue;
                count++;
                Dimension size = component.getMinimumSize();

                if (this.orientation == 1) {
                    height += size.height;

                    if (size.width <= width)
                        continue;
                    width = size.width;
                } else {
                    width += size.width;

                    if (size.height <= height)
                        continue;
                    height = size.height;
                }

            }

            if (this.orientation == 1) {
                width += this.leftGap + this.rightGap + insets.left
                        + insets.right;
                height += this.topGap + this.bottomGap + insets.top
                        + insets.bottom
                        + (count > 1 ? (count - 1) * this.gap : 0);
            } else {
                height += this.topGap + this.bottomGap + insets.top
                        + insets.bottom;
                width += this.leftGap + this.rightGap + insets.left
                        + insets.right
                        + (count > 1 ? (count - 1) * this.gap : 0);
            }

            return new Dimension(width, height);
        }
    }

    private void moveComponentsH(Container target, int maxHeight) {
        synchronized (target.getTreeLock()) {
            Insets insets = target.getInsets();
            int leftX = insets.left + this.leftGap;
            int rightX = target.getWidth() - insets.right - this.rightGap;

            for (Component component : this.startComponents) {
                if (!component.isVisible())
                    continue;
                component.setLocation(leftX,
                        getY(component, maxHeight, insets, target.getHeight()));
                leftX += component.getWidth() + this.gap;
            }

            for (int i = this.endComponents.size() - 1; i >= 0; i--) {
                Component component = (Component) this.endComponents.get(i);

                if (!component.isVisible())
                    continue;
                rightX -= component.getWidth();
                component.setLocation(rightX,
                        getY(component, maxHeight, insets, target.getHeight()));
                rightX -= this.gap;
            }

            if ((this.middleComponent != null)
                    && (this.middleComponent.isVisible())) {
                this.middleComponent.setSize(rightX > leftX ? rightX - leftX
                        : 0, this.middleComponent.getHeight());
                this.middleComponent.setLocation(
                        leftX,
                        getY(this.middleComponent, maxHeight, insets,
                                target.getHeight()));
            }
        }
    }

    private void moveComponentsV(Container target, int maxWidth) {
        synchronized (target.getTreeLock()) {
            Insets insets = target.getInsets();
            int topY = insets.top + this.topGap;
            int bottomY = target.getHeight() - insets.bottom - this.bottomGap;

            for (Component component : this.startComponents) {
                if (!component.isVisible())
                    continue;
                component.setLocation(
                        getX(component, maxWidth, insets, target.getWidth()),
                        topY);
                topY += component.getHeight() + this.gap;
            }

            for (int i = this.endComponents.size() - 1; i >= 0; i--) {
                Component component = (Component) this.endComponents.get(i);

                if (!component.isVisible())
                    continue;
                bottomY -= component.getHeight();
                component.setLocation(
                        getX(component, maxWidth, insets, target.getWidth()),
                        bottomY);
                bottomY -= this.gap;
            }

            if ((this.middleComponent != null)
                    && (this.middleComponent.isVisible())) {
                this.middleComponent.setSize(this.middleComponent.getWidth(),
                        bottomY > topY ? bottomY - topY : 0);
                this.middleComponent.setLocation(
                        getX(this.middleComponent, maxWidth, insets,
                                target.getWidth()), topY);
            }
        }
    }

    public Dimension preferredLayoutSize(Container target) {
        synchronized (target.getTreeLock()) {
            int width = 0;
            int height = 0;
            int count = 0;

            Insets insets = target.getInsets();

            for (Component component : target.getComponents()) {
                if (!component.isVisible())
                    continue;
                count++;
                Dimension size = component.getPreferredSize();

                if (this.orientation == 1) {
                    height += size.height;

                    if (size.width <= width)
                        continue;
                    width = size.width;
                } else {
                    width += size.width;

                    if (size.height <= height)
                        continue;
                    height = size.height;
                }

            }

            if (this.orientation == 1) {
                width += this.leftGap + this.rightGap + insets.left
                        + insets.right;
                height += this.topGap + this.bottomGap + insets.top
                        + insets.bottom
                        + (count > 1 ? (count - 1) * this.gap : 0);
            } else {
                height += this.topGap + this.bottomGap + insets.top
                        + insets.bottom;
                width += this.leftGap + this.rightGap + insets.left
                        + insets.right
                        + (count > 1 ? (count - 1) * this.gap : 0);
            }

            return new Dimension(width, height);
        }
    }

    public void removeLayoutComponent(Component comp) {
        synchronized (comp.getTreeLock()) {
            if (this.middleComponent == comp) {
                this.middleComponent = null;
            } else {
                this.startComponents.remove(comp);
                this.endComponents.remove(comp);
            }

            this.fillComponents.remove(comp);
        }
    }

    public void setAlignment(int alignment) {
        this.alignment = alignment;
    }

    public void setBottomGap(int bottomGap) {
        this.bottomGap = bottomGap;
    }

    public void setGap(int gap) {
        this.gap = gap;
    }

    public void setLeftGap(int leftGap) {
        this.leftGap = leftGap;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setRightGap(int rightGap) {
        this.rightGap = rightGap;
    }

    public void setTopGap(int topGap) {
        this.topGap = topGap;
    }

    public String toString() {
        return getClass().getName() + "[gap=" + this.gap + ",leftGap="
                + this.leftGap + ",rightGap=" + this.rightGap + ",topGap="
                + this.topGap + ",bottomGap=" + this.bottomGap + ",alignment="
                + this.alignment + ",position=" + this.position
                + ",orientation=" + this.orientation + "]";
    }
}