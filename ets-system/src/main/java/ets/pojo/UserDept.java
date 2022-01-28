package ets.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Primary;

import java.io.PipedReader;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_dept")
public class UserDept extends BasePojo implements Serializable {
    private static final long serialVersionUID = 2979059970495891345L;
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private Integer deptId;
    private Integer operateUserId;
}
