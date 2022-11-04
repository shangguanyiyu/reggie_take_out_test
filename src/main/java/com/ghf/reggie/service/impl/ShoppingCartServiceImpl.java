package com.ghf.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ghf.reggie.entity.ShoppingCart;
import com.ghf.reggie.mapper.ShoppingCartMapper;
import com.ghf.reggie.service.ShoppingCartService;
import org.springframework.stereotype.Service;

/*
* @Service
* extend ServiceImpl<M,entity>
impl<Ser>
*
* */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart>
    implements ShoppingCartService {
}
