package com.ghf.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ghf.reggie.entity.AddressBook;
import com.ghf.reggie.mapper.AddressBookMapper;
import com.ghf.reggie.service.AddressBookService;
import org.springframework.stereotype.Service;

/*
* Service接口实现类
* 1、加注解@Service
* 2、继承ServiceImpl<M,T>
   3、实现本地Service接口
* */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook>
        implements AddressBookService {
}
