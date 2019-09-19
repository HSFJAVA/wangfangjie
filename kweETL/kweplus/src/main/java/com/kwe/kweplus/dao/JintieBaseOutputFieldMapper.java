package com.kwe.kweplus.dao;


import com.kwe.kweplus.modal.JintieBaseOutputField;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JintieBaseOutputFieldMapper {
    List<JintieBaseOutputField> getFieldByCustomKey();
}