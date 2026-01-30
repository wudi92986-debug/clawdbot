<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'

defineProps<{
  collapse: boolean
  breadcrumbs: Array<{ title: string; path: string }>
}>()

const emit = defineEmits<{
  toggle: []
}>()

const router = useRouter()

// 用户信息
const userInfo = ref({
  name: '张管理员',
  avatar: '',
})

// 消息数量
const messageCount = ref(3)
const notificationCount = ref(5)

// 用户菜单命令
const handleCommand = (command: string) => {
  switch (command) {
    case 'profile':
      router.push('/system/profile')
      break
    case 'settings':
      router.push('/system/settings')
      break
    case 'logout':
      handleLogout()
      break
  }
}

// 退出登录
const handleLogout = () => {
  ElMessageBox.confirm('确定要退出登录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  })
    .then(() => {
      localStorage.removeItem('token')
      router.push('/login')
    })
    .catch(() => {})
}
</script>

<template>
  <header class="header">
    <div class="header-left">
      <!-- 折叠按钮 -->
      <div class="toggle-btn" @click="emit('toggle')">
        <el-icon :size="20">
          <Fold v-if="!collapse" />
          <Expand v-else />
        </el-icon>
      </div>

      <!-- 面包屑 -->
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/' }">
          <el-icon><HomeFilled /></el-icon>
        </el-breadcrumb-item>
        <el-breadcrumb-item
          v-for="(item, index) in breadcrumbs"
          :key="index"
          :to="index < breadcrumbs.length - 1 ? { path: item.path } : undefined"
        >
          {{ item.title }}
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <div class="header-right">
      <!-- 消息 -->
      <el-badge :value="notificationCount" :max="99" class="header-icon">
        <el-icon :size="20"><Bell /></el-icon>
      </el-badge>

      <!-- 邮件 -->
      <el-badge :value="messageCount" :max="99" class="header-icon">
        <el-icon :size="20"><Message /></el-icon>
      </el-badge>

      <!-- 用户下拉菜单 -->
      <el-dropdown @command="handleCommand">
        <div class="user-info">
          <el-avatar :size="32" :src="userInfo.avatar">
            {{ userInfo.name.charAt(0) }}
          </el-avatar>
          <span class="user-name">{{ userInfo.name }}</span>
          <el-icon><ArrowDown /></el-icon>
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="profile">
              <el-icon><User /></el-icon>
              个人中心
            </el-dropdown-item>
            <el-dropdown-item command="settings">
              <el-icon><Setting /></el-icon>
              系统设置
            </el-dropdown-item>
            <el-dropdown-item divided command="logout">
              <el-icon><SwitchButton /></el-icon>
              退出登录
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </header>
</template>

<style lang="scss" scoped>
.header {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 var(--spacing-lg);
  background-color: var(--bg-color-card);
  box-shadow: var(--shadow-sm);
  position: sticky;
  top: 0;
  z-index: 99;
}

.header-left {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
}

.toggle-btn {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: var(--radius-md);
  cursor: pointer;
  transition: background-color 0.3s ease;

  &:hover {
    background-color: var(--bg-color-page);
  }
}

.header-right {
  display: flex;
  align-items: center;
  gap: var(--spacing-lg);
}

.header-icon {
  cursor: pointer;
  padding: var(--spacing-sm);
  border-radius: var(--radius-md);
  transition: background-color 0.3s ease;

  &:hover {
    background-color: var(--bg-color-page);
  }
}

.user-info {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  cursor: pointer;
  padding: var(--spacing-xs) var(--spacing-sm);
  border-radius: var(--radius-md);
  transition: background-color 0.3s ease;

  &:hover {
    background-color: var(--bg-color-page);
  }

  .user-name {
    font-size: 14px;
    color: var(--text-color-primary);
  }
}

:deep(.el-breadcrumb) {
  .el-breadcrumb__item {
    .el-breadcrumb__inner {
      color: var(--text-color-secondary);

      &.is-link:hover {
        color: var(--color-primary);
      }
    }

    &:last-child .el-breadcrumb__inner {
      color: var(--text-color-primary);
      font-weight: 500;
    }
  }
}
</style>
