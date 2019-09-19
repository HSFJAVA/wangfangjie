package com.kwe.kweplus.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassNamewanli
 * @Authorwanli
 * @Date2019/7/3016:53
 **/
public class zhengzeStudy {
    public static void main(String[] args)
    {
        //输入的字符串。
        String str="abcA1,333.6gK";
        //规则。
        String rules="[\\d]{0,}([,])?[\\d]{3}([.])?[\\d]{0,}";
        Pattern p = Pattern.compile(rules);
        Matcher m = p.matcher(str);
        System.out.println(m.find());
    }

  /**
   * 注册（正则）
   */
    public static String register(String uname,String pwd,String email,String phone)
    {
        /*
            用户名：字母加数字，字母开头([a-zA-Z]+[0-9]+)[0-9a-zA-Z]*
            密码：数字和字母和标点符号,不能包括空格，字母和数字和字符至少包括两种。长度8-16
            邮箱：@   .com
            手机号：11位
         */
        String rulesUserName="";
        String rulesPwd="";
        String rulesEmail="";
        String rulesPhone="";


        return "";
    }
}
