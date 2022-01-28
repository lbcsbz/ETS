package ets.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ets.mapper.UserRoleMapper;
import ets.pojo.UserRole;
import ets.vo.SBZUtils;
import ets.mapper.RoleMenuMapper;
import ets.mapper.RolesMapper;
import ets.pojo.RoleMenu;
import ets.pojo.Roles;
import ets.service.RolesService;
import ets.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class RolesServiceImpl implements RolesService {
    @Autowired
    private RolesMapper rolesMapper;
    @Autowired
    private RoleMenuMapper roleMenuMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    //分页查询所有角色
    @Override
    public PageResult getAllRoles(PageResult pageResult) {
        Page<Roles> page= new Page<>(pageResult.getPageIndex(),pageResult.getPageSize());
        Page<Roles> page1 = rolesMapper.selectPage(page, null);
        pageResult.setList(page1.getRecords());
        pageResult.setTotal(page1.getTotal());
        return pageResult;
    }
    //分页查询指定角色
    @Override
    public PageResult getTheRoleList(PageResult pageResult) {
        Page<Roles> page = new Page<>(pageResult.getPageIndex(), pageResult.getPageSize());
        QueryWrapper<Roles> queryWrapper = new QueryWrapper<>();
        Map<String,Object> map = SBZUtils.dealWithQuery(pageResult);
        if(map.get("name")!=null)queryWrapper.like("name",map.get("name"));
        if(map.get("status")!=null)queryWrapper.eq("status",Boolean.parseBoolean(map.get("status").toString()));
        queryWrapper.eq("1",1);
        Page<Roles> page1 = rolesMapper.selectPage(page, queryWrapper);
        pageResult.setList(page1.getRecords());
        pageResult.setTotal(page1.getTotal());
        return pageResult;
    }
//    添加角色
    @Override
    @Transactional
    public void addRole(Roles roles) {
        roles.setStatus(false);
        rolesMapper.insert(roles);
    }

//    更改角色状态
    @Override
    @Transactional
    public void updataRoleStatus(Roles roles) {
        rolesMapper.updateById(roles);
    }

//    删除角色
    @Override
    @Transactional
    public void deleteRoleById(Roles roles) {
        rolesMapper.deleteById(roles.getId());
        QueryWrapper<RoleMenu> queryWrapper1 = new QueryWrapper<>();
        QueryWrapper<UserRole> queryWrapper2 = new QueryWrapper<>();
        queryWrapper1.eq("role_id",roles.getId());
        queryWrapper2.eq("role_id",roles.getId());
        roleMenuMapper.delete(queryWrapper1);
        userRoleMapper.delete(queryWrapper2);
    }
    //    查询单个角色
    @Override
    public Roles selectRoleById(Integer id) {
        Roles roles = rolesMapper.selectById(id);
        return roles;
    }

//    修改角色信息
    @Override
    @Transactional
    public void updateRoleById(Roles roles) {
        rolesMapper.updateById(roles);
    }


//    批量删除
    @Override
    @Transactional
    public void deleteRoles(List<Roles> list){
        List<Integer> idList=new ArrayList<>();
        QueryWrapper<RoleMenu> queryWrapper1 = new QueryWrapper<>();
        QueryWrapper<UserRole> queryWrapper2 = new QueryWrapper<>();
        for(Roles roles:list){
            idList.add(roles.getId());
            queryWrapper1.clear();
            queryWrapper2.clear();
            queryWrapper1.eq("role_id",roles.getId());
            queryWrapper2.eq("role_id",roles.getId());
            roleMenuMapper.delete(queryWrapper1);
            userRoleMapper.delete(queryWrapper2);
        }
        rolesMapper.deleteBatchIds(idList);
    }
    //获取角色列表
    @Override
    public List<Roles> getAllroles1() {
        QueryWrapper<Roles> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status",1);
        return rolesMapper.selectList(queryWrapper);
    }
}
