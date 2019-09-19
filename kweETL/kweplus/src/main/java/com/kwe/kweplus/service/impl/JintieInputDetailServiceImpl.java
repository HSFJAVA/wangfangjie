package com.kwe.kweplus.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kwe.kweplus.bo.impl.JintieOutputBo;
import com.kwe.kweplus.dao.JintieBaseInputFieldMapper;
import com.kwe.kweplus.dao.JintieCustomFieldMapper;
import com.kwe.kweplus.dao.JintieInputDetailMapper;
import com.kwe.kweplus.dao.JintieInputHeadMapper;
import com.kwe.kweplus.modal.*;
import com.kwe.kweplus.service.IJintieInputDetailService;
import com.kwe.kweplus.util.ExcelUtil;
import com.kwe.kweplus.util.FileUtil;
import com.kwe.kweplus.util.MapBwanUtil;
import com.kwe.kweplus.util.text.DateUtil;
import com.kwe.kweplus.util.text.MyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.io.File;
import java.io.IOException;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2019-03-22
 */
@Service
@Transactional
public class JintieInputDetailServiceImpl extends ServiceImpl<JintieInputDetailMapper, JintieInputDetail> implements IJintieInputDetailService {

    private Logger logger = LoggerFactory.getLogger(JintieInputDetailServiceImpl.class);

    @Autowired
    JintieInputDetailMapper JintieInputDetailMapper;
    @Autowired
    JintieBaseInputFieldMapper JintieBaseInputFieldMapper;
    @Autowired
    JintieCustomFieldMapper JintieCustomFieldMapper;
    @Autowired
    JintieInputHeadMapper JintieInputHeadMapper;

