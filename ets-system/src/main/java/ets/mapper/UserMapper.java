package ets.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ets.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserMapper extends BaseMapper<User> {
    Long countTotal(Map<String, Object> map);

    List<User> selectEmpsByPage(Map<String, Object> map);

    User selectEmpById(Integer id);

    User selectEmpLogin(User user);

    User selectEmpByPwd(User user);
}
