import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'

const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { title: '登录', hideLayout: true },
  },
  {
    path: '/',
    component: () => import('@/layouts/MainLayout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index.vue'),
        meta: { title: '工作台', icon: 'HomeFilled' },
      },
      // 订单中心
      {
        path: 'order',
        name: 'Order',
        redirect: '/order/list',
        meta: { title: '订单中心', icon: 'Document' },
        children: [
          {
            path: 'pending',
            name: 'OrderPending',
            component: () => import('@/views/order/list.vue'),
            meta: { title: '待处理订单' },
          },
          {
            path: 'list',
            name: 'OrderList',
            component: () => import('@/views/order/list.vue'),
            meta: { title: '全部订单' },
          },
          {
            path: 'detail/:id',
            name: 'OrderDetail',
            component: () => import('@/views/order/detail.vue'),
            meta: { title: '订单详情', hidden: true },
          },
        ],
      },
      // 客户管理
      {
        path: 'customer',
        name: 'Customer',
        component: () => import('@/views/customer/index.vue'),
        meta: { title: '客户管理', icon: 'User' },
      },
      // 宠物档案
      {
        path: 'pet',
        name: 'Pet',
        component: () => import('@/views/pet/index.vue'),
        meta: { title: '宠物档案', icon: 'PriceTag' },
      },
      // 服务套餐
      {
        path: 'service',
        name: 'Service',
        component: () => import('@/views/service/index.vue'),
        meta: { title: '服务套餐', icon: 'Box' },
      },
      // 告别仪式
      {
        path: 'ceremony',
        name: 'Ceremony',
        component: () => import('@/views/ceremony/index.vue'),
        meta: { title: '告别仪式', icon: 'MagicStick' },
      },
      // 火化管理
      {
        path: 'cremation',
        name: 'Cremation',
        component: () => import('@/views/cremation/index.vue'),
        meta: { title: '火化管理', icon: 'Sunrise' },
      },
      // 骨灰寄存
      {
        path: 'storage',
        name: 'Storage',
        component: () => import('@/views/storage/index.vue'),
        meta: { title: '骨灰寄存', icon: 'Location' },
      },
      // 纪念馆
      {
        path: 'memorial',
        name: 'Memorial',
        component: () => import('@/views/memorial/index.vue'),
        meta: { title: '纪念馆', icon: 'Picture' },
      },
      // 纪念商城
      {
        path: 'shop',
        name: 'Shop',
        component: () => import('@/views/shop/index.vue'),
        meta: { title: '纪念商城', icon: 'Present' },
      },
      // 数据统计
      {
        path: 'statistics',
        name: 'Statistics',
        component: () => import('@/views/statistics/index.vue'),
        meta: { title: '数据统计', icon: 'DataAnalysis' },
      },
      // 系统设置
      {
        path: 'system',
        name: 'System',
        redirect: '/system/settings',
        meta: { title: '系统设置', icon: 'Setting' },
        children: [
          {
            path: 'settings',
            name: 'SystemSettings',
            component: () => import('@/views/system/settings.vue'),
            meta: { title: '基础设置' },
          },
          {
            path: 'staff',
            name: 'SystemStaff',
            component: () => import('@/views/system/staff.vue'),
            meta: { title: '员工管理' },
          },
          {
            path: 'role',
            name: 'SystemRole',
            component: () => import('@/views/system/role.vue'),
            meta: { title: '角色权限' },
          },
        ],
      },
    ],
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/dashboard',
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

// 路由守卫
router.beforeEach((to, _from, next) => {
  // 设置页面标题
  document.title = `${to.meta.title || '宠爱天堂'} - 宠物殡葬管理系统`

  // TODO: 添加登录验证
  const token = localStorage.getItem('token')
  if (to.path !== '/login' && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router
