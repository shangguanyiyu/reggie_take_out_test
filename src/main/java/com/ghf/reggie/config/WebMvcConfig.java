package com.ghf.reggie.config;

import com.ghf.reggie.common.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.cbor.MappingJackson2CborHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@Slf4j
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    /*
    * 设置静态资源映射
    * */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("开启静态资源映射");
        registry.addResourceHandler("/backend/**")/*获取浏览器的请求路径*/
                .addResourceLocations("classpath:/backend/");/*classpath就是resources;映射到backend*/

        registry.addResourceHandler("/front/**")/*获取浏览器的请求路径*/
                .addResourceLocations("classpath:/front/");/*classpath就是resources;映射到backend*/

    }
    /*扩展mvc的消息转换器*/
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        /*创建消息转换器对象
        * 将Controller返回的结果R.success() 变成Json-->输出流的方式转换成页面
        * */
        final MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
/*设置对象转换器，底层使用Jackson将Java对象转换为json*/
        messageConverter.setObjectMapper(new JacksonObjectMapper());
//        super.extendMessageConverters(converters);
        /*将上面的消息转换器对象追加到MVC框架的转换器集合中,并放到最前面，优先使用*/
        converters.add(0,messageConverter);

    }
}
