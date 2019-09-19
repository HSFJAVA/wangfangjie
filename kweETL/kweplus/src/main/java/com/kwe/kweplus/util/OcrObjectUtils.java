package com.kwe.kweplus.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OcrObjectUtils {

    private static String months[][] = {
            {"JANUARY","01"},
            {"FEBRUARY","02"},
            {"MARCH","03"},
            {"APRIL","04"},
            {"MAY","05"},
            {"JUNE","06"},
            {"JULY","07"},
            {"AUGUST","08"},
            {"SEPTEMBER","09"},
            {"OCTOBER","10"},
            {"NOVEMBER","11"},
            {"DECEMBER","12"}
    };

    private static String data_dec[] = {".","-","/"};

    public static JSONArray getJSONArray(JSONObject jsonObject, String key){
        JSONArray value=null;
        try{
            JSONObject object =  jsonObject.getJSONObject(key);
            value= object.getJSONArray("value");
        }catch (Exception e){}
        return value == null? null: value;
    }

    public static String getString(JSONObject jsonObject, String key){
        Object value=null;
        try{
            JSONObject object =  jsonObject.getJSONObject(key);
            value= object.getString("value");
        }catch (Exception e){}
        return value == null? null: ((String) value).trim();
    }


    public static String getImagePath(JSONObject jsonObject, String key){
        String value=null;
        try{
            JSONObject object =  jsonObject.getJSONObject(key);
            value= object.getString("imagename");
        }catch (Exception e){}
        return value == null? null: value.trim();
    }

    public static Object getLocations(JSONObject jsonObject, String key){
        Object value=null;
        try{
            JSONObject object =  jsonObject.getJSONObject(key);
            value= object.getString("locations");
        }catch (Exception e){}
        return value == null? null: value;
    }

    public static String getString(JSONObject jsonObject, String key, int length){
        String value = (String)getString(jsonObject,key);
        if(value != null && value.length() > length){
            return value.substring(0,length);
        }
        return value;
    }

    public static BigDecimal getBigDecimal(JSONObject jsonObject, String key){
        String value = null;
        try{
            JSONObject object =  jsonObject.getJSONObject(key);
            value= object.getString("value");
        }catch (Exception e){}
        if(value != null){
            value = value.replace(" ","");
            value = value.replace(",","");
            if(!value.equals("") && isNumericzidai(value)){
                return new BigDecimal(value);
            }else{
                return null;
            }

        }
        return null;
    }



    public static boolean isNumericzidai(String str) {
        Pattern pattern = Pattern.compile("-?[0-9]+\\.?[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    private static String handleDateString(String date){
        for(String[] month:months){
            if(date.indexOf(month[0]) > -1){
                date = date.replace(month[0],month[1]);
                break;
            }

        }
        return date;
    }

    private static int findDateDec(String date){
        for(String dec:data_dec){
            int index = date.indexOf(dec);
            if(index > -1){
                return index;
            }

        }
        return -1;
    }

    private static String replaceDateDec(String date){
        for(String dec:data_dec){
            date = date.replace(dec,"");

        }
        return date;
    }

}
