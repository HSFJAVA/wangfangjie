package com.kwe.kweplus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kwe.kweplus.dao.JintieCustomMapper;
import com.kwe.kweplus.modal.JintieCustom;
import com.kwe.kweplus.service.IJintieCustomService;
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
public class JintieCustomServiceImpl extends ServiceImpl<JintieCustomMapper, JintieCustom> implements IJintieCustomService {

    @Autowired
    JintieCustomMapper JintieCustomMapper;

    @Override
    public JintieCustom getCustomId(String customName) {
        return JintieCustomMapper.getCustomId(customName);
    }

    @Override
    public JintieCustom getCustomName(String key) {
        return JintieCustomMapper.getCustomName(key);
    }
}
