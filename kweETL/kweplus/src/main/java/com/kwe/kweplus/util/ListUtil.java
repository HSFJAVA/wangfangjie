package com.kwe.kweplus.util;

import java.util.*;

/**
 * @author Ly
 * @Desc
 * @date 2019/9/6  16:00
 */
public class ListUtil {

    public static  List<Map> removeRepetition(List<Map>  cfArraylist) {
        //去除重复的Map
        //cfArraylist 表示重复的 List<Map<String，Object>>
        //listMap 表示去重复数据后的 List<Map<String，Object>>
        Map msp = new HashMap();
        List<Map> listMap = new ArrayList<Map>();
        for (Map map:cfArraylist) {

        }
        return listMap;
    }


    // 删除ArrayList中重复元素，保持顺序
    public static List<Map> removeDuplicateWithOrder(List list) {
        Set set = new HashSet();
        List newList = new ArrayList();
        for (Iterator iter = list.iterator(); iter.hasNext();) {
            Object element = iter.next();
            if (set.add(element)){
                newList.add(element);
            }
        }
        list.clear();
        list.addAll(newList);
        return list;
    }

}
