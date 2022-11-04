package com.ghf.reggie.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ghf.reggie.dto.SetmealDto;
import com.ghf.reggie.entity.SetmealDish;
import com.ghf.reggie.mapper.SetmealDishMapper;
import com.ghf.reggie.mapper.SetmealMapper;
import com.ghf.reggie.service.SetmealDishService;
import org.springframework.stereotype.Service;

/*
* 1、Service注解
* 2、继承ServiceImpl<Mapper,entity>
* 3、实现本地Service接口
* */
@Service
public class SetmealDishImpl extends ServiceImpl<SetmealDishMapper, SetmealDish>
        implements SetmealDishService {


}
