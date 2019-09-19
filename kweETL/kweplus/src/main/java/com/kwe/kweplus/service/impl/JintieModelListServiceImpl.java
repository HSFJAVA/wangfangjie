package com.kwe.kweplus.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kwe.kweplus.dao.JintieModelJsonMapper;
import com.kwe.kweplus.dao.JintieModelListMapper;
import com.kwe.kweplus.dao.JintieModelMapper;
import com.kwe.kweplus.modal.JintieModeParm;
import com.kwe.kweplus.modal.JintieModel;
import com.kwe.kweplus.modal.JintieModelList;
import com.kwe.kweplus.modal.ReturnMessage;
import com.kwe.kweplus.service.JintieModelListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class JintieModelListServiceImpl implements JintieModelListService {

    @Autowired
    JintieModelListMapper JintieModelListMapper;
    @Autowired
    JintieModelMapper JintieModelMapper;
    @Autowired
    JintieModelJsonMapper  jintieModelJsonMapper;


    @Override
    public Map getModelList(JintieModeParm parm) {
        int page =Integer.parseInt(parm.getPage());
        int rows = Integer.parseInt(parm.getRows());
        Page<JintieModelList> p = new Page<>(page, rows);
        List<JintieModelList> list = JintieModelListMapper.selectByParm(p,parm);
        Map map = new HashMap();
        map.put("rows",list);
        map.put("count",p.getTotal());
        return map;
    }

    @Override
    public Map getTemplateByModelId(String modelId){
        Map map = new HashMap();
        //得到模板详情信息json字段。
        JintieModel jintieModel=JintieModelMapper.selectByRemarks1(modelId);
        //处理json，去除目的国，指运港。
        String textStr=jintieModel.getText();
        Map<String,Object> textMap = (Map)JSONObject.parseObject(textStr);
        for(Object obj:textMap.keySet()){
            Object value=textMap.get(obj);
            Object key=obj;
            if(key.toString().equals("detailHead")){
                Map<String,Object> textMap2=(Map)value;
                for(Object obj2:textMap2.keySet()){
                    Map<String,Object> detailHeadMap = (Map)JSONObject.parseObject(textMap.get("detailHead")+"");
                    Object value2=detailHeadMap.get(obj2);
                    Object key2=obj2;
                    if(key2.toString()!=""&&key2.toString()!=null&&key2.toString().equals("countryofdestination")){
                        Map<String,Object> textMap3=(Map)value2;
                        for(Object obj3:textMap3.keySet()){
                            Object key3=obj3;
                            if(key3.toString()!=null&&key3.toString()!=""&&key3.toString().equals("value")){
                                textMap3.put("value","");
                            }
                        }
                        textMap2.put("countryofdestination",textMap3);
                    }
                }
                textMap.put("detailHead",textMap2);
            }
        }
        jintieModel.setText(JSONObject.toJSONString(textMap));
        map.put("template",jintieModel);
        return map;
    }

    @Override
    public ReturnMessage updateTemplate(JintieModel template) {
        ReturnMessage msg = new ReturnMessage();
        if( JintieModelMapper.updateByRemarks1(template) > 0){
            //修改List模板状态
            JintieModelList modelList = JintieModelListMapper.selectByModel_id(template.getRemark1());
            modelList.setUpdate_user(template.getRemark2());
            JintieModelListMapper.updateByModel_id(modelList);
            msg.setStatus(ReturnMessage.STATUS_OK);
            msg.setMessage("操作成功");
        }else{
            msg.setStatus(ReturnMessage.STATUS_ERROR);
            msg.setMessage("操作失败");
        }
        return msg;
    }

    @Override
    public ReturnMessage deleteModel(List<String> modelId) {
        ReturnMessage msg = new ReturnMessage();
        try {
            for (int i = 0; i <modelId.size() ; i++) {
                JintieModelListMapper.updateStatus(modelId.get(i)+"");
            }
            msg.setMessage("删除成功");
            msg.setStatus(ReturnMessage.STATUS_OK);
        }catch (Exception e){
            msg.setMessage("删除失败,错误原因"+e.toString());
            msg.setStatus(ReturnMessage.STATUS_ERROR);
        }
        return msg;

    }


    @Override
    public ReturnMessage createModel(JintieModelList model) {
        ReturnMessage msg = new ReturnMessage();
        String uuid = UUID.randomUUID().toString();
        model.setModel_id(uuid);
        model.setRemarks3("已启用");
        if(JintieModelListMapper.insert(model) > 0){
            JintieModel template = new JintieModel();
            template.setRemark1(uuid);
               if("0".equals(model.getRemarks2())){
                   //进仓模板创建
                   template.setText(jintieModelJsonMapper.selectById(1).getInputJson());
               }else{
                   //出仓模板创建
                   template.setText(jintieModelJsonMapper.selectById(1).getOutputJson());
               }
            if( JintieModelMapper.insert(template) > 0){
                msg.setStatus(ReturnMessage.STATUS_OK);
                msg.setMessage("模板创建成功");
            }else{
                msg.setStatus(ReturnMessage.STATUS_ERROR);
                msg.setMessage("模板template创建失败");
            }
            return msg;
        }else {
            msg.setStatus(ReturnMessage.STATUS_ERROR);
            msg.setMessage("模板列表生成失败");
            return msg;
        }
    }




}
