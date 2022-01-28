package ets.service;

import ets.pojo.Menu;
import ets.vo.PageResult;

import java.util.List;

public interface MenuService {

    List<Menu> getAllMenu();
    List<Menu> getRightMenu();
//    分页查询菜单
    PageResult getAllMenuByPage(PageResult pageResult);
//    添加菜单
    int addMenu(Menu menu);
//    删除菜单
    int deleteMenuById(Menu menu);
//    更新菜单信息
    int updateMenuById(Menu menu);
//    根据id查询菜单
    Menu selectMenuById(Integer id);
//    批量删除菜单
    int deleteMenus(List<Menu> list);
//    更新菜单状态
    int updateMenuStatus(Menu menu);
//    获取父级菜单级别
    int selectMenuParentLevel(Menu menu);
    //获取启用菜单
    List<Menu> getTrueMenus();
    //首页菜单查询
    List<Menu> getRoleMenuList(String role);
}
