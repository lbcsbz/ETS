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
public class Sites implements Serializable {

    private static final long serialVersionUID = 5200562608485704794L;
    private Integer id;
    private String name;
    private String type;
    private String site_id;
    private Integer level;
    private String  parent_id;
    @DateTimeFormat(pattern = "yyyy-MM-dd hh-mm-ss")
    private Date created;
    @DateTimeFormat(pattern = "yyyy-MM-dd hh-mm-ss")
    private Date updated;
}