    @Override
    public boolean saveExcelDate(JintieYwinfo ywinfo, MultipartFile excl) throws IOException {
        ExcelUtil util = new ExcelUtil();
        File file = null;
        if (excl.equals("") || excl.getSize() <= 0) {
            excl = null;
        } else {
            String fileName = excl.getOriginalFilename();
            fileName = ywinfo.getYwNo() + fileName.substring(fileName.indexOf("."), fileName.length());
            String date = DateUtil.toString(new Date(), "YYYYMMdd");
            String path1 = "E:\\kweFile\\excel\\" + File.separator + "in" + File.separator + date + File.separator + fileName;
            file = new File(path1);
            String realPath = "E:\\kweFile\\excel\\" + File.separator + "in" + File.separator + date;
            FileUtil.createFolder(realPath);
            excl.transferTo(file);//存文件
        }
        int a = 0;//计数
        try {
            List<Map<String, Object>> lists = util.readExcel(file, 1, 1);
            lists = util.dataConversion(lists);  //Excel数据补充
            List<JintieBaseInputField> JintieBaseInputFields = JintieBaseInputFieldMapper.getFieldByCustomKey();//基础字段
            //通过模板和客户Id拿到对应字段
            List<JintieCustomField> JintieCustomFields = JintieCustomFieldMapper.getFieldByCustomKeyOfIn(ywinfo.getRemark1(), ywinfo.getRemark2());//客户对应字段
            List<Map<Object, Object>> JintieInputDetail = new ArrayList();
            for (int i = 0; i < lists.size(); i++) {
                Map<Object, Object> map = new HashMap<>();
                for (int j = 0; j < JintieBaseInputFields.size(); j++) {
                    map.put(JintieBaseInputFields.get(j).getJintieField(), lists.get(i).get(JintieBaseInputFields.get(j).getField()));
                }
                for (int j = 0; j < JintieCustomFields.size(); j++) {
                    map.put(JintieCustomFields.get(j).getJintieField(), lists.get(i).get(JintieCustomFields.get(j).getField()));
                }
                JintieInputDetail.add(map);
            }
            logger.info("导入数据长度为：" + JintieInputDetail.size());

            //查询head是否已有
            JintieInputHead head = JintieInputHeadMapper.selectByYwID(ywinfo.getYwNo());
            if (head != null) {
                //清除原有detail
                JintieInputDetailMapper.deleteByYwNo(ywinfo.getYwNo());
                //添加到detail表
                for (int i = 0; i < JintieInputDetail.size(); i++) {
                    Map<Object, Object> map = JintieInputDetail.get(i);
                    JintieInputDetail detail = new JintieInputDetail();
                    detail.setMessagetype("D");
                    detail.setMessagehead(head.getMessagehead());
                    detail.setStoreid(ywinfo.getRemark1());
                    detail.setMessagedate(MyUtils.returnDay());
                    detail.setMessagetime(MyUtils.return24Time());
                    detail.setCopyLineNo(map.get("COPYLINENO") + "");
                    detail.setWlYwno(head.getWlYwno());
                    detail.setStatus(1);
                    if (!"".equals(map.get("CARTONQTY")) && map.get("CARTONQTY") != null && !"null".equals(map.get("CARTONQTY"))) {
                        detail.setCartonqty(map.get("CARTONQTY") + "");
                    }
                    if (!"".equals(map.get("TOTALNETWEIGHT")) && map.get("TOTALNETWEIGHT") != null && !"null".equals(map.get("TOTALNETWEIGHT"))) {
                        detail.setTotalnetweight(new BigDecimal(map.get("TOTALNETWEIGHT") + ""));
                    }
                    if (!"".equals(map.get("TOTALGROSSWEIGHT")) && map.get("TOTALGROSSWEIGHT") != null && !"null".equals(map.get("TOTALGROSSWEIGHT"))) {
                        detail.setTotalgrossweight(new BigDecimal(map.get("TOTALGROSSWEIGHT") + ""));
                    }
                    if (!"".equals(map.get("D_USERDEFINE2")) && map.get("D_USERDEFINE2") != null && !"null".equals(map.get("D_USERDEFINE2"))) {
                        detail.setdUserdefine2(map.get("D_USERDEFINE2") + "");
                    }


                    if (!"".equals(map.get("HSCODE")) && map.get("HSCODE") != null && !"null".equals(map.get("HSCODE"))) {
                        detail.setHscode(map.get("HSCODE") + "");
                    }
                    if (!"".equals(map.get("CHINESENAME")) && map.get("CHINESENAME") != null && !"null".equals(map.get("CHINESENAME"))) {
                        detail.setChinesename(map.get("CHINESENAME") + "");
                    }


                    if (!"".equals(map.get("DOCLINENO")) && map.get("DOCLINENO") != null && !"null".equals(map.get("DOCLINENO"))) {
                        detail.setDoclineno(map.get("DOCLINENO") + "");
                    }
                    if (!"".equals(map.get("PONO")) && map.get("PONO") != null && !"null".equals(map.get("PONO"))) {
                        detail.setPono("" + map.get("PONO"));
                    }


                    if (!"".equals(map.get("CUSTOMERLINENO")) && map.get("CUSTOMERLINENO") != null && !"null".equals(map.get("CUSTOMERLINENO"))) {
                        detail.setCustomerlineno("" + map.get("CUSTOMERLINENO"));
                    }
                    if (!"".equals(map.get("SKU")) && map.get("SKU") != null) {
                        detail.setSku("" + map.get("SKU"));
                    }
                    if (!"".equals(map.get("EXPECTEDQTY")) && map.get("EXPECTEDQTY") != null && !"null".equals(map.get("EXPECTEDQTY"))) {
                        detail.setExpectedqty(map.get("EXPECTEDQTY") + "");
                    }
                    if (!"".equals(map.get("DECLARATIONUNIT")) && map.get("DECLARATIONUNIT") != null && !"null".equals(map.get("DECLARATIONUNIT"))) {
                        detail.setDeclarationunit((String) map.get("DECLARATIONUNIT"));
                    }
                    if (!"".equals(map.get("PRICE")) && map.get("PRICE") != null && !"null".equals(map.get("PRICE"))) {
                        detail.setPrice(new BigDecimal("" + map.get("PRICE")));
                    }
                    if (!"".equals(map.get("TOTALPRICE")) && map.get("TOTALPRICE") != null && !"null".equals(map.get("TOTALPRICE"))) {
                        detail.setTotalprice(new BigDecimal((String) map.get("TOTALPRICE")));
                    }
                    if (!"".equals(map.get("CURRENCY")) && map.get("CURRENCY") != null && !"null".equals(map.get("CURRENCY"))) {
                        detail.setCurrency((String) map.get("CURRENCY"));
                    }
                    if (!"".equals(map.get("COUNTRYOFORIGIN")) && map.get("COUNTRYOFORIGIN") != null && !"null".equals(map.get("COUNTRYOFORIGIN"))) {
                        detail.setCountryoforigin((String) map.get("COUNTRYOFORIGIN"));
                    }
                    if (!"".equals(map.get("GROSSWEIGHT")) && map.get("GROSSWEIGHT") != null && !"null".equals(map.get("GROSSWEIGHT"))) {
                        detail.setGrossweight(new BigDecimal((String) map.get("GROSSWEIGHT")));
                    }
                    if (!"".equals(map.get("NETWEIGHT")) && map.get("NETWEIGHT") != null && !"null".equals(map.get("NETWEIGHT"))) {
                        detail.setNetweight(new BigDecimal((String) map.get("NETWEIGHT")));
                    }
                    if (!"".equals(map.get("CUBIC")) && map.get("CUBIC") != null && !"null".equals(map.get("CUBIC"))) {
                        detail.setCubic(new BigDecimal((String) map.get("CUBIC")));
                    }


                    if (map.get("LOTTABLE01") != "" && map.get("LOTTABLE01") != null && !"null".equals(map.get("LOTTABLE01"))) {
                        detail.setLottable01("" + map.get("LOTTABLE01"));
                    }
                    if (map.get("LOTTABLE02") != "" && map.get("LOTTABLE02") != null && !"null".equals(map.get("LOTTABLE02"))) {
                        detail.setLottable02("" + map.get("LOTTABLE02"));
                    }
                    if (map.get("LOTTABLE03") != "" && map.get("LOTTABLE03") != null && !"null".equals(map.get("LOTTABLE03"))) {
                        detail.setLottable03("" + map.get("LOTTABLE03"));
                    }
                    if (map.get("LOTTABLE04") != "" && map.get("LOTTABLE04") != null && !"null".equals(map.get("LOTTABLE04"))) {
                        detail.setLottable04("" + map.get("LOTTABLE04"));
                    }
                    if (map.get("LOTTABLE05") != "" && map.get("LOTTABLE05") != null && !"null".equals(map.get("LOTTABLE05"))) {
                        detail.setLottable05("" + map.get("LOTTABLE05"));
                    }
                    if (map.get("LOTTABLE06") != "" && map.get("LOTTABLE06") != null && !"null".equals(map.get("LOTTABLE06"))) {
                        detail.setLottable06("" + map.get("LOTTABLE06"));
                    }
                    if (map.get("LOTTABLE07") != "" && map.get("LOTTABLE07") != null && !"null".equals(map.get("LOTTABLE07"))) {
                        detail.setLottable07("" + map.get("LOTTABLE07"));
                    }
                    if (map.get("LOTTABLE08") != "" && map.get("LOTTABLE08") != null && !"null".equals(map.get("LOTTABLE08"))) {
                        detail.setLottable08("" + map.get("LOTTABLE08"));
                    }
                    if (map.get("LOTTABLE09") != "" && map.get("LOTTABLE09") != null && !"null".equals(map.get("LOTTABLE09"))) {
                        detail.setLottable09("" + map.get("LOTTABLE09"));
                    }
                    if (map.get("LOTTABLE10") != "" && map.get("LOTTABLE10") != null && !"null".equals(map.get("LOTTABLE10"))) {
                        detail.setLottable10("" + map.get("LOTTABLE10"));
                    }
                    if (map.get("LOTTABLE11") != "" && map.get("LOTTABLE11") != null && !"null".equals(map.get("LOTTABLE11"))) {
                        detail.setLottable11("" + map.get("LOTTABLE11"));
                    }
                    if (map.get("LOTTABLE12") != "" && map.get("LOTTABLE12") != null && !"null".equals(map.get("LOTTABLE12"))) {
                        detail.setLottable12("" + map.get("LOTTABLE12"));
                    }

                    /**
                     * 尼吉康
                     * “料号” 前加“1P”   末尾去“-W”
                     * “订单号“ 前加“ 1T”
                     * “PO” 前加“K”
                     * “CPN” 前加“P”
                     * “入库发票号” 如6位数字，在第二位后加“-”（例 93-0836）(针对出)
                     */
//                if("C000016".equals(detail.getStoreid())){
//                    //判断是否已经有"1P"
//                    if(detail.getSku() != null && !"".equals(detail.getSku()) ){
//                        if(!"1P".equals(detail.getSku().substring(0,2))){
//                            detail.setSku("1P"+detail.getSku());
//                        }
//                        //末尾去“-W”
//                        if("-W".equals(detail.getSku().substring(detail.getSku().length()-2))){
//                            detail.setSku(detail.getSku().substring(0,detail.getSku().length()-2));
//                        }
//                    }
//                    if(detail.getLottable02() != null && !"".equals(detail.getLottable02()) ){
//                        //订单号“ 前加“ 1T”
//                        if(!"1T".equals(detail.getLottable02().substring(0,2))){
//                            detail.setLottable02("1T"+detail.getLottable02());
//                        }
//                    }
//                    if(detail.getPono() != null && !"".equals(detail.getPono()) ){
//                        //“PO” 前加“K”
//                        if(!"K".equals(detail.getPono().substring(0,1))){
//                            detail.setPono("K"+detail.getPono());
//                        }
//                    }
//                    if(detail.getLottable03() != null && !"".equals(detail.getLottable03()) ){
//                        //“CPN” 前加“P”
//                        if(!"P".equals(detail.getLottable03().substring(0,1))){
//                            detail.setLottable03("P"+detail.getLottable03());
//                        }
//                    }
//
//                }

                    /**
                     * 三垦特殊需求
                     * 序列号1、2、3、4...
                     */
                    if ("C000022".equals(detail.getStoreid())) {
                        detail.setLottable03(i + 1 + "");
                    }
                    a += JintieInputDetailMapper.insert(detail);
                }
//            for (Map<Object,Object> map:JintieInputDetail) {
//
//            }
            } else {
//            //添加head 和 detail

            }
        } catch (Exception e) {
        }
        if (a > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<JintieInputDetail> selectByYwNo(String ywNo) {
        Map map = new HashMap();
        map.put("wl_ywno", ywNo);
        return JintieInputDetailMapper.selectByMap(map);
    }

    @Override
    public List<JintieCustomField> selectByStoreId(String storeid, String type, String modelId) {
        if ("进仓".equals(type)) {
            return JintieCustomFieldMapper.selectByStoreId(storeid, "0", modelId);
        } else {
            return JintieCustomFieldMapper.selectByStoreId(storeid, "1", modelId);
        }
    }

    @Override
    public ReturnMessage reverseExcel(JintieYwinfo info) throws IOException {
        ReturnMessage msg = new ReturnMessage();
        msg.setMessage("导出excel表格成功~");
        msg.setStatus("200");
        //查询详情表,详情存储list。（多条料号）
        List<JintieInputDetail> JintieInputDetails = selectByYwNo(info.getYwNo());
        //创建List<String>类型对象接收表头信息
        List<String> listTotleOld = new ArrayList<>();
        //创建表头表体新字段集合。
        List<String> listTotleNew = new ArrayList<>();
        if (JintieInputDetails.isEmpty()) {
            msg.setStatus(ReturnMessage.STATUS_ERROR);
            msg.setMessage("该进仓编号没有内容。");
            return msg;
        } else {
            //查询进field表，字段替换。
            List<JintieBaseInputField> inputFields = JintieBaseInputFieldMapper.getFieldByCustomKey();
            //查询客户field表，确定需要字段。
            List<JintieCustomField> customFields = JintieCustomFieldMapper.getFieldByCustomKeyOfIn(info.getRemark1(), info.getRemark2());
            //得到对多字段属性。(全小写)
            List<String> YY=new ArrayList<>();
            if(customFields!=null&&customFields.size()>0)
            {
                for (JintieCustomField custom:customFields) {
                    if ("Y".equals(custom.getRemarks3()))
                    {
                        YY.add(custom.getJintieField().replace("_","").toLowerCase());
                    }
                }
            }

            //得到详情表表头属性（反射）。
            if (JintieInputDetails != null && JintieInputDetails.size() > 0) {
                Field[] fields = JintieInputDetails.get(0).getClass().getDeclaredFields();

                for (int j = 0; j < inputFields.size(); j++) {
                    flag:
                    for (int i = 0; i < fields.length; i++) {
                        if (fields[i].getName().toUpperCase().equals(inputFields.get(j).getJintieField())) {
                            listTotleNew.add(inputFields.get(j).getField());
                            listTotleOld.add(inputFields.get(j).getJintieField().toLowerCase());
                            break flag;
                        }
                    }
                }
                for (int k = 0; k < customFields.size(); k++) {
                    customFields.get(k).setJintieField(customFields.get(k).getJintieField().replace("_",""));
                    flag:
                    for (int i = 0; i < fields.length; i++) {
                        if (!customFields.isEmpty() && fields[i].getName().toUpperCase().equals(customFields.get(k).getJintieField())) {
                            listTotleNew.add(customFields.get(k).getField());
                            listTotleOld.add(customFields.get(k).getJintieField().replace("_","").toLowerCase());
                            break flag;
                        }
                    }
                }
            }
            //创建List<Map<String,Object>>类型对象接收表体信息
            List<Map<String, Object>> listContent = new ArrayList<>();
            for (int i = 0; i < JintieInputDetails.size(); i++) {
                //把对象转化成map结构，添加到list集合中。
                Map<String, Object> map = MapBwanUtil.object2Map(JintieInputDetails.get(i));
                listContent.add(map);
            }

            try {
                  ExcelUtil.exportToExcel2(listContent, listTotleNew,listTotleOld, info.getYwNo(),YY);
            } catch (Exception e) {
                msg.setMessage("导出excel表格失败："+e.getMessage());
                msg.setStatus(ReturnMessage.STATUS_ERROR);
                logger.error(e.getMessage());
            }
            return msg;
        }

    }


}
