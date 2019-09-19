package com.kwe.kweplus.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kwe.kweplus.bo.IJintieCommonBo;
import com.kwe.kweplus.bo.IJintieInputBo;
import com.kwe.kweplus.bo.IJintieOutputBo;
import com.kwe.kweplus.common.HttpServiceUtil;
import com.kwe.kweplus.common.UserList;
import com.kwe.kweplus.dao.JintieModelListMapper;
import com.kwe.kweplus.dao.JintieOcrTemplateMapper;
import com.kwe.kweplus.dao.UserMapper;
import com.kwe.kweplus.modal.*;
import com.kwe.kweplus.service.*;
import com.kwe.kweplus.util.CalendarUtils;
import com.kwe.kweplus.util.FileUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

import static com.kwe.kweplus.modal.ReturnMessage.STATUS_ERROR;
import static com.kwe.kweplus.modal.ReturnMessage.STATUS_OK;

@RestController
@RequestMapping("/kwe/index")
@Transactional
public class indexController {


    @Value("${urlConfig.excelSavePath}")
    public  String excelSavePath = "";

    @Autowired
    IJintieCommonBo iJintieCommonBo;
    @Autowired
    UserMapper userMapper;
    @Autowired
    IUserService  userService;

    @Autowired
    private IJintieYwinfoService jintieYwinfoService;

    @Autowired
    private IJintieCustomService jintieCustomService;

    @Autowired
    private IJintieDictService jintieDictService;

    @Autowired
    IJintieCustomService IJintieCustomService;
    @Autowired
    private IJintieInputBo inputBo;
    @Autowired
    IJintieInputHeadService IJintieInputHeadService;
    @Autowired
    IJintieInputDetailService IJintieInputDetailService;
    @Autowired
    IJintieOutputHeadService IJintieOutputHeadService;
    @Autowired
    IJintieOutputDetailService IJintieOutputDetailService;
    @Autowired
    private IJintieOutputBo outputBo;
    @Autowired
    JintieCustomFieldService JintieCustomFieldService;
    @Autowired
    private IJintieInputBo InputBo;
    @Autowired
    private IJintieOutputBo OutputBo;
    @Autowired
    JintieModelListService JintieModelListService;

    @Autowired
    JintieModelListMapper JintieModelListMapper;
    @Autowired
    JintieOcrTemplateMapper JintieOcrTemplateMapper;
    @Autowired
    private IJintieLogsService jintieLogsService;
    @Autowired
    private UserList userList;

    private Logger logger = LoggerFactory.getLogger(indexController.class);
    ReturnMessage msg = new ReturnMessage();


    //获取客户Key
    public String getCustomKeyList(String customName) {
        JintieCustom JintieCustom = IJintieCustomService.getCustomId(customName);
        return JintieCustom.getKey();
    }
    //获取业务编号
    public  String getywno() {
        String url = "http://export.wanlish.com/external/getsimp_ywno";
        try {
            String result = HttpServiceUtil.sendGet(url, "");
            return result.replaceAll("\"", "");
        } catch (Exception e) {
            logger.error("获取业务编号失败",e);
            return "";
        }
    }


    @GetMapping("searchCustom")
    @ResponseBody
    public Object indexList(String custName) {
        QueryWrapper<JintieCustom> queryWrapper = new QueryWrapper<>();
        if (custName != null){
            queryWrapper.lambda().like(JintieCustom::getCustomName, custName);
        }
        return jintieCustomService.list(queryWrapper);
    }

    @GetMapping("login")
    @ResponseBody
    public String login(String userName,String passwd) {
        String pwwd = userList.loginMap().get(userName);
        if (passwd==null) {
            return "error";
        }else if(pwwd == null){
            return "error";
        } else if (!pwwd.split(",")[0].equals(DigestUtils.sha256Hex(passwd + "961799449d6f4487acda8b06488f3300"))) {
            return "error";
        }else {
            return "success";
        }

    }

