<template>
    <div class="sidebar">
        <el-menu
            class="sidebar-el-menu"
            :default-active="onRoutes"
            :collapse="collapse"
            background-color="#324157"
            text-color="#bfcbd9"
            active-text-color="#20a0ff"
            unique-opened
            router
        >
            <template v-for="item in items">
                <template v-if="!item.children">
                  <el-menu-item :index="item.path" :key="item.path">
                    <i :class="item.icon"></i>
                    <span slot="title">{{ item.name }}</span>
                  </el-menu-item>
                </template>
                <template v-else>
                  <el-submenu :index="item.path" :key="item.path">
                    <template slot="title">
                      <i :class="item.icon"></i>
                      <span slot="title">{{ item.name }}</span>
                    </template>
                    <template v-for="subItem in item.children">
                      <el-submenu
                          v-if="subItem.children"
                          :index="subItem.path"
                          :key="subItem.path"
                      >
                        <template slot="title">{{ subItem.name }}</template>
                        <el-menu-item
                            v-for="threeItem in subItem.children"
                            :key="threeItem.path"
                            :index="threeItem.path"
                        >{{ threeItem.name }}</el-menu-item>
                      </el-submenu>
                      <el-menu-item
                          v-else
                          :index="subItem.path"
                          :key="subItem.path"
                      >{{ subItem.name }}</el-menu-item>
                    </template>
                  </el-submenu>
                </template>
            </template>
        </el-menu>
    </div>
</template>

<script>
import bus from '../common/bus';
export default {
    data() {
        return {
            collapse: false,
            items: [],
        };
    },
    methods: {
      logout() {
        //1.删除session中的数据
        window.sessionStorage.clear()
        //2.用户访问登录页面
        this.$router.push('/login')
      },
      async getMenuList() {
        let role='role='+sessionStorage.getItem("role")
        const {data: result} =  await this.$http.post('/menus/getmenulist',role)
        if(result.status !== 200) return this.$message.error("左侧菜单查询失败")
        this.items = result.data

      },
    },
    computed: {
        onRoutes() {
            return this.$route.path.replace('/', '');
        }
    },
    created() {
        this.getMenuList()
        // 通过 Event Bus 进行组件间通信，来折叠侧边栏
        bus.$on('collapse', msg => {
            this.collapse = msg;
            bus.$emit('collapse-content', msg);
        });
    }
};
</script>

<style scoped>
.sidebar {
    display: block;
    position: absolute;
    left: 0;
    top: 70px;
    bottom: 0;
    overflow-y: scroll;
}
.sidebar::-webkit-scrollbar {
    width: 0;
}
.sidebar-el-menu:not(.el-menu--collapse) {
    width: 250px;
}
.sidebar > ul {
    height: 100%;
}
</style>
