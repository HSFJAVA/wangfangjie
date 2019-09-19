package com.kwe.kweplus.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kwe.kweplus.common.UserList;
import com.kwe.kweplus.dao.JintieLogsMapper;
import com.kwe.kweplus.modal.*;
import com.kwe.kweplus.service.IJintieYwinfoService;
import com.kwe.kweplus.service.OcrSeverService;
import com.kwe.kweplus.util.FileUtil;
import com.kwe.kweplus.util.HttpUtil;
import com.kwe.kweplus.util.OcrObjectUtils;
import com.kwe.kweplus.util.ocrUtil.OcrUtil;
import com.kwe.kweplus.util.text.DateUtil;
import com.kwe.kweplus.util.text.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/kwe/ocr")
public class OcrServiceController {

    private Logger logger = LoggerFactory.getLogger(OcrServiceController.class);
    UserList UserList = new UserList();
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
    JintieLogsMapper JintieLogsMapper;
    @Autowired
    com.kwe.kweplus.service.IJintieCustomService IJintieCustomService;
    @Autowired
    private IJintieYwinfoService jintieYwinfoService;
    @Autowired
    OcrSeverService OcrSeverService;

    public String getCustomName(String customKey) {
        JintieCustom JintieCustom = IJintieCustomService.getCustomName(customKey);
        return JintieCustom.getCustomName();
    }

