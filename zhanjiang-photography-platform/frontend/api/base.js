/**
 * 基础数据API
 */
import { get } from '@/utils/request'

// 获取高校列表
export const getUniversities = () => get('/base/universities')

// 获取风格标签
export const getStyleTags = (params) => get('/base/style-tags', params)

// 获取服务类型
export const getServiceTypes = () => get('/base/service-types')

// 获取轮播图
export const getBanners = (params) => get('/base/banners', params)

// 获取系统配置
export const getConfigs = (keys) => get('/base/configs', { keys })
