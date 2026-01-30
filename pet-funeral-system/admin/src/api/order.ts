import { request } from '@/utils/request'

export interface Order {
  id?: number
  orderNo?: string
  customerId: number
  customerName?: string
  petId: number
  petName?: string
  packageId: number
  packageName?: string
  totalAmount?: number
  paidAmount?: number
  status?: number
  statusName?: string
  payMethod?: number
  payTime?: string
  serviceDate?: string
  remark?: string
  createdAt?: string
  updatedAt?: string
}

export interface OrderQuery {
  page?: number
  pageSize?: number
  keyword?: string
  status?: number
  startDate?: string
  endDate?: string
}

// 获取订单列表
export const getOrderList = (params: OrderQuery) => {
  return request.get('/orders', { params })
}

// 获取订单详情
export const getOrderDetail = (id: number) => {
  return request.get(`/orders/${id}`)
}

// 创建订单
export const createOrder = (data: Order) => {
  return request.post('/orders', data)
}

// 更新订单
export const updateOrder = (id: number, data: Order) => {
  return request.put(`/orders/${id}`, data)
}

// 取消订单
export const cancelOrder = (id: number) => {
  return request.post(`/orders/${id}/cancel`)
}

// 确认支付
export const confirmPayment = (id: number, data: { payMethod: number; paidAmount: number }) => {
  return request.post(`/orders/${id}/pay`, data)
}

// 完成订单
export const completeOrder = (id: number) => {
  return request.post(`/orders/${id}/complete`)
}