    @RequestMapping("ywList")
    @ResponseBody
    public Map ywList(MyPage page, JintieYwinfo params) {
        QueryWrapper<JintieYwinfo> queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("create_date_time");
        queryWrapper.lambda().ne(JintieYwinfo::getStatus, "已删除");
        Boolean uniqueQuery = true;
        //唯一编号存在
        if (!StringUtils.isEmpty(params.getYwNo())) {
            queryWrapper.lambda().eq(JintieYwinfo::getYwNo, params.getYwNo());
            uniqueQuery = true;
        }
        //客户业务编号SKL存在。
        if (!StringUtils.isEmpty(params.getCustomNo())) {
            queryWrapper.lambda().eq(JintieYwinfo::getCustomNo, params.getCustomNo());
            uniqueQuery = true;
        }

        if (uniqueQuery) {
            //业务状态不为空。
            if (!StringUtils.isEmpty(params.getStatus())){
                queryWrapper.lambda().eq(JintieYwinfo::getStatus, params.getStatus());
            }

            if (!StringUtils.isEmpty(params.getClearingCustomerName())){
                queryWrapper.lambda().eq(JintieYwinfo::getClearingCustomerName, params.getClearingCustomerName());
            }
            //时间范围
            if (!StringUtils.isEmpty(params.getDateStr())) {
                LocalDateTime startDate = CalendarUtils.getLocalDateTime(CalendarUtils.getParseDate(CalendarUtils.Y_M_DHMS_LONG, params.getDateStr().substring(0, params.getDateStr().indexOf("||")) + " 00:00:00"));
                LocalDateTime endDate = CalendarUtils.getLocalDateTime(CalendarUtils.getParseDate(CalendarUtils.Y_M_DHMS_LONG, params.getDateStr().substring(params.getDateStr().indexOf("||") + 2) + " 23:59:59"));
                queryWrapper.lambda().between(JintieYwinfo::getCreateDateTime, startDate, endDate);
            }
            //进出仓类型
            if (params.getType() != null){
                queryWrapper.lambda().eq(JintieYwinfo::getType, params.getType());
            }
            //？？？？？？
            if (!StringUtils.isEmpty(params.getCreateUser())) {
                if ("1".equals(params.getRemark10())){
                    queryWrapper.lambda().eq(JintieYwinfo::getCreateUser, params.getCreateUser());
                } else{
                    queryWrapper.lambda().eq(JintieYwinfo::getCreateUserEmp, params.getCreateUser());
                }
            }
            if (!StringUtils.isEmpty(params.getRemark())){
                queryWrapper.lambda().like(JintieYwinfo::getRemark, params.getRemark());
            }
        }
        //翻页查询。
        IPage<JintieYwinfo> ywinfoList = jintieYwinfoService.page(page, queryWrapper);


        List<JintieYwinfo> jintieYwinfoList = ywinfoList.getRecords();
        for (int i = 0; i < jintieYwinfoList.size(); i++) {
            //remark2对应的是模板的id
            String remark2 = jintieYwinfoList.get(i).getRemark2();
            if (org.apache.commons.lang3.StringUtils.isNoneBlank(remark2)) {
                //查询模板对照表，得到模板信息表对象。
                JintieModelList JintieModelList = JintieModelListMapper.selectByModel_id(remark2);
                if (JintieModelList != null) {
                    //jintieyewuinfo近铁信息表添加字段存模板中文名。把中文字段信息填入。
                    jintieYwinfoList.get(i).setModelName(JintieModelList.getRemarks1());
                }
            }
            String remark3 = jintieYwinfoList.get(i).getRemark3();
            if (org.apache.commons.lang3.StringUtils.isNoneBlank(remark3)) {
                List<JintieOcrTemplate> JintieOcrTemplate = JintieOcrTemplateMapper.selectByTemplateId(remark3);
                if (JintieOcrTemplate != null && JintieOcrTemplate.size() > 0) {
                    String name = "";
                    for (int j = 0; j < JintieOcrTemplate.size(); j++) {
                        name += JintieOcrTemplate.get(j).getName();
                    }
                    jintieYwinfoList.get(i).setTemplateName(name);
                }
            }
        }

        Map<String, Object> map = new HashMap<>();
        map.put("ywList", ywinfoList);
        //根据条件查询全部的记录。
        List<Object> queryList=jintieYwinfoService.listObjs(queryWrapper);
        map.put("queryAll", queryList);
        return map;
    }



