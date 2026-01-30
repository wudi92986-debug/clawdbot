<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const form = reactive({
  phone: '',
  code: '',
})

const countdown = ref(0)
let timer: any = null

// 发送验证码
const sendCode = () => {
  if (!form.phone || !/^1\d{10}$/.test(form.phone)) {
    showToast('请输入正确的手机号')
    return
  }

  countdown.value = 60
  timer = setInterval(() => {
    countdown.value--
    if (countdown.value <= 0) {
      clearInterval(timer)
    }
  }, 1000)

  showToast('验证码已发送')
}

// 登录
const handleLogin = () => {
  if (!form.phone) {
    showToast('请输入手机号')
    return
  }
  if (!form.code) {
    showToast('请输入验证码')
    return
  }

  // 模拟登录
  localStorage.setItem('token', 'mock-token')
  showToast('登录成功')
  router.replace('/')
}

// 微信登录
const handleWechatLogin = () => {
  showToast('微信登录开发中...')
}
</script>

<template>
  <div class="login-page">
    <!-- Logo -->
    <div class="logo">
      <span class="logo-icon">🐾</span>
      <h1>宠爱天堂</h1>
      <p>让爱延续，让思念永恒</p>
    </div>

    <!-- 登录表单 -->
    <div class="login-form">
      <van-cell-group inset>
        <van-field
          v-model="form.phone"
          type="tel"
          placeholder="请输入手机号"
          maxlength="11"
        >
          <template #left-icon>
            <van-icon name="phone-o" />
          </template>
        </van-field>
        <van-field
          v-model="form.code"
          type="number"
          placeholder="请输入验证码"
          maxlength="6"
        >
          <template #left-icon>
            <van-icon name="shield-o" />
          </template>
          <template #button>
            <van-button
              size="small"
              type="primary"
              :disabled="countdown > 0"
              @click="sendCode"
            >
              {{ countdown > 0 ? `${countdown}s` : '获取验证码' }}
            </van-button>
          </template>
        </van-field>
      </van-cell-group>

      <van-button type="primary" round block class="login-btn" @click="handleLogin">
        登 录
      </van-button>

      <div class="divider">
        <span>其他登录方式</span>
      </div>

      <div class="other-login">
        <div class="login-icon" @click="handleWechatLogin">
          <van-icon name="wechat" color="#07C160" size="32" />
          <span>微信登录</span>
        </div>
      </div>
    </div>

    <!-- 协议 -->
    <div class="agreement">
      登录即表示同意
      <a href="#">《用户协议》</a>
      和
      <a href="#">《隐私政策》</a>
    </div>
  </div>
</template>

<script lang="ts">
import { showToast } from 'vant'
export default {
  setup() {
    return { showToast }
  }
}
</script>

<style lang="scss" scoped>
.login-page {
  min-height: 100vh;
  background: linear-gradient(180deg, var(--color-primary-lighter) 0%, var(--bg-color) 40%);
  padding: 60px 16px;
  display: flex;
  flex-direction: column;
}

.logo {
  text-align: center;
  margin-bottom: 40px;

  .logo-icon {
    font-size: 64px;
    display: block;
    margin-bottom: 12px;
  }

  h1 {
    font-size: 28px;
    color: var(--color-primary);
    margin: 0 0 8px;
  }

  p {
    font-size: 14px;
    color: var(--text-color-secondary);
    margin: 0;
  }
}

.login-form {
  flex: 1;

  .login-btn {
    margin-top: 24px;
  }
}

.divider {
  margin: 32px 0;
  text-align: center;
  position: relative;

  &::before,
  &::after {
    content: '';
    position: absolute;
    top: 50%;
    width: 30%;
    height: 1px;
    background-color: var(--border-color);
  }

  &::before {
    left: 0;
  }

  &::after {
    right: 0;
  }

  span {
    font-size: 12px;
    color: var(--text-color-secondary);
    padding: 0 16px;
    background-color: var(--bg-color);
  }
}

.other-login {
  display: flex;
  justify-content: center;

  .login-icon {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 8px;

    span {
      font-size: 12px;
      color: var(--text-color-secondary);
    }
  }
}

.agreement {
  text-align: center;
  font-size: 12px;
  color: var(--text-color-secondary);
  margin-top: auto;

  a {
    color: var(--color-primary);
    text-decoration: none;
  }
}
</style>
