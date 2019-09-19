package com.kwe.kweplus.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kwe.kweplus.modal.JintieYwinfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2019-03-22
 */
@Repository
public interface JintieYwinfoMapper extends BaseMapper<JintieYwinfo> {

    List<JintieYwinfo> selectByYwNo(@Param("ywNo") String ywNo);

    int selectByCustomNo(@Param("customNo")String customNo);
}
