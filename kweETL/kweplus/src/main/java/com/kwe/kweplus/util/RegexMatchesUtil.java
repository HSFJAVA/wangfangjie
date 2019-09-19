package com.kwe.kweplus.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexMatchesUtil {

    public static void main(String args[]) {
        String str = "fda1s23.2";
        String pattern = "[^0-9]";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(str);
        String all = m.replaceAll("");
        System.out.println("phone:" + all);
    }
    public static String removeEnglish(String str){
        String pattern = "[^0-9]";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(str);
        return m.replaceAll("");
    }

}
