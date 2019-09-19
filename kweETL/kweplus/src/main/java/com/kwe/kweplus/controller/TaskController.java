package com.kwe.kweplus.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kwe.kweplus.bo.ITaskBo;
import com.kwe.kweplus.modal.JintieInputHead;
import com.kwe.kweplus.modal.JintieOutputHead;
import com.kwe.kweplus.modal.JintieYwinfo;
import com.kwe.kweplus.service.IJintieInputHeadService;
import com.kwe.kweplus.service.IJintieOutputHeadService;
import com.kwe.kweplus.util.CalendarUtils;
import com.kwe.kweplus.util.MyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Controller
@EnableScheduling
public class TaskController {

    private static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private Logger logger = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    private ITaskBo taskBo;
    @Autowired
    private IJintieInputHeadService inputHeadService;
    @Autowired
    private IJintieOutputHeadService outputHeadService;

    private LocalDateTime syncTime;
    /**
     * 每隔一小时处理
     */
//    @Scheduled(cron = "0 0 0/1 * * ?")
    @Scheduled(cron = "0/50 * * * * ?")
    public void refreshStatus() {
        logger.debug("进入定时器执行");
        lock.writeLock().lock();
        String currentDateTimeStr = CalendarUtils.getFormatDate("yyyy-MM-dd HH:mm:ss",new Date());
        try {
            if(syncTime==null){//如果是初始化,取当天0点后的数据同步
                String dateStr = CalendarUtils.getFormatDate("yyyy-MM-dd",new Date())+" 00:00:00";
                LocalDateTime initTime  = CalendarUtils.getLocalDateTime(CalendarUtils.getParseDate("yyyy-MM-dd HH:mm:ss",dateStr));
                syncTime = initTime;
            }
            //处理进口回写
            QueryWrapper<JintieInputHead> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().ge(JintieInputHead::getTaketime, syncTime);
            queryWrapper.lambda().in(JintieInputHead::getStatus,3,4);
            queryWrapper.select("wl_ywno","status","taketime");
            List<JintieInputHead> list = inputHeadService.list(queryWrapper);
            if(MyUtils.isNotEmpty(list)){
                list.forEach(e->{
                    try{
                        taskBo.refreshtatus(e.getWlYwno(),e.getStatus(),e.getTaketime());
                    }catch (Exception ex){
                        logger.error("进口状态回写失败!",ex);
                    }
                });
            }

            //处理出口回写
            QueryWrapper<JintieOutputHead> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.lambda().ge(JintieOutputHead::getTaketime, syncTime);
            queryWrapper1.lambda().in(JintieOutputHead::getStatus,3,4);
            queryWrapper1.select("wl_ywno","status","taketime");
            List<JintieOutputHead> list1 = outputHeadService.list(queryWrapper1);
            if(MyUtils.isNotEmpty(list1)){
                list1.forEach(e->{
                    try{
                        taskBo.refreshtatus(e.getWlYwno(),e.getStatus(),e.getTaketime());
                    }catch (Exception ex){
                        logger.error("出口状态回写失败!",ex);
                    }
                });
            }

        }catch (Exception e){
            logger.error("定时器处理失败!",e);
        }finally {
//            syncTime  = CalendarUtils.getLocalDateTime(CalendarUtils.getParseDate("yyyy-MM-dd HH:mm:ss",currentDateTimeStr));
            lock.writeLock().unlock();
        }
    }
}
