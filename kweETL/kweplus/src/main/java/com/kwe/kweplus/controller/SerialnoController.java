package com.kwe.kweplus.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kwe.kweplus.modal.Serialno;
import com.kwe.kweplus.service.ISerialnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jobob
 * @since 2019-03-25
 */
@RestController
@RequestMapping("/kwe/serialno")
public class SerialnoController {
    @Autowired
    private ISerialnoService serialnoService;

    @GetMapping("/getMessageHead")
    public String getNO(){
        //保证表中当前年月日只有一条数据,每拿一次更新num
        String messageHead = "";
        SimpleDateFormat smf = new SimpleDateFormat("yyyy-MM-dd");
        String year = smf.format(new Date()).split("-")[0];
        String mon = smf.format(new Date()).split("-")[1];
        String day = smf.format(new Date()).split("-")[2];
        QueryWrapper<Serialno> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Serialno::getCurrDay,day);
        queryWrapper.lambda().eq(Serialno::getCurrMonth,mon);
        queryWrapper.lambda().eq(Serialno::getCurrYear,year);
        queryWrapper.lambda().eq(Serialno::getSerialnoName,"GD");
        queryWrapper.lambda().eq(Serialno::getSerialnoType,"GD");
        List<Serialno> list = serialnoService.list(queryWrapper);
        if(null == list || list.size() == 0){
            Serialno serialno = new Serialno();
            serialno.setCurrDay(day);
            serialno.setCurrMonth(mon);
            serialno.setCurrYear(year);
            serialno.setCurrNum("1");
            serialno.setSerialnoLength(16);
            serialno.setSerialnoName("GD");
            serialno.setSerialnoType("GD");
            serialno.setSerialnoRule("");
            serialnoService.save(serialno);
            return "GD"+year+""+mon+""+day+"001";
        }else if(list.size() != 0){
            int currNum = Integer.parseInt(list.get(0).getCurrNum()) + 1;
            list.get(0).setCurrNum(String.valueOf(currNum));
            Serialno serialno = list.get(0);
            String no = "";
            if(currNum >= 1 && currNum <=9){
                no = "GD"+serialno.getCurrYear()+""+serialno.getCurrMonth()+""+serialno.getCurrDay()+"00"+serialno.getCurrNum();
            }else if(currNum >= 10 && currNum <= 99){
                no = "GD"+serialno.getCurrYear()+""+serialno.getCurrMonth()+""+serialno.getCurrDay()+"0"+serialno.getCurrNum();
            }else if(currNum >= 100 && currNum <= 999){
                no = "GD"+serialno.getCurrYear()+""+serialno.getCurrMonth()+""+serialno.getCurrDay()+""+serialno.getCurrNum();
            }
            serialnoService.update(serialno,queryWrapper);
           return no;
        }
        return "error";
    }

}
