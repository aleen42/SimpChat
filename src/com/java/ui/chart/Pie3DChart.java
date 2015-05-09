package com.java.ui.chart;

import com.java.ui.util.UI3DUtil;
import com.java.ui.util.UIUtil;
import sun.swing.SwingUtilities2;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Map;
import java.util.TreeMap;

public class Pie3DChart extends Chart3D {
    private static final long serialVersionUID = 3585557597017329246L;
    private Map<String, Pie3D> chartMap;
    private NumberFormat dataFormat;
    private NumberFormat percentFormat;
    private int thickness;

    public Pie3DChart(Map<String, Number> dataMap, int thickness, String title) {
        if (thickness <= 0) {
            throw new IllegalArgumentException(
                    "thickness must be greater than 1");
        }

        this.thickness = thickness;
        this.percentFormat = new DecimalFormat("#0.00%");
        this.chartMap = new TreeMap<String, Pie3D>();
        init(dataMap, this.chartMap, title);
    }

    protected synchronized void buildChartMap() {
        double count = 0.0D;
        this.chartMap.clear();

        for (String name : this.dataMap.keySet()) {
            count += ((Number) this.dataMap.get(name)).doubleValue();
        }

        if (count <= 0.0D) {
            return;
        }

        Color[] colors = UIUtil.createRandomColors(this.dataMap.size());

        double startAngle = 0.0D;
        double extentAngle = 0.0D;
        double per = 0.0D;
        int index = 0;

        for (String name : this.dataMap.keySet()) {
            Number rawData;
            per = (rawData = (Number) this.dataMap.get(name)).doubleValue()
                    / count;
            extentAngle = per * 360.0D;
            Pie3D pie = new Pie3D(name, startAngle, extentAngle);
            pie.setPieColor(colors[index]);
            pie.setDarkerColor(UIUtil.createDarkerColor(colors[index], 0.6F));
            pie.setPercent(per);
            pie.setText(this.percentFormat == null ? String.valueOf(per)
                    : this.percentFormat.format(per));
            pie.setToolTipText(name
                    + ": "
                    + (this.dataFormat == null ? rawData : this.dataFormat
                    .format(rawData)) + "(" + pie.getText() + ")");
            pie.setRawData(rawData);
            this.chartMap.put(name, pie);
            startAngle += extentAngle;
            index++;
        }
    }

    public NumberFormat getDataFormat() {
        return this.dataFormat;
    }

    public NumberFormat getPercentFormat() {
        return this.percentFormat;
    }

    public int getThickness() {
        return this.thickness;
    }

    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        this.currentChart = null;
        int width = getWidth();
        int height = getHeight();
        paintBackground(g2d);
        int titleHeight = paintTitle(g2d, width);

        if (this.chartMap.isEmpty()) {
            return;
        }

        g2d.setFont(LEGEND_FONT);
        FontMetrics fm = g2d.getFontMetrics();
        calculateLegendBounds(fm, width, height);
        int imageWidth = width;
        int imageHeight = height - this.legendBounds.height;
        int chartWidth = imageWidth - 8;
        int chartHeight = imageHeight - this.thickness - 8 - titleHeight;
        boolean paintChart = (chartWidth > 0) && (chartHeight > 0);
        BufferedImage image = paintChart ? UIUtil
                .getGraphicsConfiguration(this).createCompatibleImage(
                        imageWidth, imageHeight, 3) : null;
        Ellipse2D ellipse = image == null ? null : new Ellipse2D.Double(4.0D,
                4 + titleHeight, chartWidth, chartHeight);
        Area ellipseArea = image == null ? null : new Area(ellipse);
        Graphics2D imageG2d = image == null ? null : image.createGraphics();
        double startAngle = 90.0D;
        double extentAngle = 0.0D;
        boolean compositeChanged = false;
        int ascent = fm.getAscent();

        Arc2D arc = null;

