package com.kwe.kweplus.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kwe.kweplus.dao.JintieLogsMapper;
import com.kwe.kweplus.modal.JintieLogs;
import com.kwe.kweplus.service.IJintieLogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by wanli on 2019/8/13 13:07
 */
@Service
public class JintieLogsServiceImpl extends ServiceImpl<JintieLogsMapper, JintieLogs> implements IJintieLogsService{

    @Autowired
    private JintieLogsMapper jintieLogsMapper;


    @Override
    public IPage<JintieLogs> getOcrBaseDataList(String data) {
        JSONObject jsonObject = JSON.parseObject(data);
        QueryWrapper<JintieLogs> queryWrapper = new QueryWrapper<>();
        Page<JintieLogs> page = new Page<>();
        page.setCurrent(Long.valueOf(jsonObject.get("currentPage").toString()));
        page.setSize(Long.valueOf(jsonObject.get("pageSize").toString()));
        IPage<JintieLogs> jintieLogsIPage = jintieLogsMapper.selectPage(page,queryWrapper);
        return jintieLogsIPage;
    }

    @Override
    public void insertLog(JintieLogs logs) {
        jintieLogsMapper.insert(logs);
    }
}