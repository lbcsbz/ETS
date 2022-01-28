package ets.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.util.JSONPObject;
import ets.pojo.Roles;
import ets.service.RolesService;
import ets.vo.JsonUtil;
import ets.vo.SysResult;
import ets.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RolesController {
    @Autowired
    private RolesService rolesService;
    @GetMapping("/rolesList")
    public SysResult getAllMenus(PageResult pageResult){
        PageResult allRoles = rolesService.getAllRoles(pageResult);
        System.out.println(allRoles);
        return SysResult.success(allRoles);
    }
    @PostMapping("/addRole")
    public SysResult addRole(@RequestBody Roles roles){
        rolesService.addRole(roles);
        return SysResult.success();
    }
    @PutMapping("/status/{id}/{status}")
    public SysResult updataRoleStatus(Roles roles){
        rolesService.updataRoleStatus(roles);
        return SysResult.success();
    }
    @DeleteMapping("/deleteRoleById/{id}")
    public SysResult deleteRoleById(Roles roles){
        rolesService.deleteRoleById(roles);
        return SysResult.success();
    }
    @GetMapping("/{id}")
    public SysResult selectRoleById(@PathVariable Integer id){
        Roles roles = rolesService.selectRoleById(id);
        return  SysResult.success(roles);
    }
    @PutMapping("/updateRoleById")
    public SysResult updateRoleById(@RequestBody Roles roles){
        rolesService.updateRoleById(roles);
        return SysResult.success();
    }
    @GetMapping("/getTheRoleList")
    public SysResult getTheRoleList(PageResult pageResult){
        PageResult roleList = rolesService.getTheRoleList(pageResult);
        return SysResult.success(roleList);
    }
    @PostMapping("/deleteRoles")
    public SysResult deleteRoles(@RequestBody List<Roles> list){
        //字符串类型接收，前端需要进行字符串转换
//        List<Roles> list = JsonUtil.from(jsonList, new TypeReference<List<Roles>>() {
//        });
        rolesService.deleteRoles(list);
        return SysResult.success();
    }
    @GetMapping("/getAllRoles")
    public SysResult getAllRoles(){
        List<Roles> roles = rolesService.getAllroles1();
        return SysResult.success(roles);
    }
}
