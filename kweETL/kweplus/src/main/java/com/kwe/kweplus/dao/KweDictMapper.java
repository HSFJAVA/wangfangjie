package com.kwe.kweplus.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kwe.kweplus.modal.KweDict;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public interface KweDictMapper extends BaseMapper<KweDict> {

    List<Map> getKweDictByCustom();

    int deleteOrUpdateById(@Param("id") int id);

    int UpdateFlagById(@Param("flagId") int flagId,@Param("id") int id);

}