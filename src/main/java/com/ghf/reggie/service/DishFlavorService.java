package com.ghf.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ghf.reggie.entity.Dish;
import com.ghf.reggie.entity.DishFlavor;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/*
* 1、继承IService<entity>
* */
public interface DishFlavorService extends IService<DishFlavor> {
}
