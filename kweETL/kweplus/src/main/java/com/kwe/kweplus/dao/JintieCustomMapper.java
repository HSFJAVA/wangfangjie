package com.kwe.kweplus.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kwe.kweplus.modal.JintieCustom;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2019-03-22
 */
@Repository
public interface JintieCustomMapper extends BaseMapper<JintieCustom> {
    public Map selectDemo(Map paraMap);

    JintieCustom getCustomId(String customName);
    JintieCustom getCustomName(String key);
}
