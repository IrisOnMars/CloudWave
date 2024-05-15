package com.liuyun.config;

import com.liuyun.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @PackageName com.liuyun.config
 * @Description TODO
 * @Author Ren
 * @Date 2024/5/3 16:26
 * @Version 1.0
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        System.out.println("开始注册拦截器");
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/myfiles/**","http://localhost:8080/getShare","/file/upload","/avatar/**","/login","/register/**"); // 应用到所有路径，根据需要调整

    }
}
