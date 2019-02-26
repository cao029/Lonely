package com.leyou.config;

import com.leyou.prop.CorsProp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class GlobalCorsConfig {

    @Bean
    public CorsFilter corsFilter(CorsProp corsProp){
//        添加cors配置信息
        CorsConfiguration config = new CorsConfiguration();
        for (String s : corsProp.getAllowedOrigins()) {
            config.addAllowedOrigin(s);
        }
//        是否发送Cookie信息
        config.setAllowCredentials(corsProp.getAllowCredentials());
//        允许的请求方式
//        config.addAllowedMethod("OPTIONS");
//        config.addAllowedMethod("HEAD");
//        config.addAllowedMethod("GET");
//        config.addAllowedMethod("PUT");
//        config.addAllowedMethod("POST");
//        config.addAllowedMethod("DELETE");
//        config.addAllowedMethod("PATCH");
        for (String s : corsProp.getAllowedMethods()) {
            config.addAllowedMethod(s);
        }
//        允许头部的信息
        for (String s : corsProp.getAllowedHeaders()) {
            config.addAllowedHeader(s);
        }
        //2.添加映射路径，我们拦截一切请求
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration(corsProp.getFilterPath(), config);

        //3.返回新的CorsFilter.
        return new CorsFilter(configSource);

    }


//    @Bean
//    public CorsFilter corsFilter(){
////        添加cors配置信息
//        CorsConfiguration config = new CorsConfiguration();
//
//        config.addAllowedOrigin("http://manage.leyou.com");
//        config.addAllowedOrigin("http://api.leyou.com");
////        是否发送Cookie信息
//        config.setAllowCredentials(true);
////        允许的请求方式
//        config.addAllowedMethod("OPTIONS");
//        config.addAllowedMethod("HEAD");
//        config.addAllowedMethod("GET");
//        config.addAllowedMethod("PUT");
//        config.addAllowedMethod("POST");
//        config.addAllowedMethod("DELETE");
//        config.addAllowedMethod("PATCH");
//
////        允许头部的信息
//        config.addAllowedHeader("*");
//
//        //2.添加映射路径，我们拦截一切请求
//        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
//        configSource.registerCorsConfiguration("/**", config);
//
//        //3.返回新的CorsFilter.
//        return new CorsFilter(configSource);
//
//    }

}
