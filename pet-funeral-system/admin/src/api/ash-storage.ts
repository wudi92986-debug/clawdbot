import { request } from '@/utils/request'

export interface AshStorage {
  id?: number
  petId?: number
  petName?: string
  customerId?: number
  customerName?: string
  locationNo?: string
  startDate?: string
  endDate?: string
  status?: number
  statusName?: string
  fee?: number
  remark?: string
  createdAt?: string
}

export interface AshStorageQuery {
  page?: number
  pageSize?: number
  status?: number
  keyword?: string
}

// 获取骨灰寄存列表
export const getAshStorageList = (params: AshStorageQuery) => {
  return request.get<any>('/ash-storage', { params })
}

// 获取寄存详情
export const getAshStorageDetail = (id: number) => {
  return request.get(`/ash-storage/${id}`)
}

// 创建骨灰寄存
export const createAshStorage = (data: AshStorage) => {
  return request.post('/ash-storage', data)
}

// 续期
export const renewAshStorage = (id: number, newEndDate: string) => {
  return request.post(`/ash-storage/${id}/renew`, null, { params: { newEndDate } })
}

// 取走骨灰
export const pickupAshStorage = (id: number) => {
  return request.post(`/ash-storage/${id}/pickup`)
}

// 获取即将到期的记录
export const getExpiringStorages = (days?: number) => {
  return request.get('/ash-storage/expiring', { params: { days } })
}

// 获取已过期的记录
export const getExpiredStorages = () => {
  return request.get('/ash-storage/expired')
}
