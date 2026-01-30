<script setup lang="ts">
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const memorialId = route.params.id

// 纪念馆数据
const memorial = ref({
  id: 1,
  name: '豆豆',
  emoji: '🐕',
  lifespan: '2018.03.15 - 2025.12.20',
  age: '7岁',
  bio: '你永远是我最好的朋友，感谢你陪伴我走过的每一个春夏秋冬',
  candleCount: 258,
  flowerCount: 1234,
  visitCount: 3567,
})

// 相册
const photos = ref([
  { id: 1, url: '', date: '2025.12' },
  { id: 2, url: '', date: '2024.06' },
  { id: 3, url: '', date: '2023.01' },
  { id: 4, url: '', date: '2022.08' },
  { id: 5, url: '', date: '2021.03' },
  { id: 6, url: '', date: '2020.05' },
])

// 留言
const messages = ref([
  { id: 1, author: '妈妈', content: '豆豆，今天又梦到你了，你在天堂还好吗？要记得吃饭，不要挑食...', time: '2天前', candles: 12 },
  { id: 2, author: '姐姐', content: '小豆豆，天堂里要开心哦，我们都很想你 💕', time: '5天前', candles: 8 },
  { id: 3, author: '小姨', content: '豆豆一路走好，来世我们还做一家人 🌈', time: '1周前', candles: 5 },
])

// 点烛弹窗
const showCandlePopup = ref(false)
const showFlowerPopup = ref(false)
const showMessagePopup = ref(false)

const messageContent = ref('')

// 点烛
const handleLightCandle = () => {
  showCandlePopup.value = true
}

// 献花
const handleOfferFlower = () => {
  showFlowerPopup.value = true
}

// 写留言
const handleWriteMessage = () => {
  showMessagePopup.value = true
}

const submitMessage = () => {
  if (messageContent.value.trim()) {
    showToast('留言成功')
    showMessagePopup.value = false
    messageContent.value = ''
  }
}

const confirmCandle = () => {
  memorial.value.candleCount++
  showCandlePopup.value = false
  showToast('已为' + memorial.value.name + '点亮一盏烛')
}

const confirmFlower = () => {
  memorial.value.flowerCount++
  showFlowerPopup.value = false
  showToast('已为' + memorial.value.name + '献上一束花')
}

// 格式化数字
const formatCount = (count: number) => {
  if (count >= 1000) return (count / 1000).toFixed(1) + 'k'
  return count.toString()
}

// 返回
const goBack = () => {
  router.back()
}
</script>

