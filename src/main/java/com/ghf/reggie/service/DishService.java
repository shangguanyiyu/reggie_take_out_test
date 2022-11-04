package com.ghf.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ghf.reggie.dto.DishDto;
import com.ghf.reggie.entity.Dish;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/*
* 继承IService
* */
public interface DishService extends IService<Dish> {
    /*
    * 新增菜品，然后插入对应的口味数据，需要操作两个表，dish,dish_flavor
    *
    * */
    public void saveWithFlavor(DishDto dishDto);
    /*根据id查菜品信息和口味信息*/
    public DishDto getByIdWithFlavor(Long id);

    public void updateWithFlavor(DishDto dishDto);

}
