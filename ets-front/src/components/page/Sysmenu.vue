<template>
  <div>
    <a>bug:1、编辑菜单不显示图标</a>
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-lx-cascades"></i> 菜单设置
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="container">
      <div class="handle-box">
        <el-button
            type="primary"
            icon="el-icon-delete"
            class="handle-del mr10"
            @click="handleAddMenu"
        >新增菜单</el-button>
        <el-button
            type="primary"
            icon="el-icon-delete"
            class="handle-del mr10"
            @click="delAllSelection"
        >批量删除</el-button>
        <el-select v-model="query.queryIn.status" placeholder="菜单状态" clearable class="handle-select mr10">
          <el-option key="1" label="禁用" value="false"></el-option>
          <el-option key="2" label="启用" value="true"></el-option>
        </el-select>
        <el-input v-model="query.queryIn.name" placeholder="菜单名称" class="handle-input mr10"></el-input>
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
        <el-table-column prop="name" label="菜单名称"></el-table-column>
        <el-table-column prop="level" label="菜单级别"></el-table-column>
        <el-table-column prop="parentName" label="父级菜单"></el-table-column>
        <el-table-column prop="path" label="菜单路径"></el-table-column>
        <el-table-column prop="icon" label="菜单图标"></el-table-column>
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
        <el-form-item label="菜单名称" prop="name">
          <el-input v-model="updateForm.name"></el-input>
        </el-form-item>
        <el-form-item label="父级菜单" prop="parentName">
          <el-select v-model="updateForm.parentName" placeholder="父级菜单" class="handle-select mr10">
            <el-option label="无父级菜单" value=""></el-option>
            <el-option :value="menu.name" v-for="menu in menus">{{menu.name}}</el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="菜单路径" prop="path">
          <el-input v-model="updateForm.path"></el-input>
        </el-form-item>
        <el-form-item label="菜单图标" prop="icon">
          <el-popover
              placement="bottom-start"
              popper-class="pupop-select-icon"
              transition="el-zoom-in-top"
              trigger="click"
              v-model="popoverVisible"
              :disabled="disabledSelected">
            <!-- 弹出框内容区 -->
            <el-scrollbar
                v-if="popoverVisible"
                class="hide-x"
                :native="false"
                :noresize="false">
              <!-- 图标项 -->
              <div
                  class="icon-item"
                  v-for="item in options"
                  :key="item"
                  :class="{ 'is-active': isActive(item) }"
                  @click="onClickSelected(item)">
                <i :class="item"></i>
              </div>
            </el-scrollbar>
            <!-- 页面显示内容区 -->
            <template slot="reference">
              <div :class="{
        'mod-select-icon': 1,
        'is-opened': popoverVisible,
        'is-active': value,
        'is-disabled': disabledSelected
        }">
                <!-- 显示图标 -->
                <div class="icon-item">
                  <i :class="value || 'el-icon-plus'"></i>
                </div>
                <!-- 清空按钮 -->
                <div v-show="value" class="btn-clear">
                  <i class="el-icon-close" @click.stop="onClickClear()"></i>
                </div>
              </div>
            </template>
          </el-popover>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
                <el-button @click="editVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveEdit">确 定</el-button>
            </span>
    </el-dialog>
    <!-- 新增菜单弹出框 -->
    <el-dialog title="新增菜单" :visible.sync="addMenuVisible" width="45%" @close="closeAddDialog">
      <el-form ref="addMenuForm" :model="addMenuForm" :rules="rules" label-width="80px">
        <el-form-item label="菜单名称" prop="name">
          <el-input v-model="addMenuForm.name"></el-input>
        </el-form-item>
        <el-form-item label="父级菜单" prop="parentName">
          <el-select v-model="addMenuForm.parentName" placeholder="父级菜单" class="handle-select mr10">
            <el-option label="无父级菜单" value=""></el-option>
            <el-option :value="menu.name" v-for="menu in menus">{{menu.name}}</el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="菜单路径" prop="path">
          <el-input v-model="addMenuForm.path" ></el-input>
        </el-form-item>

      </el-form>
      <span slot="footer" class="dialog-footer">
                <el-button @click="addMenuVisible = false">取 消</el-button>
                <el-button type="primary" @click="addMenu">确 定</el-button>
            </span>
    </el-dialog>
  </div>
</template>

