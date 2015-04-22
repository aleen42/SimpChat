package com.java.ui.util;

import com.java.ui.componentc.JCMenuItem;
import com.java.ui.componentc.JCPopupMenu;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import javax.swing.undo.UndoManager;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.*;

public class TextExtender implements KeyListener, MouseListener, ActionListener {
    private JCMenuItem miCopy;
    private JCMenuItem miCut;
    private JCMenuItem miDel;
    private JCMenuItem miPaste;
    private JCMenuItem miRedo;
    private JCMenuItem miSelectAll;
    private JCMenuItem miUndo;
    private JCPopupMenu popupMenu;
    private boolean popupMenuEnabled;
    private JTextComponent text;
    private UndoManager undo;

    public TextExtender(JTextComponent text) {
        this.text = text;
        this.undo = new UndoManager();
        text.getDocument().addUndoableEditListener(this.undo);
        text.addKeyListener(this);
        setPopupMenuEnabled(true);
    }

    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if (cmd != null) {
            if (cmd.equals("Undo")) {
                this.undo.undo();
            } else if (cmd.equals("Redo")) {
                this.undo.redo();
            } else if (cmd.equals("Cut")) {
                this.text.cut();
            } else if (cmd.equals("Copy")) {
                this.text.copy();
            } else if (cmd.equals("Paste")) {
                this.text.paste();
            } else if (cmd.equals("Del")) {
                this.text.replaceSelection(null);
            } else if (cmd.equals("SelectAll")) {
                this.text.selectAll();
            }
        }
    }

    private JCMenuItem createMenuItem(String text, String actionCommand,
                                      char mnemonic, int keyCode, int modifiers) {
        JCMenuItem item = new JCMenuItem(text, mnemonic);
        item.setActionCommand(actionCommand);
        item.setAccelerator(KeyStroke.getKeyStroke(keyCode, modifiers));
        item.addActionListener(this);
        return item;
    }

    public boolean isPopupMenuEnabled() {
        return this.popupMenuEnabled;
    }

    public void keyPressed(KeyEvent e) {
        if ((this.text.isEnabled()) && (this.text.isEditable())
                && (e.getModifiers() == 2)) {
            int keyCode = e.getKeyCode();

            if ((keyCode == 90) && (this.undo.canUndo())) {
                this.undo.undo();
            } else if ((keyCode == 89) && (this.undo.canRedo())) {
                this.undo.redo();
            }
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
        if ((SwingUtilities.isRightMouseButton(e)) && (this.text.isEnabled())) {
            showPopupMenu(e.getX(), e.getY());
        }
    }

    public void setPopupMenuEnabled(boolean popupMenuEnabled) {
        if (this.popupMenuEnabled != popupMenuEnabled) {
            if (popupMenuEnabled) {
                this.text.addMouseListener(this);
            } else {
                this.text.removeMouseListener(this);
            }

            this.popupMenuEnabled = popupMenuEnabled;
        }
    }

    private void showPopupMenu(int x, int y) {
        String selectedText = this.text.getSelectedText();
        boolean editable = this.text.isEditable();
        boolean hasSelected = (selectedText != null)
                && (!selectedText.isEmpty());
        Transferable trans = this.text.getToolkit().getSystemClipboard()
                .getContents(null);

        if (this.popupMenu == null) {
            this.popupMenu = new JCPopupMenu();
            this.popupMenu.add(this.miUndo = createMenuItem("Undo(U)", "Undo",
                    'U', 90, 2));
            this.popupMenu.add(this.miRedo = createMenuItem("Redo(R)", "Redo",
                    'R', 89, 2));
            this.popupMenu.addSeparator();
            this.popupMenu.add(this.miCut = createMenuItem("Cut(T)", "Cut",
                    'T', 88, 2));
            this.popupMenu.add(this.miCopy = createMenuItem("Copy(C)", "Copy",
                    'C', 67, 2));
            this.popupMenu.add(this.miPaste = createMenuItem("Paste(P)", "Paste",
                    'P', 86, 2));
            this.popupMenu.add(this.miDel = createMenuItem("Del(D)", "Del",
                    'D', 127, 0));
            this.popupMenu.addSeparator();
            this.popupMenu.add(this.miSelectAll = createMenuItem("SelectAll(A)",
                    "SelectAll", 'A', 65, 2));
        }

        this.miUndo.setEnabled((editable) && (this.undo.canUndo()));
        this.miRedo.setEnabled((editable) && (this.undo.canRedo()));
        this.miCut.setEnabled((editable) && (hasSelected));
        this.miCopy.setEnabled(hasSelected);
        this.miDel.setEnabled((editable) && (hasSelected));
        this.miPaste.setEnabled((editable) && (trans != null)
                && (trans.isDataFlavorSupported(DataFlavor.stringFlavor)));
        this.miSelectAll.setEnabled(true);
        this.popupMenu.show(this.text, x, y);
    }
}