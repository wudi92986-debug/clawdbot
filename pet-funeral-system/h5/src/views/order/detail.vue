<script setup lang="ts">
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const order = ref({
  id: 1,
  orderNo: 'PF20260130001',
  status: 'pending',
  pet: '豆豆',
  petType: '金毛寻回犬',
  package: '温馨告别',
  amount: 2980,
  paidAmount: 500,
  appointmentTime: '2026-01-30 10:00',
  address: '北京市朝阳区望京街道xxx小区x号楼',
  contactName: '李女士',
  contactPhone: '138****8888',
  createdAt: '2026-01-30 09:15',
  services: [
    { name: '上门接运', price: 200 },
    { name: '遗容整理', price: 300 },
    { name: '告别仪式', price: 800 },
    { name: '单独火化', price: 1200 },
    { name: '精品骨灰盒', price: 480 },
  ]
})

const getStatusText = (status: string) => {
  const map: Record<string, string> = { pending: '待确认', processing: '服务中', completed: '已完成' }
  return map[status] || status
}

const goBack = () => router.back()

const handlePay = () => {
  showToast('支付功能开发中...')
}
</script>

<template>
  <div class="order-detail-page">
    <van-nav-bar title="订单详情" left-arrow @click-left="goBack" />

    <!-- 状态 -->
    <div class="status-bar">
      <div class="status-text">{{ getStatusText(order.status) }}</div>
      <div class="status-desc">等待客服确认您的订单</div>
    </div>

    <!-- 预约信息 -->
    <van-cell-group inset title="预约信息">
      <van-cell title="预约时间" :value="order.appointmentTime" />
      <van-cell title="接运地址" :label="order.address" />
      <van-cell title="联系人" :value="order.contactName" />
      <van-cell title="联系电话" :value="order.contactPhone" />
    </van-cell-group>

    <!-- 宠物信息 -->
    <van-cell-group inset title="宠物信息">
      <van-cell title="宠物名字" :value="order.pet" />
      <van-cell title="宠物品种" :value="order.petType" />
    </van-cell-group>

    <!-- 服务明细 -->
    <van-cell-group inset title="服务明细">
      <van-cell v-for="service in order.services" :key="service.name" :title="service.name">
        <template #value>
          <span style="color: #E8B89D">¥{{ service.price }}</span>
        </template>
      </van-cell>
    </van-cell-group>

    <!-- 费用信息 -->
    <div class="price-card">
      <div class="price-row">
        <span>套餐总价</span>
        <span>¥{{ order.amount }}</span>
      </div>
      <div class="price-row">
        <span>已付定金</span>
        <span class="paid">-¥{{ order.paidAmount }}</span>
      </div>
      <div class="price-row total">
        <span>待付尾款</span>
        <span class="remain">¥{{ order.amount - order.paidAmount }}</span>
      </div>
    </div>

    <!-- 订单信息 -->
    <van-cell-group inset title="订单信息">
      <van-cell title="订单编号" :value="order.orderNo" />
      <van-cell title="下单时间" :value="order.createdAt" />
    </van-cell-group>

    <!-- 底部按钮 -->
    <div class="bottom-bar">
      <van-button round @click="goBack">联系客服</van-button>
      <van-button type="primary" round @click="handlePay">支付尾款</van-button>
    </div>
  </div>
</template>

<script lang="ts">
import { showToast } from 'vant'
export default { setup() { return { showToast } } }
</script>

<style lang="scss" scoped>
.order-detail-page {
  min-height: 100vh;
  background-color: var(--bg-color);
  padding-bottom: 80px;
}

.status-bar {
  background: linear-gradient(135deg, var(--color-primary), var(--color-primary-light));
  color: #fff;
  padding: 24px 16px;
  text-align: center;

  .status-text {
    font-size: 20px;
    font-weight: 600;
    margin-bottom: 4px;
  }

  .status-desc {
    font-size: 14px;
    opacity: 0.9;
  }
}

.price-card {
  margin: 12px 16px;
  padding: 16px;
  background-color: #fff;
  border-radius: 12px;

  .price-row {
    display: flex;
    justify-content: space-between;
    padding: 8px 0;
    font-size: 14px;

    .paid { color: var(--color-success); }

    &.total {
      border-top: 1px solid var(--border-color);
      margin-top: 8px;
      padding-top: 16px;
      font-weight: 500;

      .remain {
        font-size: 20px;
        font-weight: 700;
        color: var(--color-warning);
      }
    }
  }
}

.bottom-bar {
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

  .van-button {
    flex: 1;
  }
}
</style>
