package ets.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("roles")
public class Roles extends BasePojo implements Serializable {
    private static final long serialVersionUID = 6547373642736627041L;
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private Integer level;
    private boolean status;
//    @TableField(exist = false)
//    private List<Menu> roleMenu; //不是表格固有属性
}