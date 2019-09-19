package com.kwe.kweplus.base;

import com.kwe.kweplus.modal.JintieYwinfo;
import com.kwe.kweplus.util.ocrUtil.OcrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class OcrDataLogic {
    private Logger logger = LoggerFactory.getLogger(OcrDataLogic.class);

    OcrUtil util = new OcrUtil();
    /**
     * 目标：发票  箱单
     * 要求： 不论箱单发票全部要求有料号   --  数量待确认
     * 类型：
     *
     */
    public List<Map<String,Object>> dataFormat(Map<String, List<Map>> dataMap, JintieYwinfo ywinfo){
        //箱单数据集合
        List<Map> list = getXDCommodity(dataMap);
        //返回的lists
        List<Map<String,Object>> lists = new ArrayList<>();
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
                                if(util.checkNull(FPCommodity_Currency)){
                                    map.put("币制",FPCommodity_Currency);
                                }else {
                                    map.put("币制",FPCurrency);
                                }
                                //原产国
                                String FPCommodity_COO = FPCommodityList.get(j).get("FPCommodity_COO")+"";
                                if(util.checkNull(FPCommodity_COO)){
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
                                if(!util.checkNull(map.get("单位"))){
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
                                /**
                                 * 发票三级数据组装
                                 */
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
                                 * 新规则
                                 *          数量
                                 *          料号
                                 *          发票号
                                 *          行号
                                 *      拼接规则
                                 *              规则：
                                 *                  1，料号数量匹配！
                                 *                      一：情况：出现如GD 没有数量的情况！
                                 *                      二：出现如瑞萨的行号匹配错乱
                                 */
                                if(list != null && list.size() >0 ){
                                    for (int k = 0; k < list.size(); k++) {
                                        /**
                                         * 1，料号一致  数量一致     直接拼接放入
                                         * 2，料号一致  数量不匹配   放入数字组内，算法计算
                                         *
                                         * ⭐⭐⭐⭐⭐⭐⭐⭐数量需要进行提前格式化处理⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐
                                         *
                                         * 1,料号去空白比较一致
                                         *  一致：进行步骤2   不一致：不进行操作
                                         * 2. 确认有没有数量
                                         *  有：进行步骤3   无：进行步骤6
                                         * 3，比较数量一致否
                                         *   一致：进行步骤4  不一致：进行步骤5
                                         * 4，料号数量一致 进行匹配添加！原箱单数据移除选中数据,记得跳出         ⭐⭐⭐
                                         * 5，放入计算池中，通过方法计算出那几个数字合并可以拼凑
                                         *          得出结果进行添加，原箱单数据移除选中数据                    ⭐⭐⭐⭐⭐
                                         * 6,因为没有数量走 发票号料号匹配         待定                    ⭐
                                         *      6.1确认是否存在发票号，料号
                                         *      有：进行步骤7   无：不进行操作
                                         * 7，发票号 料号进行匹配
                                         * 一致：进行添加  不一致不进行操作
                                         *
                                         */
                                        //步骤1
                                        if(util.deleteBlank(map.get("料号")).equals(util.deleteBlank(list.get(k).get("XDCommodity_PartNumber")))){
                                            //步骤2
                                            if(util.checkNull(map.get("数量")) && util.checkNull(list.get(k).get("XDCommodity_GdsQty1"))){
                                                //步骤3
                                                if(map.get("数量").equals(list.get(k).get("XDCommodity_GdsQty1"))){
                                                    //匹配添加
                                                    //箱单数据外层补充
                                                    if(!util.checkNull(map.get("原产国"))){
                                                        map.put("原产国",list.get(k).get("XDCOO"));
                                                    }
                                                    if(!util.checkNull(map.get("原产国"))){
                                                        map.put("原产国",list.get(k).get("XDCommodity_COO"));
                                                    }
                                                    if(!util.checkNull(map.get("单位"))){
                                                        map.put("单位",list.get(k).get("XDCommodity_GdsUnit1"));
                                                    }
                                                    if(!util.checkNull(map.get("单位"))){
                                                        map.put("单位",list.get(k).get("XDGdsUnit1"));
                                                    }
                                                    if(!util.checkNull(map.get("PO号"))){
                                                        map.put("PO号",list.get(k).get("XDCommodity_PO"));
                                                    }
                                                    if(!util.checkNull(map.get("入库发票号"))){
                                                        map.put("入库发票号",list.get(k).get("XDCommodity_InXDNo"));
                                                    }
                                                    if(!util.checkNull(map.get("进库业务编号"))){
                                                        map.put("进库业务编号",list.get(k).get("XDCommodity_InWorkNo"));
                                                    }
                                                    if(!util.checkNull(map.get("进库运单号"))){
                                                        map.put("进库运单号",list.get(k).get("XDCommodity_InWayBill"));
                                                    }
                                                    if(!util.checkNull(map.get("最终用户料号"))){
                                                        map.put("最终用户料号",list.get(k).get("XDCommodity_EndUserNo"));
                                                    }
                                                    if(!util.checkNull(map.get("订单号"))){
                                                        map.put("订单号",list.get(k).get("XDCommodity_OrderNo"));
                                                    }
                                                    if(!util.checkNull(map.get("CPN"))){
                                                        map.put("CPN",list.get(k).get("XDCommodity_CPN"));
                                                    }
                                                    if(!util.checkNull(map.get("最终用户"))){
                                                        map.put("最终用户",list.get(k).get("XDCommodity_EndUser"));
                                                    }
                                                    if(!util.checkNull(map.get("SO号"))){
                                                        map.put("SO号",list.get(k).get("XDCommodity_So"));
                                                    }
                                                    if(!util.checkNull(map.get("最小包装数量"))){
                                                        map.put("最小包装数量",list.get(k).get("XDCommodity_MOQ"));
                                                    }
                                                    if(!util.checkNull(map.get("备注栏"))){
                                                        map.put("备注栏",list.get(k).get("XDCommodity_ReMarks"));
                                                    }
                                                    if(!util.checkNull(map.get("唛头/件数/米数"))){
                                                        map.put("入库发票号",list.get(k).get("XDCommodity_ReMarks"));
                                                    }
                                                    if(!util.checkNull(map.get("最终用户料号2"))){
                                                        map.put("最终用户料号2",list.get(k).get("XDCommodity_EndUser2"));
                                                    }
                                                    if(!util.checkNull(map.get("最终用户料号3"))){
                                                        map.put("最终用户料号3",list.get(k).get("XDCommodity_EndUser3"));
                                                    }
                                                    if(!util.checkNull(map.get("Shipment no"))){
                                                        map.put("Shipment no",list.get(k).get("XDCommodity_ShipmentNo"));
                                                    }
                                                    if(!util.checkNull(map.get("SN号"))){
                                                        map.put("SN号",list.get(k).get("XDCommodity_SN"));
                                                    }
                                                    if(!util.checkNull(map.get("部门"))){
                                                        map.put("部门",list.get(k).get("XDCommodity_Dept"));
                                                    }
                                                    if(!util.checkNull(map.get("Delivery No"))){
                                                        map.put("Delivery No",list.get(k).get("XDCommodity_DN"));
                                                    }
                                                    if(!util.checkNull(map.get("箱数"))){
                                                        map.put("箱数",list.get(k).get("XDCommodity_GdsCtnQty"));
                                                    }
                                                    if(!util.checkNull(map.get("生产日期"))){
                                                        map.put("生产日期",list.get(k).get("XDCommodity_InProductiondate"));
                                                    }
                                                    if(!util.checkNull(map.get("序列号"))){
                                                        map.put("序列号",list.get(k).get("XDCommodity_BoxNumber"));
                                                    }

                                                }else {
                                                    //步骤5

                                                }
                                            }else {
                                                //步骤6
                                                System.out.println("数量空的");
                                            }
                                        }
                                    }
                                }else {
                                    //因为箱单为空所以直接添加
                                    lists.add(map);
                                }

                            }
                        }else {
                            logger.error("发票无二级数据，编号:"+ywinfo.getYwNo());

                        }
                    }

                }else {
                    logger.error("发票一共0张，编号："+ywinfo.getYwNo());
                }
            return lists;
        }catch (Exception e){
            return lists;
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
                //单位
                String XDGdsUnit1 = XDList.get(i).get("XDGdsUnit1")+"";
                List<Map> XDCommodityList = (List<Map>)XDList.get(i).get("XDCommodityList");
                if(XDCommodityList != null && XDCommodityList.size() >0 ){
                    for (int j = 0; j < XDCommodityList.size(); j++) {
                        XDCommodityList.get(j).put("XDInviceno",XDInviceno);
                        XDCommodityList.get(j).put("XDCOO",XDCOO);
                        XDCommodityList.get(j).put("XDGdsUnit1",XDGdsUnit1);
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
                if(util.checkNull(o1.get("XDCommodity_ItemNo")) &&  util.checkNull(o2.get("XDCommodity_ItemNo"))){
                    if(Integer.parseInt(o1.get("XDCommodity_ItemNo")+"") > Integer.parseInt(o2.get("XDCommodity_ItemNo")+"")){
                        return 1;
                    }else
                        return -1;
                }else {
                    return 0;
                }
            }
        };
        Collections.sort(lists,comparator);
        return lists;
    }


    private void first(List<Map> list,Map<String,Object> map){
        for (int k = 0; k < list.size(); k++) {
            //箱单数据外层补充
            if(!util.checkNull(map.get("原产国"))){
                map.put("原产国",list.get(k).get("XDCOO"));
            }
            if(!util.checkNull(map.get("原产国"))){
                map.put("原产国",list.get(k).get("XDCommodity_COO"));
            }
            if(!util.checkNull(map.get("单位"))){
                map.put("单位",list.get(k).get("XDCommodity_GdsUnit1"));
            }
            if(!util.checkNull(map.get("PO号"))){
                map.put("PO号",list.get(k).get("XDCommodity_PO"));
            }
            if(!util.checkNull(map.get("入库发票号"))){
                map.put("入库发票号",list.get(k).get("XDCommodity_InXDNo"));
            }
            if(!util.checkNull(map.get("进库业务编号"))){
                map.put("进库业务编号",list.get(k).get("XDCommodity_InWorkNo"));
            }
            if(!util.checkNull(map.get("进库运单号"))){
                map.put("进库运单号",list.get(k).get("XDCommodity_InWayBill"));
            }
            if(!util.checkNull(map.get("最终用户料号"))){
                map.put("最终用户料号",list.get(k).get("XDCommodity_EndUserNo"));
            }
            if(!util.checkNull(map.get("订单号"))){
                map.put("订单号",list.get(k).get("XDCommodity_OrderNo"));
            }
            if(!util.checkNull(map.get("CPN"))){
                map.put("CPN",list.get(k).get("XDCommodity_CPN"));
            }
            if(!util.checkNull(map.get("最终用户"))){
                map.put("最终用户",list.get(k).get("XDCommodity_EndUser"));
            }
            if(!util.checkNull(map.get("SO号"))){
                map.put("SO号",list.get(k).get("XDCommodity_So"));
            }
            if(!util.checkNull(map.get("最小包装数量"))){
                map.put("最小包装数量",list.get(k).get("XDCommodity_MOQ"));
            }
            if(!util.checkNull(map.get("备注栏"))){
                map.put("备注栏",list.get(k).get("XDCommodity_ReMarks"));
            }
            if(!util.checkNull(map.get("唛头/件数/米数"))){
                map.put("入库发票号",list.get(k).get("XDCommodity_ReMarks"));
            }
            if(!util.checkNull(map.get("最终用户料号2"))){
                map.put("最终用户料号2",list.get(k).get("XDCommodity_EndUser2"));
            }
            if(!util.checkNull(map.get("最终用户料号3"))){
                map.put("最终用户料号3",list.get(k).get("XDCommodity_EndUser3"));
            }
            if(!util.checkNull(map.get("Shipment no"))){
                map.put("Shipment no",list.get(k).get("XDCommodity_ShipmentNo"));
            }
            if(!util.checkNull(map.get("SN号"))){
                map.put("SN号",list.get(k).get("XDCommodity_SN"));
            }
            if(!util.checkNull(map.get("部门"))){
                map.put("部门",list.get(k).get("XDCommodity_Dept"));
            }
            if(!util.checkNull(map.get("Delivery No"))){
                map.put("Delivery No",list.get(k).get("XDCommodity_DN"));
            }
            if(!util.checkNull(map.get("箱数"))){
                map.put("箱数",list.get(k).get("XDCommodity_GdsCtnQty"));
            }
            if(!util.checkNull(map.get("生产日期"))){
                map.put("生产日期",list.get(k).get("XDCommodity_InProductiondate"));
            }
            if(!util.checkNull(map.get("序列号"))){
                map.put("序列号",list.get(k).get("XDCommodity_BoxNumber"));
            }


        }
    }


}
