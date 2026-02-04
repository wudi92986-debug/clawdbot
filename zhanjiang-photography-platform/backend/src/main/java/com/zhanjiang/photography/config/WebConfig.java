package com.zhanjiang.photography.config;

import com.zhanjiang.photography.security.JwtInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

/**
 * Web配置
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    
    private final JwtInterceptor jwtInterceptor;
    
    /**
     * 不需要登录的接口
     */
    private static final List<String> EXCLUDE_PATHS = Arrays.asList(
            // 登录相关
            "/user/login/**",
            "/user/token/refresh",
            "/admin/login",
            // 公开接口
            "/photographers",
            "/photographers/*",
            "/photographers/*/works",
            "/photographers/*/packages",
            "/photographers/*/schedules",
            "/photographers/*/reviews",
            "/posts",
            "/posts/*",
            "/topics",
            "/topics/*",
            "/base/**",
            // Swagger文档
            "/doc.html",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/webjars/**",
            "/favicon.ico"
    );
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(EXCLUDE_PATHS);
    }
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
