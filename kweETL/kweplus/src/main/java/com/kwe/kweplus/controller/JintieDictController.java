package com.kwe.kweplus.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kwe.kweplus.modal.JintieDict;
import com.kwe.kweplus.service.IJintieDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 商检参数 前端控制器
 * </p>
 *
 * @author jobob
 * @since 2019-03-22
 */
@RestController
@RequestMapping("/kwe/dict")
public class JintieDictController {
    @Autowired
    private IJintieDictService jintieDictService;

    @GetMapping("selectCode")
    public List<JintieDict> selectCode(String dictType) {
        QueryWrapper<JintieDict> dictQueryWrapper = new QueryWrapper();
        dictQueryWrapper.lambda().eq(JintieDict::getInspectionType, dictType);
        return jintieDictService.list(dictQueryWrapper);
    }

}
