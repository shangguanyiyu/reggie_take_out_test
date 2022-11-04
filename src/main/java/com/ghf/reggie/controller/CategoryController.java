package com.ghf.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ghf.reggie.common.BaseContext;
import com.ghf.reggie.common.R;
import com.ghf.reggie.entity.Category;
import com.ghf.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    public CategoryService categoryService;


    /*
    * 是JSON的形式传来的，加RequestBody
    * */
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Category category){

        Long empId=(Long)request.getSession().getAttribute("employee");
        BaseContext.setCurrentId(empId);
        log.info("已将从session中获取的id放入线程");

        log.info("category={}",category);
        categoryService.save(category);

        return R.success("新增分类成功");

    }
    /*
    * 分页
    * */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize){
        log.info("page={},PageSize={}",page,pageSize);
        final Page pageInfo = new Page(page, pageSize);


        /*构造条件构造器*/
        final LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        /*添加排序构造器*/
        categoryLambdaQueryWrapper.like(Category::getSort,1);/*查种类 1*/
        categoryLambdaQueryWrapper.orderByDesc(Category::getUpdateTime);

        categoryService.page(pageInfo,categoryLambdaQueryWrapper);/*将查询到的数据回写到pageInfo*/
        log.info(pageInfo.toString());
        return R.success(pageInfo);
    }

    /*通过url地址传过来的，所以参数ids不用加注解
    *因为是Rest风格，DeleteMapping也不用加参数
    *
    * */
//    @DeleteMapping("/{ids}")
    @DeleteMapping
    public R<String>delete(Long ids){
        log.info("id={}",ids);
//        categoryService.removeById(ids);
        categoryService.remove(ids);
        return R.success("分类信息删除成功");

    }

    /*
    * 根据id修改分类信息;传过来的信息Category有id
    * */
    @PutMapping
    public R<String> update(@RequestBody Category category){
        categoryService.updateById(category);
        return R.success("修改成功");

    }
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        /*将参数封装成实体category，通用性更强*/

        /*条件构造器*/
        final LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
//        条件，类型，要查的值
        categoryLambdaQueryWrapper.eq(category.getType()!=null,Category::getType,category.getType());
//      条件排序条件:相同字段getSort情况下，用getUpdateTime进行排序
        categoryLambdaQueryWrapper.orderByDesc(Category::getSort).orderByDesc(Category::getUpdateTime);

        final List<Category> list = categoryService.list(categoryLambdaQueryWrapper);
        return R.success(list);
    }


}
