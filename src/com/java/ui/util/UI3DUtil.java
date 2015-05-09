package com.java.ui.util;

import sun.swing.SwingUtilities2;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;

public class UI3DUtil {
    public static Area create3DArea(Shape shape2d, int offsetX, int offsetY,
                                    boolean include2d) {
        Area area2d = new Area(shape2d);
        Area area = include2d ? (Area) area2d.clone() : new Area();
        List<Path2D> paths = createIntersectionPaths(shape2d, offsetX, offsetY);

        for (Path2D path : paths) {
            area.add(new Area(path));
        }

        if (include2d) {
            area.add(area2d);
        } else {
            area.subtract(area2d);
        }

        return area;
    }

    public static Path2D create3DPath(Shape shape2d, int offsetX, int offsetY,
                                      boolean include2d) {
        Path2D path3d = include2d ? new Path2D.Double(shape2d)
                : new Path2D.Double();
        List<Path2D> paths = createIntersectionPaths(shape2d, offsetX, offsetY);

        for (Path2D path : paths) {
            path3d.append(path, false);
        }

        return path3d;
    }

    private static Path2D createIntersectionPath(Point2D start,
                                                 double[] coords, double offsetX, double offsetY, int type) {
        Path2D path = new Path2D.Double();
        path.moveTo(start.getX(), start.getY());

        switch (type) {
            case 1:
            case 4:
                path.lineTo(coords[0], coords[1]);
                path.lineTo(coords[0] + offsetX, coords[1] + offsetY);
                path.lineTo(start.getX() + offsetX, start.getY() + offsetY);
                break;
            case 2:
                path.quadTo(coords[0], coords[1], coords[2], coords[3]);
                path.lineTo(coords[2] + offsetX, coords[3] + offsetY);
                path.quadTo(coords[0] + offsetX, coords[1] + offsetY, start.getX()
                        + offsetX, start.getY() + offsetY);
                break;
            case 3:
                path.curveTo(coords[0], coords[1], coords[2], coords[3], coords[4],
                        coords[5]);
                path.lineTo(coords[4] + offsetX, coords[5] + offsetY);
                path.curveTo(coords[2] + offsetX, coords[3] + offsetY, coords[0]
                        + offsetX, coords[1] + offsetY, start.getX() + offsetX,
                        start.getY() + offsetY);
        }

        path.closePath();
        return path;
    }

    public static List<Path2D> createIntersectionPaths(Shape shape2d,
                                                       int offsetX, int offsetY) {
        List<Path2D> paths = new ArrayList<Path2D>();
        PathIterator pi = new Path2D.Double(shape2d).getPathIterator(null);
        double[] coords = new double[6];
        Point2D start = new Point2D.Double();
        Point2D end = new Point2D.Double();
        Path2D subPath;
        while (!pi.isDone()) {
            int segment = pi.currentSegment(coords);

            switch (segment) {
                case 0:
                    start.setLocation(coords[0], coords[1]);
                    end.setLocation(coords[0], coords[1]);
                    break;
                case 1:
                    subPath = createIntersectionPath(end, coords, offsetX, offsetY,
                            segment);
                    paths.add(subPath);
                    end.setLocation(coords[0], coords[1]);
                    break;
                case 2:
                    subPath = createIntersectionPath(end, coords, offsetX, offsetY,
                            segment);
                    paths.add(subPath);
                    end.setLocation(coords[2], coords[3]);
                    break;
                case 3:
                    subPath = createIntersectionPath(end, coords, offsetX, offsetY,
                            segment);
                    paths.add(subPath);
                    end.setLocation(coords[4], coords[5]);
                    break;
                case 4:
                    subPath = createIntersectionPath(start,
                            new double[]{end.getX(), end.getY()}, offsetX,
                            offsetY, segment);
                    paths.add(subPath);
            }

            pi.next();
        }

        return paths;
    }

