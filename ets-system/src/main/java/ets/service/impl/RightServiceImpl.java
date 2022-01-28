package ets.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import ets.mapper.MenuMapper;
import ets.mapper.RightMapper;
import ets.mapper.RoleMenuMapper;
import ets.pojo.Menu;
import ets.pojo.RoleIdMenu;
import ets.pojo.RoleMenu;
import ets.pojo.RoleMenuId;
import ets.service.MenuService;
import ets.service.RightService;
import org.apache.ibatis.javassist.expr.NewArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class RightServiceImpl implements RightService {
    @Autowired
    private RightMapper rightMapper;
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private RoleMenuMapper roleMenuMapper;
    //权限回写
    @Override
    public List<Integer[]> selectRightsById(Integer id) {
        List<RoleMenuId> list = rightMapper.selectRightsById(id);
        if(list==null||list.size()==0)return null;
        Map<Integer,ArrayList<Integer>> map = new ManagedMap<>();
        for (RoleMenuId i: list) {
            if(map.containsKey(i.getParentId())){
                ArrayList<Integer> count = map.get(i.getParentId());
                count.add(i.getMenuId());
                map.put(i.getParentId(),count);
            }else{
                ArrayList<Integer> count = new ArrayList<>();
                count.add(i.getMenuId());
                map.put(i.getParentId(),count);
            }
        }
        List<Integer[]> rights = new ArrayList<>();
        ArrayList<Integer> count = map.get(0);
        Object[] count1 = count.toArray();
        for (int i = 0; i < count1.length; i++) {
            ArrayList<Integer> count2 = map.get(count1[i]);
            if(count2==null||count2.size()==0){
                Integer[] r1 = {Integer.valueOf(count1[i].toString())};
                rights.add(r1);
                continue;
            }
            Object[] count3 = count2.toArray();
            for (int j = 0; j < count3.length; j++) {
                ArrayList<Integer> count4 = map.get(count3[j]);
                if(count4==null||count4.size()==0){
                    Integer[] r2 = {Integer.valueOf(count1[i].toString()),Integer.valueOf(count3[j].toString())};
                    rights.add(r2);
                    continue;
                }
                Object[] count5 = count4.toArray();
                for (int k = 0; k < count5.length; k++) {
                    Integer[] r3 = {Integer.valueOf(count1[i].toString()),
                                    Integer.valueOf(count3[j].toString()),
                                    Integer.valueOf(count5[k].toString())};
                    rights.add(r3);
                }
            }
        }
        return rights;
    }

    //权限筛选
    @Override
    public List<Menu> selectAllRights() {
        Map<Integer,List<Menu>> map = new ManagedMap<>();
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status",1);
        List<Menu> menus = menuMapper.selectList(queryWrapper);
        for (Menu menu:menus) {
            if(map.containsKey(menu.getParentId())){
                List<Menu> list = map.get(menu.getParentId());
                list.add(menu);
                map.put(menu.getParentId(),list);
            }else {
                List<Menu> list = new ArrayList<>();
                list.add(menu);
                map.put(menu.getParentId(),list);
            }
        }
        List<Menu> list = map.get(0);
        for (Menu menu:list) {
            List<Menu> chidren = map.get(menu.getId());
            if(chidren==null||chidren.size()==0){
                continue;
            }
            for(Menu menu1:chidren){
                List<Menu> chidren2 = map.get(menu1.getId());
                if(chidren2==null||chidren2.size()==0){
                    continue;
                }
                for(Menu menu2:chidren2){
                    menu2.setChildren(map.get(menu2.getId()));
                }
                menu1.setChildren(chidren2);
            }
            menu.setChildren(chidren);
        }
        return list;
    }

    //权限添加
    @Override
    @Transactional
    public void getRoleRight(RoleIdMenu roleIdMenu) {
        Integer roleId = roleIdMenu.getId();
        QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId);
        roleMenuMapper.delete(queryWrapper);
        List<RoleMenu> rights = new ArrayList<>();
        List<Integer> list = new ArrayList<>();
        List<Integer[]> multipleRight = roleIdMenu.getMultipleRight();
        if (multipleRight!=null&&multipleRight.size()!=0){
            for (Integer[] i:multipleRight) {
                for (int j = 0; j < i.length; j++) {
                    if(list.contains(i[j]))continue;
                    list.add(i[j]);
                }
            }
            for (Integer n:list) {
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setRoleId(roleId);
                roleMenu.setMenuId(n);
                rights.add(roleMenu);
            }
            for (RoleMenu roleMenu:rights) {
                roleMenuMapper.insert(roleMenu);
            }
        }
    }
}
