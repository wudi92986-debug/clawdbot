import { request } from '@/utils/request'

export interface Ceremony {
  id?: number
  orderId?: number
  petId?: number
  petName?: string
  customerName?: string
  ceremonyType?: number
  ceremonyTypeName?: string
  hallNo?: string
  startTime?: string
  endTime?: string
  status?: number
  statusName?: string
  contactName?: string
  contactPhone?: string
  attendeeCount?: number
  specialRequests?: string
  remark?: string
  createdAt?: string
}

export interface CeremonyQuery {
  page?: number
  pageSize?: number
  status?: number
  date?: string
}

// 获取仪式列表
export const getCeremonyList = (params: CeremonyQuery) => {
  return request.get<any>('/ceremonies', { params })
}

// 获取仪式详情
export const getCeremonyDetail = (id: number) => {
  return request.get(`/ceremonies/${id}`)
}

// 创建仪式
export const createCeremony = (data: Ceremony) => {
  return request.post('/ceremonies', data)
}

// 更新仪式
export const updateCeremony = (id: number, data: Ceremony) => {
  return request.put(`/ceremonies/${id}`, data)
}

// 开始仪式
export const startCeremony = (id: number) => {
  return request.post(`/ceremonies/${id}/start`)
}

// 完成仪式
export const completeCeremony = (id: number) => {
  return request.post(`/ceremonies/${id}/complete`)
}

// 取消仪式
export const cancelCeremony = (id: number, reason?: string) => {
  return request.post(`/ceremonies/${id}/cancel`, null, { params: { reason } })
}

// 获取今日仪式
export const getTodayCeremonies = () => {
  return request.get('/ceremonies/today')
}

// 获取某日日程
export const getDaySchedule = (date: string) => {
  return request.get('/ceremonies/schedule/day', { params: { date } })
}
