package com.kwe.kweplus.base;

import com.kwe.kweplus.util.CalcUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 目标：根据回传的Json中发票数量 箱单数量 经行业务操作、
 * 要求： 不论箱单发票全部要求有料号   --  数量待确认
 * 类型：
 *      1：一张发票，一张箱单
 *          1.1 料号一致
 *          1.2 数量一致
 *      2： 可能没有发票  有合同
 *      3: 多张发票，多张箱单
 *          1： 可能有发票号，可能无发票号
 *          2： 可能发票号，与箱单发票号不一致
 *          3： 可能料号不一致
 *          4：
 */
public class OcrServer {
    private Logger logger = LoggerFactory.getLogger(OcrServer.class);


    private boolean checkNull(Object obj){
        if(obj != null && !"".equals(obj) && !"null".equals(obj)){
            return true;
        }
        return false;
    }
    /**
     *  无条件匹配箱单发票
     * @param dataMap
     * @return
     */
    public List<Map<String,Object>> dataFormat(Map<String, List<Map>> dataMap,String ywNo){

        List<Map> list = getXDCommodity(dataMap);
        List<Map<String,Object>> lists = new ArrayList<>();//转换后的lists
        try {
            List<Map> FPlist =  dataMap.get("FPList");
            if( FPlist != null  ){
                for (int i = 0; i < FPlist.size(); i++) {
                    //发票号
                    String FPNo = FPlist.get(i).get("FPNo")+"";
                    //币制
                    String FPCurrency = FPlist.get(i).get("FPCurrency")+"";
                    //原产国
                    String FPCOO = FPlist.get(i).get("FPCOO")+"";
                    //净重
                    String FPTotalNW = FPlist.get(i).get("FPTotalNW")+"";
                    //毛重
                    String FPTotalGW = FPlist.get(i).get("FPTotalGW")+"";
                    //单位
                    String FPGdsUnit = FPlist.get(i).get("FPGdsUnit")+"";
                    List<Map> FPCommodityList = (List<Map>)FPlist.get(i).get("FPCommodityList");
                    if( FPCommodityList != null && FPCommodityList.size() > 0){
                        for (int j = 0; j < FPCommodityList.size(); j++) {
                            Map<String,Object> map = new HashMap<>();//封装detail
                            map.put("发票号",FPNo);
                            //商品料号
                            String FPCommodity_PartNumber = FPCommodityList.get(j).get("FPCommodity_PartNumber")+"";
                            map.put("料号",FPCommodity_PartNumber);
                            //料号数量
                            String FPCommodity_GdsQty1 = FPCommodityList.get(j).get("FPCommodity_GdsQty1")+"";
                            map.put("数量",FPCommodity_GdsQty1);
                            //料号单价
                            String FPCommodity_GdsPrice = FPCommodityList.get(j).get("FPCommodity_GdsPrice")+"";
                            map.put("单价",FPCommodity_GdsPrice);
                            //料号总价
                            String FPCommodity_GdsAmount = FPCommodityList.get(j).get("FPCommodity_GdsAmount")+"";
                            map.put("总价",FPCommodity_GdsAmount);
                            //币制
                            String FPCommodity_Currency = FPCommodityList.get(j).get("FPCommodity_Currency")+"";
                            if(checkNull(FPCommodity_Currency)){
                                map.put("币制",FPCommodity_Currency);
                            }else {
                                map.put("币制",FPCurrency);
                            }
                            //原产国
                            String FPCommodity_COO = FPCommodityList.get(j).get("FPCommodity_COO")+"";
                            if(checkNull(FPCommodity_COO)){
                                map.put("原产国",FPCommodity_COO);
                            }else {
                                map.put("原产国",FPCOO);
                            }
                            //料号毛重
                            String FPCommodity_GdsGW = FPCommodityList.get(j).get("FPCommodity_GdsGW")+"";
                            map.put("毛重",FPCommodity_GdsGW);
                            //料号净重
                            String FPCommodity_GdsNW = FPCommodityList.get(j).get("FPCommodity_GdsNW")+"";
                            map.put("净重",FPCommodity_GdsNW);
                            //税号
                            String FPCommodity_HSCode = FPCommodityList.get(j).get("FPCommodity_HSCode")+"";
                            map.put("税号",FPCommodity_HSCode);
                            //品名
                            String FPCommodity_Desc = FPCommodityList.get(j).get("FPCommodity_Desc")+"";
                            map.put("品名",FPCommodity_Desc);
                            //数量单位
                            String FPCommodity_GdsUnit1 = FPCommodityList.get(j).get("FPCommodity_GdsUnit1")+"";
                            map.put("单位",FPCommodity_GdsUnit1);
                            if(!checkNull(map.get("单位"))){
                                map.put("单位",FPGdsUnit);
                            }
                            //料号体积
                            String FPCommodity_GdsMeasurement = FPCommodityList.get(j).get("FPCommodity_GdsMeasurement")+"";
                            map.put("体积",FPCommodity_GdsMeasurement);
                            //PO号
                            String FPCommodity_PO = FPCommodityList.get(j).get("FPCommodity_PO")+"";
                            map.put("PO号",FPCommodity_PO);
                            //料号行号
                            String FPCommodity_ItemNo = FPCommodityList.get(j).get("FPCommodity_ItemNo")+"";
                            map.put("行号",FPCommodity_ItemNo);
                            //料号行号
                            String FPCommodity_InFPNo = FPCommodityList.get(j).get("FPCommodity_InFPNo")+"";
                            map.put("入库发票号",FPCommodity_InFPNo);
                            //料号行号
                            String FPCommodity_Type = FPCommodityList.get(j).get("FPCommodity_Type")+"";
                            map.put("第二品名",FPCommodity_Type);
                            //料号行号
                            String FPCommodity_InWorkNo = FPCommodityList.get(j).get("FPCommodity_InWorkNo")+"";
                            map.put("进库业务编号",FPCommodity_InWorkNo);
                            //料号行号
                            String FPCommodity_InWayBill = FPCommodityList.get(j).get("FPCommodity_InWayBill")+"";
                            map.put("进库运单号",FPCommodity_InWayBill);
                            //最终用户料号
                            String FPCommodity_EndUserNo = FPCommodityList.get(j).get("FPCommodity_EndUserNo")+"";
                            map.put("最终用户料号",FPCommodity_EndUserNo);
                            //订单号
                            String FPCommodity_OrderNo = FPCommodityList.get(j).get("FPCommodity_OrderNo")+"";
                            map.put("订单号",FPCommodity_OrderNo);
                            //CPN
                            String FPCommodity_CPN = FPCommodityList.get(j).get("FPCommodity_CPN")+"";
                            map.put("CPN",FPCommodity_CPN);
                            //最终用户
                            String FPCommodity_EndUser = FPCommodityList.get(j).get("FPCommodity_EndUser")+"";
                            map.put("最终用户",FPCommodity_EndUser);
                            //SO号
                            String FPCommodity_So = FPCommodityList.get(j).get("FPCommodity_So")+"";
                            map.put("SO号",FPCommodity_So);
                            //最小包装数量
                            String FPCommodity_MOQ = FPCommodityList.get(j).get("FPCommodity_MOQ")+"";
                            map.put("最小包装数量",FPCommodity_MOQ);
                            //备注栏
                            String FPCommodity_ReMarks = FPCommodityList.get(j).get("FPCommodity_ReMarks")+"";
                            map.put("备注栏",FPCommodity_ReMarks);
                            //唛头/件数/米数
                            String FPCommodity_ReMarks2 = FPCommodityList.get(j).get("FPCommodity_ReMarks")+"";
                            map.put("唛头/件数/米数",FPCommodity_ReMarks2);
                            //最终用户料号2
                            String FPCommodity_EndUser2 = FPCommodityList.get(j).get("FPCommodity_EndUser2")+"";
                            map.put("最终用户料号2",FPCommodity_EndUser2);
                            //最终用户料号3
                            String FPCommodity_EndUser3 = FPCommodityList.get(j).get("FPCommodity_EndUser3")+"";
                            map.put("最终用户料号3",FPCommodity_EndUser3);
                            //Shipment No
                            String FPCommodity_DN = FPCommodityList.get(j).get("FPCommodity_DN")+"";
                            map.put("Shipment No",FPCommodity_DN);
                            //SN号
                            String FPCommodity_SN = FPCommodityList.get(j).get("FPCommodity_SN")+"";
                            map.put("SN号",FPCommodity_SN);
                            //部门
                            String FPCommodity_Dept = FPCommodityList.get(j).get("FPCommodity_Dept")+"";
                            map.put("部门",FPCommodity_Dept);
                            //Delivery No
                            String FPCommodity_DN2 = FPCommodityList.get(j).get("FPCommodity_DN")+"";
                            map.put("Delivery No",FPCommodity_DN2);
                            //箱数
                            String FPCommodity_GdsCtnQty = FPCommodityList.get(j).get("FPCommodity_GdsCtnQty")+"";
                            map.put("箱数",FPCommodity_GdsCtnQty);
                            //生产日期
                            String FPCommodity_InProductiondate = FPCommodityList.get(j).get("FPCommodity_InProductiondate")+"";
                            map.put("生产日期",FPCommodity_InProductiondate);
                            //序列号
                            String FPCommodity_BoxNumber = FPCommodityList.get(j).get("FPCommodity_BoxNumber")+"";
                            map.put("序列号",FPCommodity_BoxNumber);
                            List<Map> FPCommodity2List = (List<Map>)FPCommodityList.get(j).get("FPCommodity2List");
                            if(FPCommodity2List != null && FPCommodity2List.size() >0 ){
                                for (int k = 0; k < FPCommodity2List.size(); k++) {
                                    String FPCommodity_Attach_BatchQty = FPCommodity2List.get(k).get("FPCommodity_Attach_BatchQty")+"";
                                    String FPCommodity_Attach_SupplierNw = FPCommodity2List.get(k).get("FPCommodity_Attach_SupplierNw")+"";
                                    String FPCommodity_Attach_SupplierGw = FPCommodity2List.get(k).get("FPCommodity_Attach_SupplierGw")+"";
                                    String FPCommodity_Attach_SupplierAmount = FPCommodity2List.get(k).get("FPCommodity_Attach_SupplierAmount ")+"";
                                    String FPCommodity_Attach_BatchNo = FPCommodity2List.get(k).get("FPCommodity_Attach_BatchNo")+"";
                                    String FPCommodity_Attach_CtnNo = FPCommodity2List.get(k).get("FPCommodity_Attach_CtnNo")+"";
                                    if(FPCommodity_Attach_CtnNo.contains(".0")){
                                        FPCommodity_Attach_CtnNo = FPCommodity_Attach_CtnNo.replace(".0","");
                                    }
                                    String FPCommodity_Attach_EngineNo = FPCommodity2List.get(k).get("FPCommodity_Attach_EngineNo")+"";
                                    String FPCommodity_Attach_LotNo = FPCommodity2List.get(k).get("FPCommodity_Attach_LotNo")+"";
                                    String FPCommodity_Attach_SupplierGoodQty = FPCommodity2List.get(k).get("FPCommodity_Attach_SupplierGoodQty")+"";
                                    map.put("batch数量",FPCommodity_Attach_BatchQty);
                                    map.put("分净重",FPCommodity_Attach_SupplierNw);
                                    map.put("分毛重",FPCommodity_Attach_SupplierGw);
                                    map.put("分金额",FPCommodity_Attach_SupplierAmount);
                                    map.put("batch号",FPCommodity_Attach_BatchNo);
                                    map.put("箱号",FPCommodity_Attach_CtnNo);
                                    map.put("发动机号",FPCommodity_Attach_EngineNo);
                                    map.put("Lot No",FPCommodity_Attach_LotNo);
                                    map.put("良品数量",FPCommodity_Attach_SupplierGoodQty);
                                }
                            }






                            /**
                             * 新规则 8.2 新增
                             *      没有料号 ，没有数量 直接拼接
                             *
                             *      拼接规则
                             *          1， 有行号就按照行号拼接
                             *              such as
                             *                  在发票是第一行，区箱单的 01
                             *          2， 没有行号就走料号数量拼接
                             */
                            //发票数量检查
                                //如果为空 直接添加  无需匹配
                            if(!checkNull(map.get("数量"))){
                                continue;
                            }else {
                                if(list != null && list.size() >0 ){
                                    /**
                                     * 判断有没有行号
                                     */
                                    boolean line = false;
                                    for (int k = 0; k < list.size(); k++) {
                                        if(checkNull(list.get(k).get("XDCommodity_ItemNo"))){
                                            line = true;
                                            break;
                                        }
                                    }
                                    if(line){
                                        /**
                                         * 有行号就按照行号拼接
                                         */
                                        boolean first = true;
                                        for (int k = 0; k < list.size(); k++) {
                                            if(checkNull(list.get(k).get("XDCommodity_ItemNo"))){
                                                if(j+1 == new Double(list.get(k).get("XDCommodity_ItemNo")+"")){
                                                    //箱单数据外层补充
                                                    if(first){
                                                        if(!checkNull(map.get("原产国"))){
                                                            map.put("原产国",list.get(k).get("XDCOO"));
                                                        }
                                                        if(!checkNull(map.get("原产国"))){
                                                            map.put("原产国",list.get(k).get("XDCommodity_COO"));
                                                        }
                                                        if(!checkNull(map.get("单位"))){
                                                            map.put("单位",list.get(k).get("XDCommodity_GdsUnit1"));
                                                        }
                                                        if(!checkNull(map.get("PO号"))){
                                                            map.put("PO号",list.get(k).get("XDCommodity_PO"));
                                                        }
                                                        if(!checkNull(map.get("入库发票号"))){
                                                            map.put("入库发票号",list.get(k).get("XDCommodity_InXDNo"));
                                                        }
                                                        if(!checkNull(map.get("进库业务编号"))){
                                                            map.put("进库业务编号",list.get(k).get("XDCommodity_InWorkNo"));
                                                        }
                                                        if(!checkNull(map.get("进库运单号"))){
                                                            map.put("进库运单号",list.get(k).get("XDCommodity_InWayBill"));
                                                        }
                                                        if(!checkNull(map.get("最终用户料号"))){
                                                            map.put("最终用户料号",list.get(k).get("XDCommodity_EndUserNo"));
                                                        }
                                                        if(!checkNull(map.get("订单号"))){
                                                            map.put("订单号",list.get(k).get("XDCommodity_OrderNo"));
                                                        }
                                                        if(!checkNull(map.get("CPN"))){
                                                            map.put("CPN",list.get(k).get("XDCommodity_CPN"));
                                                        }
                                                        if(!checkNull(map.get("最终用户"))){
                                                            map.put("最终用户",list.get(k).get("XDCommodity_EndUser"));
                                                        }
                                                        if(!checkNull(map.get("SO号"))){
                                                            map.put("SO号",list.get(k).get("XDCommodity_So"));
                                                        }
                                                        if(!checkNull(map.get("最小包装数量"))){
                                                            map.put("最小包装数量",list.get(k).get("XDCommodity_MOQ"));
                                                        }
                                                        if(!checkNull(map.get("备注栏"))){
                                                            map.put("备注栏",list.get(k).get("XDCommodity_ReMarks"));
                                                        }
                                                        if(!checkNull(map.get("唛头/件数/米数"))){
                                                            map.put("入库发票号",list.get(k).get("XDCommodity_ReMarks"));
                                                        }
                                                        if(!checkNull(map.get("最终用户料号2"))){
                                                            map.put("最终用户料号2",list.get(k).get("XDCommodity_EndUser2"));
                                                        }
                                                        if(!checkNull(map.get("最终用户料号3"))){
                                                            map.put("最终用户料号3",list.get(k).get("XDCommodity_EndUser3"));
                                                        }
                                                        if(!checkNull(map.get("Shipment no"))){
                                                            map.put("Shipment no",list.get(k).get("XDCommodity_ShipmentNo"));
                                                        }
                                                        if(!checkNull(map.get("SN号"))){
                                                            map.put("SN号",list.get(k).get("XDCommodity_SN"));
                                                        }
                                                        if(!checkNull(map.get("部门"))){
                                                            map.put("部门",list.get(k).get("XDCommodity_Dept"));
                                                        }
                                                        if(!checkNull(map.get("Delivery No"))){
                                                            map.put("Delivery No",list.get(k).get("XDCommodity_DN"));
                                                        }
                                                        if(!checkNull(map.get("箱数"))){
                                                            map.put("箱数",list.get(k).get("XDCommodity_GdsCtnQty"));
                                                        }
                                                        if(!checkNull(map.get("生产日期"))){
                                                            map.put("生产日期",list.get(k).get("XDCommodity_InProductiondate"));
                                                        }
                                                        if(!checkNull(map.get("序列号"))){
                                                            map.put("序列号",list.get(k).get("XDCommodity_BoxNumber"));
                                                        }
                                                        //直接料号数量匹配 OK
                                                        map.put("分净重",list.get(k).get("XDCommodity_GdsNW"));
                                                        map.put("分毛重",list.get(k).get("XDCommodity_GdsGW"));
                                                        map.put("行号",list.get(k).get("XDCommodity_ItemNo"));
                                                        map.put("batch数量",list.get(k).get("XDCommodity_GdsQty1"));
                                                        List<Map> XDCommodity2List = (List<Map>) list.get(k).get("XDCommodity2List");
                                                        if(XDCommodity2List != null && XDCommodity2List.size() > 0 ){
                                                            map.put("箱号",XDCommodity2List.get(0).get("XDCommodity_Attach_CtnNo"));
                                                            map.put("batch号",XDCommodity2List.get(0).get("XDCommodity_Attach_BatchNo"));
                                                        }else {
                                                            map.put("净重",list.get(k).get("XDCommodity_GdsNW"));
                                                            map.put("毛重",list.get(k).get("XDCommodity_GdsGW"));
                                                        }
                                                        lists.add(map);
                                                        first = false;
                                                        list.remove(k);
                                                        k--;
                                                    }else {
                                                        Map<String,Object> mapForMore = new HashMap<>();//封装detail
                                                        mapForMore.put("行号",list.get(k).get("XDCommodity_ItemNo"));
                                                        mapForMore.put("分净重",list.get(k).get("XDCommodity_GdsNW"));
                                                        mapForMore.put("分毛重",list.get(k).get("XDCommodity_GdsGW"));
                                                        mapForMore.put("batch数量",list.get(k).get("XDCommodity_GdsQty1"));
                                                        List<Map> XDCommodity2List = (List<Map>) list.get(k).get("XDCommodity2List");
                                                        if(XDCommodity2List != null && XDCommodity2List.size() > 0 ){
                                                            mapForMore.put("箱号",XDCommodity2List.get(0).get("XDCommodity_Attach_CtnNo"));
                                                            mapForMore.put("batch号",XDCommodity2List.get(0).get("XDCommodity_Attach_BatchNo"));
                                                        }else {
                                                            mapForMore.put("净重",list.get(k).get("XDCommodity_GdsNW"));
                                                            mapForMore.put("毛重",list.get(k).get("XDCommodity_GdsGW"));
                                                        }
                                                        lists.add(mapForMore);
                                                        list.remove(k);
                                                        k--;
                                                    }
                                                }
                                            }
                                        }
                                    }else {
                                        /**
                                         * 8.2号规则变更！
                                         * FP二级里的一行与箱单所以行对比情况：
                                         *      1，料号一致  数量一致直接拼接放入
                                         *      2，料号一致  数量不一致
                                         *      3，料号匹配不上或者数量匹配不上
                                         */
                                        boolean has = true;
                                        boolean hasXD = true;
                                        for (int k = 0; k < list.size(); k++) {
                                            if(! checkNull(list.get(k).get("XDCommodity_GdsQty1")+"")){
                                                lists.add(map);
                                            }else {
                                                //1，料号一致  数量一致直接拼接放入
                                                if(deleteBlank(map.get("料号")).equals(deleteBlank(list.get(k).get("XDCommodity_PartNumber"))) && new Double(map.get("数量")+"").equals(new Double(list.get(k).get("XDCommodity_GdsQty1")+""))){
                                                    //箱单数据外层补充
                                                    if(!checkNull(map.get("原产国"))){
                                                        map.put("原产国",list.get(k).get("XDCOO"));
                                                    }
                                                    if(!checkNull(map.get("原产国"))){
                                                        map.put("原产国",list.get(k).get("XDCommodity_COO"));
                                                    }
                                                    if(!checkNull(map.get("单位"))){
                                                        map.put("单位",list.get(k).get("XDCommodity_GdsUnit1"));
                                                    }
                                                    if(!checkNull(map.get("PO号"))){
                                                        map.put("PO号",list.get(k).get("XDCommodity_PO"));
                                                    }
                                                    if(!checkNull(map.get("入库发票号"))){
                                                        map.put("入库发票号",list.get(k).get("XDCommodity_InXDNo"));
                                                    }
                                                    if(!checkNull(map.get("进库业务编号"))){
                                                        map.put("进库业务编号",list.get(k).get("XDCommodity_InWorkNo"));
                                                    }
                                                    if(!checkNull(map.get("进库运单号"))){
                                                        map.put("进库运单号",list.get(k).get("XDCommodity_InWayBill"));
                                                    }
                                                    if(!checkNull(map.get("最终用户料号"))){
                                                        map.put("最终用户料号",list.get(k).get("XDCommodity_EndUserNo"));
                                                    }
                                                    if(!checkNull(map.get("订单号"))){
                                                        map.put("订单号",list.get(k).get("XDCommodity_OrderNo"));
                                                    }
                                                    if(!checkNull(map.get("CPN"))){
                                                        map.put("CPN",list.get(k).get("XDCommodity_CPN"));
                                                    }
                                                    if(!checkNull(map.get("最终用户"))){
                                                        map.put("最终用户",list.get(k).get("XDCommodity_EndUser"));
                                                    }
                                                    if(!checkNull(map.get("SO号"))){
                                                        map.put("SO号",list.get(k).get("XDCommodity_So"));
                                                    }
                                                    if(!checkNull(map.get("最小包装数量"))){
                                                        map.put("最小包装数量",list.get(k).get("XDCommodity_MOQ"));
                                                    }
                                                    if(!checkNull(map.get("备注栏"))){
                                                        map.put("备注栏",list.get(k).get("XDCommodity_ReMarks"));
                                                    }
                                                    if(!checkNull(map.get("唛头/件数/米数"))){
                                                        map.put("入库发票号",list.get(k).get("XDCommodity_ReMarks"));
                                                    }
                                                    if(!checkNull(map.get("最终用户料号2"))){
                                                        map.put("最终用户料号2",list.get(k).get("XDCommodity_EndUser2"));
                                                    }
                                                    if(!checkNull(map.get("最终用户料号3"))){
                                                        map.put("最终用户料号3",list.get(k).get("XDCommodity_EndUser3"));
                                                    }
                                                    if(!checkNull(map.get("Shipment no"))){
                                                        map.put("Shipment no",list.get(k).get("XDCommodity_ShipmentNo"));
                                                    }
                                                    if(!checkNull(map.get("SN号"))){
                                                        map.put("SN号",list.get(k).get("XDCommodity_SN"));
                                                    }
                                                    if(!checkNull(map.get("部门"))){
                                                        map.put("部门",list.get(k).get("XDCommodity_Dept"));
                                                    }
                                                    if(!checkNull(map.get("Delivery No"))){
                                                        map.put("Delivery No",list.get(k).get("XDCommodity_DN"));
                                                    }
                                                    if(!checkNull(map.get("箱数"))){
                                                        map.put("箱数",list.get(k).get("XDCommodity_GdsCtnQty"));
                                                    }
                                                    if(!checkNull(map.get("生产日期"))){
                                                        map.put("生产日期",list.get(k).get("XDCommodity_InProductiondate"));
                                                    }
                                                    if(!checkNull(map.get("序列号"))){
                                                        map.put("序列号",list.get(k).get("XDCommodity_BoxNumber"));
                                                    }
                                                    //直接料号数量匹配 OK
                                                    map.put("分净重",list.get(k).get("XDCommodity_GdsNW"));
                                                    map.put("分毛重",list.get(k).get("XDCommodity_GdsGW"));
                                                    map.put("行号",list.get(k).get("XDCommodity_ItemNo"));
                                                    map.put("batch数量",list.get(k).get("XDCommodity_GdsQty1"));
                                                    List<Map> XDCommodity2List = (List<Map>) list.get(k).get("XDCommodity2List");
                                                    if(XDCommodity2List != null && XDCommodity2List.size() > 0 ){
                                                        map.put("箱号",XDCommodity2List.get(0).get("XDCommodity_Attach_CtnNo"));
                                                        map.put("batch号",XDCommodity2List.get(0).get("XDCommodity_Attach_BatchNo"));
                                                    }else {
                                                        map.put("净重",list.get(k).get("XDCommodity_GdsNW"));
                                                        map.put("毛重",list.get(k).get("XDCommodity_GdsGW"));
                                                    }
                                                    lists.add(map);
                                                    list.remove(k);
                                                    k--;
                                                    has = false;
                                                    hasXD = false;
                                                    break;
                                                }
                                            }
                                        }
                                        boolean is = false;
                                        List<Integer> li = new ArrayList();
                                        for (int k = 0; k < list.size(); k++) {
                                            //2，料号一致  数量不一致
                                            if(deleteBlank(map.get("料号")).equals(deleteBlank(list.get(k).get("XDCommodity_PartNumber"))) && checkNull(map.get("数量")) && checkNull(list.get(k).get("XDCommodity_GdsQty1"))  &&  new Double(map.get("数量")+"") > new Double(list.get(k).get("XDCommodity_GdsQty1")+"")){
                                                is = true;
                                                li.add(k);
                                            }
                                        }
                                        if(is && has){
                                            List<Double> count = new ArrayList<>();
                                            Comparator<Map> comparator = new Comparator<Map>() {
                                                @Override
                                                public int compare(Map o1, Map o2) {
                                                    if(checkNull(o1.get("XDCommodity_GdsQty1")) &&  checkNull(o2.get("XDCommodity_GdsQty1"))){
                                                        if(new Double(o1.get("XDCommodity_GdsQty1")+"") < new Double(o2.get("XDCommodity_GdsQty1")+"")){
                                                            return -1;
                                                        }else {
                                                            return 1;
                                                        }
                                                    }
                                                    return 1;
                                                }
                                            };
                                            Collections.sort(list,comparator);
                                            for (int k = 0; k < li.size(); k++) {
                                                if(checkNull(list.get(li.get(k)).get("XDCommodity_GdsQty1")+"")){
                                                    Double a = new Double(list.get(li.get(k)).get("XDCommodity_GdsQty1")+"");
                                                    count.add(a);
                                                }
                                            }
                                            if(checkNull(FPCommodityList.get(j).get("FPCommodity_GdsQty1"))){
                                                Double c = new Double(FPCommodityList.get(j).get("FPCommodity_GdsQty1")+"");
                                                CalcUtil CalcUtil = new CalcUtil();
                                                List<List<Double>> cc = CalcUtil.getSubset(count,c,0);
                                                if(cc != null && cc.size() >0){
                                                    for (int k = 0; k < cc.get(0).size(); k++) {
                                                        Map min = list.get(li.get(count.indexOf(cc.get(0).get(k))));
                                                        if(k == 0){
                                                            if(!checkNull(map.get("原产国"))){
                                                                map.put("原产国",list.get(k).get("XDCOO"));
                                                            }
                                                            if(!checkNull(map.get("原产国"))){
                                                                map.put("原产国",list.get(k).get("XDCommodity_COO"));
                                                            }
                                                            if(!checkNull(map.get("单位"))){
                                                                map.put("单位",list.get(k).get("XDCommodity_GdsUnit1"));
                                                            }
                                                            if(!checkNull(map.get("PO号"))){
                                                                map.put("PO号",list.get(k).get("XDCommodity_PO"));
                                                            }
                                                            if(!checkNull(map.get("入库发票号"))){
                                                                map.put("入库发票号",list.get(k).get("XDCommodity_InXDNo"));
                                                            }
                                                            if(!checkNull(map.get("进库业务编号"))){
                                                                map.put("进库业务编号",list.get(k).get("XDCommodity_InWorkNo"));
                                                            }
                                                            if(!checkNull(map.get("进库运单号"))){
                                                                map.put("进库运单号",list.get(k).get("XDCommodity_InWayBill"));
                                                            }
                                                            if(!checkNull(map.get("最终用户料号"))){
                                                                map.put("最终用户料号",list.get(k).get("XDCommodity_EndUserNo"));
                                                            }
                                                            if(!checkNull(map.get("订单号"))){
                                                                map.put("订单号",list.get(k).get("XDCommodity_OrderNo"));
                                                            }
                                                            if(!checkNull(map.get("CPN"))){
                                                                map.put("CPN",list.get(k).get("XDCommodity_CPN"));
                                                            }
                                                            if(!checkNull(map.get("最终用户"))){
                                                                map.put("最终用户",list.get(k).get("XDCommodity_EndUser"));
                                                            }
                                                            if(!checkNull(map.get("SO号"))){
                                                                map.put("SO号",list.get(k).get("XDCommodity_So"));
                                                            }
                                                            if(!checkNull(map.get("最小包装数量"))){
                                                                map.put("最小包装数量",list.get(k).get("XDCommodity_MOQ"));
                                                            }
                                                            if(!checkNull(map.get("备注栏"))){
                                                                map.put("备注栏",list.get(k).get("XDCommodity_ReMarks"));
                                                            }
                                                            if(!checkNull(map.get("唛头/件数/米数"))){
                                                                map.put("入库发票号",list.get(k).get("XDCommodity_ReMarks"));
                                                            }
                                                            if(!checkNull(map.get("最终用户料号2"))){
                                                                map.put("最终用户料号2",list.get(k).get("XDCommodity_EndUser2"));
                                                            }
                                                            if(!checkNull(map.get("最终用户料号3"))){
                                                                map.put("最终用户料号3",list.get(k).get("XDCommodity_EndUser3"));
                                                            }
                                                            if(!checkNull(map.get("Shipment no"))){
                                                                map.put("Shipment no",list.get(k).get("XDCommodity_ShipmentNo"));
                                                            }
                                                            if(!checkNull(map.get("SN号"))){
                                                                map.put("SN号",list.get(k).get("XDCommodity_SN"));
                                                            }
                                                            if(!checkNull(map.get("部门"))){
                                                                map.put("部门",list.get(k).get("XDCommodity_Dept"));
                                                            }
                                                            if(!checkNull(map.get("Delivery No"))){
                                                                map.put("Delivery No",list.get(k).get("XDCommodity_DN"));
                                                            }
                                                            if(!checkNull(map.get("箱数"))){
                                                                map.put("箱数",list.get(k).get("XDCommodity_GdsCtnQty"));
                                                            }
                                                            if(!checkNull(map.get("生产日期"))){
                                                                map.put("生产日期",list.get(k).get("XDCommodity_InProductiondate"));
                                                            }
                                                            if(!checkNull(map.get("序列号"))){
                                                                map.put("序列号",list.get(k).get("XDCommodity_BoxNumber"));
                                                            }

                                                            map.put("行号",list.get(k).get("XDCommodity_ItemNo"));
                                                            map.put("分净重",min.get("XDCommodity_GdsNW"));
                                                            map.put("分毛重",min.get("XDCommodity_GdsGW"));
                                                            map.put("batch数量",min.get("XDCommodity_GdsQty1"));
                                                            List<Map> XDCommodity2List = (List<Map>) min.get("XDCommodity2List");
                                                            if(XDCommodity2List != null && XDCommodity2List.size() > 0 ){
                                                                map.put("箱号",XDCommodity2List.get(0).get("XDCommodity_Attach_CtnNo"));
                                                                map.put("batch号",XDCommodity2List.get(0).get("XDCommodity_Attach_BatchNo"));
                                                            }else {
                                                                map.put("净重",list.get(k).get("XDCommodity_GdsNW"));
                                                                map.put("毛重",list.get(k).get("XDCommodity_GdsGW"));
                                                            }

                                                            lists.add(map);
                                                            list.remove(li.get(count.indexOf(cc.get(0).get(k))));
                                                        }else {
                                                            Map<String,Object> mapForMore = new HashMap<>();//封装detail
                                                            mapForMore.put("行号",list.get(k).get("XDCommodity_ItemNo"));
                                                            mapForMore.put("分净重",min.get("XDCommodity_GdsNW"));
                                                            mapForMore.put("分毛重",min.get("XDCommodity_GdsGW"));
                                                            mapForMore.put("batch数量",min.get("XDCommodity_GdsQty1"));
                                                            List<Map> XDCommodity2List = (List<Map>) min.get("XDCommodity2List");
                                                            if(XDCommodity2List != null && XDCommodity2List.size() > 0 ){
                                                                map.put("箱号",XDCommodity2List.get(0).get("XDCommodity_Attach_CtnNo"));
                                                                map.put("batch号",XDCommodity2List.get(0).get("XDCommodity_Attach_BatchNo"));
                                                            }else {
                                                                mapForMore.put("净重",min.get("XDCommodity_GdsNW"));
                                                                mapForMore.put("毛重",min.get("XDCommodity_GdsGW"));
                                                            }
                                                            lists.add(mapForMore);
                                                            list.remove(li.get(count.indexOf(cc.get(0).get(k))));
                                                        }
                                                    }
                                                }else {
                                                    lists.add(map);
                                                }

                                            }
                                            hasXD = false;
                                        }
//                                        if(hasXD){
//                                            lists.add(map);
//                                        }
                                    }
                                }else {
                                    lists.add(map);
                                }
                            }
                        }
                    }else {
                        logger.error("发票无二级匹配失败，操作编号"+ywNo);
                    }
                }
            }


                return listSort(lists);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("单号："+ywNo+",1v1匹配失败,原因："+e.toString());
            return listSort(lists);
        }
    }


