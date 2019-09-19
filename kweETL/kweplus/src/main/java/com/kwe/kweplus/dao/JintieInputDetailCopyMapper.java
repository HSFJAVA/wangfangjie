package com.kwe.kweplus.dao;

import com.kwe.kweplus.modal.JintieInputDetailCopy;
import com.kwe.kweplus.modal.JintieYwinfo;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface JintieInputDetailCopyMapper {
    int deleteByPrimaryKey(Long pid);


    int insert(JintieInputDetailCopy record);

    JintieInputDetailCopy selectByPrimaryKey(Long pid);

    List<JintieInputDetailCopy> selectAll();

    int updateByPrimaryKey(JintieInputDetailCopy record);

    int deleteByYwNo(String ywNo);

    List<JintieInputDetailCopy> selectByYwNo(String ywNo);
}