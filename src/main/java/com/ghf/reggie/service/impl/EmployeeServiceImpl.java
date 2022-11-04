package com.ghf.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ghf.reggie.common.CustomException;
import com.ghf.reggie.entity.Dish;
import com.ghf.reggie.entity.Employee;
import com.ghf.reggie.entity.Setmeal;
import com.ghf.reggie.mapper.EmployeeMapper;
import com.ghf.reggie.service.DishService;
import com.ghf.reggie.service.EmployeeService;
import com.ghf.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {


}
