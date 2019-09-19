package com.kwe.kweplus.dao;

import com.kwe.kweplus.modal.JintieBaseInputField;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JintieBaseInputFieldMapper {

    List<JintieBaseInputField> getFieldByCustomKey();
}