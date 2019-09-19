package com.kwe.kweplus.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kwe.kweplus.dao.KweDictFlagMapper;
import com.kwe.kweplus.dao.KweDictMapper;
import com.kwe.kweplus.modal.*;
import com.kwe.kweplus.service.KweDictService;
import com.kwe.kweplus.util.CalendarUtils;
import com.kwe.kweplus.util.ListUtil;
import com.kwe.kweplus.util.ocrUtil.OcrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.net.Proxy;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Ly
 * @Desc
 * @date 2019/8/29  14:52
 */
@Service
@Transactional
public class KweDictServiceImpl implements KweDictService {

    @Autowired
    KweDictMapper kweDictMapper;
    @Autowired
    KweDictFlagMapper kweDictFlagMapper;

    Date date = new Date();
    SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd :HH:mm:ss");
    @Override
    public List<KweDict> getKweDictList(String customKey) {
        Map map = new HashMap<>(16);
        map.put("custom_key",customKey);
        List<KweDict> list = kweDictMapper.selectByMap(map);
        return list;
    }

    @Override
    public List<Map> getKweDictByCustom() {
        return kweDictMapper.getKweDictByCustom();
    }

    @Override
    public ReturnMessage addSku(KweDict dict) {
        ReturnMessage msg = new ReturnMessage();
        dict.setCreateTime(dateFormat.format(date));
        dict.setStatus("1");
        dict.setCoo(dict.getCoo().toString().replace(" ",""));
        dict.setCurrency(dict.getCurrency().toString().replace(" ",""));
        if((kweDictMapper.insert(dict)) > 0 ){
            msg.setMessage("添加成功");
            msg.setStatus(ReturnMessage.STATUS_OK);
        }else {
            msg.setMessage("添加失败");
            msg.setStatus(ReturnMessage.STATUS_ERROR);
        }
        return msg;
    }

    @Override
    public ReturnMessage deleteSku(List<Integer> skuNum,String user) {
        KweDictFlag flag = new KweDictFlag();
        ReturnMessage msg = new ReturnMessage();
        try{
            for (Integer str:skuNum) {
                flag.setRemark1("删除操作");
                flag.setUpdateUser(user);
                flag.setUpdateTime(dateFormat.format(date));
                kweDictFlagMapper.insertFlag(flag);
                kweDictMapper.deleteOrUpdateById(str);
                kweDictMapper.UpdateFlagById(flag.getId(),str);
            }
            msg.setStatus(ReturnMessage.STATUS_OK);
            msg.setMessage("删除成功，删除"+skuNum.size()+"条！");
        }catch (Exception e){
            e.printStackTrace();
            msg.setStatus(ReturnMessage.STATUS_ERROR);
            msg.setMessage("删除失败，错误信息"+e.getMessage());
        }
        return msg;
    }

    @Override
    public ReturnMessage updateSku(KweDict dict) {
        KweDictFlag flag = new KweDictFlag();
        ReturnMessage msg = new ReturnMessage();
        try {
            flag.setOldValue(kweDictMapper.selectById(dict.getId()).toString());
            flag.setNewValue(dict.toString());
            flag.setUpdateTime(dateFormat.format(date));
            flag.setUpdateUser(dict.getUpdateUser());
            kweDictFlagMapper.insertFlag(flag);
            dict.setFlagId(flag.getId());
            dict.setCoo(dict.getCoo().toString().replace(" ",""));
            dict.setCurrency(dict.getCurrency().toString().replace(" ",""));
            kweDictMapper.updateById(dict);
            msg.setMessage("修改成功");
            msg.setStatus(ReturnMessage.STATUS_OK);
        }catch (Exception e){
            e.printStackTrace();
            msg.setStatus(ReturnMessage.STATUS_ERROR);
            msg.setMessage("修改失败，错误信息"+e.getMessage());
        }
        return msg;
    }

