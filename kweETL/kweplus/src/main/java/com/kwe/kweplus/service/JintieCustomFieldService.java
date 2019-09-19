package com.kwe.kweplus.service;

import com.kwe.kweplus.modal.JintieCustomField;

public interface JintieCustomFieldService {
    int insert(JintieCustomField jintieCustomField);


    void deleteByModelIdAndCustomId(String custom_id, String type,String modelId);
}
