package com.java.ui.componentc;

import com.java.ui.util.UIResourceManager;
import com.java.ui.util.UIUtil;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicFormattedTextFieldUI;
import javax.swing.plaf.basic.BasicTextUI;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class CFormattedTextFieldUI extends BasicFormattedTextFieldUI {
    private static final Color DISABLED_BG = UIResourceManager
            .getColor("TextDisabledBackground");

    private static final Color NON_EDITABLE_BG = UIResourceManager
            .getColor("TextNonEditableBackground");

    public static ComponentUI createUI(JComponent c) {
        return new CFormattedTextFieldUI();
    }

    private Color getBackground(JCFormattedTextField field) {
        Color color = field.getBackground();

        if (!field.isEnabled()) {
            color = DISABLED_BG;
        } else if (!field.isEditable()) {
            color = NON_EDITABLE_BG;
        }

        return color;
    }

    protected void installDefaults() {
        try {
            Method updateCursorMethod = BasicTextUI.class.getDeclaredMethod(
                    "updateCursor", new Class[0]);
            updateCursorMethod.setAccessible(true);
            updateCursorMethod.invoke(this, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void paintBackground(Graphics g) {
        JTextComponent editor = getComponent();

        if ((editor instanceof JCFormattedTextField)) {
            JCFormattedTextField field = (JCFormattedTextField) editor;
            Color bg = getBackground(field);
            UIUtil.paintBackground(g, field, bg, bg, field.getImage(),
                    field.isImageOnly(), field.getAlpha(),
                    field.getVisibleInsets());
        } else {
            super.paintBackground(g);
        }
    }

    protected void paintSafely(Graphics g) {
        paintBackground(g);
        super.paintSafely(g);
    }

    protected void uninstallDefaults() {
        JTextComponent editor = getComponent();
        try {
            Field dragListenerField = BasicTextUI.class
                    .getDeclaredField("dragListener");
            dragListenerField.setAccessible(true);
            MouseInputAdapter dragListener = (MouseInputAdapter) dragListenerField
                    .get(this);
            editor.removeMouseListener(dragListener);
            editor.removeMouseMotionListener(dragListener);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if ((editor.getCaret() instanceof UIResource)) {
            editor.setCaret(null);
        }

        if ((editor.getHighlighter() instanceof UIResource)) {
            editor.setHighlighter(null);
        }

        if ((editor.getTransferHandler() instanceof UIResource)) {
            editor.setTransferHandler(null);
        }

        if ((editor.getCursor() instanceof UIResource)) {
            editor.setCursor(null);
        }
    }
}