        if (paintChart) {
            if (chartWidth > chartHeight) {
                arc = new Arc2D.Double(4.0D, 4 + titleHeight
                        + (chartHeight - chartWidth) / 2, chartWidth,
                        chartWidth, 0.0D, 0.0D, 2);
            } else if (chartWidth < chartHeight) {
                arc = new Arc2D.Double(4 + (chartWidth - chartHeight) / 2,
                        4 + titleHeight, chartHeight, chartHeight, 0.0D, 0.0D,
                        2);
            } else {
                arc = new Arc2D.Double(4.0D, 4 + titleHeight, chartWidth,
                        chartHeight, 0.0D, 0.0D, 2);
            }

            imageG2d.setFont(CHART_FONT);
        }

        for (Pie3D pie : this.chartMap.values()) {
            Rectangle bounds = pie.getLegendBounds();
            int legendX = bounds.x + this.legendBounds.x;
            int legendY = bounds.y + this.legendBounds.y;
            pie.setLegendLocation(legendX, legendY);
            bounds = pie.getLegendBounds();

            if (pie.isSelected()) {
                this.currentChart = pie;
                g2d.setColor(LEGEND_SELECTED_COLOR);
                g2d.fill(bounds);
            }

            g2d.setColor(pie.getDarkerColor());
            g2d.fillRect(bounds.x + 1, bounds.y + 4, 8, 8);
            g2d.setColor(pie.getPieColor());
            g2d.fillRect(bounds.x + 1, bounds.y + 4, 7, 7);
            g2d.setColor(pie.isSelected() ? Color.WHITE : getForeground());
            SwingUtilities2.drawString(this, g2d, pie.getName(),
                    bounds.x + 8 + 2, bounds.y + ascent);

            if (!paintChart)
                continue;
            if ((startAngle > 270.0D) && (!compositeChanged)) {
                imageG2d.setComposite(AlphaComposite.DstAtop);
                compositeChanged = true;
            }

            arc.setAngleStart(startAngle);
            arc.setAngleExtent(extentAngle = pie.getExtentAngle());
            Area area;
            pie.setArea(area = new Area(arc));
            area.intersect(ellipseArea);
            UI3DUtil.paint3DPie(this, imageG2d, area, startAngle, extentAngle,
                    0, this.thickness, ellipse.getBounds2D(),
                    pie.getPieColor(), pie.getDarkerColor(), getForeground(),
                    pie.getText(), pie.getTextPosition(), pie.isSelected(),
                    false);
            startAngle += extentAngle;
        }

        if (chartHeight > 0) {
            imageG2d.dispose();
            g2d.drawImage(image, 0, 0, this);
            g2d.setFont(CHART_FONT);
            g2d.setColor(getForeground());

            for (Pie3D pie : this.chartMap.values()) {
                SwingUtilities2.drawString(this, g2d, pie.getText(),
                        pie.getTextPosition().x, pie.getTextPosition().y);
            }
        }
    }

    public void setDataFormat(NumberFormat dataFormat) {
        this.dataFormat = dataFormat;

        for (Pie3D pie : this.chartMap.values()) {
            pie.setToolTipText(pie.getName()
                    + ": "
                    + (dataFormat == null ? pie.getRawData() : dataFormat
                    .format(pie.getRawData())) + "(" + pie.getText()
                    + ")");
        }

        repaint();
    }

    public void setPercentFormat(NumberFormat percentFormat) {
        this.percentFormat = percentFormat;

        for (Pie3D pie : this.chartMap.values()) {
            pie.setText(percentFormat == null ? String.valueOf(pie.getPercent())
                    : percentFormat.format(pie.getPercent()));
            pie.setToolTipText(pie.getName()
                    + ": "
                    + (this.dataFormat == null ? pie.getRawData()
                    : this.dataFormat.format(pie.getRawData())) + "("
                    + pie.getText() + ")");
        }

        repaint();
    }
}