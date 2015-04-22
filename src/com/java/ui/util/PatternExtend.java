package com.java.ui.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternExtend {
    public static boolean extendRegex(String regex) {
        return regex.startsWith("[Extend]");
    }

    public static boolean matches(String input, Matcher matcher) {
        Pattern pattern = matcher.pattern();
        String regex = pattern.pattern();

        if (extendRegex(regex)) {
            return matchesExtend(regex, input);
        }

        return matcher.matches();
    }

    private static boolean matchesExtend(String regex, String input) {
        boolean ret = true;
        Integer i = null;
        Double d = null;

        if ((d = parseMaxRange(regex)) != null) {
            try {
                ret = (ret) && (d.doubleValue() >= Double.parseDouble(input));
            } catch (Exception e) {
                ret = false;
            }
        }

        if ((ret) && ((d = parseMinRange(regex)) != null)) {
            try {
                ret = (ret) && (d.doubleValue() <= Double.parseDouble(input));
            } catch (Exception e) {
                ret = false;
            }
        }

        if ((ret) && ((i = parseMaxLength(regex)) != null)) {
            try {
                ret = (ret) && (i.intValue() >= input.length());
            } catch (Exception e) {
                ret = false;
            }
        }

        if ((ret) && ((i = parseDecimalLength(regex)) != null)) {
            try {
                int dotIndex = regex.lastIndexOf('.');
                int decimalLength = dotIndex < 0 ? 0 : input.length()
                        - input.lastIndexOf('.') - 1;
                ret = (ret) && (i.intValue() >= decimalLength);
            } catch (Exception e) {
                ret = false;
            }
        }

        return ret;
    }

    public static Integer parseDecimalLength(String regex) {
        if (extendRegex(regex)) {
            Pattern pattern = Pattern.compile("DecimalLength-\\d+");
            Matcher matcher = pattern.matcher(regex);

            if (matcher.find()) {
                return Integer.valueOf(Integer.parseInt(matcher.group().split(
                        "-")[1]));
            }
        }

        return null;
    }

    public static Integer parseMaxLength(String regex) {
        if (extendRegex(regex)) {
            Pattern pattern = Pattern.compile("MaxLength-\\d+");
            Matcher matcher = pattern.matcher(regex);

            if (matcher.find()) {
                return Integer.valueOf(Integer.parseInt(matcher.group().split(
                        "-")[1]));
            }
        }

        return null;
    }

    public static Double parseMaxRange(String regex) {
        if (extendRegex(regex)) {
            Pattern pattern = Pattern.compile("Max-\\d+");
            Matcher matcher = pattern.matcher(regex);

            if (matcher.find()) {
                return Double.valueOf(Double.parseDouble(matcher.group().split(
                        "-")[1]));
            }
        }

        return null;
    }

    public static Double parseMinRange(String regex) {
        if (extendRegex(regex)) {
            Pattern pattern = Pattern.compile("Min-\\d+");
            Matcher matcher = pattern.matcher(regex);

            if (matcher.find()) {
                return Double.valueOf(Double.parseDouble(matcher.group().split(
                        "-")[1]));
            }
        }

        return null;
    }
}