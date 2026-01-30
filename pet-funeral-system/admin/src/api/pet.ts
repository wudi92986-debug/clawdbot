import { request } from '@/utils/request'

export interface Pet {
  id?: number
  customerId: number
  customerName?: string
  name: string
  species: string
  breed?: string
  color?: string
  age?: number
  weight?: number
  gender?: number
  photoUrl?: string
  status?: number
  statusName?: string
  deathTime?: string
  deathCause?: string
  remark?: string
  createdAt?: string
  updatedAt?: string
}

export interface PetQuery {
  page?: number
  pageSize?: number
  keyword?: string
  customerId?: number
  status?: number
}

// 获取宠物列表
export const getPetList = (params: PetQuery) => {
  return request.get('/pets', { params })
}

// 获取宠物详情
export const getPetDetail = (id: number) => {
  return request.get(`/pets/${id}`)
}

// 新增宠物
export const createPet = (data: Pet) => {
  return request.post('/pets', data)
}

// 更新宠物
export const updatePet = (id: number, data: Pet) => {
  return request.put(`/pets/${id}`, data)
}

// 删除宠物
export const deletePet = (id: number) => {
  return request.delete(`/pets/${id}`)
}

// 登记宠物死亡
export const registerDeath = (id: number, data: { deathTime: string; deathCause?: string }) => {
  return request.post(`/pets/${id}/death`, data)
}
