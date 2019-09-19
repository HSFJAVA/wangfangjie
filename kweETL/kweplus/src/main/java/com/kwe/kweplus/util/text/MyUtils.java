package com.kwe.kweplus.util.text;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class MyUtils {

    public static String returnDay(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        return df.format(new Date());
    }

    public static String return24Time(){
        SimpleDateFormat smf = new SimpleDateFormat("HHmmss");//设置日期格式
        String time = smf.format(new Date());
        return time;
    }

    public static String returnTime(){
        SimpleDateFormat smf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String time = smf.format(new Date());
        return time;
    }

    public static void main(String[] args) {
//        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date date =null;
//        try {
//           date = format.parse(MyUtils.return24Time());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        System.out.println(MyUtils.return24Time());
    }

}
