package ets.pojo;

import lombok.Data;

import javax.sql.rowset.serial.SerialClob;
import java.io.Serializable;

@Data
public class RoleMenuId implements Serializable {
    private static final long serialVersionUID = 7425081645867881718L;
    private Integer menuId;
    private Integer parentId;
}
