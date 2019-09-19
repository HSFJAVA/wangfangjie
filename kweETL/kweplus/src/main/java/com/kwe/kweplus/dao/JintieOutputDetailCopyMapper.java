package com.kwe.kweplus.dao;

import com.kwe.kweplus.modal.JintieInputDetailCopy;
import com.kwe.kweplus.modal.JintieOutputDetailCopy;
import com.kwe.kweplus.modal.JintieYwinfo;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface JintieOutputDetailCopyMapper {
    int deleteByPrimaryKey(Long pid);

    int insert(JintieOutputDetailCopy record);

    JintieOutputDetailCopy selectByPrimaryKey(Long pid);

    List<JintieOutputDetailCopy> selectAll();

    int updateByPrimaryKey(JintieOutputDetailCopy record);

    List<JintieOutputDetailCopy> selectByYwNo(String ywNo);

    int deleteByYwNo(String ywNo);
}