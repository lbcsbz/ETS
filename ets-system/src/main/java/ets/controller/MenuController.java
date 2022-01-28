package ets.controller;

import ets.pojo.Menu;
import ets.service.MenuService;
import ets.vo.PageResult;
import ets.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menus/")
public class MenuController {
    @Autowired
    private MenuService menuService;
    //测试首页菜单查询
//    @PostMapping("/getmenulist")
//    public SysResult getAllMenus(@RequestBody String role){
//        List<Menu> allMenu = menuService.getAllMenu();
//        return SysResult.success(allMenu);
//    }
    //首页菜单查询
    @PostMapping("/getmenulist")
    public SysResult getAllMenus(String role){
        List<Menu> roleMenuList = menuService.getRoleMenuList(role);
        return SysResult.success(roleMenuList);
    }

    //菜单设置页菜单查询
    @GetMapping("/getAllMenus")
    public SysResult getAllMenuByPage(PageResult pageResult){
        PageResult allMenuByPage = menuService.getAllMenuByPage(pageResult);
        return SysResult.success(allMenuByPage);
    }
    @PostMapping("/addMenu")
    public SysResult addMenu(@RequestBody Menu menu){
        int i = menuService.addMenu(menu);
        if(i!=1)return SysResult.fail();
        return SysResult.success();
    }
    @DeleteMapping("/deleteMenuById/{id}")
    public SysResult deleteMenuById(Menu menu){
        int i = menuService.deleteMenuById(menu);
        if(i!=1)return SysResult.fail();
        return SysResult.success();
    }
    @PutMapping("/updateMenuById")
    public SysResult updateMenuById(@RequestBody Menu menu){
        int i = menuService.updateMenuById(menu);
        if(i!=1)return SysResult.fail();
        return SysResult.success();
    }
    @GetMapping("/selectMenuById/{id}")
    public  SysResult selectMenuById(@PathVariable Integer id){
        Menu menu = menuService.selectMenuById(id);
        return SysResult.success().setData(menu);
    }
    @PostMapping("/deleteMenus")
    public SysResult deleteMenus(@RequestBody List<Menu> list){
        int i = menuService.deleteMenus(list);
        return SysResult.success();
    }
    @PutMapping("/updateMenuStatus/{id}/{status}")
    public SysResult updateMenuStatus(Menu menu){
        int i = menuService.updateMenuStatus(menu);
        if(i!=1)return SysResult.fail();
        return SysResult.success();
    }
    @GetMapping("/getTrueMenus")
    public SysResult getTrueMenus(){
        List<Menu> menus = menuService.getTrueMenus();
        return SysResult.success(menus);
    }

}
