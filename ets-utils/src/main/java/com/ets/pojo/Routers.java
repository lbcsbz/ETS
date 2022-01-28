package com.ets.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.sql.Date;
//import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Routers implements Serializable {
    private static final long serialVersionUID = -6602478378411701728L;
    private  Integer id;
    private String name;
    private String router_id;
    private String origin_site;
    private String origin_site_id;
    private String target_site;
    private String target_site_id;
    private String plan_origin_time;
    private String plan_target_time;
    @DateTimeFormat(pattern = "yyyy-MM-dd hh-mm-ss")
    private Date created;
    @DateTimeFormat(pattern = "yyyy-MM-dd hh-mm-ss")
    private Date updated;
}
