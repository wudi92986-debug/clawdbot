/**
 * 订单相关API
 */
import { get, post, put } from '@/utils/request'

// 创建订单
export const createOrder = (data) => post('/orders', data)

// 获取订单列表
export const getOrders = (params) => get('/orders', params)

// 获取订单详情
export const getOrderDetail = (id) => get(`/orders/${id}`)

// 取消订单
export const cancelOrder = (id, data) => post(`/orders/${id}/cancel`, data)

// 接受订单
export const acceptOrder = (id) => post(`/orders/${id}/accept`)

// 拒绝订单
export const rejectOrder = (id, data) => post(`/orders/${id}/reject`, data)

// 开始拍摄
export const startShooting = (id) => post(`/orders/${id}/start-shooting`)

// 完成拍摄
export const finishShooting = (id) => post(`/orders/${id}/finish-shooting`)

// 确认收货
export const confirmOrder = (id) => post(`/orders/${id}/confirm`)

// 订单改期
export const rescheduleOrder = (id, data) => post(`/orders/${id}/reschedule`, data)

// 上传原片
export const uploadOriginals = (id, data) => post(`/orders/${id}/originals`, data)

// 选片
export const selectPhotos = (id, data) => post(`/orders/${id}/select-photos`, data)

// 上传精修片
export const uploadRefined = (id, data) => post(`/orders/${id}/refined`, data)

// 获取订单作品
export const getOrderWorks = (id, params) => get(`/orders/${id}/works`, params)

// 订单评价
export const reviewOrder = (id, data) => post(`/orders/${id}/review`, data)

// 追加评价
export const appendReview = (id, data) => post(`/orders/${id}/review/append`, data)

// 回复评价
export const replyReview = (id, data) => post(`/orders/${id}/review/reply`, data)
