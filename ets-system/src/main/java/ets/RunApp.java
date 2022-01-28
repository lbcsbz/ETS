package ets;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
@MapperScan("ets.mapper")
public class RunApp {
    public static void main(String[] args) {
        SpringApplication.run(RunApp.class,args);
    }
}
