package com.ghf.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ghf.reggie.dto.SetmealDto;
import com.ghf.reggie.entity.Setmeal;
import org.springframework.stereotype.Service;

import java.util.List;

/*
* 加Service注解
* 继承IService
* */

public interface SetmealService extends IService<Setmeal> {
    /*
     * x新增套餐，同时保存套餐和菜品的关联关系
     * */
    public void saveWithDish(SetmealDto setmealDto);
    /*
    * 删除套餐及其关联数据*/
    public void removeWithDish(List<Long> ids);
}
