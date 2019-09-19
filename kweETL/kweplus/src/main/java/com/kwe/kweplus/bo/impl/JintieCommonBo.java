package com.kwe.kweplus.bo.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kwe.kweplus.bo.IJintieCommonBo;
import com.kwe.kweplus.modal.Serialno;
import com.kwe.kweplus.service.ISerialnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class JintieCommonBo implements IJintieCommonBo {

    @Autowired
    private ISerialnoService serialnoService;

    @Override
    public String getMessageHead(){
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
        queryWrapper.lambda().eq(Serialno::getSerialnoName,"KWE");
        queryWrapper.lambda().eq(Serialno::getSerialnoType,"KWE");
        List<Serialno> list = serialnoService.list(queryWrapper);
        if(null == list || list.size() == 0){
            Serialno serialno = new Serialno();
            serialno.setCurrDay(day);
            serialno.setCurrMonth(mon);
            serialno.setCurrYear(year);
            serialno.setCurrNum("1");
            serialno.setSerialnoLength(16);
            serialno.setSerialnoName("KWE");
            serialno.setSerialnoType("KWE");
            serialno.setSerialnoRule("");
            serialnoService.save(serialno);
            return "KWE"+year+""+mon+""+day+"0001";
        }else if(list.size() != 0){
            int currNum = Integer.parseInt(list.get(0).getCurrNum()) + 1;
            list.get(0).setCurrNum(String.valueOf(currNum));
            Serialno serialno = list.get(0);
            String no = "";
            if(currNum >= 1 && currNum <=9){
                no = "KWE"+serialno.getCurrYear()+""+serialno.getCurrMonth()+""+serialno.getCurrDay()+"000"+serialno.getCurrNum();
            }else if(currNum >= 10 && currNum <= 99){
                no = "KWE"+serialno.getCurrYear()+""+serialno.getCurrMonth()+""+serialno.getCurrDay()+"00"+serialno.getCurrNum();
            }else if(currNum >= 100 && currNum <= 999){
                no = "KWE"+serialno.getCurrYear()+""+serialno.getCurrMonth()+""+serialno.getCurrDay()+"0"+serialno.getCurrNum();
            }else if(currNum >= 1000 && currNum <= 9999){
                no = "KWE"+serialno.getCurrYear()+""+serialno.getCurrMonth()+""+serialno.getCurrDay()+""+serialno.getCurrNum();
            }
            serialnoService.update(serialno,queryWrapper);
            return no;
        }
        return "error";
    }
}
