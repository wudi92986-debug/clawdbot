<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login } from '@/api/auth'

const router = useRouter()
const loading = ref(false)

const formData = reactive({
  username: '',
  password: ''
})

const formRef = ref()

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleLogin = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    loading.value = true
    
    const res = await login({
      username: formData.username,
      password: formData.password
    })
    
    // 保存 token
    localStorage.setItem('token', res.data.token)
    localStorage.setItem('username', res.data.username)
    localStorage.setItem('nickname', res.data.nickname || res.data.username)
    
    ElMessage.success('登录成功')
    router.push('/')
  } catch (error: any) {
    console.error('登录失败:', error)
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="login-container">
    <div class="login-bg">
      <div class="bg-shape shape-1"></div>
      <div class="bg-shape shape-2"></div>
      <div class="bg-shape shape-3"></div>
    </div>
    
    <div class="login-card">
      <div class="login-header">
        <div class="logo">
          <span class="logo-icon">🐾</span>
        </div>
        <h1 class="title">宠物殡葬管理系统</h1>
        <p class="subtitle">用爱陪伴 · 温暖告别</p>
      </div>
      
      <el-form 
        ref="formRef" 
        :model="formData" 
        :rules="rules" 
        class="login-form"
        @keyup.enter="handleLogin"
      >
        <el-form-item prop="username">
          <el-input
            v-model="formData.username"
            placeholder="请输入用户名"
            size="large"
            prefix-icon="User"
          />
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input
            v-model="formData.password"
            type="password"
            placeholder="请输入密码"
            size="large"
            prefix-icon="Lock"
            show-password
          />
        </el-form-item>
        
        <el-form-item>
          <el-button
            type="primary"
            size="large"
            class="login-btn"
            :loading="loading"
            @click="handleLogin"
          >
            {{ loading ? '登录中...' : '登 录' }}
          </el-button>
        </el-form-item>
      </el-form>
      
      <div class="login-footer">
        <p>默认账号: admin / 123456</p>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  position: relative;
  overflow: hidden;
}

.login-bg {
  position: absolute;
  inset: 0;
  overflow: hidden;
  
  .bg-shape {
    position: absolute;
    border-radius: 50%;
    opacity: 0.1;
    background: white;
  }
  
  .shape-1 {
    width: 400px;
    height: 400px;
    top: -100px;
    left: -100px;
    animation: float 8s ease-in-out infinite;
  }
  
  .shape-2 {
    width: 300px;
    height: 300px;
    bottom: -50px;
    right: -50px;
    animation: float 6s ease-in-out infinite reverse;
  }
  
  .shape-3 {
    width: 200px;
    height: 200px;
    top: 50%;
    left: 60%;
    animation: float 10s ease-in-out infinite;
  }
}

@keyframes float {
  0%, 100% {
    transform: translateY(0) rotate(0deg);
  }
  50% {
    transform: translateY(-20px) rotate(5deg);
  }
}

.login-card {
  width: 400px;
  padding: 40px;
  background: white;
  border-radius: 20px;
  box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.25);
  position: relative;
  z-index: 1;
}

.login-header {
  text-align: center;
  margin-bottom: 40px;
  
  .logo {
    width: 80px;
    height: 80px;
    margin: 0 auto 20px;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border-radius: 20px;
    display: flex;
    align-items: center;
    justify-content: center;
    
    .logo-icon {
      font-size: 40px;
    }
  }
  
  .title {
    font-size: 24px;
    font-weight: 600;
    color: #1a1a2e;
    margin: 0 0 8px;
  }
  
  .subtitle {
    font-size: 14px;
    color: #6b7280;
    margin: 0;
  }
}

.login-form {
  :deep(.el-input__wrapper) {
    border-radius: 10px;
    box-shadow: 0 0 0 1px #e5e7eb;
    
    &:hover, &.is-focus {
      box-shadow: 0 0 0 1px #667eea;
    }
  }
  
  :deep(.el-form-item) {
    margin-bottom: 20px;
  }
}

.login-btn {
  width: 100%;
  height: 48px;
  border-radius: 10px;
  font-size: 16px;
  font-weight: 500;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  
  &:hover {
    background: linear-gradient(135deg, #5a6fd6 0%, #6a4190 100%);
  }
}

.login-footer {
  text-align: center;
  margin-top: 20px;
  
  p {
    font-size: 12px;
    color: #9ca3af;
    margin: 0;
  }
}
</style>
