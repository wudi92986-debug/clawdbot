<script setup lang="ts">
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'

defineProps<{
  collapse: boolean
}>()

const router = useRouter()
const route = useRoute()

// 菜单配置
const menuItems = [
  { 
    index: '/dashboard', 
    title: '工作台', 
    icon: 'HomeFilled' 
  },
  {
    index: '/order',
    title: '订单中心',
    icon: 'Document',
    children: [
      { index: '/order/pending', title: '待处理订单' },
      { index: '/order/list', title: '全部订单' },
    ],
  },
  { 
    index: '/customer', 
    title: '客户管理', 
    icon: 'User' 
  },
  { 
    index: '/pet', 
    title: '宠物档案', 
    icon: 'PriceTag' 
  },
  { 
    index: '/service', 
    title: '服务套餐', 
    icon: 'Box' 
  },
  { 
    index: '/ceremony', 
    title: '告别仪式', 
    icon: 'MagicStick' 
  },
  { 
    index: '/cremation', 
    title: '火化管理', 
    icon: 'Sunrise' 
  },
  { 
    index: '/storage', 
    title: '骨灰寄存', 
    icon: 'Location' 
  },
  { 
    index: '/memorial', 
    title: '纪念馆', 
    icon: 'Picture' 
  },
  { 
    index: '/shop', 
    title: '纪念商城', 
    icon: 'Present' 
  },
  { 
    index: '/statistics', 
    title: '数据统计', 
    icon: 'DataAnalysis' 
  },
  {
    index: '/system',
    title: '系统设置',
    icon: 'Setting',
    children: [
      { index: '/system/settings', title: '基础设置' },
      { index: '/system/staff', title: '员工管理' },
      { index: '/system/role', title: '角色权限' },
    ],
  },
]

// 当前激活的菜单
const activeMenu = computed(() => {
  return route.path
})

// 默认展开的子菜单
const defaultOpeneds = computed(() => {
  const path = route.path
  const parent = menuItems.find((item) =>
    item.children?.some((child) => path.startsWith(child.index))
  )
  return parent ? [parent.index] : []
})

const handleSelect = (index: string) => {
  router.push(index)
}
</script>

<template>
  <div class="sidebar-container">
    <!-- Logo -->
    <div class="logo">
      <span class="logo-icon">🐾</span>
      <transition name="fade">
        <span v-if="!collapse" class="logo-text">宠爱天堂</span>
      </transition>
    </div>

    <!-- 菜单 -->
    <el-scrollbar class="menu-scrollbar">
      <el-menu
        :default-active="activeMenu"
        :default-openeds="defaultOpeneds"
        :collapse="collapse"
        :collapse-transition="false"
        background-color="#2C2520"
        text-color="#C5B49A"
        active-text-color="#FFFFFF"
        @select="handleSelect"
      >
        <template v-for="item in menuItems" :key="item.index">
          <!-- 有子菜单 -->
          <el-sub-menu v-if="item.children" :index="item.index">
            <template #title>
              <el-icon>
                <component :is="item.icon" />
              </el-icon>
              <span>{{ item.title }}</span>
            </template>
            <el-menu-item
              v-for="child in item.children"
              :key="child.index"
              :index="child.index"
            >
              {{ child.title }}
            </el-menu-item>
          </el-sub-menu>

          <!-- 无子菜单 -->
          <el-menu-item v-else :index="item.index">
            <el-icon>
              <component :is="item.icon" />
            </el-icon>
            <template #title>{{ item.title }}</template>
          </el-menu-item>
        </template>
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<style lang="scss" scoped>
.sidebar-container {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 var(--spacing-md);
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);

  .logo-icon {
    font-size: 28px;
  }

  .logo-text {
    margin-left: var(--spacing-sm);
    font-size: 18px;
    font-weight: 600;
    color: #fff;
    white-space: nowrap;
  }
}

.menu-scrollbar {
  flex: 1;
  overflow: hidden;

  :deep(.el-scrollbar__view) {
    height: 100%;
  }
}

:deep(.el-menu) {
  border-right: none;
  padding: var(--spacing-sm) 0;

  .el-menu-item,
  .el-sub-menu__title {
    height: 48px;
    line-height: 48px;
    margin: 4px 8px;
    border-radius: var(--radius-md);

    &:hover {
      background-color: rgba(255, 255, 255, 0.1) !important;
    }
  }

  .el-menu-item.is-active {
    background: linear-gradient(90deg, var(--color-primary), var(--color-primary-light)) !important;
    color: #fff !important;
  }

  .el-sub-menu.is-opened > .el-sub-menu__title {
    color: #fff;
  }

  .el-sub-menu .el-menu-item {
    padding-left: 52px !important;
    min-width: auto;
  }
}

// 收起状态
:deep(.el-menu--collapse) {
  .el-menu-item,
  .el-sub-menu__title {
    padding: 0 !important;
    text-align: center;
  }

  .el-sub-menu__icon-arrow {
    display: none;
  }
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
