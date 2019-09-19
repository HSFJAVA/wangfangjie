package com.kwe.kweplus.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kwe.kweplus.modal.JintieCustom;
import com.kwe.kweplus.service.IJintieCustomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author jobob
 * @since 2019-03-22
 */
@RestController
@RequestMapping("/kew/custom")
public class JintieCustomController {
    @Autowired
    private IJintieCustomService jintieCustomService;

    @GetMapping("searchCustom")
    public List<JintieCustom> getByName(String name){
        QueryWrapper<JintieCustom> customQueryWrapper = new QueryWrapper();
        customQueryWrapper.lambda().like(JintieCustom::getCustomName, name);
        return jintieCustomService.list(customQueryWrapper);
    }
}
