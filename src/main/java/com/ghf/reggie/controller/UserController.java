package com.ghf.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ghf.reggie.common.R;
import com.ghf.reggie.entity.User;
import com.ghf.reggie.service.UserService;
import com.ghf.reggie.utils.SMSUtils;
import com.ghf.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        final String phone = user.getPhone();
        if(!StringUtils.isEmpty(phone)){
            final String code = ValidateCodeUtils.generateValidateCode(4).toString();
            SMSUtils.sendMessage("羿星阁小程序","",phone,code);
            log.info("code={}",code);

            session.setAttribute(phone,code);
            return R.success("验证码发送成功");
        }



        return null;
    }


    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {
        /*
        * 这里有用户输入的验证码在Map中
        *
        * */
        log.info(map.toString());
        final String phone = map.get("phone").toString();
        final Object code = map.get("code");

        final Object attribute = session.getAttribute(phone);
        if(attribute!=null && attribute.equals(code)){
            //查这个新的手机号是否在用户表中，若是不在，，自动注册
            final LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
            //条件查询器
            userLambdaQueryWrapper.eq(User::getPhone,phone);
            //查询
            User user = userService.getOne(userLambdaQueryWrapper);
            if(user==null){
                user = new User();
                user.setPhone(phone);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());
            return R.success(user);

        }



        return R.error("登录失败");
    }


}
