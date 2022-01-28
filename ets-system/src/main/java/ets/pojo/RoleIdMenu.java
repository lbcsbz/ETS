package ets.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RoleIdMenu implements Serializable {

    private static final long serialVersionUID = -7832403274170050032L;
    private Integer id;
    private List<Integer[]> multipleRight;
}
