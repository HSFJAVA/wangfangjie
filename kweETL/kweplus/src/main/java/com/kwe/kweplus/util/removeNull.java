package com.kwe.kweplus.util;

import com.kwe.kweplus.modal.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class removeNull {

    private static void eachProperties(Object model) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Field[] field = model.getClass().getDeclaredFields(); //获取实体类的所有属性，返回Field数组
        for(int j=0 ; j<field.length ; j++){ //遍历所有属性
            String name = field[j].getName(); //获取属性的名字
            name = name.substring(0,1).toUpperCase()+name.substring(1); //将属性的首字符大写，方便构造get，set方法
            String type = field[j].getGenericType().toString(); //获取属性的类型
            if(type.equals("class java.lang.String")){ //如果type是类类型，则前面包含"class "，后面跟类名
                Method m = model.getClass().getMethod("get" + name);
                // 调用getter方法获取属性值
                String value = (String) m.invoke(model);
                //set值
                Class[] parameterTypes = new Class[1];
                parameterTypes[0] = field[j].getType();
                m = model.getClass().getMethod("set" + name, parameterTypes);
                String string = "";
                if(value != null){
                    string = value.trim();
                }
                Object[] objects = new Object[1];
                objects[0] = string;
                m.invoke(model, objects);
            }
            if(type.equals("class java.lang.Integer")){
            }
            if(type.equals("class java.lang.Short")){
            }
            if(type.equals("class java.lang.Double")){
            }
            if(type.equals("class java.lang.Boolean")){
            }
            if(type.equals("class java.util.Date")){
            }
        }
    }



    public static jintie_list ToEmpty(jintie_list data) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if(!data.getInputHeadList().isEmpty()){
            for (JintieInputHead Heads:data.getInputHeadList()) {
                eachProperties(Heads);
            }
        }
        if(!data.getInputDetailList().isEmpty()){
            for (JintieInputDetail Details:data.getInputDetailList()) {
               eachProperties(Details);
            }
        }


        return data;
    }

}
