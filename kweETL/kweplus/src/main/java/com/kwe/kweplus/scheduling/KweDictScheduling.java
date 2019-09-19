package com.kwe.kweplus.scheduling;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ly on 2019/8/26 13:30
 */
//@Component
public class KweDictScheduling {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    //每隔2秒执行一次
    @Scheduled(fixedRate = 10000)
    public void tasksInSecond() {
        System.out.println("定时任务执行时间：" + dateFormat.format(new Date()));
    }

    //每天3：05执行
    @Scheduled(cron = "0 0 14 ? * *")
    public void TasksInDate() {
        System.out.println("定时任务执行时间：" + dateFormat.format(new Date()));
    }

}
