package com.ghf.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ghf.reggie.common.BaseContext;
import com.ghf.reggie.common.R;
import com.ghf.reggie.dto.DishDto;
import com.ghf.reggie.entity.Category;
import com.ghf.reggie.entity.Dish;
import com.ghf.reggie.entity.DishFlavor;
import com.ghf.reggie.service.CategoryService;
import com.ghf.reggie.service.DishFlavorService;
import com.ghf.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import sun.rmi.runtime.Log;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {


    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;//categoryId查名字

    /*
    * 新增数据
    *
    * */
    @PostMapping
    public R<String > save(@RequestBody DishDto dishDto, HttpServletRequest request){
        /*DishDto dishDto:接收前端提交过来的数据*/
        Long empId=(Long)request.getSession().getAttribute("employee");
        BaseContext.setCurrentId(empId);
        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");

    }
    /*
    * 分页
    * */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){

        log.info("page={},pageSize={},name={}",page,pageSize,name);
//1、分页构造器
        final Page<Dish> pageInfo = new Page(page, pageSize);
         Page<DishDto> dishDtoPage = new Page<>();/*dishDto有前端需要的categoryName,并且继承的Dish，有dish的属性*/
//2、LambdaQueryWrapper条件构造器
        final LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
//3、查询条件
        dishLambdaQueryWrapper.like(!StringUtils.isEmpty(name),Dish::getName,name);
        // 4、排序条件
        dishLambdaQueryWrapper.orderByDesc(Dish::getUpdateTime);
        //5、执行查询
        dishService.page(pageInfo,dishLambdaQueryWrapper);

        /*6、拷贝到dishDto*/
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");//records不拷贝

        final List<Dish> records = pageInfo.getRecords();

        List<DishDto> list=records.stream().map(
                (item)->{
                    final DishDto dishDto = new DishDto();
                    BeanUtils.copyProperties(item,dishDto);
                    final Long categoryId = item.getCategoryId();
                    final Category  category= categoryService.getById(categoryId);
                    if(category!=null){
                        final String name1 = category.getName();
                        dishDto.setCategoryName(name1);
                    }

                    return dishDto;
                }
        ).collect(Collectors.toList()); /*将所有Dto对象收集为集合*/

        dishDtoPage.setRecords(list);
        return R.success(dishDtoPage);

    }
    /*Request URL: http:/ / localhost:08080/dish/1412600741490610177
     * 不是rest风格的数据，id是在url
    * 所以要"/{id}"、以及参数@PathVariable Long id
    *
    * */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){
        final DishDto byIdWithFlavor = dishService.getByIdWithFlavor(id);
        return R.success(byIdWithFlavor);
    }

    @PutMapping
    public R<String > update(@RequestBody DishDto dishDto,HttpServletRequest request){
        /*DishDto dishDto:接收前端提交过来的数据*/
        Long empId=(Long)request.getSession().getAttribute("employee");
        BaseContext.setCurrentId(empId);
        log.info(dishDto.toString());
        dishService.updateWithFlavor(dishDto);
        return R.success("新增菜品成功");

    }

//    @GetMapping("/list")
//    public R<List<Dish>> list(Dish dish){
//        /*
//        * Long categoryId 参数也可以
//        * 但是为了通用性，参数可以是Dish dish,
//        * */
//        //1、构造查询条件
//        final LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
//
//        dishLambdaQueryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
//        dishLambdaQueryWrapper.eq(Dish::getStatus,1);//查起售的
//        //2、排序条件
//        dishLambdaQueryWrapper.orderByAsc(Dish::getSort).orderByAsc(Dish::getUpdateTime);
//        //3、
//        final List<Dish> list = dishService.list(dishLambdaQueryWrapper);
//        return R.success(list);
//
//    }
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        /*
         * Long categoryId 参数也可以
         * 但是为了通用性，参数可以是Dish dish,
         * */
        //1、构造查询条件
        final LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();

        dishLambdaQueryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
        dishLambdaQueryWrapper.eq(Dish::getStatus,1);//查起售的
        //2、排序条件
        dishLambdaQueryWrapper.orderByAsc(Dish::getSort).orderByAsc(Dish::getUpdateTime);
        //3、
    //    final List<DishDto> list = null;
        List<Dish> dishes = dishService.list(dishLambdaQueryWrapper);

        final List<DishDto> collect = dishes.stream().map(
                (item) -> {
                    final DishDto dishDto = new DishDto();
                    BeanUtils.copyProperties(item, dishDto);
                    final Long categoryId = item.getCategoryId();
                    Category category = categoryService.getById(categoryId);
                    if (category != null) {
                        final String name = category.getName();
                        dishDto.setCategoryName(name);

                    }
                    final Long id = item.getId();//菜品id
                    final LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,id);
                    final List<DishFlavor> dishFlavorList = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
                    dishDto.setFlavors(dishFlavorList);

                    return dishDto;

                }
        ).collect(Collectors.toList());



        return R.success(collect);

}


}
