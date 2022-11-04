package com.ghf.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ghf.reggie.common.CustomException;
import com.ghf.reggie.entity.Category;
import com.ghf.reggie.entity.Dish;
import com.ghf.reggie.entity.Setmeal;
import com.ghf.reggie.mapper.CategoryMapper;
import com.ghf.reggie.service.CategoryService;
import com.ghf.reggie.service.DishService;
import com.ghf.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service/*implements CategoryService*/
/*
* 根据ID删除分类，删除之前需要进行查询是否关联
* */
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    /*注入查菜品的service*/
    @Autowired
    private DishService dishService;
    /*
     * 注入套餐Service*/
    @Autowired
    private SetmealService setmealService;

    @Override
    public void remove(Long id) {
        /*
         * 根据id删除分类
         * 查询是否关联菜品，若是关联，抛出异常
         * */
        //构造查询条件，框架帮忙发SQL
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper =new LambdaQueryWrapper<Dish>();
        //等值查询
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);/*关联菜品*/
        final int count = dishService.count(dishLambdaQueryWrapper);

        if(count>0){/*已关联*/
            throw new CustomException("已关联菜品，不能删除");/*抛出，给全局异常处理器处理*/
        }

        /* 关联套餐*/
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        final int count1 = setmealService.count();
        if(count1>0){
            throw new CustomException("已关联套餐，不能删除");/*抛出，给全局异常处理器处理*/

        }
        super.removeById(id);


    }

}
