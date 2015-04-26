package com.java.ui.componentc;

import com.java.ui.util.TextExtender;
import com.java.ui.util.UIResourceManager;
import com.java.ui.util.UIUtil;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class JCTextField extends JTextField {

    private static final long serialVersionUID = -2877157868704955530L;
    
    private String text_value;
    private BufferedImage buffer = null; 
    private float alpha;
    private boolean borderChange;
    private Border disabledBorder;
    private TextExtender extender;
    private Image image;
    private boolean imageOnly;
    private boolean leadingTextVisible;
    private boolean isFocused;
    private MouseListener listener;
    private Border nonEditableBorder;
    private Border nonEditableRolloverBorder;
    private Border normalBorder;
    private Border rolloverBorder;
    private Border focusBorder;
    private Insets visibleInsets;

    public JCTextField() {
        this(null, null, 0);
    }

    public JCTextField(Document doc, String text, int columns) {
        super(doc, text, columns);
        setUI(new CTextFieldUI());
        super.setOpaque(false);
        setFont(UIUtil.getDefaultFont());
        setBackground(UIResourceManager.getWhiteColor());
        setForeground(Color.BLACK);
        setCaretColor(Color.BLACK);
        setSelectionColor(new Color(49, 106, 197));
        setSelectedTextColor(UIResourceManager.getWhiteColor());
        setDisabledTextColor(new Color(123, 123, 122));
        setCursor(new Cursor(2));
        setMargin(new Insets(0, 0, 0, 0));
        super.setBorder(this.normalBorder = new ImageBorder(UIResourceManager.getImageByName("border_normal.png"), 5, 6, 3, 4));
        this.extender = new TextExtender(this);
        this.rolloverBorder = UIResourceManager.getBorder("TextRolloverBorder");
        this.focusBorder = new ImageBorder(getToolkit().getImage("./Pic/border_focus.png"), 5, 6, 3, 4);
//        this.rolloverBorder = new ImageBorder(UIResourceManager.getImageByName("border_rollover.png"));
        this.nonEditableBorder = UIResourceManager.getBorder("TextNonEditableBorder");
        this.nonEditableRolloverBorder = UIResourceManager.getBorder("TextNonEditableRolloverBorder");
        this.disabledBorder = UIResourceManager.getBorder("TextDisabledBorder");
        this.alpha = 1.0F;
        this.visibleInsets = new Insets(1, 1, 1, 1);
        this.borderChange = true;
        this.leadingTextVisible = true;
        
        addMouseListener(new MouseAdapter() 
        {
            public void mouseEntered(MouseEvent e) 
            {
                JCTextField.this.mouseIn();
            }

            public void mouseExited(MouseEvent e) 
            {
                JCTextField.this.mouseOut();
            }
        });
        
        addFocusListener(new FocusAdapter()
        {
        	public void focusGained(FocusEvent e)
        	{
        		isFocused = true;
        		JCTextField.this.gotfocus();
        	}
        	
        	public void focusLost(FocusEvent e)
        	{
        		isFocused = false;
        		JCTextField.this.lostfocus();
        	}
        });
        
        getDocument().addDocumentListener(new DocumentListener(){
        	public void insertUpdate(DocumentEvent e) 
        	{
        		JCTextField.this.text_value = getText();
            }

             public void removeUpdate(DocumentEvent e) 
             {
            	 JCTextField.this.text_value = getText();
             }
        	
        	public void changedUpdate(DocumentEvent e)
        	{
        		JCTextField.this.text_value = getText();
            }
        });
    }
    
    @Override 
    public void paintComponent(Graphics g) { 												//重新绘制缓冲，解决输入法导致透明panel变白的问题 			
        Component window = this.getTopLevelAncestor();  
        if (window instanceof Window && !((Window)window).isOpaque()) {  
            // This is a translucent window, so we need to draw to a buffer  
            // first to work around a bug in the DirectDraw rendering in Swing.  
            int w = this.getWidth();  
            int h = this.getHeight();  
            if (buffer == null || buffer.getWidth() != w || buffer.getHeight() != h) {  
                // Create a new buffer based on the current size.  
                GraphicsConfiguration gc = this.getGraphicsConfiguration();  
                buffer = gc.createCompatibleImage(w, h, BufferedImage.TRANSLUCENT);  
            }  

            // Use the super class's paintComponent implementation to draw to  
            // the buffer, then write that buffer to the original Graphics object.  
            Graphics bufferGraphics = buffer.createGraphics();  
            try {  
                super.paintComponent(bufferGraphics);  
            } finally {  
                bufferGraphics.dispose();  
            }  
            g.drawImage(buffer, 0, 0, w, h, 0, 0, w, h, null);  
        } else {  
            // This is not a translucent window, so we can call the super class  
            // implementation directly.  
            super.paintComponent(g);  
        }          
    }  
    
    public String getvalue()
    {
    	return this.text_value;
    }

    public JCTextField(int columns) {
        this(null, null, columns);
    }

    public JCTextField(String text) {
        this(null, text, 0);
    }

    public JCTextField(String text, int columns) {
        this(null, text, columns);
    }

    public void clearBorderListener() {
        removeMouseListener(this.listener);
        this.borderChange = false;
    }

    public float getAlpha() {
        return this.alpha;
    }

    public Image getImage() {
        return this.image;
    }

    public Insets getVisibleInsets() {
        return this.visibleInsets;
    }

    public boolean isImageOnly() {
        return this.imageOnly;
    }

    public boolean isLeadingTextVisible() {
        return this.leadingTextVisible;
    }

    public boolean isPopupMenuEnabled() {
        return this.extender.isPopupMenuEnabled();
    }

    private void mouseIn() {
        if ((this.normalBorder != null) && (isEnabled() && !isFocused)) {
            super.setBorder(isEditable() ? this.rolloverBorder : this.nonEditableRolloverBorder);
        }
    }

    private void mouseOut() {
        if ((this.normalBorder != null) && (isEnabled()) && !isFocused) {
            super.setBorder(isEditable() ? this.normalBorder : this.nonEditableBorder);
        }
    }
    
    private void gotfocus() {
        if ((this.normalBorder != null) && (isEnabled())) {
            super.setBorder(this.focusBorder);
        }
    }
    
    private void lostfocus() {
        if ((this.normalBorder != null) && (isEnabled())) {
        	super.setBorder(isEditable() ? this.normalBorder : this.nonEditableBorder);
        }
    }

    public void setAlpha(float alpha) {
        if ((alpha >= 0.0F) && (alpha <= 1.0F)) {
            this.alpha = alpha;
            repaint();
        } else {
            throw new IllegalArgumentException("Invalid alpha:" + alpha);
        }
    }

    public void setBorder(Border border) {
        this.normalBorder = border;

        if ((border == null) && (this.visibleInsets != null)) {
            this.visibleInsets.set(0, 0, 0, 0);
        }

        super.setBorder(border);
    }

    public void setEditable(boolean editable) {
        super.setEditable(editable);

        if (this.borderChange) {
            mouseOut();
        }
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        if (this.borderChange) {
            if (enabled) {
                mouseOut();
            } else if (this.normalBorder != null) {
                super.setBorder(this.disabledBorder);
            }
        }
    }

    public void setImage(Image image) {
        this.image = image;
        repaint();
    }

    public void setImageOnly(boolean imageOnly) {
        this.imageOnly = imageOnly;
        repaint();
    }

    public void setLeadingTextVisible(boolean leadingTextVisible) {
        this.leadingTextVisible = leadingTextVisible;
    }

    @Deprecated
    public void setOpaque(boolean isOpaque) {
    }

    public void setPopupMenuEnabled(boolean popupMenuEnabled) {
        this.extender.setPopupMenuEnabled(popupMenuEnabled);
    }

    public void setText(String text) {
        super.setText(text);

        if (this.leadingTextVisible) {
            setSelectionStart(0);
            setSelectionEnd(0);
        }
    }

    public void setVisibleInsets(int top, int left, int bottom, int right) {
        this.visibleInsets.set(top, left, bottom, right);
        repaint();
    }

    @Deprecated
    public void updateUI() {
    }
}