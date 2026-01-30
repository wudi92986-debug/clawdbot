<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

// Banner
const banners = ref([
  { id: 1, image: '', title: '让爱延续，让思念永恒' },
])

// 服务入口
const services = ref([
  { id: 1, icon: '🚗', name: '上门接运', path: '/booking' },
  { id: 2, icon: '🕯️', name: '告别仪式', path: '/booking' },
  { id: 3, icon: '🔥', name: '火化服务', path: '/booking' },
  { id: 4, icon: '📍', name: '骨灰寄存', path: '/service' },
  { id: 5, icon: '🏛️', name: '纪念馆', path: '/memorial' },
  { id: 6, icon: '🎁', name: '纪念品商城', path: '/service' },
])

// 热门套餐
const packages = ref([
  {
    id: 1,
    name: '温馨告别套餐',
    icon: '🌸',
    description: '上门接运 + 遗容整理 + 告别仪式 + 单独火化 + 精品骨灰盒',
    price: 2980,
  },
  {
    id: 2,
    name: '尊享告别套餐',
    icon: '💫',
    description: '专车接运 + 高级美容 + VIP告别厅 + 单独火化 + 高端骨灰盒 + 纪念相册',
    price: 5980,
  },
  {
    id: 3,
    name: '基础告别套餐',
    icon: '🌿',
    description: '遗容整理 + 集体火化',
    price: 1280,
  },
])

// 跳转
const goTo = (path: string) => {
  router.push(path)
}
</script>

<template>
  <div class="home-page page">
    <!-- 顶部区域 -->
    <div class="header">
      <div class="location">
        <van-icon name="location-o" />
        <span>北京市朝阳区</span>
        <van-icon name="arrow-down" size="12" />
      </div>
    </div>

    <!-- Banner -->
    <div class="banner">
      <div class="banner-content">
        <div class="banner-icon">🐾</div>
        <h1 class="banner-title">宠爱天堂</h1>
        <p class="banner-subtitle">让爱延续，让思念永恒</p>
        <van-button type="primary" round size="small" @click="goTo('/booking')">
          立即预约服务
        </van-button>
      </div>
    </div>

    <!-- 服务入口 -->
    <div class="service-grid">
      <div
        v-for="service in services"
        :key="service.id"
        class="service-item"
        @click="goTo(service.path)"
      >
        <div class="service-icon">{{ service.icon }}</div>
        <div class="service-name">{{ service.name }}</div>
      </div>
    </div>

    <!-- 热门套餐 -->
    <div class="section">
      <div class="section-header">
        <span class="section-title">热门套餐</span>
        <span class="section-more" @click="goTo('/service')">
          查看全部 <van-icon name="arrow" />
        </span>
      </div>

      <div class="package-list">
        <div
          v-for="pkg in packages"
          :key="pkg.id"
          class="package-card"
          @click="goTo('/booking?package=' + pkg.id)"
        >
          <div class="package-header">
            <span class="package-icon">{{ pkg.icon }}</span>
            <span class="package-name">{{ pkg.name }}</span>
          </div>
          <p class="package-desc">{{ pkg.description }}</p>
          <div class="package-footer">
            <span class="package-price">
              <span class="currency">¥</span>
              <span class="amount">{{ pkg.price.toLocaleString() }}</span>
              <span class="unit">起</span>
            </span>
            <van-button type="primary" size="small" round>立即预约</van-button>
          </div>
        </div>
      </div>
    </div>

    <!-- 临终关怀 -->
    <div class="section">
      <div class="section-header">
        <span class="section-title">临终关怀</span>
      </div>
      <div class="care-card" @click="goTo('/service')">
        <div class="care-icon">📖</div>
        <div class="care-content">
          <h4>如何陪伴宠物走完最后一程</h4>
          <p>专业指南，帮助您和毛孩子好好告别</p>
        </div>
        <van-icon name="arrow" />
      </div>
    </div>

    <!-- 底部留白 -->
    <div style="height: 20px"></div>
  </div>
</template>

<style lang="scss" scoped>
.home-page {
  background-color: var(--bg-color);
}

.header {
  padding: 12px 16px;
  padding-top: calc(12px + env(safe-area-inset-top));
  background-color: #fff;

  .location {
    display: flex;
    align-items: center;
    gap: 4px;
    font-size: 14px;
    color: var(--text-color);
  }
}

.banner {
  background: linear-gradient(135deg, var(--color-primary), var(--color-primary-light));
  padding: 32px 16px;
  margin: 12px 16px;
  border-radius: 16px;
  text-align: center;
  color: #fff;

  .banner-icon {
    font-size: 48px;
    margin-bottom: 8px;
  }

  .banner-title {
    font-size: 24px;
    font-weight: 700;
    margin: 0 0 4px;
  }

  .banner-subtitle {
    font-size: 14px;
    opacity: 0.9;
    margin: 0 0 16px;
  }
}

.service-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
  padding: 0 16px;
  margin-bottom: 20px;

  .service-item {
    background-color: #fff;
    border-radius: 12px;
    padding: 16px 8px;
    text-align: center;
    transition: transform 0.2s;

    &:active {
      transform: scale(0.95);
    }

    .service-icon {
      font-size: 28px;
      margin-bottom: 8px;
    }

    .service-name {
      font-size: 13px;
      color: var(--text-color);
    }
  }
}

.section {
  padding: 0 16px;
  margin-bottom: 20px;

  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 12px;

    .section-title {
      font-size: 16px;
      font-weight: 600;
      color: var(--text-color);
    }

    .section-more {
      font-size: 13px;
      color: var(--text-color-secondary);
      display: flex;
      align-items: center;
    }
  }
}

.package-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.package-card {
  background-color: #fff;
  border-radius: 12px;
  padding: 16px;

  .package-header {
    display: flex;
    align-items: center;
    gap: 8px;
    margin-bottom: 8px;

    .package-icon {
      font-size: 20px;
    }

    .package-name {
      font-size: 16px;
      font-weight: 600;
      color: var(--text-color);
    }
  }

  .package-desc {
    font-size: 13px;
    color: var(--text-color-secondary);
    margin: 0 0 12px;
    line-height: 1.5;
  }

  .package-footer {
    display: flex;
    justify-content: space-between;
    align-items: center;

    .package-price {
      .currency {
        font-size: 14px;
        color: var(--color-warning);
      }

      .amount {
        font-size: 22px;
        font-weight: 700;
        color: var(--color-warning);
      }

      .unit {
        font-size: 12px;
        color: var(--text-color-secondary);
      }
    }
  }
}

.care-card {
  background-color: #fff;
  border-radius: 12px;
  padding: 16px;
  display: flex;
  align-items: center;
  gap: 12px;

  .care-icon {
    font-size: 32px;
  }

  .care-content {
    flex: 1;

    h4 {
      font-size: 14px;
      font-weight: 500;
      color: var(--text-color);
      margin: 0 0 4px;
    }

    p {
      font-size: 12px;
      color: var(--text-color-secondary);
      margin: 0;
    }
  }
}
</style>
