package com.java.ui.util;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;

public class SwingResourceManager {
    private static HashMap<String, Image> imageMap = new HashMap<String, Image>();

    public static void clearImages(String section) {
        for (Iterator<String> iterator = imageMap.keySet().iterator(); iterator
                .hasNext(); ) {
            String key = (String) iterator.next();

            if (!key.startsWith(section + '|')) {
                continue;
            }

            Image image = (Image) imageMap.get(key);
            image.flush();
            iterator.remove();
        }
    }

    public static ImageIcon getIcon(Class<?> clazz, String path) {
        return getIcon(getImage(clazz, path));
    }

    public static ImageIcon getIcon(Image image) {
        return image == null ? null : new ImageIcon(image);
    }

    public static ImageIcon getIcon(String path) {
        return getIcon("default", path);
    }

    public static ImageIcon getIcon(String section, String path) {
        return getIcon(getImage(section, path));
    }

    public static Image getImage(Class<?> clazz, String path) {
        String key = clazz.getName() + '|' + path;

        Image image = (Image) imageMap.get(key);

        if (image == null) {
            if ((path.length() > 0) && (path.charAt(0) == '/')) {
                String newPath = path.substring(1);

                image = getImage(new BufferedInputStream(clazz.getClassLoader()
                        .getResourceAsStream(newPath)));
            } else {
                image = getImage(clazz.getResourceAsStream(path));
            }

            imageMap.put(key, image);
        }

        return image;
    }

    private static Image getImage(InputStream is) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buf = new byte[4096];
            while (true) {
                int n = is.read(buf);

                if (n == -1) {
                    break;
                }

                baos.write(buf, 0, n);
            }

            baos.close();
            return Toolkit.getDefaultToolkit().createImage(baos.toByteArray());
        } catch (Throwable e) {
        }
        return null;
    }

    public static Image getImage(String path) {
        return getImage("default", path);
    }

    public static Image getImage(String section, String path) {
        String key = section + '|' + SwingResourceManager.class.getName() + '|'
                + path;
        Image image = (Image) imageMap.get(key);

        if (image == null) {
            try {
                FileInputStream fis = new FileInputStream(path);
                image = getImage(fis);
                imageMap.put(key, image);
                fis.close();
            } catch (IOException e) {
                return null;
            }
        }

        return image;
    }
}