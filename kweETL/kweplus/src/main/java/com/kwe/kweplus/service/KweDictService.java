package com.kwe.kweplus.service;

import com.kwe.kweplus.modal.KweDict;
import com.kwe.kweplus.modal.ReturnMessage;

import java.util.List;
import java.util.Map;

/**
 * @author Ly
 * @Desc
 * @date 2019/8/29  14:51
 */
public interface KweDictService {

    /**
     * 通过客户Key查询料号
     * @param customKey
     * @return
     */
    List<KweDict> getKweDictList(String customKey);

    List<Map> getKweDictByCustom();

    ReturnMessage addSku(KweDict dict);

    ReturnMessage deleteSku(List<Integer> skuNum,String user);

    ReturnMessage updateSku(KweDict dict);

    Map getSkuByParm(KweDict dict);

    ReturnMessage insertByDetail(String data);
}
