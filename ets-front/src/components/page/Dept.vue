<template>
  <div>
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-lx-cascades"></i> 部门设置
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="container">
      <div class="handle-box">
        <el-button
            type="primary"
            icon="el-icon-delete"
            class="handle-del mr10"
            @click="handleAddDept"
        >新增部门</el-button>
        <el-button
            type="primary"
            icon="el-icon-delete"
            class="handle-del mr10"
            @click="delAllSelection"
        >批量删除</el-button>
        <el-select v-model="query.queryIn.status" placeholder="部门状态" clearable class="handle-select mr10">
          <el-option key="1" label="禁用" value="0"></el-option>
          <el-option key="2" label="启用" value="1"></el-option>
        </el-select>
        <el-input v-model="query.queryIn.name" placeholder="部门名称" class="handle-input mr10"></el-input>
        <el-select v-model="query.queryIn.level" placeholder="部门级别" clearable class="handle-select mr10">
          <el-option key="1" label="1级" value="1"></el-option>
          <el-option key="2" label="2级" value="2"></el-option>
          <el-option key="3" label="3级" value="3"></el-option>
          <el-option key="4" label="4级" value="4"></el-option>
        </el-select>
        <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
      </div>
      <el-table
          :data="tableData"
          border
          class="table"
          ref="multipleTable"
          header-cell-class-name="table-header"
          @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column prop="id" label="ID" width="55" align="center"></el-table-column>
        <el-table-column prop="name" label="部门名称"></el-table-column>
        <el-table-column prop="level" label="部门级别"></el-table-column>
        <el-table-column prop="parentDept" label="父级部门"></el-table-column>
        <el-table-column label="状态" align="center">
          <template slot-scope="scope">
            <el-switch v-model="scope.row.status" @change="updateStatus(scope.row)"
                       active-color="#13ce66" inactive-color="#ff4949">
            </el-switch>
          </template>
        </el-table-column>
        <!--        <el-table-column prop="date" label="注册时间"></el-table-column>-->
        <el-table-column label="操作" width="220" align="center">
          <template slot-scope="scope">
            <el-button
                type="text"
                icon="el-icon-edit"
                @click="handleEdit(scope.row)"
            >编辑</el-button>
            <el-button
                type="text"
                icon="el-icon-delete"
                class="red"
                @click="handleDelete(scope.row)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination
            background
            layout="total, prev, pager, next"
            :current-page="query.pageIndex"
            :page-size="query.pageSize"
            :total="pageTotal"
            @current-change="handlePageChange"
        ></el-pagination>
      </div>
    </div>

    <!-- 编辑弹出框 -->
    <el-dialog title="编辑" :visible.sync="editVisible" width="45%" @close="closeUpdateDialog">
      <el-form ref="updateForm" :model="updateForm" :rules="rules" label-width="80px">
        <el-form-item label="部门名称" prop="name">
          <el-input v-model="updateForm.name"></el-input>
        </el-form-item>
        <el-form-item label="父级部门" prop="parentDept">
          <el-select v-model="updateForm.parentDept" placeholder="父级部门" class="handle-select mr10">
            <el-option label="无父级部门" value=""></el-option>
            <el-option :value="dept.name" v-for="dept in depts">{{dept.name}}</el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
                <el-button @click="editVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveEdit">确 定</el-button>
            </span>
    </el-dialog>
    <!-- 新增部门弹出框 -->
    <el-dialog title="新增部门" :visible.sync="addDeptVisible" width="45%" @close="closeAddDialog">
      <el-form ref="addDeptForm" :model="addDeptForm" :rules="rules" label-width="80px">
        <el-form-item label="部门名称" prop="name">
          <el-input v-model="addDeptForm.name"></el-input>
        </el-form-item>
        <el-form-item label="父级部门" prop="parentDept">
          <el-select v-model="addDeptForm.parentDept" placeholder="父级部门" class="handle-select mr10">
            <el-option label="无父级部门" value=""></el-option>
            <el-option :value="dept.name" v-for="dept in depts">{{dept.name}}</el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
                <el-button @click="addDeptVisible = false">取 消</el-button>
                <el-button type="primary" @click="addDept">确 定</el-button>
            </span>
    </el-dialog>
  </div>
</template>

