import { request } from '@/utils/request'

export interface DashboardStats {
  totalCustomers?: number
  totalPets?: number
  totalOrders?: number
  totalRevenue?: number
  todayOrders?: number
  todayRevenue?: number
  pendingOrders?: number
  expiringStorages?: number
}

// 获取仪表盘统计
export const getDashboardStats = () => {
  return request.get('/statistics/dashboard')
}

// 获取收入统计
export const getRevenueStats = (startDate: string, endDate: string) => {
  return request.get('/statistics/revenue', { params: { startDate, endDate } })
}

// 获取套餐销售统计
export const getPackageStats = () => {
  return request.get('/statistics/packages')
}

// 获取宠物种类统计
export const getPetSpeciesStats = () => {
  return request.get('/statistics/pet-species')
}

// 获取纪念馆互动统计
export const getMemorialStats = () => {
  return request.get('/statistics/memorial-interactions')
}
