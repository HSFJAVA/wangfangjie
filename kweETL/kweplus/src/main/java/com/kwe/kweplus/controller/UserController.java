package com.kwe.kweplus.controller;


import com.kwe.kweplus.dao.UserMapper;
import com.kwe.kweplus.modal.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author jobob
 * @since 2019-03-26
 */
@RestController
@RequestMapping("/kwe/user")
public class UserController {


    @Autowired
    private UserMapper userMapper;

    @PostMapping("register")
    public Map<String, Object> register(User userRegister) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            String newPassword = userRegister.getPwd();
            User user = new User();
            user.setUid(UUID.randomUUID().toString());
            user.setUname(userRegister.getUname());
            user.setPwd(newPassword);

            //注册时间。
            Date nowTime = new Date();
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd-hh:mm:ss");
            String timeStr = sf.format(nowTime);
            user.setRemarks6(timeStr);

            //就职状态默认就职。
            user.setStatus("就职");

            user.setChinesename(userRegister.getChinesename());
            user.setRemarks1(userRegister.getRemarks1());
            user.setRemarks2(userRegister.getRemarks2());
            user.setRemarks3(userRegister.getRemarks3());
            user.setRemarks4(userRegister.getRemarks4());
            user.setRemarks5(userRegister.getRemarks5());
          /*  System.out.println(user.getChinesename());
            System.out.println(user!=null);*/

            resultMap.put("code", "1");
            resultMap.put("message", "success");
            resultMap.put("user", user);
            return resultMap;
        } catch (Exception ex) {
            resultMap.put("code","-1");
            resultMap.put("message",ex.getMessage());
            return resultMap;
        }
    }
}
