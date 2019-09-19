package com.kwe.kweplus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kwe.kweplus.dao.UserMapper;
import com.kwe.kweplus.modal.User;
import com.kwe.kweplus.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jobob
 * @since 2019-03-26
 */
@Service
public class UserServiceImpl implements IUserService {


    @Override
    public User selectUnameAndPwd(String userName, String pwd) {
        return null;
    }
}
