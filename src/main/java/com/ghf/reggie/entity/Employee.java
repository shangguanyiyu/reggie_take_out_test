package com.ghf.reggie.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/*员工实体类*/
@Data
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String name;

    private String password;

    private String phone;

    private String sex;

    private String idNumber;

    private Integer status;

    /*由MP提供的注解，是公共部分*/
    @TableField(fill = FieldFill.INSERT)/*插入时填充字段*/
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)/*插入和更新填充字段*/
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)/*插入时填充字段*/
    private Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)/*插入和更新填充字段*/
    private Long updateUser;

}
