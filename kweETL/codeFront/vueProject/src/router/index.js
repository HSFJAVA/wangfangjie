import Vue from 'vue'
import Router from 'vue-router'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import CustomerList from '@/components/CustomerList'
import todo from '@/components/todo'
import DetailPage from '@/components/DetailPage'
import exportenter from '@/components/BuinessList/ExportEntering'
import importenter from '@/components/BuinessList/ImportEntering' 
import YewuList from '@/components/BuinessList/YewuList'
import login from '@/components/login/login'
import menuList from '@/components/menuList'
import OcrBaseData from '@/components/BuinessList/OcrBaseData'
Vue.use(ElementUI)
Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'login',
      component: login,
      redirect: '' // 默认就跳转此页面
    },
    {
      path: '/menulist',
      name: 'menulist',
      component: menuList,
      children: [
        {
          path: '/CustomerList',
          name: 'CustomerList',
          component: CustomerList
        },
        {
          path: '/todo',
          name: 'todo',
          component: todo
        },
        {
          path: '/DetailPage',
          name: 'DetailPage',
          component: DetailPage
        },
        {
          path: '/exportenter',
          name: 'exportenter',
          component: exportenter
        },
        {
          path: '/importenter',
          name: 'importenter',
          component: importenter
        },
        {
          path: '/YewuList',
          name: 'YewuList',
          component: YewuList
        },
        {
          path: "/OcrBaseData",
          name:"OcrBaseData",
          component:OcrBaseData
        }
      ]
    }
  ]
})
