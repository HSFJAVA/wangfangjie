package com.kwe.kweplus.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kwe.kweplus.modal.JintieOutputDetail;
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
public interface JintieOutputDetailMapper extends BaseMapper<JintieOutputDetail> {

    void deleteByYwNo(@Param("ywNo") String ywNo);
}
