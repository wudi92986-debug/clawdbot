<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import Sidebar from './components/Sidebar.vue'
import Header from './components/Header.vue'

const router = useRouter()
const route = useRoute()

const isCollapse = ref(false)

const toggleSidebar = () => {
  isCollapse.value = !isCollapse.value
}

// 面包屑
const breadcrumbs = computed(() => {
  const matched = route.matched.filter((item) => item.meta?.title)
  return matched.map((item) => ({
    title: item.meta?.title as string,
    path: item.path,
  }))
})
</script>

<template>
  <div class="layout-container">
    <!-- 侧边栏 -->
    <aside class="sidebar" :class="{ collapsed: isCollapse }">
      <Sidebar :collapse="isCollapse" />
    </aside>

    <!-- 主内容区 -->
    <div class="main-container" :class="{ expanded: isCollapse }">
      <!-- 顶部导航 -->
      <Header 
        :collapse="isCollapse" 
        :breadcrumbs="breadcrumbs"
        @toggle="toggleSidebar" 
      />

      <!-- 内容区 -->
      <main class="main-content">
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </main>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.layout-container {
  display: flex;
  height: 100vh;
  background-color: var(--bg-color-page);
}

.sidebar {
  width: 240px;
  height: 100vh;
  position: fixed;
  left: 0;
  top: 0;
  z-index: 100;
  transition: width 0.3s ease;
  background-color: #2C2520;
  box-shadow: var(--shadow-md);

  &.collapsed {
    width: 64px;
  }
}

.main-container {
  flex: 1;
  margin-left: 240px;
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  transition: margin-left 0.3s ease;

  &.expanded {
    margin-left: 64px;
  }
}

.main-content {
  flex: 1;
  padding: var(--spacing-lg);
  overflow-y: auto;
}

// 页面切换动画
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
