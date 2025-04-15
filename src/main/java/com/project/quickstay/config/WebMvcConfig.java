package com.project.quickstay.config;

import com.project.quickstay.common.interceptor.LoginInterceptor;
import com.project.quickstay.common.UserArgumentResolver;
import com.project.quickstay.common.interceptor.NewUserInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .excludePathPatterns("/css/**", "/images/**", "/js/**", "/login", "/oauth/kakao", "/callback/kakao", "/home", "/place/search", "/place/**/info", "/api/**", "/newUser")
                .order(1);

        registry.addInterceptor(new NewUserInterceptor())
                .excludePathPatterns("/login", "/oauth/kakao", "/callback/kakao", "/newUser", "/selectUserType", "/css/**", "/images/**", "/js/**")
                .order(2);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new UserArgumentResolver());
    }
}
