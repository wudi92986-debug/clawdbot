/**
 * 用户状态管理
 */
import { defineStore } from 'pinia'
import { wechatLogin, getUserProfile } from '@/api/user'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: uni.getStorageSync('token') || '',
    userInfo: uni.getStorageSync('userInfo') || null,
    isLoggedIn: !!uni.getStorageSync('token')
  }),
  
  getters: {
    // 是否是摄影师
    isPhotographer: (state) => state.userInfo?.userType === 2,
    // 是否实名认证
    isVerified: (state) => state.userInfo?.isVerified,
    // 是否学生认证
    isStudentVerified: (state) => state.userInfo?.isStudentVerified
  },
  
  actions: {
    // 检查登录状态
    checkLogin() {
      const token = uni.getStorageSync('token')
      const userInfo = uni.getStorageSync('userInfo')
      if (token && userInfo) {
        this.token = token
        this.userInfo = userInfo
        this.isLoggedIn = true
      }
    },
    
    // 微信登录
    async login() {
      return new Promise((resolve, reject) => {
        uni.login({
          provider: 'weixin',
          success: async (loginRes) => {
            try {
              const res = await wechatLogin({ code: loginRes.code })
              
              // 保存Token和用户信息
              this.token = res.accessToken
              this.userInfo = res.userInfo
              this.isLoggedIn = true
              
              uni.setStorageSync('token', res.accessToken)
              uni.setStorageSync('refreshToken', res.refreshToken)
              uni.setStorageSync('userInfo', res.userInfo)
              
              resolve(res)
            } catch (err) {
              reject(err)
            }
          },
          fail: (err) => {
            reject(err)
          }
        })
      })
    },
    
    // 获取用户信息
    async fetchUserProfile() {
      try {
        const userInfo = await getUserProfile()
        this.userInfo = userInfo
        uni.setStorageSync('userInfo', userInfo)
        return userInfo
      } catch (err) {
        console.error('获取用户信息失败', err)
        throw err
      }
    },
    
    // 更新用户信息
    updateUserInfo(data) {
      this.userInfo = { ...this.userInfo, ...data }
      uni.setStorageSync('userInfo', this.userInfo)
    },
    
    // 退出登录
    logout() {
      this.token = ''
      this.userInfo = null
      this.isLoggedIn = false
      
      uni.removeStorageSync('token')
      uni.removeStorageSync('refreshToken')
      uni.removeStorageSync('userInfo')
    }
  }
})
