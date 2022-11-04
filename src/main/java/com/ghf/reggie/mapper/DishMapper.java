package com.ghf.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ghf.reggie.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

/*
* 1、加Mapper注解
* 2、继承基类Mapper
* */
@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