<script>
import { fetchData } from '../../api/index';
export default {
  name: 'basetable',
  //图标选择
  // 设置绑定参数
  model: {
    prop: 'value',
    event: 'selected',
  },
  props: {
    disabled: Boolean,
    // 接收绑定参数 - 图标类名
    value: {
      type: String,
      required: true,
    },
    // 选项数据，图标类名数组
    options: {
      type: Array,
      default: () => ([]),
    },
  },
  computed: {
    disabledSelected() {
      if (this.disabled) return true;
      return this.$parent.form ? this.$parent.form.disabled : false;
      },
    },
  data() {
    //校验规则 rule校验规则   value校验的数据   callback回调函数
    const checkParentId = (rule, value, callback) => {
      if(this.addMenuForm.parentId<0||this.updateForm.parentId<0) return callback(new Error('请正确输入父级菜单ID'))
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
      addMenuVisible:false,
      // 总页数
      pageTotal: 0,
      // 权限表单
      rightForm: {},
      // 新增表单
      addMenuForm:{
        // name: '',
        // level:0
      },
      // 新增回显
      updateForm:{},
      idx: -1,
      id: -1,
      jsonList:'',
      menus:[],
      // 多选表单
      multipleSelection: [],
      // 权限验证
      rules: {
        name: [
          { required: true, message: '请输入菜单名', trigger: 'blur' },
          { min: 3, max: 30, message: '长度在 3 到 30 个字符', trigger: 'blur' },
        ],
        parentId: [
          { required: true, message: '请输入父级菜单ID', trigger: 'blur' },
          { validator: checkParentId , trigger: 'blur' }
        ]
      },
      props: { multiple: true },
      Menu: [],
      // 图标弹出框显示状态
      popoverVisible: false,
    };
  },
  created() {
    this.getData();
  },
  methods: {
    //获取下拉菜单
    async getTrueData() {
      const {
        data: result
      } = await this.$http.get("/menus/getTrueMenus")
      if (result.status !== 200) return this.$message.error("查询失败")
      this.menus = result.data
    },
    // 获取数据
    async getData() {
      const {
        data: result
      } = await this.$http.get("/menus/getAllMenus", {
        params: this.query
      })
      if (result.status !== 200) return this.$message.error("查询失败")
      this.tableData = result.data.list
      this.pageTotal = result.data.total
      this.getTrueData()
    },
    // 多选操作
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    async delAllSelection() {
      const result =  await this.$confirm('此操作将永久删除菜单, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).catch(error => error)
      //如果确认  confirm  如果取消 cancel
      if(result == 'cancel'){
        return this.$message.info("删除取消")
      }else if(result == 'confirm'){
        this.jsonList=this.multipleSelection
        const {data: result2} = await this.$http.post(`/menus/deleteMenus`,this.jsonList)
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
      } = await this.$http.get("/menus/getAllMenus", {
        params: this.query
      })
      if (result.status !== 200) return this.$message.error("查询失败")
      this.tableData = result.data.list
      this.pageTotal = result.data.total
    },
    async updateStatus(menu){
      //实现状态修改  注意使用模版字符串  ES6中提出的新用法 ${key}
      const {data: result} = await this.$http.put(`/menus/updateMenuStatus/${menu.id}/${menu.status}`)
      if(result.status !== 200) return this.$message.error("状态修改失败")
      this.$message.success("状态修改成功!")
      this.getData();
    },
    // 删除操作
    async handleDelete(menu) {
      //1.消息确认框
      const result =  await this.$confirm('此操作将永久删除 '+menu.name+', 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).catch(error => error)
      //如果确认  confirm  如果取消 cancel
      if(result == 'cancel'){
        return this.$message.info("删除取消")
      }else if(result == 'confirm'){
        const {data: result2} = await this.$http.delete(`/menus/deleteMenuById/${menu.id}`)
        if(result2.status !== 200) return this.$message.error("删除失败")
        this.$message.success("删除成功")
      }
      //重新加载 数据
      this.getData();
    },
    addMenu() {
      this.$refs.addMenuForm.validate(async valid => {
        if (!valid) return
        const {data: result} = await this.$http.post('/menus/addMenu',this.addMenuForm)
        if(result.status !== 200) return this.$message.error("新增失败")
        this.addMenuVisible = false;
        this.$message.success(`添加菜单${this.addMenuForm.name}成功`);
        this.getData();
      })
    },
    // 编辑操作
    async handleEdit(menu) {
      this.editVisible = true;
      const {data: result} = await this.$http.get(`/menus/selectMenuById/${menu.id}`)
      if(result.status !== 200) return this.$message.error("该菜单不存在")
      this.updateForm = result.data
    },
    // 用户新增按钮操作
    async handleAddMenu(menu) {
      this.addMenuVisible = true;
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
      this.$refs.addMenuForm.resetFields()
    },
    // 保存编辑
    saveEdit() {
      this.$refs.updateForm.validate(async valid => {
        if (!valid) return
        let menu={};
        menu.id=this.updateForm.id
        menu.name=this.updateForm.name
        menu.level=this.updateForm.level
        menu.status=this.updateForm.status
        menu.parentName=this.updateForm.parentName
        menu.path=this.updateForm.path
        menu.icon=this.updateForm.icon
        const {data: result} = await this.$http.put('/menus/updateMenuById',menu)
        if(result.status !== 200) return this.$message.error("菜单修改失败")
        this.editVisible = false;
        this.$message.success(`修改菜单${menu.name}成功`);
        this.getData();
      })
    },
    // 分页导航
    handlePageChange(val) {
      this.$set(this.query, 'pageIndex', val);
      this.getData();
    },
    // 是否为当前已选项
    isActive(item) {
      return this.value === item;
    },
    // 选中图标
    onClickSelected(item) {
      this.$emit('selected', item);
      this.popoverVisible = false;
    },
    // 清空选项
    onClickClear() {
      this.$emit('selected', '');
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
