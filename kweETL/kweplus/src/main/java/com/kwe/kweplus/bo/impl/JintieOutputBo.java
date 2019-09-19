package com.kwe.kweplus.bo.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kwe.kweplus.bo.IJintieCommonBo;
import com.kwe.kweplus.bo.IJintieOutputBo;
import com.kwe.kweplus.common.UserList;
import com.kwe.kweplus.controller.indexController;
import com.kwe.kweplus.dao.JintieCustomMapper;
import com.kwe.kweplus.modal.*;
import com.kwe.kweplus.service.*;
import com.kwe.kweplus.service.impl.KweDictServiceImpl;
import com.kwe.kweplus.util.CalendarUtils;
import com.kwe.kweplus.util.MyUtils;
import com.kwe.kweplus.util.SetGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class JintieOutputBo implements IJintieOutputBo {
    @Autowired
    private IJintieYwinfoService jintieYwinfoService;
    @Autowired
    private IJintieOutputHeadService jintieOutputHeadService;
    @Autowired
    private IJintieOutputDetailService jintieOutputDetailService;
    @Autowired
    private IJintieOutputSerialService jintieOutputSerialService;
    @Autowired
    private IJintieCommonBo commonBo;
    @Autowired
    IJintieCustomService IJintieCustomService;
    @Autowired
    private JintieCustomMapper customMapper;
    @Autowired
    JintieDataComparisonService JintieDataComparisonService;
    @Autowired
    KweDictService kweDictService;


    private Logger logger = LoggerFactory.getLogger(JintieOutputBo.class);
    ReturnMessage msg = new ReturnMessage();
    UserList UserList = new UserList();
    /**
     * 获取head信息
     * @param wlywno
     * @return
     */
    private JintieOutputHead getHeadByYwNo(String wlywno){
        QueryWrapper<JintieOutputHead> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(JintieOutputHead::getWlYwno, wlywno);
        JintieOutputHead head = jintieOutputHeadService.getOne(queryWrapper);
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
    public Map getDetailByNo(String wlywno, String type) throws Exception {
        KweDictServiceImpl kweDictServiceImpl = new KweDictServiceImpl();
        JintieOutputHead head = getHeadByYwNo(wlywno);
        Map<String, Object> resultMap = new HashMap<>();
        if(head==null){
            resultMap.put("head", new ArrayList<>());
            resultMap.put("detail", new ArrayList<>());
            resultMap.put("serial", new ArrayList<>());
            return resultMap;
        }

        QueryWrapper<JintieOutputDetail> detailQueryWrapper = new QueryWrapper<>();
        detailQueryWrapper.lambda().eq(JintieOutputDetail::getMessagehead, head.getMessagehead());
        List<JintieOutputDetail> detailList = jintieOutputDetailService.list(detailQueryWrapper);
        detailList = detailList==null ? new ArrayList<>():detailList;

        QueryWrapper<JintieOutputSerial> serialQueryWrapper = new QueryWrapper<>();
        serialQueryWrapper.lambda().eq(JintieOutputSerial::getWlYwno, wlywno);
        List<JintieOutputSerial> serialList = jintieOutputSerialService.list(serialQueryWrapper);

        List<JintieOutputHead> headList = new ArrayList<JintieOutputHead>();
        headList.add(head);
        List<JintieOutputDetail> jintieOutputDetails = analyzeDetailList(detailList,type);
        List<KweDict> kweDicts = kweDictService.getKweDictList(jintieYwinfoService.selectByYwNo(wlywno).getRemark1());
        for (int i = 0; i < jintieOutputDetails.size(); i++) {
            Map map = new HashMap();
            JintieOutputDetail jintieOutputDetail = jintieOutputDetails.get(i);
            String sku = jintieOutputDetail.getSku();
            String hscode = jintieOutputDetail.getHscode();
            String chinesename = jintieOutputDetail.getChinesename();
            String declarationunit = jintieOutputDetail.getDeclarationunit();
            String currency = jintieOutputDetail.getCurrency();
            String countryoforigin = jintieOutputDetail.getCountryoforigin();
            String netweight = jintieOutputDetail.getNetweight()+"";
            String price = jintieOutputDetail.getPrice()+"";
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
                    if(!com.kwe.kweplus.util.text.StringUtils.isEmpty(sku) && !com.kwe.kweplus.util.text.StringUtils.isEmpty(dictSku)){
                        if(sku.equals(dictSku)){
                            map.put("status",kweDict.getStatus());
                            map.put("sku",true);
                            if(!com.kwe.kweplus.util.text.StringUtils.isEmpty(hscode) && hscode.equals(kweDict.getHscode())){
                                map.put("hscode",true);
                            }
                            if(!com.kwe.kweplus.util.text.StringUtils.isEmpty(chinesename) && hscode.equals(kweDict.getChinesename())){
                                map.put("chinesename",true);
                            }
                            if(!com.kwe.kweplus.util.text.StringUtils.isEmpty(declarationunit) && hscode.equals(kweDict.getUnitEn())){
                                map.put("declarationunit",true);
                            }
                            if(!com.kwe.kweplus.util.text.StringUtils.isEmpty(currency) ){
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
                            if(!com.kwe.kweplus.util.text.StringUtils.isEmpty(countryoforigin) ){
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
                            if(!com.kwe.kweplus.util.text.StringUtils.isEmpty(netweight) && hscode.equals(kweDict.getNetweight())){
                                map.put("netweight",true);
                            }
                            if(!com.kwe.kweplus.util.text.StringUtils.isEmpty(price) && hscode.equals(kweDict.getPrice())){
                                map.put("price",true);
                            }
                        }
                    }
                }
                jintieOutputDetail.setRemark(map);
            }catch (Exception e){
                jintieOutputDetail.setRemark(map);
            }
        }


        resultMap.put("head", headList);
        resultMap.put("detail",jintieOutputDetails );
        resultMap.put("serial", serialList);
        return resultMap;
    }

    /**
     * 分析表体与Batch的关系.
     * @param list
     * @return
     */
    private List<JintieOutputDetail> analyzeDetailList( List<JintieOutputDetail> list,String type) throws Exception {
        List<JintieOutputDetail> list2 = new ArrayList<>();
        Map<String,List<JintieOutputDetail>> map = new HashMap<>();
//        int count = 0;
        for(JintieOutputDetail jod:list){
//            if(StringUtils.isEmpty(jod.getLottable01())){
//                list2.add(jod);
//                continue;
//            }
//            if(jod.getDoclineno() == null){
//                System.out.println(count);
//                jod.setDoclineno(count+"");
//            }
            String key= jod.getCopyLineNo();
            List<JintieOutputDetail> l = map.get(key);
            if(l==null){
                l=new ArrayList<>();
                map.put(key,l);
            }
            l.add(jod);
        }
        if(map.isEmpty())
            return list2;
        for(String key:map.keySet()){
            List<JintieOutputDetail> listx = map.get(key);
            List<Map> maps = new ArrayList<>();
            //获得模板ID
            String modelId = jintieYwinfoService.selectByYwNo(listx.get(0).getWlYwno()).getRemark2();
            List<JintieCustomField> fieids = jintieOutputDetailService.selectByStoreId(listx.get(0).getStoreid(),type,modelId);
            List<String> fieid = new ArrayList();
            for (JintieCustomField j:fieids) {
                fieid.add(""+j.getJintieField().toLowerCase());
            }
            for(JintieOutputDetail o:listx){
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
            JintieOutputDetail j = listx.get(0);
            j.setRemark1(maps);
            list2.add(j);
        }
        Comparator<JintieOutputDetail> comparator = new Comparator<JintieOutputDetail>() {

            @Override
            public int compare(JintieOutputDetail o1, JintieOutputDetail o2) {
                if(Integer.parseInt(o1.getCopyLineNo()) > Integer.parseInt(o2.getCopyLineNo())){
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
     * 新增操作
     * @param data
     * @return
     * @throws IllegalAccessException
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = IllegalAccessException.class)
    public String insert(jintie_list data) throws IllegalAccessException{
        logger.info("Insert,业务编号："+data.getOutputHeadList().get(0).getWlYwno()+",操作人："+data.getOp_user());
        try {
            String date1 = "";
            String time1 = "";
            JintieOutputHead head = data.getOutputHeadList().get(0);
            QueryWrapper<JintieYwinfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(JintieYwinfo::getYwNo, head.getWlYwno());
            JintieYwinfo info = jintieYwinfoService.getOne(queryWrapper);
            info.setStatus("待审核");
            info.setUpdateUser(data.getOp_user());
            info.setUpdateUserEmp(UserList.loginMap().get(data.getOp_user()).split(",")[1]);
            info.setUpdateDate(CalendarUtils.getLocalDateTime(new Date()));
            jintieYwinfoService.saveOrUpdate(info);
            SimpleDateFormat sfm = new SimpleDateFormat("yyyy-MM-dd");
            String Date = sfm.format(new Date());
            SimpleDateFormat fm = new SimpleDateFormat("HHmmss");
            String time = sfm.format(new Date());
            head.setMessagedate(Date);
            head.setMessagetime(time);
            head.setMessagetype("H");
            //  head.setUpdateflag("I");
            head.setMessagehead(commonBo.getMessageHead());
            head.setDoctype("01");
            head.setStoreid(info.getRemark1());
            head.setStatus(1);
            head.setOrdertime(LocalDateTime.now());
            jintieOutputHeadService.saveOrUpdate(head);
            int a =0;
            for(int i = 0 ; i < data.getOutputDetailList().size(); i++){
                JintieOutputDetail detail = data.getOutputDetailList().get(i);
                detail.setWlYwno(head.getWlYwno());
                detail.setStoreid(info.getRemark1());
                detail.setMessagetype("D");
                detail.setStoreid(head.getStoreid());
                detail.setMessagehead(head.getMessagehead());
                detail.setMessagetime(CalendarUtils.getFormatDate(CalendarUtils.Y_M_DHMS_LONG,new Date()));
                //  detail.setUpdateflag("I");
                detail.setStatus(1);
                detail.setMessagedate(date1);
                detail.setCopyLineNo(i+"");

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
//
//
//                }

                if(detail.getRemark1()!=null&&!detail.getRemark1().isEmpty()){
                    List<JintieOutputDetail> list = new ArrayList<>();
                    for(Map map:detail.getRemark1()){
                        a++;
                        JintieOutputDetail obj = new JintieOutputDetail();
                        obj.setCopyLineNo(i+"");
                        BeanUtils.copyProperties(detail,obj);
                        Iterator<String> it = map.keySet().iterator();
                        while (it.hasNext()){
                            String key = it.next();
                            String value = map.get(key)+"";
                            if(!value.isEmpty()){
                                if(key == "volumn" ){
                                    obj.setVolumn(new BigDecimal(value));
                                    continue;
                                }
                                if(key == "qtyordered" ){
                                    obj.setQtyordered(Integer.parseInt(value));
                                    continue;
                                }
                                if(key == "lottable01" ){
                                    obj.setLottable01(value);
                                    continue;
                                }
                                if(key == "lottable02" ){
                                    obj.setLottable02(value);
                                    continue;
                                }
                                if(key == "lottable03" ){
                                    obj.setLottable03(value);
                                    continue;
                                }
                                if(key == "lottable04" ){
                                    obj.setLottable04(value);
                                    continue;
                                }
                                if(key == "lottable05" ){
                                    obj.setLottable05(value);
                                }
                                if(key == "lottable06" ){
                                    obj.setLottable06(value);
                                    continue;
                                }
                                if(key == "lottable07" ){
                                    obj.setLottable07(value);
                                }
                                if(key == "lottable08" ){
                                    obj.setLottable08(value);
                                    continue;
                                }
                                if(key == "lottable09" ){
                                    obj.setLottable09(value);
                                    continue;
                                }
                                if(key == "lottable10" ){
                                    obj.setLottable10(value);
                                    continue;
                                }
                                if(key == "lottable11" ){
                                    obj.setLottable11(value);
                                    continue;
                                }
                                if(key == "lottable12" ){
                                    obj.setLottable12(value);
                                    continue;
                                }


                            }else {
                            }
                        }
                        //packOutBatchInfo(obj,map.get("lottable01").toString(),Integer.valueOf(map.get("qtyordered").toString()));
                        /**
                         * 三垦特殊需求
                         * 序列号1、2、3、4...
                         */
//                        if("C000022".equals(obj.getStoreid())){
//                            obj.setLottable02(a+"");
//                        }
                        /**
                         * 7.3更新
                         * 不存在分金额,batch号字段时 将料号总价 料号数量coopy分金额等字段
                         */
                        if(obj.getQtyordered() == null || "".equals(obj.getQtyordered())
                                && obj.getVolumn() == null || "".equals(obj.getVolumn())){
                            obj.setQtyordered(obj.getOpenqty());
                            obj.setVolumn(obj.getTotalprice());
                        }

                        list.add(obj);

                    }
                    jintieOutputDetailService.saveBatch(list);
                }else{
                    /**
                     * 7.3更新
                     * 不存在分金额,batch号字段时 将料号总价 料号数量coopy分金额等字段
                     */
                    detail.setQtyordered(detail.getOpenqty());
                    detail.setVolumn(detail.getTotalprice());
                    jintieOutputDetailService.saveOrUpdate(detail);
                }
            }
            if (MyUtils.isNotEmpty(data.getOutputSerialList())) {
                for (JintieOutputSerial serial : data.getOutputSerialList()) {
                    serial.setMessagetype("S");
                    serial.setMessagetime(CalendarUtils.getFormatDate(CalendarUtils.Y_M_DHMS_LONG,new Date()));
                    // serial.setUpdateflag("I");
                    serial.setStatus(1);
                    serial.setMessagedate(date1);
                    serial.setWlYwno(head.getWlYwno());
                    jintieOutputSerialService.saveOrUpdate(serial);
                }
            }

        } catch (Exception ex) {
            logger.error("出仓数据新增出错!",ex);
            throw new IllegalAccessException(ex.getMessage());
        }
        return "saveNice";
    }

    /**
     *
     * 封装批次信息
     * @param detail
     * @param batchNo
     * @param batchQty
     */
    private void packOutBatchInfo(JintieOutputDetail detail, String batchNo, Integer batchQty){
        detail.setQtyordered(batchQty);
        detail.setLottable01(batchNo);
    }

    /**
     * 更新操作
     * @param data
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = IllegalAccessException.class)
    public String update(jintie_list data) throws IllegalAccessException{
        logger.info("update:修改编号"+data.getOutputHeadList().get(0).getWlYwno()+",操作人："+data.getOp_user());
        try {
            JintieOutputHead chead =data.getOutputHeadList().get(0) ;
//            Boolean chanageModify = false;
//            if(chead.getStatus()==6){
//                chead.setStatus(5);
//                chead.setUpdateflag("U");
//                chanageModify = true;
//            }
            SimpleDateFormat sfm = new SimpleDateFormat("yyyy-MM-dd");
            String Date = sfm.format(new Date());
            SimpleDateFormat fm = new SimpleDateFormat("HHmmss");
            String time = fm.format(new Date());
            chead.setMessagedate(Date);
            chead.setMessagetime(time);
            jintieOutputHeadService.saveOrUpdate(chead);

            QueryWrapper<JintieOutputDetail> queryWrapper2 = new QueryWrapper<JintieOutputDetail>();
            queryWrapper2.lambda().eq(JintieOutputDetail::getMessagehead, chead.getMessagehead());
            List<JintieOutputDetail> oldList = jintieOutputDetailService.list(queryWrapper2);
            Boolean hasBatch=false;
            for(JintieOutputDetail jd: data.getOutputDetailList()){
                if(MyUtils.isNotEmpty(jd.getRemark1())){
                    hasBatch=true;
                    break;
                }
            }

            for(JintieOutputDetail jd: oldList){
                if(MyUtils.isNotEmpty(jd.getRemark1())){
                    hasBatch=true;
                    break;
                }
            }
            int a = 0;
            if(hasBatch){//目前只发现GD有Batch情况
                jintieOutputDetailService.remove(queryWrapper2);
                for (int i = 0; i < data.getOutputDetailList().size(); i++) {
                    JintieOutputDetail detail = data.getOutputDetailList().get(i);
                    detail.setPid(null);
                    detail.setCopyLineNo(i+"");
                    detail.setMessagetype("D");
                    detail.setMessagedate(chead.getMessagedate());
                    detail.setMessagetime(chead.getMessagetime());
                    detail.setStoreid(chead.getStoreid());
                    detail.setStatus(chead.getStatus());
                    detail.setWlYwno(chead.getWlYwno());


                    if(detail.getRemark1()!=null&&!detail.getRemark1().isEmpty()){
                        List<JintieOutputDetail> list = new ArrayList<>();
                        for(Map map:detail.getRemark1()){
                            a++;
                            JintieOutputDetail obj = new JintieOutputDetail();
                            BeanUtils.copyProperties(detail,obj);
                            obj.setPid(null);
                            obj.setCopyLineNo(i+"");
                            obj.setWlYwno(chead.getWlYwno());
                            obj.setStatus(chead.getStatus());
                            //packOutBatchInfo(obj,map.get("lottable01").toString(),Integer.valueOf(map.get("qtyordered").toString()));
//                            obj.setLottable01(map.get("lottable01").toString());
//                            obj.setLottable02(map.get("lottable02").toString());
//                            obj.setLottable03(map.get("lottable03").toString());
//                            obj.setLottable04(map.get("lottable04").toString());
//                            obj.setLottable05(map.get("lottable05").toString());
//                            obj.setLottable06(map.get("lottable06").toString());
//                            obj.setLottable07(map.get("lottable07").toString());
//                            obj.setLottable08(map.get("lottable08").toString());
//                            obj.setLottable09(map.get("lottable09").toString());
//                            obj.setLottable10(map.get("lottable10").toString());
//                            obj.setLottable11(map.get("lottable11").toString());
//                            obj.setLottable12(map.get("lottable12").toString());
                            if(map.get("volumn") != null && map.get("volumn") != ""){
                                obj.setVolumn(new BigDecimal(map.get("volumn").toString()));
                            }
                            if(map.get("qtyordered") != null && map.get("qtyordered") != ""){
                                obj.setQtyordered(Integer.parseInt(map.get("qtyordered").toString()));
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
                             */

//                            if("C000022".equals(obj.getStoreid())){
//                                obj.setLottable02(a+"");
//                            }
                            /**
                             * 7.3更新
                             * 不存在分金额,batch号字段时 将料号总价 料号数量coopy分金额等字段
                             */
                            if(obj.getQtyordered() == null || "".equals(obj.getQtyordered())
                                    && obj.getVolumn() == null || "".equals(obj.getVolumn())){
                                obj.setQtyordered(obj.getOpenqty());
                                obj.setVolumn(obj.getTotalprice());
                            }
                            list.add(obj);
                        }
                        jintieOutputDetailService.saveOrUpdateBatch(list);
                    }else{
                        /**
                         * 7.3更新
                         * 不存在分金额,batch号字段时 将料号总价 料号数量coopy分金额等字段
                         */
                        detail.setQtyordered(detail.getOpenqty());
                        detail.setVolumn(detail.getTotalprice());
                        jintieOutputDetailService.saveOrUpdate(detail);
                    }
                }




            }else{
//                List<Long> delIds = new ArrayList<>();
//                for(JintieOutputDetail jd:oldList){
//                    Boolean isMatched = false;
//                    for(JintieOutputDetail detail : data.getOutputDetailList()){
//                        if(detail.getPid().equals(jd.getPid())){
//                            isMatched=true;
//                            break;
//                        }
//                    }
//                    if(!isMatched)
//                        delIds.add(jd.getPid());
//                }
//                if(MyUtils.isNotEmpty(delIds))
//                    jintieOutputDetailService.removeByIds(delIds);

                //删除原始数据
                jintieOutputDetailService.remove(queryWrapper2);
                for (int i = 0; i < data.getOutputDetailList().size(); i++) {
                    JintieOutputDetail detail = data.getOutputDetailList().get(i);
                    detail.setWlYwno(chead.getWlYwno());
                    detail.setStatus(chead.getStatus());
                    detail.setCopyLineNo(i+"");
                    detail.setPid(null);
                    detail.setMessagetype("D");
                    detail.setMessagedate(chead.getMessagedate());
                    detail.setMessagetime(chead.getMessagetime());
                    detail.setStoreid(chead.getStoreid());
                    /**
                     * 尼吉康(没有对多)
                     * “料号” 前加“1P”   末尾去“-W”
                     * “订单号“ 前加“ 1T”
                     * “PO” 前加“K”
                     * “CPN” 前加“P”
                     * “入库发票号” 如6位数字，在第二位后加“-”（例 93-0836）(针对出)
                     */
                    //功能关闭
//                    if("C000016".equals(detail.getStoreid())){
//                        if(detail.getVolumnuom() != null && !"".equals(detail.getVolumnuom()) ){
//                            //判断是否已经有"1P"
//                            if(!detail.getVolumnuom().contains("-")){
//                                if(detail.getVolumnuom().length()==6){
//                                    detail.setVolumnuom(detail.getVolumnuom().substring(0,2)+"-"+detail.getVolumnuom().substring(2));
//                                }
//                            }
//                        }
//                        if(detail.getLottable02() != null && !"".equals(detail.getLottable02()) ){
//                            //“CPN” 前加“P”
//                            if(!"P".equals(detail.getLottable02().substring(0,1))){
//                                detail.setLottable02("P"+detail.getLottable02());
//                            }
//                        }
//
//
//                    }
                    /**
                     * 7.3更新
                     * 不存在分金额,batch号字段时 将料号总价 料号数量coopy分金额等字段
                     */
                    detail.setQtyordered(detail.getOpenqty());
                    detail.setVolumn(detail.getTotalprice());
                    jintieOutputDetailService.saveOrUpdate(detail);
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

            QueryWrapper<JintieYwinfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(JintieYwinfo::getYwNo, chead.getWlYwno());
            JintieYwinfo info = jintieYwinfoService.getOne(queryWrapper);
            info.setUpdateUser(data.getOp_user());
            info.setUpdateUserEmp(UserList.loginMap().get(data.getOp_user()).split(",")[1]);
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
     * 根据业务编号删除表头,表体
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
            JintieOutputHead head =  getHeadByYwNo(ywno);
            if(head==null)
                return;
            head.setStatus(7);//删除状态
            jintieOutputHeadService.saveOrUpdate(head);
            //表体删除
            QueryWrapper<JintieOutputDetail> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.lambda().eq(JintieOutputDetail::getMessagehead, head.getMessagehead());
            List<JintieOutputDetail> list = jintieOutputDetailService.list(queryWrapper2);
            if(MyUtils.isNotEmpty(list)){
                for(JintieOutputDetail jid:list){
                    jid.setStatus(head.getStatus());
                }
                jintieOutputDetailService.saveOrUpdateBatch(list);
            }
        }catch (Exception e){
            logger.error("出仓表头表体数据删除出错!",e);
            throw new IllegalAccessException(e.getMessage());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = IllegalAccessException.class)
    public void audit(String wlywno, JintieYwinfo ywinfo) throws IllegalAccessException{
        try{
            jintieYwinfoService.saveOrUpdate(ywinfo);
            JintieOutputHead head =  getHeadByYwNo(wlywno);
            if(ywinfo.getStatus().equals("待审核")){
                head.setStatus(1);
                head.setUpdateflag(null);
            }else if(ywinfo.getStatus().equals("待修改")){
                head.setStatus(6);
            }else if(ywinfo.getStatus().equals("已审核")){//己审核
                head.setStatus(2);
                head.setUpdateflag("I");
            }else{//己修改
                head.setStatus(5);
                head.setUpdateflag("U");
            }
            jintieOutputHeadService.saveOrUpdate(head);
            QueryWrapper<JintieOutputDetail> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.lambda().eq(JintieOutputDetail::getMessagehead, head.getMessagehead());
            List<JintieOutputDetail> list2 = jintieOutputDetailService.list(queryWrapper2);
            if (MyUtils.isNotEmpty(list2)) {
                for (JintieOutputDetail detail : list2) {
                    detail.setStatus(head.getStatus());
                    detail.setUpdateflag(head.getUpdateflag());
                    jintieOutputDetailService.saveOrUpdate(detail);
                }
            }
            QueryWrapper<JintieOutputSerial> queryWrapper3 = new QueryWrapper<>();
            queryWrapper3.lambda().eq(JintieOutputSerial::getMessagehead, head.getMessagehead());
            List<JintieOutputSerial> list3 = jintieOutputSerialService.list(queryWrapper3);
            if (MyUtils.isNotEmpty(list3)) {
                for (JintieOutputSerial serial : list3) {
                    serial.setStatus(head.getStatus());
                    serial.setUpdateflag(head.getUpdateflag());
                    jintieOutputSerialService.saveOrUpdate(serial);
                }
            }
        }catch (Exception e){
            logger.error("出仓审核数据出错!",e);
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
        JintieOutputHead head = getHeadByYwNo(ywinfo.getYwNo());
        if(head==null||head.getSoreference1().equals(ywinfo.getCustomNo()))
            return;
        head.setSoreference1(ywinfo.getCustomNo());
        jintieOutputHeadService.saveOrUpdate(head);
    }

    @Override
    public void test(){
        Map map =new HashMap();
        map.put("id",2);
        System.out.println("-------Testing result:-------");
        System.out.println(customMapper.selectDemo(map));
    }

    @Override
    public void saveOutputUpdate(JintieYwinfo ywinfo) {
        jintieOutputHeadService.saveOutputUpdate(ywinfo.getCustomNo(),ywinfo.getYwNo());
    }
    public String getCustomKey(String customName){
        JintieCustom JintieCustom = IJintieCustomService.getCustomId(customName);
        return JintieCustom.getKey();
    }
    @Override
    public ReturnMessage saveAs(JintieYwinfo ywinfo) {
        try {
            indexController indexController = new indexController();
            String date1 =  CalendarUtils.getFormatDate(CalendarUtils.Y_M_D_LONG,CalendarUtils.localDateTimeToUdate(ywinfo.getCreateDateTime()));
            String time1 = CalendarUtils.getFormatDate(CalendarUtils.TIME_FORMAT,CalendarUtils.localDateTimeToUdate(ywinfo.getCreateDateTime()));
            SimpleDateFormat sfm = new SimpleDateFormat("yyyy-MM-dd");
            String createDate = sfm.format(new Date());
            JintieOutputHead head = jintieOutputHeadService.selectByYwNo(ywinfo.getYwNo());
            List<JintieOutputDetail> Details = jintieOutputDetailService.selectByYwNo(ywinfo.getYwNo());
            ywinfo.setCustomNo("CopyOf_"+ywinfo.getYwNo());
            ywinfo.setStatus("待审核");
            ywinfo.setRemark("CopyOf_"+ywinfo.getYwNo());
            ywinfo.setYwNo(indexController.getywno());
            ywinfo.setRemark1(getCustomKey(ywinfo.getClearingCustomerName()));
            ywinfo.setCreateDate(createDate);
            ywinfo.setId(null);
            ywinfo.setCreateDateTime(LocalDateTime.now());
            ywinfo.setCreateUserEmp(UserList.loginMap().get(ywinfo.getCreateUser()).split(",")[1]);
            jintieYwinfoService.save(ywinfo);
            head.setMessagehead(commonBo.getMessageHead());
            head.setMessagedate(date1);
            head.setMessagetime(time1);
            head.setStatus(1);
            head.setStoreid(ywinfo.getRemark1());
            head.setWlYwno(ywinfo.getYwNo());
            head.setSoreference1(ywinfo.getCustomNo());
            head.setPid(null);
            jintieOutputHeadService.save(head);
            for (JintieOutputDetail detail:Details) {
                detail.setMessagehead(head.getMessagehead());
                detail.setMessagedate(date1);
                detail.setMessagetime(time1);
                detail.setPid(null);
                detail.setStoreid(ywinfo.getRemark1());
                detail.setWlYwno(head.getWlYwno());
                detail.setStatus(1);
                jintieOutputDetailService.save(detail);
            }
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
