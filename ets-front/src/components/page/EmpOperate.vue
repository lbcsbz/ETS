<template>
  <div>
    <a>bug1、照片上传下载预览2、时间显示问题3、排版
    </a>
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-lx-cascades"></i> 员工操作
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="container">
      <div class="handle-box">
        <el-button
            type="primary"
            icon="el-icon-delete"
            class="handle-del mr10"
            @click="handleAddUser"
        >新增员工</el-button>
        <el-button
            type="primary"
            icon="el-icon-delete"
            class="handle-del mr10"
            @click="delAllSelection"
        >批量删除</el-button><br><br>
        <el-select v-model="query.queryIn.status" placeholder="账号状态" class="handle-select mr10" clearable>
          <el-option key="1" label="禁用" value="0"></el-option>
          <el-option key="2" label="启用" value="1"></el-option>
        </el-select>
        <el-input v-model="query.queryIn.name" placeholder="姓名" class="handle-input mr10"></el-input>
		    <el-select v-model="query.queryIn.deptName" placeholder="部门" clearable class="handle-input mr10">
		      <el-option :value="dept.name" v-for="dept in depts">{{dept.name}}</el-option>
		    </el-select>
        <el-input v-model="query.queryIn.code" placeholder="员工编号" class="handle-input mr10"></el-input>
        <el-select v-model="query.queryIn.roleName" placeholder="角色" class="handle-select mr10" clearable>
          <el-option :value="role.name" v-for="role in roles">{{role.name}}</el-option>
        </el-select>
        <el-select v-model="query.queryIn.gender" placeholder="性别" class="handle-select mr10" clearable>
          <el-option key="1" label="男" value="男"></el-option>
          <el-option key="2" label="女" value="女"></el-option>
        </el-select>
        <el-button type="primary" icon="el-icon-search" @click="handleSearch">条件搜索</el-button>
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
        <el-table-column prop="name" label="姓名">
          <template slot-scope="scope">
            <el-button
                type="text"
                @click="handleUserMessage(scope.row)"
            >{{scope.row.name}}</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="code" label="员工编号"></el-table-column>
        <el-table-column prop="roleName" label="角色"></el-table-column>
        <el-table-column prop="deptName" label="部门"></el-table-column>
        <el-table-column label="性别" align="center" >
          <template slot-scope="scope">
            <template v-if="scope.row.gender=='男'">
              <el-tag
                  type=""
                  size="small"
                  effect="dark">男
              </el-tag>
            </template>
            <template v-else>
              <el-tag
                  type="danger"
                  size="small"
                  effect="dark">女
              </el-tag>
            </template>
          </template>
		    </el-table-column>
        <el-table-column prop="created" label="创建时间"></el-table-column>
        <el-table-column prop="lastLoginDate" label="末次登录时间"></el-table-column>
        <el-table-column label="账号状态" align="center">
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
                icon="el-icon-edit"
                @click="handleResetPwd(scope.row)"
            >重置密码</el-button>
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

    <!-- 详情弹出框 -->
    <el-dialog title="员工详情" :visible.sync="userMessageVisible" width="45%" @close="closeUserMessageDialog">
        <el-descriptions class="margin-top"
                         title="员工详情"
                         :column="4"
						 border
                         direction="vertical">
          <el-descriptions-item label="照片" :span="4">苏州市</el-descriptions-item>
          <el-descriptions-item label="姓名">{{user.name}}</el-descriptions-item>
          <el-descriptions-item label="性别">
            <el-tag
                :type=type1
                size="small"
                effect="dark">
              {{ user.gender }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="年龄">{{user.age}}</el-descriptions-item>
          <el-descriptions-item label="账号状态">
            <el-tag
                :type=type2
                size="small"
                effect="dark">
              {{ status1 }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="员工编号">{{user.code}}</el-descriptions-item>
          <el-descriptions-item label="部门">{{user.deptName}}</el-descriptions-item>
          <el-descriptions-item label="角色">{{user.roleName}}</el-descriptions-item>
          <el-descriptions-item label="邮箱">{{user.email}}</el-descriptions-item>
          <el-descriptions-item label="手机号">{{user.phone}}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{user.created}}</el-descriptions-item>
          <el-descriptions-item label="更新时间">{{user.updated}}</el-descriptions-item>
          <el-descriptions-item label="末次登录时间">{{user.lastLoginDate}}</el-descriptions-item>
        </el-descriptions>
        <span slot="footer" class="dialog-footer">
                  <el-button @click="userMessageVisible = false">关闭</el-button>
        </span>
    </el-dialog>
    <!-- 编辑弹出框 -->
    <el-dialog title="编辑" :visible.sync="editVisible" width="45%" @close="closeUpdateDialog">
      <el-form ref="updateForm" :model="updateForm" :rules="rules" label-width="80px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="updateForm.name"></el-input>
        </el-form-item>
        <el-form-item label="角色" prop="roleName">
          <el-select v-model="updateForm.roleName" placeholder="角色" class="handle-select mr10" clearable>
            <el-option :value="role.name" v-for="role in roles">{{role.name}}</el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="部门" prop="deptName">
          <el-select v-model="updateForm.deptName" placeholder="部门" clearable class="handle-input mr10">
            <el-option :value="dept.name" v-for="dept in depts">{{dept.name}}</el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-select v-model="updateForm.gender" placeholder="性别" class="handle-select mr10" clearable>
            <el-option key="1" label="男" value="男"></el-option>
            <el-option key="2" label="女" value="女"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="updateForm.email"></el-input>
        </el-form-item>
        <el-form-item label="手机" prop="phone">
          <el-input v-model="updateForm.phone"></el-input>
        </el-form-item>
        <el-form-item label="照片" prop="picture">
          <el-popover>

          </el-popover>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
                <el-button @click="editVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveEdit">确 定</el-button>
            </span>
    </el-dialog>
    <!-- 新增员工弹出框 -->
    <el-dialog title="新增员工" :visible.sync="addEmpVisible" width="45%" @close="closeAddDialog">
      <el-form ref="addEmpForm" :model="addEmpForm" :rules="rules" label-width="80px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="addEmpForm.name"></el-input>
        </el-form-item>
        <el-form-item label="角色" prop="roleName">
          <el-select v-model="addEmpForm.roleName" placeholder="角色" class="handle-select mr10" clearable>
            <el-option :value="role.name" v-for="role in roles">{{role.name}}</el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="部门" prop="deptName">
          <el-select v-model="addEmpForm.deptName" placeholder="部门" clearable class="handle-input mr10">
            <el-option :value="dept.name" v-for="dept in depts">{{dept.name}}</el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-select v-model="addEmpForm.gender" placeholder="性别" class="handle-select mr10" clearable>
            <el-option key="1" label="男" value="男"></el-option>
            <el-option key="2" label="女" value="女"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="addEmpForm.email"></el-input>
        </el-form-item>
		<el-form-item label="身份证号" prop="residentId">
		  <el-input v-model="addEmpForm.residentId"></el-input>
		</el-form-item>
        <el-form-item label="手机" prop="phone">
          <el-input v-model="addEmpForm.phone"></el-input>
        </el-form-item>
        <el-form-item label="照片" prop="picture">
          <el-popover>

          </el-popover>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
                <el-button @click="addEmpVisible = false">取 消</el-button>
                <el-button type="primary" @click="addEmp">确 定</el-button>
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
    const checkEmail = (rule, value, callback) => {
      //定义邮箱的正则表达式  JS中用/来表示正则表达式的开始和结束
      const emailRege = /^[a-zA-Z0-9-_]+@[a-zA-Z0-9-_]+\.[a-zA-Z0-9-_]+$/
      //正则表达式语法校验  test(xx) 校验成功 返回true  校验失败返回false
      if (emailRege.test(value)) {
        //表示邮箱合法 正确返回
        return callback()
      }
      callback(new Error('请填写正确的邮箱地址'))
    }
    //校验手机号规则
    const checkPhone = (rule, value, callback) => {
      //定义校验手机号的正则语法
      const phoneRege = /^1[34578][0-9]{9}$/
      if (phoneRege.test(value)) {
        return callback()
      }
      callback(new Error('请填写正确的手机号'))
    }
	//校验身份证号规则
	const checkResidentId = (rule, value, callback) => {
	  //定义校验的正则语法
	  let _IDRe18 = /^([1-6][1-9]|50)\d{4}(18|19|20)\d{2}((0[1-9])|10|11|12)(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/
	  let _IDre15 =  /^([1-6][1-9]|50)\d{4}\d{2}((0[1-9])|10|11|12)(([0-2][1-9])|10|20|30|31)\d{3}$/
	  // 校验身份证：
	  if( _IDRe18.test( value ) || _IDre15.test( value )  ) {
	  	return callback()
	  } 
	  callback(new Error('请填写正确的身份证号'))
	}
    return {
      // 分页查询参数
      query: {
        queryIn:{
          name:'',
          status:'',
          deptName:'',
          code:'',
          gender:'',
          roleName:''
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
      addEmpVisible:false,
      //详情显示
      userMessageVisible:false,
      // 总页数
      pageTotal: 0,
      // 权限表单
      rightForm: {},
      // 新增表单
      addEmpForm:{
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
          { required: true, message: '请输入员工名称', trigger: 'blur' },
          { min: 3, max: 30, message: '长度在 3 到 30 个字符', trigger: 'blur' },
        ],
        roleName: [
          { required: true, message: '请选择员工角色', trigger: 'blur' },
        ],
        deptName: [
          { required: true, message: '请选择员工所属部门', trigger: 'blur' },
        ],
        gender: [
          { required: true, message: '请选择员工性别', trigger: 'blur' },
        ],
        email: [
          { required: true, message: '请输入员工邮箱', trigger: 'blur' },
          { validator: checkEmail , trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '请输入员工手机号', trigger: 'blur' },
          { validator: checkPhone , trigger: 'blur' }
        ],
		residentId: [
		  { required: true, message: '请输入身份证号', trigger: 'blur' },
		  { validator: checkResidentId , trigger: 'blur' }
		],
      },
      props: { multiple: true },
      depts: [],
	    roles:[],
		  user:{},
      type1:"",
      type2:"",
      status1:"",
    };
  },
  created() {
    this.getData();
  },
  methods: {
    // 获取角色数据
    async getRolesData() {
      const {
        data: result
      } = await this.$http.get("/roles/getAllRoles")
      if (result.status !== 200) return this.$message.error("查询失败")
      this.roles = result.data
    },
    // 获取部门数据
    async getDeptsData() {
      const {
        data: result
      } = await this.$http.get("/depts/selectAllDepts")
      if (result.status !== 200) return this.$message.error("查询失败")
      this.depts = result.data
    },
    // 获取员工数据
    async getData() {
      const {
        data: result
      } = await this.$http.get("/emps/getAllEmps", {
        params: this.query
      })
      if (result.status !== 200) return this.$message.error("查询失败")
      this.tableData = result.data.list
      this.pageTotal = result.data.total
      this.getDeptsData()
      this.getRolesData()
    },
    // 多选操作
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    async delAllSelection() {
      const result =  await this.$confirm('此操作将永久删除员工, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).catch(error => error)
      //如果确认  confirm  如果取消 cancel
      if(result == 'cancel'){
        return this.$message.info("删除取消")
      }else if(result == 'confirm'){
        this.jsonList=this.multipleSelection
        const {data: result2} = await this.$http.post(`/emps/deleteEmps`,this.jsonList)
        if(result2.status !== 200) return this.$message.error("删除失败")
        this.$message.success("删除成功")
        this.getData();
        this.multipleSelection = [];
      }
    },
    //显示详情
    async handleUserMessage(emp){
      this.user=emp
      this.type1 = emp.gender=='男'?"":"danger"
      this.type2 = emp.status=='1'?"":"danger"
      this.status1 = emp.status=='1'?"启用":"禁用"
      this.userMessageVisible = true
    },
    async handleSearch() {
      this.query.pageIndex=1
      const {
        data: result
      } = await this.$http.get("/emps/getAllEmps", {
        params: this.query
      })
      if (result.status !== 200) return this.$message.error("查询失败")
      this.tableData = result.data.list
      this.pageTotal = result.data.total
    },
    async updateStatus(emp){
      //实现状态修改  注意使用模版字符串  ES6中提出的新用法 ${key}
      const {data: result} = await this.$http.put(`/emps/updateEmpStatus/${emp.id}/${emp.status}`)
      if(result.status !== 200) return this.$message.error("状态修改失败")
      this.$message.success("状态修改成功!")
      this.getData();
    },
    // 删除操作
    async handleDelete(emp) {
      //1.消息确认框
      const result =  await this.$confirm('此操作将永久删除 '+emp.name+', 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).catch(error => error)
      //如果确认  confirm  如果取消 cancel
      if(result == 'cancel'){
        return this.$message.info("删除取消")
      }else if(result == 'confirm'){
        const {data: result2} = await this.$http.delete(`/emps/deleteEmpById/${emp.id}`)
        if(result2.status !== 200) return this.$message.error("删除失败")
        this.$message.success("删除成功")
      }
      //重新加载 数据
      this.getData();
    },
    // 密码重置操作
    async handleResetPwd(emp) {
      //1.消息确认框
      const result =  await this.$confirm('此操作将重置 '+emp.name+'账户密码, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).catch(error => error)
      //如果确认  confirm  如果取消 cancel
      if(result == 'cancel'){
        return this.$message.info("操作取消")
      }else if(result == 'confirm'){
        const {data: result2} = await this.$http.delete(`/emps/resetPwdById/${emp.id}`)
        if(result2.status !== 200) return this.$message.error("密码重置失败")
        this.$message.success("密码重置成功")
      }
    },
    addEmp() {
      this.$refs.addEmpForm.validate(async valid => {
        if (!valid) return
        const {data: result} = await this.$http.post('/emps/addEmp',this.addEmpForm)
        if(result.status !== 200) return this.$message.error("新增失败")
        this.addEmpVisible = false;
        this.$message.success(`添加员工${this.addEmpForm.name}成功`);
        this.getData();
      })
    },
    // 编辑操作
    async handleEdit(emp) {
      this.editVisible = true;
      const {data: result} = await this.$http.get(`/emps/selectEmpById/${emp.id}`)
      if(result.status !== 200) return this.$message.error("该员工不存在")
      this.updateForm = result.data
    },
    // 用户新增按钮操作
    async handleAddUser(emp) {
      this.addEmpVisible = true;
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
      this.$refs.addEmpForm.resetFields()
    },
	closeUserMessageDialog(){
		// this.$refs.user.resetFields()
	},
    // 保存编辑
    saveEdit() {
      this.$refs.updateForm.validate(async valid => {
        if (!valid) return
        const {data: result} = await this.$http.put('/emps/updateEmpById',this.updateForm)
        if(result.status !== 200) return this.$message.error("员工修改失败")
        this.editVisible = false;
        this.$message.success(`修改员工${this.updateForm.name}成功`);
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
