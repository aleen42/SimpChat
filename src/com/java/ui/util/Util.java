package com.java.ui.util;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.regex.Pattern;

public class Util {
    public static final String IPV4_BLOCK_REGEX = "(2[0-4]\\d|25[0-5]|[01]?\\d?\\d)";
    public static final String IPV4_REGEX = "((2[0-4]\\d|25[0-5]|[01]?\\d?\\d)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d?\\d)";
    public static final String MAC_REGEX = "([0-9A-Fa-f]{2}:){5}[0-9A-Fa-f]{2}";

    public static String bytesToHex(byte[] bytes) {
        return bytesToHex(bytes, null);
    }

    public static String bytesToHex(byte[] bytes, int start, int end,
                                    String split) {
        StringBuilder sb = new StringBuilder();
        boolean hasSplit = (split != null) && (!split.isEmpty());
        end = Math.min(end, bytes.length);

        for (int i = start; i < end; i++) {
            if ((hasSplit) && (i > start)) {
                sb.append(split);
            }

            sb.append(byteToHex(bytes[i]));
        }

        return sb.toString();
    }

    public static String bytesToHex(byte[] bytes, String split) {
        return bytesToHex(bytes, 0, bytes.length, split);
    }

    public static String byteToHex(byte b) {
        String hex = Integer.toHexString(0xFF & b);
        return (b >= 0) && (b <= 15) ? '0' + hex : hex;
    }

    public static boolean checkIPV4(String ip) {
        return Pattern
                .matches(
                        "((2[0-4]\\d|25[0-5]|[01]?\\d?\\d)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d?\\d)",
                        ip);
    }

    public static boolean checkMac(String mac) {
        return Pattern.matches("([0-9A-Fa-f]{2}:){5}[0-9A-Fa-f]{2}", mac);
    }

    public static boolean checkMac(String mac, char split) {
        return Pattern.matches(
                "([0-9A-Fa-f]{2}:){5}[0-9A-Fa-f]{2}".replace(':', split), mac);
    }

    public static Class<?>[] createUnprimitiveClasses(Class<?>[] classes) {
        @SuppressWarnings("rawtypes")
        Class[] unPrimitiveClass = new Class[classes.length];

        for (int i = 0; i < unPrimitiveClass.length; i++) {
            Class<?> clazz = classes[i];

            if (clazz.isAssignableFrom(Integer.TYPE)) {
                unPrimitiveClass[i] = Integer.class;
            } else if (clazz.isAssignableFrom(Long.TYPE)) {
                unPrimitiveClass[i] = Long.class;
            } else if (clazz.isAssignableFrom(Short.TYPE)) {
                unPrimitiveClass[i] = Short.class;
            } else if (clazz.isAssignableFrom(Byte.TYPE)) {
                unPrimitiveClass[i] = Byte.class;
            } else if (clazz.isAssignableFrom(Character.TYPE)) {
                unPrimitiveClass[i] = Character.class;
            } else if (clazz.isAssignableFrom(Double.TYPE)) {
                unPrimitiveClass[i] = Double.class;
            } else if (clazz.isAssignableFrom(Float.TYPE)) {
                unPrimitiveClass[i] = Float.class;
            } else if (clazz.isAssignableFrom(Boolean.TYPE)) {
                unPrimitiveClass[i] = Boolean.class;
            } else {
                unPrimitiveClass[i] = clazz;
            }
        }

        return unPrimitiveClass;
    }

    public static Point getMouseLocation() {
        return MouseInfo.getPointerInfo().getLocation();
    }

