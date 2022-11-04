package com.ghf.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ghf.reggie.common.BaseContext;
import com.ghf.reggie.common.R;
import com.ghf.reggie.entity.Employee;
import com.ghf.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService  employeeService;

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest httpServletRequest, @RequestBody Employee employee){ /*前端返回一个json对象{key:username,value:值
    key:password,value:
    },因此要用RequestBody，
    httpServletRequest：用户登录后，通过httpServletRequest获取用户登录的session
    */
        /*
        * 处理逻辑如下∶
        1、将页面提交的密码password进行md5加密处理2、根据页面提交的用户名username查询数据库3、如果没有查询到则返回登录失败结果
        4、密码比对，如果不一致则返回登录失败结果
        5、查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        * 6、登录成功，将员工id存入Session并返回登录成功结果

        * */
        val password = employee.getPassword();
        final val pd = DigestUtils.md5DigestAsHex(password.getBytes());
        /*查数据库*/
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<Employee>();

        queryWrapper.eq(Employee::getUsername,employee.getUsername());/*根据用户名等值查询*/

        Employee emp = employeeService.getOne(queryWrapper);/*getOne的前提是查询字段是唯一的*/
        if(emp==null){
            return R.error("登录失败，该用户不存在");
        }
        /*密码比对*/
        if(!emp.getPassword().equals(pd)){/*数据库查询的比对pd*/
            return R.error("登录失败");
        }

        /*状态*/
        if(emp.getStatus()==0){
            return R.error("无权限，禁用");
        }

        httpServletRequest.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);/*将查询后的对象返回*/


    }
    /*员工退出*/
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest httpServletRequest){
        httpServletRequest.getSession().removeAttribute("employee");/*移除*/
        return R.success("退出成功");
    }

    /*
    * 新增员工：从前端页面拿数据
    * */
    @PostMapping()
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        log.info("新增员工，员工信息={}",employee.toString());
        final String s = DigestUtils.md5DigestAsHex("123456".getBytes());

        employee.setPassword(s);
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        /*
        * 通过HttpServletRequest拿到session中的对象
        * 登录时候放的httpServletRequest.getSession().setAttribute("employee",emp.getId());

         * */
        Long empId = (Long)request.getSession().getAttribute("employee");
        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);
        employeeService.save(employee);/*save是由MP提供的*/
        return R.success("新增成功");
    }

    @GetMapping("/page")
    public R<Page> page(int  page,int pageSize,String name){/*Page是MP提供的对象*/
    log.info("page={},pageSize={},name={}",page,pageSize,name);

    //构造分页构造器
        final Page pageInfo = new Page(page, pageSize);


        //有name则构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper =  new LambdaQueryWrapper();
        /*添加查询条件*/
        queryWrapper.like(!StringUtils.isEmpty(name),Employee::getName,name);
        /*添加排序条件*/
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        /*执行查询*/
        employeeService.page(pageInfo,queryWrapper);/*查到的数据会直接封装到pageInfo*/

        log.info(pageInfo.toString());
        return R.success(pageInfo);
    }


    @GetMapping("/{id}")/*带问号的是路径变量，需要用/花括号*/
    public R<Employee> getById(@PathVariable Long id){
        log.info("根据Id查员工");
        final Employee employee = employeeService.getById(id);
        log.info("employee={}",employee);
        if(employee!=null){
            return R.success(employee);

        }
        return R.error("error");

    }
    @PutMapping()/*修改*/
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
        /*拿到请求体
        * @RequestBody请求体
        *另外可以封装对象@RequestBody Employee employee是高级用法
        * @requestBody接收的是前端传过来的json字符串
        * */
        log.info(employee.toString());
        Long empId=(Long)request.getSession().getAttribute("employee");
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(empId);
        final long id = Thread.currentThread().getId();
        final String name = Thread.currentThread().getName();
        BaseContext.setCurrentId(empId);
        log.info("已将从session中获取的id放入线程");
        /*
         * 获取当前线程的id
         * */
        log.info("当前线程id={},name={}",id,name);
        employeeService.updateById(employee);
        return R.success("员工信息修改成功");

    }


}
