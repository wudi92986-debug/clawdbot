<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import type { FormInstance, FormRules } from 'element-plus'

const router = useRouter()

const formRef = ref<FormInstance>()
const loading = ref(false)

const form = reactive({
  username: '',
  password: '',
  captcha: '',
  remember: false,
})

const rules: FormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' },
  ],
  captcha: [
    { required: true, message: '请输入验证码', trigger: 'blur' },
  ],
}

// 验证码
const captchaCode = ref('A3K9')
const refreshCaptcha = () => {
  const chars = 'ABCDEFGHJKLMNPQRSTUVWXYZ23456789'
  let code = ''
  for (let i = 0; i < 4; i++) {
    code += chars.charAt(Math.floor(Math.random() * chars.length))
  }
  captchaCode.value = code
}

// 登录
const handleLogin = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      // 验证码校验
      if (form.captcha.toUpperCase() !== captchaCode.value) {
        ElMessage.error('验证码错误')
        refreshCaptcha()
        return
      }

      loading.value = true

      // 模拟登录请求
      setTimeout(() => {
        // 模拟登录成功
        localStorage.setItem('token', 'mock-token-' + Date.now())
        ElMessage.success('登录成功')
        router.push('/dashboard')
        loading.value = false
      }, 1000)
    }
  })
}
</script>

<template>
  <div class="login-container">
    <!-- 背景装饰 -->
    <div class="bg-decoration">
      <div class="circle circle-1"></div>
      <div class="circle circle-2"></div>
      <div class="circle circle-3"></div>
    </div>

    <!-- 登录卡片 -->
    <div class="login-card">
      <!-- Logo -->
      <div class="logo">
        <span class="logo-icon">🐾</span>
        <h1 class="logo-title">宠爱天堂</h1>
        <p class="logo-subtitle">宠物殡葬管理系统</p>
      </div>

      <!-- 登录表单 -->
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        class="login-form"
        @submit.prevent="handleLogin"
      >
        <el-form-item prop="username">
          <el-input
            v-model="form.username"
            placeholder="请输入用户名"
            size="large"
            :prefix-icon="User"
          />
        </el-form-item>

        <el-form-item prop="password">
          <el-input
            v-model="form.password"
            type="password"
            placeholder="请输入密码"
            size="large"
            :prefix-icon="Lock"
            show-password
          />
        </el-form-item>

        <el-form-item prop="captcha">
          <div class="captcha-row">
            <el-input
              v-model="form.captcha"
              placeholder="请输入验证码"
              size="large"
              :prefix-icon="Key"
              class="captcha-input"
            />
            <div class="captcha-code" @click="refreshCaptcha">
              {{ captchaCode }}
            </div>
          </div>
        </el-form-item>

        <el-form-item>
          <div class="form-options">
            <el-checkbox v-model="form.remember">记住我</el-checkbox>
            <a href="#" class="forgot-link">忘记密码?</a>
          </div>
        </el-form-item>

        <el-form-item>
          <el-button
            type="primary"
            size="large"
            :loading="loading"
            class="login-btn"
            native-type="submit"
          >
            {{ loading ? '登录中...' : '登 录' }}
          </el-button>
        </el-form-item>
      </el-form>

      <!-- 底部 -->
      <div class="login-footer">
        <p>遇到问题？<a href="#">联系管理员</a></p>
      </div>
    </div>

    <!-- 版权信息 -->
    <div class="copyright">
      © 2026 宠爱天堂 版权所有
    </div>
  </div>
</template>

<script lang="ts">
import { User, Lock, Key } from '@element-plus/icons-vue'
export default {
  components: { User, Lock, Key },
  data() {
    return { User, Lock, Key }
  }
}
</script>

<style lang="scss" scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #FAF8F5 0%, #F0EBE3 100%);
  position: relative;
  overflow: hidden;
}

// 背景装饰
.bg-decoration {
  position: absolute;
  inset: 0;
  pointer-events: none;
  overflow: hidden;

  .circle {
    position: absolute;
    border-radius: 50%;
    opacity: 0.1;
  }

  .circle-1 {
    width: 400px;
    height: 400px;
    background-color: var(--color-primary);
    top: -100px;
    right: -100px;
  }

  .circle-2 {
    width: 300px;
    height: 300px;
    background-color: var(--color-success);
    bottom: -50px;
    left: -50px;
  }

  .circle-3 {
    width: 200px;
    height: 200px;
    background-color: var(--color-warning);
    top: 50%;
    left: 10%;
  }
}

.login-card {
  width: 420px;
  background-color: #fff;
  border-radius: var(--radius-xl);
  box-shadow: var(--shadow-lg);
  padding: 48px 40px;
  position: relative;
  z-index: 1;
}

.logo {
  text-align: center;
  margin-bottom: 40px;

  .logo-icon {
    font-size: 48px;
    display: block;
    margin-bottom: var(--spacing-sm);
  }

  .logo-title {
    font-size: 28px;
    font-weight: 600;
    color: var(--color-primary);
    margin: 0 0 var(--spacing-xs);
  }

  .logo-subtitle {
    font-size: 14px;
    color: var(--text-color-secondary);
    margin: 0;
  }
}

.login-form {
  :deep(.el-input__wrapper) {
    border-radius: var(--radius-md);
    box-shadow: 0 0 0 1px var(--border-color) inset;

    &:hover,
    &.is-focus {
      box-shadow: 0 0 0 1px var(--color-primary) inset;
    }
  }
}

.captcha-row {
  display: flex;
  gap: var(--spacing-md);
  width: 100%;

  .captcha-input {
    flex: 1;
  }

  .captcha-code {
    width: 120px;
    height: 40px;
    display: flex;
    align-items: center;
    justify-content: center;
    background: linear-gradient(135deg, var(--color-primary-lighter), var(--color-primary-light));
    border-radius: var(--radius-md);
    font-size: 20px;
    font-weight: 700;
    letter-spacing: 4px;
    color: #fff;
    cursor: pointer;
    user-select: none;

    &:hover {
      opacity: 0.9;
    }
  }
}

.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;

  .forgot-link {
    color: var(--color-primary);
    text-decoration: none;
    font-size: 14px;

    &:hover {
      text-decoration: underline;
    }
  }
}

.login-btn {
  width: 100%;
  height: 44px;
  font-size: 16px;
  border-radius: var(--radius-md);
}

.login-footer {
  text-align: center;
  margin-top: var(--spacing-lg);
  padding-top: var(--spacing-lg);
  border-top: 1px solid var(--border-color-light);

  p {
    color: var(--text-color-secondary);
    font-size: 14px;
    margin: 0;

    a {
      color: var(--color-primary);
      text-decoration: none;

      &:hover {
        text-decoration: underline;
      }
    }
  }
}

.copyright {
  position: absolute;
  bottom: 24px;
  color: var(--text-color-secondary);
  font-size: 12px;
}
</style>
