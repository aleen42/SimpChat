package com.java.ui.chart;

import com.java.ui.util.UIUtil;
import sun.swing.SwingUtilities2;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public abstract class Chart3D extends JComponent {
    private class MouseHandle extends MouseAdapter {
        private MouseHandle() {
        }

        public void mousePressed(MouseEvent e) {
            if (!SwingUtilities.isLeftMouseButton(e)) {
                return;
            }

            Point point = e.getPoint();
            Chart3DBean selectedChart = null;

            for (Chart3DBean chart : Chart3D.this.chartMap.values()) {
                if ((!chart.getArea().contains(point))
                        && (!chart.getLegendBounds().contains(point)))
                    continue;
                selectedChart = chart;
                break;
            }

            if (selectedChart != Chart3D.this.currentChart) {
                if (selectedChart != null) {
                    selectedChart.setSelected(true);
                }

                if (Chart3D.this.currentChart != null) {
                    Chart3D.this.currentChart.setSelected(false);
                }

                Chart3D.this.repaint();
            }
        }
    }

    protected static final Font CHART_FONT = new Font("Arial", 1, 12);

    protected static final int CHART_GAP = 4;

    protected static final Font LEGEND_FONT = UIUtil.getDefaultFont();

    protected static final int LEGEND_GAP_X = 1;
    protected static final int LEGEND_LOGO_SIZE = 8;
    protected static final int LEGEND_ROW_HEIGHT = 16;
    protected static final Color LEGEND_SELECTED_COLOR = new Color(130, 130,
            130);
    protected static final int SELECTED_GAP = 4;
    private static final long serialVersionUID = 5990850212081253215L;
    protected static final Font TITLE_FONT = new Font(UIUtil.getDefaultFont()
            .getName(), 1, 14);
    private Map<String, ? extends Chart3DBean> chartMap;
    protected Chart3DBean currentChart;
    protected Map<String, Number> dataMap;
    protected Rectangle legendBounds;

    protected String title;

    protected abstract void buildChartMap();

    protected void calculateLegendBounds(FontMetrics fm, int width, int height) {
        int rowCount = 1;
        int x = 0;
        int maxRowWidth = 0;

        for (Chart3DBean chart : this.chartMap.values()) {
            int textWidth = fm.stringWidth(chart.getName());
            int legendWidth = 8 + textWidth + 2;

            if ((x > 0) && (x + legendWidth > width)) {
                maxRowWidth = Math.max(maxRowWidth, x - 1);
                x = 0;
                rowCount++;
            }

            chart.setLegendBounds(x, (rowCount - 1) * 16, legendWidth, 16);
            x += legendWidth + 1;
        }

        maxRowWidth = Math.max(maxRowWidth, x - 1);
        int legendHeight = rowCount * 16 + 4;
        int deltaX = (width - maxRowWidth) / 2;
        int deltaY = height - rowCount * 16;
        this.legendBounds.setBounds(deltaX, deltaY, maxRowWidth, legendHeight);
    }

    public BufferedImage createImage() {
        BufferedImage image = UIUtil.getGraphicsConfiguration(this)
                .createCompatibleImage(getWidth(), getHeight(), 3);
        Graphics2D g = image.createGraphics();
        paint(g);
        g.dispose();
        return image;
    }

    public Map<String, Number> getDatas() {
        return new HashMap<String, Number>(this.dataMap);
    }

    public String getTitle() {
        return this.title;
    }

    public String getToolTipText(MouseEvent event) {
        Point point = event.getPoint();

        for (Chart3DBean chart : this.chartMap.values()) {
            if ((chart.getArea().contains(point))
                    || (chart.getLegendBounds().contains(point))) {
                return chart.getToolTipText();
            }
        }

        return null;
    }

    protected void init(Map<String, Number> dataMap,
                        Map<String, ? extends Chart3DBean> chartMap, String title) {
        this.title = title;
        this.chartMap = chartMap;
        this.dataMap = new HashMap<String, Number>();
        this.legendBounds = new Rectangle();
        putClientProperty(SwingUtilities2.AA_TEXT_PROPERTY_KEY,
                UIUtil.COMMON_AATEXT_INFO);
        setBorder(new EmptyBorder(0, 0, 0, 0));
        setBackground(Color.WHITE);
        setForeground(Color.BLACK);
        setOpaque(true);
        setDatas(dataMap);
        addMouseListener(new MouseHandle());
        ToolTipManager.sharedInstance().registerComponent(this);
    }

    protected void paintBackground(Graphics2D g) {
        if (isOpaque()) {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    protected int paintTitle(Graphics2D g, int width) {
        if ((this.title == null) || (this.title.trim().isEmpty())) {
            return 0;
        }

        g.setFont(TITLE_FONT);
        FontMetrics fm = g.getFontMetrics();
        int titleWidth = fm.stringWidth(this.title);
        int titleHeight = fm.getHeight() - fm.getLeading();
        int titleX = (width - titleWidth) / 2;
        int titleY = fm.getAscent();
        g.setColor(getForeground());
        SwingUtilities2.drawString(this, g, this.title, titleX, titleY);
        return titleHeight + 4;
    }

    public void setDatas(Map<String, Number> dataMap) {
        this.dataMap.clear();

        if (dataMap != null) {
            this.dataMap.putAll(dataMap);
        }

        buildChartMap();
    }

    public void setDatasAndRepaint(Map<String, Number> dataMap) {
        setDatas(dataMap);
        repaint();
    }

    public void setTitle(String title) {
        this.title = title;
        repaint();
    }
}