package com.kwe.kweplus.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kwe.kweplus.modal.JintieInputHead;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2019-03-22
 */
@Repository
public interface JintieInputHeadMapper extends BaseMapper<JintieInputHead> {

    void saveInputUpdate(@Param("customNo") String customNo,@Param("ywNo") String ywNo);

    JintieInputHead selectByYwID(@Param("ywNo") String ywNo);

    void deleteByYwNo(String ywNo);

}
