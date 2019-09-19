import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },

  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },

  {
    path: '',
    component: Layout,
    redirect: '',
    children: [{
      path: '',
      name: '首页',
      component: () => import('@/views/dashboard/index'),
      meta: { title: '首页', icon: 'dashboard' }
    }]
  },

  {
    path: '/',
    component: Layout,
    redirect: '/example/table',
    name: 'Example',
    meta: { title: '业务操作', icon: 'example' },
    children: [
      // {
      //   path: 'table',
      //   name: 'Table',
      //   component: () => import('@/views/table/index'),
      //   meta: { title: 'Table', icon: 'table' }
      // },
      {
        path: 'tree',
        name: 'Tree',
        component: () => import('@/views/tree/index'),
        meta: { title: '业务数据列表', icon: 'dashboard' }
      },
      {
        path: 'ImportEntering',  
        name: 'ImportEntering',
        component: () => import('@/views/ImportEntering/index'),
        meta: { title: '进仓数据', icon: 'tree',disable:true }
      },
      {
        path: 'ExportEntering',
        name: 'ExportEntering',
        component: () => import('@/views/ExportEntering/index'),
        meta: { title: '出仓数据', icon: 'tree',disable:true }
      }
    ]
  },

  // {
  //   path: '/form',
  //   component: Layout,
  //   children: [
  //     {
  //       path: 'index',
  //       name: 'Form',
  //       component: () => import('@/views/form/index'),
  //       meta: { title: '模板配置', icon: 'form' }
  //     }
  //   ]
  // },
  {
    path: '/importTemplate',
    component: Layout,
    redirect: '/importTemplate',
    name: 'importTemplate',
    meta: { title: '模板配置', icon: 'example' },
    children: [
      {
        path: 'importTemplate',
        name: 'importTemplate',
        component: () => import('@/views/importTemplate/index'),
        meta: { title: '客户模板列表', icon: 'form' }
      },
      {
        path: 'ocrTemplate',  
        name: 'ocrTemplate',
        component: () => import('@/views/importTemplate/ocrTemplate'),
        meta: { title: '识别模板列表', icon: 'table',disable:true }
      },
      {
        path: 'ImportEnteringTemplate',  
        name: 'ImportEnteringTemplate',
        component: () => import('@/views/importTemplate/inTemplate'),
        meta: { title: '进仓模板', icon: 'tree',disable:true }
      },
      {
        path: 'ExportEnteringTemplate',
        name: 'ExportEnteringTemplate',
        component: () => import('@/views/importTemplate/outTemplate'),
        meta: { title: '出仓模板', icon: 'tree',disable:true }
      }
    ]
  },
  {
    path: '/commodity',
    component: Layout,
    children: [
      {
        path: 'commodity',
        name: 'commodity',
        component: () => import('@/views/commodity/index'),
        meta: { title: '商品库', icon: 'form',disable:false }
      }
    ]
  },
  {
    path: 'external-link',
    component: Layout,
    children: [
      {
        path: '',
        meta: { title: '用户列表', icon: 'user' }
      }
    ]
  },


  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
