package com.ghf.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ghf.reggie.common.BaseContext;
import com.ghf.reggie.common.R;
import com.ghf.reggie.dto.SetmealDto;
import com.ghf.reggie.entity.Category;
import com.ghf.reggie.entity.Setmeal;
import com.ghf.reggie.entity.SetmealDish;
import com.ghf.reggie.service.CategoryService;
import com.ghf.reggie.service.SetmealDishService;
import com.ghf.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/*
*1、RestController
* 2、RequestMapping()
* */
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;

    /*
    * 参数是json格式的，所以要用@RequestBody
    * */
    @PostMapping
    public R<String > save(@RequestBody SetmealDto setmealDto, HttpServletRequest request){
        log.info(setmealDto.toString());
        Long employeeId =(Long)request.getSession().getAttribute("employee");
        BaseContext.setCurrentId(employeeId);

        setmealService.saveWithDish(setmealDto);
        return R.success("新增成功");

    }

    /*
    * 分页信息查询
    * */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        log.info("page={},pageSize={},name={}",page,pageSize,name);
//分页构造器
        final Page<Setmeal> setmealPageinfo = new Page<>(page, pageSize);

        //条件构造器
        final LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();

        //查询条件
        setmealLambdaQueryWrapper.like(!StringUtils.isEmpty(name),Setmeal::getName,name);

        //排序条件
        setmealLambdaQueryWrapper.orderByAsc(Setmeal::getUpdateTime);

        //执行
        setmealService.page(setmealPageinfo,setmealLambdaQueryWrapper);

        //拷贝到SetmealDto,拿到categoryName
        final Page<SetmealDto> setmealDtoPageInfo = new Page<>();
        BeanUtils.copyProperties(setmealPageinfo,setmealDtoPageInfo);

        //拿到记录
        final List<Setmeal> records = setmealPageinfo.getRecords();
        //遍历记录的每个实体，查套餐名字
        final List<SetmealDto> list = records.stream().map(
                (item) -> {
                    final SetmealDto setmealDto = new SetmealDto();
                    BeanUtils.copyProperties(item, setmealDto);
                    final Long categoryId = item.getCategoryId();
                    //通过id查名字
                    final Category category = categoryService.getById(categoryId);

                    if (category != null) {
                        setmealDto.setCategoryName(category.getName());

                    }
                    return setmealDto;
                }
        ).collect(Collectors.toList());
        setmealDtoPageInfo.setRecords(list);

        return R.success(setmealDtoPageInfo);
    }

    /*
    * 进来的参数是一个或者多个
    * 在控制层 Controller 中，有时候写接口会带@RequestParam，有时候也可以不带。

如果带@RequestParam，它一般有三个值，Value，required，defaultValue。

Value： 代表你传参的参数名称，例如  @RequestParam(value="id")  Long userId。在这里，你传userId是没有用的，你必须传id，因为value指定了你必须传哪个参数名。当然，你也可以省略value不写，直接写参数名。

required：默认是true，也就是说，你不显示定义required=false，那么你就必须传参

defaultValue：表示默认值的意思，如果你不传参，也可以通过，因为defaultValue起到了作用。

    * */
    @DeleteMapping
    public R<String > delete(@RequestParam List<Long>  ids){
    log.info(ids.toString());
        setmealService.removeWithDish(ids);
    return R.success("删除成功");
    }

//    @GetMapping("/list")
//    public R<List<Setmeal>> list(Setmeal setmeal){
//        /*将参数封装成实体category，通用性更强*/
//
//        /*条件构造器*/
//        final LambdaQueryWrapper<Setmeal> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
////        条件，类型，要查的值
//        categoryLambdaQueryWrapper.eq(setmeal.getStatus()!=0,Setmeal::getCategoryId,setmeal.getCategoryId());
////      条件排序条件:相同字段getSort情况下，用getUpdateTime进行排序
//        categoryLambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);
//
//        final List<Setmeal> list = setmealService.list(categoryLambdaQueryWrapper);
//        return R.success(list);
//    }
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        /*
       不是json格式的
        * */
        final LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(setmeal.getCategoryId()!=null,Setmeal::getCategoryId,setmeal.getCategoryId() );
        setmealLambdaQueryWrapper.eq(setmeal.getStatus()!=null,Setmeal::getStatus,setmeal.getStatus() );
        setmealLambdaQueryWrapper.orderByAsc(Setmeal::getUpdateTime);
        List<Setmeal> list = setmealService.list(setmealLambdaQueryWrapper);

        return R.success(list);


    }

}
