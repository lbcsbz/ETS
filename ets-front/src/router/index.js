import Vue from 'vue';
import Router from 'vue-router';

Vue.use(Router);

export default new Router({
    routes: [
        {
            path: '/',
            redirect: '/dashboard'
        },
        {
            path: '/',
            component: () => import(/* webpackChunkName: "home" */ '../components/common/Home.vue'),
            meta: { title: 'ETS运管系统' },
            children: [
                {
                    path: '/dashboard',
                    component: () => import(/* webpackChunkName: "dashboard" */ '../components/page/Dashboard.vue'),
                    meta: { title: '系统首页' }
                },
                {
                    path: '/userSetting',
                    component: () => import(/* webpackChunkName: "dashboard" */ '../components/page/UserSetting'),
                    meta: { title: '账号设置' }
                },
                {
                    path: '/orders',
                    component: () => import(/* webpackChunkName: "icon" */ '../components/page/Orders.vue'),
                    meta: { title: '订单管理' }
                },
                {
                    path: '/trans',
                    component: () => import(/* webpackChunkName: "table" */ '../components/page/Trans.vue'),
                    meta: { title: '运输管理' }
                },
                {
                    path: '/qc',
                    component: () => import(/* webpackChunkName: "tabs" */ '../components/page/Qc.vue'),
                    meta: { title: '质控平台' }
                },
                {
                    path: '/emp',
                    component: () => import(/* webpackChunkName: "form" */ '../components/page/Emp.vue'),
                    meta: { title: '员工管理' }
                },
                {
                    path: '/settings',
                    component: () => import(/* webpackChunkName: "editor" */ '../components/page/Setttings.vue'),
                    meta: { title: '系统设置' }
                },
                {
                    path: '/orders/operate',
                    component: () => import(/* webpackChunkName: "markdown" */ '../components/page/OrdersOperate.vue'),
                    meta: { title: '订单操作' }
                },
                {
                    path: '/orders/select',
                    component: () => import(/* webpackChunkName: "upload" */ '../components/page/OrdersSelect.vue'),
                    meta: { title: '订单查询' }
                },
                {
                    path: '/trans/operate',
                    component: () => import(/* webpackChunkName: "chart" */ '../components/page/TransOperate.vue'),
                    meta: { title: '运输管理' }
                },
                {
                    path: '/trans/select',
                    component: () => import(/* webpackChunkName: "drag" */ '../components/page/TransSelect.vue'),
                    meta: { title: '运输查询' }
                },
                {
                    path: '/qc/operate',
                    component: () => import(/* webpackChunkName: "dragdialog" */ '../components/page/QcOperate.vue'),
                    meta: { title: '质控上报' }
                },
                {
                    path: '/qc/select',
                    component: () => import(/* webpackChunkName: "i18n" */ '../components/page/QcSelect.vue'),
                    meta: { title: '质控查询' }
                },
                {
                    path: '/emp/operate',
                    component: () => import(/* webpackChunkName: "permission" */ '../components/page/EmpOperate.vue'),
                    meta: { title: '员工操作', permission: true }
                },
                {
                    path: '/404',
                    component: () => import(/* webpackChunkName: "404" */ '../components/page/404.vue'),
                    meta: { title: '404' }
                },
                {
                    path: '/403',
                    component: () => import(/* webpackChunkName: "403" */ '../components/page/403.vue'),
                    meta: { title: '403' }
                },
                {
                    path: '/emp/select',
                    component: () => import(/* webpackChunkName: "donate" */ '../components/page/EmpSelect.vue'),
                    meta: { title: '员工查询' }
                },
                {
                    path: '/sysmenu',
                    component: () => import(/* webpackChunkName: "donate" */ '../components/page/Sysmenu.vue'),
                    meta: { title: '菜单设置' }
                },
                {
                    path: '/sysrole',
                    component: () => import(/* webpackChunkName: "donate" */ '../components/page/Sysrole.vue'),
                    meta: { title: '角色设置' }
                },
                {
                    path: '/dept',
                    component: () => import(/* webpackChunkName: "donate" */ '../components/page/Dept.vue'),
                    meta: { title: '部门设置' }
                }
            ]
        },
        {
            path: '/login',
            component: () => import(/* webpackChunkName: "login" */ '../components/page/Login.vue'),
            meta: { title: '登录' }
        },
        {
            path: '*',
            redirect: '/404'
        }
    ]
});
