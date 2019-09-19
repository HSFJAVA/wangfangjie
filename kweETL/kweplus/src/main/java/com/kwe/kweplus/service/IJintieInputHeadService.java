package com.kwe.kweplus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kwe.kweplus.modal.JintieInputHead;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jobob
 * @since 2019-03-22
 */
public interface IJintieInputHeadService extends IService<JintieInputHead> {

    void saveInputUpdate(String customNo, String ywNo);

    JintieInputHead selectByYwNo(String ywNo);

    boolean removeByYwNo(String ywNo);
}
