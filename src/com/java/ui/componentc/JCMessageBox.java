package com.java.ui.componentc;

import com.java.ui.component.JImagePane;
import com.java.ui.layout.LineLayout;
import com.java.ui.util.UIResourceManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class JCMessageBox extends JCDialog implements ActionListener {
    public static enum MessageType {
        ERROR, INFORMATION, QUESTION, WARNING;
    }

    public static final int CANCEL_OPTION = 2;
    public static final int CLOSE_OPTION = 0;
    public static final int NO_OPTION = 8;
    public static final int OK_OPTION = 1;
    private static final long serialVersionUID = 3983953036367048891L;
    public static final int YES_OPTION = 4;

    public static JCMessageBox createErrorMessageBox(Window parent,
                                                     String title, String message) {
        return createErrorMessageBox(parent, title, message, 1);
    }

    public static JCMessageBox createErrorMessageBox(Window parent,
                                                     String title, String message, int option) {
        return createMessageBox(parent, title, message, MessageType.ERROR,
                option);
    }

    public static JCMessageBox createInformationMessageBox(Window parent,
                                                           String title, String message) {
        return createInformationMessageBox(parent, title, message, 1);
    }

    public static JCMessageBox createInformationMessageBox(Window parent,
                                                           String title, String message, int option) {
        return createMessageBox(parent, title, message,
                MessageType.INFORMATION, option);
    }

    public static JCMessageBox createMessageBox(Window parent, String title,
                                                String message, MessageType messageType, int option) {
        return new JCMessageBox(parent, title, message, messageType, option);
    }

    public static JCMessageBox createQuestionMessageBox(Window parent,
                                                        String title, String message) {
        return createQuestionMessageBox(parent, title, message, 12);
    }

    public static JCMessageBox createQuestionMessageBox(Window parent,
                                                        String title, String message, int option) {
        return createMessageBox(parent, title, message, MessageType.QUESTION,
                option);
    }

    public static JCMessageBox createWarningMessageBox(Window parent,
                                                       String title, String message) {
        return createWarningMessageBox(parent, title, message, 3);
    }

    public static JCMessageBox createWarningMessageBox(Window parent,
                                                       String title, String message, int option) {
        return createMessageBox(parent, title, message, MessageType.WARNING,
                option);
    }

    private JCButton btnCancel;

    private JCButton btnNo;

    private JCButton btnOK;

    private JCButton btnYes;

    private Map<MessageType, Icon> iconMap;

    private JCLabel lbMessage;

    private int option;

    private JCMessageBox(Window owner, String title, String message,
                         MessageType messageType, int option) {
        super(owner, title, Dialog.ModalityType.DOCUMENT_MODAL);
        init(message, messageType, option);
    }

    public void actionPerformed(ActionEvent e) {
        JCButton button = (JCButton) e.getSource();
        this.option = Integer.parseInt(button.getActionCommand());
        dispose();
    }

    public Dimension getPreferredSize() {
        int width = Math.max(315, this.lbMessage.getPreferredSize().width) + 15;
        int height = Math.max(150,
                this.lbMessage.getPreferredSize().height + 15 + 28 + 34);
        return new Dimension(width, height);
    }

    private void init(String message, MessageType messageType, int option) {
        this.iconMap = new HashMap<MessageType, Icon>();
        this.iconMap.put(MessageType.ERROR,
                UIResourceManager.getIcon("MessageBoxErrorIcon"));
        this.iconMap.put(MessageType.INFORMATION,
                UIResourceManager.getIcon("MessageBoxInfomationIcon"));
        this.iconMap.put(MessageType.QUESTION,
                UIResourceManager.getIcon("MessageBoxQuestionIcon"));
        this.iconMap.put(MessageType.WARNING,
                UIResourceManager.getIcon("MessageBoxWarningIcon"));

        JImagePane buttonPane = new JImagePane();
        this.lbMessage = new JCLabel(message, this.iconMap.get(messageType), 2);
        this.btnOK = new JCButton("纭畾");
        this.btnCancel = new JCButton("鍙栨秷");
        this.btnYes = new JCButton("鏄�");
        this.btnNo = new JCButton("鍚�");
        JCButton[] buttons = {this.btnOK, this.btnYes, this.btnNo,
                this.btnCancel};
        int[] options = {1, 4, 8, 2};
        Dimension buttonSize = new Dimension(69, 21);
        int index = 0;
        boolean hasDefaultButton = false;

        this.lbMessage.setBackground(UIResourceManager.getWhiteColor());
        this.lbMessage.setBackgroundAlpha(0.9F);
        this.lbMessage.setIconTextGap(16);
        this.lbMessage.setHorizontalAlignment(2);
        this.lbMessage.setVerticalAlignment(1);
        this.lbMessage.setVerticalTextPosition(1);
        this.lbMessage.setBorder(new EmptyBorder(15, 25, 15, 25));
        this.lbMessage.setDeltaY(5);
        buttonPane.setLayout(new LineLayout(6, 0, 0, 0, 0, 10, 10, 0));
        buttonPane.setPreferredSize(new Dimension(-1, 33));
        buttonPane.setBorder(new EmptyBorder(5, 9, 0, 9));
        buttonPane.setBackground(new Color(255, 255, 255, 170));
        buttonPane.setCornerSizeAt(3, 2);
        buttonPane.setCornerSizeAt(4, 2);

        for (JCButton button : buttons) {
            button.setActionCommand(String.valueOf(options[index]));
            button.setPreferredSize(buttonSize);
            button.setVisible((option & options[index]) != 0);
            button.addActionListener(this);
            buttonPane.add(button, "End");
            index++;

            if ((hasDefaultButton) || (!button.isVisible()))
                continue;
            getRootPane().setDefaultButton(button);
            hasDefaultButton = true;
        }

        getContentPane().setLayout(new LineLayout(0, 1, 1, 3, 1, 10, 10, 1));
        getContentPane().add(this.lbMessage, "MiddleFill");
        getContentPane().add(buttonPane, "EedFill");
    }

    public int open() {
        this.option = 0;
        setSize(getPreferredSize());
        setLocationRelativeTo(getParent());
        setResizable(false);
        setVisible(true);
        return this.option;
    }
}