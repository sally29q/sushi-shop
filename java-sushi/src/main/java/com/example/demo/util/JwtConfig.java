//package com.example.demo.util;
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class JwtConfig implements WebMvcConfigurer {
//    @Autowired
//    private JsonWebTokenInterceptor jsonWebTokenInterceptor;
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(jsonWebTokenInterceptor)
//            .addPathPatterns("/ingredients/**")
//            .addPathPatterns("/orders/**")
//            .addPathPatterns("/order-details/**")
//            .addPathPatterns("/products/**")
//            .addPathPatterns("/product-ingredients/**")
//            .addPathPatterns("/admin/all")
//            .addPathPatterns("/admin/edit")
//            .addPathPatterns("/admin/delete");
//    }
//}