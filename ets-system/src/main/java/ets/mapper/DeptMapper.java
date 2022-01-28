package ets.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ets.pojo.Dept;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface DeptMapper extends BaseMapper<Dept> {
    Long countTotal(Map<String, Object> map);

    List<Dept> selectDeptsByPage(Map<String, Object> map);

    Dept selectDeptById(Integer id);
}
