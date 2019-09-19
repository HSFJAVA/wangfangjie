package com.kwe.kweplus.bo.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kwe.kweplus.bo.IJintieCommonBo;
import com.kwe.kweplus.bo.IJintieInputBo;
import com.kwe.kweplus.controller.KweDictController;
import com.kwe.kweplus.controller.indexController;
import com.kwe.kweplus.modal.*;
import com.kwe.kweplus.service.*;
import com.kwe.kweplus.service.impl.KweDictServiceImpl;
import com.kwe.kweplus.util.CalendarUtils;
import com.kwe.kweplus.util.MyUtils;
import com.kwe.kweplus.util.SetGet;
import com.kwe.kweplus.util.ocrUtil.OcrUtil;
import com.kwe.kweplus.util.text.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class JintieInputBo implements IJintieInputBo {
    @Autowired
    private IJintieYwinfoService jintieYwinfoService;
    @Autowired
    private IJintieInputHeadService jintieInputHeadService;
    @Autowired
    private IJintieInputDetailService jintieInputDetailService;
    @Autowired
    private IJintieCommonBo commonBo;
    @Autowired
    private IJintieOutputSerialService jintieOutputSerialService;
    @Autowired
    IJintieCustomService IJintieCustomService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    JintieDataComparisonService JintieDataComparisonService;
    @Autowired
    KweDictService kweDictService;

    private Logger logger = LoggerFactory.getLogger(JintieInputBo.class);
    ReturnMessage msg = new ReturnMessage();
    /**
     * 获取head信息
     * @param wlywno
     * @return
     */
    private JintieInputHead getHeadByYwNo(String wlywno){
        QueryWrapper<JintieInputHead> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(JintieInputHead::getWlYwno, wlywno);
        JintieInputHead head = jintieInputHeadService.getOne(queryWrapper);
        return head;
    }
    /**
     * 根据业务编号返回表头,表体信息
     * @param wlywno
     * @param type
     * @return
     * @throws Exception
     */
    @Override
    public Map getDetailByNo(String wlywno, String type) throws Exception{
        JintieInputHead head = getHeadByYwNo(wlywno);
        Map<String, Object> resultMap = new HashMap<>();
        KweDictServiceImpl kweDictServiceImpl = new KweDictServiceImpl();
        if(head==null){
            resultMap.put("head", new ArrayList<>());
            resultMap.put("detail", new ArrayList<>());
            resultMap.put("serial", new ArrayList<>());
            return resultMap;
        }

        QueryWrapper<JintieInputDetail> detailQueryWrapper = new QueryWrapper<>();
        detailQueryWrapper.lambda().eq(JintieInputDetail::getMessagehead, head.getMessagehead());
        List<JintieInputDetail> detailList = jintieInputDetailService.list(detailQueryWrapper);
        detailList = detailList==null ? new ArrayList<>():detailList;

        QueryWrapper<JintieOutputSerial> serialQueryWrapper = new QueryWrapper<>();
        serialQueryWrapper.lambda().eq(JintieOutputSerial::getWlYwno, wlywno);
        List<JintieOutputSerial> serialList = jintieOutputSerialService.list(serialQueryWrapper);

        List<JintieInputHead> headList = new ArrayList<JintieInputHead>();
        headList.add(head);
        List<JintieInputDetail> jintieInputDetails = analyzeDetailList(detailList,type);
        List<KweDict> kweDicts = kweDictService.getKweDictList(jintieYwinfoService.selectByYwNo(wlywno).getRemark1());
        for (int i = 0; i < jintieInputDetails.size(); i++) {
            Map map = new HashMap();
            JintieInputDetail jintieInputDetail = jintieInputDetails.get(i);
            String sku = jintieInputDetail.getSku();
            String hscode = jintieInputDetail.getHscode();
            String chinesename = jintieInputDetail.getChinesename();
            String declarationunit = jintieInputDetail.getDeclarationunit();
            String currency = jintieInputDetail.getCurrency();
            String countryoforigin = jintieInputDetail.getCountryoforigin();
            String netweight = jintieInputDetail.getNetweight()+"";
            String price = jintieInputDetail.getPrice()+"";
            map.put("sku",false);
            map.put("hscode",false);
            map.put("chhinesename",false);
            map.put("declarationunit",false);
            map.put("currency",false);
            map.put("countryoforigin",false);
            map.put("netweight",false);
            map.put("price",false);
            map.put("status","-1");
            try {
                for (KweDict kweDict:kweDicts) {
                    String dictSku = kweDict.getSku();
                        if(!StringUtils.isEmpty(sku) && !StringUtils.isEmpty(dictSku)){
                            if(sku.equals(dictSku)){
                                map.put("status",kweDict.getStatus());
                                map.put("sku",true);
                                if(!StringUtils.isEmpty(hscode) && hscode.equals(kweDict.getHscode())){
                                    map.put("hscode",true);
                                }
                                if(!StringUtils.isEmpty(chinesename) && hscode.equals(kweDict.getChinesename())){
                                    map.put("chinesename",true);
                                }
                                if(!StringUtils.isEmpty(declarationunit) && hscode.equals(kweDict.getUnitEn())){
                                    map.put("declarationunit",true);
                                }
                                if(!StringUtils.isEmpty(currency) ){
                                    List<String> list = kweDictServiceImpl.toArray(kweDict.getCurrency());
                                    if(list != null && list.size() >0) {
                                        for (int j = 0; j < list.size(); j++) {
                                            if(hscode.equals(list.get(j))){
                                                map.put("currency",true);
                                                break;
                                            }
                                        }
                                    }
                                }
                                if(!StringUtils.isEmpty(countryoforigin) ){
                                    List<String> list = kweDictServiceImpl.toArray(kweDict.getCoo());
                                    if(list != null && list.size() >0) {
                                        for (int j = 0; j < list.size(); j++) {
                                            if(countryoforigin.equals(list.get(j))){
                                                map.put("countryoforigin",true);
                                                break;
                                            }
                                        }
                                    }
                                }
                                if(!StringUtils.isEmpty(netweight) && hscode.equals(kweDict.getNetweight())){
                                    map.put("netweight",true);
                                }
                                if(!StringUtils.isEmpty(price) && hscode.equals(kweDict.getPrice())){
                                    map.put("price",true);
                                }
                            }
                        }
                }
                jintieInputDetail.setRemark(map);
            }catch (Exception e){
                jintieInputDetail.setRemark(map);
            }
        }
        resultMap.put("head", headList);
        resultMap.put("detail", jintieInputDetails);
        resultMap.put("serial", serialList);
        return resultMap;
    }
    /**
     * 分析表体与Batch的关系.
     * @param list
     * @return
     */
    private List<JintieInputDetail> analyzeDetailList( List<JintieInputDetail> list,String type) throws Exception {
            List<JintieInputDetail> list2 = new ArrayList<>();
        Map<String,List<JintieInputDetail>> map = new HashMap<>();
        for(JintieInputDetail jod:list){
            String key= jod.getCopyLineNo();
            List<JintieInputDetail> l = map.get(key);
            if(l==null){
                l=new ArrayList<>();
                map.put(key,l);
            }
            l.add(jod);
        }
        if(map.isEmpty()) {
            return list2;
        }
        for(String key:map.keySet()){
            List<JintieInputDetail> listx = map.get(key);
            List<Map> maps = new ArrayList<>();
            //获得模板ID
            String modelId = jintieYwinfoService.selectByYwNo(listx.get(0).getWlYwno()).getRemark2();
            List<JintieCustomField> fieids = jintieInputDetailService.selectByStoreId(listx.get(0).getStoreid(),type,modelId);
            List<String> fieid = new ArrayList();
            for (JintieCustomField j:fieids) {
                fieid.add(""+j.getJintieField().toLowerCase());
            }
            for(JintieInputDetail o:listx){
                Map map1 = new HashMap();
                for (String str:fieid) {
                    if(SetGet.getGetMethod(o,str) != null ){
                        map1.put(str,SetGet.getGetMethod(o,str));
                    }
                }
                if(map1.size() > 0){
                    maps.add(map1);
                }
            }
            JintieInputDetail j = listx.get(0);
            j.setRemark1(maps);
            list2.add(j);
        }

        Comparator<JintieInputDetail> comparator = new Comparator<JintieInputDetail>() {

            @Override
            public int compare(JintieInputDetail o1, JintieInputDetail o2) {
                if(Integer.parseInt(o1.getPid()+"") > Integer.parseInt(o2.getPid()+"")){
                    return 1;
                }else {
                    return -1;
                }
            }
        };
        Collections.sort(list2,comparator);
        return list2;
    }



    /**
     * 新增时调用
     * @param data
     * @return
     * @throws IllegalAccessException
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = IllegalAccessException.class)
    public String insert(jintie_list data) throws IllegalAccessException{
        logger.info("Insert,新增编号："+data.getInputHeadList().get(0).getWlYwno()+",操作人："+data.getOp_user());
        HttpSession session=request.getSession();
        try {
            String date1 = "";
            String time1 = "";
            JintieInputHead head = data.getInputHeadList().get(0);

            QueryWrapper<JintieYwinfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(JintieYwinfo::getYwNo, head.getWlYwno());
            JintieYwinfo info = jintieYwinfoService.getOne(queryWrapper);
            info.setStatus("待审核");
            info.setUpdateUser(data.getOp_user());
            info.setUpdateUserEmp(session.getAttribute("user")+"");
            info.setUpdateDate(CalendarUtils.getLocalDateTime(new Date()));
            jintieYwinfoService.saveOrUpdate(info);

            SimpleDateFormat sfm = new SimpleDateFormat("yyyy-MM-dd");
            String Date = sfm.format(new Date());
            SimpleDateFormat fm = new SimpleDateFormat("HHmmss");
            String time = fm.format(new Date());
            head.setMessagedate(Date);
            head.setMessagetime(time);
            head.setMessagehead(commonBo.getMessageHead());
            head.setMessagetype("H");
            head.setDoctype("01");
            head.setStoreid(info.getRemark1());
            head.setStatus(1);
            head.setAsncreationtime(CalendarUtils.getFormatDate(CalendarUtils.Y_M_DHMS_LONG,CalendarUtils.localDateTimeToUdate(LocalDateTime.now())));
            jintieInputHeadService.saveOrUpdate(head);
            int a = 0;
            for (int i = 0; i < data.getInputDetailList().size(); i++) {
                JintieInputDetail detail = data.getInputDetailList().get(i);
                detail.setMessagetype("D");
                detail.setMessagehead(head.getMessagehead());
                detail.setMessagedate(date1);
                detail.setMessagetime(time1);
                detail.setStoreid(info.getRemark1());
                detail.setWlYwno(head.getWlYwno());
                detail.setStatus(1);
                detail.setCopyLineNo(i+"");
                //特殊客户修改

                /**
                 * 尼吉康
                 * “料号” 前加“1P”   末尾去“-W”
                 * “订单号“ 前加“ 1T”
                 * “PO” 前加“K”
                 * “CPN” 前加“P”
                 * “入库发票号” 如6位数字，在第二位后加“-”（例 93-0836）(针对出)
                 */
                //功能关闭
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

                if(detail.getRemark1()!=null&&!detail.getRemark1().isEmpty()){
                    List<JintieInputDetail> list = new ArrayList<>();
                    for(Map map:detail.getRemark1()){
                        a++;
                        JintieInputDetail obj = new JintieInputDetail();
                        obj.setCopyLineNo(i+"");
                        BeanUtils.copyProperties(detail,obj);
                        Iterator<String> it = map.keySet().iterator();
                        while (it.hasNext()){
                            String key = it.next();
                            String value = map.get(key)+"";
                            if(!value.isEmpty()){
                                if(key == "cartonqty"){
                                    obj.setCartonqty(value);
                                    continue;
                                }
                                if(key == "netweight"){
                                    obj.setNetweight(new BigDecimal(value));
                                    continue;
                                }
                                if(key == "grossweight"){
                                    obj.setGrossweight(new BigDecimal(value));
                                    continue;
                                }
                                if(key == "d_userdefine2"){
                                    obj.setdUserdefine2(value);
                                    continue;
                                }

                                if(key == "lottable01"){
                                    obj.setLottable01(value);
                                    continue;
                                }
                                if(key == "lottable02"){
                                    obj.setLottable02(value);
                                    continue;
                                }
                                if(key == "lottable03"){
                                    obj.setLottable03(value);
                                    continue;
                                }
                                if(key == "lottable04"){
                                    obj.setLottable04(value);
                                    continue;
                                }
                                if(key == "lottable05"){
                                    obj.setLottable05(value);
                                }
                                if(key == "lottable06"){
                                    obj.setLottable06(value);
                                    continue;
                                }
                                if(key == "lottable07"){
                                    obj.setLottable07(value);
                                }
                                if(key == "lottable08"){
                                    obj.setLottable08(value);
                                    continue;
                                }
                                if(key == "lottable09"){
                                    obj.setLottable09(value);
                                    continue;
                                }
                                if(key == "lottable10"){
                                    obj.setLottable10(value);
                                    continue;
                                }
                                if(key == "lottable11"){
                                    obj.setLottable11(value);
                                    continue;
                                }
                                if(key == "lottable12"){
                                    obj.setLottable12(value);
                                    continue;
                                }

                            }else {
                            }
                        }

                        /**
                         * 三垦特殊需求
                         * 序列号1、2、3、4...
                         * 8.12号更新
                         * 原序列号为Lottable02变更为Lottable03
                         */
                        if("C000022".equals(obj.getStoreid())){
                            obj.setLottable03(a+"");
                        }
                        //6.27更新
                        //不存在分金额等字段时候 将金额写道分金额字段
                        if(obj.getCartonqty() == null || "".equals(obj.getCartonqty())
                                && obj.getNetweight() == null || "".equals(obj.getNetweight())
                                && obj.getGrossweight() == null || "".equals(obj.getGrossweight())
                                && obj.getdUserdefine2() == null || "".equals(obj.getdUserdefine2())){

                            obj.setCartonqty(obj.getExpectedqty());
                            if(!"".equals(obj.getTotalprice()) && !"null".equals(obj.getTotalprice()) && obj.getTotalprice()!= null){
                                obj.setdUserdefine2(obj.getTotalprice()+"");
                            }
                            obj.setGrossweight(obj.getTotalgrossweight());
                            obj.setNetweight(obj.getTotalnetweight());
                        }
                        list.add(obj);
                    }
                    jintieInputDetailService.saveBatch(list);
                }else{
                    //6.27更新
                    //不存在分金额等字段时候 将金额写道分金额字段
                    detail.setCartonqty(detail.getExpectedqty());
                    if(!"".equals(detail.getTotalprice()) && !"null".equals(detail.getTotalprice()) && detail.getTotalprice()!= null){
                        detail.setdUserdefine2(detail.getTotalprice()+"");
                    }
                    detail.setGrossweight(detail.getTotalgrossweight());
                    detail.setNetweight(detail.getTotalnetweight());
                    jintieInputDetailService.saveOrUpdate(detail);
                }
            }
            return "saveNice";
        } catch (Exception ex) {
            logger.error("进仓数据新增失败!",ex);
            throw new IllegalAccessException(ex.getMessage());
        }
    }

    /**
     * 更新时调用
     * @param data
     * @return
     * @throws IllegalAccessException
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = IllegalAccessException.class)
    public String update(jintie_list data) throws IllegalAccessException{
        logger.info("Update,修改单号："+data.getInputHeadList().get(0).getWlYwno()+",操作人："+data.getOp_user());
        try {
            JintieInputHead chead =data.getInputHeadList().get(0) ;
            SimpleDateFormat sfm = new SimpleDateFormat("yyyy-MM-dd");
            String Date = sfm.format(new Date());
            SimpleDateFormat fm = new SimpleDateFormat("HHmmss");
            String time = fm.format(new Date());
            chead.setMessagedate(Date);
            chead.setMessagetime(time);
//            Boolean chanageModify = false;
//            if(chead.getStatus()==6){
//                chead.setStatus(5);
//                chead.setUpdateflag("U");
//                chanageModify = true;
//            }
            jintieInputHeadService.saveOrUpdate(chead);

            QueryWrapper<JintieInputDetail> queryWrapper2 = new QueryWrapper<JintieInputDetail>();
            queryWrapper2.lambda().eq(JintieInputDetail::getMessagehead, chead.getMessagehead());
            List<JintieInputDetail> oldList = jintieInputDetailService.list(queryWrapper2);
            Boolean hasBatch=false;
            for(JintieInputDetail jd: data.getInputDetailList()){
                if(MyUtils.isNotEmpty(jd.getRemark1())){
                    hasBatch=true;
                    break;
                }
            }

            for(JintieInputDetail jd: oldList){
                if(MyUtils.isNotEmpty(jd.getRemark1())){
                    hasBatch=true;
                    break;
                }
            }
            if(hasBatch){
                jintieInputDetailService.remove(queryWrapper2);
                int a = 0;
                for (int i = 0; i < data.getInputDetailList().size(); i++) {
                    JintieInputDetail detail =data.getInputDetailList().get(i);
                    detail.setPid(null);
                    detail.setCopyLineNo(i+"");
                    detail.setMessagetype("D");
                    detail.setMessagedate(chead.getMessagedate());
                    detail.setMessagetime(chead.getMessagetime());
                    detail.setStoreid(chead.getStoreid());
                    detail.setStatus(chead.getStatus());
                    detail.setWlYwno(chead.getWlYwno());


                    if(detail.getRemark1()!=null&&!detail.getRemark1().isEmpty()){
                        List<JintieInputDetail> list = new ArrayList<>();
                        for(Map map:detail.getRemark1()){
                                a++;
                                JintieInputDetail obj = new JintieInputDetail();
                                BeanUtils.copyProperties(detail,obj);
                                obj.setPid(null);
                                obj.setCopyLineNo(i+"");
                                obj.setWlYwno(chead.getWlYwno());
                                obj.setStatus(chead.getStatus());
                                if(map.get("cartonqty") != null && map.get("cartonqty") != ""){
                                    obj.setCartonqty(map.get("cartonqty").toString());
                                }
                                if( !"".equals(map.get("netweight")) && map.get("netweight") != null){
                                    obj.setNetweight(new BigDecimal(map.get("netweight").toString()));
                                }
                                if( !"".equals(map.get("grossweight")) && map.get("grossweight") != null){
                                    obj.setGrossweight(new BigDecimal(map.get("grossweight").toString()));
                                }
                                if(map.get("d_userdefine2") != null && map.get("d_userdefine2") != ""){
                                    obj.setdUserdefine2(map.get("d_userdefine2").toString());
                                }

                                if(map.get("lottable01") != null && map.get("lottable01") != ""){
                                    obj.setLottable01(map.get("lottable01").toString());
                                }
                                if(map.get("lottable02") != null && map.get("lottable02") != ""){
                                    obj.setLottable02(map.get("lottable02").toString());
                                }
                                if(map.get("lottable03") != null && map.get("lottable03") != ""){
                                    obj.setLottable03(map.get("lottable03").toString());
                                }
                                if(map.get("lottable04") != null && map.get("lottable04") != ""){
                                    obj.setLottable04(map.get("lottable04").toString());
                                }
                                if(map.get("lottable05") != null && map.get("lottable05") != ""){
                                    obj.setLottable05(map.get("lottable05").toString());
                                }
                                if(map.get("lottable06") != null && map.get("lottable06") != ""){
                                    obj.setLottable06(map.get("lottable06").toString());
                                }
                                if(map.get("lottable07") != null && map.get("lottable07") != ""){
                                    obj.setLottable07(map.get("lottable07").toString());
                                }
                                if(map.get("lottable08") != null && map.get("lottable08") != ""){
                                    obj.setLottable08(map.get("lottable08").toString());
                                }
                                if(map.get("lottable09") != null && map.get("lottable09") != ""){
                                    obj.setLottable09(map.get("lottable09").toString());
                                }
                                if(map.get("lottable10") != null && map.get("lottable10") != ""){
                                    obj.setLottable10(map.get("lottable10").toString());
                                }
                                if(map.get("lottable11") != null && map.get("lottable11") != ""){
                                    obj.setLottable11(map.get("lottable11").toString());
                                }
                                if(map.get("lottable12") != null && map.get("lottable12") != ""){
                                    obj.setLottable12(map.get("lottable12").toString());
                                }
                                /**
                                 * 三垦特殊需求
                                 * 序列号1、2、3、4...
                                 * 8.12号更新
                                 * 原序列号为Lottable02变更为Lottable03
                                 */

                                if("C000022".equals(obj.getStoreid())){
                                    obj.setLottable03(a+"");
                                }
                            //6.27更新
                            //不存在分金额等字段时候 将金额写道分金额字段
                            if(!obj.getRemark1().get(0).keySet().contains("cartonqty") && !obj.getRemark1().get(0).keySet().contains("netweight ")
                                    && !obj.getRemark1().get(0).keySet().contains("grossweight ") && !obj.getRemark1().get(0).keySet().contains("d_userdefine2")
                                    ){
                                    obj.setCartonqty(obj.getExpectedqty());
                                    if(!"".equals(obj.getTotalprice()) && !"null".equals(obj.getTotalprice()) && obj.getTotalprice()!= null){
                                        obj.setdUserdefine2(obj.getTotalprice()+"");
                                    }
                                    obj.setGrossweight(obj.getTotalgrossweight());
                                    obj.setNetweight(obj.getTotalnetweight());
                                }
                            list.add(obj);
                        }
                        jintieInputDetailService.saveOrUpdateBatch(list);
                    }else{
                        detail.setCartonqty(detail.getExpectedqty());
                        if(!"".equals(detail.getTotalprice()) && !"null".equals(detail.getTotalprice()) && detail.getTotalprice()!= null){
                            detail.setdUserdefine2(detail.getTotalprice()+"");
                        }
                        detail.setGrossweight(detail.getTotalgrossweight());
                        detail.setNetweight(detail.getTotalnetweight());
                        jintieInputDetailService.saveOrUpdate(detail);
                    }
                }

            }else{
                //没有remark1
                jintieInputDetailService.remove(queryWrapper2);
                for (int i = 0; i <  data.getInputDetailList().size(); i++) {
                    JintieInputDetail detail = data.getInputDetailList().get(i);
                    detail.setPid(null);
                    detail.setMessagetype("D");
                    detail.setMessagedate(chead.getMessagedate());
                    detail.setMessagetime(chead.getMessagetime());
                    detail.setStoreid(chead.getStoreid());
                    detail.setStatus(chead.getStatus());
                    detail.setWlYwno(chead.getWlYwno());
                    detail.setCopyLineNo(i+"");
                    //6.27更新
                    //不存在分金额等字段时候 将金额写道分金额字段
                    detail.setCartonqty(detail.getExpectedqty());
                    if(!"".equals(detail.getTotalprice()) && detail.getTotalprice() != null && !"".equals(detail.getTotalprice())){
                        detail.setdUserdefine2(detail.getTotalprice()+"");
                    }
                    detail.setGrossweight(detail.getTotalgrossweight());
                    detail.setNetweight(detail.getTotalnetweight());
                    /**
                     * 尼吉康
                     * “料号” 前加“1P”   末尾去“-W”
                     * “订单号“ 前加“ 1T”
                     * “PO” 前加“K”
                     * “CPN” 前加“P”
                     * “入库发票号” 如6位数字，在第二位后加“-”（例 93-0836）(针对出)
                     */
                    //功能关闭
//                    if("C000016".equals(detail.getStoreid())){
//                        //判断是否已经有"1P"
//                        if(detail.getSku() != null && !"".equals(detail.getSku()) ){
//                            if(!"1P".equals(detail.getSku().substring(0,2))){
//                                detail.setSku("1P"+detail.getSku());
//                            }
//                            //末尾去“-W”
//                            if("-W".equals(detail.getSku().substring(detail.getSku().length()-2))){
//                                detail.setSku(detail.getSku().substring(0,detail.getSku().length()-2));
//                            }
//                        }
//                        if(detail.getLottable02() != null && !"".equals(detail.getLottable02()) ){
//                            //订单号“ 前加“ 1T”
//                            if(!"1T".equals(detail.getLottable02().substring(0,2))){
//                                detail.setLottable02("1T"+detail.getLottable02());
//                            }
//                        }
//                        if(detail.getPono() != null && !"".equals(detail.getPono()) ){
//                            //“PO” 前加“K”
//                            if(!"K".equals(detail.getPono().substring(0,1))){
//                                detail.setPono("K"+detail.getPono());
//                            }
//                        }
//                        if(detail.getLottable03() != null && !"".equals(detail.getLottable03()) ){
//                            //“CPN” 前加“P”
//                            if(!"P".equals(detail.getLottable03().substring(0,1))){
//                                detail.setLottable03("P"+detail.getLottable03());
//                            }
//                        }
//                    }
                    jintieInputDetailService.saveOrUpdate( data.getInputDetailList().get(i));
                }
            }

            if (null != data.getOutputSerialList()) {
                for (JintieOutputSerial serial : data.getOutputSerialList()) {
//                    if(chanageModify){
//                        serial.setStatus(5);
//                        serial.setUpdateflag("U");
//                    }
                    jintieOutputSerialService.saveOrUpdate(serial);
                }
            }
            HttpSession session=request.getSession();
            QueryWrapper<JintieYwinfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(JintieYwinfo::getYwNo, chead.getWlYwno());
            JintieYwinfo info = jintieYwinfoService.getOne(queryWrapper);
            info.setUpdateUser(data.getOp_user());
            info.setUpdateUserEmp(session.getAttribute("user")+"");
            info.setUpdateDate(CalendarUtils.getLocalDateTime(new Date()));
            if(chead.getStatus()==5)
                info.setStatus("已修改");
            else if(chead.getStatus()==1)
                info.setStatus("待审核");
            if(info.getRemark4() != null && !"".equals(info.getRemark4()) &&  !"null".equals(info.getRemark4())){
                String remarks6 = JintieDataComparisonService.DataComparison(info.getYwNo());
                info.setRemark6(remarks6);
            }
            jintieYwinfoService.saveOrUpdate(info);
            return "updateNice";
        } catch (Exception ex) {
            logger.error("出仓更新数据出错!",ex);
            throw new IllegalAccessException(ex.getMessage());
        }
    }

    /**
     * 删除整票业务,需要同时删除表头及表体
     * @param ywno
     * @param ywInfo
     * @throws IllegalAccessException
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = IllegalAccessException.class)
    public void remove(String ywno, JintieYwinfo ywInfo) throws IllegalAccessException{
        try{
//            jintieYwinfoService.removeById(ywInfo.getId());
            jintieYwinfoService.saveOrUpdate(ywInfo);
            //表头删除
            JintieInputHead head = getHeadByYwNo(ywno);
            if(head==null)
                return;
            head.setStatus(7);
            jintieInputHeadService.saveOrUpdate(head);
            //表体删除
            QueryWrapper<JintieInputDetail> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.lambda().eq(JintieInputDetail::getMessagehead, head.getMessagehead());
            List<JintieInputDetail> list = jintieInputDetailService.list(queryWrapper2);
            if(MyUtils.isNotEmpty(list)){
                for(JintieInputDetail jid:list){
                    jid.setStatus(head.getStatus());
                }
                jintieInputDetailService.saveOrUpdateBatch(list);
            }
        }catch(Exception e){
            logger.error("进仓表头表体删除失败!",e);
            throw e;
        }
    }

    /**
     * 审核流程
     * @param wlywno
     * @param ywinfo
     * @throws IllegalAccessException
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = IllegalAccessException.class)
    public void audit(String wlywno, JintieYwinfo ywinfo) throws IllegalAccessException{
        try{
            jintieYwinfoService.saveOrUpdate(ywinfo);
            JintieInputHead head = getHeadByYwNo(wlywno);
            if(ywinfo.getStatus().equals("待审核")){
                head.setStatus(1);
                head.setUpdateflag(null);
            }else if(ywinfo.getStatus().equals("待修改")){
                head.setStatus(6);
                head.setUpdateflag(null);
            }else if(ywinfo.getStatus().equals("已审核")){//己审核
                head.setStatus(2);
                head.setUpdateflag("I");
            }else{//己修改
                head.setStatus(5);
                head.setUpdateflag("U");
            }
            jintieInputHeadService.saveOrUpdate(head);
            QueryWrapper<JintieInputDetail> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.lambda().eq(JintieInputDetail::getMessagehead, head.getMessagehead());
            List<JintieInputDetail> list = jintieInputDetailService.list(queryWrapper2);
            if (list!=null&&!list.isEmpty()) {
                for (JintieInputDetail detail : list) {
                    detail.setStatus(head.getStatus());
                    detail.setUpdateflag(head.getUpdateflag());
                    jintieInputDetailService.saveOrUpdate(detail);
                }
            }
        }catch (Exception e){
            logger.error("进仓审核失败!",e);
            throw new IllegalAccessException(e.getMessage());
        }
    }

    /**
     * 更新业务表同时需要处理head情况
     * @param ywinfo
     * @throws IllegalAccessException
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = IllegalAccessException.class)
    public void updateYw(JintieYwinfo ywinfo) throws IllegalAccessException{
        ywinfo.setUpdateDate(CalendarUtils.getLocalDateTime(new Date()));
        jintieYwinfoService.saveOrUpdate(ywinfo);
        JintieInputHead head = getHeadByYwNo(ywinfo.getYwNo());
        if(head==null||head.getAsngroup().equals(ywinfo.getCustomNo()))
            return;
        head.setAsngroup(ywinfo.getCustomNo());
        jintieInputHeadService.saveOrUpdate(head);
    }

    @Override
    public void saveInputUpdate(JintieYwinfo ywinfo) {
        jintieInputHeadService.saveInputUpdate(ywinfo.getCustomNo(),ywinfo.getYwNo());
    }
    public String getCustomKey(String customName){
        JintieCustom JintieCustom = IJintieCustomService.getCustomId(customName);
        return JintieCustom.getKey();
    }
    @Override
    public ReturnMessage savaAs(JintieYwinfo ywinfo) {
        try {
            HttpSession session=request.getSession();
            indexController indexController = new indexController();
            String date1 =  CalendarUtils.getFormatDate(CalendarUtils.Y_M_D_LONG,CalendarUtils.localDateTimeToUdate(ywinfo.getCreateDateTime()));
            String time1 = CalendarUtils.getFormatDate(CalendarUtils.TIME_FORMAT,CalendarUtils.localDateTimeToUdate(ywinfo.getCreateDateTime()));
            SimpleDateFormat sfm = new SimpleDateFormat("yyyy-MM-dd");
            String createDate = sfm.format(new Date());
            JintieInputHead head = jintieInputHeadService.selectByYwNo(ywinfo.getYwNo());
            List<JintieInputDetail> Details = jintieInputDetailService.selectByYwNo(ywinfo.getYwNo());
            ywinfo.setCustomNo("CopyOf_"+ywinfo.getYwNo());
            ywinfo.setStatus("待审核");
            ywinfo.setRemark("CopyOf_"+ywinfo.getYwNo());
            ywinfo.setYwNo(indexController.getywno());
            ywinfo.setRemark1(getCustomKey(ywinfo.getClearingCustomerName()));
            ywinfo.setCreateDate(createDate);
            ywinfo.setId(null);
            ywinfo.setCreateDateTime(LocalDateTime.now());
            ywinfo.setCreateUserEmp(session.getAttribute("user")+"");
            jintieYwinfoService.save(ywinfo);
            head.setMessagehead(commonBo.getMessageHead());
            head.setMessagedate(date1);
            head.setMessagetime(time1);
            head.setStatus(1);
            head.setAsngroup(ywinfo.getCustomNo());
            head.setStoreid(ywinfo.getRemark1());
            head.setAsncreationtime(CalendarUtils.getFormatDate(CalendarUtils.Y_M_DHMS_LONG,CalendarUtils.localDateTimeToUdate(LocalDateTime.now())));
            head.setWlYwno(ywinfo.getYwNo());
            head.setPid(null);
            jintieInputHeadService.save(head);
            for (int i = 0; i < Details.size(); i++) {
                JintieInputDetail detail = Details.get(i);
                detail.setMessagehead(head.getMessagehead());
                detail.setMessagedate(date1);
                detail.setMessagetime(time1);
                detail.setPid(null);
                detail.setStoreid(ywinfo.getRemark1());
                detail.setWlYwno(head.getWlYwno());
                detail.setStatus(1);
                jintieInputDetailService.save(detail);
            }
//            for (JintieInputDetail detail:Details) {
//
//            }
            msg.setStatus("200");
            msg.setMessage("copyNice");
            return msg;
        }catch (Exception e){
            msg.setStatus("400");
            msg.setMessage("失败，错误信息："+e.toString());
            return msg;
        }
    }
}
