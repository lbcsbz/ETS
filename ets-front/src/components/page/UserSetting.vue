<template>
  <div>
    <a>bug1、照片上传下载预览
    </a>
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-lx-cascades"></i> 账号设置
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="container">
      <el-descriptions class="margin-top"
                       :column="4"
                       border
                       direction="vertical">
        <el-descriptions-item label="照片" :span="4">XXXX</el-descriptions-item>
        <el-descriptions-item label="姓名" :span="2">{{user.name}}</el-descriptions-item>
        <el-descriptions-item label="性别" :span="2">
          <el-tag
              :type=type1
              size="small"
              effect="dark">
            {{ user.gender }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="年龄" :span="2">{{user.age}}</el-descriptions-item>
        <el-descriptions-item label="账号状态" :span="2">
          <el-tag
              :type=type2
              size="small"
              effect="dark">
            {{ status1 }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="员工编号" :span="2">{{user.code}}</el-descriptions-item>
        <el-descriptions-item label="部门" :span="2">{{user.deptName}}</el-descriptions-item>
        <el-descriptions-item label="角色" :span="2">{{user.roleName}}</el-descriptions-item>
        <el-descriptions-item label="邮箱" :span="2">{{user.email}}</el-descriptions-item>
        <el-descriptions-item label="手机号" :span="2">{{user.phone}}</el-descriptions-item>
        <el-descriptions-item label="创建时间" :span="2">{{user.created}}</el-descriptions-item>
        <el-descriptions-item label="更新时间" :span="2">{{user.updated}}</el-descriptions-item>
        <el-descriptions-item label="末次登录时间" :span="2">{{user.lastLoginDate}}</el-descriptions-item>
      </el-descriptions>
      <el-button
          type="text"
          icon="el-icon-edit"
          @click="handleEdit()"
      >基础信息编辑</el-button>
      <el-button
          type="text"
          icon="el-icon-edit"
          @click="handleResetPwd()"
      >修改密码</el-button>
    </div>
    <!-- 编辑弹出框 -->
    <el-dialog title="基础信息编辑" :visible.sync="editVisible" width="45%" @close="closeUpdateDialog">
      <el-form ref="updateForm" :model="updateForm" :rules="rules" label-width="80px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="updateForm.name"></el-input>
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
        <el-form-item label="身份证号" prop="residentId">
          <el-input v-model="updateForm.residentId"></el-input>
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
    <!-- 修改密码 -->
    <el-dialog title="修改密码" :visible.sync="resetPwdVisible" width="45%" @close="closeResetPwdDialog">
      <el-form ref="resetPwdForm" :model="resetPwdForm" :rules="rules" label-width="80px">
        <el-form-item label="原密码" prop="pwd">
          <el-input v-model="resetPwdForm.pwd"></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newPwd">
          <el-input v-model="resetPwdForm.newPwd"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
                <el-button @click="resetPwdVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveResetPwd">确 定</el-button>
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
    //校验密码规则
    const checkPwd = (rule, value, callback) => {
      //定义校验的正则语法
      let newPwd = /^.*(?=.{6,})(?=.*\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%^&*? ]).*$/
      // 校验密码：
      if( newPwd.test( value ) ) {
        return callback()
      }
      callback(new Error('新密码最少6位，包括至少1个大写字母，1个小写字母，1个数字，1个特殊字符'))
    }
    return {
      // 编辑显示
      editVisible: false,
      resetPwdVisible:false,
      updateForm:{},
      type1:"",
      type2:"",
      status1:"",
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
        pwd: [
          { required: true, message: '请输入原密码', trigger: 'blur' },
        ],
        newPwd: [
          { required: true, message: '请输入新密码，新密码最少6位，包括至少1个大写字母，1个小写字母，1个数字，1个特殊字符', trigger: 'blur' },
          { validator: checkPwd , trigger: 'blur' }
        ],
      },
      user:{},
      resetPwdForm:{},
    };
  },
  created() {
    this.getData();
  },
  methods: {
    // 获取员工数据
    async getData() {
      let id = sessionStorage.getItem("id")
      const {
        data: result
      } = await this.$http.get(`/emps/selectEmpById/${id}`)
      if (result.status !== 200) return this.$message.error("查询失败")
      this.user = result.data
      this.type1 = this.user.gender=='男'?"":"danger"
      this.type2 = this.user.status=='1'?"":"danger"
      this.status1 = this.user.status=='1'?"启用":"禁用"
    },
    async updateStatus(){
      //实现状态修改  注意使用模版字符串  ES6中提出的新用法 ${key}
      const {data: result} = await this.$http.put(`/emps/updateEmpStatus/${this.user.id}/${this.user.status}`)
      if(result.status !== 200) return this.$message.error("状态修改失败")
      this.$message.success("状态修改成功!")
      this.getData();
    },
    // 密码操作
    async handleResetPwd() {
      //1.消息确认框
      const result =  await this.$confirm('此操作将修改账户密码, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).catch(error => error)
      //如果确认  confirm  如果取消 cancel
      if(result == 'cancel'){
        return this.$message.info("操作取消")
      }else if(result == 'confirm'){
        this.resetPwdVisible = true;
      }
    },
    // 编辑操作
    async handleEdit() {
      this.editVisible = true;
      this.updateForm =this.user
    },
    closeUpdateDialog(){
      //重置表格数据
      this.$refs.updateForm.resetFields()
    },
    closeResetPwdDialog(){
      //重置表格数据
      this.$refs.resetPwdForm.resetFields()
    },
    // 保存编辑
    saveEdit() {
      this.$refs.updateForm.validate(async valid => {
        if (!valid) return
        const {data: result} = await this.$http.put('/emps/updateEmpById',this.updateForm)
        if(result.status !== 200) return this.$message.error("员工修改失败")
        this.editVisible = false;
        this.getData();
        this.$message.success(`修改员工${this.user.name}成功`);
      })
    },
    // 保存密码
    saveResetPwd() {
      this.$refs.resetPwdForm.validate(async valid => {
        if (!valid) return
        this.resetPwdForm.id=this.user.id
        const {data: result} = await this.$http.put('/emps/resetPwdById',this.resetPwdForm)
        if(result.status == 203) return this.$message.error("原密码输入错误")
        if(result.status !== 200) return this.$message.error("密码修改失败")
        this.resetPwdVisible = false;
        this.$message.success(`修改员工${this.user.name}密码成功，请重新登录`);
        localStorage.removeItem('ms_username');
        this.$router.push('/login');
      })
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
