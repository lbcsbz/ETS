package ets.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ets.pojo.Menu;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MenuMapper extends BaseMapper<Menu> {
    @Select("select m.id id,m.name name,m.path path,m.level level,m.parent_id parent_id,m.status status,m.icon icon " +
            "from roles r left join role_menu rm " +
            "on r.id = rm.role_id left join menu m " +
            "on rm.menu_id = m.id " +
            "where r.name = #{role} and " +
            "r.status = 1 and " +
            "1=1" )
    List<Menu> selectMenuByRole(String role);
}
