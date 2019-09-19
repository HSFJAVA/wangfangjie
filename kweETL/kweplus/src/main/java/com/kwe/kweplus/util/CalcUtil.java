package com.kwe.kweplus.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 */
public class CalcUtil {
    private  List<Double> list = new ArrayList();


    public  List<List<Double>> getSubset(List<Double> a, double m, int i) {
        List<List<Double>> resList = new ArrayList();
        while (i < a.size()) {
            list.add(a.get(i));
            if (getsum(list) == m) {
                resList.add(list);
                return resList;
            }
            i++;
            getSubset(a, m, i);
            list.remove(list.size() - 1);
        }
        return resList;
    }
    private static int getsum(List<Double> list) {
        int sum = 0;
        Iterator<Double> iterator = list.iterator();
        while(iterator.hasNext()){
            sum += iterator.next();
        }
        return sum;
    }

    public static void main(String[] args) {
        CalcUtil CalcUtil = new CalcUtil();
        List<Integer> a = new ArrayList<>();
        a.add(692);
        a.add(1693);
        a.add(1875);
        a.add(2413);
        a.add(3308);
        System.out.println(a);
        a.clear();
        System.out.println("==========================================");
        System.out.println(a);
        a.add(6921);
        a.add(3308);
        a.add(691);
        a.add(692);
        a.add(111);
        System.out.println(a);
        CalcUtil c = new CalcUtil();
    }
}