    @GetMapping("getDetailByNo")
    @ResponseBody
    public Map getDetailByNoList(String wlywno, String type) throws Exception {
        if (!"进仓".equals(type) && !"出仓".equals(type))
            throw new IllegalAccessException("操作类型参数提供有误");
        if (StringUtils.isEmpty(wlywno))
            throw new IllegalAccessException("wlYwno参数提供有误");

        if ("进仓".equals(type)) {//进仓
            return inputBo.getDetailByNo(wlywno, type);
        } else {
            return outputBo.getDetailByNo(wlywno, type);
        }
    }


    @PostMapping("inputInsert")
    @ResponseBody
    public String inputInsert(@RequestBody jintie_list data) {
        try {
            return inputBo.insert(data);
        } catch (Exception ex) {
            return "saveError";
        }
    }

    @PostMapping("inputUpdate")
    @ResponseBody
    public String inputUpdate(@RequestBody jintie_list data) {
        try {
            return inputBo.update(data);
        } catch (Exception ex) {
            return "updateError";
        }
    }

    @PostMapping("outputInsert")
    @ResponseBody
    public String outputInsert(@RequestBody jintie_list data) {
        try {
            return outputBo.insert(data);
        } catch (Exception ex) {
            return "saveError";
        }
    }

    @PostMapping("outputUpdate")
    @ResponseBody
    public String outputUpdate(@RequestBody jintie_list data) {
        try {
            return outputBo.update(data);
        } catch (Exception ex) {
            return "updateError";
        }
    }

    @PostMapping("ywDrop")
    @ResponseBody
    public String ywDrop(@RequestBody DelInfo delInfo) {
        try {
            for (String ywno : delInfo.getYwlist()) {
                QueryWrapper<JintieYwinfo> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(JintieYwinfo::getYwNo, ywno);
                JintieYwinfo ywInfo = jintieYwinfoService.getOne(queryWrapper);
                ywInfo.setStatus("已删除");
                ywInfo.setUpdateUser(delInfo.getOpUser());
                ywInfo.setUpdateUserEmp(userList.loginMap().get(delInfo.getOpUser()).split(",")[1]);
                ywInfo.setUpdateDate(CalendarUtils.getLocalDateTime(new Date()));
                if (ywInfo.getType() == 0) {//进
                    inputBo.remove(ywno, ywInfo);
                } else if (ywInfo.getType() == 1) {//出
                    outputBo.remove(ywno, ywInfo);
                }
            }
        } catch (Exception ex) {
            logger.error("del wrong", ex);
            return "fail";
        }
        return "success";
    }

    @PostMapping("addYW")
    @ResponseBody
    public ReturnMessage addYW(@RequestBody JintieYwinfo ywinfo) {
        try {
            ywinfo.setCustomNo(ywinfo.getCustomNo().trim());
            if(jintieYwinfoService.selectByCustomNo(ywinfo.getCustomNo()) > 0){
                //结算客户存在
                msg.setStatus(ReturnMessage.STATUS_ERROR);
                msg.setMessage("结算客户编号已存在");
                return msg;
            }else{
                //结算客户不存在
                String ywno = getywno();
                SimpleDateFormat sfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String createDate = sfm.format(new Date());
                ywinfo.setYwNo(ywno);
                ywinfo.setRemark1(getCustomKeyList(ywinfo.getClearingCustomerName()));
                ywinfo.setCreateDate(createDate);
                ywinfo.setCreateDateTime(LocalDateTime.now());
//                ywinfo.setCreateUserEmp(UserList.loginMap.get(ywinfo.getCreateUser()).split(",")[1]);
                ywinfo.setCreateUserEmp(userList.loginMap().get(ywinfo.getCreateUser()).split(",")[1]);
                ywinfo.setStatus("未录入");
                jintieYwinfoService.saveOrUpdate(ywinfo);
                msg.setStatus("200");
                msg.setMessage("创建业务成功");
                return msg;
            }
        } catch (Exception ex) {
            logger.error("业务添加失败!",ex);
            msg.setStatus(ReturnMessage.STATUS_ERROR);
            msg.setMessage("saveError，原因"+ex.toString());
            return msg;
        }
    }

