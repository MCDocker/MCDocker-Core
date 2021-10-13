package me.hottutorials.utils;

public class StringUtils {

    public static String format(String str, Object... object) {
        String returnString = str;
        for(int i = 0; i < object.length; i++) {
            Object obj = object[i];
            returnString = returnString.replace("${" + i + "}", String.valueOf(obj));
        }
        return returnString;
    }

}
