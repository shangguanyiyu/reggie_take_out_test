package com.ghf.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ghf.reggie.dto.SetmealDto;
import com.ghf.reggie.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;
/*
* 1、加Mapper注解
* 继承BaseMapper<entity>
*
* */
@Mapper
public interface SetmealDishMapper extends BaseMapper<SetmealDish> {

}
