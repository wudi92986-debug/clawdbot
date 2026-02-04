/**
 * 网络请求封装
 */

const BASE_URL = 'http://localhost:8080/api/v1'

// 请求拦截器
const requestInterceptor = (config) => {
  const token = uni.getStorageSync('token')
  if (token) {
    config.header = {
      ...config.header,
      'Authorization': `Bearer ${token}`
    }
  }
  return config
}

// 响应拦截器
const responseInterceptor = (response) => {
  const { statusCode, data } = response
  
  if (statusCode === 200) {
    if (data.code === 200) {
      return data.data
    } else if (data.code === 401 || data.code === 40101 || data.code === 40102) {
      // Token过期，跳转登录
      uni.removeStorageSync('token')
      uni.removeStorageSync('userInfo')
      uni.showToast({
        title: '请重新登录',
        icon: 'none'
      })
      setTimeout(() => {
        uni.reLaunch({ url: '/pages/mine/index' })
      }, 1500)
      return Promise.reject(data)
    } else {
      uni.showToast({
        title: data.message || '请求失败',
        icon: 'none'
      })
      return Promise.reject(data)
    }
  } else {
    uni.showToast({
      title: '网络错误',
      icon: 'none'
    })
    return Promise.reject(response)
  }
}

// 请求方法
const request = (options) => {
  const config = requestInterceptor({
    url: BASE_URL + options.url,
    method: options.method || 'GET',
    data: options.data,
    header: {
      'Content-Type': 'application/json',
      ...options.header
    }
  })
  
  return new Promise((resolve, reject) => {
    uni.request({
      ...config,
      success: (res) => {
        responseInterceptor(res)
          .then(resolve)
          .catch(reject)
      },
      fail: (err) => {
        uni.showToast({
          title: '网络连接失败',
          icon: 'none'
        })
        reject(err)
      }
    })
  })
}

// 导出方法
export const get = (url, data) => request({ url, method: 'GET', data })
export const post = (url, data) => request({ url, method: 'POST', data })
export const put = (url, data) => request({ url, method: 'PUT', data })
export const del = (url, data) => request({ url, method: 'DELETE', data })

export default request
