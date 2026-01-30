import { request } from '@/utils/request'

export interface ServicePackage {
  id?: number
  name: string
  description?: string
  price: number
  originalPrice?: number
  duration?: number
  items?: string
  imageUrl?: string
  sort?: number
  status?: number
  createdAt?: string
  updatedAt?: string
}

export interface PackageQuery {
  page?: number
  pageSize?: number
  keyword?: string
  status?: number
}

// 获取套餐列表
export const getPackageList = (params?: PackageQuery) => {
  return request.get('/packages', { params })
}

// 获取套餐详情
export const getPackageDetail = (id: number) => {
  return request.get(`/packages/${id}`)
}

// 新增套餐
export const createPackage = (data: ServicePackage) => {
  return request.post('/packages', data)
}

// 更新套餐
export const updatePackage = (id: number, data: ServicePackage) => {
  return request.put(`/packages/${id}`, data)
}

// 删除套餐
export const deletePackage = (id: number) => {
  return request.delete(`/packages/${id}`)
}

// 更新套餐状态
export const updatePackageStatus = (id: number, status: number) => {
  return request.put(`/packages/${id}/status`, { status })
}
