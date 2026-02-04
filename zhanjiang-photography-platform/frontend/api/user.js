/**
 * 用户相关API
 */
import { get, post, put } from '@/utils/request'

// 微信登录
export const wechatLogin = (data) => post('/user/login/wechat', data)

// 刷新Token
export const refreshToken = (data) => post('/user/token/refresh', data)

// 获取用户信息
export const getUserProfile = () => get('/user/profile')

// 更新用户信息
export const updateUserProfile = (data) => put('/user/profile', data)

// 提交实名认证
export const submitRealNameVerification = (data) => post('/user/verification/realname', data)

// 提交学生认证
export const submitStudentVerification = (data) => post('/user/verification/student', data)

// 获取认证状态
export const getVerificationStatus = () => get('/user/verification/status')

// 获取紧急联系人列表
export const getEmergencyContacts = () => get('/user/emergency-contacts')

// 添加紧急联系人
export const addEmergencyContact = (data) => post('/user/emergency-contacts', data)

// 删除紧急联系人
export const deleteEmergencyContact = (id) => del(`/user/emergency-contacts/${id}`)

// 发送紧急求助
export const sendEmergencyHelp = (data) => post('/user/emergency/help', data)
