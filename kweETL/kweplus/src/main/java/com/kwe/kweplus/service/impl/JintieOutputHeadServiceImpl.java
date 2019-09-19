package com.kwe.kweplus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kwe.kweplus.dao.JintieOutputHeadMapper;
import com.kwe.kweplus.modal.JintieOutputHead;
import com.kwe.kweplus.service.IJintieOutputHeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 表一 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2019-03-22
 */
@Service
public class JintieOutputHeadServiceImpl extends ServiceImpl<JintieOutputHeadMapper, JintieOutputHead> implements IJintieOutputHeadService {

    @Autowired
    JintieOutputHeadMapper JintieOutputHeadMapper;

    @Override
    public void saveOutputUpdate(String customNo, String ywNo) {
        System.out.println("out"+customNo);
        System.out.println("out"+ywNo);
        JintieOutputHeadMapper.saveOutputUpdate(customNo,ywNo);
    }

    @Override
    public JintieOutputHead selectByYwNo(String ywNo) {
        return JintieOutputHeadMapper.selectByYwID(ywNo);
    }

    @Override
    public void removeYwNo(String ywNo) {
        JintieOutputHeadMapper.deleteByYwNo(ywNo);
    }
}
