import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    component: () => import('@/layouts/TabbarLayout.vue'),
    children: [
      {
        path: '',
        name: 'Home',
        component: () => import('@/views/home/index.vue'),
        meta: { title: '首页' },
      },
      {
        path: 'service',
        name: 'Service',
        component: () => import('@/views/service/index.vue'),
        meta: { title: '服务' },
      },
      {
        path: 'memorial',
        name: 'MemorialList',
        component: () => import('@/views/memorial/list.vue'),
        meta: { title: '纪念馆' },
      },
      {
        path: 'user',
        name: 'User',
        component: () => import('@/views/user/index.vue'),
        meta: { title: '我的' },
      },
    ],
  },
  // 预约页面
  {
    path: '/booking',
    name: 'Booking',
    component: () => import('@/views/booking/index.vue'),
    meta: { title: '预约服务' },
  },
  // 纪念馆详情
  {
    path: '/memorial/:id',
    name: 'MemorialDetail',
    component: () => import('@/views/memorial/detail.vue'),
    meta: { title: '纪念馆' },
  },
  // 订单相关
  {
    path: '/order',
    name: 'OrderList',
    component: () => import('@/views/order/list.vue'),
    meta: { title: '我的订单' },
  },
  {
    path: '/order/:id',
    name: 'OrderDetail',
    component: () => import('@/views/order/detail.vue'),
    meta: { title: '订单详情' },
  },
  // 登录
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/user/login.vue'),
    meta: { title: '登录' },
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach((to, _from, next) => {
  document.title = `${to.meta.title || '宠爱天堂'} - 让爱延续`
  next()
})

export default router
