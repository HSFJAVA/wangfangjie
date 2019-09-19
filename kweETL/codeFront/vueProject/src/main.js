// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import axios from 'axios'
import Moment from 'moment'
import utiljs from './publicjs/util.js';
Vue.config.productionTip = false
Vue.prototype.$http = axios //将axios挂载在Vue实例原型上
Vue.prototype.moment = Moment //将moment挂载在Vue实例原型上
Vue.prototype.utiljs = utiljs
// 设置axios请求的token
axios.defaults.headers.common['token'] = 'f4c902c9ae5a2a9d8f84868ad064e706'
//设置请求头
// axios.defaults.headers.post["Content-type"] = "application/json"

/* eslint-disable no-new */
new Vue({
  el: '#app',
  axios,
  router,
  Moment,
  utiljs,
  components: { App },
  template: '<App/>'
})