    @PostMapping("updateYW")
    @ResponseBody
    public String updateYW(@RequestBody JintieYwinfo ywinfo) {
        try {
            ywinfo.setUpdateDate(CalendarUtils.getLocalDateTime(new Date()));
            jintieYwinfoService.saveOrUpdate(ywinfo);
            if(ywinfo.getType() == 0){
                inputBo.saveInputUpdate(ywinfo);
            }else{
                outputBo.saveOutputUpdate(ywinfo);
            }
            return "updateNice";
        } catch (Exception ex) {
            logger.error("业务更新失败!",ex);
            return "updateError";
        }
    }


    @GetMapping("selectCode")
    @ResponseBody
    public List<JintieDict> selectCodeList(String dictType) {
        QueryWrapper<JintieDict> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(JintieDict::getInspectionType, dictType);
        queryWrapper.lambda().groupBy(JintieDict::getInspectionName);
        List<JintieDict> list = jintieDictService.list(queryWrapper);
        return list;
    }


    @PostMapping("audit")
    @ResponseBody
    public String audit(String wlywno, String status,String op_user) {
        QueryWrapper<JintieYwinfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(JintieYwinfo::getYwNo, wlywno);
        JintieYwinfo ywinfo= jintieYwinfoService.getOne(queryWrapper);

        if("1".equals(status)){//1-审核通过
            if("待审核".equals(ywinfo.getStatus()))
                ywinfo.setStatus("已审核");
            else if("待修改".equals(ywinfo.getStatus()))
                ywinfo.setStatus("已修改");
        }else{//0-驳回
            if("已审核".equals(ywinfo.getStatus()))
                ywinfo.setStatus("待审核");
            else if("已同步".equals(ywinfo.getStatus()))
                ywinfo.setStatus("待修改");
            else if("已修改".equals(ywinfo.getStatus()))
                ywinfo.setStatus("待修改");
            else if("已完成".equals(ywinfo.getStatus()))
                ywinfo.setStatus("待修改" );
        }
        ywinfo.setAuditUser(op_user);
        ywinfo.setAuditTime(CalendarUtils.getLocalDateTime(new Date()));
        ywinfo.setAuditUserEmp(userList.loginMap().get(op_user).split(",")[1]);
        ywinfo.setUpdateUser(op_user);
        ywinfo.setUpdateDate(CalendarUtils.getLocalDateTime(new Date()));
        ywinfo.setUpdateUserEmp(userList.loginMap().get(op_user).split(",")[1]);
        try{
            if (ywinfo.getType()==0) {//进
                inputBo.audit(wlywno,ywinfo);
            } else {//出
                outputBo.audit(wlywno,ywinfo);
            }
        }catch (Exception e){
            return "updateError";
        }
        return "updateNice";
    }