    @Override
    public Map getSkuByParm(KweDict dict) {
        Map map = new HashMap(16);
        try{
            Page page = new Page();
            page.setSize(Integer.parseInt(dict.getRows()));
            page.setCurrent(Integer.parseInt(dict.getPage()));
            QueryWrapper<KweDict> queryWrapper = new QueryWrapper<>();
            queryWrapper.orderByDesc("id");
            queryWrapper.lambda().ne(KweDict::getStatus,"0");
            if (!StringUtils.isEmpty(dict.getSku())){
                queryWrapper.lambda().eq(KweDict::getSku, dict.getSku());
            }
            if (!StringUtils.isEmpty(dict.getHscode())){
                queryWrapper.lambda().eq(KweDict::getHscode, dict.getHscode());
            }
            if (!StringUtils.isEmpty(dict.getCreateUser())){
                queryWrapper.lambda().eq(KweDict::getCreateUser, dict.getCreateUser());
            }
            if (!StringUtils.isEmpty(dict.getCustomKey())){
                queryWrapper.lambda().eq(KweDict::getCustomKey, dict.getCustomKey());
            }
            if (!StringUtils.isEmpty(dict.getStatus())){
                queryWrapper.lambda().eq(KweDict::getStatus, dict.getStatus());
            }
            if (!StringUtils.isEmpty(dict.getCreateTime()) ) {
                LocalDateTime startDate = CalendarUtils.getLocalDateTime(CalendarUtils.getParseDate(CalendarUtils.Y_M_DHMS_LONG,dict.getCreateTime().substring(0,dict.getCreateTime().indexOf("||"))+" 00:00:00"));
                LocalDateTime endDate = CalendarUtils.getLocalDateTime(CalendarUtils.getParseDate(CalendarUtils.Y_M_DHMS_LONG,dict.getCreateTime().substring(dict.getCreateTime().indexOf("||")+2)+" 23:59:59"));
                queryWrapper.lambda().between(KweDict::getCreateTime, startDate, endDate);
            }
            IPage<KweDict> list = kweDictMapper.selectPage(page,queryWrapper);
            for (int i = 0; i < list.getRecords().size(); i++) {
                KweDict m = list.getRecords().get(i);
                if(!StringUtils.isEmpty(m.getFlagId())){
                    KweDictFlag f = kweDictFlagMapper.selectById(Integer.parseInt(m.getFlagId()+""));
                    m.setUpdateUser(f.getUpdateUser());
                    m.setUpdateTime(f.getUpdateTime());
                }
                m.setCoo(toArray(m.getCoo()));
                m.setCurrency(toArray(m.getCurrency()));
            }
            map.put("data",list.getRecords());
            map.put("total",list.getTotal());
            return map;
        }catch (Exception e){
            e.printStackTrace();
            map.put("msg","查询错误，请联系管理员解决！");
            return map;
        }
    }

