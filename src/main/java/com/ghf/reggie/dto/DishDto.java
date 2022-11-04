package com.ghf.reggie.dto;

import com.ghf.reggie.entity.Dish;
import com.ghf.reggie.entity.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {
    /*
    * 利用父类的属性
    * */

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
