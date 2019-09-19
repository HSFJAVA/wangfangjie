package com.kwe.kweplus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kwe.kweplus.dao.JintieInputHeadMapper;
import com.kwe.kweplus.modal.JintieInputHead;
import com.kwe.kweplus.service.IJintieInputHeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jobob
 * @since 2019-03-22
 */
@Service
public class JintieInputHeadServiceImpl extends ServiceImpl<JintieInputHeadMapper, JintieInputHead> implements IJintieInputHeadService {

    @Autowired
    JintieInputHeadMapper JintieInputHeadMapper;

    @Override
    public void saveInputUpdate(String customNo, String ywNo) {
        JintieInputHeadMapper.saveInputUpdate(customNo,ywNo);
    }

    @Override
    public JintieInputHead selectByYwNo(String ywNo) {
        return JintieInputHeadMapper.selectByYwID(ywNo);
    }

    @Override
    public boolean removeByYwNo(String ywNo) {
        JintieInputHeadMapper.deleteByYwNo(ywNo);
        return true;
    }
}
