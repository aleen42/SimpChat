package com.java.ui.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

public class EncodeChanger {
    private static final char[] HEX_DIGIT = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private static char toHex(int nibble, boolean uppercase) {
        char hex = HEX_DIGIT[(nibble & 0xF)];
        return uppercase ? hex : Character.toLowerCase(hex);
    }

    public static String unicode2UnicodeEsc(String uniStr) {
        return unicode2UnicodeEsc(uniStr, true);
    }

    public static String unicode2UnicodeEsc(String uniStr, boolean uppercase) {
        if ((uniStr == null) || (uniStr.isEmpty()) || (uniStr.trim().isEmpty())) {
            return uniStr;
        }

        int length = uniStr.length();
        StringBuilder ret = new StringBuilder();

        for (int index = 0; index < length; index++) {
            char ch = uniStr.charAt(index);

            if ((ch >= ' ') && (ch <= '~')) {
                ret.append(ch);
            } else {
                ret.append("\\u");
                ret.append(toHex(ch >> '\f' & 0xF, uppercase));
                ret.append(toHex(ch >> '\b' & 0xF, uppercase));
                ret.append(toHex(ch >> '\004' & 0xF, uppercase));
                ret.append(toHex(ch & 0xF, uppercase));
            }
        }

        return ret.toString();
    }

    public static String unicode2UnicodeEscWithoutComment(String uniStr)
            throws IOException {
        return unicode2UnicodeEscWithoutComment(uniStr, true);
    }

    public static String unicode2UnicodeEscWithoutComment(String uniStr,
                                                          boolean uppercase) throws IOException {
        StringBuffer ret = new StringBuffer();
        BufferedReader reader = new BufferedReader(new StringReader(uniStr));
        boolean continueFlag = false;
        String line;
        while ((line = reader.readLine()) != null) {

            String lineWithoutSpace = line.trim();

            if (((lineWithoutSpace.startsWith("#")) || (lineWithoutSpace
                    .startsWith("!"))) && (!continueFlag)) {
                ret.append(line);
            } else {
                continueFlag = line.endsWith("\\");
                ret.append(unicode2UnicodeEsc(line, uppercase));
            }

            ret.append('\n');
        }

        return ret.toString();
    }

    public static String unicodeEsc2Unicode(String unicodeStr) {
        if ((unicodeStr == null) || (unicodeStr.isEmpty())
                || (unicodeStr.trim().isEmpty())) {
            return unicodeStr;
        }

        int length = unicodeStr.length();
        StringBuffer ret = new StringBuffer();

        for (int index = 0; index < length; index++) {
            char ch = unicodeStr.charAt(index);
            boolean needChange = false;

            if ((ch == '\\')
                    && (index < length - 5)
                    && (Character.toUpperCase(unicodeStr.charAt(index + 1)) == 'U')) {
                try {
                    ret.append((char) Integer.parseInt(
                            unicodeStr.substring(index + 2, index + 6), 16));
                    index += 5;
                    needChange = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (needChange)
                continue;
            ret.append(ch);
        }

        return ret.toString();
    }
}