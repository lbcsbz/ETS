package ets.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("menu")
public class Menu extends BasePojo implements Serializable  {
    private static final long serialVersionUID = 3307053807974527473L;
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private Integer parentId;
    private String path;
    private boolean status;
    private Integer level;
    private String icon;
    @TableField(exist = false)
    private List<Menu> children; //不是表格固有属性
    @TableField(exist = false)
    private String parentName; //不是表格固有属性
}
