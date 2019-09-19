package com.kwe.kweplus.base;


import com.kwe.kweplus.util.ocrUtil.OcrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 根据客户或则识别模板定制化开发
 */
public class SpecialHanding {

    private Logger logger = LoggerFactory.getLogger(SpecialHanding.class);

    OcrUtil util = new OcrUtil();

    public List<Map<String,Object>> dataFormatFor47(Map<String, List<Map>> dataMap,String ywNo){
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
                            boolean isOk = false;
                            if(list != null && list.size() >0 ){
                                boolean first = true;
                                for (int k = 0; k < list.size(); k++) {
                                    if(j+1 == new Double(list.get(k).get("XDCommodity_ItemNo")+"")){
                                        //箱单数据外层补充
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
//                                            List<Map> XDCommodity2List = (List<Map>)list.get(k).get("XDCommodity2List");
//                                            map.put("箱号",XDCommodity2List.get(0).get("XDCommodity_Attach_CtnNo"));
                                            map.put("箱号",list.get(k).get("XDCommodity_ContainerNo"));
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
//                                            List<Map> XDCommodity2List = (List<Map>)list.get(k).get("XDCommodity2List");
//                                            mapForMore.put("箱号",XDCommodity2List.get(0).get("XDCommodity_Attach_CtnNo"));
                                            mapForMore.put("箱号",list.get(k).get("XDCommodity_ContainerNo"));
                                            lists.add(mapForMore);
                                            list.remove(k);
                                            k--;
                                        }
                                        isOk = true;
                                    }
                                }
                            }else {
                                isOk = true;
                                lists.add(map);
                            }
                            if(! isOk){
                                lists.add(map);
                            }
                        }
                    }else {
                       logger.error("发票无二级不予以匹配"+ywNo);
                    }
                }
            }
            return lists;
        }catch (Exception e){
            e.printStackTrace();
            return lists;
        }
    }
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

                        String XDCommodity_ContainerNo = XDCommodityList.get(j).get("XDCommodity_ContainerNo")+"";
                        String XDCommodity_GdsGW = XDCommodityList.get(j).get("XDCommodity_GdsGW")+"";
                        if(XDCommodity_GdsGW.contains("@")){
                            XDCommodity_GdsGW = XDCommodity_GdsGW.replace("@","");
                        }
                        String XDCommodity_GdsNW = XDCommodityList.get(j).get("XDCommodity_GdsNW")+"";
                        if(XDCommodity_GdsNW.contains("@")){
                            XDCommodity_GdsNW = XDCommodity_GdsNW.replace("@","");
                        }
                        List<Map> XDCommodity2List = (List<Map>)XDCommodityList.get(j).get("XDCommodity2List");
                        if(XDCommodity2List != null  && XDCommodity2List.size() >0 ){
                            for (int k = 0; k < XDCommodity2List.size(); k++) {
                                //行号
                                String XDCommodity_ItemNo = XDCommodity2List.get(k).get("XDCommodity_Attach_BatchNo")+"";
                                //数量
                                String XDCommodity_GdsQty1 = XDCommodity2List.get(k).get("XDCommodity_Attach_SupplierTotalQty")+"";
                                if(XDCommodity_GdsQty1.contains(",")){
                                    XDCommodity_GdsQty1 = XDCommodity_GdsQty1.replace(",","");
                                }
                                if(XDCommodity_GdsQty1.contains("@")){
                                    XDCommodity_GdsQty1 = XDCommodity_GdsQty1.replace("@","");
                                }
                                //料号
                                String XDCommodity_PartNumber = XDCommodity2List.get(k).get("XDCommodity_Attach_SupplierNumber")+"";
                                Map map = new HashMap();
                                map.put("XDCommodity_ItemNo",XDCommodity_ItemNo);
                                map.put("XDCommodity_GdsQty1",XDCommodity_GdsQty1);
                                map.put("XDCommodity_PartNumber",XDCommodity_PartNumber);
                                map.put("XDCommodity_GdsGW",XDCommodity_GdsGW);
                                map.put("XDCommodity_ContainerNo",XDCommodity_ContainerNo);
                                map.put("XDCommodity_GdsNW",XDCommodity_GdsNW);
                                map.put("XDInviceno",XDInviceno);
                                map.put("XDCOO",XDCOO);
                                lists.add(map);
                            }
                        }
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


    public List<Map<String,Object>> dataFormatFor21(Map<String,List<Map>> dataMap, String ywNo) {
        List<Map> list = getXDCommodityFor21(dataMap);
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
                            /**
                             * 逻辑处理
                             *      料号匹配上把三级数量加到一起匹配
                             */
                            if(list != null && list.size() >0 ){
                                boolean ok = true;
                                for (int k = 0; k < list.size(); k++) {
                                    if(map.get("料号").equals(list.get(k).get("XDCommodity_PartNumber")) && map.get("数量").equals(list.get(k).get("数量")+"")){
                                        map.put("净重",list.get(k).get("XDCommodity_Attach_SupplierNw"));
                                        List<Map> XDCommodity2List = (List<Map>) list.get(k).get("XDCommodity2List");
                                        for (int l = 0; l < XDCommodity2List.size(); l++) {
                                            if(l == 0 ){
                                                map.put("batch号",XDCommodity2List.get(l).get("XDCommodity_Attach_BatchNo"));
                                                map.put("batch数量",XDCommodity2List.get(l).get("XDCommodity_Attach_BatchQty"));
                                                map.put("分净重",XDCommodity2List.get(l).get("XDCommodity_Attach_SupplierNw"));
                                                lists.add(map);
                                            }else {
                                                Map<String,Object> mapForMore = new HashMap<>();//封装detail
                                                mapForMore.put("batch号",XDCommodity2List.get(l).get("XDCommodity_Attach_BatchNo"));
                                                mapForMore.put("batch数量",XDCommodity2List.get(l).get("XDCommodity_Attach_BatchQty"));
                                                mapForMore.put("分净重",XDCommodity2List.get(l).get("XDCommodity_Attach_SupplierNw"));
                                                lists.add(mapForMore);
                                            }
                                        }
                                        list.remove(k);
                                        k--;
                                        ok = false;
                                        break;
                                    }
                                }
                                if(ok){
                                    lists.add(map);
                                }
                            }else {
                                //没有箱单 直接添加
                                lists.add(map);
                            }
                        }
                    }else {
                        //发票无二级无法处理
                    }
                }
            }

            return lists;
            }catch (Exception e){
            return null;
        }
    }

    private List<Map>  getXDCommodityFor21(Map<String, List<Map>> dataMap){//,String FPCommodity_PartNumber,String FPCommodity_GdsQty1
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
                        List<Map> XDCommodity2List = (List<Map>)XDCommodityList.get(j).get("XDCommodity2List");
                        if(XDCommodity2List != null && XDCommodity2List.size() >0 ){
                            int count = 0;
                            Double XDCommodity_Attach_SupplierNw = new Double("0");
                            for (int l = 0; l < XDCommodity2List.size(); l++) {
                                String XDCommodity_Attach_BatchQty = XDCommodity2List.get(l).get("XDCommodity_Attach_BatchQty")+"";
                                String XDCommodity_AttachSupplierNw = XDCommodity2List.get(l).get("XDCommodity_Attach_SupplierNw")+"";
                                if(util.checkNull(XDCommodity_AttachSupplierNw)){
                                    XDCommodity_Attach_SupplierNw += new Double(XDCommodity_AttachSupplierNw);

                                }
                                if(".0".equals(XDCommodity_Attach_BatchQty.substring(XDCommodity_Attach_BatchQty.length()-2,XDCommodity_Attach_BatchQty.length()))){
                                    count += Integer.parseInt(XDCommodity_Attach_BatchQty.substring(0,XDCommodity_Attach_BatchQty.indexOf(".")));
                                }
                            }
                            XDCommodityList.get(j).put("数量",count);
                            XDCommodityList.get(j).put("XDCommodity_Attach_SupplierNw",XDCommodity_Attach_SupplierNw);
                            lists.add(XDCommodityList.get(j));
                        }
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
}
