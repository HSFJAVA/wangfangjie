package com.kwe.kweplus.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kwe.kweplus.modal.JintieLogs;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface JintieLogsMapper extends BaseMapper<JintieLogs> {
    int deleteByPrimaryKey(Integer logId);

//    int insert(JintieLogs record);

    JintieLogs selectByPrimaryKey(Integer logId);

    List<JintieLogs> selectAll();

    int updateByPrimaryKey(JintieLogs record);
}