    private List<Map>  getXDCommodity(Map<String, List<Map>> dataMap){
        /**
         * 箱单所有数据组装到集合中
         */
        List<Map> lists = new ArrayList<>();
        List<Map> XDList = dataMap.get("XDList");
        if(XDList != null && XDList.size() >0 ){
            for (int i = 0; i < XDList.size(); i++) {
                //发票号
                String XDInviceno = XDList.get(i).get("XDInviceno")+"";
                //原产国
                String XDCOO = XDList.get(i).get("XDCOO")+"";
                List<Map> XDCommodityList = (List<Map>)XDList.get(i).get("XDCommodityList");
                if(XDCommodityList != null && XDCommodityList.size() >0 ){
                    for (int j = 0; j < XDCommodityList.size(); j++) {
                        XDCommodityList.get(j).put("XDInviceno",XDInviceno);
                        XDCommodityList.get(j).put("XDCOO",XDCOO);
                        lists.add(XDCommodityList.get(j));
                    }
                }
            }
        }




        /**
         * 箱单排序
         *  原则
         *      1，有行号的 按照行号搞
         *
         *      2，没用行号按照料号数量大小
         */
        Comparator<Map> comparator = new Comparator<Map>() {
            @Override
            public int compare(Map o1, Map o2) {
                if(checkNull(o1.get("XDCommodity_ItemNo")) &&  checkNull(o2.get("XDCommodity_ItemNo"))){
                    Double D1 = null;
                    Double D2 = null;
                    try {
                         D1 = new Double(o1.get("XDCommodity_ItemNo")+"");

                    }catch (Exception e){
                        D1 = new Double("-1");
                    }
                    try {
                        D2 = new Double(o2.get("XDCommodity_ItemNo")+"");
                    }catch (Exception e){
                        D2 = new Double("-1");
                    }
                    if( D1 < D2){
                        return -1;
                    }else {
                        return 1;
                    }
                }else {
                    return 0;
//                    if(checkNull(o1.get("XDCommodity_GdsQty1")) &&  checkNull(o2.get("XDCommodity_GdsQty1"))){
//                        if(new Double(CleaningString(o1.get("XDCommodity_GdsQty1")+"")) < new Double(CleaningString(o2.get("XDCommodity_GdsQty1")+""))){
//                            return -1;
//                        }else {
//                            return 1;
//                        }
//                    }else {
//
//                    }
                }
            }
        };
        Collections.sort(lists,comparator);
        return lists;
    }

    /**
     * 料号去 空格 比较
     * 无视 横杠  匹配 无视 波浪
     * @param obj
     * @return
     */
    private String deleteBlank(Object obj){
        if((obj+"").contains(" ")){
            return (obj+"").replace(" ","");
        }else
            return obj+"";
    }

    private  List<Map<String,Object>> listSort( List<Map<String,Object>> list){
//        for (int i = 0; i < list.size(); i++) {
//            System.out.println(list.get(i).get("行号"));
//        }
//        Comparator<Map> comparator = new Comparator<Map>() {
//            @Override
//            public int compare(Map o1, Map o2) {
//                if(checkNull(o1.get("行号")) &&  checkNull(o2.get("行号"))){
//                    if(new Double(o1.get("行号")+"") < new Double(o2.get("行号")+"")){
//                        return -1;
//                    }else {
//                        return 1;
//                    }
//                }
//                return 0;
//            }
//        };
//        Collections.sort(list,comparator);
//        System.out.println("=====================================");
//        for (int i = 0; i < list.size(); i++) {
//            System.out.println(list.get(i).get("行号"));
//        }
        return list;
    }

}
