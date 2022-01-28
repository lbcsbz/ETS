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
@TableName("departments")
public class Dept extends BasePojo implements Serializable {
    private static final long serialVersionUID = -8455421361984434011L;
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private Integer level;
    private Integer parentId;
    private boolean status;
    @TableField(exist = false)
    private String parentDept; //不是表格固有属性
}
