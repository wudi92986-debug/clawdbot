<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const memorials = ref([
  { id: 1, name: '豆豆', emoji: '🐕', lifespan: '2018 - 2025', candles: 258, flowers: 1234, visits: 3567 },
  { id: 2, name: '咪咪', emoji: '🐱', lifespan: '2020 - 2026', candles: 89, flowers: 567, visits: 1234 },
  { id: 3, name: '球球', emoji: '🐕', lifespan: '2015 - 2026', candles: 456, flowers: 2345, visits: 5678 },
  { id: 4, name: '小白', emoji: '🐰', lifespan: '2019 - 2025', candles: 123, flowers: 890, visits: 2134 },
  { id: 5, name: '旺财', emoji: '🐕', lifespan: '2017 - 2026', candles: 67, flowers: 345, visits: 876 },
  { id: 6, name: '橘子', emoji: '🐱', lifespan: '2018 - 2025', candles: 189, flowers: 678, visits: 1567 },
])

const formatCount = (count: number) => {
  if (count >= 1000) return (count / 1000).toFixed(1) + 'k'
  return count.toString()
}

const goDetail = (id: number) => {
  router.push(`/memorial/${id}`)
}
</script>

<template>
  <div class="memorial-page page">
    <!-- 顶部 -->
    <div class="page-header">
      <h1>云纪念馆</h1>
      <p>让思念永存，让爱延续</p>
    </div>

    <!-- 搜索 -->
    <div class="search-bar">
      <van-search placeholder="搜索纪念馆" shape="round" />
    </div>

    <!-- 纪念馆列表 -->
    <div class="memorial-grid">
      <div
        v-for="item in memorials"
        :key="item.id"
        class="memorial-card"
        @click="goDetail(item.id)"
      >
        <div class="memorial-avatar">
          <span class="emoji">{{ item.emoji }}</span>
        </div>
        <div class="memorial-name">{{ item.name }}</div>
        <div class="memorial-lifespan">{{ item.lifespan }}</div>
        <div class="memorial-stats">
          <span>🕯️ {{ formatCount(item.candles) }}</span>
          <span>🌸 {{ formatCount(item.flowers) }}</span>
        </div>
      </div>
    </div>

    <!-- 创建纪念馆入口 -->
    <div class="create-entry">
      <van-button type="primary" round block icon="plus">
        为爱宠创建纪念馆
      </van-button>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.memorial-page {
  padding-bottom: 80px;
}

.page-header {
  background: linear-gradient(135deg, var(--color-primary), var(--color-primary-light));
  color: #fff;
  padding: 24px 16px;
  padding-top: calc(24px + env(safe-area-inset-top));
  text-align: center;

  h1 {
    font-size: 22px;
    margin: 0 0 4px;
  }

  p {
    font-size: 14px;
    opacity: 0.9;
    margin: 0;
  }
}

.search-bar {
  margin: -20px 16px 16px;
  position: relative;
  z-index: 1;

  :deep(.van-search) {
    padding: 0;

    .van-search__content {
      background-color: #fff;
      box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
    }
  }
}

.memorial-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
  padding: 0 16px;
}

.memorial-card {
  background-color: #fff;
  border-radius: 12px;
  padding: 16px;
  text-align: center;
  transition: transform 0.2s;

  &:active {
    transform: scale(0.98);
  }

  .memorial-avatar {
    width: 64px;
    height: 64px;
    margin: 0 auto 12px;
    border-radius: 50%;
    background: linear-gradient(135deg, var(--color-primary-lighter), var(--color-primary-light));
    display: flex;
    align-items: center;
    justify-content: center;

    .emoji {
      font-size: 32px;
    }
  }

  .memorial-name {
    font-size: 16px;
    font-weight: 600;
    color: var(--text-color);
    margin-bottom: 4px;
  }

  .memorial-lifespan {
    font-size: 12px;
    color: var(--text-color-secondary);
    margin-bottom: 12px;
  }

  .memorial-stats {
    display: flex;
    justify-content: center;
    gap: 16px;
    font-size: 12px;
    color: var(--text-color-secondary);
  }
}

.create-entry {
  position: fixed;
  bottom: 70px;
  left: 16px;
  right: 16px;
  z-index: 10;
}
</style>
