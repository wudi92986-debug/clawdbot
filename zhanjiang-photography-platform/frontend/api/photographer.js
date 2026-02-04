/**
 * 摄影师相关API
 */
import { get, post, put, del } from '@/utils/request'

// 获取摄影师列表
export const getPhotographers = (params) => get('/photographers', params)

// 获取摄影师详情
export const getPhotographerDetail = (id) => get(`/photographers/${id}`)

// 获取摄影师作品集
export const getPhotographerWorks = (id, params) => get(`/photographers/${id}/works`, params)

// 获取摄影师服务套餐
export const getPhotographerPackages = (id) => get(`/photographers/${id}/packages`)

// 获取摄影师档期
export const getPhotographerSchedules = (id, params) => get(`/photographers/${id}/schedules`, params)

// 获取摄影师评价
export const getPhotographerReviews = (id, params) => get(`/photographers/${id}/reviews`, params)

// 申请成为摄影师
export const applyPhotographer = (data) => post('/photographer/apply', data)

// 更新摄影师信息
export const updatePhotographerProfile = (data) => put('/photographer/profile', data)

// 获取我的摄影师信息
export const getMyPhotographerInfo = () => get('/photographer/mine')

// 获取我的套餐列表
export const getMyPackages = () => get('/photographer/packages')

// 创建服务套餐
export const createPackage = (data) => post('/photographer/packages', data)

// 更新服务套餐
export const updatePackage = (id, data) => put(`/photographer/packages/${id}`, data)

// 删除服务套餐
export const deletePackage = (id) => del(`/photographer/packages/${id}`)

// 获取我的作品列表
export const getMyWorks = (params) => get('/photographer/works', params)

// 上传作品
export const uploadWorks = (data) => post('/photographer/works', data)

// 更新作品
export const updateWorks = (id, data) => put(`/photographer/works/${id}`, data)

// 删除作品
export const deleteWorks = (id) => del(`/photographer/works/${id}`)

// 获取我的档期
export const getMySchedules = (params) => get('/photographer/schedules', params)

// 设置档期
export const setSchedules = (data) => post('/photographer/schedules', data)

// 获取数据看板
export const getDashboard = () => get('/photographer/dashboard')
