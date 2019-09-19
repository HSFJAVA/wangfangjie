package com.kwe.kweplus.util.ocrUtil;


import java.util.*;

public class OcrUtil {

    /**
     * 模糊算法取概率最大值
     * @param word
     * @param word2
     * @return
     */

    public static double Count(String word, String word2) {
        int w1 = word.length(); //横
        int w2 = word2.length(); //纵
        int[][] arr = new int[w1 + 1][w2 + 1];
        if (w1 == 0 || w2 == 0) return (1 - 0);
        for (int i = 0; i < w1; i++) {
            arr[i][0] = i;
        }
        for (int i = 0; i < w2; i++) {
            arr[0][i] = i;
        }

        for (int i = 1; i <= w1; i++) {
            int cost = 1;
            char ch1 = word.charAt(i - 1);  //用于返回指定索引处的字符串
            for (int j = 1; j <= w2; j++) {
                char ch2 = word2.charAt(j - 1);
                if (ch1 == ch2) cost = 0;
                arr[i][j] = Math.min(Math.min(arr[i - 1][j] + 1, arr[i][j - 1] + 1), arr[i - 1][j - 1] + cost);
            }
        }
        return 1 - (double) arr[w1][w2] / Math.max(w1, w2);
    }

    public static Map<String, Integer> CountMax(List<String> listFrist, List<String> listSecond) {
        if(listFrist.size()>listSecond.size())
        {
            return  null;
        }
        Map<String, Integer> map = new HashMap<>();
        for (String s : listFrist) {
            List<Double> rate = new LinkedList<Double>();
            for (String m : listSecond) {
                rate.add(Count(s, m));
            }
            Double max = Collections.max(rate);
            int index = rate.indexOf(max);
            String expect = listSecond.get(index);
            map.put(s, index);
            listSecond.remove(expect);
        }
        return map;
    }

    /**
     * 此字段是否为空
     * @param obj
     * @return true 不为空
     */
    public  boolean checkNull(Object obj){
        if(obj != null && !"".equals(obj) && !"null".equals(obj)){
            return true;
        }
        return false;
    }
    /**
     * 料号去 空格 比较
     * @param obj
     * @return
     */
    public String deleteBlank(Object obj){
        if((obj+"").contains(" ")){
            return (obj+"").replace(" ","");
        }else
            return obj+"";
    }

    /**
     * 料号去特殊符号
     * @param obj
     * @return
     */
    public String deleteSpecificSymbol(Object obj){
        return (obj+"").replace("/\\s*/g","");
    }




}
