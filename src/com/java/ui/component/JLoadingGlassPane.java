package com.java.ui.component;

import com.java.ui.util.UIResourceManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.*;

public class JLoadingGlassPane extends JComponent implements ActionListener {
    private class Task<V> extends FutureTask<V> {
        @SuppressWarnings("unchecked")
        public Task(V runnable) {
            super((Callable<V>) runnable);
        }

        protected void done() {
            JLoadingGlassPane.this.executor.shutdown();
            JLoadingGlassPane.this.executor = null;
            JLoadingGlassPane.this.future = null;
            JLoadingGlassPane.this.setVisible(false);

            if ((!JLoadingGlassPane.this.cancelled)
                    && (JLoadingGlassPane.this.finishedAction != null)) {
                JLoadingGlassPane.this.finishedAction.actionPerformed(null);
            }

            synchronized (JLoadingGlassPane.this.lock) {
                JLoadingGlassPane.this.lock.notifyAll();
            }
        }
    }

    private static final long serialVersionUID = -2161086706185983717L;
    private float alpha;
    private JButton btnCancel;
    private Action cancelAction;
    private boolean cancelButtonVisible;
    private boolean cancelled;
    private ScheduledThreadPoolExecutor executor;
    private Action finishedAction;
    private ScheduledFuture<?> future;
    private String info;
    private JLabel lbInfo;
    private byte[] lock;


	private JProgressBar progressBar;

    public JLoadingGlassPane() {
        this(null);
    }

    public JLoadingGlassPane(JRootPane owner) {
        this(owner, true, true);
    }

    public JLoadingGlassPane(JRootPane owner, boolean indeterminate,
                             boolean cancelButtonVisible) {
        this(owner, indeterminate, cancelButtonVisible, null);
    }

    public JLoadingGlassPane(JRootPane owner, boolean indeterminate,
                             boolean cancelButtonVisible, String info) {
        if (owner != null) {
            owner.setGlassPane(this);
        }

        init();
        this.progressBar.setIndeterminate(indeterminate);
        setCancelButtonVisible(cancelButtonVisible);
        setInfo(info);
    }

    public void actionPerformed(ActionEvent e) {
        if (this.cancelAction != null) {
            this.cancelAction.actionPerformed(null);
        } else {
            stopCommand();
        }
    }

    public float getAlpha() {
        return this.alpha;
    }

    public ActionListener getCancelAction() {
        return this.cancelAction;
    }

    public JButton getCancelButton() {
        return this.btnCancel;
    }

    public Action getFinishedAction() {
        return this.finishedAction;
    }

    public String getInfo() {
        return this.info;
    }

    public JProgressBar getProgressBar() {
        return this.progressBar;
    }

    private void init() {
        this.progressBar = new JRound3DProgressBar();
        this.lbInfo = new JLabel();
        this.btnCancel = new JButton(
                UIManager.getString("OptionPane.cancelButtonText"));
        this.lock = new byte[0];
        this.alpha = 0.65F;

        this.lbInfo.setVerticalAlignment(3);
        this.lbInfo.setHorizontalAlignment(2);
        this.progressBar.setPreferredSize(new Dimension(-1, 12));
        this.btnCancel.setPreferredSize(new Dimension(-1, 22));
        this.btnCancel.addActionListener(this);
        setOpaque(false);
        setFont(new Font("Dialog", 1, 15));
        setVisible(false);
        setBackground(UIResourceManager.getWhiteColor());
        setForeground(Color.BLUE);
        setFocusTraversalKeysEnabled(false);
        setLayout(null);
        add(this.lbInfo);
        add(this.progressBar);
        add(this.btnCancel);
        addMouseListener(new MouseAdapter() {
        });
        addMouseMotionListener(new MouseMotionAdapter() {
        });
        addKeyListener(new KeyAdapter() {
        });
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                JLoadingGlassPane.this.reLayout();
            }

            public void componentShown(ComponentEvent evt) {
                JLoadingGlassPane.this.requestFocusInWindow();
            }
        });
    }

    public boolean isCancelButtonVisible() {
        return this.cancelButtonVisible;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Composite oldComposite = g2d.getComposite();
        Rectangle clip = g.getClipBounds();
        g2d.setComposite(AlphaComposite.SrcOver.derive(this.alpha));
        g2d.setColor(getBackground());
        g2d.fillRect(clip.x, clip.y, clip.width, clip.height);
        g2d.setComposite(oldComposite);
    }

    private void reLayout() {
        int width = getWidth();
        int height = getHeight();
        int buttonHeight = this.btnCancel.getPreferredSize().height;
        int barWidth = width * 2 / 3;
        int barHeight = this.progressBar.getPreferredSize().height;
        int barX = (width - barWidth) / 2;
        int barY = (height - barHeight) / 2;

        this.progressBar.setBounds(barX, barY, barWidth, barHeight);
        this.lbInfo.setBounds(barX, 0, barWidth, barY - 10);

        if (this.cancelButtonVisible) {
            this.btnCancel.setBounds(0, height - buttonHeight, width,
                    buttonHeight);
        }
    }

    public void runCommand(Runnable command) {
        runCommand(command, true);
    }

    private void runCommand(Runnable command, boolean wait) {
        this.cancelled = false;
        Task<Runnable> task = new Task<Runnable>(command);
        this.executor = new ScheduledThreadPoolExecutor(1);
        this.future = this.executor.schedule(task, 0L, TimeUnit.MILLISECONDS);
        setVisible(true);

        if (wait) {
            synchronized (this.lock) {
                try {
                    this.lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void runCommandWithoutLock(Runnable command) {
        runCommand(command, false);
    }

    public void setAlpha(float alpha) {
        if ((alpha >= 0.0F) && (alpha <= 1.0F)) {
            this.alpha = alpha;
            repaint();
        } else {
            throw new IllegalArgumentException("Invalid alpha:" + alpha);
        }
    }

    public void setCancelAction(Action cancelAction) {
        this.cancelAction = cancelAction;
    }

    public void setCancelButtonVisible(boolean cancelButtonVisible) {
        this.cancelButtonVisible = cancelButtonVisible;
        this.btnCancel.setVisible(cancelButtonVisible);
    }

    public void setFinishedAction(Action finishedAction) {
        this.finishedAction = finishedAction;
    }

    public void setFont(Font font) {
        super.setFont(font);
        this.lbInfo.setFont(font);
    }

    public void setForeground(Color color) {
        super.setForeground(color);
        this.lbInfo.setForeground(color);
    }

    public void setInfo(String info) {
        this.info = info;
        this.lbInfo.setText("<html>" + info);
    }

    public void stopCommand() {
        this.future.cancel(true);
        this.cancelled = true;
    }
}