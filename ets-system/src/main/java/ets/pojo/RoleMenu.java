package ets.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("role_menu")
public class RoleMenu extends BasePojo implements Serializable {
    private static final long serialVersionUID = -3977531388626455765L;
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer roleId;
    private Integer menuId;
    private Integer opreateUserId;
}