    /**
     * Excl导入Detail
     * @param excel
     * @param ywNo
     * @return
     * @throws IOException
     */
    @PostMapping("uploadExcel")
    @ResponseBody
    public ReturnMessage InsertExcelModel(MultipartFile excel,String ywNo) throws IOException {
        if("".equals(ywNo)){
            msg.setStatus(STATUS_ERROR);
            msg.setMessage("业务编号不能为空");
            return msg;
        }else {
            if (excel == null){
                msg.setStatus(STATUS_ERROR);
                msg.setMessage("文件不能为空");
                return msg;
            } else{
                logger.info("操作编号："+ywNo);
                JintieYwinfo ywinfo = jintieYwinfoService.selectByYwNo(ywNo);
                if(ywinfo.getType() == 0){
                    if(IJintieInputDetailService.saveExcelDate(ywinfo,excel)){
                        msg.setStatus(STATUS_OK);
                        msg.setMessage("Excel加载数据成功");
                        return msg;
                    }else {
                        msg.setStatus(STATUS_ERROR);
                        msg.setMessage("Excel加载数据失败");
                        return msg;
                    }

                }else{
                    if(IJintieOutputDetailService.saveExcelDate(ywinfo,excel)){
                        msg.setStatus(STATUS_OK);
                        msg.setMessage("Excel加载数据成功");
                        return msg;
                    }else {
                        msg.setStatus(STATUS_ERROR);
                        msg.setMessage("Excel加载数据失败");
                        return msg;
                    }

                }
            }
        }
    }

    /**
     * 另存为功能接口
     * @param ywNo
     * @return
     * @throws Exception
     */
    @PostMapping("SaveAs")
    @ResponseBody
    public ReturnMessage SaveAs(@RequestBody String ywNo){
        Map maps = (Map) JSON.parse(ywNo);
        JintieYwinfo ywinfo = jintieYwinfoService.selectByYwNo(maps.get("ywNo")+"");
        logger.info("另存为编号"+maps.get("ywNo"));
        if(ywinfo.getType() == 0){
            return InputBo.savaAs(ywinfo);
        }else {
            return OutputBo.saveAs(ywinfo);
        }

    }

    /**
     * 模板接口
     * @return
     */
    @PostMapping("createModel")
    @ResponseBody
    public ReturnMessage createModel(@RequestBody JintieModelList model){
        //model.setCreat_user(UserList.loginMap.get(model.getCreate_user()).split(",")[1]);
        return JintieModelListService.createModel(model);
    }

    /**
     * 删除模板
     * @return
     */
    @PostMapping("deleteModel")
    @ResponseBody
    public ReturnMessage deleteModel(@RequestBody String modelList){
        Map map = (Map) JSON.parse(modelList);
        List<String> list = (List<String>) map.get("model_id");
        return JintieModelListService.deleteModel(list);

    }

    /**
     * 获得详细模板界面
     * @return
     */
    @PostMapping("getTemplateByModelId")
    @ResponseBody
    public Map getTemplateByModelIdList(@RequestBody String modelId){
        Map maps = (Map) JSON.parse(modelId);
        return JintieModelListService.getTemplateByModelId(maps.get("modelId")+"");
    }

    /**
     * 模板列表
     * @return
     */
    @PostMapping("modelList")
    @ResponseBody
    public Map modelList(@RequestBody JintieModeParm parm){
        return JintieModelListService.getModelList(parm);
    }
    /**
     * 通过模板配置客户字段
     * @return
     */
    public String setCustomField(String text,String model_id){
        Map maps = (Map) JSON.parse(text);
        String json = maps.get("detailDeploy")+"";
        Map map = (Map) JSON.parse(json);
        Iterator<String> it = map.keySet().iterator();
        JintieModelList modelList = JintieModelListMapper.selectByModel_id(model_id);
        JintieCustom JintieCustom = IJintieCustomService.getCustomId(modelList.getCustom_key());
        //删除原始显示
        JintieCustomFieldService.deleteByModelIdAndCustomId(JintieCustom.getKey(),modelList.getRemarks2(),model_id);
        while (it.hasNext()){
            JintieCustomField customField = new JintieCustomField();
            String key = it.next();
            String value = map.get(key)+"";
            if(key !="btnAdd"){
                Map valueMap = (Map) JSON.parse(value);
                if("true".equals(valueMap.get("showInput")+"")){
                    if(!"".equals(valueMap.get("value"))){
                        customField.setCustomid(Integer.parseInt(JintieCustom.getCustomId()+""));
                        customField.setJintieField(key.toUpperCase());
                        customField.setField(valueMap.get("value")+"");
                        customField.setRemarks1(JintieCustom.getKey());
                        customField.setRemarks2(modelList.getRemarks2());
                        if( "true".equals(valueMap.get("showFlag")+"")){
                            customField.setRemarks3("Y");
                        }
                        customField.setRemarks4(modelList.getCustom_key());
                        customField.setRemarks5(model_id);
                        JintieCustomFieldService.insert(customField);
                    }
                    if("countryoforigin".equals(key)){
                        customField.setCustomid(Integer.parseInt(JintieCustom.getCustomId()+""));
                        customField.setJintieField(key.toUpperCase());
                        customField.setField("原产国");
                        customField.setRemarks1(JintieCustom.getKey());
                        customField.setRemarks2(modelList.getRemarks2());
                        customField.setRemarks4(modelList.getCustom_key());
                        customField.setRemarks5(model_id);
                        JintieCustomFieldService.insert(customField);
                    }
                }
            }
        }
        return "OK";
    }






