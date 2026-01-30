import { request } from '@/utils/request'

export interface Memorial {
  id?: number
  petId?: number
  petName?: string
  customerId?: number
  customerName?: string
  title?: string
  description?: string
  coverImage?: string
  status?: number
  viewCount?: number
  candleCount?: number
  flowerCount?: number
  createdAt?: string
}

export interface MemorialQuery {
  page?: number
  pageSize?: number
  status?: number
}

// 获取纪念馆列表
export const getMemorialList = (params: MemorialQuery) => {
  return request.get<any>('/memorials', { params })
}

// 获取纪念馆详情
export const getMemorialDetail = (id: number) => {
  return request.get(`/memorials/${id}`)
}

// 创建纪念馆
export const createMemorial = (data: Memorial) => {
  return request.post('/memorials', data)
}

// 获取留言列表
export const getMemorialMessages = (id: number, limit?: number) => {
  return request.get(`/memorials/${id}/messages`, { params: { limit } })
}

// 获取相册
export const getMemorialAlbums = (id: number) => {
  return request.get(`/memorials/${id}/albums`)
}
