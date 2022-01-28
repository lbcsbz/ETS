package ets.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ets.pojo.RoleMenuId;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface RightMapper extends BaseMapper {
    @Select("select rm.menu_id,m.parent_id from role_menu rm right join menu m on rm.menu_id=m.id where rm.role_id=#{id}")
    List<RoleMenuId> selectRightsById(Integer id);


}