    /**
     * 方法说明：
     *  先把传进来的Datail对象处理下 是否有多个料号相同，如果是就合并对多信息（币制，原产国）
     * @param data
     * @return
     */
    @Override
    public ReturnMessage insertByDetail(String data) {
        OcrUtil util = new OcrUtil();
        ReturnMessage msg = new ReturnMessage();
        Map map = (Map) JSON.parse(data);
        List list = (List) map.get("selectList");
        List<Map> lists = new ArrayList<>();
        try {
            List<KweDict> kweDicts = getKweDictList(map.get("customKey")+"");
            for (int i = 0; i < list.size(); i++) {
                JSONObject jsonObject = (JSONObject) list.get(i);
                Map Map = new HashMap();
                List currencyList = new ArrayList();
                List cooList = new ArrayList();
                String sku = jsonObject.get("sku")+"";
                if(!util.checkNull(sku)){
                    msg.setMessage("料号不能为空，请检验数据！");
                    msg.setStatus(ReturnMessage.STATUS_ERROR);
                    return msg;
                }
                String hscode = jsonObject.get("hscode")+"";
                String chinesename = jsonObject.get("chinesename")+"";
                String declarationunit = jsonObject.get("declarationunit")+"";
                String currency = jsonObject.get("currency")+"";
                String countryoforigin = jsonObject.get("countryoforigin")+"";
                if(util.checkNull(currency)){
                    currencyList.add(currency);
                }
                if(util.checkNull(countryoforigin)){
                    cooList.add(countryoforigin);
                }
                Map.put("sku",sku);
                Map.put("hscode",hscode);
                Map.put("chinesename",chinesename);
                Map.put("declarationunit",declarationunit);
                Map.put("currency",currencyList.toString());
                Map.put("countryoforigin",cooList.toString());
                lists.add(Map);
            }
            //去除重复
            lists = ListUtil.removeDuplicateWithOrder(lists);
            List<Map> li = new ArrayList();
            //合并
            for (int i = 0; i < lists.size(); i++) {
                if(i == 0){
                    li.add(lists.get(i));
                }else {
                    for (int j = 0; j < li.size(); j++) {
                        if(li.get(j).get("sku").equals(lists.get(i).get("sku"))){
                            List currencyList = new ArrayList(toArray(li.get(j).get("currency")));
                            List cooList = new ArrayList(toArray(li.get(j).get("countryoforigin")));
                            List oldCurrencyList = new ArrayList(toArray(toArray(lists.get(i).get("currency"))));
                            List oldCooList = new ArrayList(toArray(lists.get(i).get("countryoforigin")));
                            if(!"[]".equals(lists.get(i).get("currency"))){
                                currencyList.addAll(oldCurrencyList);
                            }
                            if(!"[]".equals(lists.get(i).get("countryoforigin"))){
                                cooList.addAll(oldCooList);
                            }
                            lists.get(i).put("currency",currencyList.toString());
                            lists.get(i).put("countryoforigin",cooList.toString());
                            li.add(lists.get(i));
                            li.remove(li.get(j));
                            break;
                        }else {
                            li.add(lists.get(i));
                            break;
                        }
                    }
                }
            }
            List list1 = new ArrayList();
            for (int i = 0; i <kweDicts.size() ; i++) {
                KweDict dict = kweDicts.get(i);
                Map Map = new HashMap();
                String sku = dict.getSku();
                String hscode = dict.getHscode();
                String chinesename =dict.getChinesename();
                String declarationunit = dict.getUnitEn();
                String currency = dict.getCurrency()+"";
                String countryoforigin =dict.getCoo()+"";
                Map.put("sku",sku);
                Map.put("hscode",hscode);
                Map.put("chinesename",chinesename);
                Map.put("declarationunit",declarationunit);
                Map.put("currency",currency);
                Map.put("countryoforigin",countryoforigin);
                list1.add(Map);
            }

            for (int i = 0; i < li.size(); i++) {
                boolean has = false;
                for (int j = 0; j < list1.size(); j++) {
                    if(list1.get(j).toString().equals(li.get(i).toString())){
                        has = true;
                        break;
                    }
                }
                if(!has){
                    KweDict dict = new KweDict();
                    if(util.checkNull(li.get(i).get("sku")) ){
                        dict.setSku(li.get(i).get("sku")+"");
                    }
                    if(util.checkNull(li.get(i).get("hscode"))){
                        dict.setHscode(li.get(i).get("hscode")+"");
                    }
                    if(util.checkNull(li.get(i).get("chinesename"))){
                        dict.setChinesename(li.get(i).get("chinesename")+"");
                    }
                    if(util.checkNull(li.get(i).get("declarationunit"))){
                        dict.setUnitEn(li.get(i).get("declarationunit")+"");
                    }
                    if(util.checkNull(li.get(i).get("currency"))){
                        dict.setCurrency(li.get(i).get("currency")+"");
                    }
                    if(util.checkNull(li.get(i).get("countryoforigin"))){
                        dict.setCoo(li.get(i).get("countryoforigin")+"");
                    }
                    dict.setCreateUser(map.get("user")+"");
                    dict.setCustomKey(map.get("customKey")+"");
                    dict.setCreateTime(dateFormat.format(date));
                    dict.setStatus("2");
                    kweDictMapper.insert(dict);

//                    KweDictFlag flag = new KweDictFlag();
//                    flag.setOldValue(data);
//                    flag.setUpdateTime(dateFormat.format(date));
//                    flag.setUpdateUser(map.get("user")+"");
//                    kweDictFlagMapper.insertFlag(flag);
                }
            }
            msg.setMessage("料号确认成功");
            msg.setStatus(ReturnMessage.STATUS_OK);
        }catch (Exception e){
            msg.setMessage("料号确认失败，失败信息："+e.getMessage());
            msg.setStatus(ReturnMessage.STATUS_ERROR);
            e.printStackTrace();
        }
        return msg;
    }

    public List toArray(Object obj){
        List<String> demoList = new ArrayList<>();
        if ( !StringUtils.isEmpty(obj)) {
            String demosub = obj.toString().substring(1,obj.toString().length()-1);
            String demoArray[] = demosub.split(",");
            demoList = Arrays.asList(demoArray);
        }
        return demoList;
    }

}