    /**
     * 识别业务接口
     *
     * @param
     * @param serviceFile
     * @return
     */
    @PostMapping("addFileYW")
    @ResponseBody
    public ReturnMessage addFileYW(HttpServletRequest request,String customKey, String customerNo, String type, String remark, String createUser, MultipartFile serviceFile,String templateID,String ocrTemplateID,String ocrUrl) {
        JintieLogs logs =  new JintieLogs();
        ReturnMessage msg = new ReturnMessage();
        JintieYwinfo ywinfo = new JintieYwinfo();
        ywinfo.setCustomNo(customerNo.trim());
        if(jintieYwinfoService.selectByCustomNo(ywinfo.getCustomNo()) > 0) {
            //结算客户存在
            msg.setStatus(ReturnMessage.STATUS_ERROR);
            msg.setMessage("结算客户编号已存在");
            return msg;
        }

        try {
            ywinfo.setRemark(remark);
            ywinfo.setRemark1(customKey);
            ywinfo.setClearingCustomerName(getCustomName(customKey));
            ywinfo.setType(Integer.parseInt(type));
            ywinfo.setRemark2(templateID);
            ywinfo.setRemark3(ocrTemplateID);
            ywinfo.setCreateUser(createUser);

            logs.setUrl("/kwe/ocr/addFileYW");
            logs.setIp(HttpUtil.getIpAddr(request));
            logs.setParameter("customerNo:"+customerNo+",files:"+serviceFile.getOriginalFilename()+",createUser:"+createUser+",templateID:"+templateID+",ocrTemplateID:"+ocrTemplateID);
            logs.setUsername(createUser);

            indexController indexController = new indexController();
            String number = indexController.getywno();//业务唯一编号
            HttpUtil httpUtil = new HttpUtil();
            String fileName = serviceFile.getOriginalFilename();
            fileName = number + fileName.substring(fileName.indexOf("."), fileName.length());
            String date = DateUtil.toString(new Date(), "YYYYMMdd");
            String path2 = uploadFileSavePath + File.separator + date + File.separator + fileName;
            File file = new File(path2);
            String realPath = uploadFileSavePath + File.separator + date;
            FileUtil.createFolder(realPath);
            serviceFile.transferTo(file);//存文件
            boolean str = false;
            if("DG".equals(ocrUrl)){
                str = httpUtil.upload(dgUrl, number, type, file, ywinfo.getRemark1(),templateID);
            }else if("SDK".equals(ocrUrl)){
                str = httpUtil.upload(sandaoPdfUrl, number, type, file, ywinfo.getRemark1(),ocrTemplateID);
            } else if("SDX".equals(ocrUrl)){
                str = httpUtil.upload(sandaoExcelUrl, number, type, file, ywinfo.getRemark1(),ocrTemplateID);
            }
            //上传文件,编号，类型到sandao识别接口
            if (str) {
                //成功
                SimpleDateFormat sfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String createDate = sfm.format(new Date());
                ywinfo.setYwNo(number);
                ywinfo.setCreateDate(createDate);
                ywinfo.setCreateDateTime(LocalDateTime.now());
                ywinfo.setRemark4(ocrUrl);
                ywinfo.setCreateUserEmp(UserList.loginMap().get(ywinfo.getCreateUser()).split(",")[1]);
                ywinfo.setStatus("待识别");
                jintieYwinfoService.save(ywinfo);
            } else {
                logger.error("业务添加失败!上传失败，操作人："+ywinfo.getCreateUser());
                msg.setStatus(ReturnMessage.STATUS_ERROR);
                msg.setMessage("业务添加失败!上传失败");
                return msg;
            }
            msg.setStatus(ReturnMessage.STATUS_OK);
            msg.setMessage("业务上传成功");
            logger.info("业务上传成功，业务编号："+ywinfo.getYwNo()+"操作人："+ywinfo.getCreateUser());
            return msg;
        } catch (NullPointerException e) {
            logger.error("业务添加失败!，文件为空，操作人："+ywinfo.getCreateUser());
            msg.setStatus(ReturnMessage.STATUS_ERROR);
            msg.setMessage("文件为空,创建失败！");
            return msg;

        } catch (Exception ex) {
            logger.error("业务添加失败!", ex+"，操作人："+ywinfo.getCreateUser());
            msg.setStatus(ReturnMessage.STATUS_ERROR);
            msg.setMessage("业务添加失败!");
            return msg;
        }finally {
            logs.setResult(msg.toString());
            JintieLogsMapper.insert(logs);
        }
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


//    /**
//     * 文件接收接口
//     * @param serviceNo
//     * @param files
//     * @return
//     */
//    @RequestMapping(value = "fileResult", method = RequestMethod.POST)
//    @ResponseBody
//    public Map fileResult(HttpServletRequest request,String serviceNo, MultipartFile files){
//        JintieLogs logs =  new JintieLogs();
//        logs.setUrl("/kwe/ocr/fileResult");
//        logs.setIp(HttpUtil.getIpAddr(request));
//        logs.setParameter("serviceNo"+serviceNo+",files"+files.getName());
//
//        Map map = new HashMap();
//        String filePath=null;
//            try{
//                //文件目录路径
//                String realPath = ocrFileSavePath;
//                //文件绝对路径
//                filePath = ocrFileSavePath + File.separator + files.getOriginalFilename();
//                //将文件存到本地
//                FileUtil.createFolder(realPath);
//                //读取文件并转储
//                files.transferTo(new File(filePath));
//                map.put("code",1);
//                map.put("message","接收成功");
//            }catch (Exception e){
//                map.put("code",-1);
//                map.put("message",e.getMessage());
//            }finally {
//
//
//                JintieLogsMapper.insert(logs);
//            }
//
//        return map;
//    }
//


    /**
     * json数据接收接口
     *
     * @param data
     * @return
     */
    @PostMapping(value = "dataResult")
    @ResponseBody
    public Map dataResult(HttpServletRequest request,String data, String code, String message, String serviceNo) {
        OcrUtil util = new OcrUtil();
        JintieLogs logs =  new JintieLogs();
        logs.setUrl("/kwe/ocr/dataResult");
        logs.setIp(HttpUtil.getIpAddr(request));
        logs.setMethod(serviceNo);
        logs.setParameter(data);
        logs.setStartTime(new Date());
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
                if (ywinfo.getType() == 0) {
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
                        /**
                         * 发票
                         */
                        JSONArray FpJSONArray = jsonObject.getJSONArray("FPList");
                        if (FpJSONArray != null) {
                            for (int i = 0; i < FpJSONArray.size(); i++) {
                                JSONObject fpJSONObject = FpJSONArray.getJSONObject(i);
                                String FPNo = OcrObjectUtils.getString(fpJSONObject, "FPNo");
                                String FPTermType = OcrObjectUtils.getString(fpJSONObject, "FPTermType");
                                String FPModeOfTransport = OcrObjectUtils.getString(fpJSONObject, "FPModeOfTransport");
                                String FPFrom = OcrObjectUtils.getString(fpJSONObject, "FPFrom");
                                String FPTo = OcrObjectUtils.getString(fpJSONObject, "FPTo");
                                String FPCOO = OcrObjectUtils.getString(fpJSONObject, "FPCOO");
                                String FPCurrency = OcrObjectUtils.getString(fpJSONObject, "FPCurrency");
                                String FPSupName = OcrObjectUtils.getString(fpJSONObject, "FPSupName");
                                String FPQTY = OcrObjectUtils.getString(fpJSONObject, "FPQTY");
                                String FPGW = OcrObjectUtils.getString(fpJSONObject, "FPGW");
                                String FPTotalNW = OcrObjectUtils.getString(fpJSONObject, "FPTotalNW");
                                String FPTotalGW = OcrObjectUtils.getString(fpJSONObject, "FPTotalGW");
                                String FPGdsUnit = OcrObjectUtils.getString(fpJSONObject, "FPGdsUnit");
                                String aaa= "";
                                Map fpMap = new HashMap();
                                formatData("FPNo",FPNo,fpMap);
                                formatData("FPTermType",FPTermType,fpMap);
                                formatData("FPModeOfTransport",FPModeOfTransport,fpMap);
                                formatData("FPFrom",FPFrom,fpMap);
                                formatData("FPTo",FPTo,fpMap);
                                formatData("FPCOO",FPCOO,fpMap);
                                formatData("FPCurrency",FPCurrency,fpMap);
                                formatData("FPSupName",FPSupName,fpMap);
                                formatData("FPQTY",FPQTY,fpMap);
                                formatData("FPGW",FPGW,fpMap);
                                formatData("FPTotalNW",FPTotalNW,fpMap);
                                formatData("FPTotalGW",FPTotalGW,fpMap);
                                formatData("FPGdsUnit",FPGdsUnit,fpMap);

                                /**
                                 * 发票结构体
                                 */
                                JSONArray FPCommodity = fpJSONObject.getJSONArray("FPCommodity");
                                List<Map> FPCommodityList= new ArrayList();
                                if(FPCommodity != null){
                                    for (int j = 0; j < FPCommodity.size(); j++) {
                                        JSONObject FPCommodityJsonObject = FPCommodity.getJSONObject(j);
                                        String FPCommodity_ItemNo = OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_ItemNo");
                                        String FPCommodity_SN = OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_SN");
                                        String FPCommodity_PO = OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_PO");
                                        String FPCommodity_PartNumber = OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_PartNumber");
                                        String FPCommodity_GdsPrice = OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_GdsPrice");
                                        String FPCommodity_GdsQty1 = OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_GdsQty1");
                                        String FPCommodity_GdsUnit1 = OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_GdsUnit1");
                                        String FPCommodity_GdsAmount = OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_GdsAmount");
                                        String FPCommodity_Currency = OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_Currency");
                                        String FPCommodity_COO = OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_COO");
                                        String FPCommodity_GdsNW = OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_GdsNW");
                                        String FPCommodity_GdsGW = OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_GdsGW");
                                        String FPCommodity_GdsMeasurement = OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_GdsMeasurement");
                                        String FPCommodity_EndUserNo = OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_EndUserNo");
                                        String FPCommodity_OrderNo = OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_OrderNo");
                                        String FPCommodity_CPN = OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_CPN");
                                        String FPCommodity_EndUser = OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_EndUser");
                                        String FPCommodity_So = OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_So");
                                        String FPCommodity_MOQ = OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_MOQ");
                                        String FPCommodity_ReMarks = OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_ReMarks");
                                        String FPCommodity_EndUser2 = OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_EndUser2");
                                        String FPCommodity_EndUser3 = OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_EndUser3");
                                        String FPCommodity_ShipmentNo = OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_ShipmentNo");
                                        String FPCommodity_Dept = OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_Dept");
                                        String FPCommodity_DN = OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_DN");
                                        String FPCommodity_GdsCtnQty = OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_GdsCtnQty");
                                        String FPCommodity_InProductiondate = OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_InProductiondate");
                                        Map fpCommodityMap = new HashMap();
                                        formatData( "FPCommodity_GdsQty1",CleaningString(ywinfo,FPCommodity_GdsQty1),fpCommodityMap);
                                        formatData("FPCommodity_ItemNo",CleaningString(ywinfo.getYwNo(),FPCommodity_ItemNo),fpCommodityMap);
                                        formatData( "FPCommodity_GdsPrice",CleaningString(ywinfo,FPCommodity_GdsPrice),fpCommodityMap);
                                        formatData( "FPCommodity_GdsAmount",CleaningString(ywinfo,FPCommodity_GdsAmount),fpCommodityMap);
                                        formatData( "FPCommodity_GdsGW",CleaningString(ywinfo,FPCommodity_GdsGW),fpCommodityMap);
                                        formatData( "FPCommodity_GdsNW",CleaningString(ywinfo,FPCommodity_GdsNW),fpCommodityMap);
                                        formatData( "FPCommodity_PartNumber",CleaningSC(FPCommodity_PartNumber),fpCommodityMap);
                                        formatData( "FPCommodity_SN",FPCommodity_SN,fpCommodityMap);
                                        formatData( "FPCommodity_PO",FPCommodity_PO,fpCommodityMap);
                                        formatData( "FPCommodity_GdsUnit1",FPCommodity_GdsUnit1,fpCommodityMap);
                                        formatData( "FPCommodity_Currency",FPCommodity_Currency,fpCommodityMap);
                                        formatData( "FPCommodity_COO",FPCommodity_COO,fpCommodityMap);
                                        formatData( "FPCommodity_GdsMeasurement",FPCommodity_GdsMeasurement,fpCommodityMap);
                                        formatData( "FPCommodity_EndUserNo",FPCommodity_EndUserNo,fpCommodityMap);
                                        formatData( "FPCommodity_OrderNo",FPCommodity_OrderNo,fpCommodityMap);
                                        formatData( "FPCommodity_CPN",FPCommodity_CPN,fpCommodityMap);
                                        formatData( "FPCommodity_EndUser",FPCommodity_EndUser,fpCommodityMap);
                                        formatData( "FPCommodity_So",FPCommodity_So,fpCommodityMap);
                                        formatData( "FPCommodity_MOQ",FPCommodity_MOQ,fpCommodityMap);
                                        formatData( "FPCommodity_ReMarks",FPCommodity_ReMarks,fpCommodityMap);
                                        formatData( "FPCommodity_EndUser2",FPCommodity_EndUser2,fpCommodityMap);
                                        formatData( "FPCommodity_EndUser3",FPCommodity_EndUser3,fpCommodityMap);
                                        formatData( "FPCommodity_ShipmentNo",FPCommodity_ShipmentNo,fpCommodityMap);
                                        formatData( "FPCommodity_Dept",FPCommodity_Dept,fpCommodityMap);
                                        formatData( "FPCommodity_DN",FPCommodity_DN,fpCommodityMap);
                                        formatData( "FPCommodity_GdsCtnQty",FPCommodity_GdsCtnQty,fpCommodityMap);
                                        formatData( "FPCommodity_InProductiondate",FPCommodity_InProductiondate,fpCommodityMap);


                                        /**
                                         * 发票结构体2
                                         */
                                        JSONArray FPCommodity2 = FPCommodityJsonObject.getJSONArray("FPCommodity_Attach");
                                        List<Map> FPCommodity2List= new ArrayList();
                                        if(FPCommodity2 != null){
                                            for (int n = 0; n < FPCommodity2.size(); n++) {
                                                JSONObject FPCommodity2JsonObject = FPCommodity2.getJSONObject(n);
                                                String FPCommodity_Attach_BatchQty = OcrObjectUtils.getString(FPCommodity2JsonObject,"FPCommodity_Attach_BatchQty");
                                                String FPCommodity_Attach_SupplierNw = OcrObjectUtils.getString(FPCommodity2JsonObject,"FPCommodity_Attach_SupplierNw");
                                                String FPCommodity_Attach_SupplierGw = OcrObjectUtils.getString(FPCommodity2JsonObject,"FPCommodity_Attach_SupplierGw");
                                                String FPCommodity_Attach_SupplierAmount = OcrObjectUtils.getString(FPCommodity2JsonObject,"FPCommodity_Attach_SupplierAmount ");
                                                String FPCommodity_Attach_BatchNo = OcrObjectUtils.getString(FPCommodity2JsonObject,"FPCommodity_Attach_BatchNo");
                                                String FPCommodity_Attach_CtnNo = OcrObjectUtils.getString(FPCommodity2JsonObject,"FPCommodity_Attach_CtnNo");
                                                String FPCommodity_Attach_EngineNo = OcrObjectUtils.getString(FPCommodity2JsonObject,"FPCommodity_Attach_EngineNo");
                                                String FPCommodity_Attach_LotNo = OcrObjectUtils.getString(FPCommodity2JsonObject,"FPCommodity_Attach_LotNo");
                                                String FPCommodity_Attach_SupplierGoodQty = OcrObjectUtils.getString(FPCommodity2JsonObject,"FPCommodity_Attach_SupplierGoodQty");
                                                if(util.checkNull(FPCommodity_Attach_CtnNo) && FPCommodity_Attach_CtnNo.contains(".0")){
                                                    FPCommodity_Attach_CtnNo = FPCommodity_Attach_CtnNo.replace(".0","");
                                                }
                                                Map fpCommodity2Map = new HashMap();
                                                formatData( "FPCommodity_Attach_CtnNo",FPCommodity_Attach_CtnNo,fpCommodity2Map);
                                                formatData( "FPCommodity_Attach_BatchQty",CleaningString(ywinfo,FPCommodity_Attach_BatchQty),fpCommodity2Map);
                                                formatData( "FPCommodity_Attach_SupplierNw",CleaningString(ywinfo,FPCommodity_Attach_SupplierNw),fpCommodity2Map);
                                                formatData( "FPCommodity_Attach_SupplierGw",CleaningString(ywinfo,FPCommodity_Attach_SupplierGw),fpCommodity2Map);
                                                formatData( "FPCommodity_Attach_SupplierGoodQty",CleaningString(ywinfo,FPCommodity_Attach_SupplierGoodQty),fpCommodity2Map);
                                                formatData( "FPCommodity_Attach_SupplierAmount",FPCommodity_Attach_SupplierAmount,fpCommodity2Map);
                                                formatData( "FPCommodity_Attach_BatchNo",FPCommodity_Attach_BatchNo,fpCommodity2Map);
                                                formatData( "FPCommodity_Attach_EngineNo",FPCommodity_Attach_EngineNo,fpCommodity2Map);
                                                formatData( "FPCommodity_Attach_LotNo",FPCommodity_Attach_LotNo,fpCommodity2Map);
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
                                String XDShipper = OcrObjectUtils.getString(xdJSONObject, "XDShipper");
                                String XDFrom = OcrObjectUtils.getString(xdJSONObject, "XDFrom");
                                String XDTo = OcrObjectUtils.getString(xdJSONObject, "XDTo");
                                String XDTermType = OcrObjectUtils.getString(xdJSONObject, "XDTermType");
                                String XDModeOfTransport = OcrObjectUtils.getString(xdJSONObject, "XDModeOfTransport");
                                String XDCOO = OcrObjectUtils.getString(xdJSONObject, "XDCOO");
                                String XDSupName = OcrObjectUtils.getString(xdJSONObject, "XDSupName");
                                String XDQTY = OcrObjectUtils.getString(xdJSONObject, "XDQTY");
                                String XDGW = OcrObjectUtils.getString(xdJSONObject, "XDGW");
                                String XDInviceno = OcrObjectUtils.getString(xdJSONObject, "XDInviceno");
                                Map XdMap = new HashMap();
                                formatData( "XDShipper",XDShipper,XdMap);
                                formatData( "XDFrom",XDFrom,XdMap);
                                formatData( "XDTo",XDTo,XdMap);
                                formatData( "XDTermType",XDTermType,XdMap);
                                formatData( "XDModeOfTransport",XDModeOfTransport,XdMap);
                                formatData( "XDCOO",XDCOO,XdMap);
                                formatData( "XDSupName",XDSupName,XdMap);
                                formatData( "XDQTY",XDQTY,XdMap);
                                formatData( "XDGW",XDGW,XdMap);
                                formatData( "XDInviceno",XDInviceno,XdMap);
                                /**
                                 * 箱单结构体
                                 */
                                JSONArray XDCommodity = xdJSONObject.getJSONArray("XDCommodity");
                                List<Map> XDCommodityList = new ArrayList();
                                if(XDCommodity != null){
                                    for (int j = 0; j < XDCommodity.size(); j++) {
                                        JSONObject XDCommodityJsonObject = XDCommodity.getJSONObject(j);
                                        String XDCommodity_ItemNo = OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_ItemNo");
                                        String XDCommodity_PO = OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_PO");
                                        String XDCommodity_PartNumber = OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_PartNumber");
                                        String XDCommodity_SN = OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_SN");
                                        String XDCommodity_GdsQty1 = OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_GdsQty1");
                                        String XDCommodity_GdsUnit1 = OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_GdsUnit1");
                                        String XDCommodity_GdsNW = OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_GdsNW");
                                        String XDCommodity_GdsGW = OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_GdsGW");
                                        String XDCommodity_GdsMeasurement = OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_GdsMeasurement");
                                        String XDCommodity_COO = OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_COO");
                                        String XDCommodity_EndUserNo = OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_EndUserNo");
                                        String XDCommodity_OrderNo = OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_OrderNo");
                                        String XDCommodity_CPN = OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_CPN");
                                        String XDCommodity_EndUser = OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_EndUser");
                                        String XDCommodity_So = OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_So");
                                        String XDCommodity_MOQ = OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_MOQ");
                                        String XDCommodity_ReMarks = OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_ReMarks");
                                        String XDCommodity_EndUser2 = OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_EndUser2");
                                        String XDCommodity_EndUser3 = OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_EndUser3");
                                        String XDCommodity_ShipmentNo = OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_ShipmentNo");
                                        String XDCommodity_Dept = OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_Dept");
                                        String XDCommodity_DN = OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_DN");
                                        String XDCommodity_GdsCtnQty = OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_GdsCtnQty");
                                        String XDCommodity_InProductiondate = OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_InProductiondate");
                                        String XDCommodity_ContainerNo = OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_ContainerNo");
                                        Map XdCommodityMap = new HashMap();
                                        formatData( "XDCommodity_ItemNo",CleaningString(ywinfo.getYwNo(),XDCommodity_ItemNo),XdCommodityMap);
                                        formatData( "XDCommodity_GdsQty1",CleaningString(ywinfo,XDCommodity_GdsQty1),XdCommodityMap);
                                        formatData( "XDCommodity_GdsNW",CleaningString(ywinfo,XDCommodity_GdsNW),XdCommodityMap);
                                        formatData( "XDCommodity_GdsGW",CleaningString(ywinfo,XDCommodity_GdsGW),XdCommodityMap);
                                        formatData( "XDCommodity_GdsMeasurement",CleaningString(ywinfo,XDCommodity_GdsMeasurement),XdCommodityMap);
                                        formatData( "XDCommodity_PO",XDCommodity_PO,XdCommodityMap);
                                        formatData( "XDCommodity_PartNumber",XDCommodity_PartNumber,XdCommodityMap);
                                        formatData( "XDCommodity_SN",XDCommodity_SN,XdCommodityMap);
                                        formatData( "XDCommodity_GdsUnit1",XDCommodity_GdsUnit1,XdCommodityMap);
                                        formatData( "XDCommodity_COO",XDCommodity_COO,XdCommodityMap);
                                        formatData( "XDCommodity_EndUserNo",XDCommodity_EndUserNo,XdCommodityMap);
                                        formatData( "XDCommodity_OrderNo",XDCommodity_OrderNo,XdCommodityMap);
                                        formatData( "XDCommodity_CPN",XDCommodity_CPN,XdCommodityMap);
                                        formatData( "XDCommodity_EndUser",XDCommodity_EndUser,XdCommodityMap);
                                        formatData( "XDCommodity_So",XDCommodity_So,XdCommodityMap);
                                        formatData( "XDCommodity_MOQ",XDCommodity_MOQ,XdCommodityMap);
                                        formatData( "XDCommodity_ReMarks",XDCommodity_ReMarks,XdCommodityMap);
                                        formatData( "XDCommodity_EndUser2",XDCommodity_EndUser2,XdCommodityMap);
                                        formatData( "XDCommodity_EndUser3",XDCommodity_EndUser3,XdCommodityMap);
                                        formatData( "XDCommodity_ShipmentNo",XDCommodity_ShipmentNo,XdCommodityMap);
                                        formatData( "XDCommodity_Dept",XDCommodity_Dept,XdCommodityMap);
                                        formatData( "XDCommodity_DN",XDCommodity_DN,XdCommodityMap);
                                        formatData( "XDCommodity_GdsCtnQty",XDCommodity_GdsCtnQty,XdCommodityMap);
                                        formatData( "XDCommodity_InProductiondate",XDCommodity_InProductiondate,XdCommodityMap);
                                        formatData( "XDCommodity_ContainerNo",XDCommodity_ContainerNo,XdCommodityMap);
                                        //箱单结构体2
                                        JSONArray XDCommodity2 = XDCommodityJsonObject.getJSONArray("XDCommodity_Attach");
                                        List<Map> XDCommodity2List= new ArrayList();
                                        if(XDCommodity2!= null){
                                            for (int n = 0; n < XDCommodity2.size(); n++) {
                                                JSONObject XDCommodity2JsonObject = XDCommodity2.getJSONObject(n);
                                                String XDCommodity_Attach_BatchQty = OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_BatchQty");
                                                String XDCommodity_Attach_SupplierNw = OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_SupplierNw");
                                                String XDCommodity_Attach_SupplierGw = OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_SupplierGw");
                                                String XDCommodity_Attach_SupplierAmount = OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_SupplierAmount");
                                                String XDCommodity_Attach_BatchNo = OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_BatchNo");
                                                String XDCommodity_Attach_CtnNo = OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_CtnNo");
                                                String XDCommodity_Attach_EngineNo = OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_EngineNo");
                                                String XDCommodity_Attach_LotNo = OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_LotNo");
                                                String XDCommodity_Attach_SupplierGoodQty = OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_SupplierGoodQty");
                                                String XDCommodity_Attach_SupplierTotalQty = OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_SupplierTotalQty");
                                                String XDCommodity_Attach_SupplierNumber = OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_SupplierNumber");
                                                if(util.checkNull(XDCommodity_Attach_CtnNo) && XDCommodity_Attach_CtnNo.contains(".0")){
                                                    XDCommodity_Attach_CtnNo = XDCommodity_Attach_CtnNo.replace(".0","");
                                                }
                                                Map XdCommodity2Map = new HashMap();
                                                formatData( "XDCommodity_Attach_CtnNo",XDCommodity_Attach_CtnNo,XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_BatchQty",CleaningString(ywinfo,XDCommodity_Attach_BatchQty),XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_SupplierNw",CleaningString(ywinfo,XDCommodity_Attach_SupplierNw),XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_SupplierGw",CleaningString(ywinfo,XDCommodity_Attach_SupplierGw),XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_SupplierAmount",CleaningString(ywinfo,XDCommodity_Attach_SupplierAmount),XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_SupplierGoodQty",CleaningString(ywinfo,XDCommodity_Attach_SupplierGoodQty),XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_BatchNo",XDCommodity_Attach_BatchNo,XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_EngineNo",XDCommodity_Attach_EngineNo,XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_LotNo",XDCommodity_Attach_LotNo,XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_SupplierTotalQty",XDCommodity_Attach_SupplierTotalQty,XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_SupplierNumber",XDCommodity_Attach_SupplierNumber,XdCommodity2Map);
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
                                String HTShipper = OcrObjectUtils.getString(htJSONObject, "HTShipper");
                                String HTFrom = OcrObjectUtils.getString(htJSONObject, "HTFrom");
                                String HTTo = OcrObjectUtils.getString(htJSONObject, "HTTo");
                                String HTInviceno = OcrObjectUtils.getString(htJSONObject, "HTInviceno");
                                String HTCurrency = OcrObjectUtils.getString(htJSONObject, "HTCurrency");
                                String HTCOO = OcrObjectUtils.getString(htJSONObject, "HTCOO");
                                String HTTermType = OcrObjectUtils.getString(htJSONObject, "HTTermType");
                                String HTCtnQty = OcrObjectUtils.getString(htJSONObject, "HTCtnQty");
                                String HTTotalGW = OcrObjectUtils.getString(htJSONObject, "HTTotalGW");
                                Map htMap = new HashMap();
                                formatData("HTShipper",HTShipper,htMap);
                                formatData("HTFrom",HTFrom,htMap);
                                formatData("HTTo",HTTo,htMap);
                                formatData("HTInviceno",HTInviceno,htMap);
                                formatData("HTCurrency",HTCurrency,htMap);
                                formatData("HTCOO",HTCOO,htMap);
                                formatData("HTTermType",HTTermType,htMap);
                                formatData("HTCtnQty",HTCtnQty,htMap);
                                formatData("HTTotalGW",HTTotalGW,htMap);
                                //合同结构体
                                JSONArray HtCommodity = htJSONObject.getJSONArray("HtCommodity");
                                List<Map> HtCommodityList = new ArrayList<>();
                                if(HtCommodity != null){
                                    for (int j = 0; j < HtCommodity.size(); j++) {
                                        JSONObject HTCommodityJsonObject = HtCommodity.getJSONObject(j);
                                        String HTCommodity_ItemNo = OcrObjectUtils.getString(HTCommodityJsonObject,"HTCommodity_ItemNo");
                                        String HTCommodity_PartNumber = OcrObjectUtils.getString(HTCommodityJsonObject,"HTCommodity_PartNumber");
                                        String HTCommodity_GdsQty1 = OcrObjectUtils.getString(HTCommodityJsonObject,"HTCommodity_GdsQty1");
                                        String HECommodity_GdsUnit1 = OcrObjectUtils.getString(HTCommodityJsonObject,"HECommodity_GdsUnit1");
                                        String HTCommodity_GdsPrice = OcrObjectUtils.getString(HTCommodityJsonObject,"HTCommodity_GdsPrice");
                                        String HTCommodity_GdsAmount = OcrObjectUtils.getString(HTCommodityJsonObject,"HTCommodity_GdsAmount");
                                        String HTCommodity_Currency = OcrObjectUtils.getString(HTCommodityJsonObject,"HTCommodity_Currency");
                                        String HTCommodity_COO = OcrObjectUtils.getString(HTCommodityJsonObject,"HTCommodity_COO");
                                        Map htCommodityMap = new HashMap();
                                        formatData("HTCommodity_ItemNo",HTCommodity_ItemNo,htCommodityMap);
                                        formatData("HTCommodity_PartNumber",HTCommodity_PartNumber,htCommodityMap);
                                        formatData("HTCommodity_GdsQty1",HTCommodity_GdsQty1,htCommodityMap);
                                        formatData("HECommodity_GdsUnit1",HECommodity_GdsUnit1,htCommodityMap);
                                        formatData("HTCommodity_GdsPrice",HTCommodity_GdsPrice,htCommodityMap);
                                        formatData("HTCommodity_GdsAmount",HTCommodity_GdsAmount,htCommodityMap);
                                        formatData("HTCommodity_Currency",HTCommodity_Currency,htCommodityMap);
                                        formatData("HTCommodity_COO",HTCommodity_COO,htCommodityMap);
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
                    /**
                     * 出仓对应字段处理
                     */
                    try {
                        /**
                         * 空运单
                         */
//                        JSONArray AwJSONArray = jsonObject.getJSONArray("AWList");
//                        if (AwJSONArray != null) {
//                            for (int i = 0; i < AwJSONArray.size(); i++) {
//                                JSONObject awJSONArray = AwJSONArray.getJSONObject(i);
//                                    String AWMawb = OcrObjectUtils.getString(awJSONArray, "AWMawb");
//                                    String AWHawb = OcrObjectUtils.getString(awJSONArray, "AWHawb");
//                                    String AWDestinationAirport = OcrObjectUtils.getString(awJSONArray, "AWDestinationAirport");
//                                    String AWModeOfTransport = OcrObjectUtils.getString(awJSONArray, "AWModeOfTransport");
//                                    Map awMap = new HashMap();
//                                    formatData("AWMawb",AWMawb,awMap);
//                                    formatData("AWHawb",AWHawb,awMap);
//                                    formatData("AWDestinationAirport",AWDestinationAirport,awMap);
//                                    formatData("AWModeOfTransport",AWModeOfTransport,awMap);
//                                    if(!awMap.isEmpty()){
//                                        AWList.add(awMap);
//                                    }
//                            }
//                        }
                        /**
                         * 通知书
                         */
//                        JSONArray AnJSONArray = jsonObject.getJSONArray("CLList");
//                        if (AnJSONArray != null) {
//                            for (int i = 0; i < AnJSONArray.size(); i++) {
//                                JSONObject anJSONArray = AnJSONArray.getJSONObject(i);
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
                                String FPNo = OcrObjectUtils.getString(fpJSONObject, "FPNo");
                                String FPTermType = OcrObjectUtils.getString(fpJSONObject, "FPTermType");
                                String FPModeOfTransport = OcrObjectUtils.getString(fpJSONObject, "FPModeOfTransport");
                                String FPFrom = OcrObjectUtils.getString(fpJSONObject, "FPFrom");
                                String FPTo = OcrObjectUtils.getString(fpJSONObject, "FPTo");
                                String FPCOO = OcrObjectUtils.getString(fpJSONObject, "FPCOO");
                                String FPCurrency = OcrObjectUtils.getString(fpJSONObject, "FPCurrency");
                                String FPSupName = OcrObjectUtils.getString(fpJSONObject, "FPSupName");
                                String FPGdsUnit = OcrObjectUtils.getString(fpJSONObject, "FPGdsUnit");
                                Map fpMap = new HashMap();
                                formatData("FPNo",FPNo,fpMap);
                                formatData("FPTermType",FPTermType,fpMap);
                                formatData("FPModeOfTransport",FPModeOfTransport,fpMap);
                                formatData("FPFrom",FPFrom,fpMap);
                                formatData("FPTo",FPTo,fpMap);
                                formatData("FPCOO",FPCOO,fpMap);
                                formatData("FPCurrency",FPCurrency,fpMap);
                                formatData("FPSupName",FPSupName,fpMap);
                                formatData("FPGdsUnit",FPGdsUnit,fpMap);
                                //发票结构体
                                JSONArray FPCommodity = fpJSONObject.getJSONArray("FPCommodity");
                                List<Map> FPCommodityList= new ArrayList();
                                if(FPCommodity != null){
                                    for (int j = 0; j < FPCommodity.size(); j++) {
                                        JSONObject FPCommodityJsonObject = FPCommodity.getJSONObject(j);
                                        String FPCommodity_ItemNo = OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_ItemNo");
                                        String FPCommodity_SN = OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_SN");
                                        String FPCommodity_PartNumber = OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_PartNumber");
                                        String FPCommodity_GdsPrice = OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_GdsPrice");
                                        String FPCommodity_GdsQty1 = OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_GdsQty1");
                                        String FPCommodity_GdsUnit1 = OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_GdsUnit1");
                                        String FPCommodity_GdsAmount = OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_GdsAmount");
                                        String FPCommodity_Currency = OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_Currency");
                                        String FPCommodity_COO = OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_COO");
                                        String FPCommodity_CPN = OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_CPN");
                                        String FPCommodity_EndUser = OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_EndUser");
                                        String FPCommodity_So = OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_So");
                                        String FPCommodity_DN = OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_DN");
                                        String FPCommodity_GdsCtnQty = OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_GdsCtnQty");
                                        String FPCommodity_InWorkNo = OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_InWorkNo");
                                        String FPCommodity_InFPNo = OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_InFPNo");
                                        String FPCommodity_InWayBill = OcrObjectUtils.getString(FPCommodityJsonObject,"FPCommodity_InWayBill");
                                        Map fpCommodityMap = new HashMap();
                                        formatData("FPCommodity_ItemNo",CleaningString(ywinfo.getYwNo(),FPCommodity_ItemNo),fpCommodityMap);
                                        formatData( "FPCommodity_GdsPrice",CleaningString(ywinfo,FPCommodity_GdsPrice),fpCommodityMap);
                                        formatData( "FPCommodity_GdsQty1",CleaningString(ywinfo,FPCommodity_GdsQty1),fpCommodityMap);
                                        formatData( "FPCommodity_GdsAmount",CleaningString(ywinfo,FPCommodity_GdsAmount),fpCommodityMap);
                                        formatData("FPCommodity_SN",FPCommodity_SN,fpCommodityMap);
                                        formatData( "FPCommodity_PartNumber",CleaningSC(FPCommodity_PartNumber),fpCommodityMap);
                                        formatData( "FPCommodity_GdsUnit1",FPCommodity_GdsUnit1,fpCommodityMap);
                                        formatData( "FPCommodity_Currency",FPCommodity_Currency,fpCommodityMap);
                                        formatData( "FPCommodity_COO",FPCommodity_COO,fpCommodityMap);
                                        formatData( "FPCommodity_CPN",FPCommodity_CPN,fpCommodityMap);
                                        formatData( "FPCommodity_EndUser",FPCommodity_EndUser,fpCommodityMap);
                                        formatData( "FPCommodity_So",FPCommodity_So,fpCommodityMap);
                                        formatData( "FPCommodity_DN",FPCommodity_DN,fpCommodityMap);
                                        formatData( "FPCommodity_GdsCtnQty",FPCommodity_GdsCtnQty,fpCommodityMap);
                                        formatData( "FPCommodity_InWorkNo",FPCommodity_InWorkNo,fpCommodityMap);
                                        formatData( "FPCommodity_InFPNo",FPCommodity_InFPNo,fpCommodityMap);
                                        formatData( "FPCommodity_InWayBill",FPCommodity_InWayBill,fpCommodityMap);

                                        //发票结构体2
                                        JSONArray FPCommodity2 = FPCommodityJsonObject.getJSONArray("FPCommodity_Attach");
                                        List<Map> FPCommodity2List= new ArrayList();
                                        if(FPCommodity2 != null){
                                            for (int n = 0; n < FPCommodity2.size(); n++) {
                                                JSONObject FPCommodity2JsonObject = FPCommodity2.getJSONObject(n);
                                                String FPCommodity_Attach_BatchQty = OcrObjectUtils.getString(FPCommodity2JsonObject,"FPCommodity_Attach_BatchQty");
                                                String FPCommodity_Attach_SupplierNw = OcrObjectUtils.getString(FPCommodity2JsonObject,"FPCommodity_Attach_SupplierNw");
                                                String FPCommodity_Attach_SupplierGw = OcrObjectUtils.getString(FPCommodity2JsonObject,"FPCommodity_Attach_SupplierGw");
                                                String FPCommodity_Attach_SupplierAmount = OcrObjectUtils.getString(FPCommodity2JsonObject,"FPCommodity_Attach_SupplierAmount");
                                                String FPCommodity_Attach_BatchNo = OcrObjectUtils.getString(FPCommodity2JsonObject,"FPCommodity_Attach_BatchNo");
                                                String FPCommodity_Attach_CtnNo = OcrObjectUtils.getString(FPCommodity2JsonObject,"FPCommodity_Attach_CtnNo");
                                                String FPCommodity_Attach_EngineNo = OcrObjectUtils.getString(FPCommodity2JsonObject,"FPCommodity_Attach_EngineNo");
                                                String FPCommodity_Attach_LotNo = OcrObjectUtils.getString(FPCommodity2JsonObject,"FPCommodity_Attach_LotNo");
                                                String FPCommodity_Attach_SupplierGoodQty = OcrObjectUtils.getString(FPCommodity2JsonObject,"FPCommodity_Attach_SupplierGoodQty");
                                                if(util.checkNull(FPCommodity_Attach_CtnNo) && FPCommodity_Attach_CtnNo.contains(".0")){
                                                    FPCommodity_Attach_CtnNo = FPCommodity_Attach_CtnNo.replace(".0","");
                                                }
                                                Map fpCommodity2Map = new HashMap();
                                                formatData( "FPCommodity_Attach_CtnNo",CleaningString(ywinfo.getYwNo(),FPCommodity_Attach_CtnNo),fpCommodity2Map);
                                                formatData( "FPCommodity_Attach_BatchQty",CleaningString(ywinfo,FPCommodity_Attach_BatchQty),fpCommodity2Map);
                                                formatData( "FPCommodity_Attach_SupplierNw",CleaningString(ywinfo,FPCommodity_Attach_SupplierNw),fpCommodity2Map);
                                                formatData( "FPCommodity_Attach_SupplierGw",CleaningString(ywinfo,FPCommodity_Attach_SupplierGw),fpCommodity2Map);
                                                formatData( "FPCommodity_Attach_SupplierAmount",CleaningString(ywinfo,FPCommodity_Attach_SupplierAmount),fpCommodity2Map);
                                                formatData( "FPCommodity_Attach_SupplierGoodQty",CleaningString(ywinfo,FPCommodity_Attach_SupplierGoodQty),fpCommodity2Map);
                                                formatData( "FPCommodity_Attach_BatchNo",FPCommodity_Attach_BatchNo,fpCommodity2Map);
                                                formatData( "FPCommodity_Attach_EngineNo",FPCommodity_Attach_EngineNo,fpCommodity2Map);
                                                formatData( "FPCommodity_Attach_LotNo",FPCommodity_Attach_LotNo,fpCommodity2Map);
                                                if(!fpCommodity2Map.isEmpty()){
                                                    FPCommodity2List.add(fpCommodity2Map);
                                                }
                                            }
                                        }
                                        if(FPCommodity2List != null && FPCommodity2List.size() > 0 ){
                                            fpCommodityMap.put("FPCommodity2List",FPCommodity2List);
                                        }
                                        if(!fpCommodityMap.isEmpty()){
                                            FPCommodityList.add(fpCommodityMap);
                                        }
                                    }
                                }
                                if(FPCommodityList != null && FPCommodityList.size() > 0 ){
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
                                String XDConsignee = OcrObjectUtils.getString(xdJSONObject, "XDConsignee");
                                String XDFrom = OcrObjectUtils.getString(xdJSONObject, "XDFrom");
                                String XDTo = OcrObjectUtils.getString(xdJSONObject, "XDTo");
                                String XDTermType = OcrObjectUtils.getString(xdJSONObject, "XDTermType");
                                String XDModeOfTransport = OcrObjectUtils.getString(xdJSONObject, "XDModeOfTransport");
                                String XDCOO = OcrObjectUtils.getString(xdJSONObject, "XDCOO");
                                String XDSupName = OcrObjectUtils.getString(xdJSONObject, "XDSupName");
                                String XDInviceno = OcrObjectUtils.getString(xdJSONObject, "XDInviceno");
                                Map XdMap = new HashMap();
                                formatData( "XDConsignee",XDConsignee,XdMap);
                                formatData( "XDFrom",XDFrom,XdMap);
                                formatData( "XDTo",XDTo,XdMap);
                                formatData( "XDTermType",XDTermType,XdMap);
                                formatData( "XDModeOfTransport",XDModeOfTransport,XdMap);
                                formatData( "XDCOO",XDCOO,XdMap);
                                formatData( "XDSupName",XDSupName,XdMap);
                                formatData( "XDInviceno",XDInviceno,XdMap);

                                //箱单结构体
                                JSONArray XDCommodity = xdJSONObject.getJSONArray("XDCommodity");
                                List<Map> XDCommodityList = new ArrayList();
                                if(XDCommodity!=null){
                                    for (int j = 0; j < XDCommodity.size(); j++) {
                                        JSONObject XDCommodityJsonObject = XDCommodity.getJSONObject(j);
                                        String XDCommodity_ItemNo = OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_ItemNo");
                                        String XDCommodity_PartNumber = OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_PartNumber");
                                        String XDCommodity_SN = OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_SN");
                                        String XDCommodity_GdsQty1 = OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_GdsQty1");
                                        String XDCommodity_GdsUnit1 = OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_GdsUnit1");
                                        String XDCommodity_COO = OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_COO");
                                        String XDCommodity_CPN = OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_CPN");
                                        String XDCommodity_EndUser = OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_EndUser");
                                        String XDCommodity_So = OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_So");
                                        String XDCommodity_DN = OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_DN");
                                        String XDCommodity_GdsCtnQty = OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_GdsCtnQty");
                                        String XDCommodity_InWorkNo = OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_InWorkNo");
                                        String XDCommodity_InXDNo = OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_InXDNo");
                                        String XDCommodity_InWayBill = OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_InWayBill");
                                        String XDCommodity_GdsGW = OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_GdsGW");
                                        String XDCommodity_GdsNW = OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_GdsNW");
                                        String XDCommodity_ContainerNo = OcrObjectUtils.getString(XDCommodityJsonObject,"XDCommodity_ContainerNo");
                                        Map XdCommodityMap = new HashMap();
                                        formatData( "XDCommodity_ItemNo",CleaningString(ywinfo.getYwNo(),XDCommodity_ItemNo),XdCommodityMap);
                                        formatData( "XDCommodity_GdsQty1",CleaningString(ywinfo,XDCommodity_GdsQty1),XdCommodityMap);
                                        formatData( "XDCommodity_GdsGW",CleaningString(ywinfo,XDCommodity_GdsGW),XdCommodityMap);
                                        formatData( "XDCommodity_GdsNW",CleaningString(ywinfo,XDCommodity_GdsNW),XdCommodityMap);
                                        formatData( "XDCommodity_PartNumber",XDCommodity_PartNumber,XdCommodityMap);
                                        formatData( "XDCommodity_SN",XDCommodity_SN,XdCommodityMap);
                                        formatData( "XDCommodity_GdsUnit1",XDCommodity_GdsUnit1,XdCommodityMap);
                                        formatData( "XDCommodity_COO",XDCommodity_COO,XdCommodityMap);
                                        formatData( "XDCommodity_CPN",XDCommodity_CPN,XdCommodityMap);
                                        formatData( "XDCommodity_EndUser",XDCommodity_EndUser,XdCommodityMap);
                                        formatData( "XDCommodity_So",XDCommodity_So,XdCommodityMap);
                                        formatData( "XDCommodity_DN",XDCommodity_DN,XdCommodityMap);
                                        formatData( "XDCommodity_GdsCtnQty",XDCommodity_GdsCtnQty,XdCommodityMap);
                                        formatData( "XDCommodity_InWorkNo",XDCommodity_InWorkNo,XdCommodityMap);
                                        formatData( "XDCommodity_InXDNo",XDCommodity_InXDNo,XdCommodityMap);
                                        formatData( "XDCommodity_InWayBill",XDCommodity_InWayBill,XdCommodityMap);
                                        formatData( "XDCommodity_ContainerNo",XDCommodity_ContainerNo,XdCommodityMap);
                                        //箱单结构体2
                                        JSONArray XDCommodity2 = XDCommodityJsonObject.getJSONArray("XDCommodity_Attach");
                                        List<Map> XDCommodity2List= new ArrayList();
                                        if(XDCommodity2!= null){
                                            for (int n = 0; n < XDCommodity2.size(); n++) {
                                                JSONObject XDCommodity2JsonObject = XDCommodity2.getJSONObject(n);
                                                String XDCommodity_Attach_BatchNo = OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_BatchNo");
                                                String XDCommodity_Attach_BatchQty = OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_BatchQty");
                                                String XDCommodity_Attach_SupplierAmount = OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_SupplierAmount");
                                                String XDCommodity_Attach_CtnGdsQty = OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_CtnGdsQty");
                                                String XDCommodity_Attach_EngineNo = OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_EngineNo");
                                                String XDCommodity_Attach_CtnNo = OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_CtnNo");
                                                if(util.checkNull(XDCommodity_Attach_CtnNo) && XDCommodity_Attach_CtnNo.contains(".0")){
                                                    XDCommodity_Attach_CtnNo = XDCommodity_Attach_CtnNo.replace(".0","");
                                                }
                                                String XDCommodity_Attach_SupplierTotalQty = OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_SupplierTotalQty");
                                                String XDCommodity_Attach_SupplierNumber = OcrObjectUtils.getString(XDCommodity2JsonObject,"XDCommodity_Attach_SupplierNumber");
                                                Map XdCommodity2Map = new HashMap();
                                                formatData( "XDCommodity_Attach_CtnNo",CleaningString(ywinfo.getYwNo(),XDCommodity_Attach_CtnNo),XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_BatchQty",CleaningString(ywinfo,XDCommodity_Attach_BatchQty),XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_CtnGdsQty",CleaningString(ywinfo,XDCommodity_Attach_CtnGdsQty),XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_BatchNo",XDCommodity_Attach_BatchNo,XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_SupplierAmount",XDCommodity_Attach_SupplierAmount,XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_EngineNo",XDCommodity_Attach_EngineNo,XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_SupplierTotalQty",XDCommodity_Attach_SupplierTotalQty,XdCommodity2Map);
                                                formatData( "XDCommodity_Attach_SupplierNumber",XDCommodity_Attach_SupplierNumber,XdCommodity2Map);
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
                        //合同
                        JSONArray HtJSONArray = jsonObject.getJSONArray("HTList");
                        if (HtJSONArray != null) {
                            for (int i = 0; i < HtJSONArray.size(); i++) {
                                JSONObject htJSONObject = HtJSONArray.getJSONObject(i);
                                String HTConsignee = OcrObjectUtils.getString(htJSONObject, "HTConsignee");
                                String HTFrom = OcrObjectUtils.getString(htJSONObject, "HTFrom");
                                String HTTo = OcrObjectUtils.getString(htJSONObject, "HTTo");
                                String HTInviceno = OcrObjectUtils.getString(htJSONObject, "HTInviceno");
                                String HTCurrency = OcrObjectUtils.getString(htJSONObject, "HTCurrency");
                                String HTCOO = OcrObjectUtils.getString(htJSONObject, "HTCOO");
                                String HTTermType = OcrObjectUtils.getString(htJSONObject, "HTTermType");
                                String HTCtnQty = OcrObjectUtils.getString(htJSONObject, "HTCtnQty");
                                Map htMap = new HashMap();
                                formatData("HTShipper",HTConsignee,htMap);
                                formatData("HTFrom",HTFrom,htMap);
                                formatData("HTTo",HTTo,htMap);
                                formatData("HTInviceno",HTInviceno,htMap);
                                formatData("HTCurrency",HTCurrency,htMap);
                                formatData("HTCOO",HTCOO,htMap);
                                formatData("HTTermType",HTTermType,htMap);
                                formatData("HTCtnQty",HTCtnQty,htMap);
                                //合同结构体
                                JSONArray HtCommodity = htJSONObject.getJSONArray("HtCommodity");
                                List<Map> HtCommodityList = new ArrayList<>();
                                if(HtCommodity != null){
                                    for (int j = 0; j < HtCommodity.size(); j++) {
                                        JSONObject HTCommodityJsonObject = HtCommodity.getJSONObject(j);
                                        String HTCommodity_ItemNo = OcrObjectUtils.getString(HTCommodityJsonObject,"HTCommodity_ItemNo");
                                        String HTCommodity_PartNumber = OcrObjectUtils.getString(HTCommodityJsonObject,"HTCommodity_PartNumber");
                                        String HTCommodity_GdsQty1 = OcrObjectUtils.getString(HTCommodityJsonObject,"HTCommodity_GdsQty1");
                                        String HECommodity_GdsUnit1 = OcrObjectUtils.getString(HTCommodityJsonObject,"HECommodity_GdsUnit1");
                                        String HTCommodity_GdsPrice = OcrObjectUtils.getString(HTCommodityJsonObject,"HTCommodity_GdsPrice");
                                        String HTCommodity_GdsAmount = OcrObjectUtils.getString(HTCommodityJsonObject,"HTCommodity_GdsAmount");
                                        String HTCommodity_Currency = OcrObjectUtils.getString(HTCommodityJsonObject,"HTCommodity_Currency");
                                        String HTCommodity_COO = OcrObjectUtils.getString(HTCommodityJsonObject,"HTCommodity_COO");
                                        Map htCommodityMap = new HashMap();
                                        formatData("HTCommodity_ItemNo",HTCommodity_ItemNo,htCommodityMap);
                                        formatData("HTCommodity_PartNumber",HTCommodity_PartNumber,htCommodityMap);
                                        formatData("HTCommodity_GdsQty1",HTCommodity_GdsQty1,htCommodityMap);
                                        formatData("HECommodity_GdsUnit1",HECommodity_GdsUnit1,htCommodityMap);
                                        formatData("HTCommodity_GdsPrice",HTCommodity_GdsPrice,htCommodityMap);
                                        formatData("HTCommodity_GdsAmount",HTCommodity_GdsAmount,htCommodityMap);
                                        formatData("HTCommodity_Currency",HTCommodity_Currency,htCommodityMap);
                                        formatData("HTCommodity_COO",HTCommodity_COO,htCommodityMap);
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
                                if(HtCommodityList != null  && HtCommodityList.size() >0 ){
                                    htMap.put("HtCommodityList",HtCommodityList);
                                }
                                if(!htMap.isEmpty()){
                                    HTList.add(htMap);
                                }
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("出错了" + e.toString());
                    }
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

    @RequestMapping("getIP")
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
                logger.warn(ywinfo.getYwNo()+"非法数字："+obj+",原因:"+e.getMessage());
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

    private String CleaningSC(String obj){
        if(checkNull(obj)){
            if(obj.contains("*")){
                obj = obj.replace("*","");
            }
            obj = obj.trim();
        }
        return obj;
    }


}
