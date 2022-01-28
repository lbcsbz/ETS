package ets.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

//MP自动更新时间配置
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        Date date=new java.sql.Date(System.currentTimeMillis());
        this.setFieldValByName("created",date,metaObject);
        this.setFieldValByName("updated",date,metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Date date=new java.sql.Date(System.currentTimeMillis());
        this.setFieldValByName("updated",date,metaObject);
    }
}