    /**
     * 修改模板
     * @return
     */
    @PostMapping("updateTemplate")
    @ResponseBody
    public ReturnMessage updateTemplate(@RequestBody String data){
        Map maps = (Map) JSON.parse(data);
        JintieModel template = new JintieModel();
        template.setRemark1(maps.get("model_id")+"");
        template.setText(maps.get("text")+"");
        setCustomField(maps.get("text")+"",maps.get("model_id")+"");
        template.setRemark2(maps.get("user")+"");
        logger.info("模板修改，模板："+template.getRemark1()+",操作人："+template.getRemark2());
        return JintieModelListService.updateTemplate(template);
    }







    /**
     * 新建识别模板
     * @return
     */
    @PostMapping("insertOcrList")
    @ResponseBody
    public ReturnMessage insertOcrTemplate(@RequestBody JintieOcrTemplate jintieOcrTemplate){
        ReturnMessage msg = new ReturnMessage();
        jintieOcrTemplate.setRemarks10("已启用");
        if(JintieOcrTemplateMapper.insert(jintieOcrTemplate) > 0){
            msg.setStatus(ReturnMessage.STATUS_OK);
            msg.setMessage("新增成功");
        }else {
            msg.setStatus(ReturnMessage.STATUS_ERROR);
            msg.setMessage("添加失败");
        }
        return msg;
    }
    /**
     * 删除识别模板
     * @return
     */
    @PostMapping("deleteOcrTemplate")
    @ResponseBody
    @Transactional
    public ReturnMessage deleteOcrTemplate(@RequestBody String idList){
        ReturnMessage msg = new ReturnMessage();
        Map map = (Map) JSON.parse(idList);
        try {
            List<Integer> list = (List<Integer>) map.get("idList");
            for (Integer obj:list) {
                JintieOcrTemplateMapper.deleteByPrimaryKey(obj);
            }
            msg.setStatus(ReturnMessage.STATUS_OK);
            msg.setMessage("删除成功");
        }catch (Exception e){
            msg.setStatus(ReturnMessage.STATUS_ERROR);
            msg.setMessage("删除失败，原因："+e.toString());
        }
        return msg;
    }

    /**
     * 修改识别模板
     * @return
     */
    @PostMapping("updateOcrTemplate")
    @ResponseBody
    public ReturnMessage updateOcrTemplate(@RequestBody JintieOcrTemplate JintieOcrTemplate){
        ReturnMessage msg = new ReturnMessage();
        if(JintieOcrTemplateMapper.updateByPrimaryKey(JintieOcrTemplate) == 1){
            msg.setStatus(ReturnMessage.STATUS_OK);
            msg.setMessage("修改成功");
        }else {
            msg.setStatus(ReturnMessage.STATUS_ERROR);
            msg.setMessage("修改失败");
        }
        return msg;
    }
    /**
     * 识别模板列表
     * @return
     */
    @PostMapping("getOcrList")
    @ResponseBody
    public Map getOcrList(@RequestBody JintieOcrTemplateParm parm){
        Map map = new HashMap();
        int page =Integer.parseInt(parm.getPage());
        int rows = Integer.parseInt(parm.getRows());
        Page<JintieOcrTemplate> p = new Page<>(page, rows);
        List<JintieOcrTemplate> list = JintieOcrTemplateMapper.selectByParm(p,parm);
        for (JintieOcrTemplate model:list) {
            model.setCustomkey(IJintieCustomService.getCustomName(model.getCustomkey()).getCustomName());
        }
        map.put("rows",list);
        map.put("count",p.getTotal());
        return map;
    }

