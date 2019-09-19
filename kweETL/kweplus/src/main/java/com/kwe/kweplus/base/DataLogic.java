package com.kwe.kweplus.base;


import com.kwe.kweplus.util.CalcUtil;
import com.kwe.kweplus.util.ocrUtil.OcrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class DataLogic {

    private Logger logger = LoggerFactory.getLogger(DataLogic.class);

    OcrUtil util = new OcrUtil();
    /**
     * 格式化数据
     *      确认发票箱单页数
     * @param dataMap
     * @param ywNo
     * @return
     */
    public List<Map<String,Object>> dataFormat(Map<String, List<Map>> dataMap,String ywNo,Map<String,Boolean> fields){
        List<Map<String,Object>> lists = new ArrayList<>();//转换后的lists
        try {
            List<Map> FPlist =  dataMap.get("FPList");
            List<Map> XDList =  dataMap.get("XDList");
            boolean hasFP = false;
            boolean hasManyFP = false;
            boolean hasXD = false;
            boolean hasManyXD = false;
            if( FPlist != null &&  FPlist.size() > 0  ){
                hasFP = true;
                if(FPlist.size() > 1){
                    hasManyFP = true;
                }
            }else {
                logger.info("编号："+ywNo+",业务没有发票!");
            }
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
                lists = onlyHasInvoice(dataMap.get("FPList"),fields);
                logger.info("编号："+ywNo+",只有发票,展示发票数据!");
            }else if(hasFP && hasXD){
                if(!hasManyFP && !hasManyXD){
                    lists = oneInvoiceAndOnePackingList(dataMap,ywNo,fields);
                    logger.info("编号："+ywNo+",发票，箱单一张对一张");
                }else if(hasManyFP && hasManyXD){
                    lists = manyInvoiceAndManyPackingList(dataMap,ywNo,fields);
                    logger.info("编号："+ywNo+",发票箱单多对多，通过发票号匹配");
                }
            }
            return lists;
        }catch (Exception e){
            e.printStackTrace();
            return lists;
        }
    }

    /**
     * 只有一张发票，一张箱单
     * @param dataMap
     * @param ywNo
     * @return
     */
    private List<Map<String,Object>> oneInvoiceAndOnePackingList(Map<String, List<Map>> dataMap,String ywNo,Map<String,Boolean> fields){
        List<Map<String,Object>> lists = new ArrayList<>();//转换后的lists
        try {
            lists = logicForData(dataMap.get("FPList"),dataMap.get("XDList"),ywNo,fields);
            return lists;
        }catch (Exception e){
            return lists;
        }
    }

    /**
     * 多张发票多张箱单
     * @param dataMap
     * @param ywNo
     * @return
     */
    private List<Map<String,Object>> manyInvoiceAndManyPackingList(Map<String, List<Map>> dataMap,String ywNo,Map<String,Boolean> fields){
        List<Map<String,Object>> lists = new ArrayList<>();//转换后的lists
        try {
            List<Map> FPList = dataMap.get("FPList");
            List<Map> XDList = dataMap.get("XDList");
            boolean isItOk = true;
            for (int i = 0; i < FPList.size(); i++) {
                String FPNo = FPList.get(i).get("FPNo")+"";
                if(util.checkNull(FPNo)){
                    for (int j = 0; j < XDList.size(); j++) {
                        //发票号
                        String XDInviceno = XDList.get(j).get("XDInviceno")+"";
                        if(util.checkNull(XDInviceno)){
                            if(XDInviceno.equals(FPNo)){
                                List<Map> Fplist = new ArrayList<>();
                                Fplist.add(FPList.get(i));
                                List<Map> Xdlist = new ArrayList<>();
                                Xdlist.add(XDList.get(j));
                                List<Map<String,Object>> add = logicForData(Fplist,Xdlist,ywNo,fields);
                                for (int k = 0; k < add.size(); k++) {
                                    lists.add(add.get(k));
                                }
                                isItOk = false;
                                XDList.remove(j);
                                j--;
                                FPList.remove(i);
                                i--;
                                break;
                            }
                        } else {
                            logger.warn("多张发票和多张箱单时，第"+(j+1)+"张***箱单***没有发票号，业务编号"+ywNo);
                        }
                    }
                } else {
                    logger.warn("多张发票和多张箱单时，第"+(i+1)+"张***发票***没有发票号，业务编号"+ywNo);
                }
                if(isItOk){
                   //只添加发票
                    List<Map<String,Object>> add = onlyHasInvoice(FPList,fields);
                    for (int k = 0; k < add.size(); k++) {
                        lists.add(add.get(k));
                    }
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
    private List<Map<String,Object>> logicForData(List<Map> FPlist,List<Map> XDList,String ywNo,Map<String,Boolean> fields){
        List<Map<String,Object>> lists = new ArrayList<>();//转换后的lists

        try {
            boolean FPhasPartNumber = true;
            boolean FPhasGdsQty1 = true;
            boolean XDhasPartNumber = true;
            boolean XDhasGdsQty1 = true;

            if(FPlist != null && FPlist.size() > 0 ){
                for (int i = 0; i < FPlist.size(); i++) {
                    List<Map> FPCommodityList = (List<Map>)FPlist.get(i).get("FPCommodityList");
                    if(FPCommodityList != null && FPCommodityList.size() >0 ){
                        for (int j = 0; j < FPCommodityList.size(); j++) {
                            //商品料号
                            String FPCommodity_PartNumber = FPCommodityList.get(j).get("FPCommodity_PartNumber")+"";
                            //料号数量
                            String FPCommodity_GdsQty1 = FPCommodityList.get(j).get("FPCommodity_GdsQty1")+"";
                            if(!util.checkNull(FPCommodity_PartNumber)){
                                FPhasPartNumber = false;
                            }
                            if(!util.checkNull(FPCommodity_GdsQty1)){
                                FPhasGdsQty1 = false;
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
                            if(!util.checkNull(XDCommodity_PartNumber)){
                                XDhasPartNumber= false;
                            }
                            if(!util.checkNull(XDCommodity_GdsQty1)){
                                XDhasGdsQty1 = false;
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
                byGdsQty1 = true;
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
                //直接展示发票
                lists = onlyHasInvoice(FPlist,fields);
            }else if(byPartNumber && !byGdsQty1 ){
                //通过料号匹配
                lists = logicForDataByPartNumber(FPlist,XDList,fields);
            }else if(!byPartNumber && byGdsQty1 ){
                //通过数量匹配
                lists = logicForDataByGdsQty1(FPlist,XDList,fields);
            }else if(byPartNumber && byGdsQty1){
                //通过料号数量匹配
                lists = logicForDataByPartNumberAndGdsQty1(FPlist,XDList,fields);
            }
            return lists;
        }catch (Exception e){
            e.printStackTrace();
            return lists;
        }
    }

    /**
     * 只有发票
     * @param FPlist
     * @return
     */
    private List<Map<String,Object>> onlyHasInvoice(List<Map> FPlist,Map<String,Boolean> fields){
        List<Map<String,Object>> lists = new ArrayList<>();//转换后的lists
        try {
            if( FPlist != null &&  FPlist.size() > 0  ){
                for (int i = 0; i < FPlist.size(); i++) {
                    //发票号
                    String FPNo = FPlist.get(i).get("FPNo")+"";
                    //币制
                    String FPCurrency = FPlist.get(i).get("FPCurrency")+"";
                    //原产国
                    String FPCOO = FPlist.get(i).get("FPCOO")+"";
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
                        logger.warn("在仅有发票的前提下，发票没有二级");
                    }
                }
            }
            return lists;
        }catch (Exception e){
            e.printStackTrace();
            return lists;
        }
    }


    /**
     * 通过料号匹配
     * @param FPlist
     * @param XDList
     * @return
     */
    private List<Map<String,Object>> logicForDataByPartNumber(List<Map> FPlist,List<Map> XDList,Map<String,Boolean> fields){
        List<Map<String,Object>> lists = new ArrayList<>();//转换后的lists
        List<Integer> index = new ArrayList<>();//转换后的lists
        List<String> li = new ArrayList();
        for (int i = 0; i < FPlist.size(); i++) {
            //发票号
            String FPNo = FPlist.get(i).get("FPNo")+"";
            //币制
            String FPCurrency = FPlist.get(i).get("FPCurrency")+"";
            //原产国
            String FPCOO = FPlist.get(i).get("FPCOO")+"";
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
                            List<Map> XDCommodityList = (List<Map>)XDList.get(i).get("XDCommodityList");
                            boolean ok = true;
                            for (int l = 0; l < XDCommodityList.size(); l++) {
                                if(util.deleteBlank(map.get("料号")).equals(util.deleteBlank(XDCommodityList.get(l).get("XDCommodity_PartNumber"))) ){
                                    logger.warn("通过料号匹配成功");
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
                                    //直接料号数量匹配 OK
                                    if (util.checkNull(XDCommodityList.get(l).get("XDCommodity_GdsNW"))) {
                                          if(fields.get("分净重")){
                                              map.put("分净重",XDCommodityList.get(l).get("XDCommodity_GdsNW"));
                                            } else {
                                                map.put("净重",XDCommodityList.get(l).get("XDCommodity_GdsNW"));
                                            }
                                    }

                                    if (util.checkNull(XDCommodityList.get(l).get("XDCommodity_GdsGW"))) {
                                        if(fields.get("分毛重")){
                                            map.put("分毛重",XDCommodityList.get(l).get("XDCommodity_GdsGW"));
                                        } else {
                                            map.put("净重",XDCommodityList.get(l).get("XDCommodity_GdsGW"));
                                        }
                                    }


                                    if(fields.get("batch数量")){
                                        map.put("batch数量",XDCommodityList.get(l).get("XDCommodity_GdsQty1"));
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
                                                if(util.checkNull(XDCommodity_Attach_BatchQty)){
                                                    map.put("batch数量",XDCommodity_Attach_BatchQty);
                                                }
                                                if(util.checkNull(XDCommodity_Attach_SupplierNw)){
                                                    map.put("分净重",XDCommodity_Attach_SupplierNw);
                                                }
                                                if(util.checkNull(XDCommodity_Attach_SupplierGw)){
                                                    map.put("分毛重",XDCommodity_Attach_SupplierGw);
                                                }
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
                            if(ok){
                                //循环一遍料号无法匹配直接添加
                                logger.warn("通过料号匹配失败，直接添加");
                                index.add(j);
                                li.add(XDCommodityList.get(0).get("XDCommodity_PartNumber")+"");
                                lists.add(map);

                            }
                        }
                    }
                }
            }else {
                logger.warn("在仅有发票的前提下，发票没有二级");
            }
        }
        List listmap = fuzzyMatchingByLists(lists,index,li);
        lists = getCompleteData(lists,listmap,XDList);

        return lists;
    }


    /**
     * 通过数量匹配
     * @param FPlist
     * @param XDList
     * @return
     */
    private List<Map<String,Object>> logicForDataByGdsQty1(List<Map> FPlist,List<Map> XDList,Map<String,Boolean> fields){
    List<Map<String,Object>> lists = new ArrayList<>();//转换后的lists
    for (int i = 0; i < FPlist.size(); i++) {
        //发票号
        String FPNo = FPlist.get(i).get("FPNo")+"";
        //币制
        String FPCurrency = FPlist.get(i).get("FPCurrency")+"";
        //原产国
        String FPCOO = FPlist.get(i).get("FPCOO")+"";
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
                        List<Map> XDCommodityList = (List<Map>)XDList.get(i).get("XDCommodityList");
                        boolean ok = true;
                        for (int l = 0; l < XDCommodityList.size(); l++) {
                            if(map.get("数量").equals(XDCommodityList.get(l).get("XDCommodity_GdsQty1")+"") ){
                                logger.warn("通过数量匹配成功");
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
                                //直接料号数量匹配 OK
                                if(fields.get("分净重")){
                                    map.put("分净重",XDCommodityList.get(l).get("XDCommodity_GdsNW"));
                                } else {
                                    map.put("净重",XDCommodityList.get(l).get("XDCommodity_GdsNW"));
                                }

                                if(fields.get("分毛重")){
                                    map.put("分毛重",XDCommodityList.get(l).get("XDCommodity_GdsGW"));
                                } else {
                                    map.put("毛重",XDCommodityList.get(l).get("XDCommodity_GdsGW"));
                                }

                                if(fields.get("batch数量")){
                                    map.put("batch数量",XDCommodityList.get(l).get("XDCommodity_GdsQty1"));
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
                                            if(util.checkNull(XDCommodity_Attach_BatchQty)){
                                                map.put("batch数量",XDCommodity_Attach_BatchQty);
                                            }
                                            if(util.checkNull(XDCommodity_Attach_SupplierNw)){
                                                map.put("分净重",XDCommodity_Attach_SupplierNw);
                                            }
                                            if(util.checkNull(XDCommodity_Attach_SupplierGw)){
                                                map.put("分毛重",XDCommodity_Attach_SupplierGw);
                                            }
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
                                FPCommodityList.remove(j);
                                j--;
                                break;
                            }
                        }
                    }
                }
            }
            for (int j = 0; j < FPCommodityList.size(); j++) {
                System.out.println(FPCommodityList.get(j));
            }
        }else {
            logger.warn("在仅有发票的前提下，发票没有二级");
        }
    }
    return lists;
}

    /**
     * 通过料号和数量匹配
     * @param FPlist
     * @param XDList
     * @return
     */
    private List<Map<String,Object>> logicForDataByPartNumberAndGdsQty1(List<Map> FPlist,List<Map> XDList,Map<String,Boolean> fields){
        List<Map<String,Object>> lists = new ArrayList<>();//转换后的lists
        List<Map> list = getXDCommodity(XDList);
        for (int i = 0; i < FPlist.size(); i++) {
            //发票号
            String FPNo = FPlist.get(i).get("FPNo")+"";
            //币制
            String FPCurrency = FPlist.get(i).get("FPCurrency")+"";
            //原产国
            String FPCOO = FPlist.get(i).get("FPCOO")+"";
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
                                    //直接料号数量匹配 OK
                                    if(fields.get("分净重")){
                                        map.put("分净重",list.get(k).get("XDCommodity_GdsNW"));
                                    }else {
                                        map.put("净重",list.get(k).get("XDCommodity_GdsNW"));
                                    }

                                    if(fields.get("分毛重")){
                                        map.put("分毛重",list.get(k).get("XDCommodity_GdsGW"));
                                    }else {
                                        map.put("毛重",list.get(k).get("XDCommodity_GdsGW"));
                                    }

                                    if(fields.get("batch数量")){
                                        map.put("batch数量",list.get(k).get("XDCommodity_GdsQty1"));
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
                                                if(util.checkNull(XDCommodity_Attach_BatchQty)){
                                                    map.put("batch数量",XDCommodity_Attach_BatchQty);
                                                }
                                                if(util.checkNull(XDCommodity_Attach_SupplierNw)){
                                                    map.put("分净重",XDCommodity_Attach_SupplierNw);
                                                }
                                                if(util.checkNull(XDCommodity_Attach_SupplierGw)){
                                                    map.put("分毛重",XDCommodity_Attach_SupplierGw);
                                                }
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
                                        Map min = list.get(li.get(count.indexOf(cc.get(0).get(k))));
                                        if(k == 0){
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

                                            if(fields.get("分净重")){
                                                map.put("分净重",list.get(k).get("XDCommodity_GdsNW"));
                                            }else {
                                                map.put("净重",list.get(k).get("XDCommodity_GdsNW"));
                                            }

                                            if(fields.get("分毛重")){
                                                map.put("分毛重",list.get(k).get("XDCommodity_GdsGW"));
                                            }else {
                                                map.put("毛重",list.get(k).get("XDCommodity_GdsGW"));
                                            }

                                            if(fields.get("batch数量")){
                                                map.put("batch数量",list.get(k).get("XDCommodity_GdsQty1"));
                                            }

                                            List<Map> XDCommodity2List = (List<Map>) min.get("XDCommodity2List");
                                            if(XDCommodity2List != null && XDCommodity2List.size() > 0 ){
                                                map.put("箱号",XDCommodity2List.get(0).get("XDCommodity_Attach_CtnNo"));
                                                map.put("batch号",XDCommodity2List.get(0).get("XDCommodity_Attach_BatchNo"));
                                            }
                                            lists.add(map);
                                            list.remove(li.get(count.indexOf(cc.get(0).get(k))));
                                        }else {
                                            Map<String,Object> mapForMore = new HashMap<>();//封装detail
                                            if(fields.get("分净重") && fields.get("分毛重")){
                                                mapForMore.put("分净重",min.get("XDCommodity_GdsNW"));
                                                mapForMore.put("分毛重",min.get("XDCommodity_GdsGW"));
                                                mapForMore.put("batch数量",min.get("XDCommodity_GdsQty1"));
                                            }else {
                                                mapForMore.put("净重",list.get(k).get("XDCommodity_GdsNW"));
                                                mapForMore.put("毛重",list.get(k).get("XDCommodity_GdsGW"));
                                            }
                                            List<Map> XDCommodity2List = (List<Map>) min.get("XDCommodity2List");
                                            if(XDCommodity2List != null && XDCommodity2List.size() > 0 ){
                                                map.put("箱号",XDCommodity2List.get(0).get("XDCommodity_Attach_CtnNo"));
                                                map.put("batch号",XDCommodity2List.get(0).get("XDCommodity_Attach_BatchNo"));
                                            }
                                            lists.add(mapForMore);
                                            list.remove(li.get(count.indexOf(cc.get(0).get(k))));
                                        }
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
                logger.warn("在仅有发票的前提下，发票没有二级");
            }
        }
        return lists;
    }


    /**
     *  针对类似于瑞萨的单证，通过行号匹配
     * @param dataMap
     * @param ywNo
     * @return
     */
   public List<Map<String,Object>> logicForDataByItemNo(Map<String, List<Map>> dataMap,String ywNo){
       List<Map> list = getXDCommodityFor47(dataMap);
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
                           if("000".equals(FPCommodity_PartNumber.substring(0,3))){
                               map.put("料号",FPCommodity_PartNumber.substring(3,FPCommodity_PartNumber.length()));
                           }else {
                               map.put("料号",FPCommodity_PartNumber);
                           }
                           //料号数量
                           String FPCommodity_GdsQty1 = FPCommodityList.get(j).get("FPCommodity_GdsQty1")+"";
                           if(".0".equals(FPCommodity_GdsQty1.substring(FPCommodity_GdsQty1.indexOf("."),FPCommodity_GdsQty1.length()))){
                               map.put("数量",FPCommodity_GdsQty1.substring(0,FPCommodity_GdsQty1.indexOf(".")));
                           }else {
                               map.put("数量",FPCommodity_GdsQty1);
                           }
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
                           boolean ok = false;
                           if(list != null && list.size() >0 ){
                               boolean first = true;
                               for (int k = 0; k < list.size(); k++) {
                                   if(j+1 == new Double(list.get(k).get("XDCommodity_ItemNo")+"")){
                                       if(first){
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
                                           //直接料号数量匹配 OK
                                           map.put("分净重",list.get(k).get("XDCommodity_GdsNW"));
                                           map.put("分毛重",list.get(k).get("XDCommodity_GdsGW"));
                                           map.put("行号",list.get(k).get("XDCommodity_ItemNo"));
                                           map.put("batch数量",list.get(k).get("XDCommodity_GdsQty1"));
                                           List<Map> XDCommodity2List = (List<Map>)list.get(k).get("XDCommodity2List");
                                           map.put("箱号",XDCommodity2List.get(0).get("XDCommodity_Attach_CtnNo"));
                                           lists.add(map);
                                           list.remove(k);
                                           k--;
                                           first = false;
                                       }else {
                                           Map<String,Object> mapForMore = new HashMap<>();//封装detail
                                           mapForMore.put("行号",list.get(k).get("XDCommodity_ItemNo"));
                                           mapForMore.put("分净重",list.get(k).get("XDCommodity_GdsNW"));
                                           mapForMore.put("分毛重",list.get(k).get("XDCommodity_GdsGW"));
                                           mapForMore.put("batch数量",list.get(k).get("XDCommodity_GdsQty1"));
                                           List<Map> XDCommodity2List = (List<Map>)list.get(k).get("XDCommodity2List");
                                           mapForMore.put("箱号",XDCommodity2List.get(0).get("XDCommodity_Attach_CtnNo"));
                                           lists.add(mapForMore);
                                           list.remove(k);
                                           k--;
                                           first = false;
                                       }
                                       ok = true;
                                   }
                               }
                               if(!ok){
                                   lists.add(map);
                               }
                           } else {
                               lists.add(map);
                               logger.warn("瑞萨特殊处理（47号模板），***箱单***是空的，直接展示发票"+ywNo);
                           }
                       }
                   }else {
                       logger.warn("瑞萨特殊处理（47号模板），***发票二级***是空的"+ywNo);
                   }
               }
           }else {
               logger.warn("瑞萨特殊处理（47号模板），***发票***是空的"+ywNo);
           }
           return lists;
       }catch (Exception e){
           e.printStackTrace();
           return lists;
       }
   }







    public void getNewList(List<Map<String,Object>> lists,Integer key ,List<Map> XDlist,Map<Integer, Integer> map,String chineseDescribe,String XdDescribe){
        if(!util.checkNull(lists.get(key).get(chineseDescribe))){
            lists.get(key).put(chineseDescribe,XDlist.get(map.get(key)).get(XdDescribe));
        }
    }

    public void getNewTwoList(List<Map<String,Object>> lists,Integer key ,List<Map> XDlist,Map<Integer, Integer> map,String chineseDescribe,String XdDescribe){
        if(!util.checkNull(lists.get(key).get(chineseDescribe))){
           List<Map<String,Object>> list  = (List<Map<String,Object>>) XDlist.get(map.get(key)).get("XDCommodityList");
            for (Map<String,Object> Map3:list) {
                lists.get(key).put(chineseDescribe,Map3.get(XdDescribe));
            }

        }
    }

    public void getNewThreeList(List<Map<String,Object>> lists,Integer key ,List<Map> XDlist,Map<Integer, Integer> map,String chineseDescribe,String XdDescribe){
        if(!util.checkNull(lists.get(key).get(chineseDescribe))){
            List<Map<String,Object>> list  = (List<Map<String,Object>>) XDlist.get(map.get(key)).get("XDCommodityList");
            for (Map<String,Object> Map3:list) {
                List <Map<String,Object>> list2 =(List<Map<String,Object>>) Map3.get("XDCommodity2List");
                for (Map<String,Object> Map4:list2){
                    lists.get(key).put(chineseDescribe,Map4.get(XdDescribe));
                }
            }

        }
    }


    public List getCompleteData(List<Map<String,Object>> lists,List<Map<Integer,Integer>> listmap,List<Map> XDlist ){
        for( Map<Integer, Integer> map:listmap){
            for (Integer key : map.keySet()) {
                getNewList(lists,key,XDlist,map,"原产国","XDCOO");
                getNewList(lists,key,XDlist,map,"运输方式","XDModeOfTransport");
                getNewList(lists,key,XDlist,map,"成交方式","XDTermType");
                getNewList(lists,key,XDlist,map,"启运国/运抵国","XDFrom");
                getNewList(lists,key,XDlist,map,"经停港/指运港","XDFrom");
                getNewList(lists,key,XDlist,map,"目的国","XDTo");
                getNewList(lists,key,XDlist,map,"总件数","XDCtnQty");
                getNewList(lists,key,XDlist,map,"总毛重","XDTotalGW");
                getNewList(lists,key,XDlist,map,"总净重","XDTotalNW");
                getNewList(lists,key,XDlist,map,"供应商","XDSupName");

                getNewTwoList(lists,key,XDlist,map,"Shipment No","XDCommodity_ShipmentNo");
                getNewTwoList(lists,key,XDlist,map,"料号毛重","XDCommodity_GdsGW");
                getNewTwoList(lists,key,XDlist,map,"料号净重","XDCommodity_GdsNW");
                getNewTwoList(lists,key,XDlist,map,"税号","XDCommodity_HSCode");
                getNewTwoList(lists,key,XDlist,map,"品名","XDCommodity_Desc");
                getNewTwoList(lists,key,XDlist,map,"数量单位","XDCommodity_GdsUnit1");
                getNewTwoList(lists,key,XDlist,map,"料号体积","XDCommodity_GdsMeasurement");
                getNewTwoList(lists,key,XDlist,map,"PO号","XDCommodity_PO");
                getNewTwoList(lists,key,XDlist,map,"料号行号","XDCommodity_ItemNo");
                getNewTwoList(lists,key,XDlist,map,"入库发票号","XDCommodity_InXDNo");
                getNewTwoList(lists,key,XDlist,map,"进库业务编号","XDCommodity_InWorkNo");
                getNewTwoList(lists,key,XDlist,map,"进库运单号","XDCommodity_InWayBill");
                getNewTwoList(lists,key,XDlist,map,"最终用户料号","XDCommodity_EndUserNo");
                getNewTwoList(lists,key,XDlist,map,"订单号","XDCommodity_OrderNo");
                getNewTwoList(lists,key,XDlist,map,"CPN","XDCommodity_CPN");
                getNewTwoList(lists,key,XDlist,map,"最终用户","XDCommodity_EndUser");
                getNewTwoList(lists,key,XDlist,map,"SO号","XDCommodity_So");
                getNewTwoList(lists,key,XDlist,map,"最小包装数量","XDCommodity_MOQ");
                getNewTwoList(lists,key,XDlist,map,"备注栏(唛头/件数/米数)","XDCommodity_ReMarks");
                getNewTwoList(lists,key,XDlist,map,"最终用户料号2","XDCommodity_EndUser2");
                getNewTwoList(lists,key,XDlist,map,"最终用户料号3","XDCommodity_EndUser3");
                getNewTwoList(lists,key,XDlist,map,"SN号","XDCommodity_SN");
                getNewTwoList(lists,key,XDlist,map,"部门","XDCommodity_Dept");
                getNewTwoList(lists,key,XDlist,map,"Delivery No","XDCommodity_DN");
                getNewTwoList(lists,key,XDlist,map,"箱数","XDCommodity_GdsCtnQty");
                getNewTwoList(lists,key,XDlist,map,"生产日期","XDCommodity_InProductiondate");
                getNewTwoList(lists,key,XDlist,map,"入库分单号","XDCommodity_InWayBill");
                getNewTwoList(lists,key,XDlist,map,"序列号","XDCommodity_BoxNumber");


                getNewThreeList(lists,key,XDlist,map,"batch数量","XDCommodity_Attach_BatchQty");
                getNewThreeList(lists,key,XDlist,map,"分净重","XDCommodity_Attach_SupplierNw");
                getNewThreeList(lists,key,XDlist,map,"分毛重","XDCommodity_Attach_SupplierGw");
                getNewThreeList(lists,key,XDlist,map,"分金额","XDCommodity_Attach_SupplierAmount");
                getNewThreeList(lists,key,XDlist,map,"毛重","XDCommodity_Attach_SupplierGw");
                getNewThreeList(lists,key,XDlist,map,"batch号","XDCommodity_Attach_BatchNo");
                getNewThreeList(lists,key,XDlist,map,"箱号","XDCommodity_Attach_CtnNo");
                getNewThreeList(lists,key,XDlist,map,"发动机号","XDCommodity_Attach_EngineNo");
                getNewThreeList(lists,key,XDlist,map,"Lot No","XDCommodity_Attach_LotNo");
                getNewThreeList(lists,key,XDlist,map,"良品数量","XDCommodity_Attach_SupplierGoodQty");

            }
        }

        return lists;
    }


    /**
     * 模糊匹配料号
     *
     * @param lists
     * @param index
     * @param XDList
     * @return
     */
    public List fuzzyMatchingByLists(List<Map<String,Object>> lists,List<Integer> index,List XDList){
        List list = new ArrayList();
        Map<Integer,Integer> maps = new HashMap();
        List<Map<Integer, Integer>> listmap = new ArrayList<>();
        for (Integer indexs : index){
            list.add(lists.get(indexs).get("料号"));
            Map<String, Integer> map = OcrUtil.CountMax(list,XDList);
            maps.put(index.get(indexs),map.get(lists.get(indexs).get("料号")));
            listmap.add(maps);
        }
        return listmap;
    }


    /**
     *  通用版箱单整理
     * @param XDList
     * @return
     */
    private List<Map>  getXDCommodity(List<Map> XDList){
        /**
         * 箱单所有数据组装到集合中
         */
        List<Map> lists = new ArrayList<>();
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
                if(util.checkNull(o1.get("XDCommodity_ItemNo")) &&  util.checkNull(o2.get("XDCommodity_ItemNo"))){
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
                }
            }
        };
        Collections.sort(lists,comparator);
        return lists;
    }

    /**
     *  瑞萨特殊箱单整理
      * @param dataMap
     * @return
     */
    private List<Map>  getXDCommodityFor47(Map<String, List<Map>> dataMap){//,String FPCommodity_PartNumber,String FPCommodity_GdsQty1
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
                        XDCommodityList.get(j).put("",XDCOO);
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
                }
            }
        };
        Collections.sort(lists,comparator);
        return lists;
    }
}
