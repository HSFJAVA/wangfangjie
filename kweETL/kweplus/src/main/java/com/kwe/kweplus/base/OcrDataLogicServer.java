package com.kwe.kweplus.base;

import com.kwe.kweplus.util.CalcUtil;
import com.kwe.kweplus.util.ocrUtil.OcrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by Ly on 2019/8/21 8:45
 */
public class OcrDataLogicServer {

    private Logger logger = LoggerFactory.getLogger(OcrDataLogicServer.class);
    OcrUtil util = new OcrUtil();
    /**
     * 格式化数据
     *      确认发票箱单页数
     * @param dataMap
     * @param ywNo
     * @return
     */
    public List<Map<String,Object>> dataFormat(Map<String, List<Map>> dataMap, String ywNo){
        List<Map<String,Object>> lists = new ArrayList<>();//转换后的lists
        try {
            List<Map> FPList =  dataMap.get("FPList");
            List<Map> XDList =  dataMap.get("XDList");
            boolean hasFP = false;
            boolean hasManyFP = false;
            boolean hasXD = false;
            boolean hasManyXD = false;
            //判断发票
            if( FPList != null &&  FPList.size() > 0  ){
                hasFP = true;
                if(FPList.size() > 1){
                    hasManyFP = true;
                }
            }else {
                logger.info("编号："+ywNo+",业务没有发票!");
            }
            //判断箱单
            if(XDList != null && XDList.size() >0 ){
                hasXD = true;
                if(XDList.size() > 1){
                    hasManyXD = true;
                }
            }else {
                logger.info("编号："+ywNo+",业务没有箱单!");
            }
            /**
             * 发票不为空，分析发票的页数和箱单的页数
             *      情况1，只有发票
             *      情况2，发票 箱单刚好一张对一张
             *      情况3，发票箱单多对多，通过发票号匹配
             */
            if(hasFP && !hasXD){
                lists = onlyHasInvoice(dataMap,ywNo);
                logger.info("编号："+ywNo+",只有发票,展示发票数据!");
            }else if(hasFP && hasXD){
                if(!hasManyFP && !hasManyXD){
                    lists = oneInvoiceAndOnePackingList(dataMap,ywNo);
                    logger.info("编号："+ywNo+",发票，箱单一张对一张");
                }else if(hasManyFP && hasManyXD){
                    lists = manyInvoiceAndManyPackingList(dataMap,ywNo);
                    logger.info("编号："+ywNo+",发票箱单多对多，通过发票号匹配");
                }
            }
            return lists;
        }catch (Exception e){
            return lists;
        }
    }