<template>
  <div class="memorial-detail-page">
    <!-- 顶部背景 -->
    <div class="header-bg">
      <van-nav-bar
        left-arrow
        @click-left="goBack"
        :border="false"
        style="background: transparent"
      >
        <template #right>
          <van-icon name="share-o" color="#fff" size="20" />
        </template>
      </van-nav-bar>

      <!-- 宠物信息 -->
      <div class="pet-info">
        <div class="pet-avatar">
          <span class="emoji">{{ memorial.emoji }}</span>
        </div>
        <h1 class="pet-name">{{ memorial.name }}</h1>
        <p class="pet-lifespan">{{ memorial.lifespan }}</p>
        <p class="pet-age">{{ memorial.age }}</p>
        <p class="pet-bio">"{{ memorial.bio }}"</p>
      </div>

      <!-- 统计 -->
      <div class="stats-bar">
        <div class="stat-item">
          <span class="stat-icon">🕯️</span>
          <span class="stat-value">{{ formatCount(memorial.candleCount) }}</span>
          <span class="stat-label">点烛</span>
        </div>
        <div class="stat-item">
          <span class="stat-icon">🌸</span>
          <span class="stat-value">{{ formatCount(memorial.flowerCount) }}</span>
          <span class="stat-label">献花</span>
        </div>
        <div class="stat-item">
          <span class="stat-icon">👁️</span>
          <span class="stat-value">{{ formatCount(memorial.visitCount) }}</span>
          <span class="stat-label">浏览</span>
        </div>
      </div>
    </div>

    <!-- 内容区 -->
    <div class="content">
      <!-- 时光相册 -->
      <div class="section">
        <div class="section-header">
          <span class="section-title">📷 时光相册</span>
          <span class="section-more">查看全部 (36) ></span>
        </div>
        <div class="photo-grid">
          <div v-for="photo in photos" :key="photo.id" class="photo-item">
            <div class="photo-placeholder">📸</div>
            <span class="photo-date">{{ photo.date }}</span>
          </div>
        </div>
      </div>

      <!-- 思念留言 -->
      <div class="section">
        <div class="section-header">
          <span class="section-title">💬 思念留言</span>
          <span class="section-more">查看全部 (28) ></span>
        </div>
        <div class="message-list">
          <div v-for="msg in messages" :key="msg.id" class="message-item">
            <div class="message-header">
              <span class="message-author">{{ msg.author }}</span>
              <span class="message-time">{{ msg.time }}</span>
            </div>
            <p class="message-content">{{ msg.content }}</p>
            <div class="message-footer">
              <span>🕯️ {{ msg.candles }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 底部操作栏 -->
    <div class="action-bar">
      <div class="action-btn" @click="handleLightCandle">
        <span class="btn-icon">🕯️</span>
        <span class="btn-text">点烛</span>
      </div>
      <div class="action-btn" @click="handleOfferFlower">
        <span class="btn-icon">🌸</span>
        <span class="btn-text">献花</span>
      </div>
      <div class="action-btn primary" @click="handleWriteMessage">
        <span class="btn-text">写下思念</span>
      </div>
    </div>

    <!-- 点烛弹窗 -->
    <van-popup v-model:show="showCandlePopup" round position="bottom" :style="{ height: '50%' }">
      <div class="popup-content">
        <h3>为{{ memorial.name }}点亮一盏烛</h3>
        <div class="candle-display">🕯️</div>
        <p class="blessing">愿你在天堂永远快乐</p>
        <van-button type="primary" round block @click="confirmCandle">点 亮</van-button>
      </div>
    </van-popup>

    <!-- 献花弹窗 -->
    <van-popup v-model:show="showFlowerPopup" round position="bottom" :style="{ height: '50%' }">
      <div class="popup-content">
        <h3>为{{ memorial.name }}献上一束花</h3>
        <div class="flower-display">🌸</div>
        <p class="blessing">愿你在天堂被温柔以待</p>
        <van-button type="primary" round block @click="confirmFlower">献 花</van-button>
      </div>
    </van-popup>

    <!-- 留言弹窗 -->
    <van-popup v-model:show="showMessagePopup" round position="bottom" :style="{ height: '60%' }">
      <div class="popup-content">
        <h3>写下对{{ memorial.name }}的思念</h3>
        <van-field
          v-model="messageContent"
          type="textarea"
          placeholder="写下你想对TA说的话..."
          rows="5"
          maxlength="500"
          show-word-limit
        />
        <van-button type="primary" round block @click="submitMessage" style="margin-top: 20px">
          发 送
        </van-button>
      </div>
    </van-popup>
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
.memorial-detail-page {
  min-height: 100vh;
  background-color: var(--bg-color);
  padding-bottom: 80px;
}

.header-bg {
  background: linear-gradient(180deg, var(--color-primary) 0%, var(--color-primary-light) 100%);
  padding-bottom: 24px;

  :deep(.van-nav-bar) {
    .van-nav-bar__arrow {
      color: #fff;
    }
  }
}

.pet-info {
  text-align: center;
  color: #fff;
  padding: 0 24px;

  .pet-avatar {
    width: 100px;
    height: 100px;
    margin: 0 auto 16px;
    border-radius: 50%;
    background-color: rgba(255, 255, 255, 0.2);
    display: flex;
    align-items: center;
    justify-content: center;

    .emoji {
      font-size: 48px;
    }
  }

  .pet-name {
    font-size: 24px;
    font-weight: 700;
    margin: 0 0 4px;
  }

  .pet-lifespan {
    font-size: 14px;
    opacity: 0.9;
    margin: 0 0 4px;
  }

  .pet-age {
    font-size: 12px;
    opacity: 0.8;
    margin: 0 0 16px;
  }

  .pet-bio {
    font-size: 14px;
    font-style: italic;
    opacity: 0.9;
    margin: 0;
    line-height: 1.6;
  }
}

.stats-bar {
  display: flex;
  justify-content: center;
  gap: 40px;
  margin-top: 24px;
  padding: 16px;
  background-color: rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  margin: 24px 16px 0;

  .stat-item {
    text-align: center;
    color: #fff;

    .stat-icon {
      font-size: 20px;
      display: block;
      margin-bottom: 4px;
    }

    .stat-value {
      font-size: 18px;
      font-weight: 600;
      display: block;
    }

    .stat-label {
      font-size: 12px;
      opacity: 0.8;
    }
  }
}

.content {
  padding: 16px;
  margin-top: -12px;
  background-color: var(--bg-color);
  border-radius: 16px 16px 0 0;
  position: relative;
}

.section {
  margin-bottom: 24px;

  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 12px;

    .section-title {
      font-size: 16px;
      font-weight: 600;
    }

    .section-more {
      font-size: 12px;
      color: var(--text-color-secondary);
    }
  }
}

.photo-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 8px;

  .photo-item {
    aspect-ratio: 1;
    background-color: #fff;
    border-radius: 8px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;

    .photo-placeholder {
      font-size: 32px;
      opacity: 0.5;
    }

    .photo-date {
      font-size: 10px;
      color: var(--text-color-secondary);
      margin-top: 4px;
    }
  }
}

.message-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.message-item {
  background-color: #fff;
  border-radius: 12px;
  padding: 16px;

  .message-header {
    display: flex;
    justify-content: space-between;
    margin-bottom: 8px;

    .message-author {
      font-weight: 500;
      color: var(--text-color);
    }

    .message-time {
      font-size: 12px;
      color: var(--text-color-secondary);
    }
  }

  .message-content {
    font-size: 14px;
    color: var(--text-color);
    line-height: 1.6;
    margin: 0 0 12px;
  }

  .message-footer {
    font-size: 12px;
    color: var(--text-color-secondary);
  }
}

.action-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  gap: 12px;
  padding: 12px 16px;
  padding-bottom: calc(12px + env(safe-area-inset-bottom));
  background-color: #fff;
  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.05);

  .action-btn {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 4px;
    padding: 12px;
    border-radius: 24px;
    background-color: var(--bg-color);
    font-size: 14px;

    &.primary {
      background-color: var(--color-primary);
      color: #fff;
    }

    .btn-icon {
      font-size: 18px;
    }
  }
}

.popup-content {
  padding: 24px;
  text-align: center;

  h3 {
    font-size: 18px;
    margin: 0 0 24px;
  }

  .candle-display,
  .flower-display {
    font-size: 80px;
    margin-bottom: 16px;
  }

  .blessing {
    color: var(--text-color-secondary);
    margin-bottom: 24px;
  }
}
</style>
