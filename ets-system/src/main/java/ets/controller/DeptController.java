package ets.controller;

import ets.pojo.Dept;
import ets.service.DeptService;
import ets.vo.PageResult;
import ets.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/depts")
public class DeptController {
    @Autowired
    private DeptService deptService;
    //查询所有部门
    @GetMapping("/selectAllDepts")
    public SysResult getAllDept(){
        List<Dept> depts = deptService.getAllDept();
        return SysResult.success(depts);
    }
//    条件查询部门
    @GetMapping("/getAllDeptsByPage")
    public SysResult getAllDeptsByPage(PageResult pageResult){
        PageResult allDeptsByPage = deptService.getAllDeptsByPage(pageResult);
        return SysResult.success(allDeptsByPage);
    }
//    删除部门
    @DeleteMapping("/deleteDeptById/{id}")
    public SysResult deleteDeptById(Dept dept){
        deptService.deleteDeptById(dept);
        return SysResult.success();
    }
//    批量删除部门
    @PostMapping("/deleteDepts")
    public SysResult deleteDepts(@RequestBody List<Dept> list){
        deptService.deleteDepts(list);
        return SysResult.success();
    }
//    新增部门
    @PostMapping("/addDept")
    public  SysResult addDept(@RequestBody Dept dept){
        deptService.addDept(dept);
        return SysResult.success();
    }
//    修改部门状态
    @PutMapping("/updateDeptStatus/{id}/{status}")
    public SysResult updateDeptStatus(Dept dept){
        deptService.updateDeptStatus(dept);
        return SysResult.success();
    }
//    编辑部门信息
    @PutMapping("/updateDeptById")
    public  SysResult updateDeptById(@RequestBody Dept dept){
        deptService.updateDeptById(dept);
        return SysResult.success();
    }
//    编辑部门信息回写
    @GetMapping("/selectDeptById/{id}")
    public SysResult selectDeptById(@PathVariable Integer id){
        Dept dept = deptService.selectDeptById(id);
        return SysResult.success(dept);
    }
}
