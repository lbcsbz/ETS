package ets.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsFilerConfig {
    @Bean
    public FilterRegistrationBean filterRegistrationBean(){
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();//构建url方式跨域配置对象
        CorsConfiguration configuration=new CorsConfiguration();//设置规则配置器
        configuration.addAllowedHeader("*");//配置请求头
        configuration.addAllowedMethod("*");//配置请求方法
        configuration.addAllowedOrigin("*");//配置请求ip和port
//        configuration.setAllowCredentials(true);//配置带cookie跨域
        configSource.registerCorsConfiguration("/**",configuration);//将规则配置注册到url
        FilterRegistrationBean filterRegistrationBean=new FilterRegistrationBean(
                new CorsFilter(configSource));//注册过滤器
        filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);//设置过滤器优先级
        return filterRegistrationBean;
    }
}
