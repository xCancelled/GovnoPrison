package ru.nightwill.prison.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public final class DoubleFormatter {
    private static final DecimalFormat SPACE_FORMAT = new DecimalFormat("");
    private static final DecimalFormat COMMA_FORMAT;
    private static final DecimalFormat CHAR_FORMAT;
    private static final String[] CHARS;

    public static String space(double to) {
        String format = SPACE_FORMAT.format(to);
        return format.contains(".") ? format.split("\\.")[0] : format;
    }

    public static String comma(double to) {
        String format = COMMA_FORMAT.format(to);
        return format.contains(".") ? format.split("\\.")[0] : format;
    }

    public static String chars(double toFormat) {
        String formatted = "";

        for (int i = 33; i >= 3; i -= 3) {
            double timely = toFormat / Math.pow(10.0D, (double) i);
            if (timely >= 1.0D) {
                formatted = CHAR_FORMAT.format(timely) + CHARS[i];
                break;
            }
        }

        if (formatted.isEmpty()) {
            formatted = CHAR_FORMAT.format(toFormat);
        }

        while (formatted.contains(".") && (formatted.endsWith("0") || formatted.endsWith("."))) {
            formatted = formatted.substring(0, formatted.length() - 1);
        }

        return formatted.replace(",", ".");
    }

    public static String fix(double to) {
        String formatted;
        for (formatted = CHAR_FORMAT.format(to); formatted.contains(".") && (formatted.endsWith("0") || formatted.endsWith(".")); formatted = formatted.substring(0, formatted.length() - 1)) {
        }

        return formatted.replace(",", ".");
    }

    public static String clear(double to) {
        String format = CHAR_FORMAT.format(to);
        return format.contains(".") ? format.split("\\.")[0] : format;
    }

    private DoubleFormatter() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    static {
        DecimalFormatSymbols custom = new DecimalFormatSymbols();
        custom.setGroupingSeparator(' ');
        custom.setDecimalSeparator('.');
        SPACE_FORMAT.setDecimalFormatSymbols(custom);
        SPACE_FORMAT.setGroupingSize(3);
        COMMA_FORMAT = new DecimalFormat("");
        custom = new DecimalFormatSymbols();
        custom.setGroupingSeparator(',');
        custom.setDecimalSeparator('.');
        COMMA_FORMAT.setDecimalFormatSymbols(custom);
        COMMA_FORMAT.setGroupingSize(3);
        CHAR_FORMAT = new DecimalFormat("##.##");
        CHARS = new String[34];
        CHARS[33] = "d";
        CHARS[30] = "N";
        CHARS[27] = "O";
        CHARS[24] = "S";
        CHARS[21] = "s";
        CHARS[18] = "Q";
        CHARS[15] = "q";
        CHARS[12] = "T";
        CHARS[9] = "B";
        CHARS[6] = "M";
        CHARS[3] = "k";
    }
}
