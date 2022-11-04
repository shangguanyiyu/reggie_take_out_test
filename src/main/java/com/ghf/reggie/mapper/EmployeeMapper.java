package com.ghf.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ghf.reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper /*创建实体类对应的mapper接口，继承BaseMapper（里面有CRUD操作）*/
public interface EmployeeMapper extends BaseMapper<Employee> {
}
