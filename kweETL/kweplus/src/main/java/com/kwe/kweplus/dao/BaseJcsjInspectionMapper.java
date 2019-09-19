package com.kwe.kweplus.dao;

import com.kwe.kweplus.modal.BaseJcsjInspection;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.Map;
import java.util.List;
@Repository
public interface BaseJcsjInspectionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BaseJcsjInspection record);

    BaseJcsjInspection selectByPrimaryKey(Integer id);

    List<BaseJcsjInspection> selectAll();

    int updateByPrimaryKey(BaseJcsjInspection record);


    List<Map> getInspectionCode(@Param("map") Map map);

}