    public static Object getSystemClipboardData(DataFlavor flavor) {
        Object ret = null;
        Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable transferable = sysClip.getContents(null);

        if ((transferable != null)
                && (transferable.isDataFlavorSupported(flavor))) {
            try {
                ret = transferable.getTransferData(flavor);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return ret;
    }

    @SuppressWarnings("unchecked")
    public static List<File> getSystemClipboardFiles() {
        return (List<File>) getSystemClipboardData(DataFlavor.javaFileListFlavor);
    }

    public static Image getSystemClipboardImage() {
        return (Image) getSystemClipboardData(DataFlavor.imageFlavor);
    }

    public static String getSystemClipboardText() {
        Object data = getSystemClipboardData(DataFlavor.stringFlavor);
        return data == null ? null : data.toString();
    }

    public static byte[] hexToBytes(String hex) {
        return hexToBytes(hex, null);
    }

    public static byte[] hexToBytes(String hex, String split) {
        if (hex == null) {
            return null;
        }
        if (hex.isEmpty()) {
            return new byte[0];
        }
        if (!isHexString(hex, split)) {
            throw new IllegalArgumentException("Invalid hex:" + hex);
        }

        if ((split != null) && (!split.isEmpty())) {
            hex = hex.replaceAll(split, "");
        }

        int length = hex.length();
        ByteArrayOutputStream baos = new ByteArrayOutputStream(length / 2);

        for (int i = 0; i < length; i += 2) {
            baos.write(Integer.parseInt(hex.substring(i, i + 2), 16));
        }

        return baos.toByteArray();
    }

    public static String hexToString(String hexCode) {
        return hexToString(hexCode, null);
    }

    public static String hexToString(String hexCode, String charsetName) {
        return hexToString(hexCode, null, charsetName);
    }

    public static String hexToString(String hexCode, String split,
                                     String charsetName) {
        if ((hexCode == null) || (hexCode.isEmpty())) {
            return hexCode;
        }

        if (!isHexString(hexCode, split)) {
            throw new IllegalArgumentException("Invalid hexCode:" + hexCode);
        }

        byte[] bytes = hexToBytes(hexCode, split);
        String ret = null;

        if (charsetName == null) {
            ret = new String(bytes);
        } else {
            try {
                ret = new String(bytes, charsetName);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return ret;
    }

    public static boolean isChinese(char ch) {
        Character.UnicodeBlock block = Character.UnicodeBlock.of(ch);

        return (block == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS)
                || (block == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS)
                || (block == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A)
                || (block == Character.UnicodeBlock.GENERAL_PUNCTUATION)
                || (block == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION)
                || (block == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS);
    }

    public static boolean isChinese(String str) {
        return isChinese(str.charAt(0));
    }

    public static boolean isEmpty(Object obj) {
        return isEmpty(obj, false);
    }

    public static boolean isEmpty(Object obj, boolean spaceAllowed) {
        if (obj == null) {
            return true;
        }

        String str = spaceAllowed ? obj.toString() : obj.toString().trim();
        return (str.isEmpty()) || (str.equalsIgnoreCase("null"));
    }

    public static boolean isHexString(String str) {
        return isHexString(str, "");
    }

    public static boolean isHexString(String str, String split) {
        if (str == null) {
            return false;
        }

        split = split == null ? "" : split;
        String regex = "([0-9A-Fa-f]{2}" + split + ")*[0-9A-Fa-f]{2}+";
        return Pattern.matches(regex, str);
    }

    public static int searchFromArray(Object[] objs, Object obj) {
        int pos = -1;
        int index = 0;

        Object[] arrayOfObject = objs;
        int j = objs.length;
        for (int i = 0; i < j; i++) {
            Object o = arrayOfObject[i];

            if (((o == null) && (obj == null)) || (o == obj)
                    || ((o != null) && (obj != null) && (o.equals(obj)))) {
                pos = index;
                break;
            }

            index++;
        }

        return pos;
    }

    public static String stringToHex(String str) {
        return stringToHex(str, null);
    }

    public static String stringToHex(String str, String charsetName) {
        return stringToHex(str, null, charsetName);
    }

    public static String stringToHex(String str, String split,
                                     String charsetName) {
        if ((str == null) || (str.isEmpty())) {
            return str;
        }
        byte[] bytes;
        if (charsetName == null) {
            bytes = str.getBytes();
        } else {
            try {
                bytes = str.getBytes(charsetName);
            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();
                return null;
            }
        }

        return bytesToHex(bytes, split);
    }

    public static void writeDataToSystemClipboard(final Object data,
                                                  final DataFlavor dataFlavor) {
        Transferable transferable = new Transferable() {
            public Object getTransferData(DataFlavor flavor)
                    throws UnsupportedFlavorException, IOException {
                if (isDataFlavorSupported(flavor)) {
                    return data;
                }

                throw new UnsupportedFlavorException(flavor);
            }

            public DataFlavor[] getTransferDataFlavors() {
                return new DataFlavor[]{dataFlavor};
            }

            public boolean isDataFlavorSupported(DataFlavor flavor) {
                return dataFlavor.equals(flavor);
            }
        };
        Toolkit.getDefaultToolkit().getSystemClipboard()
                .setContents(transferable, null);
    }

    public static void writeFilesToSystemClipboard(List<File> files) {
        writeDataToSystemClipboard(files, DataFlavor.javaFileListFlavor);
    }

    public static void writeImageToSystemClipboard(Image image) {
        writeDataToSystemClipboard(image, DataFlavor.imageFlavor);
    }

    public static void writeTextToSystemClipboard(String text) {
        Transferable transferable = new StringSelection(text);
        Toolkit.getDefaultToolkit().getSystemClipboard()
                .setContents(transferable, null);
    }
}