    /**
     * 只有发票
     * @param dataMap
     * @return
     */
    private List<Map<String,Object>> onlyHasInvoice(Map<String, List<Map>> dataMap,String ywNo){
        List<Map<String,Object>> lists = new ArrayList<>();//转换后的lists
        try {
            List<Map> FPList =  dataMap.get("FPList");
            if( FPList != null &&  FPList.size() > 0  ){
                for (int i = 0; i < FPList.size(); i++) {
                    //发票号
                    String FPNo = FPList.get(i).get("FPNo")+"";
                    //币制
                    String FPCurrency = FPList.get(i).get("FPCurrency")+"";
                    //原产国
                    String FPCOO = FPList.get(i).get("FPCOO")+"";
                    //单位
                    String FPGdsUnit = FPList.get(i).get("FPGdsUnit")+"";
                    List<Map> FPCommodityList = (List<Map>)FPList.get(i).get("FPCommodityList");
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
                            if(util.checkNull(FPCommodity_GdsUnit1)){
                                map.put("单位",FPCommodity_GdsUnit1);
                            }else {
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
                                    String FPCommodity_Attach_EngineNo = FPCommodity2List.get(k).get("FPCommodity_Attach_EngineNo")+"";
                                    String FPCommodity_Attach_LotNo = FPCommodity2List.get(k).get("FPCommodity_Attach_LotNo")+"";
                                    String FPCommodity_Attach_SupplierGoodQty = FPCommodity2List.get(k).get("FPCommodity_Attach_SupplierGoodQty")+"";
                                    if(k == 0){
                                        map.put("batch数量",FPCommodity_Attach_BatchQty);
                                        map.put("分净重",FPCommodity_Attach_SupplierNw);
                                        map.put("分毛重",FPCommodity_Attach_SupplierGw);
                                        map.put("分金额",FPCommodity_Attach_SupplierAmount);
                                        map.put("batch号",FPCommodity_Attach_BatchNo);
                                        map.put("箱号",FPCommodity_Attach_CtnNo);
                                        map.put("发动机号",FPCommodity_Attach_EngineNo);
                                        map.put("Lot No",FPCommodity_Attach_LotNo);
                                        map.put("良品数量",FPCommodity_Attach_SupplierGoodQty);
                                        lists.add(map);
                                    }else {
                                        Map<String,Object> mapForMore = new HashMap<>();//封装detail
                                        mapForMore.put("batch数量",FPCommodity_Attach_BatchQty);
                                        mapForMore.put("分净重",FPCommodity_Attach_SupplierNw);
                                        mapForMore.put("分毛重",FPCommodity_Attach_SupplierGw);
                                        mapForMore.put("分金额",FPCommodity_Attach_SupplierAmount);
                                        mapForMore.put("batch号",FPCommodity_Attach_BatchNo);
                                        mapForMore.put("箱号",FPCommodity_Attach_CtnNo);
                                        mapForMore.put("发动机号",FPCommodity_Attach_EngineNo);
                                        mapForMore.put("Lot No",FPCommodity_Attach_LotNo);
                                        mapForMore.put("良品数量",FPCommodity_Attach_SupplierGoodQty);
                                        lists.add(mapForMore);
                                    }
                                }
                            }else {
                                lists.add(map);
                            }
                        }
                    }else {
                        logger.warn("在仅有发票的前提下，发票没有二级，业务编号："+ywNo);
                    }
                }
            }
            return lists;
        }catch (Exception e){
//            e.printStackTrace();
            return lists;
        }
    }

    /**
     * 多张发票多张箱单
     * @param dataMap
     * @param ywNo
     * @return
     */
    private List<Map<String,Object>> manyInvoiceAndManyPackingList(Map<String, List<Map>> dataMap,String ywNo){
        List<Map<String,Object>> lists = new ArrayList<>();//转换后的lists
        try {
            List<Map> FPList = dataMap.get("FPList");
            List<Map> XDList = dataMap.get("XDList");
            for (int i = 0; i < FPList.size(); i++) {
                String FPNo = FPList.get(i).get("FPNo")+"";
                boolean jointIsFalse = true;

                for (int j = 0; j < XDList.size(); j++) {
                    String XDInviceno = XDList.get(j).get("XDInviceno")+"";
                    if(util.checkNull(FPNo)){
                        if(XDInviceno.equals(FPNo)){
                            jointIsFalse = false;
                            //调用发票箱单1v1的
                            lists = oneInvoiceAndOnePackingList(dataMap,ywNo);
                        }
                    }else {
                        //发票号为空
                        break;
                    }
                }
                if(jointIsFalse){
                        //多张情况下没办法匹配，直接料号数量
                }
            }
            return lists;
        }catch (Exception e){
            return lists;
        }
    }


    /**
     * 传进来一张发票一张箱单进行逻辑处理
     * @param ywNo
     * @return
     */
    private List<Map<String,Object>> oneInvoiceAndOnePackingList(Map<String, List<Map>> dataMap,String ywNo){
        List<Map<String,Object>> lists = new ArrayList<>();//转换后的lists
        try {
            boolean FPhasPartNumber = false;
            boolean FPhasGdsQty1 = false;
            boolean XDhasPartNumber = false;
            boolean XDhasGdsQty1 = false;
            List<Map> FPList = dataMap.get("FPList");
            List<Map> XDList = dataMap.get("XDList");
            if(FPList != null && FPList.size() > 0 ){
                for (int i = 0; i < FPList.size(); i++) {
                    List<Map> FPCommodityList = (List<Map>)FPList.get(i).get("FPCommodityList");
                    if(FPCommodityList != null && FPCommodityList.size() >0 ){
                        for (int j = 0; j < FPCommodityList.size(); j++) {
                            //商品料号
                            String FPCommodity_PartNumber = FPCommodityList.get(j).get("FPCommodity_PartNumber")+"";
                            //料号数量
                            String FPCommodity_GdsQty1 = FPCommodityList.get(j).get("FPCommodity_GdsQty1")+"";
                            if(util.checkNull(FPCommodity_PartNumber)){
                                FPhasPartNumber = true;
                            }
                            if(util.checkNull(FPCommodity_GdsQty1)){
                                FPhasGdsQty1 = true;
                            }

                        }
                    }else {
                        logger.warn("编号："+ywNo+",发票箱单1v1匹配成功后，发票无二级");
                    }
                }
            }
            if(XDList != null && XDList.size() >0 ){
                for (int i = 0; i < XDList.size(); i++) {
                    List<Map> XDCommodityList = (List<Map>)XDList.get(i).get("XDCommodityList");
                    if(XDCommodityList != null && XDCommodityList.size() >0 ){
                        for (int j = 0; j < XDCommodityList.size(); j++) {
                            //商品料号
                            String XDCommodity_PartNumber = XDCommodityList.get(j).get("XDCommodity_PartNumber")+"";
                            //料号数量
                            String XDCommodity_GdsQty1 = XDCommodityList.get(j).get("XDCommodity_GdsQty1")+"";
                            if(util.checkNull(XDCommodity_PartNumber)){
                                XDhasPartNumber= true;
                            }
                            if(util.checkNull(XDCommodity_GdsQty1)){
                                XDhasGdsQty1 = true;
                            }
                        }
                    }else {
                        logger.warn("编号："+ywNo+",发票箱单1v1匹配成功后，箱单无二级");
                    }
                }
            }

            /**
             * 十六种概率
             */
            boolean byPartNumber = false;
            boolean byGdsQty1 = false;
            if(!FPhasPartNumber && !FPhasGdsQty1){
                logger.warn("编号："+ywNo+",发票没有料号,没有数量，直接展示发票数据");
                //情况2
            }else if(FPhasPartNumber && !FPhasGdsQty1 && !XDhasPartNumber && !XDhasGdsQty1){
                logger.warn("编号："+ywNo+",发票有料号,没有数量，箱单没有料号，没有数量，直接展示发票数据");
            }else if(FPhasPartNumber && !FPhasGdsQty1 && XDhasPartNumber && !XDhasGdsQty1){
                byPartNumber = true;
                logger.warn("编号："+ywNo+",发票有料号,没有数量，箱单有料号，没有数量，料号匹配");
            }else if(FPhasPartNumber && !FPhasGdsQty1 && !XDhasPartNumber && XDhasGdsQty1){
                logger.warn("编号："+ywNo+",发票有料号,没有数量，箱单没有料号，有数量，直接展示发票数据");
            }else if(FPhasPartNumber && !FPhasGdsQty1 && XDhasPartNumber && XDhasGdsQty1){
                byPartNumber = true;
                logger.warn("编号："+ywNo+",发票有料号,没有数量，箱单有料号，有数量，料号匹配");
                //情况3
            }else if(!FPhasPartNumber && FPhasGdsQty1 && !XDhasPartNumber && !XDhasGdsQty1){
                logger.warn("编号："+ywNo+",发票无料号，有数量，箱单没有料号，没有数量，放弃箱单");
            }else if(!FPhasPartNumber && FPhasGdsQty1 && XDhasPartNumber && !XDhasGdsQty1){
                logger.warn("编号："+ywNo+",发票无料号，有数量，箱单有料号，无数量，放弃箱单");
            }else if(!FPhasPartNumber && FPhasGdsQty1 && !XDhasPartNumber && XDhasGdsQty1){
                byGdsQty1 = true;
                logger.warn("编号："+ywNo+",发票无料号，有数量，箱单无料号，有数量，数量匹配");
            }else if(!FPhasPartNumber && FPhasGdsQty1 && XDhasPartNumber && XDhasGdsQty1){
                byGdsQty1 = true;
                logger.warn("编号："+ywNo+",发票无料号，有数量，箱单有料号，有数量，数量匹配");
                //情况4
            }else if(FPhasPartNumber && FPhasGdsQty1 && !XDhasPartNumber && !XDhasGdsQty1){
                logger.warn("编号："+ywNo+",发票有料号，有数量，箱单无料号，无数量，放弃箱单");
            }else if(FPhasPartNumber && FPhasGdsQty1 && XDhasPartNumber && !XDhasGdsQty1){
                byPartNumber = true;
                logger.warn("编号："+ywNo+",发票有料号，有数量，箱单有料号，无数量，料号匹配");
            }else if(FPhasPartNumber && FPhasGdsQty1 && !XDhasPartNumber && XDhasGdsQty1){
                logger.warn("编号："+ywNo+",发票有料号，有数量，箱单无料号，有数量，数量匹配");
            }else if(FPhasPartNumber && FPhasGdsQty1 && XDhasPartNumber && XDhasGdsQty1){
                byPartNumber = true;
                byGdsQty1 = true;
                logger.warn("编号："+ywNo+",发票有料号，有数量，箱单有料号，有数量，料号数量匹配");
                //情况5
            }
            /**
             * 四种拼接方式
             *  1，直接展示发票信息
             *  2，通过料号匹配
             *  3，通过数量匹配
             *  4，通过料号数量匹配
             *
             *   @Date 2019年8月13日
             *      新思路：通过查询业务表判断此票业务的字段是否有对多，
             *          如有那么箱单上的二级数量就是batch数量，毛重就说分毛重，净重就是分镜中
             *          没有，那么二级毛重就是毛重，净重就是净重
             */
            if(!byPartNumber && !byGdsQty1 ){
                lists = onlyHasInvoice(dataMap,ywNo);
            }else if(byPartNumber && !byGdsQty1 ){
                //通过料号匹配
                lists = logicForDataByPartNumber(dataMap);
            }else if(!byPartNumber && byGdsQty1 ){
                //通过数量匹配
                lists = logicForDataByGdsQty1(dataMap);
            }else if(byPartNumber && byGdsQty1){
                //通过料号数量匹配
                lists = logicForDataByPartNumberAndGdsQty1(dataMap);
            }
            return lists;
        }catch (Exception e){
            e.printStackTrace();
            return lists;
        }
    }

    /**
     * 通过料号匹配
     * @param dataMap
     * @return
     */
    private List<Map<String,Object>> logicForDataByPartNumber(Map<String, List<Map>> dataMap){
        List<Map<String,Object>> lists = new ArrayList<>();//转换后的lists
        List<Map> FPList = dataMap.get("FPList");
        List<Map> XDList = dataMap.get("XDList");
        for (int i = 0; i < FPList.size(); i++) {
            //发票号
            String FPNo = FPList.get(i).get("FPNo")+"";
            //币制
            String FPCurrency = FPList.get(i).get("FPCurrency")+"";
            //原产国
            String FPCOO = FPList.get(i).get("FPCOO")+"";
            //单位
            String FPGdsUnit = FPList.get(i).get("FPGdsUnit")+"";
            List<Map> FPCommodityList = (List<Map>)FPList.get(i).get("FPCommodityList");
            if( FPCommodityList != null && FPCommodityList.size() > 0){
                boolean ok = true;
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
                    if(util.checkNull(FPCommodity_GdsUnit1)){
                        map.put("单位",FPCommodity_GdsUnit1);
                    }else {
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
                            String FPCommodity_Attach_EngineNo = FPCommodity2List.get(k).get("FPCommodity_Attach_EngineNo")+"";
                            String FPCommodity_Attach_LotNo = FPCommodity2List.get(k).get("FPCommodity_Attach_LotNo")+"";
                            String FPCommodity_Attach_SupplierGoodQty = FPCommodity2List.get(k).get("FPCommodity_Attach_SupplierGoodQty")+"";
                            if(k == 0){
                                map.put("batch数量",FPCommodity_Attach_BatchQty);
                                map.put("分净重",FPCommodity_Attach_SupplierNw);
                                map.put("分毛重",FPCommodity_Attach_SupplierGw);
                                map.put("分金额",FPCommodity_Attach_SupplierAmount);
                                map.put("batch号",FPCommodity_Attach_BatchNo);
                                map.put("箱号",FPCommodity_Attach_CtnNo);
                                map.put("发动机号",FPCommodity_Attach_EngineNo);
                                map.put("Lot No",FPCommodity_Attach_LotNo);
                                map.put("良品数量",FPCommodity_Attach_SupplierGoodQty);
                                lists.add(map);
                            }else {
                                Map<String,Object> mapForMore = new HashMap<>();//封装detail
                                mapForMore.put("batch数量",FPCommodity_Attach_BatchQty);
                                mapForMore.put("分净重",FPCommodity_Attach_SupplierNw);
                                mapForMore.put("分毛重",FPCommodity_Attach_SupplierGw);
                                mapForMore.put("分金额",FPCommodity_Attach_SupplierAmount);
                                mapForMore.put("batch号",FPCommodity_Attach_BatchNo);
                                mapForMore.put("箱号",FPCommodity_Attach_CtnNo);
                                mapForMore.put("发动机号",FPCommodity_Attach_EngineNo);
                                mapForMore.put("Lot No",FPCommodity_Attach_LotNo);
                                mapForMore.put("良品数量",FPCommodity_Attach_SupplierGoodQty);
                                lists.add(mapForMore);
                            }
                        }
                    }else {
                        /**
                         * 料号匹配箱单
                         */
                        for (int k = 0; k < XDList.size(); k++) {
                            //原产国
                            String XDCOO = XDList.get(k).get("XDCOO")+"";
                            //数量单位
                            String XDGdsUnit1 = XDList.get(k).get("XDGdsUnit1")+"";
                            List<Map> XDCommodityList = (List<Map>)XDList.get(i).get("XDCommodityList");
                            for (int l = 0; l < XDCommodityList.size(); l++) {
                                if(util.deleteBlank(map.get("料号")).equals(util.deleteBlank(XDCommodityList.get(l).get("XDCommodity_PartNumber"))) ){
                                    //箱单数据外层补充
                                    if(!util.checkNull(map.get("原产国"))){
                                        map.put("原产国",XDCommodityList.get(l).get("XDCommodity_COO"));
                                    }
                                    if(!util.checkNull(map.get("原产国"))){
                                        map.put("原产国",XDCOO);
                                    }
                                    if(!util.checkNull(map.get("单位"))){
                                        map.put("单位",XDCommodityList.get(l).get("XDCommodity_GdsUnit1"));
                                    }
                                    if(!util.checkNull(map.get("单位"))){
                                        map.put("单位",XDGdsUnit1);
                                    }
                                    if(!util.checkNull(map.get("PO号"))){
                                        map.put("PO号",XDCommodityList.get(l).get("XDCommodity_PO"));
                                    }
                                    if(!util.checkNull(map.get("入库发票号"))){
                                        map.put("入库发票号",XDCommodityList.get(l).get("XDCommodity_InXDNo"));
                                    }
                                    if(!util.checkNull(map.get("进库业务编号"))){
                                        map.put("进库业务编号",XDCommodityList.get(l).get("XDCommodity_InWorkNo"));
                                    }
                                    if(!util.checkNull(map.get("进库运单号"))){
                                        map.put("进库运单号",XDCommodityList.get(l).get("XDCommodity_InWayBill"));
                                    }
                                    if(!util.checkNull(map.get("最终用户料号"))){
                                        map.put("最终用户料号",XDCommodityList.get(l).get("XDCommodity_EndUserNo"));
                                    }
                                    if(!util.checkNull(map.get("订单号"))){
                                        map.put("订单号",XDCommodityList.get(l).get("XDCommodity_OrderNo"));
                                    }
                                    if(!util.checkNull(map.get("CPN"))){
                                        map.put("CPN",XDCommodityList.get(l).get("XDCommodity_CPN"));
                                    }
                                    if(!util.checkNull(map.get("最终用户"))){
                                        map.put("最终用户",XDCommodityList.get(l).get("XDCommodity_EndUser"));
                                    }
                                    if(!util.checkNull(map.get("SO号"))){
                                        map.put("SO号",XDCommodityList.get(l).get("XDCommodity_So"));
                                    }
                                    if(!util.checkNull(map.get("最小包装数量"))){
                                        map.put("最小包装数量",XDCommodityList.get(l).get("XDCommodity_MOQ"));
                                    }
                                    if(!util.checkNull(map.get("备注栏"))){
                                        map.put("备注栏",XDCommodityList.get(l).get("XDCommodity_ReMarks"));
                                    }
                                    if(!util.checkNull(map.get("唛头/件数/米数"))){
                                        map.put("入库发票号",XDCommodityList.get(l).get("XDCommodity_ReMarks"));
                                    }
                                    if(!util.checkNull(map.get("最终用户料号2"))){
                                        map.put("最终用户料号2",XDCommodityList.get(l).get("XDCommodity_EndUser2"));
                                    }
                                    if(!util.checkNull(map.get("最终用户料号3"))){
                                        map.put("最终用户料号3",XDCommodityList.get(l).get("XDCommodity_EndUser3"));
                                    }
                                    if(!util.checkNull(map.get("Shipment no"))){
                                        map.put("Shipment no",XDCommodityList.get(l).get("XDCommodity_ShipmentNo"));
                                    }
                                    if(!util.checkNull(map.get("SN号"))){
                                        map.put("SN号",XDCommodityList.get(l).get("XDCommodity_SN"));
                                    }
                                    if(!util.checkNull(map.get("部门"))){
                                        map.put("部门",XDCommodityList.get(l).get("XDCommodity_Dept"));
                                    }
                                    if(!util.checkNull(map.get("Delivery No"))){
                                        map.put("Delivery No",XDCommodityList.get(l).get("XDCommodity_DN"));
                                    }
                                    if(!util.checkNull(map.get("箱数"))){
                                        map.put("箱数",XDCommodityList.get(l).get("XDCommodity_GdsCtnQty"));
                                    }
                                    if(!util.checkNull(map.get("生产日期"))){
                                        map.put("生产日期",XDCommodityList.get(l).get("XDCommodity_InProductiondate"));
                                    }
                                    if(!util.checkNull(map.get("序列号"))){
                                        map.put("序列号",XDCommodityList.get(l).get("XDCommodity_BoxNumber"));
                                    }
                                    if(!util.checkNull(map.get("行号"))){
                                        map.put("行号",XDCommodityList.get(l).get("XDCommodity_ItemNo"));
                                    }

                                    if(!util.checkNull(map.get("净重"))){
                                        map.put("净重",XDCommodityList.get(l).get("XDCommodity_GdsNW"));
                                    }
                                    if(!util.checkNull(map.get("毛重"))){
                                        map.put("毛重",XDCommodityList.get(l).get("XDCommodity_GdsGW"));
                                    }
                                    List<Map> XDCommodity2List = (List<Map>) XDCommodityList.get(l).get("XDCommodity2List");
                                    if(XDCommodity2List != null && XDCommodity2List.size() > 0 ){
                                        for (int m = 0; m < XDCommodity2List.size(); m++) {
                                            String XDCommodity_Attach_BatchQty = XDCommodity2List.get(m).get("XDCommodity_Attach_BatchQty")+"";
                                            String XDCommodity_Attach_SupplierNw = XDCommodity2List.get(m).get("XDCommodity_Attach_SupplierNw")+"";
                                            String XDCommodity_Attach_SupplierGw = XDCommodity2List.get(m).get("XDCommodity_Attach_SupplierGw")+"";
                                            String XDCommodity_Attach_SupplierAmount = XDCommodity2List.get(m).get("XDCommodity_Attach_SupplierAmount ")+"";
                                            String XDCommodity_Attach_BatchNo = XDCommodity2List.get(m).get("XDCommodity_Attach_BatchNo")+"";
                                            String XDCommodity_Attach_CtnNo = XDCommodity2List.get(m).get("XDCommodity_Attach_CtnNo")+"";
                                            String XDCommodity_Attach_EngineNo = XDCommodity2List.get(m).get("XDCommodity_Attach_EngineNo")+"";
                                            String XDCommodity_Attach_LotNo = XDCommodity2List.get(m).get("XDCommodity_Attach_LotNo")+"";
                                            String XDCommodity_Attach_SupplierGoodQty = XDCommodity2List.get(m).get("XDCommodity_Attach_SupplierGoodQty")+"";
                                            if(m == 0){
                                                map.put("batch数量",XDCommodity_Attach_BatchQty);
                                                map.put("分净重",XDCommodity_Attach_SupplierNw);
                                                map.put("分毛重",XDCommodity_Attach_SupplierGw);
                                                map.put("分金额",XDCommodity_Attach_SupplierAmount);
                                                map.put("batch号",XDCommodity_Attach_BatchNo);
                                                map.put("箱号",XDCommodity_Attach_CtnNo);
                                                map.put("发动机号",XDCommodity_Attach_EngineNo);
                                                map.put("Lot No",XDCommodity_Attach_LotNo);
                                                map.put("良品数量",XDCommodity_Attach_SupplierGoodQty);
                                                lists.add(map);
                                            }else {
                                                Map<String,Object> mapForMore = new HashMap<>();//封装detail
                                                mapForMore.put("batch数量",XDCommodity_Attach_BatchQty);
                                                mapForMore.put("分净重",XDCommodity_Attach_SupplierNw);
                                                mapForMore.put("分毛重",XDCommodity_Attach_SupplierGw);
                                                mapForMore.put("分金额",XDCommodity_Attach_SupplierAmount);
                                                mapForMore.put("batch号",XDCommodity_Attach_BatchNo);
                                                mapForMore.put("箱号",XDCommodity_Attach_CtnNo);
                                                mapForMore.put("发动机号",XDCommodity_Attach_EngineNo);
                                                mapForMore.put("Lot No",XDCommodity_Attach_LotNo);
                                                mapForMore.put("良品数量",XDCommodity_Attach_SupplierGoodQty);
                                                lists.add(mapForMore);
                                            }
                                        }
                                    }else {
                                        lists.add(map);
                                    }
                                    XDCommodityList.remove(l);
                                    l--;
                                    ok = false;
                                    break;
                                }
                            }
                        }
                    }
                    if(ok){
                        lists.add(map);
                    }

                }
            }else {
                logger.warn("在仅有发票的前提下，发票没有二级");
            }
        }
        return lists;
    }

    /**
     * 通过数量匹配
     * @return
     */
    private List<Map<String,Object>> logicForDataByGdsQty1(Map<String, List<Map>> dataMap){
        List<Map<String,Object>> lists = new ArrayList<>();//转换后的lists
        List<Map> FPList = dataMap.get("FPList");
        List<Map> XDList = dataMap.get("XDList");
        for (int i = 0; i < FPList.size(); i++) {
            //发票号
            String FPNo = FPList.get(i).get("FPNo")+"";
            //币制
            String FPCurrency = FPList.get(i).get("FPCurrency")+"";
            //原产国
            String FPCOO = FPList.get(i).get("FPCOO")+"";
            //单位
            String FPGdsUnit = FPList.get(i).get("FPGdsUnit")+"";
            List<Map> FPCommodityList = (List<Map>)FPList.get(i).get("FPCommodityList");
            if( FPCommodityList != null && FPCommodityList.size() > 0){
                boolean ok = true;
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
                    List<Map> FPCommodity2List = (List<Map>)FPCommodityList.get(j).get("FPCommodity2List");
                    if(FPCommodity2List != null && FPCommodity2List.size() >0 ){
                        for (int k = 0; k < FPCommodity2List.size(); k++) {
                            String FPCommodity_Attach_BatchQty = FPCommodity2List.get(k).get("FPCommodity_Attach_BatchQty")+"";
                            String FPCommodity_Attach_SupplierNw = FPCommodity2List.get(k).get("FPCommodity_Attach_SupplierNw")+"";
                            String FPCommodity_Attach_SupplierGw = FPCommodity2List.get(k).get("FPCommodity_Attach_SupplierGw")+"";
                            String FPCommodity_Attach_SupplierAmount = FPCommodity2List.get(k).get("FPCommodity_Attach_SupplierAmount ")+"";
                            String FPCommodity_Attach_BatchNo = FPCommodity2List.get(k).get("FPCommodity_Attach_BatchNo")+"";
                            String FPCommodity_Attach_CtnNo = FPCommodity2List.get(k).get("FPCommodity_Attach_CtnNo")+"";
                            String FPCommodity_Attach_EngineNo = FPCommodity2List.get(k).get("FPCommodity_Attach_EngineNo")+"";
                            String FPCommodity_Attach_LotNo = FPCommodity2List.get(k).get("FPCommodity_Attach_LotNo")+"";
                            String FPCommodity_Attach_SupplierGoodQty = FPCommodity2List.get(k).get("FPCommodity_Attach_SupplierGoodQty")+"";
                            if(k == 0){
                                map.put("batch数量",FPCommodity_Attach_BatchQty);
                                map.put("分净重",FPCommodity_Attach_SupplierNw);
                                map.put("分毛重",FPCommodity_Attach_SupplierGw);
                                map.put("分金额",FPCommodity_Attach_SupplierAmount);
                                map.put("batch号",FPCommodity_Attach_BatchNo);
                                map.put("箱号",FPCommodity_Attach_CtnNo);
                                map.put("发动机号",FPCommodity_Attach_EngineNo);
                                map.put("Lot No",FPCommodity_Attach_LotNo);
                                map.put("良品数量",FPCommodity_Attach_SupplierGoodQty);
                                lists.add(map);
                            }else {
                                Map<String,Object> mapForMore = new HashMap<>();//封装detail
                                mapForMore.put("batch数量",FPCommodity_Attach_BatchQty);
                                mapForMore.put("分净重",FPCommodity_Attach_SupplierNw);
                                mapForMore.put("分毛重",FPCommodity_Attach_SupplierGw);
                                mapForMore.put("分金额",FPCommodity_Attach_SupplierAmount);
                                mapForMore.put("batch号",FPCommodity_Attach_BatchNo);
                                mapForMore.put("箱号",FPCommodity_Attach_CtnNo);
                                mapForMore.put("发动机号",FPCommodity_Attach_EngineNo);
                                mapForMore.put("Lot No",FPCommodity_Attach_LotNo);
                                mapForMore.put("良品数量",FPCommodity_Attach_SupplierGoodQty);
                                lists.add(mapForMore);
                            }
                        }
                    }else {
                        /**
                         * 数量匹配箱单
                         */
                        for (int k = 0; k < XDList.size(); k++) {
                            //原产国
                            String XDCOO = XDList.get(k).get("XDCOO")+"";
                            //数量单位
                            String XDGdsUnit1 = XDList.get(k).get("XDGdsUnit1")+"";
                            List<Map> XDCommodityList = (List<Map>)XDList.get(i).get("XDCommodityList");
                            for (int l = 0; l < XDCommodityList.size(); l++) {
                                if(map.get("料号").equals(XDCommodityList.get(l).get("XDCommodity_GdsQty1")) ){
                                    //箱单数据外层补充
                                    if(!util.checkNull(map.get("原产国"))){
                                        map.put("原产国",XDCommodityList.get(l).get("XDCommodity_COO"));
                                    }
                                    if(!util.checkNull(map.get("原产国"))){
                                        map.put("原产国",XDCOO);
                                    }
                                    if(!util.checkNull(map.get("单位"))){
                                        map.put("单位",XDCommodityList.get(l).get("XDCommodity_GdsUnit1"));
                                    }
                                    if(!util.checkNull(map.get("单位"))){
                                        map.put("单位",XDGdsUnit1);
                                    }
                                    if(!util.checkNull(map.get("PO号"))){
                                        map.put("PO号",XDCommodityList.get(l).get("XDCommodity_PO"));
                                    }
                                    if(!util.checkNull(map.get("入库发票号"))){
                                        map.put("入库发票号",XDCommodityList.get(l).get("XDCommodity_InXDNo"));
                                    }
                                    if(!util.checkNull(map.get("进库业务编号"))){
                                        map.put("进库业务编号",XDCommodityList.get(l).get("XDCommodity_InWorkNo"));
                                    }
                                    if(!util.checkNull(map.get("进库运单号"))){
                                        map.put("进库运单号",XDCommodityList.get(l).get("XDCommodity_InWayBill"));
                                    }
                                    if(!util.checkNull(map.get("最终用户料号"))){
                                        map.put("最终用户料号",XDCommodityList.get(l).get("XDCommodity_EndUserNo"));
                                    }
                                    if(!util.checkNull(map.get("订单号"))){
                                        map.put("订单号",XDCommodityList.get(l).get("XDCommodity_OrderNo"));
                                    }
                                    if(!util.checkNull(map.get("CPN"))){
                                        map.put("CPN",XDCommodityList.get(l).get("XDCommodity_CPN"));
                                    }
                                    if(!util.checkNull(map.get("最终用户"))){
                                        map.put("最终用户",XDCommodityList.get(l).get("XDCommodity_EndUser"));
                                    }
                                    if(!util.checkNull(map.get("SO号"))){
                                        map.put("SO号",XDCommodityList.get(l).get("XDCommodity_So"));
                                    }
                                    if(!util.checkNull(map.get("最小包装数量"))){
                                        map.put("最小包装数量",XDCommodityList.get(l).get("XDCommodity_MOQ"));
                                    }
                                    if(!util.checkNull(map.get("备注栏"))){
                                        map.put("备注栏",XDCommodityList.get(l).get("XDCommodity_ReMarks"));
                                    }
                                    if(!util.checkNull(map.get("唛头/件数/米数"))){
                                        map.put("入库发票号",XDCommodityList.get(l).get("XDCommodity_ReMarks"));
                                    }
                                    if(!util.checkNull(map.get("最终用户料号2"))){
                                        map.put("最终用户料号2",XDCommodityList.get(l).get("XDCommodity_EndUser2"));
                                    }
                                    if(!util.checkNull(map.get("最终用户料号3"))){
                                        map.put("最终用户料号3",XDCommodityList.get(l).get("XDCommodity_EndUser3"));
                                    }
                                    if(!util.checkNull(map.get("Shipment no"))){
                                        map.put("Shipment no",XDCommodityList.get(l).get("XDCommodity_ShipmentNo"));
                                    }
                                    if(!util.checkNull(map.get("SN号"))){
                                        map.put("SN号",XDCommodityList.get(l).get("XDCommodity_SN"));
                                    }
                                    if(!util.checkNull(map.get("部门"))){
                                        map.put("部门",XDCommodityList.get(l).get("XDCommodity_Dept"));
                                    }
                                    if(!util.checkNull(map.get("Delivery No"))){
                                        map.put("Delivery No",XDCommodityList.get(l).get("XDCommodity_DN"));
                                    }
                                    if(!util.checkNull(map.get("箱数"))){
                                        map.put("箱数",XDCommodityList.get(l).get("XDCommodity_GdsCtnQty"));
                                    }
                                    if(!util.checkNull(map.get("生产日期"))){
                                        map.put("生产日期",XDCommodityList.get(l).get("XDCommodity_InProductiondate"));
                                    }
                                    if(!util.checkNull(map.get("序列号"))){
                                        map.put("序列号",XDCommodityList.get(l).get("XDCommodity_BoxNumber"));
                                    }
                                    if(!util.checkNull(map.get("行号"))){
                                        map.put("行号",XDCommodityList.get(l).get("XDCommodity_ItemNo"));
                                    }

                                    if(!util.checkNull(map.get("净重"))){
                                        map.put("净重",XDCommodityList.get(l).get("XDCommodity_GdsNW"));
                                    }
                                    if(!util.checkNull(map.get("毛重"))){
                                        map.put("毛重",XDCommodityList.get(l).get("XDCommodity_GdsGW"));
                                    }
                                    List<Map> XDCommodity2List = (List<Map>) XDCommodityList.get(l).get("XDCommodity2List");
                                    if(XDCommodity2List != null && XDCommodity2List.size() > 0 ){
                                        for (int m = 0; m < XDCommodity2List.size(); m++) {
                                            String XDCommodity_Attach_BatchQty = XDCommodity2List.get(m).get("XDCommodity_Attach_BatchQty")+"";
                                            String XDCommodity_Attach_SupplierNw = XDCommodity2List.get(m).get("XDCommodity_Attach_SupplierNw")+"";
                                            String XDCommodity_Attach_SupplierGw = XDCommodity2List.get(m).get("XDCommodity_Attach_SupplierGw")+"";
                                            String XDCommodity_Attach_SupplierAmount = XDCommodity2List.get(m).get("XDCommodity_Attach_SupplierAmount ")+"";
                                            String XDCommodity_Attach_BatchNo = XDCommodity2List.get(m).get("XDCommodity_Attach_BatchNo")+"";
                                            String XDCommodity_Attach_CtnNo = XDCommodity2List.get(m).get("XDCommodity_Attach_CtnNo")+"";
                                            String XDCommodity_Attach_EngineNo = XDCommodity2List.get(m).get("XDCommodity_Attach_EngineNo")+"";
                                            String XDCommodity_Attach_LotNo = XDCommodity2List.get(m).get("XDCommodity_Attach_LotNo")+"";
                                            String XDCommodity_Attach_SupplierGoodQty = XDCommodity2List.get(m).get("XDCommodity_Attach_SupplierGoodQty")+"";
                                            if(m == 0){
                                                map.put("batch数量",XDCommodity_Attach_BatchQty);
                                                map.put("分净重",XDCommodity_Attach_SupplierNw);
                                                map.put("分毛重",XDCommodity_Attach_SupplierGw);
                                                map.put("分金额",XDCommodity_Attach_SupplierAmount);
                                                map.put("batch号",XDCommodity_Attach_BatchNo);
                                                map.put("箱号",XDCommodity_Attach_CtnNo);
                                                map.put("发动机号",XDCommodity_Attach_EngineNo);
                                                map.put("Lot No",XDCommodity_Attach_LotNo);
                                                map.put("良品数量",XDCommodity_Attach_SupplierGoodQty);
                                                lists.add(map);
                                            }else {
                                                Map<String,Object> mapForMore = new HashMap<>();//封装detail
                                                mapForMore.put("batch数量",XDCommodity_Attach_BatchQty);
                                                mapForMore.put("分净重",XDCommodity_Attach_SupplierNw);
                                                mapForMore.put("分毛重",XDCommodity_Attach_SupplierGw);
                                                mapForMore.put("分金额",XDCommodity_Attach_SupplierAmount);
                                                mapForMore.put("batch号",XDCommodity_Attach_BatchNo);
                                                mapForMore.put("箱号",XDCommodity_Attach_CtnNo);
                                                mapForMore.put("发动机号",XDCommodity_Attach_EngineNo);
                                                mapForMore.put("Lot No",XDCommodity_Attach_LotNo);
                                                mapForMore.put("良品数量",XDCommodity_Attach_SupplierGoodQty);
                                                lists.add(mapForMore);
                                            }
                                        }
                                    }else {
                                        lists.add(map);
                                    }
                                    ok = false;
                                    XDCommodityList.remove(l);
                                    l--;
                                    break;
                                }
                            }
                        }
                    }
                    if(ok){
                        lists.add(map);
                    }
                }
            }else {
                logger.warn("在仅有发票的前提下，发票没有二级");
            }
        }
        return lists;
    }

    /**
     * 通过料号和数量匹配
     * @param dataMap
     * @return
     */
    private List<Map<String,Object>> logicForDataByPartNumberAndGdsQty1(Map<String, List<Map>> dataMap){
        List<Map<String,Object>> lists = new ArrayList<>();//转换后的lists
        List<Map> FPList = dataMap.get("FPList");
        List<Map> list = getXDCommodity(dataMap);
        for (int i = 0; i < FPList.size(); i++) {
            //发票号
            String FPNo = FPList.get(i).get("FPNo")+"";
            //币制
            String FPCurrency = FPList.get(i).get("FPCurrency")+"";
            //原产国
            String FPCOO = FPList.get(i).get("FPCOO")+"";
            //单位
            String FPGdsUnit = FPList.get(i).get("FPGdsUnit")+"";
            List<Map> FPCommodityList = (List<Map>)FPList.get(i).get("FPCommodityList");
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
                    List<Map> FPCommodity2List = (List<Map>)FPCommodityList.get(j).get("FPCommodity2List");
                    if(FPCommodity2List != null && FPCommodity2List.size() >0 ){
                        for (int k = 0; k < FPCommodity2List.size(); k++) {
                            String FPCommodity_Attach_BatchQty = FPCommodity2List.get(k).get("FPCommodity_Attach_BatchQty")+"";
                            String FPCommodity_Attach_SupplierNw = FPCommodity2List.get(k).get("FPCommodity_Attach_SupplierNw")+"";
                            String FPCommodity_Attach_SupplierGw = FPCommodity2List.get(k).get("FPCommodity_Attach_SupplierGw")+"";
                            String FPCommodity_Attach_SupplierAmount = FPCommodity2List.get(k).get("FPCommodity_Attach_SupplierAmount ")+"";
                            String FPCommodity_Attach_BatchNo = FPCommodity2List.get(k).get("FPCommodity_Attach_BatchNo")+"";
                            String FPCommodity_Attach_CtnNo = FPCommodity2List.get(k).get("FPCommodity_Attach_CtnNo")+"";
                            String FPCommodity_Attach_EngineNo = FPCommodity2List.get(k).get("FPCommodity_Attach_EngineNo")+"";
                            String FPCommodity_Attach_LotNo = FPCommodity2List.get(k).get("FPCommodity_Attach_LotNo")+"";
                            String FPCommodity_Attach_SupplierGoodQty = FPCommodity2List.get(k).get("FPCommodity_Attach_SupplierGoodQty")+"";
                            if(k == 0){
                                map.put("batch数量",FPCommodity_Attach_BatchQty);
                                map.put("分净重",FPCommodity_Attach_SupplierNw);
                                map.put("分毛重",FPCommodity_Attach_SupplierGw);
                                map.put("分金额",FPCommodity_Attach_SupplierAmount);
                                map.put("batch号",FPCommodity_Attach_BatchNo);
                                map.put("箱号",FPCommodity_Attach_CtnNo);
                                map.put("发动机号",FPCommodity_Attach_EngineNo);
                                map.put("Lot No",FPCommodity_Attach_LotNo);
                                map.put("良品数量",FPCommodity_Attach_SupplierGoodQty);
                                lists.add(map);
                            }else {
                                Map<String,Object> mapForMore = new HashMap<>();//封装detail
                                mapForMore.put("batch数量",FPCommodity_Attach_BatchQty);
                                mapForMore.put("分净重",FPCommodity_Attach_SupplierNw);
                                mapForMore.put("分毛重",FPCommodity_Attach_SupplierGw);
                                mapForMore.put("分金额",FPCommodity_Attach_SupplierAmount);
                                mapForMore.put("batch号",FPCommodity_Attach_BatchNo);
                                mapForMore.put("箱号",FPCommodity_Attach_CtnNo);
                                mapForMore.put("发动机号",FPCommodity_Attach_EngineNo);
                                mapForMore.put("Lot No",FPCommodity_Attach_LotNo);
                                mapForMore.put("良品数量",FPCommodity_Attach_SupplierGoodQty);
                                lists.add(mapForMore);
                            }
                        }
                    }else {
                        /**
                         * 料号和数量匹配箱单
                         */
                        boolean has = true;
                        boolean three = true;
                        for (int k = 0; k < list.size(); k++) {
                            //1，料号一致  数量一致直接拼接放入
                            if(util.deleteBlank(map.get("料号")).equals(util.deleteBlank(list.get(k).get("XDCommodity_PartNumber"))) && new Double(map.get("数量")+"").equals(new Double(list.get(k).get("XDCommodity_GdsQty1")+""))){
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
                                if(!util.checkNull(map.get("行号"))){
                                    map.put("行号",list.get(k).get("XDCommodity_ItemNo"));
                                }


                                if(!util.checkNull(map.get("净重"))){
                                    map.put("净重",list.get(k).get("XDCommodity_GdsNW"));
                                }
                                if(!util.checkNull(map.get("毛重"))){
                                    map.put("毛重",list.get(k).get("XDCommodity_GdsGW"));
                                }
                                List<Map> XDCommodity2List = (List<Map>) list.get(k).get("XDCommodity2List");
                                if(XDCommodity2List != null && XDCommodity2List.size() > 0 ){
                                    for (int m = 0; m < XDCommodity2List.size(); m++) {
                                        String XDCommodity_Attach_BatchQty = XDCommodity2List.get(m).get("XDCommodity_Attach_BatchQty")+"";
                                        String XDCommodity_Attach_SupplierNw = XDCommodity2List.get(m).get("XDCommodity_Attach_SupplierNw")+"";
                                        String XDCommodity_Attach_SupplierGw = XDCommodity2List.get(m).get("XDCommodity_Attach_SupplierGw")+"";
                                        String XDCommodity_Attach_SupplierAmount = XDCommodity2List.get(m).get("XDCommodity_Attach_SupplierAmount ")+"";
                                        String XDCommodity_Attach_BatchNo = XDCommodity2List.get(m).get("XDCommodity_Attach_BatchNo")+"";
                                        String XDCommodity_Attach_CtnNo = XDCommodity2List.get(m).get("XDCommodity_Attach_CtnNo")+"";
                                        String XDCommodity_Attach_EngineNo = XDCommodity2List.get(m).get("XDCommodity_Attach_EngineNo")+"";
                                        String XDCommodity_Attach_LotNo = XDCommodity2List.get(m).get("XDCommodity_Attach_LotNo")+"";
                                        String XDCommodity_Attach_SupplierGoodQty = XDCommodity2List.get(m).get("XDCommodity_Attach_SupplierGoodQty")+"";
                                        if(m == 0){
                                            map.put("batch数量",XDCommodity_Attach_BatchQty);
                                            map.put("分净重",XDCommodity_Attach_SupplierNw);
                                            map.put("分毛重",XDCommodity_Attach_SupplierGw);
                                            map.put("分金额",XDCommodity_Attach_SupplierAmount);
                                            map.put("batch号",XDCommodity_Attach_BatchNo);
                                            map.put("箱号",XDCommodity_Attach_CtnNo);
                                            map.put("发动机号",XDCommodity_Attach_EngineNo);
                                            map.put("Lot No",XDCommodity_Attach_LotNo);
                                            map.put("良品数量",XDCommodity_Attach_SupplierGoodQty);
                                            lists.add(map);
                                        }else {
                                            Map<String,Object> mapForMore = new HashMap<>();//封装detail
                                            mapForMore.put("batch数量",XDCommodity_Attach_BatchQty);
                                            mapForMore.put("分净重",XDCommodity_Attach_SupplierNw);
                                            mapForMore.put("分毛重",XDCommodity_Attach_SupplierGw);
                                            mapForMore.put("分金额",XDCommodity_Attach_SupplierAmount);
                                            mapForMore.put("batch号",XDCommodity_Attach_BatchNo);
                                            mapForMore.put("箱号",XDCommodity_Attach_CtnNo);
                                            mapForMore.put("发动机号",XDCommodity_Attach_EngineNo);
                                            mapForMore.put("Lot No",XDCommodity_Attach_LotNo);
                                            mapForMore.put("良品数量",XDCommodity_Attach_SupplierGoodQty);
                                            lists.add(mapForMore);
                                        }
                                    }
                                }else {
                                    lists.add(map);
                                }
                                list.remove(k);
                                k--;
                                has = false;
                                three = false;
                                break;
                            }
                        }
                        boolean is = false;
                        List<Integer> li = new ArrayList();
                        for (int k = 0; k < list.size(); k++) {
                            //2，料号一致  数量不一致
                            if(util.deleteBlank(map.get("料号")).equals(util.deleteBlank(list.get(k).get("XDCommodity_PartNumber"))) && util.checkNull(map.get("数量")) && util.checkNull(list.get(k).get("XDCommodity_GdsQty1"))  &&  new Double(map.get("数量")+"") > new Double(list.get(k).get("XDCommodity_GdsQty1")+"")){
                                is = true;
                                li.add(k);
                            }
                        }
                        if(is && has){
                            List<Double> count = new ArrayList<>();
                            Comparator<Map> comparator = new Comparator<Map>() {
                                @Override
                                public int compare(Map o1, Map o2) {
                                    if(util.checkNull(o1.get("XDCommodity_GdsQty1")) &&  util.checkNull(o2.get("XDCommodity_GdsQty1"))){
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
                                if(util.checkNull(list.get(li.get(k)).get("XDCommodity_GdsQty1")+"")){
                                    Double a = new Double(list.get(li.get(k)).get("XDCommodity_GdsQty1")+"");
                                    count.add(a);
                                }
                            }
                            if(util.checkNull(FPCommodityList.get(j).get("FPCommodity_GdsQty1"))){
                                Double c = new Double(FPCommodityList.get(j).get("FPCommodity_GdsQty1")+"");
                                CalcUtil CalcUtil = new CalcUtil();
                                List<List<Double>> cc = CalcUtil.getSubset(count,c,0);
                                if(cc != null && cc.size() >0){
                                    for (int k = 0; k < cc.get(0).size(); k++) {
                                        logger.error("==========="+cc.get(0).get(k));
                                    }
                                }else {
                                    lists.add(map);
                                }

                            }
                            three = false;
                        }
                        if(three){
                            lists.add(map);
                        }

                    }
                }
            }else {
                logger.warn("发票没有二级");
            }
        }
        return lists;
    }

    /**
     * 通用箱单整理
     * @param dataMap
     * @return
     */
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
}
