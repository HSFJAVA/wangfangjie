package com.kwe.kweplus.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kwe.kweplus.modal.JintieCustom;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jobob
 * @since 2019-03-22
 */
public interface IJintieCustomService extends IService<JintieCustom> {
    JintieCustom getCustomId(String customName);
    JintieCustom getCustomName(String key);
}
