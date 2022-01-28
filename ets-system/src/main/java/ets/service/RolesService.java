package ets.service;

import ets.pojo.Roles;
import ets.vo.PageResult;

import java.util.List;

public interface RolesService {
    PageResult getAllRoles(PageResult pageResult);

    void addRole(Roles roles);

    void updataRoleStatus(Roles roles);

    void deleteRoleById(Roles roles);

    Roles selectRoleById(Integer id);

    void updateRoleById(Roles roles);

    PageResult getTheRoleList(PageResult pageResult);

    void deleteRoles(List<Roles> list);

    List<Roles> getAllroles1();
}
