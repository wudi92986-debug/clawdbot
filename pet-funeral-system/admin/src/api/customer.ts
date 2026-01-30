import { request } from '@/utils/request'

export interface Customer {
  id?: number
  name: string
  phone: string
  email?: string
  address?: string
  idCard?: string
  remark?: string
  createdAt?: string
  updatedAt?: string
}

export interface CustomerQuery {
  page?: number
  pageSize?: number
  keyword?: string
}

// 获取客户列表
export const getCustomerList = (params: CustomerQuery) => {
  return request.get<any>('/customers', { params })
}

// 获取客户详情
export const getCustomerDetail = (id: number) => {
  return request.get(`/customers/${id}`)
}

// 新增客户
export const createCustomer = (data: Customer) => {
  return request.post('/customers', data)
}

// 更新客户
export const updateCustomer = (id: number, data: Customer) => {
  return request.put(`/customers/${id}`, data)
}

// 删除客户
export const deleteCustomer = (id: number) => {
  return request.delete(`/customers/${id}`)
}
