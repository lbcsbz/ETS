package ets.service;

import ets.pojo.User;
import ets.vo.PageResult;

import java.util.List;
import java.util.Map;

public interface UserService {
    Map<String,String> login(User user);
    //分页获取员工
    PageResult getAllEmps(PageResult pageResult);
    //    多选删除
    void deleteEmps(List<User> list);
    //    更新账号状态
    void updateEmpStatus(User user);
    //    根据Id删除员工
    void deleteEmpById(User user);
    //    员工账号密码重置
    void resetPwdById(User user);
    //    新增员工
    void addEmp(User user);
    //    根据ID查找员工
    User selectEmpById(Integer id);
    //    员工信息编辑
    void updateEmpById(User user);
    //用户登录
    Map<String, Object> checkUser(User user);

    int resetPwdByCode(String resetPwdForm);
}
