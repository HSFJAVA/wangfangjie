package com.kwe.kweplus.controller;

import com.alibaba.fastjson.JSON;
import com.kwe.kweplus.modal.KweDict;
import com.kwe.kweplus.modal.ReturnMessage;
import com.kwe.kweplus.service.KweDictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author Ly
 * @Desc  商品库相关接口
 * @date 2019/8/29  14:06
 */
@Api
@RestController
@RequestMapping("/dict")
public class KweDictController {


    @Autowired
    KweDictService kweDictService;

    @ApiOperation(httpMethod = "POST", value = "首页展示所有料")
    @RequestMapping("getSkuByParm")
    public Map getSkuByParm(@RequestBody KweDict dict){
        return kweDictService.getSkuByParm(dict);
    }


    /**
     * 新增料
     * @param dict
     * @return
     */
    @ApiOperation(httpMethod = "POST", value = "新增料")
    @RequestMapping("addSku")
    public ReturnMessage addSku(@RequestBody KweDict dict){
        return kweDictService.addSku(dict);
    }


    /**
     *  删除料
      * @param parm
     * @return
     */
    @ApiOperation(httpMethod = "POST", value = "删除料")
    @RequestMapping("deleteSku")
    public ReturnMessage deleteSku(@RequestBody String parm){
        Map map = (Map) JSON.parse(parm);
        String user = map.get("user")+"";
        List idlist = (List) map.get("idlist");
        return kweDictService.deleteSku(idlist,user);
    }



    @ApiOperation(httpMethod = "POST", value = "修改料")
    @RequestMapping("updateSku")
    public ReturnMessage updateSku(@RequestBody KweDict dict){
        return kweDictService.updateSku(dict);
    }



    /**
     * 传入客户Key 得到对应客户的料号信息
     * @param customKey
     * @return
     */
    @ApiOperation(httpMethod = "POST", value = "通过客户Key查询对应客户料号")
    @RequestMapping("/getKweDictList")
    public List<KweDict>  comparisonByDict(String customKey){
        return kweDictService.getKweDictList(customKey);
    }

    /**
     *  通过客户  区分   料
     * @return
     */
    @ApiOperation(httpMethod = "POST", value = "通过客户分组料号")
    @RequestMapping("/getKweDictByCustom")
    public List<Map>  getKweDictByCustom(){
        return kweDictService.getKweDictByCustom();
    }


    @ApiOperation(httpMethod = "POST", value = "用户确认动作")
    @RequestMapping("/insertByDetail")
    public ReturnMessage  insertByDetail(@RequestBody String data){
        return kweDictService.insertByDetail(data);
    }

}
