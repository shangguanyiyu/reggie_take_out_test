package com.ghf.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ghf.reggie.common.BaseContext;
import com.ghf.reggie.dto.DishDto;
import com.ghf.reggie.entity.Dish;
import com.ghf.reggie.entity.DishFlavor;
import com.ghf.reggie.mapper.DishMapper;
import com.ghf.reggie.service.DishFlavorService;
import com.ghf.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
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
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService
{
    /*
    * 新增菜品，口味手机
    *
    * */
    @Autowired
    private DishFlavorService dishFlavorService;
//    @Override
    @Transactional/*涉及到多表操作，要设置为事务，但是要让这个表生效，要在启动类开启@EnableTransactionManagement
     */
    @Override
    public void saveWithFlavor(DishDto dishDto) {
        this.save(dishDto);/*保存后，这里已经有dish_id了*/

        /*
        * 保存到口味,批量保存
        * */
        final Long id = dishDto.getId();/*拿到菜品的ID*/
//        dishDto.setId(id);
        final List<DishFlavor> flavors = dishDto.getFlavors();
        /*
        * 一个item就是一个DishFlavor
        * */
        flavors.stream().map((item)->{
            item.setDishId(id);
            item.setCreateUser(BaseContext.getCurrentId());
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }

    /*
    * 1、查询菜品基本信息
    * 2、口味信息
    * */
    @Override
    public DishDto getByIdWithFlavor(Long id) {

        //1、查询菜品基本信息
        final Dish dish = this.getById(id);
        final DishDto dishDto = new DishDto();

        BeanUtils.copyProperties(dish,dishDto);
//2、口味信息
        //2.1 条件构造
        final LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //2.2下限
        dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,dish.getId());

        final List<DishFlavor> flavors = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
        dishDto.setFlavors(flavors);
        return dishDto;
    }

    @Transactional/*涉及到多表操作，要设置为事务，但是要让这个表生效，要在启动类开启@EnableTransactionManagement
     */
    @Override
    public void updateWithFlavor(DishDto dishDto) {
        this.updateById(dishDto);/*1更新，dishDto是dish的自子类*/



        /*
         * 2\完全清理数据delete，3、然后在添加insert（口味）
         * */

        //2、
        final LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(dishFlavorLambdaQueryWrapper);
//3、拿到口味数据
        final List<DishFlavor> flavors = dishDto.getFlavors();

        /*给口味数据加id*/
        flavors.stream().map(
                (item)->{
                    item.setDishId(dishDto.getId());
                    item.setUpdateUser(BaseContext.getCurrentId());

                    return item;
                }
        ).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);

    }

}
