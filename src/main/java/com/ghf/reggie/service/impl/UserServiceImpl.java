package com.ghf.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ghf.reggie.entity.User;
import com.ghf.reggie.mapper.UserMapper;
import com.ghf.reggie.service.UserService;
import org.springframework.stereotype.Service;

/*
* 1、@Service
* 2、MP的ServiceImpl<M,T>
    3、继承本地的Service
* */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
}
