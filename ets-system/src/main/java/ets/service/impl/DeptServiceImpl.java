package ets.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import ets.pojo.Roles;
import ets.pojo.UserDept;
import ets.vo.SBZUtils;
import ets.mapper.DeptMapper;
import ets.mapper.UserDeptMapper;
import ets.pojo.Dept;
import ets.service.DeptService;
import ets.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DeptServiceImpl implements DeptService {
    @Autowired
    private DeptMapper deptMapper;
    @Autowired
    private UserDeptMapper userDeptMapper;
    //查询所有部门
    @Override
    public List<Dept> getAllDept() {
        QueryWrapper<Dept> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status",1);
        return deptMapper.selectList(queryWrapper);
    }
    //    条件查询部门
    @Override
    public PageResult getAllDeptsByPage(PageResult pageResult) {
        Map<String, Object> map = SBZUtils.dealWithQuery(pageResult);
        Long total = deptMapper.countTotal(map);
        List<Dept> depts = deptMapper.selectDeptsByPage(map);
        pageResult.setList(depts);
        pageResult.setTotal(total);
        return pageResult;
    }
    //    删除部门
    @Override
    @Transactional
    public void deleteDeptById(Dept dept) {
        deptMapper.deleteById(dept.getId());
        QueryWrapper<UserDept> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dept_id",dept.getId());
        userDeptMapper.delete(queryWrapper);
    }
    //    批量删除部门
    @Override
    @Transactional
    public void deleteDepts(List<Dept> list) {
        List<Integer> idList=new ArrayList<>();
        QueryWrapper<UserDept> queryWrapper = new QueryWrapper<>();
        for(Dept dept:list){
            idList.add(dept.getId());
            queryWrapper.clear();
            queryWrapper.eq("dept_id",dept.getId());
            userDeptMapper.delete(queryWrapper);
        }
        deptMapper.deleteBatchIds(idList);
    }
    //    新增部门
    @Override
    @Transactional
    public void addDept(Dept dept) {
        String parentDept = dept.getParentDept();
        Integer parentLevel = 0;
        Integer parentId = 0;
        QueryWrapper<Dept> queryWrapper = new QueryWrapper<>();
        if(parentDept!=null||parentDept.length()!=0){
            queryWrapper.clear();
            queryWrapper.eq("name",parentDept);
            Dept dept1 = deptMapper.selectOne(queryWrapper);
            parentLevel = dept1.getLevel();
            parentId = dept1.getId();
        }
        dept.setLevel(parentLevel+1);
        dept.setParentId(parentId);
        dept.setStatus(false);
        deptMapper.insert(dept);
    }
    //    修改部门状态
    @Override
    @Transactional
    public void updateDeptStatus(Dept dept) {
        deptMapper.updateById(dept);
    }
    //    编辑部门信息
    @Override
    @Transactional
    public void updateDeptById(Dept dept) {
        String parentDept = dept.getParentDept();
        Integer parentLevel = 0;
        Integer parentId = 0;
        QueryWrapper<Dept> queryWrapper = new QueryWrapper<>();
        if(parentDept!=null||parentDept.length()!=0){
            queryWrapper.clear();
            queryWrapper.eq("name",parentDept);
            Dept dept1 = deptMapper.selectOne(queryWrapper);
            parentLevel = dept1.getLevel();
            parentId = dept1.getId();
        }
        dept.setLevel(parentLevel+1);
        dept.setParentId(parentId);
        deptMapper.updateById(dept);
    }
    //    编辑部门信息回写
    @Override
    public Dept selectDeptById(Integer id) {
        Dept dept = deptMapper.selectDeptById(id);
        return dept;
    }
}
