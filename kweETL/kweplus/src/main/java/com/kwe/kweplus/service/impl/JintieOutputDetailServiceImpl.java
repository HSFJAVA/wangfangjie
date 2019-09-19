package com.kwe.kweplus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kwe.kweplus.dao.*;
import com.kwe.kweplus.modal.*;
import com.kwe.kweplus.service.IJintieOutputDetailService;
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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

import static java.math.BigDecimal.ROUND_HALF_DOWN;

/**
 * <p>
 * 表一 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2019-03-22
 */
@Service
@Transactional
public class JintieOutputDetailServiceImpl extends ServiceImpl<JintieOutputDetailMapper, JintieOutputDetail> implements IJintieOutputDetailService {

    private Logger logger = LoggerFactory.getLogger(JintieOutputDetailServiceImpl.class);

    @Autowired
    JintieOutputDetailMapper JintieOutputDetailMapper;
    @Autowired
    JintieOutputHeadMapper JintieOutputHeadMapper;
    @Autowired
    JintieBaseOutputFieldMapper JintieBaseOutputFieldMapper;
    @Autowired
    JintieCustomFieldMapper JintieCustomFieldMapper;

    @Override
    public boolean saveExcelDate(JintieYwinfo ywinfo, MultipartFile excel)throws IOException {
        ExcelUtil util = new ExcelUtil();
        File file = null;
        if(excel.equals("")||excel.getSize()<=0){
            excel = null;
        }else {
            String fileName = excel.getOriginalFilename();
            fileName = ywinfo.getYwNo() + fileName.substring(fileName.indexOf("."), fileName.length());
            String date = DateUtil.toString(new Date(), "YYYYMMdd");
            String path1 = "E:\\kweFile\\excel\\" + File.separator +"out"+ File.separator + date + File.separator + fileName;
            file = new File(path1);
            String realPath = "E:\\kweFile\\excel\\" + File.separator +"out"+ File.separator + date;
            FileUtil.createFolder(realPath);
            excel.transferTo(file);//存文件
        }
        int a = 0;//计数
        try {
            List<Map<String,Object>> lists = util.readExcel(file,1,1);
            lists = util.dataConversion(lists);  //Excel数据补充
            List<JintieBaseOutputField> JintieBaseInputFields  =  JintieBaseOutputFieldMapper.getFieldByCustomKey();//基础字段
            //通过客户ID 和 模板id拿到对应字段
            List<JintieCustomField> JintieCustomFields =  JintieCustomFieldMapper.getFieldByCustomKeyOfOut(ywinfo.getRemark1(),ywinfo.getRemark2());//客户对应字段
            List<Map<Object,Object>> JintieOutputDetail = new ArrayList();
            for (int i = 0; i < lists.size(); i++) {
                Map<Object,Object> map = new HashMap<>();
                for (int j = 0; j < JintieBaseInputFields.size(); j++) {
                    map.put(JintieBaseInputFields.get(j).getJintieField(),(lists.get(i).get(JintieBaseInputFields.get(j).getField())+"").trim());
                }
                for (int j = 0; j < JintieCustomFields.size(); j++) {
                    map.put(JintieCustomFields.get(j).getJintieField(),(lists.get(i).get(JintieCustomFields.get(j).getField())+"").trim());
                }
                JintieOutputDetail.add(map);
            }
            //查询head是否已有
            JintieOutputHead head = JintieOutputHeadMapper.selectByYwID(ywinfo.getYwNo());
            if(head != null ){
                //清除原有detail
                JintieOutputDetailMapper.deleteByYwNo(ywinfo.getYwNo());
                //添加到detail表
                for (int i = 0; i < JintieOutputDetail.size(); i++) {
                    Map<Object,Object> map = JintieOutputDetail.get(i);
                    JintieOutputDetail detail = new JintieOutputDetail();
                    detail.setWlYwno(head.getWlYwno());
                    detail.setStoreid(ywinfo.getRemark1());
                    detail.setMessagetype("D");
                    detail.setMessagehead(head.getMessagehead());
                    detail.setMessagedate(MyUtils.returnDay());
                    detail.setMessagetime(MyUtils.return24Time());
                    detail.setStatus(1);
                    detail.setCopyLineNo(map.get("COPYLINENO")+"");
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
                        detail.setQtyordered(Integer.parseInt(map.get("QTYORDERED")+"") );
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
                    if(!"".equals(map.get("DECLARATIONUNIT") ) && map.get("DECLARATIONUNIT") != null  && !"null".equals(map.get("DECLARATIONUNIT"))){
                        detail.setDeclarationunit(map.get("DECLARATIONUNIT")+"");
                    }


                    if(map.get("LOTTABLE01") != "" && map.get("LOTTABLE01") != null  && !"null".equals(map.get("LOTTABLE01"))){
                        detail.setLottable01( map.get("LOTTABLE01")+"");
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
//                    if("C000022".equals(detail.getStoreid())){
//                        detail.setLottable02(i+1+"");
//                    }

                    a+= JintieOutputDetailMapper.insert(detail);
                }
            }else {
//            //添加head 和 detail
            }
            return true;
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        if(a > 0){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public List<JintieOutputDetail> selectByYwNo(String ywNo) {
        Map map = new HashMap();
        map.put("wl_ywno",ywNo);
        return JintieOutputDetailMapper.selectByMap(map);
    }

    @Override
    public List<JintieCustomField> selectByStoreId(String storeid, String type,String modelId) {
        if("进仓".equals(type)){
            return JintieCustomFieldMapper.selectByStoreId(storeid,"0",modelId);
        }else {
            return JintieCustomFieldMapper.selectByStoreId(storeid,"1",modelId);
        }
    }



    @Override
    public ReturnMessage reverseExcel(JintieYwinfo info) throws IOException {
            ReturnMessage msg = new ReturnMessage();
            msg.setStatus("200");
            msg.setMessage("导出excel表格成功~");
            //查询详情表。
            List<JintieOutputDetail> JintieOutputDetail = selectByYwNo(info.getYwNo());
            //创建List<String>类型对象接收表头信息
            List<String> listTotle = new ArrayList<>();
            //创建一个新的表头，筛选表头。
            List<String> listTotleNew = new ArrayList<>();


            if(JintieOutputDetail.isEmpty()){
                msg.setStatus("400");
                msg.setMessage("该出仓编号没有内容。");
                return msg;
            }else {
                //查询进field表，字段替换。
                List<JintieBaseOutputField> outputFields = JintieBaseOutputFieldMapper.getFieldByCustomKey();
                //查询客户field表，确定需要字段。
                List<JintieCustomField> customFields = JintieCustomFieldMapper.getFieldByCustomKeyOfOut(info.getRemark1(), info.getRemark2());
                //得到对多字段属性。(全小写)
                List<String> YY=new ArrayList<>();
                for (JintieCustomField custom:customFields) {
                    if ("Y".equals(custom.getRemarks3()))
                    {
                        YY.add(custom.getJintieField().toLowerCase());
                    }
                }
                if (JintieOutputDetail != null && JintieOutputDetail.size() > 0) {
                    //得到详情表表头属性（反射）。
                    Field[] fields = JintieOutputDetail.get(0).getClass().getDeclaredFields();
                    for (int j = 0; j < outputFields.size(); j++) {
                        flag:
                        for (int i = 0; i < fields.length; i++) {
                            if (fields[i].getName().toUpperCase().equals(outputFields.get(j).getJintieField())) {
                                listTotleNew.add(outputFields.get(j).getField());
                                listTotle.add(outputFields.get(j).getJintieField().toLowerCase());
                                break flag;
                            }
                        }
                    }
                    for (int k = 0; k < customFields.size(); k++) {
                        flag:
                        for (int i = 0; i < fields.length; i++) {
                            if (!customFields.isEmpty() && fields[i].getName().toUpperCase().equals(customFields.get(k).getJintieField())) {
                                listTotleNew.add(customFields.get(k).getField());
                                listTotle.add(customFields.get(k).getJintieField().toLowerCase());
                                break flag;
                            }
                        }
                    }
                }

                //创建List<Map<String,Object>>类型对象接收表体信息
            List<Map<String,Object>> listContent=new ArrayList<>();
            for (int i = 0; i < JintieOutputDetail.size(); i++) {
                //把对象转化成map结构，添加到list集合中。
                Map<String, Object> map =MapBwanUtil.object2Map(JintieOutputDetail.get(i));
                listContent.add(map);
            }

            try {
                ExcelUtil.exportToExcel2(listContent,listTotleNew,listTotle,info.getYwNo(),YY);
            } catch (Exception e) {
                msg.setMessage("导出excel表格失败："+e.getMessage());
                msg.setStatus(ReturnMessage.STATUS_ERROR);
                logger.error(e.getMessage());
            }
            return msg;
            }
        }
}
