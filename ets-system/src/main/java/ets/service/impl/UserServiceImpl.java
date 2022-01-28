package ets.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import ets.mapper.*;
import ets.pojo.*;
import ets.service.UserService;
import ets.vo.PageResult;
import ets.vo.SBZUtils;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserDeptMapper userDeptMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RolesMapper rolesMapper;
    @Autowired
    private DeptMapper deptMapper;
    String originPwd = DigestUtils.md5DigestAsHex("123456".getBytes(StandardCharsets.UTF_8));
    @Override
    public Map<String,String> login(User user) {
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        QueryWrapper<User> quertWrapper = new QueryWrapper<>(user);
        User user1 = userMapper.selectOne(quertWrapper);
        if(user1==null) return null;
        if(!user1.isStatus()) return null;
        String role = null;
        String  token= UUID.randomUUID().toString().replace("-","");
        Map<String,String> map=new HashMap<>();
        map.put("token",token);
        map.put("role",role);
        return null;
    }
    //分页获取员工
    @Override
    public PageResult getAllEmps(PageResult pageResult) {
        Map<String, Object> map = SBZUtils.dealWithQuery(pageResult);
        Long total = userMapper.countTotal(map);
        List<User> emps = userMapper.selectEmpsByPage(map);
        for (User emp : emps) {
            String residentId = emp.getResidentId();
            emp.setAge(SBZUtils.getAgeByResident(residentId));
        }
        pageResult.setList(emps);
        pageResult.setTotal(total);
        return pageResult;
    }
    //    多选删除
    @Override
    @Transactional
    public void deleteEmps(List<User> list) {
        List<Integer> idList=new ArrayList<>();
        QueryWrapper<UserDept> queryWrapper1 = new QueryWrapper<>();
        QueryWrapper<UserRole> queryWrapper2 = new QueryWrapper<>();
        for(User user:list){
            idList.add(user.getId());
            queryWrapper1.clear();
            queryWrapper2.clear();
            queryWrapper1.eq("user_id",user.getId());
            queryWrapper2.eq("user_id",user.getId());
            userDeptMapper.delete(queryWrapper1);
            userRoleMapper.delete(queryWrapper2);
        }
        userMapper.deleteBatchIds(idList);
    }
    //    更新账号状态
    @Override
    @Transactional
    public void updateEmpStatus(User user) {
        userMapper.updateById(user);
    }
    //    根据Id删除员工
    @Override
    @Transactional
    public void deleteEmpById(User user) {
        QueryWrapper<UserDept> queryWrapper1 = new QueryWrapper<>();
        QueryWrapper<UserRole> queryWrapper2 = new QueryWrapper<>();
        queryWrapper1.eq("user_id",user.getId());
        queryWrapper2.eq("user_id",user.getId());
        userDeptMapper.delete(queryWrapper1);
        userRoleMapper.delete(queryWrapper2);
        userMapper.deleteById(user.getId());
    }
    //    员工账号密码重置
    @Override
    @Transactional
    public void resetPwdById(User user) {
        user.setPassword(originPwd);
        userMapper.updateById(user);
    }
    //    新增员工
    @Override
    @Transactional
    public void addEmp(User user) {
        user.setPassword(originPwd);
        String userCode = SBZUtils.getUserCode();
        user.setCode(userCode);
        user.setLastLoginDate(new Date(System.currentTimeMillis()));
        String deptName = user.getDeptName();
        String roleName = user.getRoleName();
        userMapper.insert(user);
        QueryWrapper<User> queryWrapper3  = new QueryWrapper<>();
        queryWrapper3.eq("code",userCode);
        User user1 = userMapper.selectOne(queryWrapper3);
        QueryWrapper<Dept> queryWrapper1  = new QueryWrapper<>();
        QueryWrapper<Roles> queryWrapper2  = new QueryWrapper<>();
        queryWrapper1.eq("name",deptName);
        queryWrapper2.eq("name",roleName);
        Dept dept = deptMapper.selectOne(queryWrapper1);
        Roles roles = rolesMapper.selectOne(queryWrapper2);
        UserDept userDept = new UserDept();
        UserRole userRole = new UserRole();
        userDept.setUserId(user1.getId());
        userDept.setDeptId(dept.getId());
        userRole.setUserId(user1.getId());
        userRole.setRoleId(roles.getId());
        userRoleMapper.insert(userRole);
        userDeptMapper.insert(userDept);
    }
    //    根据ID查找员工
    @Override
    public User selectEmpById(Integer id) {
        User user = userMapper.selectEmpById(id);
        String residentId = user.getResidentId();
        user.setAge(SBZUtils.getAgeByResident(residentId));
        return user;
    }
    //    员工信息编辑
    @Override
    @Transactional
    public void updateEmpById(User user) {
        userMapper.updateById(user);
        QueryWrapper<UserDept> queryWrapper3 = new QueryWrapper<>();
        QueryWrapper<UserRole> queryWrapper4 = new QueryWrapper<>();
        queryWrapper3.eq("user_id",user.getId());
        queryWrapper4.eq("user_id",user.getId());
        userDeptMapper.delete(queryWrapper3);
        userRoleMapper.delete(queryWrapper4);
        String deptName = user.getDeptName();
        String roleName = user.getRoleName();
        QueryWrapper<Dept> queryWrapper1  = new QueryWrapper<>();
        QueryWrapper<Roles> queryWrapper2  = new QueryWrapper<>();
        queryWrapper1.eq("name",deptName);
        queryWrapper2.eq("name",roleName);
        Dept dept = deptMapper.selectOne(queryWrapper1);
        Roles roles = rolesMapper.selectOne(queryWrapper2);
        UserDept userDept = new UserDept();
        UserRole userRole = new UserRole();
        userDept.setUserId(user.getId());
        userDept.setDeptId(dept.getId());
        userRole.setUserId(user.getId());
        userRole.setRoleId(roles.getId());
        userRoleMapper.insert(userRole);
        userDeptMapper.insert(userDept);
    }
    //用户登录
    @Override
    public Map<String, Object> checkUser(User user) {
        String md5Pwd = DigestUtils.md5DigestAsHex(user.getPassword().getBytes(StandardCharsets.UTF_8));
        Map<String, Object> map = new HashMap<>();
        user.setPassword(md5Pwd);
        User user1 = userMapper.selectEmpLogin(user);
        if(user1==null)return null;
        String token = UUID.randomUUID().toString().replace("-","");
        map.put("token",token);
        map.put("role",user1.getRoleName());
        map.put("code",user1.getCode());
        map.put("name",user1.getName());
        map.put("lastLoginDate",user1.getLastLoginDate());
        map.put("id",user1.getId());
        user1.setLastLoginDate(new Date(System.currentTimeMillis()));
        userMapper.updateById(user1);
        return map;
    }

    @Override
    @Transactional
    public int resetPwdByCode(String resetPwdForm) {
        Map<String, Object> map = SBZUtils.dealWithJson(resetPwdForm);
        Object id = map.get("id");
        Object pwd = map.get("pwd");
        Object newPwd = map.get("newPwd");
        String md5Pwd = DigestUtils.md5DigestAsHex(pwd.toString().getBytes(StandardCharsets.UTF_8));
        User user = new User();
        user.setPassword(md5Pwd);
        user.setId(Integer.valueOf(id.toString()));
        User user1 = userMapper.selectEmpByPwd(user);
        if(user1==null)return 2;
        String md5NewPwd = DigestUtils.md5DigestAsHex(newPwd.toString().getBytes());
        user1.setPassword(md5NewPwd);
        int i = userMapper.updateById(user1);
        if(i!=1)return 0;
        return 1;
    }
}