    @PostMapping("getOcrBaseData")
    @ResponseBody
    public IPage<JintieLogs> getOcrBaseDataList(@RequestBody String data) {
        IPage<JintieLogs> jintieLogsIPage = jintieLogsService.getOcrBaseDataList(data);
        return jintieLogsIPage;
    }

    /**
     * Excel导出
     * @param
     */
    public ReturnMessage reverseExcel(String ywNo) throws IOException {
            //从数据库查询出信息（唯一编号，直接返回对象）
            JintieYwinfo info = jintieYwinfoService.selectByYwNo(ywNo);
            if(info != null){
                //type判断进出类型。（0进1出）
                if( info.getType() == 0 ){
                    msg = IJintieInputDetailService.reverseExcel(info);
                }else {
                    msg = IJintieOutputDetailService.reverseExcel(info);
                }
            }else{
                msg.setStatus("500");
                msg.setMessage("业务数据不存在");
            }
            return  msg;
    }

    /**
     * 报表导出
     * @param page
     * @param params
     * @return
     */
    @RequestMapping("exportList")
    @ResponseBody
    public ReturnMessage exportList(MyPage page,JintieYwinfo params){
        //导出业务记录信息EXCEL表。
        page.setCurrent(1);
        page.setSize(100000);
        List<JintieYwinfo> queryList =(List<JintieYwinfo>) ywList(page,params).get("queryAll");
        ReturnMessage msg=jintieYwinfoService.reverseExcel(queryList);
        return msg;
    }
    /**
     * 文件下载
     * @return
     * @throws IOException
     */
    @GetMapping("/download")
    public void download(String ywNo, HttpServletResponse response) {
       try {
           ReturnMessage msg = reverseExcel(ywNo);
           File file=new File("E:\\kweServer\\reverseExcelFile\\"+ ywNo+".xls");
           String fileName = file.getName();
           byte[] data = FileUtil.File2byte(file);
           response.setContentType("application/octet-stream");//告诉浏览器输出内容为流
           fileName = new String(fileName.getBytes(), "utf-8");
           response.setHeader("Content-disposition", "attachment;filename=" + fileName);
           response.setHeader("status", msg.getStatus());
           Base64.Encoder encoder = Base64.getEncoder();
           try (OutputStream os = response.getOutputStream()) {
               os.write(data);
               os.flush();
           }
       }catch (Exception e){

       }
    }

    @RequestMapping("/downloadList")
    public void download(@RequestBody JintieYwinfo params,HttpServletResponse response) {
        try {
            MyPage page = new MyPage();
            ReturnMessage msg = exportList(page,params);
            String time=new SimpleDateFormat("yyyyMMdd").format(new Date());
            File file=new File("E:\\kweServer\\reverseExcelFile\\"+ time +".xls");
            String fileName = file.getName();
            byte[] data = FileUtil.File2byte(file);
            response.setContentType("application/octet-stream");//告诉浏览器输出内容为流
            fileName = new String(fileName.getBytes(), "utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName);
            response.setHeader("status", msg.getStatus());
            Base64.Encoder encoder = Base64.getEncoder();
            try (OutputStream os = response.getOutputStream()) {
                os.write(data);
                os.flush();
            }
        }catch (Exception e){

        }
    }



}
