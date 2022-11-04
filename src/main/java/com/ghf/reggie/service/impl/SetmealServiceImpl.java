package com.ghf.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ghf.reggie.common.CustomException;
import com.ghf.reggie.dto.SetmealDto;
import com.ghf.reggie.entity.Dish;
import com.ghf.reggie.entity.Setmeal;
import com.ghf.reggie.entity.SetmealDish;
import com.ghf.reggie.mapper.SetmealMapper;
import com.ghf.reggie.service.SetmealDishService;
import com.ghf.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/*
* 加Service
* 继承ServiceImpl
* 实现本地Service
* */
@Service
public class SetmealServiceImpl extends
        ServiceImpl<SetmealMapper, Setmeal>
        implements SetmealService{

    /*
     * x新增套餐，同时保存套餐和菜品的关联关系
     * */
    @Autowired
    private SetmealDishService setmealDishService;


    @Override
    @Transactional//因为要操作两张表
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套餐基本信息，操作setmeal insert
        this.save(setmealDto);//因为当前的setmealDto，继承了setmeal,而Setmeal与setmeanl的表有映射关系

        //遍历每个setmeal,设置setmealId
        final List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map(
                (item)->{
                    item.setSetmealId(setmealDto.getId());
                    return item;
                }
        ).collect(Collectors.toList());

        //2,套餐和菜品 操作setmeal_dish
        setmealDishService.saveBatch(setmealDto.getSetmealDishes());

    }

    @Override
    public void removeWithDish(List<Long> ids) {
        /* select count(*) from setmeal where id in (1,2,3) and status = 1
        查询状态是否可以删除,不能删，则抛出异常
         * 删除套餐及其关联数据*/
        LambdaQueryWrapper<Setmeal> dishLambdaQueryWrapper =new LambdaQueryWrapper<Setmeal>();
        dishLambdaQueryWrapper.in(Setmeal::getId, ids);
        dishLambdaQueryWrapper.eq(Setmeal::getStatus,1);
        final int count = this.count(dishLambdaQueryWrapper);
        if(count>0){
            throw  new CustomException("套餐售卖中，删不了");

        }


        //1.删除套餐
        this.removeByIds(ids);
        //2。删除关联数据，其数据在setmeanl_dish表中
        final LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);

        setmealDishService.remove(setmealDishLambdaQueryWrapper);

    }
}
