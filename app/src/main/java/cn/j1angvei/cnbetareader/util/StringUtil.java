package cn.j1angvei.cnbetareader.util;

/**
 * Created by Wayne on 2016/7/6.
 */
public final class StringUtil {
    public static String removeBlanks(String input) {
        return input.replaceAll("\\s+", "");
    }

    public static String removeTailingBlanks(String input) {
        return input.replaceAll("\\s+$", "");
    }

}

