package com.ghf.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ghf.reggie.entity.DishFlavor;
import com.ghf.reggie.mapper.DishFlavorMapper;
import com.ghf.reggie.service.DishFlavorService;
import org.springframework.stereotype.Service;
/*
* 1、加Service注解
* 2、继承ServiceImpl<Mapper,entity>
3、实现entity的Service接口
* */
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor>
                implements DishFlavorService {
}
