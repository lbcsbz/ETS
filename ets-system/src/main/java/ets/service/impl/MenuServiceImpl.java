package ets.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ets.vo.SBZUtils;
import ets.mapper.MenuMapper;
import ets.mapper.RoleMenuMapper;
import ets.pojo.Menu;
import ets.pojo.RoleMenu;
import ets.service.MenuService;
import ets.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private RoleMenuMapper roleMenuMapper;
    //测试用登录菜单
    @Override
    public List<Menu> getAllMenu() {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id",0).eq("status",1);
        List<Menu> menus = menuMapper.selectList(queryWrapper);
        for(Menu menu : menus){
            queryWrapper.clear();
            queryWrapper.eq("parent_id",menu.getId()).eq("status",1);
            List<Menu> menus1 = menuMapper.selectList(queryWrapper);
            menu.setChildren(menus1);
        }
        return menus;
    }

    @Override
    public List<Menu> getRightMenu() {
        return null;
    }

    @Override
    public PageResult getAllMenuByPage(PageResult pageResult) {
        Page<Menu> page=new Page<>(pageResult.getPageIndex(),pageResult.getPageSize());
        QueryWrapper<Menu> queryWrapper= new QueryWrapper<>();
        Map<String, Object> map = SBZUtils.dealWithQuery(pageResult);
        if(map.get("name")!=null)queryWrapper.like("name",map.get("name"));
        if(map.get("status")!=null)queryWrapper.eq("status",Boolean.parseBoolean(map.get("status").toString()));
        queryWrapper.eq("1",1);
        Page<Menu> page1 = menuMapper.selectPage(page, queryWrapper);
        List<Menu> records = page1.getRecords();
        for (Menu record : records) {
            record.setParentName(getParentName(record.getParentId()));
        }
        pageResult.setList(records);
        pageResult.setTotal(page1.getTotal());
        return pageResult;
    }

    @Override
    @Transactional
    public int addMenu(Menu menu) {
        menu.setStatus(false);
        int parentLevel = getParentLevel(menu.getParentName());
        int parentId = getParentId(menu.getParentName());
        menu.setParentId(parentId);
        menu.setLevel(parentLevel+1);
        int insert = menuMapper.insert(menu);
        return insert;
    }

    //获取父级level
    public int getParentLevel(String name){
        if(name.length()==0||name==null)return 0;
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",name);
        Menu menu = menuMapper.selectOne(queryWrapper);
        return menu.getLevel();
    }
    //获取父级id
    public int getParentId(String name){
        if(name.length()==0||name==null)return 0;
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",name);
        Menu menu = menuMapper.selectOne(queryWrapper);
        return menu.getId();
    }
    //获取父级name
    public String getParentName(Integer parentId){
        if(parentId==0)return null;
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",parentId);
        Menu menu = menuMapper.selectOne(queryWrapper);
        return menu.getName();
    }
    @Override
    @Transactional
    public int deleteMenuById(Menu menu) {
        int i = menuMapper.deleteById(menu.getId());
        QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("menu_id",menu.getId());
        int delete = roleMenuMapper.delete(queryWrapper);
        if(i==0||delete==0)i=0;
        i=1;
        return i;
    }

    @Override
    @Transactional
    public int updateMenuById(Menu menu) {
        int parentLevel = getParentLevel(menu.getParentName());
        int parentId = getParentId(menu.getParentName());
        menu.setParentId(parentId);
        menu.setLevel(parentLevel+1);
        int i = menuMapper.updateById(menu);
        return i;
    }

    @Override
    public Menu selectMenuById(Integer id) {
        Menu menu = menuMapper.selectById(id);
        menu.setParentName(getParentName(menu.getParentId()));
        return menu;
    }

    @Override
    @Transactional
    public int deleteMenus(List<Menu> list) {
        List<Integer> idList=new ArrayList<>();
        QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper<>();
        for(Menu menu:list){
            idList.add(menu.getId());
            queryWrapper.clear();
            queryWrapper.eq("menu_id",menu.getId());
            roleMenuMapper.delete(queryWrapper);
        }
        int i = menuMapper.deleteBatchIds(idList);
        return 0;
    }

    @Override
    @Transactional
    public int updateMenuStatus(Menu menu) {
        int i = menuMapper.updateById(menu);
        return i;
    }

    @Override
    public int selectMenuParentLevel(Menu menu) {
        if(menu.getParentId()==0)return 0;
        Menu menu1 = menuMapper.selectById(menu.getParentId());
        return menu1.getLevel();
    }

    @Override
    public List<Menu> getTrueMenus() {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status",1);
        List<Menu> list = menuMapper.selectList(queryWrapper);
        return list;
    }

    @Override
    public List<Menu> getRoleMenuList(String role) {
        Map<Integer,List<Menu>> map = new ManagedMap<>();
        List<Menu> list = menuMapper.selectMenuByRole(role);
        for (Menu menu : list) {
            if(map.containsKey(menu.getParentId())){
                List<Menu> list1 = map.get(menu.getParentId());
                list1.add(menu);
                map.put(menu.getParentId(),list1);
            }else{
                List<Menu> list1 = new ArrayList<>();
                list1.add(menu);
                map.put(menu.getParentId(),list1);
            }
        }
        List<Menu> list2 = map.get(0);
        for (Menu menu : list2) {
            List<Menu> list3 = map.get(menu.getId());
            if(list3==null||list3.size()==0)continue;
            for (Menu menu1 : list3) {
                List<Menu> list4 = map.get(menu1.getId());
                if(list4==null||list4.size()==0)continue;
                for (Menu menu2 : list4) {
                    menu2 = menu2.setChildren(map.get(menu2.getId()));
                }
                menu1 = menu1.setChildren(list4);
            }
            menu = menu.setChildren(list3);
        }
        return list2;
    }
}
