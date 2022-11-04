package com.ghf.reggie.controller;

import com.ghf.reggie.common.R;
import com.ghf.reggie.entity.Orders;
import com.ghf.reggie.service.OrderDetailService;
import com.ghf.reggie.service.OrderService;
import com.tencentcloudapi.tse.v20201207.models.SREInstance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/order")
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        orderService.submit(orders);
        return R.success("下单成功");
    }
}
