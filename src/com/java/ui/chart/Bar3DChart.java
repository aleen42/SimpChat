package com.java.ui.chart;

import com.java.ui.util.UI3DUtil;
import com.java.ui.util.UIUtil;
import sun.swing.SwingUtilities2;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Map;
import java.util.TreeMap;

public class Bar3DChart extends Chart3D {
    private static final Color SCALE_BORDER_COLOR = new Color(128, 128, 128);
    private static final Color SCALE_COLOR = new Color(231, 231, 231);

    private static final Font SCALE_FONT = new Font("Arial", 0, 11);

    private static final long serialVersionUID = -8775657595745367716L;
    private Map<String, Bar3D> chartMap;
    private NumberFormat format;
    private double max;
    private int offsetX;
    private int offsetY;
    private int scaleYGap;
    private int step;

    public Bar3DChart(Map<String, Number> dataMap, int offsetX, int offsetY,
                      String title) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.scaleYGap = 30;
        this.format = new DecimalFormat("#0.00");
        this.chartMap = new TreeMap<String, Bar3D>();
        init(dataMap, this.chartMap, title);
    }

    protected synchronized void buildChartMap() {
        this.chartMap.clear();

        if (this.dataMap.isEmpty()) {
            return;
        }

        Color[] colors = UIUtil.createRandomColors(this.dataMap.size());
        int index = 0;
        this.max = 0.0D;

        for (String name : this.dataMap.keySet()) {
            double value = ((Number) this.dataMap.get(name)).doubleValue();
            this.max = Math.max(this.max, value);
            Bar3D bar = new Bar3D(name);
            bar.setBarColor(colors[index]);
            bar.setDarkerColor(UIUtil.createDarkerColor(colors[index], 0.6F));
            bar.setText(this.format == null ? String.valueOf(value)
                    : this.format.format(value));
            bar.setToolTipText(name + ": " + bar.getText());
            bar.setData(value);
            bar.setOffsetX(this.offsetX);
            bar.setOffsetY(this.offsetY);
            bar.setTopAlpha(0.4F);
            this.chartMap.put(name, bar);
            index++;
        }
    }

    public NumberFormat getFormat() {
        return this.format;
    }

    public int getOffsetX() {
        return this.offsetX;
    }

    public int getOffsetY() {
        return this.offsetY;
    }

    public int getScaleYGap() {
        return this.scaleYGap;
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
        int leftGap = paintScale(g2d, width, height, titleHeight);
        boolean paintChart = leftGap > 0;
        int ascent = fm.getAscent();

        int x = leftGap - this.offsetX;
        int y = titleHeight + 11;
        int chartWidth = width - leftGap - 3 - 11;
        int chartHeight = height - titleHeight - 11 - this.legendBounds.height
                - this.offsetY / 2;
        int thickness = chartWidth / (this.chartMap.size() * 2);

        for (Bar3D bar : this.chartMap.values()) {
            Rectangle bounds = bar.getLegendBounds();
            int legendX = bounds.x + this.legendBounds.x;
            int legendY = bounds.y + this.legendBounds.y;
            bar.setLegendLocation(legendX, legendY);
            bounds = bar.getLegendBounds();

            if (bar.isSelected()) {
                this.currentChart = bar;
                g2d.setColor(LEGEND_SELECTED_COLOR);
                g2d.fill(bounds);
            }

            g2d.setColor(bar.getDarkerColor());
            g2d.fillRect(bounds.x + 1, bounds.y + 4, 8, 8);
            g2d.setColor(bar.getBarColor());
            g2d.fillRect(bounds.x + 1, bounds.y + 4, 7, 7);
            g2d.setColor(bar.isSelected() ? Color.WHITE : getForeground());
            g2d.setFont(LEGEND_FONT);
            SwingUtilities2.drawString(this, g2d, bar.getName(),
                    bounds.x + 8 + 2, bounds.y + ascent);

            if (!paintChart)
                continue;
            g2d.setFont(CHART_FONT);
            x += thickness;
            double rectHeight = bar.getData().doubleValue()
                    * (this.scaleYGap / this.step);
            double startY = y + (chartHeight - rectHeight);
            Rectangle2D rect = new Rectangle2D.Double(x, startY, thickness,
                    rectHeight);
            Area area = UI3DUtil.paint3DBar(this, g2d, rect, this.offsetX,
                    this.offsetY, bar.getTopAlpha(), bar.getBarColor(),
                    bar.getDarkerColor(), getForeground(), bar.getText(),
                    bar.isSelected());
            bar.setArea(area);
            x += thickness;
        }
    }

    protected int paintScale(Graphics2D g, int width, int height,
                             int titleHeight) {
        g.setFont(SCALE_FONT);
        FontMetrics fm = g.getFontMetrics();
        int maxScaleWidth = fm.stringWidth((int) this.max + "0");
        int leftGap = maxScaleWidth + 3 + 11;
        int chartWidth = width - leftGap - 3;
        int chartHeight = height - titleHeight - this.legendBounds.height
                - this.offsetY / 2;

        if ((chartWidth <= 11) || (chartHeight <= 11)) {
            return 0;
        }

        Object oldAntialias = g
                .getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        chartWidth += 11;
        int deltaX = maxScaleWidth + 3;
        int[] leftX = {10, 0, 0, 10};
        int[] leftY = {1, 11, chartHeight - 1, chartHeight - 11};
        int[] bottomX = {0, 10, chartWidth - 1, chartWidth - 11};
        int[] bottomY = {chartHeight - 1, chartHeight - 11, chartHeight - 11,
                chartHeight - 1};
        Polygon left = new Polygon(leftX, leftY, 4);
        Polygon bottom = new Polygon(bottomX, bottomY, 4);
        g.translate(deltaX, titleHeight);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(SCALE_COLOR);
        g.fill(left);
        g.fill(bottom);
        g.setColor(SCALE_BORDER_COLOR);
        g.draw(left);
        g.draw(bottom);

        Stroke oldStroke = g.getStroke();
        Stroke stroke = new BasicStroke(1.0F, 0, 1, 2.0F, new float[]{2.0F,
                2.0F}, 3.0F);
        double stepDouble = this.max / ((chartHeight - 11) / this.scaleYGap);
        this.step = ((int) (stepDouble / 10.0D) * 10);

        while (this.max / this.step * this.scaleYGap > chartHeight - 11) {
            this.step += 5;
        }

        int y = chartHeight - 1;
        int scale = 0;

        while (y >= 11) {
            if (y < chartHeight - 1) {
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g.setStroke(stroke);
                g.drawLine(0, y, 10, y - 11 + 1);
                g.drawLine(11, y - 11 + 1, chartWidth, y - 11 + 1);
            }
            String scaleStr;
            int scaleWidth = fm.stringWidth(scaleStr = String.valueOf(scale));
            g.setStroke(oldStroke);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldAntialias);
            SwingUtilities2.drawString(this, g, scaleStr, -scaleWidth - 3, y
                    + fm.getDescent());
            scale += this.step;
            y -= this.scaleYGap;
        }

        g.translate(-deltaX, -titleHeight);
        return leftGap;
    }

    protected int paintTitle(Graphics2D g, int width) {
        int titleHeight = super.paintTitle(g, width);
        return titleHeight > 0 ? titleHeight : 3;
    }

    public void setFormat(NumberFormat format) {
        this.format = format;

        for (Bar3D bar : this.chartMap.values()) {
            bar.setText(format == null ? String.valueOf(bar.getData()) : format
                    .format(bar.getData()));
            bar.setToolTipText(bar.getName() + ": " + bar.getText());
        }

        repaint();
    }

    public void setScaleYGap(int scaleYGap) {
        if (scaleYGap < 10) {
            throw new IllegalArgumentException(
                    "scaleYGap must be greater than 10");
        }

        this.scaleYGap = scaleYGap;
        repaint();
    }
}