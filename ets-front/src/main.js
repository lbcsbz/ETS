import Vue from 'vue';
import App from './App.vue';
import router from './router';
import ElementUI from 'element-ui';
import VueI18n from 'vue-i18n';
import { messages } from './components/common/i18n';
import echarts from "echarts";
// import {Modal, notification} from 'ant-design-vue';
import 'element-ui/lib/theme-chalk/index.css'; // 默认主题
// import './assets/css/theme-green/index.css'; // 浅绿色主题
import './assets/css/icon.css';
import './components/common/directives';
import 'babel-polyfill';

import axios from 'axios';
/* 设定axios的请求根目录 */
axios.defaults.baseURL = 'http://localhost:8091/'
// axios.defaults.baseURL='http://manage.ets.com/'
/* 向vue对象中添加全局对象 以后发送ajax请求使用$http对象 */
Vue.prototype.$http = axios

// 引入echarts
Vue.prototype.$echarts = echarts
Vue.config.productionTip = false;
Vue.use(VueI18n);
Vue.use(ElementUI, {
    size: 'small'
});
const i18n = new VueI18n({
    locale: 'zh',
    messages
});

// 使用钩子函数对路由进行权限跳转
router.beforeEach((to,from,next)=>{
    if(to.path==="/login") return next()
    let token=window.sessionStorage.getItem("token")
    if(!token) return next('/login')
    next()
})
export default router


new Vue({
    router,
    i18n,
    render: h => h(App)
}).$mount('#app');
