package ets.service;

import ets.pojo.Dept;
import ets.vo.PageResult;
import ets.vo.SysResult;

import java.util.List;

public interface DeptService {
    //查询所有部门
    List<Dept> getAllDept();

//    条件查询部门
    PageResult getAllDeptsByPage(PageResult pageResult);

//    删除部门
    void deleteDeptById(Dept dept);

//    批量删除部门
    void deleteDepts(List<Dept> list);

//    新增部门
    void addDept(Dept dept);

//    修改部门状态
    void updateDeptStatus(Dept dept);

//    编辑部门信息
    void updateDeptById(Dept dept);

//    编辑部门信息回写
    Dept selectDeptById(Integer id);

}
