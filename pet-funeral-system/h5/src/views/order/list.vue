<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const activeTab = ref('all')

const orders = ref([
  { id: 1, orderNo: 'PF20260130001', pet: '豆豆', petType: '金毛', package: '温馨告别', amount: 2980, status: 'pending', time: '01-30 10:00' },
  { id: 2, orderNo: 'PF20260125002', pet: '咪咪', petType: '英短', package: '基础告别', amount: 1280, status: 'completed', time: '01-25 14:00' },
])

const getStatusText = (status: string) => {
  const map: Record<string, string> = {
    pending: '待确认',
    processing: '服务中',
    completed: '已完成',
    cancelled: '已取消',
  }
  return map[status] || status
}

const getStatusClass = (status: string) => status

const goDetail = (id: number) => {
  router.push(`/order/${id}`)
}

const goBack = () => {
  router.back()
}
</script>

<template>
  <div class="order-list-page">
    <van-nav-bar title="我的订单" left-arrow @click-left="goBack" />

    <van-tabs v-model:active="activeTab" sticky>
      <van-tab title="全部" name="all" />
      <van-tab title="待确认" name="pending" />
      <van-tab title="进行中" name="processing" />
      <van-tab title="已完成" name="completed" />
    </van-tabs>

    <div class="order-list">
      <div
        v-for="order in orders"
        :key="order.id"
        class="order-card"
        @click="goDetail(order.id)"
      >
        <div class="order-header">
          <span class="order-no">订单号: {{ order.orderNo }}</span>
          <span class="order-status" :class="getStatusClass(order.status)">
            {{ getStatusText(order.status) }}
          </span>
        </div>
        <div class="order-content">
          <div class="pet-avatar">
            {{ order.petType.includes('猫') ? '🐱' : '🐕' }}
          </div>
          <div class="order-info">
            <div class="pet-name">{{ order.pet }} · {{ order.petType }}</div>
            <div class="package-name">{{ order.package }}套餐</div>
            <div class="order-time">预约时间: {{ order.time }}</div>
          </div>
          <div class="order-amount">¥{{ order.amount }}</div>
        </div>
        <div class="order-actions">
          <van-button v-if="order.status === 'completed'" size="small" round>创建纪念馆</van-button>
          <van-button size="small" round plain>查看详情</van-button>
        </div>
      </div>

      <van-empty v-if="orders.length === 0" description="暂无订单" />
    </div>
  </div>
</template>

<style lang="scss" scoped>
.order-list-page {
  min-height: 100vh;
  background-color: var(--bg-color);
}

.order-list {
  padding: 16px;
}

.order-card {
  background-color: #fff;
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 12px;

  .order-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 12px;
    padding-bottom: 12px;
    border-bottom: 1px solid var(--border-color);

    .order-no {
      font-size: 12px;
      color: var(--text-color-secondary);
    }

    .order-status {
      font-size: 13px;
      font-weight: 500;

      &.pending { color: #FA8C16; }
      &.processing { color: #1890FF; }
      &.completed { color: #52C41A; }
      &.cancelled { color: #909399; }
    }
  }

  .order-content {
    display: flex;
    align-items: center;
    gap: 12px;

    .pet-avatar {
      width: 48px;
      height: 48px;
      border-radius: 8px;
      background-color: var(--bg-color);
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 24px;
    }

    .order-info {
      flex: 1;

      .pet-name {
        font-size: 15px;
        font-weight: 500;
        margin-bottom: 4px;
      }

      .package-name {
        font-size: 13px;
        color: var(--text-color-secondary);
        margin-bottom: 4px;
      }

      .order-time {
        font-size: 12px;
        color: var(--text-color-light);
      }
    }

    .order-amount {
      font-size: 16px;
      font-weight: 600;
      color: var(--color-warning);
    }
  }

  .order-actions {
    display: flex;
    justify-content: flex-end;
    gap: 8px;
    margin-top: 12px;
    padding-top: 12px;
    border-top: 1px solid var(--border-color);
  }
}
</style>
