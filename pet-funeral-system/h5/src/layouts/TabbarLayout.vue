<script setup lang="ts">
import { ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const active = ref(0)

// 监听路由变化，更新底部导航状态
watch(
  () => route.path,
  (path) => {
    if (path === '/') active.value = 0
    else if (path === '/service') active.value = 1
    else if (path === '/memorial') active.value = 2
    else if (path === '/user') active.value = 3
  },
  { immediate: true }
)

const handleTabChange = (index: number | string) => {
  const paths = ['/', '/service', '/memorial', '/user']
  router.push(paths[index as number])
}
</script>

<template>
  <div class="layout-container">
    <main class="main-content">
      <router-view v-slot="{ Component }">
        <transition name="fade" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
    </main>

    <van-tabbar v-model="active" @change="handleTabChange" active-color="#8B7355">
      <van-tabbar-item icon="wap-home-o">首页</van-tabbar-item>
      <van-tabbar-item icon="orders-o">服务</van-tabbar-item>
      <van-tabbar-item icon="photo-o">纪念馆</van-tabbar-item>
      <van-tabbar-item icon="user-o">我的</van-tabbar-item>
    </van-tabbar>
  </div>
</template>

<style lang="scss" scoped>
.layout-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.main-content {
  flex: 1;
  overflow-y: auto;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
