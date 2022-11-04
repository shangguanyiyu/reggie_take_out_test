package com.ghf.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ghf.reggie.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;

/*
* 1、加Mapper注解
* 2、继承MP的BaseMapper<entity>
* */
@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {
}
