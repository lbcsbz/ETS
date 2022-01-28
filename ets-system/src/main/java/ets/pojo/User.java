package ets.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.context.annotation.Bean;
import org.springframework.format.annotation.DateTimeFormat;
import sun.security.util.Password;

import javax.lang.model.element.NestingKind;
import java.io.Serializable;
import java.sql.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("user")
public class User extends BasePojo implements Serializable {
    private static final long serialVersionUID = 1844391276892101069L;
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String code;
    private String Password;
    private String gender;
    private String email;
    private String residentId;
    private String phone;
    private boolean status;
    private String picture;
    @DateTimeFormat(pattern = "yyyy-MM-dd hh-mm-ss")
    private Date lastLoginDate;
    @TableField(exist = false)
    private Integer roleId;
    @TableField(exist = false)
    private String roleName;
    @TableField(exist = false)
    private Integer deptId;
    @TableField(exist = false)
    private String deptName;
    @TableField(exist = false)
    private Integer age;

}
