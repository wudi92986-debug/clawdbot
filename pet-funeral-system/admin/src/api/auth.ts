import { request } from '@/utils/request'

export interface LoginParams {
  username: string
  password: string
}

export interface LoginResult {
  data: {
    token: string
    username: string
    nickname: string
  }
}

// 登录
export const login = (data: LoginParams) => {
  return request.post<LoginResult>('/auth/login', data)
}

// 获取当前用户信息
export const getCurrentUser = () => {
  return request.get('/auth/me')
}

// 初始化管理员
export const initAdmin = () => {
  return request.post('/auth/init')
}
