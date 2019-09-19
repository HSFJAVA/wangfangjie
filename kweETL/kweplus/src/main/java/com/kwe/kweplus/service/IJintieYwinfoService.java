package com.kwe.kweplus.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kwe.kweplus.modal.JintieYwinfo;
import com.kwe.kweplus.modal.ReturnMessage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jobob
 * @since 2019-03-22
 */
public interface IJintieYwinfoService extends IService<JintieYwinfo> {

    JintieYwinfo selectByYwNo(String ywNo);

    int selectByCustomNo(String customNo);

    public ReturnMessage reverseExcel(List<JintieYwinfo> queryList);
}
