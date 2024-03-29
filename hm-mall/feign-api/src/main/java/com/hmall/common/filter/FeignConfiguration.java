package com.hmall.common.filter;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * feign 配置文件
 * 将请求头中的参数，全部作为 feign 请求头参数传递
 * @author: linjinp
 * @create: 2020-06-28 09:54
 **/
@Configuration
public class FeignConfiguration implements RequestInterceptor {

    public static HttpServletRequest getHttpServletRequest(){
        return  ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        HttpServletRequest request = getHttpServletRequest();
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                String values = request.getHeader(name);
                System.out.println(name+"="+values);
                requestTemplate.header(name, values);
            }
        }
    }
}
