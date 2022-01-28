package ets.service;

import ets.pojo.Menu;
import ets.pojo.RoleIdMenu;

import java.util.ArrayList;
import java.util.List;

public interface RightService {
    List<Integer[]> selectRightsById(Integer id);

    List<Menu> selectAllRights();

    void getRoleRight(RoleIdMenu roleIdMenu);
}
