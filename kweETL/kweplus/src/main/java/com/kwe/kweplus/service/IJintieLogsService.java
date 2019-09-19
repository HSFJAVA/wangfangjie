package com.kwe.kweplus.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kwe.kweplus.modal.JintieLogs;

import java.util.List;

/**
 * Created by wanli on 2019/8/13 13:06
 */
public interface IJintieLogsService extends IService<JintieLogs> {

    IPage<JintieLogs> getOcrBaseDataList(String data);

    void insertLog(JintieLogs logs);
}
