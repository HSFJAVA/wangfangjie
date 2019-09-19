package com.kwe.kweplus.dao;

import com.kwe.kweplus.modal.JintieCustomField;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JintieCustomFieldMapper {

    List<JintieCustomField> getFieldByCustomKeyOfIn(@Param("key") String key,@Param("modelId") String remarks2);

    List<JintieCustomField> getFieldByCustomKeyOfOut(@Param("key") String key,@Param("modelId") String remarks2);

    int insert(JintieCustomField jintieCustomField);

    List<JintieCustomField> selectByStoreId(@Param("storeid")String storeid,@Param("type") String type,@Param("modelId") String remarks2);

    void deleteByModelIdAndCustomId(@Param("custom_id") String custom_id, @Param("type") String type,@Param("modelId") String modelId);
}