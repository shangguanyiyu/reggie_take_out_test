package com.ghf.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ghf.reggie.common.BaseContext;
import com.ghf.reggie.common.R;
import com.ghf.reggie.entity.ShoppingCart;
import com.ghf.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;


    @PostMapping("/add")
    public R<ShoppingCart> add (@RequestBody ShoppingCart shoppingCart, HttpServletRequest request){

        log.info("购物车数据={}",shoppingCart.toString());

        //1.设置用户id
        Long currentId = BaseContext.getCurrentId();
//        Long currentId=new Long(1);

        shoppingCart.setUserId(currentId);

        //2、当前菜品或套餐是否在购物车中，存在则加1，不存在则1
        final Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId,currentId);
        if(dishId != null){//菜品
            shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getDishId,dishId);


        }else {//套餐

            shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());

        }
        ShoppingCart one = shoppingCartService.getOne(shoppingCartLambdaQueryWrapper);
        if(one !=null){
            log.info("已经存在，更新操作");
            final Integer number = one.getNumber();
            one.setNumber(number+1);
            shoppingCartService.updateById(one);

        }else {
            shoppingCart.setNumber(1);
            log.info("执行插入操作");
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            one = shoppingCart;
        }

        return R.success(one);
    }
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(HttpSession httpSession){
        //根据userId
//        final Object user =(Long)httpSession.getAttribute("user");



        final LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        shoppingCartLambdaQueryWrapper.orderByAsc(ShoppingCart::getCreateTime);
        final List<ShoppingCart> list = shoppingCartService.list(shoppingCartLambdaQueryWrapper);
        return R.success(list);


    }

    /*
    * 清空购物车
    * */
    @DeleteMapping("/clean")
    public R<String> clean(){
        final LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        shoppingCartService.remove(shoppingCartLambdaQueryWrapper);

        return R.success("成功删除");

    }
}
