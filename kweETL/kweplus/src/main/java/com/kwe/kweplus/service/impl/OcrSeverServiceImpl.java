package com.kwe.kweplus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kwe.kweplus.base.*;
import com.kwe.kweplus.bo.IJintieCommonBo;
import com.kwe.kweplus.dao.*;
import com.kwe.kweplus.modal.*;
import com.kwe.kweplus.service.*;
import com.kwe.kweplus.util.CalendarUtils;
import com.kwe.kweplus.util.ExcelUtil;
import com.kwe.kweplus.util.ocrUtil.OcrUtil;
import com.kwe.kweplus.util.text.MyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class OcrSeverServiceImpl implements OcrSeverService {

    private Logger logger = LoggerFactory.getLogger(OcrSeverServiceImpl.class);

    @Autowired
    com.kwe.kweplus.dao.JintieBaseInputFieldMapper JintieBaseInputFieldMapper;
    @Autowired
    com.kwe.kweplus.dao.JintieCustomFieldMapper JintieCustomFieldMapper;
    @Autowired
    private IJintieCommonBo commonBo;
    @Autowired
    private IJintieOutputHeadService jintieOutputHeadService;
    @Autowired
    private IJintieInputHeadService jintieinputHeadService;
    @Autowired
    JintieInputHeadMapper JintieInputHeadMapper;
    @Autowired
    JintieOutputHeadMapper JintieOutputHeadMapper;
    @Autowired
    JintieInputDetailMapper JintieInputDetailMapper;
    @Autowired
    JintieOutputDetailMapper JintieOutputDetailMapper;
    @Autowired
    JintieBaseOutputFieldMapper JintieBaseOutputFieldMapper;
    @Autowired
    IJintieYwinfoService jintieYwinfoService;
    @Autowired
    BaseJcsjInspectionMapper BaseJcsjInspectionMapper;
    @Autowired
    IJintieDictService jintieDictService;


    @Autowired
    JintieInputDetailCopyMapper jintieInputDetailCopyMapper;
    @Autowired
    JintieOutputDetailCopyMapper jintieOutputDetailCopyMapper;

    @Override
    @Transactional
    public void formatData(Map<String, List<Map>> dataMap, JintieYwinfo ywinfo) {
        OcrDataLogic logic = new OcrDataLogic();
        List<Map<String,Object>> lists = new ArrayList<>();//转换后的lists
        ExcelUtil ExcelUtil = new ExcelUtil();
        OcrUtil util = new OcrUtil();
        if(ywinfo.getType() ==  0){
            JintieInputHead head = new JintieInputHead();
            //进仓获得所有数据
            //拼接head

            //head 基础参数设置
            String date1 =  CalendarUtils.getFormatDate(CalendarUtils.Y_M_D_LONG,CalendarUtils.localDateTimeToUdate(ywinfo.getCreateDateTime()));
            String time1 = CalendarUtils.getFormatDate(CalendarUtils.TIME_FORMAT,CalendarUtils.localDateTimeToUdate(ywinfo.getCreateDateTime()));
            head.setMessagehead(commonBo.getMessageHead());
            head.setMessagetype("H");
            head.setMessagedate(date1);
            head.setMessagetime(time1);
            head.setAsncreationtime(CalendarUtils.getFormatDate(CalendarUtils.Y_M_DHMS_LONG,CalendarUtils.localDateTimeToUdate(LocalDateTime.now())));
            head.setAsngroup(ywinfo.getCustomNo());
            head.setStoreid(ywinfo.getRemark1());
            head.setDoctype("01");
            head.setWlYwno(ywinfo.getYwNo());
            head.setStatus(1);

//            /**
//             * 运输方式
//             * 顺序  空运  海运 发票 箱单
//             */
//            if(       dataMap.get("AWList").size() > 0 && checkNull(dataMap.get("AWList").get(0).get("AWModeOfTransport"))){
//                    head.setTransitmode((String) dataMap.get("AWList").get(0).get("AWModeOfTransport"));
//            }else if( dataMap.get("SWList").size() > 0 && checkNull(dataMap.get("SWList").get(0).get("SWModeOfTransport"))){
//                    head.setTransitmode((String) dataMap.get("SWList").get(0).get("SWModeOfTransport"));
//            }else if( dataMap.get("FPList").size() > 0 && checkNull(dataMap.get("FPList").get(0).get("FPModeOfTransport"))){
//                    head.setTransitmode((String) dataMap.get("FPList").get(0).get("FPModeOfTransport"));
//            }else if( dataMap.get("XDList").size() > 0 && checkNull(dataMap.get("XDList").get(0).get("XDModeOfTransport"))){
//                head.setTransitmode((String) dataMap.get("XDList").get(0).get("XDModeOfTransport"));
//            }
//            /**
//             *  成交方式
//             *  顺序  海运单 发票 箱单 合同
//             *  DAP、EXW等特殊成交方式的转化。（8086有相关参数库）
//             */
//            if(       dataMap.get("SWList").size() > 0 && checkNull(dataMap.get("SWList").get(0).get("SWTermType"))){
//                head.setDeliveryterms((String) dataMap.get("SWList").get(0).get("SWTermType"));
//            }else if( dataMap.get("FPList").size() > 0 && checkNull(dataMap.get("FPList").get(0).get("FPTermType"))){
//                head.setDeliveryterms((String) dataMap.get("FPList").get(0).get("FPTermType"));
//            }else if( dataMap.get("XDList").size() > 0 && checkNull(dataMap.get("XDList").get(0).get("XDTermType"))){
//                head.setDeliveryterms((String) dataMap.get("XDList").get(0).get("XDTermType"));
//            }else if( dataMap.get("HTList").size() > 0 && checkNull(dataMap.get("HTList").get(0).get("HTTermType"))){
//                head.setDeliveryterms((String) dataMap.get("HTList").get(0).get("HTTermType"));
//            }
//            /**
//             * 启运国/运抵国   经停港/指运港（一致）
//             * 顺序  空运  联系 海运 发票 箱单 合同
//             * 国家特殊字段转换（8086有相关参数库）。如“其它运输”为“中国”
//             */
//            if( dataMap.get("AWList").size() > 0 && checkNull(dataMap.get("AWList").get(0).get("AWDepartureAirport")) ){
//                head.setPlaceofloading((String) dataMap.get("AWList").get(0).get("AWDepartureAirport"));
//                head.setPlaceofdelivery((String) dataMap.get("AWList").get(0).get("AWDepartureAirport"));
//            }
//            else if( dataMap.get("CLList").size() > 0 && checkNull(dataMap.get("CLList").get(0).get("CLDeliveryPort")) ){
//                head.setPlaceofloading((String) dataMap.get("CLList").get(0).get("CLDeliveryPort"));
//                head.setPlaceofdelivery((String) dataMap.get("CLList").get(0).get("CLDeliveryPort"));
//            }
//            else if( dataMap.get("SWList").size() > 0 && checkNull(dataMap.get("SWList").get(0).get("SWLoadingPort")) ){
//                head.setPlaceofloading((String) dataMap.get("SWList").get(0).get("SWLoadingPort"));
//                head.setPlaceofdelivery((String) dataMap.get("SWList").get(0).get("SWLoadingPort"));
//            }
//            else if( dataMap.get("FPList").size() > 0 && checkNull(dataMap.get("FPList").get(0).get("FPFrom")) ){
//                head.setPlaceofloading((String) dataMap.get("FPList").get(0).get("FPFrom"));
//                head.setPlaceofdelivery((String) dataMap.get("FPList").get(0).get("FPFrom"));
//            }
//            else if( dataMap.get("XDList").size() > 0 && checkNull(dataMap.get("XDList").get(0).get("XDFrom")) ){
//                head.setPlaceofloading((String) dataMap.get("XDList").get(0).get("XDFrom"));
//                head.setPlaceofdelivery((String) dataMap.get("XDList").get(0).get("XDFrom"));
//            }
//            else if( dataMap.get("HTList").size() > 0 && checkNull(dataMap.get("HTList").get(0).get("HTFrom")) ){
//                head.setPlaceofloading((String) dataMap.get("HTList").get(0).get("HTFrom"));
//                head.setPlaceofdelivery((String) dataMap.get("HTList").get(0).get("HTFrom"));
//            }
//            /**
//             * 目的国
//             * 顺序
//             * 国家特殊字段转换（8086有相关参数库）。如“其它运输”为“中国”
//             */
//            if( dataMap.get("AWList").size() > 0 && checkNull(dataMap.get("AWList").get(0).get("AWDepartureAirport")) ){
//                head.setCountryofdestination((String) dataMap.get("AWList").get(0).get("AWDepartureAirport"));
//            }
//            else if( dataMap.get("SWList").size() > 0 && checkNull(dataMap.get("SWList").get(0).get("SWDeliveryPlace")) ){
//                head.setCountryofdestination((String) dataMap.get("SWList").get(0).get("SWDeliveryPlace"));
//            }
//            else if( dataMap.get("FPList").size() > 0 && checkNull(dataMap.get("FPList").get(0).get("FPTo")) ){
//                head.setCountryofdestination((String) dataMap.get("FPList").get(0).get("FPTo"));
//            }
//            else if( dataMap.get("XDList").size() > 0 && checkNull(dataMap.get("XDList").get(0).get("XDTo")) ){
//                head.setCountryofdestination((String) dataMap.get("XDList").get(0).get("XDTo"));
//            }
//            else if( dataMap.get("HTList").size() > 0 && checkNull(dataMap.get("HTList").get(0).get("HTTo")) ){
//                head.setCountryofdestination((String) dataMap.get("HTList").get(0).get("HTTo"));
//            }
//            /**
//             * 总件数
//             * 为“整数”最小为1
//             * 多个“FPCtnQty”累加  或 多个"XDCtnQty"累加= 总件数
//             *
//             */
//            if( dataMap.get("AWList").size() > 0 && checkNull(dataMap.get("AWList").get(0).get("AWQTY")) ){
//                head.setContainerqty((String) dataMap.get("AWList").get(0).get("AWQTY"));
//            }
//            else if( dataMap.get("ANList").size() > 0 && checkNull(dataMap.get("ANList").get(0).get("ANQTY")) ){
//                head.setContainerqty((String) dataMap.get("ANList").get(0).get("ANQTY"));
//            }
//            else if( dataMap.get("CLList").size() > 0 && checkNull(dataMap.get("CLList").get(0).get("CLQTY")) ){
//                head.setContainerqty((String) dataMap.get("CLList").get(0).get("CLQTY"));
//            }
//            else if( dataMap.get("SWList").size() > 0 && checkNull(dataMap.get("SWList").get(0).get("SWQTY")) ){
//                head.setContainerqty((String) dataMap.get("SWList").get(0).get("SWQTY"));
//            }
//            else if( dataMap.get("FPList").size() > 0 && checkNull(dataMap.get("FPList").get(0).get("FPFrom")) ){
//                head.setContainerqty((String) dataMap.get("FPList").get(0).get("FPFrom"));
//            }
//            else if( dataMap.get("XDList").size() > 0 && checkNull(dataMap.get("XDList").get(0).get("XDFrom")) ){
//                head.setContainerqty((String) dataMap.get("XDList").get(0).get("XDFrom"));
//            }
//            else if( dataMap.get("HTList").size() > 0 && checkNull(dataMap.get("HTList").get(0).get("HTCtnQty")) ){
//                head.setContainerqty((String) dataMap.get("HTList").get(0).get("HTCtnQty"));
//            }
//            /**
//             * 总毛重
//             * 为“整数”最小为1
//             * 多个“FPCtnQty”累加  或 多个"XDCtnQty"累加= 总件数
//             * 为“数字”，小数保留5位
//             * 多个“FPTotalGW”累加  或 多个"XDTotalGW"累加= 总毛重
//             */
//            if( dataMap.get("AWList").size() > 0 && checkNull(dataMap.get("AWList").get(0).get("AWGW")) ){
//                head.setSpotservice01((String) dataMap.get("AWList").get(0).get("AWGW"));
//            }
//            else if( dataMap.get("ANList").size() > 0 && checkNull(dataMap.get("ANList").get(0).get("ANGW")) ){
//                head.setSpotservice01((String) dataMap.get("ANList").get(0).get("ANGW"));
//            }
//            else if( dataMap.get("CLList").size() > 0 && checkNull(dataMap.get("CLList").get(0).get("CLGW")) ){
//                head.setSpotservice01((String) dataMap.get("CLList").get(0).get("CLGW"));
//            }
//            else if( dataMap.get("SWList").size() > 0 && checkNull(dataMap.get("SWList").get(0).get("SWGW")) ){
//                head.setSpotservice01((String) dataMap.get("SWList").get(0).get("SWGW"));
//            }
//            else if( dataMap.get("FPList").size() > 0 && checkNull(dataMap.get("FPList").get(0).get("FPFrom")) ){
//                //head.setSpotservice01((String) dataMap.get("FPList").get(0).get("FPFrom"));
//            }
//            else if( dataMap.get("XDList").size() > 0 && checkNull(dataMap.get("XDList").get(0).get("XDFrom")) ){
//                //head.setSpotservice01((String) dataMap.get("XDList").get(0).get("XDFrom"));
//            }
//            else if( dataMap.get("HTList").size() > 0 && checkNull(dataMap.get("HTList").get(0).get("HTTotalGW")) ){
//                head.setSpotservice01((String) dataMap.get("HTList").get(0).get("HTTotalGW"));
//            }
            jintieinputHeadService.removeByYwNo(ywinfo.getYwNo());
            jintieinputHeadService.save(head);
            try {
                /**
                 * 拼接detail
                 */
                List<JintieBaseInputField> JintieBaseInputFields  =  JintieBaseInputFieldMapper.getFieldByCustomKey();//基础字段
                //通过模板和客户Id拿到对应字段
                List<JintieCustomField> JintieCustomFields =  JintieCustomFieldMapper.getFieldByCustomKeyOfIn(ywinfo.getRemark1(),ywinfo.getRemark2());//客户对应字段

                Map<String,Boolean> fields = new HashMap();
                fields.put("batch数量",false);
                fields.put("分净重",false);
                fields.put("分毛重",false);
                for (JintieCustomField JintieCustomField:JintieCustomFields) {
                    if ("batch数量".equals(JintieCustomField.getField())){
                        fields.put("batch数量",true);
                    }
                    if ("分净重".equals(JintieCustomField.getField())){
                        fields.put("分净重",true);
                    }
                    if ("分毛重".equals(JintieCustomField.getField())){
                        fields.put("分毛重",true);
                    }
                }


                //瑞萨特殊处理
                SpecialHanding SpecialHanding = new SpecialHanding();
//                if("C000047_01".equals(ywinfo.getRemark3()) || "C000047_02".equals(ywinfo.getRemark3()) ||
//                        "C000053_05".equals(ywinfo.getRemark3()) || "C000019_10".equals(ywinfo.getRemark3()) ||
//                        "C000065_01".equals(ywinfo.getRemark3()) || "C000065_02".equals(ywinfo.getRemark3())
//                        ){
//                    lists = SpecialHanding.dataFormatFor47(dataMap,ywinfo.getYwNo());
//                }else if( "C000021_01".equals(ywinfo.getRemark3()) || "C000021_02".equals(ywinfo.getRemark3()) ||
//                        "C000021_03".equals(ywinfo.getRemark3()) ){
//                    //罗门哈斯特殊处理
//                    lists = SpecialHanding.dataFormatFor21(dataMap,ywinfo.getYwNo());
//                }else  {
//                    //通用路径处理
////                   OcrServer one = new OcrServer();
////                   lists = one.dataFormat(dataMap,ywinfo.getYwNo());
////                    OcrCopy one = new OcrCopy();
////                    lists = one.dataFormat(dataMap,ywinfo.getYwNo());
//                    DataLogic DataLogic = new DataLogic();
//                    lists = DataLogic.dataFormat(dataMap,ywinfo.getYwNo(),fields);
//
//                }
                DataLogic DataLogic = new DataLogic();
                if("C000047_01".equals(ywinfo.getRemark3()) || "C000047_02".equals(ywinfo.getRemark3()) ||
                        "C000053_05".equals(ywinfo.getRemark3()) || "C000019_10".equals(ywinfo.getRemark3()) ||
                        "C000065_01".equals(ywinfo.getRemark3()) || "C000065_02".equals(ywinfo.getRemark3())
                        || "C0000cs".equals(ywinfo.getRemark3())
                        ){
                    lists = DataLogic.logicForDataByItemNo(dataMap,ywinfo.getYwNo());
                }else  {
                    //通用路径处理
                    lists = DataLogic.dataFormat(dataMap,ywinfo.getYwNo(),fields);
                }
                if(lists == null || lists.size() == 0){
                    logger.info("发票箱单异常，展示发票数据：编号"+ywinfo.getYwNo());
                    OcrCopy OcrCopy = new OcrCopy();
                    lists = OcrCopy.dataFormat(dataMap,ywinfo.getYwNo());
                }

                //格式化数据（指：像Excel导入一样，数据补充）
                lists = ExcelUtil.dataConversion(lists);
                //已经拼接数据处理
                for (int i = 0; i < lists.size(); i++) {
                    //币制转换
                    if( util.checkNull(lists.get(i).get("币制")+"")){
                        QueryWrapper<JintieDict> queryWrapper = new QueryWrapper<>();
                        queryWrapper.lambda().eq(JintieDict::getInspectionType,2);
                        List<JintieDict> list = jintieDictService.list(queryWrapper);
                        for (JintieDict JintieDict:list) {
                            if(lists.get(i).get("币制").equals(JintieDict.getInspectionName()) || lists.get(i).get("币制").equals(JintieDict.getName()) || lists.get(i).get("币制").equals(JintieDict.getNcode()) || lists.get(i).get("币制").equals(JintieDict.getEn())){
                                lists.get(i).put("币制",JintieDict.getNcode());
                                break;
                            }
                        }
                   }
                    //原产国转换
                    if(util.checkNull(lists.get(i).get("原产国")+"")){
                        QueryWrapper<JintieDict> queryWrapper = new QueryWrapper<>();
                        queryWrapper.lambda().eq(JintieDict::getInspectionType, 6);
                        List<JintieDict> list = jintieDictService.list(queryWrapper);
                        for (JintieDict JintieDict:list) {
                            if(lists.get(i).get("原产国").equals(JintieDict.getInspectionName()) || lists.get(i).get("原产国").equals(JintieDict.getName()) || lists.get(i).get("原产国").equals(JintieDict.getNcode()) || lists.get(i).get("原产国").equals(JintieDict.getEn()) || lists.get(i).get("原产国").equals(JintieDict.getRemake())){
                                lists.get(i).put("原产国",JintieDict.getNcode());
                            }
                        }
                    }
                    //CPN去掉  .0
                    if(util.checkNull(lists.get(i).get("CPN"))){
                       String cpn = lists.get(i).get("CPN")+"";
                       if(".0".equals(cpn.substring(cpn.length()-2,cpn.length())) ){
                           lists.get(i).put("CPN",cpn.substring(0,cpn.length()-2));
                       }
                   }
                   //箱号去掉  .0
//                    if(checkNull(lists.get(i).get("箱号"))){
//                        String ctn = lists.get(i).get("箱号")+"";
//                        if(ctn.contains(".0")){
//                            lists.get(i).put("箱号",ctn.replaceAll(".0",""));
//                        }
//                    }
                }

                List<Map<String,Object>> JintieInputDetail = new ArrayList();
                for (int i = 0; i < lists.size(); i++) {
                    Map<String,Object> map = new HashMap<>();
                    for (int j = 0; j < JintieBaseInputFields.size(); j++) {
                        if(util.checkNull(lists.get(i).get(JintieBaseInputFields.get(j).getField()))){
                            map.put(JintieBaseInputFields.get(j).getJintieField(),(lists.get(i).get(JintieBaseInputFields.get(j).getField())+"").trim());
                        }
                    }
                    for (int j = 0; j < JintieCustomFields.size(); j++) {
                        if(util.checkNull(lists.get(i).get(JintieCustomFields.get(j).getField()))){
                            map.put(JintieCustomFields.get(j).getJintieField(),(lists.get(i).get(JintieCustomFields.get(j).getField())+"").trim());
                        }
                    }
                    JintieInputDetail.add(map);
                }
                //查询头部是否添加成功
                JintieInputHead head2 = JintieInputHeadMapper.selectByYwID(ywinfo.getYwNo());
                //录入detail
                if(head2 != null ){
                    //清除原有detail
                    JintieInputDetailMapper.deleteByYwNo(ywinfo.getYwNo());
                    //添加到detail表
                    for (int i = 0; i < JintieInputDetail.size(); i++) {
                        Map<String,Object> map = JintieInputDetail.get(i);
                        JintieInputDetail detail = new JintieInputDetail();
                        detail.setMessagetype("D");
                        detail.setMessagehead(head2.getMessagehead());
                        detail.setStoreid(ywinfo.getRemark1());
                        detail.setMessagedate(MyUtils.returnDay());
                        detail.setMessagetime(MyUtils.return24Time());
                        detail.setCopyLineNo(map.get("COPYLINENO")+"");
                        detail.setWlYwno(head2.getWlYwno());
                        detail.setStatus(1);
                        if( !"".equals(map.get("CARTONQTY")) && map.get("CARTONQTY") != null && !"null".equals(map.get("CARTONQTY"))  ){
                            if((map.get("CARTONQTY")+"").contains(".")){
                                detail.setCartonqty((""+map.get("CARTONQTY")).substring(0,(""+map.get("CARTONQTY")).indexOf(".")));
                            }else
                                detail.setCartonqty(map.get("CARTONQTY")+"");
                        }
                        if( !"".equals(map.get("TOTALNETWEIGHT")) && map.get("TOTALNETWEIGHT") != null  && !"null".equals(map.get("TOTALNETWEIGHT"))  ){
                            detail.setTotalnetweight(new BigDecimal(map.get("TOTALNETWEIGHT")+""));
                        }
                        if( !"".equals(map.get("TOTALGROSSWEIGHT")) && map.get("TOTALGROSSWEIGHT") != null  && !"null".equals(map.get("TOTALGROSSWEIGHT")) ){
                            detail.setTotalgrossweight(new BigDecimal(map.get("TOTALGROSSWEIGHT")+""));
                        }
                        if( !"".equals(map.get("D_USERDEFINE2")) && map.get("D_USERDEFINE2") != null && !"null".equals(map.get("D_USERDEFINE2"))  ){
                            detail.setdUserdefine2(map.get("D_USERDEFINE2")+"");
                        }


                        if( !"".equals(map.get("HSCODE")) && map.get("HSCODE") != null && !"null".equals(map.get("HSCODE"))  ){
                            detail.setHscode(map.get("HSCODE")+"");
                        }
                        if( !"".equals(map.get("CHINESENAME")) && map.get("CHINESENAME") != null && !"null".equals(map.get("CHINESENAME"))  ){
                            detail.setChinesename(map.get("CHINESENAME")+"");
                        }




                        if( !"".equals(map.get("DOCLINENO")) && map.get("DOCLINENO") != null && !"null".equals(map.get("DOCLINENO"))  ){
                            detail.setDoclineno(map.get("DOCLINENO")+"");
                        }
                        if( !"".equals(map.get("PONO")) && map.get("PONO") != null && !"null".equals(map.get("PONO")) ){
                            detail.setPono(""+map.get("PONO"));
                        }


                        if( !"".equals(map.get("CUSTOMERLINENO")) && map.get("CUSTOMERLINENO") != null && !"null".equals(map.get("CUSTOMERLINENO"))  ){
                            detail.setCustomerlineno(""+map.get("CUSTOMERLINENO"));
                        }
                        if( !"".equals(map.get("SKU")) && map.get("SKU") != null){
                            detail.setSku(""+map.get("SKU"));
                        }
                        if( !"".equals(map.get("EXPECTEDQTY")) && map.get("EXPECTEDQTY") != null && !"null".equals(map.get("EXPECTEDQTY"))  ){
                            if((map.get("EXPECTEDQTY")+"").contains(".")){
                                detail.setExpectedqty((""+map.get("EXPECTEDQTY")).substring(0,(""+map.get("EXPECTEDQTY")).indexOf(".")));
                            }else
                                detail.setExpectedqty(map.get("EXPECTEDQTY")+"");
                        }
                        if( !"".equals(map.get("DECLARATIONUNIT")) && map.get("DECLARATIONUNIT") != null && !"null".equals(map.get("DECLARATIONUNIT")) ){
                            detail.setDeclarationunit(""+map.get("DECLARATIONUNIT"));
                        }
                        if( !"".equals(map.get("PRICE"))  && map.get("PRICE") != null && !"null".equals(map.get("PRICE"))  ){
                            detail.setPrice(new BigDecimal(""+map.get("PRICE")));
                        }
                        if( !"".equals(map.get("TOTALPRICE")) && map.get("TOTALPRICE") != null && !"null".equals(map.get("TOTALPRICE"))  ){
                            detail.setTotalprice(new BigDecimal(""+map.get("TOTALPRICE")));
                        }
                        if( !"".equals(map.get("CURRENCY")) && map.get("CURRENCY") != null && !"null".equals(map.get("CURRENCY"))  ){
                            detail.setCurrency(""+map.get("CURRENCY"));
                        }
                        if( !"".equals(map.get("COUNTRYOFORIGIN")) && map.get("COUNTRYOFORIGIN") != null && !"null".equals(map.get("COUNTRYOFORIGIN"))  ){
                            detail.setCountryoforigin(""+map.get("COUNTRYOFORIGIN"));
                        }
                        if( !"".equals(map.get("GROSSWEIGHT")) && map.get("GROSSWEIGHT") != null && !"null".equals(map.get("GROSSWEIGHT"))  ){
                            detail.setGrossweight(new BigDecimal(map.get("GROSSWEIGHT")+""));
                        }
                        if( !"".equals(map.get("NETWEIGHT")) && map.get("NETWEIGHT") != null && !"null".equals(map.get("NETWEIGHT")) ){
                            detail.setNetweight(new BigDecimal(""+map.get("NETWEIGHT")));
                        }
                        if( !"".equals(map.get("CUBIC")) && map.get("CUBIC") != null && !"null".equals(map.get("CUBIC"))  ){
                            detail.setCubic(new BigDecimal(""+map.get("CUBIC")));
                        }



                        if(map.get("LOTTABLE01") != "" && map.get("LOTTABLE01") != null && !"null".equals(map.get("LOTTABLE01")) ){
                            if((map.get("LOTTABLE01")+"").contains(".")){
                                detail.setLottable01((""+map.get("LOTTABLE01")).substring(0,(""+map.get("LOTTABLE01")).indexOf(".")));
                            }else
                                detail.setLottable01(""+map.get("LOTTABLE01"));
                        }
                        if(map.get("LOTTABLE02") != "" && map.get("LOTTABLE02") != null && !"null".equals(map.get("LOTTABLE02"))  ){
                            detail.setLottable02(""+map.get("LOTTABLE02"));
                        }
                        if(map.get("LOTTABLE03") != "" && map.get("LOTTABLE03") != null && !"null".equals(map.get("LOTTABLE03"))  ){
                            detail.setLottable03(""+map.get("LOTTABLE03"));
                        }
                        if(map.get("LOTTABLE04") != "" && map.get("LOTTABLE04") != null && !"null".equals(map.get("LOTTABLE04"))  ){
                            detail.setLottable04(""+map.get("LOTTABLE04"));
                        }
                        if(map.get("LOTTABLE05") != "" && map.get("LOTTABLE05") != null && !"null".equals(map.get("LOTTABLE05")) ){
                            detail.setLottable05(""+map.get("LOTTABLE05"));
                        }
                        if(map.get("LOTTABLE06") != "" && map.get("LOTTABLE06") != null && !"null".equals(map.get("LOTTABLE06"))  ){
                            detail.setLottable06(""+map.get("LOTTABLE06"));
                        }
                        if(map.get("LOTTABLE07") != "" && map.get("LOTTABLE07") != null && !"null".equals(map.get("LOTTABLE07")) ){
                            detail.setLottable07(""+map.get("LOTTABLE07"));
                        }
                        if(map.get("LOTTABLE08") != "" && map.get("LOTTABLE08") != null && !"null".equals(map.get("LOTTABLE08")) ){
                            detail.setLottable08(""+map.get("LOTTABLE08"));
                        }
                        if(map.get("LOTTABLE09") != "" && map.get("LOTTABLE09") != null && !"null".equals(map.get("LOTTABLE09")) ){
                            detail.setLottable09(""+map.get("LOTTABLE09"));
                        }
                        if(map.get("LOTTABLE10") != "" && map.get("LOTTABLE10") != null && !"null".equals(map.get("LOTTABLE10"))  ){
                            detail.setLottable10(""+map.get("LOTTABLE10"));
                        }
                        if(map.get("LOTTABLE11") != "" && map.get("LOTTABLE11") != null && !"null".equals(map.get("LOTTABLE11"))  ){
                            detail.setLottable11(""+map.get("LOTTABLE11"));
                        }
                        if(map.get("LOTTABLE12") != "" && map.get("LOTTABLE12") != null && !"null".equals(map.get("LOTTABLE12"))  ){
                            detail.setLottable12(""+map.get("LOTTABLE12"));
                        }


                        /**
                         * 三垦特殊需求
                         * 序列号1、2、3、4...
                         */
                        if("C000022".equals(detail.getStoreid())){
                            detail.setLottable03(i+1+"");
                        }
                        JintieInputDetailMapper.insert(detail);
                    }
                }
                try {
                    int denominator = (JintieBaseInputFields.size() + JintieCustomFields.size()) * JintieInputDetail.size();
                    int member = 0;
                    for (int i = 0; i < JintieInputDetail.size(); i++) {
                        member += JintieInputDetail.get(i).size();
                    }
                    DecimalFormat df=new DecimalFormat("0.00");//设置保留位数
                    String percentage = df.format((float)member/denominator);
                    ywinfo.setRemark5(percentage);
                    ywinfo.setRemark6("1.00");
                }catch (Exception e){
                    logger.warn("识别率统计错误"+ywinfo.getYwNo());
                }
                try {
                    jintieInputDetailCopyMapper.deleteByYwNo(head.getWlYwno());
                    for (int i = 0; i < JintieInputDetail.size(); i++) {
                        JintieInputDetailCopy detail= new JintieInputDetailCopy();
                        Map<String,Object> map = JintieInputDetail.get(i);
                        detail.setWlYwno(head2.getWlYwno());
                        if( !"".equals(map.get("CARTONQTY")) && map.get("CARTONQTY") != null && !"null".equals(map.get("CARTONQTY"))  ){
                            if((map.get("CARTONQTY")+"").contains(".")){
                                detail.setCartonqty((""+map.get("CARTONQTY")).substring(0,(""+map.get("CARTONQTY")).indexOf(".")));
                            }else
                                detail.setCartonqty(map.get("CARTONQTY")+"");
                        }
                        if( !"".equals(map.get("TOTALNETWEIGHT")) && map.get("TOTALNETWEIGHT") != null  && !"null".equals(map.get("TOTALNETWEIGHT"))  ){
                            detail.setTotalnetweight(new BigDecimal(map.get("TOTALNETWEIGHT")+""));
                        }
                        if( !"".equals(map.get("TOTALGROSSWEIGHT")) && map.get("TOTALGROSSWEIGHT") != null  && !"null".equals(map.get("TOTALGROSSWEIGHT")) ){
                            detail.setTotalgrossweight(new BigDecimal(map.get("TOTALGROSSWEIGHT")+""));
                        }
                        if( !"".equals(map.get("D_USERDEFINE2")) && map.get("D_USERDEFINE2") != null && !"null".equals(map.get("D_USERDEFINE2"))  ){
                            detail.setdUserdefine2(new BigDecimal(map.get("D_USERDEFINE2")+""));
                        }


                        if( !"".equals(map.get("HSCODE")) && map.get("HSCODE") != null && !"null".equals(map.get("HSCODE"))  ){
                            detail.setHscode(map.get("HSCODE")+"");
                        }
                        if( !"".equals(map.get("CHINESENAME")) && map.get("CHINESENAME") != null && !"null".equals(map.get("CHINESENAME"))  ){
                            detail.setChinesename(map.get("CHINESENAME")+"");
                        }




                        if( !"".equals(map.get("DOCLINENO")) && map.get("DOCLINENO") != null && !"null".equals(map.get("DOCLINENO"))  ){
                            detail.setDoclineno(map.get("DOCLINENO")+"");
                        }
                        if( !"".equals(map.get("PONO")) && map.get("PONO") != null && !"null".equals(map.get("PONO")) ){
                            detail.setPono(""+map.get("PONO"));
                        }


                        if( !"".equals(map.get("CUSTOMERLINENO")) && map.get("CUSTOMERLINENO") != null && !"null".equals(map.get("CUSTOMERLINENO"))  ){
                            detail.setCustomerlineno(""+map.get("CUSTOMERLINENO"));
                        }
                        if( !"".equals(map.get("SKU")) && map.get("SKU") != null){
                            detail.setSku(""+map.get("SKU"));
                        }
                        if( !"".equals(map.get("EXPECTEDQTY")) && map.get("EXPECTEDQTY") != null && !"null".equals(map.get("EXPECTEDQTY"))  ){
                            if((map.get("EXPECTEDQTY")+"").contains(".")){
                                detail.setExpectedqty((""+map.get("EXPECTEDQTY")).substring(0,(""+map.get("EXPECTEDQTY")).indexOf(".")));
                            }else
                                detail.setExpectedqty(map.get("EXPECTEDQTY")+"");
                        }
                        if( !"".equals(map.get("DECLARATIONUNIT")) && map.get("DECLARATIONUNIT") != null && !"null".equals(map.get("DECLARATIONUNIT")) ){
                            detail.setDeclarationunit(""+map.get("DECLARATIONUNIT"));
                        }
                        if( !"".equals(map.get("PRICE"))  && map.get("PRICE") != null && !"null".equals(map.get("PRICE"))  ){
                            detail.setPrice(new BigDecimal(""+map.get("PRICE")));
                        }
                        if( !"".equals(map.get("TOTALPRICE")) && map.get("TOTALPRICE") != null && !"null".equals(map.get("TOTALPRICE"))  ){
                            detail.setTotalprice(new BigDecimal(""+map.get("TOTALPRICE")));
                        }
                        if( !"".equals(map.get("CURRENCY")) && map.get("CURRENCY") != null && !"null".equals(map.get("CURRENCY"))  ){
                            detail.setCurrency(""+map.get("CURRENCY"));
                        }
                        if( !"".equals(map.get("COUNTRYOFORIGIN")) && map.get("COUNTRYOFORIGIN") != null && !"null".equals(map.get("COUNTRYOFORIGIN"))  ){
                            detail.setCountryoforigin(""+map.get("COUNTRYOFORIGIN"));
                        }
                        if( !"".equals(map.get("GROSSWEIGHT")) && map.get("GROSSWEIGHT") != null && !"null".equals(map.get("GROSSWEIGHT"))  ){
                            detail.setGrossweight(new BigDecimal(map.get("GROSSWEIGHT")+""));
                        }
                        if( !"".equals(map.get("NETWEIGHT")) && map.get("NETWEIGHT") != null && !"null".equals(map.get("NETWEIGHT")) ){
                            detail.setNetweight(new BigDecimal(""+map.get("NETWEIGHT")));
                        }
                        if( !"".equals(map.get("CUBIC")) && map.get("CUBIC") != null && !"null".equals(map.get("CUBIC"))  ){
                            detail.setCubic(new BigDecimal(""+map.get("CUBIC")));
                        }



                        if(map.get("LOTTABLE01") != "" && map.get("LOTTABLE01") != null && !"null".equals(map.get("LOTTABLE01")) ){
                            if((map.get("LOTTABLE01")+"").contains(".")){
                                detail.setLottable01((""+map.get("LOTTABLE01")).substring(0,(""+map.get("LOTTABLE01")).indexOf(".")));
                            }else
                                detail.setLottable01(""+map.get("LOTTABLE01"));
                        }
                        if(map.get("LOTTABLE02") != "" && map.get("LOTTABLE02") != null && !"null".equals(map.get("LOTTABLE02"))  ){
                            detail.setLottable02(""+map.get("LOTTABLE02"));
                        }
                        if(map.get("LOTTABLE03") != "" && map.get("LOTTABLE03") != null && !"null".equals(map.get("LOTTABLE03"))  ){
                            detail.setLottable03(""+map.get("LOTTABLE03"));
                        }
                        if(map.get("LOTTABLE04") != "" && map.get("LOTTABLE04") != null && !"null".equals(map.get("LOTTABLE04"))  ){
                            detail.setLottable04(""+map.get("LOTTABLE04"));
                        }
                        if(map.get("LOTTABLE05") != "" && map.get("LOTTABLE05") != null && !"null".equals(map.get("LOTTABLE05")) ){
                            detail.setLottable05(""+map.get("LOTTABLE05"));
                        }
                        if(map.get("LOTTABLE06") != "" && map.get("LOTTABLE06") != null && !"null".equals(map.get("LOTTABLE06"))  ){
                            detail.setLottable06(""+map.get("LOTTABLE06"));
                        }
                        if(map.get("LOTTABLE07") != "" && map.get("LOTTABLE07") != null && !"null".equals(map.get("LOTTABLE07")) ){
                            detail.setLottable07(""+map.get("LOTTABLE07"));
                        }
                        if(map.get("LOTTABLE08") != "" && map.get("LOTTABLE08") != null && !"null".equals(map.get("LOTTABLE08")) ){
                            detail.setLottable08(""+map.get("LOTTABLE08"));
                        }
                        if(map.get("LOTTABLE09") != "" && map.get("LOTTABLE09") != null && !"null".equals(map.get("LOTTABLE09")) ){
                            detail.setLottable09(""+map.get("LOTTABLE09"));
                        }
                        if(map.get("LOTTABLE10") != "" && map.get("LOTTABLE10") != null && !"null".equals(map.get("LOTTABLE10"))  ){
                            detail.setLottable10(""+map.get("LOTTABLE10"));
                        }
                        if(map.get("LOTTABLE11") != "" && map.get("LOTTABLE11") != null && !"null".equals(map.get("LOTTABLE11"))  ){
                            detail.setLottable11(""+map.get("LOTTABLE11"));
                        }
                        if(map.get("LOTTABLE12") != "" && map.get("LOTTABLE12") != null && !"null".equals(map.get("LOTTABLE12"))  ){
                            detail.setLottable12(""+map.get("LOTTABLE12"));
                        }
                        jintieInputDetailCopyMapper.insert(detail);
                    }

                }catch (Exception e){
                    logger.warn("Copy识别数据存放错误"+ywinfo.getYwNo());
                }
                //回写业务表更改状态
                ywinfo.setStatus("已识别待补充");
                jintieYwinfoService.updateById(ywinfo);
                logger.info("识别数据导入数据长度为："+JintieInputDetail.size()+"，编号:"+ywinfo.getYwNo());
            }catch (Exception e){
//                e.printStackTrace();
                ywinfo.setStatus("识别数据异常");
                ywinfo.setRemark(e.getMessage());
                jintieYwinfoService.updateById(ywinfo);
            }finally {
                //打印到excel
                try {
                    List<String> list1 = new ArrayList<>();
                    Iterator aa = lists.get(0).keySet().iterator();
                    while (aa.hasNext()){
                        list1.add(aa.next()+"");
                    }
                    ExcelUtil.exportToExcel(lists,list1,ywinfo.getYwNo());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }








        }else {
           //出仓 设计
            JintieOutputHead head = new JintieOutputHead();
            //基础数据设置
            String date1 =  CalendarUtils.getFormatDate(CalendarUtils.Y_M_D_LONG,CalendarUtils.localDateTimeToUdate(ywinfo.getCreateDateTime()));
            String time1 = CalendarUtils.getFormatDate(CalendarUtils.TIME_FORMAT,CalendarUtils.localDateTimeToUdate(ywinfo.getCreateDateTime()));
            head.setMessagehead(commonBo.getMessageHead());
            head.setMessagetype("H");
            head.setMessagedate(date1);
            head.setMessagetime(time1);
            head.setOrdertime(LocalDateTime.now());
            head.setStoreid(ywinfo.getRemark1());
            head.setDoctype("01");
            head.setSoreference1(ywinfo.getCustomNo());
            head.setStatus(1);
            head.setWlYwno(ywinfo.getYwNo());


//            /**
//             * 进出口口岸  SPOTSERVICE03
//             * 运输方式“航空运输”为2233，“海运运输”为2225。非空海运就为“其它运输”为2218
//             */
//            //head.setSpotservice03();
//            /**
//             * 运输方式 TRANSPORTATION
//             * “air、sea”等字段需要进行转化
//             */
//            if(       dataMap.get("AWList").size() > 0 && checkNull(dataMap.get("AWList").get(0).get("AWModeOfTransport"))){
//                head.setTransportation((String) dataMap.get("AWList").get(0).get("AWModeOfTransport"));
//            }else if( dataMap.get("SWList").size() > 0 && checkNull(dataMap.get("SWList").get(0).get("SWModeOfTransport"))){
//                head.setTransportation((String) dataMap.get("SWList").get(0).get("SWModeOfTransport"));
//            }else if( dataMap.get("FPList").size() > 0 && checkNull(dataMap.get("FPList").get(0).get("FPModeOfTransport"))){
//                head.setTransportation((String) dataMap.get("FPList").get(0).get("FPModeOfTransport"));
//            }else if( dataMap.get("XDList").size() > 0 && checkNull(dataMap.get("XDList").get(0).get("XDModeOfTransport"))){
//                head.setTransportation((String) dataMap.get("XDList").get(0).get("XDModeOfTransport"));
//            }
//            /**
//             *  成交方式 SPOTSERVICE01
//             *  顺序  海运单 发票 箱单 合同
//             *  DAP、EXW等特殊成交方式的转化。（8086有相关参数库）
//             */
//            if(       dataMap.get("SWList").size() > 0 && checkNull(dataMap.get("SWList").get(0).get("SWTermType"))){
//                head.setSpotservice01((String) dataMap.get("SWList").get(0).get("SWTermType"));
//            }else if( dataMap.get("FPList").size() > 0 && checkNull(dataMap.get("FPList").get(0).get("FPTermType"))){
//                head.setSpotservice01((String) dataMap.get("FPList").get(0).get("FPTermType"));
//            }else if( dataMap.get("XDList").size() > 0 && checkNull(dataMap.get("XDList").get(0).get("XDTermType"))){
//                head.setSpotservice01((String) dataMap.get("XDList").get(0).get("XDTermType"));
//            }else if( dataMap.get("HTList").size() > 0 && checkNull(dataMap.get("HTList").get(0).get("HTTermType"))){
//                head.setSpotservice01((String) dataMap.get("HTList").get(0).get("HTTermType"));
//            }
//            /**
//             * 启运国/运抵国   经停港/指运港（一致）  PLACEOFDISCHARGE
//             * 顺序  空运  联系 海运 发票 箱单 合同
//             * 国家特殊字段转换（8086有相关参数库）。如“其它运输”为“中国”
//             */
//            if( dataMap.get("AWList").size() > 0 && checkNull(dataMap.get("AWList").get(0).get("AWDepartureAirport")) ){
//                head.setPlaceofdischarge((String) dataMap.get("AWList").get(0).get("AWDepartureAirport"));
//                head.setPlaceofdelivery((String) dataMap.get("AWList").get(0).get("AWDepartureAirport"));
//            }
//            else if( dataMap.get("CLList").size() > 0 && checkNull(dataMap.get("CLList").get(0).get("CLDeliveryPort")) ){
//                head.setPlaceofdischarge((String) dataMap.get("CLList").get(0).get("CLDeliveryPort"));
//                head.setPlaceofdelivery((String) dataMap.get("CLList").get(0).get("CLDeliveryPort"));
//            }
//            else if( dataMap.get("SWList").size() > 0 && checkNull(dataMap.get("SWList").get(0).get("SWLoadingPort")) ){
//                head.setPlaceofdischarge((String) dataMap.get("SWList").get(0).get("SWLoadingPort"));
//                head.setPlaceofdelivery((String) dataMap.get("SWList").get(0).get("SWLoadingPort"));
//            }
//            else if( dataMap.get("FPList").size() > 0 && checkNull(dataMap.get("FPList").get(0).get("FPFrom")) ){
//                head.setPlaceofdischarge((String) dataMap.get("FPList").get(0).get("FPFrom"));
//                head.setPlaceofdelivery((String) dataMap.get("FPList").get(0).get("FPFrom"));
//            }
//            else if( dataMap.get("XDList").size() > 0 && checkNull(dataMap.get("XDList").get(0).get("XDFrom")) ){
//                head.setPlaceofdischarge((String) dataMap.get("XDList").get(0).get("XDFrom"));
//                head.setPlaceofdelivery((String) dataMap.get("XDList").get(0).get("XDFrom"));
//            }
//            else if( dataMap.get("HTList").size() > 0 && checkNull(dataMap.get("HTList").get(0).get("HTFrom")) ){
//                head.setPlaceofdischarge((String) dataMap.get("HTList").get(0).get("HTFrom"));
//                head.setPlaceofdelivery((String) dataMap.get("HTList").get(0).get("HTFrom"));
//            }
            jintieOutputHeadService.removeYwNo(ywinfo.getYwNo());
            jintieOutputHeadService.save(head);


            /**
             * 拼接detail
             */
            //难点  判断detail的条数
            //1，必有单证  发票





            try {




                List<JintieBaseOutputField> JintieBaseInputFields  =  JintieBaseOutputFieldMapper.getFieldByCustomKey();//基础字段
                //通过客户ID 和 模板id拿到对应字段
                List<JintieCustomField> JintieCustomFields =  JintieCustomFieldMapper.getFieldByCustomKeyOfOut(ywinfo.getRemark1(),ywinfo.getRemark2());//客户对应字段
                Map<String,Boolean> fields = new HashMap();
               for (JintieCustomField JintieCustomField:JintieCustomFields) {
                   fields.put("batch数量",false);
                   fields.put("分净重",false);
                   fields.put("分毛重",false);
                   if ("batch数量".equals(JintieCustomField.getField())){
                       fields.put("batch数量",true);
                   }
                   if ("分净重".equals(JintieCustomField.getField())){
                       fields.put("分净重",true);
                   }
                   if ("分毛重".equals(JintieCustomField.getField())){
                       fields.put("分毛重",true);
                   }
               }
               //瑞萨特殊处理
               SpecialHanding SpecialHanding = new SpecialHanding();
                DataLogic DataLogic = new DataLogic();
//               if("C000047_01".equals(ywinfo.getRemark3()) || "C000047_02".equals(ywinfo.getRemark3()) ||
//                       "C000053_05".equals(ywinfo.getRemark3()) || "C000019_10".equals(ywinfo.getRemark3()) ||
//                       "C000065_01".equals(ywinfo.getRemark3()) || "C000065_02".equals(ywinfo.getRemark3())
//                       ){
//                   DataLogic.logicForDataByItemNo(dataMap,ywinfo.getYwNo());
//               }else if( "C000021_01".equals(ywinfo.getRemark3()) || "C000021_02".equals(ywinfo.getRemark3()) ||
//                       "C000021_03".equals(ywinfo.getRemark3()) ){
//                   //罗门哈斯特殊处理
//                   lists = SpecialHanding.dataFormatFor21(dataMap,ywinfo.getYwNo());
//               }else  {
//                   //通用路径处理
//                lists = DataLogic.dataFormat(dataMap,ywinfo.getYwNo(),fields);
//               }
                if("C000047_01".equals(ywinfo.getRemark3()) || "C000047_02".equals(ywinfo.getRemark3()) ||
                        "C000053_05".equals(ywinfo.getRemark3()) || "C000019_10".equals(ywinfo.getRemark3()) ||
                        "C000065_01".equals(ywinfo.getRemark3()) || "C000065_02".equals(ywinfo.getRemark3())
                        || "C0000cs".equals(ywinfo.getRemark3())
                        ){
                    lists = DataLogic.logicForDataByItemNo(dataMap,ywinfo.getYwNo());
                }else  {
                    //通用路径处理
                    lists = DataLogic.dataFormat(dataMap,ywinfo.getYwNo(),fields);
                }
                if(lists == null || lists.size() == 0){
                    logger.info("发票箱单异常，展示发票数据：编号"+ywinfo.getYwNo());
                    OcrCopy OcrCopy = new OcrCopy();
                    lists = OcrCopy.dataFormat(dataMap,ywinfo.getYwNo());
                }
               //格式化数据
               lists = ExcelUtil.dataConversion(lists);
                //已经拼接数据处理
                for (int i = 0; i < lists.size(); i++) {
                    //币制转换
                    if( util.checkNull(lists.get(i).get("币制")+"")){
                        QueryWrapper<JintieDict> queryWrapper = new QueryWrapper<>();
                        queryWrapper.lambda().eq(JintieDict::getInspectionType,2);
                        List<JintieDict> list = jintieDictService.list(queryWrapper);
                        for (JintieDict JintieDict:list) {
                            if(lists.get(i).get("币制").equals(JintieDict.getInspectionName()) || lists.get(i).get("币制").equals(JintieDict.getName()) || lists.get(i).get("币制").equals(JintieDict.getNcode()) || lists.get(i).get("币制").equals(JintieDict.getEn())){
                                lists.get(i).put("币制",JintieDict.getNcode());
                                break;
                            }
                        }
                    }
                    //原产国转换
                    if(util.checkNull(lists.get(i).get("原产国")+"")){
                        QueryWrapper<JintieDict> queryWrapper = new QueryWrapper<>();
                        queryWrapper.lambda().eq(JintieDict::getInspectionType, 6);
                        List<JintieDict> list = jintieDictService.list(queryWrapper);
                        for (JintieDict JintieDict:list) {
                            if(lists.get(i).get("原产国").equals(JintieDict.getInspectionName()) || lists.get(i).get("原产国").equals(JintieDict.getName()) || lists.get(i).get("原产国").equals(JintieDict.getNcode()) || lists.get(i).get("原产国").equals(JintieDict.getEn()) || lists.get(i).get("原产国").equals(JintieDict.getRemake())){
                                lists.get(i).put("原产国",JintieDict.getNcode());
                            }
                        }
                    }
                    //CPN去掉  .0
                    if(util.checkNull(lists.get(i).get("CPN"))){
                        String cpn = lists.get(i).get("CPN")+"";
                        if(".0".equals(cpn.substring(cpn.length()-2,cpn.length())) ){
                            lists.get(i).put("CPN",cpn.substring(0,cpn.length()-2));
                        }
                    }
                    //箱号去掉  .0
//                    if(checkNull(lists.get(i).get("箱号"))){
//                        String ctn = lists.get(i).get("箱号")+"";
//                        if(ctn.contains(".0")){
//                            lists.get(i).put("箱号",ctn.replaceAll(".0",""));
//                        }
//                    }
                }

               List<Map<String,Object>> JintieOutputDetail = new ArrayList();
               for (int i = 0; i < lists.size(); i++) {
                   Map<String,Object> map = new HashMap<>();
                   for (int j = 0; j < JintieBaseInputFields.size(); j++) {
                       map.put(JintieBaseInputFields.get(j).getJintieField(),(lists.get(i).get(JintieBaseInputFields.get(j).getField())+"").trim());
                   }
                   for (int j = 0; j < JintieCustomFields.size(); j++) {
                       map.put(JintieCustomFields.get(j).getJintieField(),(lists.get(i).get(JintieCustomFields.get(j).getField())+"").trim());
                   }
                   JintieOutputDetail.add(map);
               }
               //查询头部是否添加成功
               JintieOutputHead head2 = JintieOutputHeadMapper.selectByYwID(ywinfo.getYwNo());
               //录入detail

               if(head2 != null ){
                   //清除原有detail
                   JintieOutputDetailMapper.deleteByYwNo(ywinfo.getYwNo());
                   //添加到detail表
                   for (int i = 0; i < JintieOutputDetail.size(); i++) {
                       Map<String,Object> map = JintieOutputDetail.get(i);
                       JintieOutputDetail detail = new JintieOutputDetail();
                       detail.setMessagetype("D");
                       detail.setMessagehead(head2.getMessagehead());
                       detail.setStoreid(ywinfo.getRemark1());
                       detail.setMessagedate(MyUtils.returnDay());
                       detail.setMessagetime(MyUtils.return24Time());
                       detail.setCopyLineNo(map.get("COPYLINENO")+"");
                       detail.setWlYwno(head2.getWlYwno());
                       detail.setStatus(1);
                       if( !"".equals(map.get("VOLUMN")) && map.get("VOLUMN") != null && !"null".equals(map.get("VOLUMN"))){
                           detail.setVolumn(new BigDecimal(map.get("VOLUMN")+""));
                       }
                       if( !"".equals(map.get("VOLUMNUOM")) && map.get("VOLUMNUOM") != null && !"null".equals(map.get("VOLUMNUOM")) ){
                           detail.setVolumnuom(map.get("VOLUMNUOM")+"");
                       }
                       if( !"".equals(map.get("DOCLINENO")) && map.get("DOCLINENO") != null && !"null".equals(map.get("DOCLINENO")) ){
                           detail.setDoclineno(map.get("DOCLINENO")+"");
                       }
                       if( !"".equals(map.get("QTYORDERED")) && map.get("QTYORDERED") != null && !"null".equals(map.get("QTYORDERED")) ){
                           if((""+map.get("QTYORDERED")).contains(".")){
                               detail.setQtyordered(Integer.parseInt((""+map.get("QTYORDERED")).substring(0,(""+map.get("QTYORDERED")).indexOf("."))));
                           }else {
                               detail.setQtyordered(Integer.parseInt(map.get("QTYORDERED")+"") );
                           }

                       }
                       if( !"".equals(map.get("DECLARATIONUNIT")) && map.get("DECLARATIONUNIT") != null && !"null".equals(map.get("DECLARATIONUNIT")) ){
                           detail.setDeclarationunit(map.get("DECLARATIONUNIT")+"");
                       }


                       if( !"".equals(map.get("CUSTOMERLINENO")) && map.get("CUSTOMERLINENO") != null  && !"null".equals(map.get("CUSTOMERLINENO"))  ){
                           detail.setCustomerlineno(map.get("CUSTOMERLINENO")+"");
                       }
                       if( !"".equals(map.get("SKU")) && map.get("SKU") != null  && !"null".equals(map.get("SKU")) ){
                           detail.setSku(map.get("SKU")+"");
                       }
                       if( !"".equals(map.get("OPENQTY")) && map.get("OPENQTY") != null  && !"null".equals(map.get("OPENQTY")) ){
                           if((""+map.get("OPENQTY")).contains(".")){
                               detail.setOpenqty(Integer.parseInt((""+map.get("OPENQTY")).substring(0,(""+map.get("OPENQTY")).indexOf("."))));
                           }else {
                               detail.setOpenqty(Integer.parseInt(""+map.get("OPENQTY")));
                           }
                       }
                       if( !"".equals(map.get("PRICE")) && map.get("PRICE") != null  && !"null".equals(map.get("PRICE")) ){
                           detail.setPrice(new BigDecimal( map.get("PRICE")+""));
                       }
                       if( !"".equals(map.get("TOTALPRICE")) && map.get("TOTALPRICE") != null  && !"null".equals(map.get("TOTALPRICE")) ){
                           detail.setTotalprice(new BigDecimal(map.get("TOTALPRICE")+""));
                       }
                       if( !"".equals(map.get("CURRENCY")) && map.get("CURRENCY") != null  && !"null".equals(map.get("CURRENCY"))){
                           detail.setCurrency(map.get("CURRENCY")+"");
                       }
                       if(!"".equals(map.get("COUNTRYOFORIGIN") ) && map.get("COUNTRYOFORIGIN") != null  && !"null".equals(map.get("COUNTRYOFORIGIN"))){
                           detail.setCountryoforigin(map.get("COUNTRYOFORIGIN")+"");
                       }


                       if(map.get("LOTTABLE01") != "" && map.get("LOTTABLE01") != null  && !"null".equals(map.get("LOTTABLE01"))){
                           if((map.get("LOTTABLE01")+"").contains(".")){
                               detail.setLottable01((""+map.get("LOTTABLE01")).substring(0,(""+map.get("LOTTABLE01")).indexOf(".")));
                           }else
                               detail.setLottable01(""+map.get("LOTTABLE01"));
                       }
                       if(map.get("LOTTABLE02") != "" && map.get("LOTTABLE02") != null  && !"null".equals(map.get("LOTTABLE02"))){
                           detail.setLottable02(""+ map.get("LOTTABLE02"));
                       }
                       if(map.get("LOTTABLE03") != "" && map.get("LOTTABLE03") != null  && !"null".equals(map.get("LOTTABLE03"))){
                           detail.setLottable03(""+ map.get("LOTTABLE03"));
                       }
                       if(map.get("LOTTABLE04") != "" && map.get("LOTTABLE04") != null  && !"null".equals(map.get("LOTTABLE04"))){
                           detail.setLottable04(""+ map.get("LOTTABLE04"));
                       }
                       if(map.get("LOTTABLE05") != "" && map.get("LOTTABLE05") != null  && !"null".equals(map.get("LOTTABLE05"))){
                           detail.setLottable05(""+ map.get("LOTTABLE05"));
                       }
                       if(map.get("LOTTABLE06") != "" && map.get("LOTTABLE06") != null  && !"null".equals(map.get("LOTTABLE06"))){
                           detail.setLottable06(""+ map.get("LOTTABLE06"));
                       }
                       if(map.get("LOTTABLE07") != "" && map.get("LOTTABLE07") != null  && !"null".equals(map.get("LOTTABLE07"))){
                           detail.setLottable07(""+ map.get("LOTTABLE07"));
                       }
                       if(map.get("LOTTABLE08") != "" && map.get("LOTTABLE08") != null  && !"null".equals(map.get("LOTTABLE08"))){
                           detail.setLottable08(""+ map.get("LOTTABLE08"));
                       }
                       if(map.get("LOTTABLE09") != "" && map.get("LOTTABLE09") != null  && !"null".equals(map.get("LOTTABLE09"))){
                           detail.setLottable09(""+ map.get("LOTTABLE09"));
                       }
                       if(map.get("LOTTABLE10") != "" && map.get("LOTTABLE10") != null  && !"null".equals(map.get("LOTTABLE10"))){
                           detail.setLottable10(""+ map.get("LOTTABLE10"));
                       }
                       if(map.get("LOTTABLE11") != "" && map.get("LOTTABLE11") != null  && !"null".equals(map.get("LOTTABLE11"))){
                           detail.setLottable11(""+ map.get("LOTTABLE11"));
                       }
                       if(map.get("LOTTABLE12") != "" && map.get("LOTTABLE12") != null  && !"null".equals(map.get("LOTTABLE12"))){
                           detail.setLottable12(""+ map.get("LOTTABLE12"));
                       }
                       //计算单价
//                if(map.get("TOTALPRICE") != "" && map.get("TOTALPRICE") != null && map.get("OPENQTY") != "" && map.get("OPENQTY") != null){
//                    BigDecimal bigDecimal1 = new BigDecimal(map.get("TOTALPRICE")+"");
//                    BigDecimal bigDecimal2 = new BigDecimal(map.get("OPENQTY")+"");
//                    detail.setPrice(bigDecimal1.divide(bigDecimal2,3,ROUND_HALF_DOWN));
//                }
                       /**
                        * 尼吉康(没有对多)
                        * “料号” 前加“1P”   末尾去“-W”
                        * “订单号“ 前加“ 1T”
                        * “PO” 前加“K”
                        * “CPN” 前加“P”
                        * “入库发票号” 如6位数字，在第二位后加“-”（例 93-0836）(针对出)
                        */
                       //功能关闭
//                if("C000016".equals(detail.getStoreid())){
//                    if(detail.getVolumnuom() != null && !"".equals(detail.getVolumnuom()) ){
//                        //判断是否已经有"1P"
//                        if(!detail.getVolumnuom().contains("-")){
//                            if(detail.getVolumnuom().length()==6){
//                                detail.setVolumnuom(detail.getVolumnuom().substring(0,2)+"-"+detail.getVolumnuom().substring(2));
//                            }
//                        }
//                    }
//                    if(detail.getLottable02() != null && !"".equals(detail.getLottable02()) ){
//                        //“CPN” 前加“P”
//                        if(!"P".equals(detail.getLottable02().substring(0,1))){
//                            detail.setLottable02("P"+detail.getLottable02());
//                        }
//                    }
//                }
                       /**
                        * 三垦特殊需求
                        * 序列号1、2、3、4...
                        */
                       if("C000022".equals(detail.getStoreid())){
                           detail.setLottable03(i+1+"");
                       }
                      JintieOutputDetailMapper.insert(detail);
                   }
               }
                try {
                    int denominator = (JintieBaseInputFields.size() + JintieCustomFields.size()) * JintieOutputDetail.size();
                    int member = 0;
                    for (int i = 0; i < JintieOutputDetail.size(); i++) {
                        member += JintieOutputDetail.get(i).size();
                    }
                    DecimalFormat df=new DecimalFormat("0.00");//设置保留位数
                    String percentage = df.format((float)member/denominator);
                    ywinfo.setRemark5(percentage);
                    ywinfo.setRemark6("1.00");
                }catch (Exception e){
                   logger.warn("识别率统计错误"+ywinfo.getYwNo());
                }
                try {
                    jintieOutputDetailCopyMapper.deleteByYwNo(ywinfo.getYwNo());
                    for (int i = 0; i < JintieOutputDetail.size(); i++) {
                        JintieOutputDetailCopy detail= new JintieOutputDetailCopy();
                        Map<String,Object> map = JintieOutputDetail.get(i);
                        detail.setWlYwno(head2.getWlYwno());
                        if( !"".equals(map.get("VOLUMN")) && map.get("VOLUMN") != null && !"null".equals(map.get("VOLUMN"))){
                            detail.setVolumn(new BigDecimal(map.get("VOLUMN")+""));
                        }
                        if( !"".equals(map.get("VOLUMNUOM")) && map.get("VOLUMNUOM") != null && !"null".equals(map.get("VOLUMNUOM")) ){
                            detail.setVolumnuom(map.get("VOLUMNUOM")+"");
                        }
                        if( !"".equals(map.get("DOCLINENO")) && map.get("DOCLINENO") != null && !"null".equals(map.get("DOCLINENO")) ){
                            detail.setDoclineno(map.get("DOCLINENO")+"");
                        }
                        if( !"".equals(map.get("QTYORDERED")) && map.get("QTYORDERED") != null && !"null".equals(map.get("QTYORDERED")) ){
                            if((""+map.get("QTYORDERED")).contains(".")){
                                detail.setQtyordered(Integer.parseInt((""+map.get("QTYORDERED")).substring(0,(""+map.get("QTYORDERED")).indexOf("."))));
                            }else {
                                detail.setQtyordered(Integer.parseInt(map.get("QTYORDERED")+"") );
                            }

                        }
                        if( !"".equals(map.get("DECLARATIONUNIT")) && map.get("DECLARATIONUNIT") != null && !"null".equals(map.get("DECLARATIONUNIT")) ){
                            detail.setDeclarationunit(map.get("DECLARATIONUNIT")+"");
                        }


                        if( !"".equals(map.get("CUSTOMERLINENO")) && map.get("CUSTOMERLINENO") != null  && !"null".equals(map.get("CUSTOMERLINENO"))  ){
                            detail.setCustomerlineno(map.get("CUSTOMERLINENO")+"");
                        }
                        if( !"".equals(map.get("SKU")) && map.get("SKU") != null  && !"null".equals(map.get("SKU")) ){
                            detail.setSku(map.get("SKU")+"");
                        }
                        if( !"".equals(map.get("OPENQTY")) && map.get("OPENQTY") != null  && !"null".equals(map.get("OPENQTY")) ){
                            if((""+map.get("OPENQTY")).contains(".")){
                                detail.setOpenqty(Integer.parseInt((""+map.get("OPENQTY")).substring(0,(""+map.get("OPENQTY")).indexOf("."))));
                            }else {
                                detail.setOpenqty(Integer.parseInt(""+map.get("OPENQTY")));
                            }
                        }
                        if( !"".equals(map.get("PRICE")) && map.get("PRICE") != null  && !"null".equals(map.get("PRICE")) ){
                            detail.setPrice(new BigDecimal( map.get("PRICE")+""));
                        }
                        if( !"".equals(map.get("TOTALPRICE")) && map.get("TOTALPRICE") != null  && !"null".equals(map.get("TOTALPRICE")) ){
                            detail.setTotalprice(new BigDecimal(map.get("TOTALPRICE")+""));
                        }
                        if( !"".equals(map.get("CURRENCY")) && map.get("CURRENCY") != null  && !"null".equals(map.get("CURRENCY"))){
                            detail.setCurrency(map.get("CURRENCY")+"");
                        }
                        if(!"".equals(map.get("COUNTRYOFORIGIN") ) && map.get("COUNTRYOFORIGIN") != null  && !"null".equals(map.get("COUNTRYOFORIGIN"))){
                            detail.setCountryoforigin(map.get("COUNTRYOFORIGIN")+"");
                        }


                        if(map.get("LOTTABLE01") != "" && map.get("LOTTABLE01") != null  && !"null".equals(map.get("LOTTABLE01"))){
                            if((map.get("LOTTABLE01")+"").contains(".")){
                                detail.setLottable01((""+map.get("LOTTABLE01")).substring(0,(""+map.get("LOTTABLE01")).indexOf(".")));
                            }else
                                detail.setLottable01(""+map.get("LOTTABLE01"));
                        }
                        if(map.get("LOTTABLE02") != "" && map.get("LOTTABLE02") != null  && !"null".equals(map.get("LOTTABLE02"))){
                            detail.setLottable02(""+ map.get("LOTTABLE02"));
                        }
                        if(map.get("LOTTABLE03") != "" && map.get("LOTTABLE03") != null  && !"null".equals(map.get("LOTTABLE03"))){
                            detail.setLottable03(""+ map.get("LOTTABLE03"));
                        }
                        if(map.get("LOTTABLE04") != "" && map.get("LOTTABLE04") != null  && !"null".equals(map.get("LOTTABLE04"))){
                            detail.setLottable04(""+ map.get("LOTTABLE04"));
                        }
                        if(map.get("LOTTABLE05") != "" && map.get("LOTTABLE05") != null  && !"null".equals(map.get("LOTTABLE05"))){
                            detail.setLottable05(""+ map.get("LOTTABLE05"));
                        }
                        if(map.get("LOTTABLE06") != "" && map.get("LOTTABLE06") != null  && !"null".equals(map.get("LOTTABLE06"))){
                            detail.setLottable06(""+ map.get("LOTTABLE06"));
                        }
                        if(map.get("LOTTABLE07") != "" && map.get("LOTTABLE07") != null  && !"null".equals(map.get("LOTTABLE07"))){
                            detail.setLottable07(""+ map.get("LOTTABLE07"));
                        }
                        if(map.get("LOTTABLE08") != "" && map.get("LOTTABLE08") != null  && !"null".equals(map.get("LOTTABLE08"))){
                            detail.setLottable08(""+ map.get("LOTTABLE08"));
                        }
                        if(map.get("LOTTABLE09") != "" && map.get("LOTTABLE09") != null  && !"null".equals(map.get("LOTTABLE09"))){
                            detail.setLottable09(""+ map.get("LOTTABLE09"));
                        }
                        if(map.get("LOTTABLE10") != "" && map.get("LOTTABLE10") != null  && !"null".equals(map.get("LOTTABLE10"))){
                            detail.setLottable10(""+ map.get("LOTTABLE10"));
                        }
                        if(map.get("LOTTABLE11") != "" && map.get("LOTTABLE11") != null  && !"null".equals(map.get("LOTTABLE11"))){
                            detail.setLottable11(""+ map.get("LOTTABLE11"));
                        }
                        if(map.get("LOTTABLE12") != "" && map.get("LOTTABLE12") != null  && !"null".equals(map.get("LOTTABLE12"))){
                            detail.setLottable12(""+ map.get("LOTTABLE12"));
                        }
                        jintieOutputDetailCopyMapper.insert(detail);
                    }

                }catch (Exception e){
                    logger.warn("Copy识别数据存放错误"+ywinfo.getYwNo());
                }
               //回写业务表更改状态
               ywinfo.setStatus("已识别待补充");
               jintieYwinfoService.updateById(ywinfo);
               logger.info("识别数据导入数据长度为："+JintieOutputDetail.size()+"，编号:"+ywinfo.getYwNo());
           }catch (Exception e){
               //e.printStackTrace();
               ywinfo.setStatus("识别数据异常");
               ywinfo.setRemark(e.getMessage());
               jintieYwinfoService.updateById(ywinfo);
           }finally {
               //打印到excel

               try {
                   List<String> list1 = new ArrayList<>();
                   Iterator aa = lists.get(0).keySet().iterator();
                   while (aa.hasNext()){
                       list1.add(aa.next()+"");
                   }
                   ExcelUtil.exportToExcel(lists,list1,ywinfo.getYwNo());
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }

        }

    }

    /**
     *
     * @param SKU 料号
     * @param number 数量
     * @param COUNTRYOFORIGIN  原产国
     * @param dataMap
     * @return
     */
    private static Map<String,Object> getXD(String SKU,String number,String COUNTRYOFORIGIN,Map<String, List<Map>> dataMap){
        if(dataMap.get("XDList").size() == 1 && dataMap.get("FPList").size() == 1){
            List<Map> XDList = dataMap.get("XDList");
            if(XDList != null){
                for (int i = 0; i < XDList.size(); i++) {
                    //原产国
                    String XDCOO = XDList.get(i).get("XDCOO")+"";
                    List<Map> XDCommodityList = (List<Map>)XDList.get(i).get("XDCommodityList");
                    if(XDCommodityList != null){
                        for (int j = 0; j < XDCommodityList.size(); j++) {
                            return  XDCommodityList.get(j);
                        }
                    }
                }
            }
        }else {
            List<Map> XDList = dataMap.get("XDList");
            if(XDList != null){
                for (int i = 0; i < XDList.size(); i++) {
                    //原产国
                    String XDCOO = XDList.get(i).get("XDCOO")+"";
                    List<Map> XDCommodityList = (List<Map>)XDList.get(i).get("XDCommodityList");
                    if(XDCommodityList != null){
                        for (int j = 0; j < XDCommodityList.size(); j++) {
                            //箱单料号
                            String XDCommodity_PartNumber = XDCommodityList.get(j).get("XDCommodity_PartNumber")+"";
                            //数量
                            String XDCommodity_GdsQty1 = XDCommodityList.get(j).get("XDCommodity_GdsQty1")+"";
                            //原产国
                            String XDCommodity_COO = XDCommodityList.get(j).get("XDCommodity_COO")+"";
                            if(SKU.equals(XDCommodity_PartNumber) && number.equals(XDCommodity_GdsQty1) ){
                                return  XDCommodityList.get(j);
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
}
