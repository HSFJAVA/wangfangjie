package com.kwe.kweplus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kwe.kweplus.modal.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jobob
 * @since 2019-03-26
 */
/*public interface IUserService extends IService<User> {

}*/
public interface IUserService {

    //通过用户名密码查询User信息。
   /* User selectUnameAndPwd (String userName,String pwd);*/
    User selectUnameAndPwd (String userName,String pwd);
    //注册新用户。

}