<script>
import { fetchData } from '../../api/index';
export default {
  name: 'basetable',
  data() {
    //校验规则 rule校验规则   value校验的数据   callback回调函数
    const checkParentId = (rule, value, callback) => {
      if(this.addDeptForm.parentId<0||this.updateForm.parentId<0) return callback(new Error('请正确输入父级部门ID'))
      //否则校验成功
      callback()
    }
    return {
      // 分页查询参数
      query: {
        queryIn:{
			name:'',
			status:'',
			level:'',
		  },
        pageIndex: 1,
        pageSize: 10
      },
      // 绑定表格
      tableData: [],
      delList: [],
      // 编辑显示
      editVisible: false,
      // 权限绑定显示
      rightEditVisible: false,
      // 新增显示
      addDeptVisible:false,
      // 总页数
      pageTotal: 0,
      // 权限表单
      rightForm: {},
      // 新增表单
      addDeptForm:{
        // name: '',
        // level:0
      },
      // 新增回显
      updateForm:{},
      idx: -1,
      id: -1,
      jsonList:''
      ,
      // 多选表单
      multipleSelection: [],
      // 权限验证
      rules: {
        name: [
          { required: true, message: '请输入部门名', trigger: 'blur' },
          { min: 3, max: 30, message: '长度在 3 到 30 个字符', trigger: 'blur' },
        ],
        parentId: [
          { required: true, message: '请输入父级部门ID', trigger: 'blur' },
          { validator: checkParentId , trigger: 'blur' }
        ]
      },
      props: { multiple: true },
      depts: [],
    };
  },
  created() {
    this.getData();
	  this.getAllRightData();
  },
  methods: {
	  // 获取全部部门数据
	  async getAllRightData() {
	    const {
	      data: result
	    } = await this.$http.get("/depts/selectAllDepts")
	    if (result.status !== 200) return this.$message.error("查询失败")
	    this.depts = result.data
	  },
    // 获取分页数据
    async getData() {
      const {
        data: result
      } = await this.$http.get("/depts/getAllDeptsByPage", {
        params: this.query
      })
      if (result.status !== 200) return this.$message.error("查询失败")
      this.tableData = result.data.list
      this.pageTotal = result.data.total
      this.getAllRightData();
    },
    // 多选操作
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    async delAllSelection() {
      const result =  await this.$confirm('此操作将永久删除部门, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).catch(error => error)
      console.log(result)
      //如果确认  confirm  如果取消 cancel
      if(result == 'cancel')return this.$message.info("删除取消")
      {
        this.jsonList=this.multipleSelection
        const {data: result2} = await this.$http.post(`/depts/deleteDepts`,this.jsonList)
        if(result2.status !== 200) return this.$message.error("删除失败")
        this.$message.success("删除成功")
        this.getData();
        this.multipleSelection = [];
      }
    },
    async handleSearch() {
      this.query.pageIndex=1
      const {
        data: result
      } = await this.$http.get("/depts/getAllDeptsByPage", {
        params: this.query
      })
      if (result.status !== 200) return this.$message.error("查询失败")
      this.tableData = result.data.list
      this.pageTotal = result.data.total
    },
    async updateStatus(dept){
      //实现状态修改  注意使用模版字符串  ES6中提出的新用法 ${key}
      const {data: result} = await this.$http.put(`/depts/updateDeptStatus/${dept.id}/${dept.status}`)
      if(result.status !== 200) return this.$message.error("状态修改失败")
      this.$message.success("状态修改成功!")
      this.getData();
    },
    // 删除操作
    async handleDelete(dept) {
      //1.消息确认框
      const result =  await this.$confirm('此操作将永久删除 '+dept.name+', 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).catch(error => error)
      //如果确认  confirm  如果取消 cancel
      if(result == 'cancel'){
        return this.$message.info("删除取消")
      }else if(result == 'confirm'){
        const {data: result2} = await this.$http.delete(`/depts/deleteDeptById/${dept.id}`)
        if(result2.status !== 200) return this.$message.error("删除失败")
        this.$message.success("删除成功")
      }
      //重新加载 数据
      this.getData();
    },
    addDept() {
      this.$refs.addDeptForm.validate(async valid => {
        if (!valid) return
        const {data: result} = await this.$http.post('/depts/addDept',this.addDeptForm)
        if(result.status !== 200) return this.$message.error("新增失败")
        this.addDeptVisible = false;
        this.$message.success(`添加部门${this.addDeptForm.name}成功`);
        this.getData();
      })
    },
    // 编辑操作
    async handleEdit(dept) {
      this.editVisible = true;
      const {data: result} = await this.$http.get(`/depts/selectDeptById/${dept.id}`)
      if(result.status !== 200) return this.$message.error("该部门不存在")
      this.updateForm = result.data
    },
    // 用户新增按钮操作
    async handleAddDept(dept) {
      this.addDeptVisible = true;
    },
    closeUpdateDialog(){
      //重置表格数据
      this.$refs.updateForm.resetFields()
    },
    closeRightDialog(){
      //重置表格数据
      this.$refs.rightForm.resetFields()
    },
    closeAddDialog(){
      //重置表格数据
      this.$refs.addDeptForm.resetFields()
    },
    // 保存编辑
    saveEdit() {
      this.$refs.updateForm.validate(async valid => {
        if (!valid) return
        let dept={};
        dept.id=this.updateForm.id
        dept.name=this.updateForm.name
        dept.status=this.updateForm.status
        dept.parentDept=this.updateForm.parentDept
        const {data: result} = await this.$http.put('/depts/updateDeptById',dept)
        if(result.status !== 200) return this.$message.error("部门修改失败")
        this.editVisible = false;
        this.$message.success(`修改部门${dept.name}成功`);
        this.getData();
      })
    },
    // 分页导航
    handlePageChange(val) {
      this.$set(this.query, 'pageIndex', val);
      this.getData();
    },
  }
};
</script>

<style scoped>
.handle-box {
  margin-bottom: 20px;
}

.handle-select {
  width: 120px;
}

.handle-input {
  width: 300px;
  display: inline-block;
}
.table {
  width: 100%;
  font-size: 14px;
}
.red {
  color: #ff0000;
}
.mr10 {
  margin-right: 10px;
}
.table-td-thumb {
  display: block;
  margin: auto;
  width: 40px;
  height: 40px;
}
</style>
