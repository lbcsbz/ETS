package ets.controller;

import ets.pojo.Menu;
import ets.pojo.RoleIdMenu;
import ets.service.RightService;
import ets.vo.SysResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/rights")
public class RinghtController {
    @Autowired
    private RightService rightService;
    //根据角色id获取权限菜单列表
    @GetMapping("/selectRightsById/{id}")
    public SysResult selectRights(@PathVariable("id") Integer id){
        List<Integer[]> list = rightService.selectRightsById(id);
        return SysResult.success(list);
    }
//    获取所有权限菜单列表
    @GetMapping("/selectAllRights")
    public SysResult selectAllRights(){
        List<Menu> list = rightService.selectAllRights();
        return SysResult.success(list);
    }
//    通过角色id、菜单id设置权限
    @PostMapping ("/setRoleRights")
    public  SysResult setRoleRights(@RequestBody RoleIdMenu roleIdMenu){
        System.out.println(roleIdMenu);
        rightService.getRoleRight(roleIdMenu);
        return SysResult.success();
    }

}
