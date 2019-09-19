package com.kwe.kweplus.util;


import com.kwe.kweplus.dao.BaseJcsjInspectionMapper;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OcrDataUtil {
    @Autowired
    private static BaseJcsjInspectionMapper baseJcsjInspectionMapper;
    /**
     * 清除 .0  逗号（ ,）
     * @param lists
     * @return
     */
    public static  List<Map<String,Object>> CleansingZERO(List<Map<String,Object>> lists){
            String pattern = "(?:\\.0*|(\\.\\d+?)0+)$";//正则去掉小数点后面多于的0
            Pattern r = Pattern.compile(pattern);
            for (int i = 0; i < lists.size(); i++) {
                Matcher m = r.matcher(lists.get(i).get("数量")+"");
                if( m.matches()){
                    lists.get(i).put("数量",(lists.get(i).get("数量")+"").replaceAll(pattern,""));
                }
                if( (lists.get(i).get("数量")+"").contains(",")){
                    lists.get(i).put("数量",(lists.get(i).get("数量")+"").replace(",",""));
                }
                m = r.matcher(lists.get(i).get("箱号")+"");
                if( m.matches()){
                    lists.get(i).put("箱号",(lists.get(i).get("箱号")+"").replaceAll(pattern,""));
                }
                if( (lists.get(i).get("总价")+"").contains(",")){
                    lists.get(i).put("总价",(lists.get(i).get("总价")+"").replace(",",""));
                }
                if( (lists.get(i).get("总价")+"").contains(" ")){
                    lists.get(i).put("总价",(lists.get(i).get("总价")+"").replace(" ",""));
                }
                if( (lists.get(i).get("良品数量")+"").contains(",")){
                    lists.get(i).put("良品数量",(lists.get(i).get("良品数量")+"").replace(",",""));
                }
                if( (lists.get(i).get("batch数量")+"").contains(",")){
                    lists.get(i).put("batch数量",(lists.get(i).get("batch数量")+"").replace(",",""));
                }
            }
            return lists;
        }

    /**
     * 数据转换
     * @param paramList
     * @return
     */
    public static String dataTransition(Integer typeCode,String remakeStr) throws Exception{
        Map map = new HashMap();
        map.put("InspectionType",typeCode);
        map.put("remake",remakeStr);
        try {
            List<Map> paramList = baseJcsjInspectionMapper.getInspectionCode(map);
            if (MyUtils.isNotEmpty(paramList)) {
                return MapUtils.getString(paramList.get(0),"nCode");
            }
        } catch (Exception e) {
            new Exception("数据转换异常");
        }
        return null;
    }



    public static  List<Map<String,Object>> dataCleansing(List<Map<String,Object>> lists){
            lists = CleansingZERO(lists);
            return lists;
    }

}
