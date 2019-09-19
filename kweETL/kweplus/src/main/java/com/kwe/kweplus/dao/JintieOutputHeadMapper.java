package com.kwe.kweplus.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kwe.kweplus.modal.JintieInputHead;
import com.kwe.kweplus.modal.JintieOutputHead;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 表一 Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2019-03-22
 */
@Repository
public interface JintieOutputHeadMapper extends BaseMapper<JintieOutputHead> {

    void saveOutputUpdate(@Param("customNo") String customNo,@Param("ywNo") String ywNo);

    JintieOutputHead selectByYwID(@Param("ywNo") String ywNo);

    void deleteByYwNo(String ywNo);
}
