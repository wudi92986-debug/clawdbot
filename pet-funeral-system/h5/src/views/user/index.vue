<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const userInfo = ref({
  name: '豆豆麻麻',
  phone: '138****8888',
  avatar: '',
  level: '金卡会员',
  points: 2580,
  coupons: 3,
})

const goTo = (path: string) => {
  router.push(path)
}
</script>

<template>
  <div class="user-page page">
    <!-- 用户信息 -->
    <div class="user-header">
      <div class="user-avatar">
        <van-image round width="60" height="60" :src="userInfo.avatar">
          <template #error>👤</template>
        </van-image>
      </div>
      <div class="user-info">
        <div class="user-name">{{ userInfo.name }}</div>
        <div class="user-phone">{{ userInfo.phone }}</div>
      </div>
      <van-icon name="setting-o" size="22" @click="goTo('/settings')" />
    </div>

    <!-- 会员卡 -->
    <div class="member-card">
      <div class="member-level">
        <span class="level-icon">💎</span>
        <span class="level-text">{{ userInfo.level }}</span>
      </div>
      <div class="member-stats">
        <div class="stat-item">
          <div class="stat-value">{{ userInfo.points }}</div>
          <div class="stat-label">积分</div>
        </div>
        <div class="stat-item">
          <div class="stat-value">{{ userInfo.coupons }}</div>
          <div class="stat-label">优惠券</div>
        </div>
      </div>
    </div>

    <!-- 我的服务 -->
    <van-cell-group inset title="我的服务">
      <van-cell title="我的订单" icon="orders-o" is-link @click="goTo('/order')" />
      <van-cell title="我的纪念馆" icon="photo-o" is-link @click="goTo('/memorial')" />
      <van-cell title="宠物档案" icon="records-o" is-link />
      <van-cell title="骨灰寄存" icon="location-o" is-link />
      <van-cell title="购物订单" icon="shopping-cart-o" is-link />
    </van-cell-group>

    <!-- 其他 -->
    <van-cell-group inset title="其他">
      <van-cell title="临终关怀指南" icon="description-o" is-link />
      <van-cell title="在线客服" icon="service-o" is-link />
      <van-cell title="联系电话" icon="phone-o" is-link value="400-888-8888" />
      <van-cell title="设置" icon="setting-o" is-link />
    </van-cell-group>
  </div>
</template>

<style lang="scss" scoped>
.user-page {
  background-color: var(--bg-color);
}

.user-header {
  background: linear-gradient(135deg, var(--color-primary), var(--color-primary-light));
  padding: 24px 16px;
  padding-top: calc(24px + env(safe-area-inset-top));
  display: flex;
  align-items: center;
  gap: 16px;
  color: #fff;

  .user-avatar {
    :deep(.van-image__error) {
      font-size: 32px;
      background-color: rgba(255, 255, 255, 0.2);
    }
  }

  .user-info {
    flex: 1;

    .user-name {
      font-size: 18px;
      font-weight: 600;
      margin-bottom: 4px;
    }

    .user-phone {
      font-size: 14px;
      opacity: 0.9;
    }
  }
}

.member-card {
  margin: -20px 16px 16px;
  padding: 16px;
  background: linear-gradient(135deg, #F9F3E8, #EDE4D3);
  border-radius: 12px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);

  .member-level {
    display: flex;
    align-items: center;
    gap: 8px;

    .level-icon {
      font-size: 24px;
    }

    .level-text {
      font-size: 16px;
      font-weight: 600;
      color: var(--color-primary);
    }
  }

  .member-stats {
    display: flex;
    gap: 32px;

    .stat-item {
      text-align: center;

      .stat-value {
        font-size: 18px;
        font-weight: 600;
        color: var(--color-primary);
      }

      .stat-label {
        font-size: 12px;
        color: var(--text-color-secondary);
      }
    }
  }
}
</style>
