package com.kwe.kweplus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kwe.kweplus.dao.JintieModelListMapper;
import com.kwe.kweplus.dao.JintieOcrTemplateMapper;
import com.kwe.kweplus.dao.JintieYwinfoMapper;
import com.kwe.kweplus.modal.JintieModelList;
import com.kwe.kweplus.modal.JintieOcrTemplate;
import com.kwe.kweplus.modal.JintieYwinfo;
import com.kwe.kweplus.modal.ReturnMessage;
import com.kwe.kweplus.service.IJintieYwinfoService;
import com.kwe.kweplus.util.ExcelUtil;
import com.kwe.kweplus.util.MapBwanUtil;
import com.kwe.kweplus.util.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jobob
 * @since 2019-03-22
 */
@Service
public class JintieYwinfoServiceImpl extends ServiceImpl<JintieYwinfoMapper, JintieYwinfo> implements IJintieYwinfoService {

    @Autowired
    JintieYwinfoMapper JintieYwinfoMapper;
    @Autowired
    JintieModelListMapper jintieModelListMapper;
    @Autowired
    JintieOcrTemplateMapper jintieOcrTemplateMapper;
    @Override
    public JintieYwinfo selectByYwNo(String ywNo) {
        List<JintieYwinfo> jintieYwinfos = JintieYwinfoMapper.selectByYwNo(ywNo);
//        QueryWrapper<JintieYwinfo> queryWrapper = new QueryWrapper<>();
//        queryWrapper.lambda().eq(JintieYwinfo::getYwNo,ywNo);
//        List<JintieYwinfo> jintieYwinfos = JintieYwinfoMapper.selectList(queryWrapper);
        if (MyUtils.isNotEmpty(jintieYwinfos)) {
            return jintieYwinfos.get(0);
        }
        return null;
    }

    @Override
    public int selectByCustomNo(String customNo) {
        return JintieYwinfoMapper.selectByCustomNo(customNo);
    }

    @Override
    public ReturnMessage reverseExcel(List<JintieYwinfo> queryList) {
        ReturnMessage msg = new ReturnMessage();
        msg.setStatus("200");
        msg.setMessage("导出excel表格成功~");
        //得到List集合。存表体数据
        List<Map<String,Object>> listCountent=new ArrayList<>();
        for (int i = 0; i < queryList.size(); i++) {
            //remark2对应的是模板的id
            String remark2 = queryList.get(i).getRemark2();
            if (org.apache.commons.lang3.StringUtils.isNoneBlank(remark2)) {
                //查询模板对照表，得到模板信息表对象。
                JintieModelList JintieModelList = jintieModelListMapper.selectByModel_id(remark2);
                if (JintieModelList != null) {
                    //jintieyewuinfo近铁信息表添加字段存模板中文名。把中文字段信息填入。
                    queryList.get(i).setModelName(JintieModelList.getRemarks1());
                }
            }
            String remark3 = queryList.get(i).getRemark3();
            if (org.apache.commons.lang3.StringUtils.isNoneBlank(remark3)) {
                List<JintieOcrTemplate> JintieOcrTemplate = jintieOcrTemplateMapper.selectByTemplateId(remark3);
                if (JintieOcrTemplate != null && JintieOcrTemplate.size() > 0) {
                    String name = "";
                    for (int j = 0; j < JintieOcrTemplate.size(); j++) {
                        name += JintieOcrTemplate.get(j).getName();
                    }
                    queryList.get(i).setTemplateName(name);
                }
            }
            Map<String,Object> mapContent =MapBwanUtil.object2Map(queryList.get(i));
            if("0".equals(mapContent.get("type")+""))
            {
                mapContent.put("type","进仓");
            }
            if("1".equals(mapContent.get("type")+""))
            {
                mapContent.put("type","出仓");
            }
            listCountent.add(mapContent);
        }
        if(queryList!=null&&queryList.size()>0) {
            //创建map存入要放出的字段名和对应的属性。
            List<String> totleValueShow = new ArrayList<>();
            List<String> totleKeyShow = new ArrayList<>();
            totleKeyShow.add("type".toLowerCase());
            totleValueShow.add("类型");
            totleKeyShow.add("customNo".toLowerCase());
            totleValueShow.add("客户业务编号");
            totleKeyShow.add("clearingCustomerName".toLowerCase());
            totleValueShow.add("客户名称");
            totleKeyShow.add("ywNo".toLowerCase());
            totleValueShow.add("唯一编号");
            totleKeyShow.add("status".toLowerCase());
            totleValueShow.add("状态");
            totleKeyShow.add("createUser".toLowerCase());
            totleValueShow.add("创建人");
            totleKeyShow.add("createDate".toLowerCase());
            totleValueShow.add("创建时间");
            totleKeyShow.add("updateUser".toLowerCase());
            totleValueShow.add("更新人");
            totleKeyShow.add("updateDate".toLowerCase());
            totleValueShow.add("更新时间");
            totleKeyShow.add("auditUser".toLowerCase());
            totleValueShow.add("审核人");
            totleKeyShow.add("remark".toLowerCase());
            totleValueShow.add("备注");
            totleKeyShow.add("remark4".toLowerCase());
            totleValueShow.add("识别引擎");
            totleKeyShow.add("templateName".toLowerCase());
            totleValueShow.add("识别模板");
            totleKeyShow.add("modelName".toLowerCase());
            totleValueShow.add("客户模板");
            totleKeyShow.add("remark5".toLowerCase());
            totleValueShow.add("识别率");
            totleKeyShow.add("remark6".toLowerCase());
            totleValueShow.add("正确率");
            try {
                ExcelUtil.exportToExcel3(listCountent, totleValueShow, totleKeyShow);
            } catch (Exception e) {
                msg.setMessage("导出excel表格失败：" + e.getMessage());
                msg.setStatus(ReturnMessage.STATUS_ERROR);
            }
        }
        return msg;
    }
}
