package com.kwe.kweplus.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kwe.kweplus.base.OcrLogicService;
import com.kwe.kweplus.common.FirstField;
import com.kwe.kweplus.common.ThreeField;
import com.kwe.kweplus.common.TwoField;
import com.kwe.kweplus.dao.JintieLogsMapper;
import com.kwe.kweplus.modal.JintieCustom;
import com.kwe.kweplus.modal.JintieLogs;
import com.kwe.kweplus.modal.JintieYwinfo;
import com.kwe.kweplus.service.IJintieYwinfoService;
import com.kwe.kweplus.service.OcrSeverService;
import com.kwe.kweplus.util.HttpUtil;
import com.kwe.kweplus.util.OcrObjectUtils;
import com.kwe.kweplus.util.ocrUtil.OcrUtil;
import com.kwe.kweplus.util.text.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ly on 2019/8/26 16:31
 */
@Controller
@RequestMapping("/kwe/ocr")
public class OcrLogicController {
        private Logger logger = LoggerFactory.getLogger(com.kwe.kweplus.controller.OcrServiceMergeController.class);

        @Value("${urlConfig.sandaoPdfUrl}")
        public String sandaoPdfUrl = "";
        @Value("${urlConfig.sandaoExcelUrl}")
        public String sandaoExcelUrl = "";
        @Value("${urlConfig.dgUrl}")
        public String dgUrl = "";
        @Value("${urlConfig.uploadFileSavePath}")
        public String uploadFileSavePath = "";
        @Value("${urlConfig.ocrFileSavePath}")
        public String ocrFileSavePath = "";
        @Autowired
        com.kwe.kweplus.dao.JintieLogsMapper JintieLogsMapper;
        @Autowired
        com.kwe.kweplus.service.IJintieCustomService IJintieCustomService;
        @Autowired
        private IJintieYwinfoService jintieYwinfoService;
        @Autowired
        com.kwe.kweplus.service.OcrSeverService OcrSeverService;

        public String getCustomName(String customKey) {
            JintieCustom JintieCustom = IJintieCustomService.getCustomName(customKey);
            return JintieCustom.getCustomName();
        }


        public boolean checkNull(String str) {
            if ("".equals(str)) {
                return false;
            }
            if ("null".equals(str)) {
                return false;
            }
            if (str == null) {
                return false;
            }
            return true;
        }
        public void formatData(String key,Object value,Map map) {
            if (checkNull(value+"")) {
                map.put(key,value);
            }

        }




