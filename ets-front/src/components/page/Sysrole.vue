<template>
  <div>
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-lx-cascades"></i> 角色设置
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="container">
      <div class="handle-box">
        <el-button
            type="primary"
            icon="el-icon-delete"
            class="handle-del mr10"
            @click="handleAddrole"
        >新增角色</el-button>
        <el-button
            type="primary"
            icon="el-icon-delete"
            class="handle-del mr10"
            @click="delAllSelection"
        >批量删除</el-button>
        <el-select v-model="query.queryIn.status" placeholder="角色状态" clearable class="handle-select mr10">
          <el-option key="1" label="禁用" value="false"></el-option>
          <el-option key="2" label="启用" value="true"></el-option>
        </el-select>
        <el-input v-model="query.queryIn.name" placeholder="角色名" class="handle-input mr10"></el-input>
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
        <el-table-column prop="name" label="角色名"></el-table-column>
        <el-table-column prop="level" label="等级"></el-table-column>
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
                icon="el-icon-lx-hot"
                @click="handleRights(scope.row)"
            >权限绑定</el-button>
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
      <el-form ref="updateForm" :model="updateForm" :rules="rules" label-width="70px">
        <el-form-item label="角色名" prop="name">
          <el-input v-model="updateForm.name"></el-input>
        </el-form-item>
        <el-form-item label="级别" prop="level">
          <el-input v-model="updateForm.level" type="number"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
                <el-button @click="editVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveEdit">确 定</el-button>
            </span>
    </el-dialog>
    <!-- 权限绑定弹出框 -->
    <el-dialog title="权限绑定" :visible.sync="rightEditVisible" width="30%" >
      <div class="block">
        <span class="demonstration">选择角色菜单权限</span>
        <el-cascader
            :options="menus"
            :props="props"
            :label="menus.name"
            :children="menus.children"
            v-model="roleRight.multipleRight"
            clearable></el-cascader>
      </div>
      <span slot="footer" class="dialog-footer">
                <el-button @click="rightEditVisible = false">取 消</el-button>
                <el-button type="primary" @click="savaRights">确 定</el-button>
      </span>
    </el-dialog>
    <!-- 新增角色弹出框 -->
    <el-dialog title="新增角色" :visible.sync="addRoleVisible" width="45%" @close="closeAddDialog">
      <el-form ref="addRoleForm" :model="addRoleForm" :rules="rules" label-width="70px">
        <el-form-item label="角色名" prop="name">
          <el-input v-model="addRoleForm.name"></el-input>
        </el-form-item>
        <el-form-item label="级别" prop="level">
          <el-input v-model="addRoleForm.level" type="number"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
                <el-button @click="addRoleVisible = false">取 消</el-button>
                <el-button type="primary" @click="addRole">确 定</el-button>
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
    // const checkName = (rule, value, callback) => {
    //   if(this.addRoleForm.name==null) return callback(new Error('请输入角色名'))
    //   //否则校验成功
    //   callback()
    // }
    const checkLevel = (rule, value, callback) => {
      if(this.addRoleForm.level<0||this.updateForm.level<0) return callback(new Error('请正确输入角色级别'))
      //否则校验成功
      callback()
    }
    return {
      // 分页查询参数
      query: {
        queryIn:{name:'',
          status:''},
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
      addRoleVisible:false,
      // 总页数
      pageTotal: 0,
      // 权限表单
      rightForm: {},
      // 新增表单
      addRoleForm:{
        name: '',
        level:0,
      },
      // 新增回显
      updateForm:{},
      idx: -1,
      id: -1,
      jsonList:''
      ,
      // 多选表单
      multipleSelection: {},
      // 权限验证
      rules: {
        name: [
          { required: true, message: '请输入角色名', trigger: 'blur' },
          { min: 3, max: 30, message: '长度在 3 到 30 个字符', trigger: 'blur' },
          // { validator: checkName , trigger: 'blur' }
        ],
        level: [
          { required: true, message: '请输入角色级别', trigger: 'blur' },
          { validator: checkLevel , trigger: 'blur' }
        ]
      },
      props: { multiple: true ,clearable:true, value:"id",label:"name",children:"children"},
      menus: [],
      roleRight:{
        multipleRight:[],
        id:0
      }
    };
  },
  created() {
    this.getData();
    this.getAllRightData();
  },
  methods: {
    // 获取数据
    async getAllRightData() {
      const {
        data: result
      } = await this.$http.get("/rights/selectAllRights")
      if (result.status !== 200) return this.$message.error("查询失败")
      this.menus = result.data
    },
    // 获取数据
    async getData() {
      const {
        data: result
      } = await this.$http.get("/roles/getTheRoleList", {
        params: this.query
      })
      if (result.status !== 200) return this.$message.error("查询失败")
      this.tableData = result.data.list
      this.pageTotal = result.data.total
    },
    // 多选操作
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    // 删除多选角色
    async delAllSelection() {
      const result =  await this.$confirm('此操作将永久删除角色, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).catch(error => error)
      //如果确认  confirm  如果取消 cancel
      if(result == 'cancel'){
        return this.$message.info("删除取消")
      }else if(result == 'confirm'){
        this.jsonList=this.multipleSelection
        const {data: result2} = await this.$http.post(`/roles/deleteRoles`,this.jsonList)
        if(result2.status !== 200) return this.$message.error("删除失败")
        this.$message.success("删除成功")
        this.getData();
        this.multipleSelection = [];
      }
    },
    // 关键词查询
    async handleSearch() {
      this.query.pageIndex=1
      const {
        data: result
      } = await this.$http.get("/roles/getTheRoleList", {
        params: this.query
      })
      if (result.status !== 200) return this.$message.error("查询失败")
      this.tableData = result.data.list
      this.pageTotal = result.data.total
    },
    // 修改角色状态
    async updateStatus(role){
      //实现状态修改  注意使用模版字符串  ES6中提出的新用法 ${key}
      const {data: result} = await this.$http.put(`/roles/status/${role.id}/${role.status}`)
      if(result.status !== 200) return this.$message.error("状态修改失败")
      this.$message.success("状态修改成功!")
      this.getData();
    },
    // 删除操作
    async handleDelete(role) {
      //1.消息确认框
      const result =  await this.$confirm('此操作将永久删除 '+role.name+', 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).catch(error => error)
      //如果确认  confirm  如果取消 cancel
      if(result == 'cancel'){
        return this.$message.info("删除取消")
      }else if(result == 'confirm'){
        const {data: result2} = await this.$http.delete(`/roles/deleteRoleById/${role.id}`)
        if(result2.status !== 200) return this.$message.error("删除失败")
        this.$message.success("删除成功")
      }
      //重新加载 数据
      this.getData();
    },
    // 新增角色
    addRole() {
      this.$refs.addRoleForm.validate(async valid => {
        if (!valid) return
        const {data: result} = await this.$http.post('/roles/addRole',this.addRoleForm)
        if(result.status !== 200) return this.$message.error("新增失败")
        this.addRoleVisible = false;
        this.$message.success(`添加角色${this.addRoleForm.name}成功`);
        this.getData();
      })
    },
    // 编辑角色前查询复写操作
    async handleEdit(role) {
      this.editVisible = true;
      const {data: result} = await this.$http.get(`/roles/${role.id}`)
      if(result.status !== 200) return this.$message.error("该角色不存在")
      this.updateForm = result.data
    },
    // 角色权限绑定前查询操作
    async handleRights(role) {
      this.rightEditVisible = true;
      const {data: result} = await this.$http.get(`/rights/selectRightsById/${role.id}`)
      if(result.status !== 200) return this.$message.error("权限查询失败")
      this.roleRight.id = role.id
      // console.log(this.roleRight)
      if(result.data==null){
        this.roleRight.multipleRight = []
      }
      this.roleRight.multipleRight = result.data
    },
    // 用户新增按钮操作
    async handleAddrole(role) {
      this.addRoleVisible = true;
    },
    closeUpdateDialog(){
      //重置表格数据
      this.$refs.updateForm.resetFields()
    },
    // closeRightDialog(){
    //   //重置表格数据
    //   this.$refs.rightForm.resetFields()
    // },
    closeAddDialog(){
      //重置表格数据
      this.$refs.addRoleForm.resetFields()
    },
    // 保存角色编辑
    async saveEdit() {
      this.$refs.updateForm.validate(async valid => {
        if (!valid) return
        let role={};
        role.id=this.updateForm.id
        role.name=this.updateForm.name
        role.level=this.updateForm.level
        role.status=this.updateForm.status
        const {data: result} = await this.$http.put('/roles/updateRoleById',role)
        if(result.status !== 200) return this.$message.error("角色修改失败")
        this.editVisible = false;
        this.$message.success(`修改角色${role.name}成功`);
        this.getData();
      })
    },
    //保存角色权限绑定
    async savaRights(){
      const {data: result} = await this.$http.post('/rights/setRoleRights',this.roleRight)
      if(result.status !== 200) return this.$message.error("权限修改失败")
      this.rightEditVisible = false;
      this.$message.success(`权限绑定成功`);
      this.getData();
    },
    // 分页导航
    handlePageChange(val) {
      this.$set(this.query, 'pageIndex', val);
      this.getData();
    }
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
