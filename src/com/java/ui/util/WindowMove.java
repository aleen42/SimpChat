package com.java.ui.util;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class WindowMove extends MouseAdapter {
    private Component component;
    private boolean moveable;
    private Point point;
    private Window window;

    public WindowMove(Component component) {
        this.moveable = true;
        this.point = new Point(-1, -1);
        changeComponent(component);
    }

    public void changeComponent(Component component) {
        if (this.component != null) {
            this.component.removeMouseMotionListener(this);
            this.component.removeMouseListener(this);
        }

        this.component = component;
        this.window = UIUtil.getWindowFromComponent(component);

        if (this.window != null) {
            component.addMouseMotionListener(this);
            component.addMouseListener(this);
        }
    }

    public boolean isMoveable() {
        return this.moveable;
    }

    public void mouseDragged(MouseEvent e) {
        boolean move = this.moveable;

        if ((move) && ((this.window instanceof Frame))) {
            move = (((Frame) this.window).getExtendedState() & 0x6) == 0;
        }

        if ((move) && (this.point.x >= 0) && (this.point.y >= 0)) {
            int x = e.getXOnScreen() - this.point.x;
            int y = e.getYOnScreen() - this.point.y;
            this.window.setLocation(x, y);
        }
    }

    public void mousePressed(MouseEvent e) {
        if (this.moveable) {
            if (e.getButton() == 1) {
                this.point.move(e.getXOnScreen() - this.window.getX(),
                        e.getYOnScreen() - this.window.getY());
            } else {
                this.point.move(-1, -1);
            }
        }
    }

    public void mouseReleased(MouseEvent e) {
        if ((this.moveable) && (e.getButton() == 1)) {
            this.point.move(-1, -1);
        }
    }

    public void setMoveable(boolean moveable) {
        this.moveable = moveable;
    }
}