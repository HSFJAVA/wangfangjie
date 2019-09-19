package com.kwe.kweplus.bo.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kwe.kweplus.bo.ITaskBo;
import com.kwe.kweplus.controller.TaskController;
import com.kwe.kweplus.modal.JintieYwinfo;
import com.kwe.kweplus.service.IJintieYwinfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class TaskBo implements ITaskBo {

    private Logger logger = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    private IJintieYwinfoService jintieYwinfoService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = IllegalAccessException.class)
    public void refreshtatus(String wlYwno, Integer status, LocalDateTime takeTime) throws IllegalAccessException {
        QueryWrapper<JintieYwinfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(JintieYwinfo::getYwNo,wlYwno);
        JintieYwinfo info  = jintieYwinfoService.getOne(queryWrapper);
        if(status==3&&"已同步".equals(info.getStatus()))
            return;
        else if(status==4&&"已完成".equals(info.getStatus()))
            return;
        if(status==3)
            info.setStatus("已同步");
        else if(status==4)
            info.setStatus("已完成");
        info.setTaketime(takeTime);
        try{
            jintieYwinfoService.updateById(info);
        }catch (Exception e){
            logger.error("wrong!",e);
            throw new IllegalAccessException(e.getMessage());
        }
    }

}
