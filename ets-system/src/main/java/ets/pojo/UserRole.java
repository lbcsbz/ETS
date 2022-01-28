package ets.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_role")
public class UserRole extends BasePojo implements Serializable {
    private static final long serialVersionUID = 7662129469560408669L;
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private Integer roleId;
    private Integer operateUserId;
}
