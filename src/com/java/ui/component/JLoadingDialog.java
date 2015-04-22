package com.java.ui.component;

import com.java.ui.util.WindowMove;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.*;

public class JLoadingDialog extends JDialog implements ActionListener {
    private class Task<V> extends FutureTask<V> {
        public Task(Callable<V> runnable) {
            super(runnable);
        }

        protected void done() {
            JLoadingDialog.this.executor.shutdown();
            JLoadingDialog.this.executor = null;
            JLoadingDialog.this.future = null;
            JLoadingDialog.this.dispose();

            if ((!JLoadingDialog.this.cancelled)
                    && (JLoadingDialog.this.finishedAction != null)) {
                JLoadingDialog.this.finishedAction.actionPerformed(null);
            }
        }
    }

    private static final long serialVersionUID = -6555677237581881143L;
    private JButton btnCancel;
    private Action cancelAction;
    private boolean cancelButtonVisible;
    private boolean cancelled;
    private Runnable command;
    private Container contentPane;
    private ScheduledThreadPoolExecutor executor;
    private Action finishedAction;
    private ScheduledFuture<?> future;
    private JLabel lbInfo;
    private ComponentListener listener;
    private WindowMove move;
    private Window parent;
    private JProgressBar progressBar;

    private String statusInfo;

    public JLoadingDialog(Window parent) {
        this(parent, null, null);
    }

    public JLoadingDialog(Window parent, Runnable command, String statusInfo) {
        this(parent, command, statusInfo, true);
    }

    public JLoadingDialog(Window parent, Runnable command, String statusInfo,
                          boolean cancelButtonVisible) {
        super(parent, Dialog.ModalityType.DOCUMENT_MODAL);
        this.move = new WindowMove(this);
        this.parent = parent;
        this.command = command;
        this.statusInfo = statusInfo;
        this.cancelButtonVisible = cancelButtonVisible;
        initUI();
    }

    public void actionPerformed(ActionEvent e) {
        if (this.cancelButtonVisible) {
            if (this.cancelAction != null) {
                this.cancelAction.actionPerformed(null);
            } else {
                stopCommand();
            }
        }
    }

    public void changeContentPane(Container newContentPane) {
        this.contentPane.removeComponentListener(this.listener);
        newContentPane.removeAll();
        newContentPane.setLayout(null);
        newContentPane.addComponentListener(this.listener);

        for (Component c : this.contentPane.getComponents()) {
            newContentPane.add(c);
        }

        setContentPane(newContentPane);
        getRootPane().doLayout();
        this.contentPane = newContentPane;
    }

    public Border getBorder() {
        return getRootPane().getBorder();
    }

    public ActionListener getCancelAction() {
        return this.cancelAction;
    }

    public JButton getCancelButton() {
        return this.btnCancel;
    }

    public Runnable getCommand() {
        return this.command;
    }

    public Action getFinishedAction() {
        return this.finishedAction;
    }

    public JLabel getInfoLabel() {
        return this.lbInfo;
    }

    public Window getParent() {
        return this.parent;
    }

    public JProgressBar getProgressBar() {
        return this.progressBar;
    }

    public String getStatusInfo() {
        return this.statusInfo;
    }

    private void initUI() {
        this.contentPane = getContentPane();
        this.progressBar = new JRound3DProgressBar();
        this.lbInfo = new JLabel("<html>" + this.statusInfo);
        this.btnCancel = new JButton(
                UIManager.getString("OptionPane.cancelButtonText"));
        this.listener = new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                JLoadingDialog.this.reLayout();
            }
        };
        setDefaultCloseOperation(0);
        this.progressBar.setPreferredSize(new Dimension(-1, 15));
        this.progressBar.setIndeterminate(true);
        this.btnCancel.setPreferredSize(new Dimension(75, 22));
        this.btnCancel.addActionListener(this);
        this.btnCancel.setVisible(this.cancelButtonVisible);
        this.lbInfo.setVerticalAlignment(1);
        this.contentPane.setLayout(null);
        this.contentPane.add(this.progressBar);
        this.contentPane.add(this.lbInfo);
        this.contentPane.add(this.btnCancel);
        this.contentPane.addComponentListener(this.listener);
        setUndecorated(true);
        setResizable(false);
        setBorder(new LineBorder(new Color(200, 230, 230), 2, false));
        setSize(390, 100);
        setLocationRelativeTo(this.parent);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                JLoadingDialog.this.actionPerformed(null);
            }
        });
    }

    public boolean isCancelButtonVisible() {
        return this.cancelButtonVisible;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public boolean isMoveable() {
        return this.move.isMoveable();
    }

    private void reLayout() {
        int width = this.contentPane.getWidth();
        int height = this.contentPane.getHeight();
        Dimension buttonSize = this.btnCancel.getPreferredSize();
        Dimension barSize = this.progressBar.getPreferredSize();
        int labelY = 30 + barSize.height;
        this.progressBar.setBounds(15, 20, width - 30, barSize.height);
        this.lbInfo.setBounds(15, labelY, width - 30, height - 10 - labelY);

        if (this.btnCancel.isVisible()) {
            this.btnCancel.setBounds(width - buttonSize.width - 10, height
                    - buttonSize.height - 10, buttonSize.width,
                    buttonSize.height);
        }
    }

    public void runCommand() {
        this.cancelled = false;
        Task<?> task = new Task<Object>(null);
        this.executor = new ScheduledThreadPoolExecutor(1);
        this.future = this.executor.schedule(task, 0L, TimeUnit.MILLISECONDS);
        setVisible(true);
    }

    public void runCommand(Runnable command) {
        this.command = command;
        runCommand();
    }

    public void setBorder(Border border) {
        getRootPane().setBorder(border);
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

    public void setMoveable(boolean moveable) {
        this.move.setMoveable(moveable);
    }

    public void setStatusInfo(String statusInfo) {
        this.statusInfo = statusInfo;
        this.lbInfo.setText("<html>" + statusInfo);
    }

    public void stopCommand() {
        this.future.cancel(true);
        this.cancelled = true;
    }
}