        /**
         * json数据接收接口
         *
         * @param
         * @return
         */
        @PostMapping(value = "dataResultTest123")
        @ResponseBody
        public Map dataResult(HttpServletRequest request, String data, String code, String message, String serviceNo) {

            OcrUtil util = new OcrUtil();
            JintieLogs logs =  new JintieLogs();
            logs.setUrl("/kwe/ocr/dataResult");
            logs.setIp(HttpUtil.getIpAddr(request));
            logs.setMethod(serviceNo);
            logs.setParameter(data);
            Map map = new HashMap();
            JintieYwinfo ywinfo = null;

            //业务编号, 唯一标记一次识别的单号
            if (StringUtils.isEmpty(serviceNo)) {
                map.put("code", 0);
                map.put("message", "业务编号不能为空！");
                return map;
            } else {
                ywinfo = jintieYwinfoService.selectByYwNo(serviceNo);
                if (ywinfo == null) {
                    map.put("code", 0);
                    map.put("message", "业务编号不存在！");
                    logger.info("业务编号不存在！");
                    return map;
                }else {
//                if(!"待识别".equals(ywinfo.getStatus()) ){
//                    map.put("code", 0);
//                    map.put("message", "收据已经修改，识别再次推数据！");
//                    logger.info("收据已经修改，识别再次推数据！"+serviceNo);
//                    return map;
//                }
                }
            }

            //待清洗的仓储数据
            List<Map> AWList = new ArrayList<>();
            List<Map> ANList = new ArrayList<>();
            List<Map> SWList = new ArrayList<>();
            List<Map> FPList= new ArrayList();
            List<Map> XDList = new ArrayList();
            List<Map> HTList = new ArrayList<>();
            List<Map> CLList = new ArrayList<>();
            try {
                //Json返回状态处理
                if ("1".equals(code)) {
                    //json转换为对象
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = JSON.parseObject(data);
                    } catch (Exception e) {
                        map.put("code", 0);
                        map.put("message", "JSON格式化异常,JSON格式不正确！");
                        logger.error("JSON格式化异常,JSON格式不正确！");
                        return map;
                    }
                    //查询业务编号对应类别（进仓还是出仓）
                    //进仓对应字段处理
                    try {
                        //数据整理，通过字段整理分类
                        /**
                         * 空运单
                         */
//                        JSONArray AwJSONArray = jsonObject.getJSONArray("AWList");
//                        if (AwJSONArray != null) {
//                            for (int i = 0; i < AwJSONArray.size(); i++) {
//                                JSONObject awJSONArray = AwJSONArray.getJSONObject(i);
//                                String AWMawb = OcrObjectUtils.getString(awJSONArray, "AWMawb");
//                                String AWHawb = OcrObjectUtils.getString(awJSONArray, "AWHawb");
//                                String AWDestinationAirport = OcrObjectUtils.getString(awJSONArray, "AWModeOfTransport");
//                         String AWModeOfTransport = OcrObjectUtils.getString(awJSONArray, "AWModeOfTransport");
//                                String AWQTY = OcrObjectUtils.getString(awJSONArray, "AWQTY");
//                                String AWGW = OcrObjectUtils.getString(awJSONArray, "AWGW");
//                                String AWModeOfTransport = OcrObjectUtils.getString(awJSONArray, "AWModeOfTransport");
//                                Map awMap = new HashMap();
//                                formatData("AWMawb",AWMawb,awMap);
//                                formatData("AWHawb",AWHawb,awMap);
//                                formatData("AWDestinationAirport",AWDestinationAirport,awMap);
//                                formatData("AWQTY",AWQTY,awMap);
//                                formatData("AWGW",AWGW,awMap);
//                                formatData("AWModeOfTransport",AWModeOfTransport,awMap);
//                                if(!awMap.isEmpty()){
//                                    AWList.add(awMap);
//                                }
//                            }
//                        }
                        /**
                         * 通知书
                         */
//                        JSONArray AnJSONArray = jsonObject.getJSONArray("ANList");
//                        if (AnJSONArray != null) {
//                            for (int i = 0; i < AnJSONArray.size(); i++) {
//                                JSONObject anJSONArray = AnJSONArray.getJSONObject(i);
//                                String ANMawb = OcrObjectUtils.getString(anJSONArray, "ANMawb");
//                                String ANHawb = OcrObjectUtils.getString(anJSONArray, "ANHawb");
//                                String ANQTY = OcrObjectUtils.getString(anJSONArray, "ANQTY");
//                                String ANGW = OcrObjectUtils.getString(anJSONArray, "ANGW");
//                                Map anMap = new HashMap();
//                                formatData("ANMawb",ANMawb,anMap);
//                                formatData("ANHawb",ANHawb,anMap);
//                                formatData("ANQTY",ANQTY,anMap);
//                                formatData("ANGW",ANGW,anMap);
//                                if(!anMap.isEmpty()){
//                                    ANList.add(anMap);
//                                }
//                                String CLMawb = OcrObjectUtils.getString(anJSONArray, "CLMawb");
//                                String CLHawb = OcrObjectUtils.getString(anJSONArray, "CLHawb");
//                                String CLQTY = OcrObjectUtils.getString(anJSONArray, "CLQTY");
//                                String CLGW = OcrObjectUtils.getString(anJSONArray, "CLGW");
//                                String CLDeliveryPort = OcrObjectUtils.getString(anJSONArray, "CLDeliveryPort");
//                                Map clMap = new HashMap();
//                                formatData("CLMawb",CLMawb,clMap);
//                                formatData("CLHawb",CLHawb,clMap);
//                                formatData("CLQTY",CLQTY,clMap);
//                                formatData("CLGW",CLGW,clMap);
//                                formatData("CLDeliveryPort",CLDeliveryPort,clMap);
//                                if(!clMap.isEmpty()){
//                                    CLList.add(clMap);
//                                }
//
//                            }
//                        }
                        /**
                         * 海运单
                         */
//                        JSONArray SwJSONArray = jsonObject.getJSONArray("SwResultList");
//                        if (SwJSONArray != null) {
//                            for (int i = 0; i < SwJSONArray.size(); i++) {
//                                JSONObject swJSONObject = SwJSONArray.getJSONObject(i);
//                                String SWNo = OcrObjectUtils.getString(swJSONObject, "SWNo");
//                                String SWShipper = OcrObjectUtils.getString(swJSONObject, "SWShipper");
//                                String SWConsignee = OcrObjectUtils.getString(swJSONObject, "C");
//                                String SWVessel = OcrObjectUtils.getString(swJSONObject, "SWVessel");
//                                String SWVoyage = OcrObjectUtils.getString(swJSONObject, "SWVoyage");
//                                String SWLoadingPort = OcrObjectUtils.getString(swJSONObject, "SWLoadingPort");
//                                String SWDeliveryPlace = OcrObjectUtils.getString(swJSONObject, "SWDeliveryPlace");
//                                String SWModeOfTransport = OcrObjectUtils.getString(swJSONObject, "SWModeOfTransport");
//                                String SWQTY = OcrObjectUtils.getString(swJSONObject, "SWQTY");
//                                String SWGW = OcrObjectUtils.getString(swJSONObject, "SWGW");
//                                String SWTermType = OcrObjectUtils.getString(swJSONObject, "SWTermType");
//                                Map swMap = new HashMap();
//                                formatData("SWNo",SWNo,swMap);
//                                formatData("SWShipper",SWShipper,swMap);
//                                formatData("SWConsignee",SWConsignee,swMap);
//                                formatData("SWVessel",SWVessel,swMap);
//                                formatData("SWVoyage",SWVoyage,swMap);
//                                formatData("SWLoadingPort",SWLoadingPort,swMap);
//                                formatData("SWDeliveryPlace",SWDeliveryPlace,swMap);
//                                formatData("SWModeOfTransport",SWModeOfTransport,swMap);
//                                formatData("SWQTY",SWQTY,swMap);
//                                formatData("SWGW",SWGW,swMap);
//                                formatData("SWTermType",SWTermType,swMap);
//                                if(!swMap.isEmpty()){
//                                    SWList.add(swMap);
//                                }
//                            }
//                        }
                        //                        JSONArray SwJSONArray = jsonObject.getJSONArray("SwResultList");
//                        if (SwJSONArray != null) {
//                            for (int i = 0; i < SwJSONArray.size(); i++) {
//                                JSONObject swJSONObject = SwJSONArray.getJSONObject(i);
//                                String SWNo = OcrObjectUtils.getString(swJSONObject, "SWNo");
//                                String SWConsignee = OcrObjectUtils.getString(swJSONObject, "C");
//                                String SWVessel = OcrObjectUtils.getString(swJSONObject, "SWVessel");
//                                String SWVoyage = OcrObjectUtils.getString(swJSONObject, "SWVoyage");
//                                String SWLoadingPort = OcrObjectUtils.getString(swJSONObject, "SWLoadingPort");
//                                String SWDeliveryPlace = OcrObjectUtils.getString(swJSONObject, "SWDeliveryPlace");
//                                String SWModeOfTransport = OcrObjectUtils.getString(swJSONObject, "SWModeOfTransport");
//                                String SWTermType = OcrObjectUtils.getString(swJSONObject, "SWTermType");
//                                Map swMap = new HashMap();
//                                formatData("SWNo",SWNo,swMap);
//                                formatData("SWConsignee",SWConsignee,swMap);
//                                formatData("SWVessel",SWVessel,swMap);
//                                formatData("SWVoyage",SWVoyage,swMap);
//                                formatData("SWLoadingPort",SWLoadingPort,swMap);
//                                formatData("SWDeliveryPlace",SWDeliveryPlace,swMap);
//                                formatData("SWModeOfTransport",SWModeOfTransport,swMap);
//                                formatData("SWTermType",SWTermType,swMap);
//                                if(!swMap.isEmpty()){
//                                    SWList.add(swMap);
//                                }
//                            }
//                        }
                        /**
                         * 发票
                         */
                        JSONArray FpJSONArray = jsonObject.getJSONArray("FPList");
                        if (FpJSONArray != null) {
                            for (int i = 0; i < FpJSONArray.size(); i++) {
                                JSONObject fpJSONObject = FpJSONArray.getJSONObject(i);
                                Map fpMap = new HashMap();
                                fpFormatDate(fpJSONObject,fpMap,"FPNo");
                                fpFormatDate(fpJSONObject,fpMap,"FPTermType");
                                fpFormatDate(fpJSONObject,fpMap,"FPModeOfTransport");
                                fpFormatDate(fpJSONObject,fpMap,"FPFrom");
                                fpFormatDate(fpJSONObject,fpMap,"FPTo");
                                fpFormatDate(fpJSONObject,fpMap,"FPCOO");
                                fpFormatDate(fpJSONObject,fpMap,"FPCurrency");
                                fpFormatDate(fpJSONObject,fpMap,"FPSupName");
                                fpFormatDate(fpJSONObject,fpMap,"FPQTY");
                                fpFormatDate(fpJSONObject,fpMap,"FPGW");
                                fpFormatDate(fpJSONObject,fpMap,"FPTotalNW");
                                fpFormatDate(fpJSONObject,fpMap,"FPTotalGW");
                                fpFormatDate(fpJSONObject,fpMap,"FPGdsUnit");
                                /**
                                 * 发票结构体
                                 */
                                JSONArray FPCommodity = fpJSONObject.getJSONArray("FPCommodity");
                                List<Map> FPCommodityList= new ArrayList();
                                if(FPCommodity != null){
                                    for (int j = 0; j < FPCommodity.size(); j++) {
                                        JSONObject FPCommodityJsonObject = FPCommodity.getJSONObject(j);
                                        Map fpCommodityMap = new HashMap();
                                        formatData("FPCommodity_ItemNo",CleaningString(ywinfo.getYwNo(),OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_ItemNo")),fpCommodityMap);
                                        formatData( "FPCommodity_GdsQty1",CleaningString(ywinfo,OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_GdsQty1")),fpCommodityMap);
                                        formatData( "FPCommodity_GdsPrice",CleaningString(ywinfo,OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_GdsPrice")),fpCommodityMap);
                                        formatData( "FPCommodity_GdsAmount",CleaningString(ywinfo,OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_GdsAmount")),fpCommodityMap);
                                        formatData( "FPCommodity_GdsGW",CleaningString(ywinfo,OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_GdsGW")),fpCommodityMap);
                                        formatData( "FPCommodity_GdsNW",CleaningString(ywinfo,OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_GdsNW")),fpCommodityMap);
                                        formatData( "FPCommodity_PartNumber",CleaningSC(OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_PartNumber")),fpCommodityMap);
                                        formatData( "FPCommodity_SN",OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_SN"),fpCommodityMap);
                                        formatData( "FPCommodity_PO",OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_PO"),fpCommodityMap);
                                        formatData( "FPCommodity_GdsUnit1",OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_GdsUnit1"),fpCommodityMap);
                                        formatData( "FPCommodity_Currency",OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_Currency"),fpCommodityMap);
                                        formatData( "FPCommodity_COO",OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_COO"),fpCommodityMap);
                                        formatData( "FPCommodity_GdsMeasurement",OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_GdsMeasurement"),fpCommodityMap);
                                        formatData( "FPCommodity_EndUserNo",OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_EndUserNo"),fpCommodityMap);
                                        formatData( "FPCommodity_OrderNo",OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_OrderNo"),fpCommodityMap);
                                        formatData( "FPCommodity_CPN",OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_CPN"),fpCommodityMap);
                                        formatData( "FPCommodity_EndUser",OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_EndUser"),fpCommodityMap);
                                        formatData( "FPCommodity_So",OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_So"),fpCommodityMap);
                                        formatData( "FPCommodity_MOQ",OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_MOQ"),fpCommodityMap);
                                        formatData( "FPCommodity_ReMarks",OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_ReMarks"),fpCommodityMap);
                                        formatData( "FPCommodity_EndUser2",OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_EndUser2"),fpCommodityMap);
                                        formatData( "FPCommodity_EndUser3",OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_EndUser3"),fpCommodityMap);
                                        formatData( "FPCommodity_ShipmentNo",OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_ShipmentNo"),fpCommodityMap);
                                        formatData( "FPCommodity_Dept",OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_Dept"),fpCommodityMap);
                                        formatData( "FPCommodity_DN",OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_DN"),fpCommodityMap);
                                        formatData( "FPCommodity_GdsCtnQty",OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_GdsCtnQty"),fpCommodityMap);
                                        formatData( "FPCommodity_InProductiondate", OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_InProductiondate"),fpCommodityMap);
                                        formatData( "FPCommodity_InWorkNo",OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_InWorkNo"),fpCommodityMap);
                                        formatData( "FPCommodity_InFPNo",OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_InFPNo"),fpCommodityMap);
                                        formatData( "FPCommodity_InWayBill",OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_InWayBill"),fpCommodityMap);
                                        /**
                                         * 发票结构体2
                                         */
                                        JSONArray FPCommodity2 = FPCommodityJsonObject.getJSONArray("FPCommodity_Attach");
                                        List<Map> FPCommodity2List= new ArrayList();
                                        if(FPCommodity2 != null){
                                            for (int n = 0; n < FPCommodity2.size(); n++) {
                                                JSONObject FPCommodity2JsonObject = FPCommodity2.getJSONObject(n);
                                                String FPCommodity_Attach_CtnNo = OcrObjectUtils.getString(FPCommodity2JsonObject,"FPCommodity_Attach_CtnNo");
                                                if(util.checkNull(FPCommodity_Attach_CtnNo) && FPCommodity_Attach_CtnNo.contains(".0")){
                                                    FPCommodity_Attach_CtnNo = FPCommodity_Attach_CtnNo.replace(".0","");
                                                }
                                                Map fpCommodity2Map = new HashMap();
                                                formatData( "FPCommodity_Attach_CtnNo",FPCommodity_Attach_CtnNo,fpCommodity2Map);
                                                formatData( "FPCommodity_Attach_BatchQty",CleaningString(ywinfo,OcrObjectUtils.getString(FPCommodity2JsonObject,"FPCommodity_Attach_BatchQty")),fpCommodity2Map);
                                                formatData( "FPCommodity_Attach_SupplierNw",CleaningString(ywinfo,OcrObjectUtils.getString(FPCommodity2JsonObject,"FPCommodity_Attach_SupplierNw")),fpCommodity2Map);
                                                formatData( "FPCommodity_Attach_SupplierGw",CleaningString(ywinfo,OcrObjectUtils.getString(FPCommodity2JsonObject,"FPCommodity_Attach_SupplierGw")),fpCommodity2Map);
                                                formatData( "FPCommodity_Attach_SupplierGoodQty",CleaningString(ywinfo,OcrObjectUtils.getString(FPCommodity2JsonObject,"FPCommodity_Attach_SupplierGoodQty")),fpCommodity2Map);
                                                formatData( "FPCommodity_Attach_SupplierAmount",OcrObjectUtils.getString(FPCommodity2JsonObject,"FPCommodity_Attach_SupplierAmount "),fpCommodity2Map);
                                                formatData( "FPCommodity_Attach_BatchNo", OcrObjectUtils.getString(FPCommodity2JsonObject,"FPCommodity_Attach_BatchNo"),fpCommodity2Map);
                                                formatData( "FPCommodity_Attach_EngineNo",OcrObjectUtils.getString(FPCommodity2JsonObject,"FPCommodity_Attach_EngineNo"),fpCommodity2Map);
                                                formatData( "FPCommodity_Attach_LotNo",OcrObjectUtils.getString(FPCommodity2JsonObject,"FPCommodity_Attach_LotNo"),fpCommodity2Map);
                                                if(!fpCommodity2Map.isEmpty()){
                                                    FPCommodity2List.add(fpCommodity2Map);
                                                }
                                            }
                                        }
                                        if(FPCommodity2List != null && FPCommodity2List.size() > 0){
                                            fpCommodityMap.put("FPCommodity2List",FPCommodity2List);
                                        }
                                        if(!fpCommodityMap.isEmpty()){
                                            FPCommodityList.add(fpCommodityMap);
                                        }
                                    }
                                }
                                if(FPCommodityList != null && FPCommodityList.size() >0 ){
                                    fpMap.put("FPCommodityList",FPCommodityList);
                                }
                                if(!fpMap.isEmpty()){
                                    FPList.add(fpMap);
                                }
                            }
                        }
                        /**
                         * 箱单
                         */
                        JSONArray XdJSONArray = jsonObject.getJSONArray("XDList");
                        if (XdJSONArray != null) {
                            for (int i = 0; i < XdJSONArray.size(); i++) {
                                JSONObject xdJSONObject = XdJSONArray.getJSONObject(i);
                                Map XdMap = new HashMap();
                                formatData( "XDShipper",OcrObjectUtils.getString(xdJSONObject, "XDShipper"),XdMap);
                                formatData( "XDFrom",OcrObjectUtils.getString(xdJSONObject, "XDFrom"),XdMap);
                                formatData( "XDTo",OcrObjectUtils.getString(xdJSONObject, "XDTo"),XdMap);
                                formatData( "XDTermType",OcrObjectUtils.getString(xdJSONObject, "XDTermType"),XdMap);
                                formatData( "XDModeOfTransport",OcrObjectUtils.getString(xdJSONObject, "XDModeOfTransport"),XdMap);
                                formatData( "XDCOO",OcrObjectUtils.getString(xdJSONObject, "XDCOO"),XdMap);
                                formatData( "XDSupName",OcrObjectUtils.getString(xdJSONObject, "XDSupName"),XdMap);
                                formatData( "XDQTY",OcrObjectUtils.getString(xdJSONObject, "XDQTY"),XdMap);
                                formatData( "XDGW",OcrObjectUtils.getString(xdJSONObject, "XDGW"),XdMap);
                                formatData( "XDInviceno",OcrObjectUtils.getString(xdJSONObject, "XDInviceno"),XdMap);
                                formatData( "XDConsignee",OcrObjectUtils.getString(xdJSONObject, "XDConsignee"),XdMap);
                                /**
                                 * 箱单结构体
                                 */
                                JSONArray XDCommodity = xdJSONObject.getJSONArray("XDCommodity");
                                System.out.println("XDCommodity.size()的大小"+XDCommodity.size());
                                List<Map> XDCommodityList = new ArrayList();
                                if(XDCommodity != null){
                                    for (int j = 0; j < XDCommodity.size(); j++) {
                                        JSONObject XDCommodityJsonObject = XDCommodity.getJSONObject(j);
                                        Map XdCommodityMap = new HashMap();
                                        formatData( "XDCommodity_ItemNo",CleaningString(ywinfo.getYwNo(),OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_ItemNo")),XdCommodityMap);
                                        formatData( "XDCommodity_PO",OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_PO"),XdCommodityMap);
                                        formatData( "XDCommodity_PartNumber",OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_PartNumber"),XdCommodityMap);
                                        formatData( "XDCommodity_SN",OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_SN"),XdCommodityMap);
                                        formatData( "XDCommodity_GdsQty1",CleaningString(ywinfo, OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_GdsQty1")),XdCommodityMap);
                                        formatData( "XDCommodity_GdsUnit1",OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_GdsUnit1"),XdCommodityMap);
                                        formatData( "XDCommodity_GdsNW",CleaningString(ywinfo,OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_GdsNW")),XdCommodityMap);
                                        formatData( "XDCommodity_GdsGW",CleaningString(ywinfo,OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_GdsGW")),XdCommodityMap);
                                        formatData( "XDCommodity_GdsMeasurement",CleaningString(ywinfo,OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_GdsMeasurement")),XdCommodityMap);
                                        formatData( "XDCommodity_COO",OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_COO"),XdCommodityMap);
                                        formatData( "XDCommodity_EndUserNo",OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_EndUserNo"),XdCommodityMap);
                                        formatData( "XDCommodity_OrderNo",OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_OrderNo"),XdCommodityMap);
                                        formatData( "XDCommodity_CPN",OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_CPN"),XdCommodityMap);
                                        formatData( "XDCommodity_EndUser",OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_EndUser"),XdCommodityMap);
                                        formatData( "XDCommodity_So",OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_So"),XdCommodityMap);
                                        formatData( "XDCommodity_MOQ",OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_MOQ"),XdCommodityMap);
                                        formatData( "XDCommodity_ReMarks",OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_ReMarks"),XdCommodityMap);
                                        formatData( "XDCommodity_EndUser2",OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_EndUser2"),XdCommodityMap);
                                        formatData( "XDCommodity_EndUser3",OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_EndUser3"),XdCommodityMap);
                                        formatData( "XDCommodity_ShipmentNo",OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_ShipmentNo"),XdCommodityMap);
                                        formatData( "XDCommodity_Dept",OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_Dept"),XdCommodityMap);
                                        formatData( "XDCommodity_DN",OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_DN"),XdCommodityMap);
                                        formatData( "XDCommodity_GdsCtnQty",OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_GdsCtnQty"),XdCommodityMap);
                                        formatData( "XDCommodity_InWorkNo",OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_InWorkNo"),XdCommodityMap);
                                        formatData( "XDCommodity_InXDNo",OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_InXDNo"),XdCommodityMap);
                                        formatData( "XDCommodity_InWayBill",OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_InWayBill"),XdCommodityMap);
                                        formatData( "XDCommodity_InProductiondate",OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_InProductiondate"),XdCommodityMap);

                                        formatData( "XDCommodity_BatchNo",OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_BatchNo"),XdCommodityMap);
                                        formatData( "XDCommodity_BatchQty",OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_BatchQty"),XdCommodityMap);
                                        formatData( "XDCommodity_SupplierNumber",OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_SupplierNumber"),XdCommodityMap);
                                        formatData( "XDCommodity_SupplierTotalQty",OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_SupplierTotalQty"),XdCommodityMap);
                                        formatData( "XDCommodity_SupplierGoodQty",OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_SupplierGoodQty"),XdCommodityMap);
                                        formatData( "XDCommodity_SupplierNw",OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_SupplierNw"),XdCommodityMap);
                                        formatData( "XDCommodity_SupplierGw",OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_SupplierGw"),XdCommodityMap);
                                        formatData( "XDCommodity_SupplierAmount",OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_SupplierAmount"),XdCommodityMap);
                                        formatData( "XDCommodity_CtnNo",OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_CtnNo"),XdCommodityMap);
                                        formatData( "XDCommodity_CtnDim",OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_CtnDim"),XdCommodityMap);
                                        formatData( "XDCommodity_CtnDimUnit",OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_CtnDimUnit"),XdCommodityMap);
                                        formatData( "XDCommodity_CtnGdsQty",OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_CtnGdsQty"),XdCommodityMap);
                                        formatData( "XDCommodity_EngineNo",OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_EngineNo"),XdCommodityMap);
                                        formatData( "XDCommodity_LotNo",OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_LotNo"),XdCommodityMap);
                                        //箱单结构体2
                                        JSONArray XDCommodity2 = XDCommodityJsonObject.getJSONArray("XDCommodity_Attach");
                                        List<Map> XDCommodity2List= new ArrayList();
                                        if(XDCommodity2!= null){
                                            for (int n = 0; n < XDCommodity2.size(); n++) {
                                                JSONObject XDCommodity2JsonObject = XDCommodity2.getJSONObject(n);
                                                Map XdCommodity2Map = new HashMap();
                                                formatData( "XDCommodity_Attach_CtnNo", OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_CtnNo"),XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_BatchQty",CleaningString(ywinfo,OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_BatchQty")),XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_SupplierNw",CleaningString(ywinfo,OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_SupplierNw")),XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_SupplierGw",CleaningString(ywinfo,OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_SupplierGw")),XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_SupplierAmount",CleaningString(ywinfo,OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_SupplierAmount")),XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_SupplierGoodQty",CleaningString(ywinfo,OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_SupplierGoodQty")),XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_BatchNo", OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_BatchNo"),XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_EngineNo", OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_EngineNo"),XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_LotNo",OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_LotNo"),XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_SupplierTotalQty",OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_SupplierTotalQty"),XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_SupplierNumber",OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_SupplierNumber"),XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_CtnGdsQty",CleaningString(ywinfo,OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_CtnGdsQty")),XdCommodity2Map);


                                                formatData( "XDCommodity_Attach_ItemNo",CleaningString(ywinfo,OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_ItemNo")),XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_PO",CleaningString(ywinfo,OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_PO")),XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_POLine",CleaningString(ywinfo,OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_POLine")),XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_PartNumber",CleaningString(ywinfo,OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_PartNumber")),XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_SN",CleaningString(ywinfo,OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_SN")),XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_HSCode",CleaningString(ywinfo,OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_HSCode")),XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_ECCN",CleaningString(ywinfo,OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_ECCN")),XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_Desc",CleaningString(ywinfo,OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_Desc")),XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_Standard",CleaningString(ywinfo,OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_Standard")),XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_StandardEx",CleaningString(ywinfo,OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_StandardEx")),XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_GdsQty1",CleaningString(ywinfo,OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_GdsQty1")),XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_GdsUnit1",CleaningString(ywinfo,OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_GdsUnit1")),XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_GdsQty2",CleaningString(ywinfo,OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_GdsQty2")),XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_GdsUnit2",CleaningString(ywinfo,OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_GdsUnit2")),XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_GdsNW",CleaningString(ywinfo,OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_GdsNW")),XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_GdsGW",CleaningString(ywinfo,OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_GdsGW")),XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_GdsMeasurement",CleaningString(ywinfo,OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_GdsMeasurement")),XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_ContainerNo",CleaningString(ywinfo,OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_ContainerNo")),XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_COO",CleaningString(ywinfo,OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_COO")),XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_EndUserNo",CleaningString(ywinfo,OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_EndUserNo")),XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_OrderNo",CleaningString(ywinfo,OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_OrderNo")),XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_CPN",CleaningString(ywinfo,OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_CPN")),XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_EndUser",CleaningString(ywinfo,OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_EndUser")),XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_So",CleaningString(ywinfo,OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_So")),XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_MOQ",CleaningString(ywinfo,OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_MOQ")),XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_ReMarks",CleaningString(ywinfo,OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_ReMarks")),XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_EndUser2",CleaningString(ywinfo,OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_EndUser2")),XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_EndUser3",CleaningString(ywinfo,OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_EndUser3")),XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_ShipmentNo",CleaningString(ywinfo,OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_ShipmentNo")),XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_Dept",CleaningString(ywinfo,OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_Dept")),XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_DN",CleaningString(ywinfo,OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_DN")),XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_GdsCtnQty",CleaningString(ywinfo,OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_GdsCtnQty")),XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_InWorkNo",CleaningString(ywinfo,OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_InWorkNo")),XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_InXDNo",CleaningString(ywinfo,OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_InXDNo")),XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_InWayBill",CleaningString(ywinfo,OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_InWayBill")),XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_InProductiondate",CleaningString(ywinfo,OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_InProductiondate")),XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_BoxNumber",CleaningString(ywinfo,OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_BoxNumber")),XdCommodity2Map);
                                                if(!XdCommodity2Map.isEmpty()){
                                                    XDCommodity2List.add(XdCommodity2Map);
                                                }
                                            }

                                        }
                                        if(XDCommodity2List != null && XDCommodity2List.size() >0 ){
                                            XdCommodityMap.put("XDCommodity2List",XDCommodity2List);
                                        }
                                        if(!XdCommodityMap.isEmpty()){
                                            XDCommodityList.add(XdCommodityMap);
                                        }
                                    }
                                }
                                if(XDCommodityList != null && XDCommodityList.size() >0 ){
                                    XdMap.put("XDCommodityList",XDCommodityList);
                                }
                                if(!XdMap.isEmpty()){
                                    XDList.add(XdMap);
                                }
                            }
                        }
                        /**
                         * 合同
                         */
                        JSONArray HtJSONArray = jsonObject.getJSONArray("HtList");
                        if (HtJSONArray != null) {
                            for (int i = 0; i < HtJSONArray.size(); i++) {
                                JSONObject htJSONObject = HtJSONArray.getJSONObject(i);
                                Map htMap = new HashMap();
                                formatData("HTShipper",OcrObjectUtils.getString(htJSONObject, "HTShipper"),htMap);
                                formatData("HTFrom",OcrObjectUtils.getString(htJSONObject, "HTFrom"),htMap);
                                formatData("HTTo",OcrObjectUtils.getString(htJSONObject, "HTTo"),htMap);
                                formatData("HTInviceno",OcrObjectUtils.getString(htJSONObject, "HTInviceno"),htMap);
                                formatData("HTCurrency",OcrObjectUtils.getString(htJSONObject, "HTCurrency"),htMap);
                                formatData("HTCOO", OcrObjectUtils.getString(htJSONObject, "HTCOO"),htMap);
                                formatData("HTTermType",OcrObjectUtils.getString(htJSONObject, "HTTermType"),htMap);
                                formatData("HTCtnQty",OcrObjectUtils.getString(htJSONObject, "HTCtnQty"),htMap);
                                formatData("HTTotalGW",OcrObjectUtils.getString(htJSONObject, "HTTotalGW"),htMap);
                                formatData("HTConsignee",OcrObjectUtils.getString(htJSONObject, "HTConsignee"),htMap);
                                //合同结构体
                                JSONArray HtCommodity = htJSONObject.getJSONArray("HtCommodity");
                                List<Map> HtCommodityList = new ArrayList<>();
                                if(HtCommodity != null){
                                    for (int j = 0; j < HtCommodity.size(); j++) {
                                        JSONObject HTCommodityJsonObject = HtCommodity.getJSONObject(j);
                                        Map htCommodityMap = new HashMap();
                                        formatData("HTCommodity_ItemNo", OcrObjectUtils.getString(HTCommodityJsonObject,"HTCommodity_ItemNo"),htCommodityMap);
                                        formatData("HTCommodity_PartNumber",OcrObjectUtils.getString(HTCommodityJsonObject,"HTCommodity_PartNumber"),htCommodityMap);
                                        formatData("HTCommodity_GdsQty1",OcrObjectUtils.getString(HTCommodityJsonObject,"HTCommodity_GdsQty1"),htCommodityMap);
                                        formatData("HECommodity_GdsUnit1",OcrObjectUtils.getString(HTCommodityJsonObject,"HECommodity_GdsUnit1"),htCommodityMap);
                                        formatData("HTCommodity_GdsPrice",OcrObjectUtils.getString(HTCommodityJsonObject,"HTCommodity_GdsPrice"),htCommodityMap);
                                        formatData("HTCommodity_GdsAmount", OcrObjectUtils.getString(HTCommodityJsonObject,"HTCommodity_GdsAmount"),htCommodityMap);
                                        formatData("HTCommodity_Currency", OcrObjectUtils.getString(HTCommodityJsonObject,"HTCommodity_Currency"),htCommodityMap);
                                        formatData("HTCommodity_COO",OcrObjectUtils.getString(HTCommodityJsonObject,"HTCommodity_COO"),htCommodityMap);
                                        //合同结构体2
                                        JSONArray HTCommodity2 = HTCommodityJsonObject.getJSONArray("XDCommodity_Attach");
                                        List<Map> HTCommodity2List= new ArrayList();
                                        if(HTCommodity2 != null){
                                            for (int n = 0; n < HTCommodity2.size(); n++) {
                                                JSONObject HTCommodity2JsonObject = HTCommodity2.getJSONObject(n);
                                                String HTCommodity_Attach_BatchNo = OcrObjectUtils.getString(HTCommodity2JsonObject,"HTCommodity_Attach_BatchNo");
                                                String HTCommodity_Attach_BatchQty = OcrObjectUtils.getString(HTCommodity2JsonObject,"HTCommodity_Attach_BatchQty");
                                                Map htCommodity2Map = new HashMap();
                                                formatData("HTCommodity_Attach_BatchNo",HTCommodity_Attach_BatchNo,htCommodity2Map);
                                                formatData("HTCommodity_Attach_BatchQty",HTCommodity_Attach_BatchQty,htCommodity2Map);
                                                if(!htCommodity2Map.isEmpty()){
                                                    HTCommodity2List.add(htCommodity2Map);
                                                }
                                            }
                                        }
                                        //一级结构体
                                        if(HTCommodity2List != null && HTCommodity2List.size() >0 ){
                                            htCommodityMap.put("HTCommodity2List",HTCommodity2List);
                                        }
                                        if(!htCommodityMap.isEmpty()){
                                            HtCommodityList.add(htCommodityMap);
                                        }
                                    }
                                }
                                if(HtCommodityList != null && HtCommodityList.size() >0 ){
                                    htMap.put("HtCommodityList",HtCommodityList);
                                }
                                if(!htMap.isEmpty()){
                                    HTList.add(htMap);
                                }
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("出错了" + e);
                    }
                } else {
                    //识别失败返回信息
                    map.put("code", 0);
                    map.put("message", "识别服务出了问题");
                    ywinfo.setStatus("识别失败");
                    ywinfo.setRemark("错误信息："+message);
                    jintieYwinfoService.updateById(ywinfo);
                    return  map;
                }
                Map<String,List<Map>> dataMap = new HashMap<>();
                //dataMap.put("AWList",AWList);
                //dataMap.put("ANList",ANList);
                //dataMap.put("CLList",CLList);
                //dataMap.put("SWList",SWList);
                dataMap.put("HTList",HTList);
                dataMap.put("FPList",FPList);
                dataMap.put("XDList",XDList);
                OcrLogicService ocr = new OcrLogicService();
                dataMap = ocr.forMatOfController(dataMap);
                OcrSeverService.formatData(dataMap,ywinfo);
                map.put("code", 1);
                map.put("message", "接收成功!");
            } catch (Exception e) {
                map.put("code", 1);
                map.put("message", "接收成功!");
                e.printStackTrace();
                ywinfo.setRemark5("0.00");
                ywinfo.setStatus("识别数据异常");
                ywinfo.setRemark(e.getMessage());
                jintieYwinfoService.updateById(ywinfo);
            } finally {
                try {
                    //日志记录data
                    logs.setResult(map.toString());
                    JintieLogsMapper.insert(logs);
                } catch (Exception e) {
                    logger.info("log fail!" + e.getMessage());
                }
                return map;
            }

        }

        @RequestMapping("getIPTest/s1212")
        @ResponseBody
        public String getIP(HttpServletRequest request, String id) {
            return HttpUtil.getIpAddr(request) + "___" + id;
        }



        /**
         * 使用java正则表达式去掉多余的.与0
         * @param obj 带小数点的          单价 总价  毛重  净重
         * @param obj 可能带可能不带      良品数量 batch数量   料号数量
         */
        private  Double CleaningString(JintieYwinfo ywinfo,String obj){
            if(checkNull(obj)){
                if(obj.contains(",")){
                    obj = obj.replace(",","");
                }
                if(obj.contains(" ")){
                    obj = obj.replace(" ","");
                }
                if(obj.contains("@")){
                    obj = obj.replace("@","");
                }
                if(obj.indexOf(".") > 0){
                    obj = obj.replaceAll("0+?$", "");//去掉多余的0
                    obj = obj.replaceAll("[.]$", "");//如最后一位是.则去掉
                }
                try{
                    Double dou = new Double(obj);
                    return dou;
                }catch (Exception e){
                    logger.warn(ywinfo.getYwNo()+"非法数字："+e.getMessage());
                    return new Double("-1");
                }
            }else
                return null;
        }
        /**
         * @param obj 不带小数点的        箱号  行号
         */
        private String CleaningString(String YwNo,String obj){
            if (checkNull(obj)) {
                if(obj.contains(",")){
                    obj = obj.replace(",","");
                }
                if(obj.contains(" ")){
                    obj = obj.replace(" ","");
                }
                if(obj.indexOf(".") > 0){
                    obj = obj.replaceAll("0+?$", "");//去掉多余的0
                    obj = obj.replaceAll("[.]$", "");//如最后一位是.则去掉
                }
                if(!obj.contains("-")){
                    try {
                        Integer.parseInt(obj);
                    }catch (NumberFormatException e){
                        logger.warn("行号或着箱号数据异常（非法），编号："+YwNo+",数据："+e.getMessage());
                        return "-1";
                    }
                }
                return obj;
            }else
                return null;
        }


        /**
         * 料号去*
         * @param obj
         * @return
         */
        private String CleaningSC(String obj){
            if(checkNull(obj)){
                if(obj.contains("*")){
                    obj = obj.replace("*","");
                }
                obj = obj.trim();
            }
            return obj;
        }


        /**
         * 二级转三级
         * @param key
         * @param XdCommodity2Map
         * @param XDCommodityJsonObject
         * @param ywinfo
         * @param YwNo
         */
        private void formatDateBtTwoField(String key,Map XdCommodity2Map,JSONObject XDCommodityJsonObject,JintieYwinfo ywinfo,String YwNo){
            List list = new ArrayList();
            list.add("XDCommodity_BatchQty");
            list.add("XDCommodity_SupplierGoodQty");
            list.add("XDCommodity_SupplierGw");
            list.add("XDCommodity_SupplierNw");
            list.add("XDCommodity_SupplierAmount");
            if(OcrObjectUtils.getString(XDCommodityJsonObject,key)!=null){
                if(list.contains(key)){
                    formatData(TwoField.mapTwo.get(key),CleaningString(ywinfo,OcrObjectUtils.getString(XDCommodityJsonObject,key)),XdCommodity2Map);
                }else if (key.equals("XDCommodity_SupplierNumber")){
                    formatData(TwoField.mapTwo.get(key),CleaningSC(OcrObjectUtils.getString(XDCommodityJsonObject,key)),XdCommodity2Map);
                }else if(key.equals("XDCommodity_CtnNo")){
                    formatData(TwoField.mapTwo.get(key),CleaningString(YwNo,OcrObjectUtils.getString(XDCommodityJsonObject,key)),XdCommodity2Map);
                } else {
                    formatData(TwoField.mapTwo.get(key),OcrObjectUtils.getString(XDCommodityJsonObject,key),XdCommodity2Map);
                }
            }
        }


        /**
         * 三级转二级
         * @param key
         * @param XdCommodityMap
         * @param XDCommodity2JsonObject
         * @param ywinfo
         * @param YwNo
         */
        private void formatDateBtThreeField(String key,Map XdCommodityMap,JSONObject XDCommodity2JsonObject,JintieYwinfo ywinfo,String YwNo){
            List list = new ArrayList();
            list.add("XDCommodity_Attach_GdsQty1");
            list.add("XDCommodity_Attach_GdsQty2");
            list.add("XDCommodity_Attach_GdsNW");
            list.add("XDCommodity_Attach_GdsGW");
            if(OcrObjectUtils.getString(XDCommodity2JsonObject,key)!=null){
                if(list.contains(key)){
                    formatData(ThreeField.mapThree.get(key),CleaningString(ywinfo,OcrObjectUtils.getString(XDCommodity2JsonObject,key)),XdCommodityMap);
                }else if (key.equals("XDCommodity_Attach_PartNumber")){
                    formatData(ThreeField.mapThree.get(key),CleaningSC(OcrObjectUtils.getString(XDCommodity2JsonObject,key)),XdCommodityMap);
                }else if(key.equals("XDCommodity_Attach_ItemNo")|key.equals("XDCommodity_Attach_ContainerNo")){
                    formatData(ThreeField.mapThree.get(key),CleaningString(YwNo,OcrObjectUtils.getString(XDCommodity2JsonObject,key)),XdCommodityMap);
                } else {
                    formatData(ThreeField.mapThree.get(key),OcrObjectUtils.getString(XDCommodity2JsonObject,key),XdCommodityMap);
                }
            }
        }


        /**
         * 发票一级数据处理
         * @param fpJSONObject
         * @param fpMap
         * @param fpData
         */
        private void  fpFormatDate(JSONObject fpJSONObject,Map fpMap,String fpData){
            formatData(fpData,OcrObjectUtils.getString(fpJSONObject, fpData),fpMap);
        }



}
