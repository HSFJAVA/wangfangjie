package com.kwe.kweplus.service.impl;

import com.kwe.kweplus.dao.JintieCustomFieldMapper;
import com.kwe.kweplus.modal.JintieCustomField;
import com.kwe.kweplus.service.JintieCustomFieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JintieCustomFieldServiceImpl implements JintieCustomFieldService {

    @Autowired
    JintieCustomFieldMapper JintieCustomFieldMapper;

    @Override
    public int insert(JintieCustomField jintieCustomField) {
        return JintieCustomFieldMapper.insert(jintieCustomField);
    }

    @Override
    public void deleteByModelIdAndCustomId(String custom_id, String type,String modelId) {
        JintieCustomFieldMapper.deleteByModelIdAndCustomId(custom_id,type,modelId);
    }
}