    public static Area paint3DBar(JComponent c, Graphics2D g, Rectangle2D rect,
                                  int offsetX, int offsetY, float topAlpha, Color barColor,
                                  Color darkerColor, Color textColor, String text, boolean selected) {
        if ((offsetX < 0) || (offsetY < 0)) {
            throw new IllegalArgumentException(
                    "offsetX and offsetY must be greater than or equals 0");
        }

        if ((topAlpha < 0.0F) || (topAlpha >= 1.0F)) {
            throw new IllegalArgumentException(
                    "topAlpha must be between 0.0f to 1.0f");
        }

        Area area = new Area(rect);
        int x = Math.round((float) rect.getX());
        int y = Math.round((float) rect.getY());
        int w = Math.round((float) rect.getWidth());
        int h = Math.round((float) rect.getHeight());
        int[] topX = {x, x + offsetX, x + offsetX + w, x + w};
        int[] topY = {y, y - offsetY, y - offsetY, y};
        int[] rightX = {x + offsetX + w, x + w, x + w, x + w + offsetX};
        int[] rightY = {y - offsetY, y, y + h, y + h - offsetY};
        Polygon top = new Polygon(topX, topY, 4);
        Polygon right = new Polygon(rightX, rightY, 4);
        Composite oldComposite = g.getComposite();
        Object oldAntialias = g
                .getRenderingHint(RenderingHints.KEY_ANTIALIASING);

        if (selected) {
            g.translate(-3, 3);
        }

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(darkerColor);
        g.fill(right);
        g.setColor(barColor);
        g.fill(rect);
        g.setComposite(AlphaComposite.SrcOver.derive(topAlpha));
        g.fill(top);
        g.setComposite(oldComposite);

        if (selected) {
            Stroke oldStroke = g.getStroke();
            g.setColor(darkerColor);
            g.setStroke(new BasicStroke(2.0F, 0, 1));
            g.draw(rect);
            g.draw(top);
            g.draw(right);
            g.setStroke(oldStroke);
        }

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldAntialias);

        if ((text != null) && (!text.trim().isEmpty())) {
            int textWidth = g.getFontMetrics().stringWidth(text);
            g.setColor(textColor);
            SwingUtilities2.drawString(c, g, text, x
                    + (w + offsetX - textWidth) / 2, y - offsetY / 2);
        }

        if (selected) {
            g.translate(3, -3);
        }

        area.add(new Area(top));
        area.add(new Area(right));
        return area;
    }

    public static Area paint3DPie(JComponent c, Graphics2D g, Shape pie,
                                  double startAngle, double extentAngle, int offsetX, int offsetY,
                                  Rectangle2D bounds, Color pieColor, Color darkerColor,
                                  Color textColor, String text, Point textPosition, boolean selected,
                                  boolean paintText) {
        Area pie3d = create3DArea(pie, offsetX, offsetY, true);
        Object oldAntialias = g
                .getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        Composite oldComposite = g.getComposite();
        double centerDegree = (startAngle + extentAngle / 2.0D) % 360.0D;
        centerDegree = centerDegree < 0.0D ? 360.0D + centerDegree
                : centerDegree;
        double centerRadian = Math.toRadians(centerDegree);
        int deltaX = 0;
        int deltaY = 0;

        if (selected) {
            deltaX = Math.round((float) Math.cos(centerRadian) * 3.0F);
            deltaY = Math.round((float) Math.sin(centerRadian) * 3.0F);
            g.translate(deltaX, -deltaY);
        }

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(darkerColor);
        g.fill(pie3d);
        g.setComposite(AlphaComposite.SrcOver);
        g.setColor(pieColor);
        g.fill(pie);

        if (selected) {
            Stroke oldStroke = g.getStroke();
            g.setColor(darkerColor);
            g.setStroke(new BasicStroke(2.0F, 0, 1));
            g.setComposite(oldComposite);
            g.draw(pie3d);
            g.setStroke(oldStroke);
            g.setComposite(AlphaComposite.SrcOver);
        }

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldAntialias);

        if ((text != null) && (!text.trim().isEmpty())) {
            FontMetrics fm = g.getFontMetrics();
            int centerX = Math
                    .round((float) (bounds.getX() + bounds.getWidth() / 2.0D));
            int centerY = Math.round((float) (bounds.getY() + bounds
                    .getHeight() / 2.0D));
            double a = bounds.getWidth() * 0.382D;
            double b = bounds.getHeight() * 0.382D;
            double k = Math.tan(centerRadian);
            double dx = a * b / Math.sqrt(b * b + a * a * k * k);
            double dy = k * dx;
            int x = Math.round((float) dx);
            int y = Math.round((float) dy);

            if (((centerDegree > 0.0D) && (centerDegree <= 90.0D))
                    || ((centerDegree > 270.0D) && (centerDegree <= 360.0D))) {
                y = -y;
            } else if (((centerDegree > 90.0D) && (centerDegree <= 180.0D))
                    || ((centerDegree > 180.0D) && (centerDegree <= 270.0D))) {
                x = -x;
            }

            int textX = deltaX + centerX + x - fm.stringWidth(text) / 2;
            int textY = -deltaY + centerY + y + fm.getDescent()
                    + fm.getLeading();
            textPosition.setLocation(textX, textY);

            if (paintText) {
                g.setColor(textColor);
                SwingUtilities2.drawString(c, g, text, textX, textY);
            }
        }

        if (selected) {
            g.translate(-deltaX, deltaY);
        }

        g.setComposite(oldComposite);
        return pie3d;
    }
}