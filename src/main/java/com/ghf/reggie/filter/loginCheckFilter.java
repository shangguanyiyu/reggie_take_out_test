package com.ghf.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.ghf.reggie.common.BaseContext;
import com.ghf.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
* 拦截器防止直输网址进入（用户是否已完成登录）
* */
@Slf4j
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")/*拦截所有请求*/
public class loginCheckFilter implements Filter {
    public static final AntPathMatcher PATH_MATCHER= new AntPathMatcher();/*Spring 提供的路径匹配器*/
    @Override/*实现过滤*/
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request =(HttpServletRequest) servletRequest;/*拿到请求*/
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        // 获取本次请求的URL
        final String requestURI = request.getRequestURI();
        log.info("拦截请求:{}",request.getRequestURI());


        // 判断本次请求是否需要处理如果不需要处理，则直接放行；/backend/index：不放行
        String[] url = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",/*文件上传的*/
                "/user/sendMsg",/*发短信*/
                "/user/login" //移动端


        };
        final boolean check = check(requestURI, url);
        if(check){
            log.info("放行请求{}",requestURI);
            filterChain.doFilter(request,response);
            return;
        }
        // -1，判断登录状态，如果已登录，则直接放行(哪里获取：request.getSession().getAttribute("employee")) 登录的时候放了.getAttribute("employee")
        Long employeeId =(Long)request.getSession().getAttribute("employee");
        if(employeeId!=null){
            log.info("用户已登录 放行，用户id为{}",request
            .getSession().getAttribute("employee"));
//            final long id = Thread.currentThread().getId();
//            final String name = Thread.currentThread().getName();
            /*
             * 获取当前线程的id
             * */
//            log.info("当前线程id={},name={}",id,name);
            BaseContext.setCurrentId(employeeId);

            filterChain.doFilter(request,response);



            return;
        }

        // -2,移动端登录：判断登录状态，如果已登录，则直接放行(哪里获取：request.getSession().getAttribute("employee")) 登录的时候放了.getAttribute("employee")

        //4-2、判断登录状态，如果已登录，则直接放行
        if(request.getSession().getAttribute("user") != null){
            log.info("用户已登录，用户id为：{}",request.getSession().getAttribute("user"));

            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);

            filterChain.doFilter(request,response);
            return;
        }

        // 如果未登录则返回未登录结果 通过输出
        log.info("用户未登录");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }
    public boolean check(String requestURL,String[] urls){
        for (String url : urls) {
            final boolean match = PATH_MATCHER.match(url, requestURL);
            if(match){
                return true;/*匹配上了*/
            }
        }
        return false;
    }
}
