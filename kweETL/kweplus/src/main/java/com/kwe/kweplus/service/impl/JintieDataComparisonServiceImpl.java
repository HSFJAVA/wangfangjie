package com.kwe.kweplus.service.impl;

import com.kwe.kweplus.dao.JintieInputDetailCopyMapper;
import com.kwe.kweplus.dao.JintieInputDetailMapper;
import com.kwe.kweplus.dao.JintieOutputDetailCopyMapper;
import com.kwe.kweplus.dao.JintieOutputDetailMapper;
import com.kwe.kweplus.modal.*;
import com.kwe.kweplus.service.IJintieYwinfoService;
import com.kwe.kweplus.service.JintieDataComparisonService;
import com.kwe.kweplus.util.SetGet;
import com.kwe.kweplus.util.ocrUtil.OcrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by Ly on 2019/8/14 17:04
 */
@Service
public class JintieDataComparisonServiceImpl implements JintieDataComparisonService {

    @Autowired
    IJintieYwinfoService jintieYwinfoService;
    @Autowired
    JintieInputDetailMapper JintieInputDetailMapper;
    @Autowired
    JintieOutputDetailMapper JintieOutputDetailMapper;

    @Autowired
    JintieInputDetailCopyMapper JintieInputDetailCopyMapper;
    @Autowired
    JintieOutputDetailCopyMapper JintieOutputDetailCopyMapper;


    @Override
    public String DataComparison(String ywNo) throws Exception {
        DecimalFormat df=new DecimalFormat("0.00");//设置保留位数
        String percentage = "";
        int denominator = 0 ;
        int member = 0 ;
        OcrUtil util = new OcrUtil();
        JintieYwinfo ywinfo = jintieYwinfoService.selectByYwNo(ywNo);
        List<Map<String,String>> list = new ArrayList<>();
        List<String> strings = new ArrayList<>();


        if(ywinfo.getType() == 0 ){
            List<JintieInputDetailCopy>  JintieInputDetailCopys = JintieInputDetailCopyMapper.selectByYwNo(ywNo);
            for (int i = 0; i < JintieInputDetailCopys.size(); i++) {
                JintieInputDetailCopy JintieInputDetailCopy = JintieInputDetailCopys.get(i);
                Field[] fields = JintieInputDetailCopy.getClass().getDeclaredFields();
                //先把识别出来的数据拿出来
                Map<String,String> map = new HashMap();
                for(int j = 0 ; j < fields.length ; j++) {
                    //设置是否允许访问，不是修改原来的访问权限修饰词。
                    fields[j].setAccessible(true);
                    if(util.checkNull(fields[j].get(JintieInputDetailCopy))){
                        if(!fields[j].getName().equals("pid")){
                            map.put(fields[j].getName(),fields[j].get(JintieInputDetailCopy)+"");
                            if(i == 0){
                                strings.add(fields[j].getName());
                            }
                        }
                    }
                }
                list.add(map);
            }
            Map ma = new HashMap();
            ma.put("wl_ywno",ywNo);
            List<JintieInputDetail> JintieInputDetails = JintieInputDetailMapper.selectByMap(ma);
            try {
                if(list != null && list.size() > 0 ){
                    for (int i = 0; i < list.size(); i++) {
                        if(strings != null && strings.size() >0){
                            for (int j = 0; j < strings.size(); j++) {
                                if(list.get(i).get(strings.get(j)).equals(SetGet.getGetMethod(JintieInputDetails.get(i),strings.get(j))+"")){
                                    member++;
                                }
                                denominator++;
                            }
                        }
                    }
                }
            }catch (Exception e){
                System.out.println("数组越界"+ywNo);
            }
        }else {
            List<JintieOutputDetailCopy>  JintieOutputDetailCopys = JintieOutputDetailCopyMapper.selectByYwNo(ywNo);
            for (int i = 0; i < JintieOutputDetailCopys.size(); i++) {
                JintieOutputDetailCopy JintieOutputDetailCopy = JintieOutputDetailCopys.get(i);
                Field[] fields = JintieOutputDetailCopy.getClass().getDeclaredFields();
                //先把识别出来的数据拿出来
                Map<String,String> map = new HashMap();
                for(int j = 0 ; j < fields.length ; j++) {
                    //设置是否允许访问，不是修改原来的访问权限修饰词。
                    fields[j].setAccessible(true);
                    if(util.checkNull(fields[j].get(JintieOutputDetailCopy))){
                        if(!fields[j].getName().equals("pid")){
                            map.put(fields[j].getName(),fields[j].get(JintieOutputDetailCopy)+"");
                            if(i == 0){
                                strings.add(fields[j].getName());
                            }
                        }
                    }
                }
                list.add(map);
            }
            Map ma = new HashMap();
            ma.put("wl_ywno",ywNo);
            List<JintieOutputDetail> JintieOutputDetail = JintieOutputDetailMapper.selectByMap(ma);
            try {
                if(list != null && list.size() > 0 ){
                    for (int i = 0; i < list.size(); i++) {
                        if(strings != null && strings.size() >0){
                            for (int j = 0; j < strings.size(); j++) {
                                if(list.get(i).get(strings.get(j)).equals(SetGet.getGetMethod(JintieOutputDetail.get(i),strings.get(j))+"")){
                                    member++;
                                }
                                denominator++;
                            }
                        }
                    }
                }
            }catch (Exception e){
                System.out.println("数组越界"+ywNo);
            }

        }
        percentage = df.format((float)member/denominator);
        return percentage;
    }
}
