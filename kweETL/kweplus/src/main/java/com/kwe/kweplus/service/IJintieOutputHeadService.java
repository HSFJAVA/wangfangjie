package com.kwe.kweplus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kwe.kweplus.modal.JintieOutputHead;

/**
 * <p>
 * 表一 服务类
 * </p>
 *
 * @author jobob
 * @since 2019-03-22
 */
public interface IJintieOutputHeadService extends IService<JintieOutputHead> {

    void saveOutputUpdate(String customNo, String ywNo);

    JintieOutputHead selectByYwNo(String ywNo);

    void removeYwNo(String ywNo);

}
