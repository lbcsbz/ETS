package ets.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
@Data
@Accessors(chain = true)
public class PageResult implements Serializable {
    private static final long serialVersionUID = 2910177443462099086L;
    private Object queryIn;
    private Integer pageIndex;
    private Integer pageSize;
    private Long total;
    private Object list;

}
