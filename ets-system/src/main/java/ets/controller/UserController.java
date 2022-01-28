package ets.controller;

import ets.pojo.Menu;
import ets.pojo.User;
import ets.service.UserService;
import ets.vo.PageResult;
import ets.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/emps/")
public class UserController {
    @Autowired
    private UserService userService;
    //分页获取员工
    @GetMapping("/getAllEmps")
    public SysResult getAllEmps(PageResult pageResult){
        PageResult allEmps = userService.getAllEmps(pageResult);
        return SysResult.success(allEmps);
    }
//    多选删除
    @PostMapping("/deleteEmps")
    public SysResult deleteEmps(@RequestBody List<User> list){
        userService.deleteEmps(list);
        return SysResult.success();
    }
//    更新账号状态
    @PutMapping("/updateEmpStatus/{id}/{status}")
    public SysResult updateEmpStatus(User user){
        userService.updateEmpStatus(user);
        return SysResult.success();
    }
//    根据Id删除员工
    @DeleteMapping("/deleteEmpById/{id}")
    public SysResult deleteEmpById(User user){
        userService.deleteEmpById(user);
        return SysResult.success();
    }
//    员工账号密码重置
    @DeleteMapping("/resetPwdById/{id}")
    public SysResult resetPwdById(User user){
        userService.resetPwdById(user);
        return SysResult.success();
    }
//    新增员工
    @PostMapping("/addEmp")
    public SysResult addEmp(@RequestBody User user){
        userService.addEmp(user);
        return SysResult.success();
    }
//    根据ID查找员工
    @GetMapping("/selectEmpById/{id}")
    public SysResult selectEmpById(@PathVariable Integer id){
        User user = userService.selectEmpById(id);
        return SysResult.success(user);
    }
//    员工信息编辑
    @PutMapping("/updateEmpById")
    public SysResult updateEmpById(@RequestBody User user){
        userService.updateEmpById(user);
        return SysResult.success();
    }
    //用户登录
    @PostMapping("/login")
    public SysResult login(@RequestBody User user){
        Map<String,Object> map = userService.checkUser(user);
        if(map==null||map.isEmpty())return SysResult.fail();
        return SysResult.success(map);
    }

    //根据code修改密码
    @PutMapping("/resetPwdById")
    public SysResult resetPwdByCode(@RequestBody String resetPwdForm){
        int i = userService.resetPwdByCode(resetPwdForm);
        if(i==2)return SysResult.fail().setStatus(203);
        if(i==1)return SysResult.success();
        return SysResult.fail();
    }


}
