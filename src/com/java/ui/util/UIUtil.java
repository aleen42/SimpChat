package com.java.ui.util;

import com.java.ui.componentc.CRootPaneUI;
import com.java.ui.componentc.JCDialog;
import com.java.ui.componentc.JCFrame;
import sun.awt.shell.ShellFolder;
import sun.swing.SwingUtilities2;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.awt.image.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class UIUtil {
	private static int[] colorFactors;

	public static final SwingUtilities2.AATextInfo COMMON_AATEXT_INFO = new SwingUtilities2.AATextInfo(
			RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB, Integer.valueOf(140));
	public static final SwingUtilities2.AATextInfo DEFAULT_AATEXT_INFO = new SwingUtilities2.AATextInfo(
			RenderingHints.VALUE_TEXT_ANTIALIAS_GASP, null);

	public static void actionLabel(final JLabel label, final Action action) {
		label.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				underLine(true);
			}

			public void mouseExited(MouseEvent e) {
				underLine(false);
			}

			public void mouseReleased(MouseEvent e) {
				if ((action != null) && (SwingUtilities.isLeftMouseButton(e))) {
					action.actionPerformed(new ActionEvent(label, 0, null));
				}
			}

			@SuppressWarnings("unchecked")
			private void underLine(boolean flag) {
				Font font = label.getFont();
				@SuppressWarnings("rawtypes")
				Map map = font.getAttributes();
				map.put(TextAttribute.UNDERLINE, Integer
						.valueOf(flag ? TextAttribute.UNDERLINE_ON.intValue()
								: -1));
				label.setFont(font.deriveFont(map));
			}
		});
	}

	public static Color createDarkerColor(Color color, float factor) {
		return new Color(Math.max((int) (color.getRed() * factor), 0),
				Math.max((int) (color.getGreen() * factor), 0), Math.max(
						(int) (color.getBlue() * factor), 0));
	}

	public static BufferedImage createEdgeBlurryImage(Image image,
			int blurwSize, BufferedImageOp blurFilter, Component c) {
		GraphicsConfiguration gc = getGraphicsConfiguration(c);
		int width = image.getWidth(null);
		int height = image.getHeight(null);
		int imageMaxSize = Math.max(width, height);
		float scale = imageMaxSize / 600.0F;
		int zoomWidth = scale > 1.0F ? Math.round(width / scale) : width;
		int zoomHeight = scale > 1.0F ? Math.round(height / scale) : height;
		BufferedImage bufferedImage = toBufferedImage(image, c);
		BufferedImage blurImage = gc.createCompatibleImage(zoomWidth,
				zoomHeight, 3);
		BufferedImage edgeBlurImage = gc
				.createCompatibleImage(width, height, 3);
		Graphics2D g2d = blurImage.createGraphics();
		g2d.drawImage(scale > 1.0F ? zoomImage(bufferedImage, 1.0F / scale, c)
				: bufferedImage, blurFilter, 0, 0);
		g2d.dispose();
		blurImage = scale > 1.0F ? zoomImage(blurImage, scale, c) : blurImage;
		g2d = edgeBlurImage.createGraphics();
		float alphaStep = 1.0F / blurwSize;
		int x = width - blurwSize;
		int y = height - blurwSize;
		g2d.drawImage(bufferedImage, 0, 0, null);

		for (int i = 1; i <= blurwSize; i++) {
			g2d.setComposite(AlphaComposite.SrcOver.derive(Math.min(1.0F, i
					* alphaStep)));
			g2d.drawImage(blurImage, x, 0, x + 1, y + 1, x, 0, x + 1, y + 1,
					null);
			g2d.drawImage(blurImage, 0, y, x, y + 1, 0, y, x, y + 1, null);
			x++;
			y++;
		}

		g2d.dispose();
		return edgeBlurImage;
	}

	public static Color[] createRandomColors(int count) {
		if ((count <= 0) || (count > 256)) {
			throw new IllegalArgumentException("count must be between 1 to 256");
		}

		Color[] colors = new Color[count];
		Set<Color> set = new HashSet<Color>();
		Random rand = new Random();

		if (colorFactors == null) {
			colorFactors = new int[15];

			for (int i = 0; i < 15; i++) {
				colorFactors[i] = (i * 18);
			}
		}

		while (set.size() < count) {
			int indexB;
			int indexG;
			int indexR = indexG = indexB = 0;

			while ((Math.abs(indexR - indexG) < 3)
					&& (Math.abs(indexG - indexB) < 3)
					&& (Math.abs(indexB - indexR) < 3)) {
				indexR = rand.nextInt(15);
				indexG = rand.nextInt(15);
				indexB = rand.nextInt(15);
			}

			set.add(new Color(colorFactors[indexR], colorFactors[indexG],
					colorFactors[indexB]));
		}

		return set.toArray(colors);
	}

	public static Polygon createRoundRect(int x, int y, int width, int height,
			int cornerSize1, int cornerSize2, int cornerSize3, int cornerSize4) {
		int nPoints = 8;
		int[] xPoints = { x, x + cornerSize1, x + width - cornerSize2,
				x + width, x + width, x + width - cornerSize3, x + cornerSize4,
				x };
		int[] yPoints = { y + cornerSize1, y, y, y + cornerSize2,
				y + height - cornerSize3, y + height, y + height,
				y + height - cornerSize4 };
		return new Polygon(xPoints, yPoints, nPoints);
	}

	public static TexturePaint createTexturePaint(Image image, Component c) {
		BufferedImage bufferedImage = toBufferedImage(image, c);
		return new TexturePaint(bufferedImage, new Rectangle(0, 0,
				bufferedImage.getWidth(), bufferedImage.getHeight()));
	}

	public static BufferedImage cutImage(Image image, Rectangle rect,
			Component c) {
		BufferedImage bufferedImage = getGraphicsConfiguration(c)
				.createCompatibleImage(rect.width, rect.height, 3);
		Graphics g = bufferedImage.getGraphics();
		g.drawImage(image, 0, 0, rect.width, rect.height, rect.x, rect.y,
				rect.width + rect.x, rect.height + rect.y, null);
		g.dispose();
		return bufferedImage;
	}

	public static void escAndEnterAction(RootPaneContainer root,
			JButton defaultButton, Action escAction) {
		if (root != null) {
			registerKeyEvent(root, escAction, "ESC", 27, 0);

			if (defaultButton != null) {
				root.getRootPane().setDefaultButton(defaultButton);
			}
		}
	}

	public static Image getBackgroundImageFromContainer(Container c) {
		if (c == null) {
			return null;
		}

		if ((c instanceof JCFrame)) {
			return ((JCFrame) c).getBackgroundImage();
		}
		if ((c instanceof JCDialog)) {
			return ((JCDialog) c).getBackgroundImage();
		}

		return null;
	}

	public static Font getDefaultFont() {
		return new Font("Dialog", 0, 12);
	}

	public static BufferedImage getEdgeBlurImageFromContainer(Container c) {
		if (c == null) {
			return null;
		}

		if ((c instanceof JCFrame)) {
			return ((JCFrame) c).getEdgeBlurImage();
		}
		if ((c instanceof JCDialog)) {
			return ((JCDialog) c).getEdgeBlurImage();
		}

		return null;
	}

	public static Icon getFileIcon(File file, boolean big) {
		Icon icon = null;

		if (file != null) {
			try {
				ShellFolder folder = ShellFolder.getShellFolder(file);
				Image image = folder.getIcon(big);

				if (image != null) {
					icon = new ImageIcon(image, folder.getFolderType());
				} else {
					icon = UIManager
							.getIcon(file.isDirectory() ? "FileView.directoryIcon"
									: "FileView.fileIcon");
				}
			} catch (FileNotFoundException e) {
				icon = null;
			}
		}

		return icon;
	}

	public static Icon getFileIcon(String format, boolean big) {
		Icon icon = null;
		File file = null;

		if (Util.isEmpty(format)) {
			file = new File(System.getProperty("java.io.tmpdir")
					+ "//icon.temp");
			file.mkdir();
		} else {
			try {
				file = File.createTempFile("icon.temp", '.' + format);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (file != null) {
			icon = getFileIcon(file, big);
			file.delete();
		}

		return icon;
	}

	public static ConvolveOp getGaussianBlurFilter(int radius,
			boolean horizontal) {
		if (radius < 1) {
			throw new IllegalArgumentException("Radius must be >= 1");
		}

		int size = radius * 2 + 1;
		float[] data = new float[size];
		float sigma = radius / 3.0F;
		float twoSigmaSquare = 2.0F * sigma * sigma;
		float sigmaRoot = (float) Math
				.sqrt(twoSigmaSquare * 3.141592653589793D);
		float total = 0.0F;

		for (int i = -radius; i <= radius; i++) {
			float distance = i * i;
			int index = i + radius;
			data[index] = ((float) Math.exp(-distance / twoSigmaSquare) / sigmaRoot);
			total += data[index];
		}

		for (int i = 0; i < data.length; i++) {
			data[i] /= total;
		}

		Kernel kernel = horizontal ? new Kernel(size, 1, data) : new Kernel(1,
				size, data);
		return new ConvolveOp(kernel, 1, null);
	}

	public static GraphicsConfiguration getGraphicsConfiguration(Component c) {
		GraphicsConfiguration gc = null;

		if ((c == null) || ((gc = c.getGraphicsConfiguration()) == null)) {
			GraphicsEnvironment ge = GraphicsEnvironment
					.getLocalGraphicsEnvironment();
			GraphicsDevice gd = ge.getDefaultScreenDevice();
			gc = gd.getDefaultConfiguration();
		}

		return gc;
	}

	public static float getImageAlphaFromContainer(Container c) {
		if (c == null) {
			return 1.0F;
		}

		if ((c instanceof JCFrame)) {
			return ((JCFrame) c).getImageAlpha();
		}
		if ((c instanceof JCDialog)) {
			return ((JCDialog) c).getImageAlpha();
		}

		return 1.0F;
	}

	public static CRootPaneUI.ImageDisplayMode getImageDisplayModeFromContainer(
			Container c) {
		if (c == null) {
			return null;
		}

		if ((c instanceof JCFrame)) {
			return ((JCFrame) c).getImageDisplayMode();
		}
		if ((c instanceof JCDialog)) {
			return ((JCDialog) c).getImageDisplayMode();
		}

		return null;
	}

	public static String getImageFormat(Object obj) {
		String format = "unknown";
		try {
			ImageInputStream input = ImageIO.createImageInputStream(obj);
			Iterator<ImageReader> iter = ImageIO.getImageReaders(input);
			input.close();

			if (iter.hasNext()) {
				format = iter.next().getFormatName();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return format;
	}

	public static JPopupMenu getPopupMenuByItem(JMenuItem item) {
		JPopupMenu menu = (JPopupMenu) item.getParent();
		Component invoker = menu.getInvoker();

		while ((invoker != null) && ((invoker instanceof JMenuItem))) {
			menu = (JPopupMenu) invoker.getParent();
			invoker = menu.getInvoker();
		}

		return menu;
	}

	public static RootPaneContainer getRootPaneContainerFromComponent(
			Component component) {
		if ((component instanceof JMenuItem)) {
			Container container = component.getParent();
			component = ((JPopupMenu) container).getInvoker();
			do {
				container = component.getParent();

				if ((container instanceof JMenuBar)) {
					component = container;
					break;
				}

				component = ((JPopupMenu) container).getInvoker();

				if (component == null)
					break;
			} while ((component instanceof JMenuItem));
		} else if ((component instanceof JPopupMenu)) {
			component = ((JPopupMenu) component).getInvoker();
		}

		while ((component != null)
				&& (!(component instanceof RootPaneContainer))) {
			component = component.getParent();
		}

		return component == null ? null : (RootPaneContainer) component;
	}

	public static JRootPane getRootPaneFromComponent(Component component) {
		RootPaneContainer container = getRootPaneContainerFromComponent(component);
		return container == null ? null : container.getRootPane();
	}

	public static Font getTitleFont(boolean isCH) {
		String osName = System.getProperty("os.name").toLowerCase();
		String osVersion = System.getProperty("os.version");
		Font font;

		if (osName.startsWith("windows")) {

			if (osVersion.compareTo("6") >= 0) {
				font = new Font("����", 1, isCH ? 13 : 12);
			} else {

				if (isCH) {
					font = new Font("����", 1, 13);
				} else {
					font = new Font("Tahoma", 1, 12);
				}
			}
		} else {
			font = new Font("Dialog", 1, isCH ? 13 : 12);
		}

		return font;
	}

	public static Window getWindowFromComponent(Component component) {
		if ((component instanceof JMenuItem)) {
			Container container = component.getParent();
			component = ((JPopupMenu) container).getInvoker();
			do {
				container = component.getParent();

				if ((container instanceof JMenuBar)) {
					component = container;
					break;
				}

				component = ((JPopupMenu) container).getInvoker();

				if (component == null)
					break;
			} while ((component instanceof JMenuItem));
		} else if ((component instanceof JPopupMenu)) {
			component = ((JPopupMenu) component).getInvoker();
		}

		while ((component != null) && (!(component instanceof Window))) {
			component = component.getParent();
		}

		return component == null ? null : (Window) component;
	}

	public static void hideInputRect() {
		System.setProperty("java.awt.im.style", "on-the-spot");
	}

	public static void hideToolTip(JComponent component) {
		setToolTipVisible(component, false);
	}

	public static Image imageRotate(Image image, double angle, Component c) {
		double radians = Math.toRadians(angle % 360.0D);
		int oldWidth = image.getWidth(null);
		int oldHeight = image.getHeight(null);
		int newWidth = (int) (Math.abs(Math.cos(radians) * oldWidth) + Math
				.abs(Math.sin(radians) * oldHeight));
		int newHeight = (int) (Math.abs(Math.sin(radians) * oldWidth) + Math
				.abs(Math.cos(radians) * oldHeight));
		int centerX = newWidth / 2;
		int centerY = newHeight / 2;
		BufferedImage bufferedImage = getGraphicsConfiguration(c)
				.createCompatibleImage(newWidth, newHeight, 3);
		Graphics2D g2d = (Graphics2D) bufferedImage.getGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.rotate(radians, centerX, centerY);
		g2d.drawImage(image, (newWidth - oldWidth) / 2,
				(newHeight - oldHeight) / 2, null);
		g2d.rotate(-radians, centerX, centerY);
		g2d.dispose();
		return bufferedImage;
	}

	public static void initToolTipForSystemStyle() {
		try {
			String className = UIManager.getSystemLookAndFeelClassName();
			Class<?> clazz = Class.forName(className);
			LookAndFeel laf = (LookAndFeel) clazz.newInstance();
			Enumeration<Object> keys = laf.getDefaults().keys();

			while (keys.hasMoreElements()) {
				Object key = keys.nextElement();

				if (!key.toString().startsWith("ToolTip."))
					continue;
				UIManager.getDefaults().put(key, laf.getDefaults().get(key));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void optimizeText(JComponent c) {
		if (c.getClientProperty("AATextPropertyBackKey") == null) {
			c.putClientProperty("AATextPropertyBackKey",
					c.getClientProperty(SwingUtilities2.AA_TEXT_PROPERTY_KEY));
		}

		c.putClientProperty(SwingUtilities2.AA_TEXT_PROPERTY_KEY,
				DEFAULT_AATEXT_INFO);
	}

	public static void optimizeTextWithChildren(JComponent c) {
		optimizeText(c);

		for (Component child : c.getComponents()) {
			if (!(child instanceof JComponent))
				continue;
			optimizeTextWithChildren((JComponent) child);
		}
	}

	public static void paintBackground(Graphics g, JComponent c,
			Color background, Color disabledBackground, Image image,
			boolean imageOnly, float alpha, Insets visibleInsets) {
		if ((alpha > 0.0D) && ((!imageOnly) || (image != null))) {
			Graphics2D g2d = (Graphics2D) g;
			Composite oldComposite = g2d.getComposite();
			g2d.setComposite(AlphaComposite.SrcOver.derive(alpha));

			if (!imageOnly) {
				Color oldColor = g2d.getColor();
				g2d.setColor(c.isEnabled() ? background : disabledBackground);
				g2d.fillRect(
						visibleInsets.left,
						visibleInsets.top,
						c.getWidth() - visibleInsets.left - visibleInsets.right,
						c.getHeight() - visibleInsets.top
								- visibleInsets.bottom);
				g2d.setColor(oldColor);
			}

			if (image != null) {
				Object oldHintValue = g2d
						.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);
				g2d.drawImage(
						image,
						visibleInsets.left,
						visibleInsets.top,
						c.getWidth() - visibleInsets.left - visibleInsets.right,
						c.getHeight() - visibleInsets.top
								- visibleInsets.bottom, c);
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						oldHintValue);
			}

			g2d.setComposite(oldComposite);
		}
	}

	public static void paintImage(Graphics g, Image image, Insets imageInsets,
			Rectangle paintRect, ImageObserver observer) {
		int x = paintRect.x;
		int y = paintRect.y;
		int width = paintRect.width;
		int height = paintRect.height;

		if ((width <= 0) || (height <= 0) || (x + width <= 0)
				|| (y + height <= 0)) {
			return;
		}

		Graphics2D g2d = (Graphics2D) g;
		Object oldHintValue = g2d
				.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
		int imageLeft = imageInsets.left;
		int imageRight = imageInsets.right;
		int imageTop = imageInsets.top;
		int imageBottom = imageInsets.bottom;
		int imageWidth = image.getWidth(observer);
		int imageHeight = image.getHeight(observer);

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.translate(x, y);

		g2d.drawImage(image, 0, 0, imageLeft, imageTop, 0, 0, imageLeft,
				imageTop, observer);
		g2d.drawImage(image, imageLeft, 0, width - imageRight, imageTop,
				imageLeft, 0, imageWidth - imageRight, imageTop, observer);
		g2d.drawImage(image, width - imageRight, 0, width, imageTop, imageWidth
				- imageRight, 0, imageWidth, imageTop, observer);

		g2d.drawImage(image, 0, imageTop, imageLeft, height - imageBottom, 0,
				imageTop, imageLeft, imageHeight - imageBottom, observer);
		g2d.drawImage(image, imageLeft, imageTop, width - imageRight, height
				- imageBottom, imageLeft, imageTop, imageWidth - imageRight,
				imageHeight - imageBottom, observer);
		g2d.drawImage(image, width - imageRight, imageTop, width, height
				- imageBottom, imageWidth - imageRight, imageTop, imageWidth,
				imageHeight - imageBottom, observer);

		g2d.drawImage(image, 0, height - imageBottom, imageLeft, height, 0,
				imageHeight - imageBottom, imageLeft, imageHeight, observer);
		g2d.drawImage(image, imageLeft, height - imageBottom, width
				- imageRight, height, imageLeft, imageHeight - imageBottom,
				imageWidth - imageRight, imageHeight, observer);
		g2d.drawImage(image, width - imageRight, height - imageBottom, width,
				height, imageWidth - imageRight, imageHeight - imageBottom,
				imageWidth, imageHeight, observer);
		g2d.translate(-x, -y);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldHintValue);
	}

	public static void registerKeyEvent(RootPaneContainer root, Action action,
			String keyName, int keyCode, int modifiers) {
		if (root != null) {
			JRootPane rootPane = root.getRootPane();
			InputMap inputMap = rootPane.getInputMap(2);
			inputMap.put(KeyStroke.getKeyStroke(keyCode, modifiers), keyName);
			rootPane.getActionMap().put(keyName, action);
		}
	}

	public static void setPopupMenuConsumeEventOnClose(boolean consume) {
		UIManager
				.put("PopupMenu.consumeEventOnClose", Boolean.valueOf(consume));
	}

	private static void setToolTipVisible(JComponent component, boolean visible) {
		String actionName = visible ? "postTip" : "hideTip";
		Action action = component.getActionMap().get(actionName);

		if (action != null) {
			ActionEvent event = new ActionEvent(component, 1001, actionName,
					EventQueue.getMostRecentEventTime(), 0);
			action.actionPerformed(event);
		}
	}

	public static void setUIFont(Font font) {
		Enumeration<Object> keys = UIManager.getDefaults().keys();

		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);

			if (!(value instanceof Font))
				continue;
			UIManager.put(key, font);
		}
	}

	public static void showToolTip(JComponent component) {
		setToolTipVisible(component, true);
	}

	public static BufferedImage toBufferedImage(Icon icon, Component c) {
		return toBufferedImage(icon, 1.0F, c);
	}

	public static BufferedImage toBufferedImage(Icon icon, float alpha,
			Component c) {
		BufferedImage bufferedImage = getGraphicsConfiguration(c)
				.createCompatibleImage(icon.getIconWidth(),
						icon.getIconHeight(), 3);
		Graphics2D g2d = (Graphics2D) bufferedImage.getGraphics();

		if ((alpha >= 0.0D) && (alpha < 1.0D)) {
			g2d.setComposite(AlphaComposite.SrcOver.derive(alpha));
		}

		icon.paintIcon(null, g2d, 0, 0);
		g2d.dispose();
		return bufferedImage;
	}

	public static BufferedImage toBufferedImage(Image image, Component c) {
		return toBufferedImage(image, 1.0F, c);
	}

	public static BufferedImage toBufferedImage(Image image, float alpha,
			Component c) {
		BufferedImage bufferedImage;

		if (((image instanceof BufferedImage))
				&& ((alpha < 0.0D) || (alpha >= 1.0D))) {
			bufferedImage = (BufferedImage) image;
		} else {
			bufferedImage = getGraphicsConfiguration(c).createCompatibleImage(
					image.getWidth(null), image.getHeight(null), 3);
			Graphics2D g2d = (Graphics2D) bufferedImage.getGraphics();

			if ((alpha >= 0.0D) && (alpha < 1.0D)) {
				g2d.setComposite(AlphaComposite.SrcOver.derive(alpha));
			}

			g2d.drawImage(image, 0, 0, null);
			g2d.dispose();
		}

		return bufferedImage;
	}

	public static void undoOptimizeText(JComponent c) {
		c.putClientProperty(SwingUtilities2.AA_TEXT_PROPERTY_KEY,
				c.getClientProperty("AATextPropertyBackKey"));
	}

	public static void undoOptimizeTextWithChildren(JComponent c) {
		undoOptimizeText(c);

		for (Component child : c.getComponents()) {
			if (!(child instanceof JComponent))
				continue;
			undoOptimizeTextWithChildren((JComponent) child);
		}
	}

	public static BufferedImage zoomImage(Image image, float scale, Component c) {
		int width = Math.round(image.getWidth(null) * scale);
		int height = Math.round(image.getHeight(null) * scale);
		BufferedImage bufferedImage = getGraphicsConfiguration(c)
				.createCompatibleImage(width, height, 3);
		Graphics2D g2d = (Graphics2D) bufferedImage.getGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.drawImage(image, 0, 0, width, height, null);
		g2d.dispose();
		return bufferedImage;
	}

	protected void actionPerformed(ActionEvent actionEvent) {

	}
}