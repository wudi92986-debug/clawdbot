<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()

const orderId = computed(() => route.params.id)

// 订单状态步骤
const orderSteps = [
  { title: '已下单', time: '2026-01-30 09:15' },
  { title: '已确认', time: '' },
  { title: '接运中', time: '' },
  { title: '服务中', time: '' },
  { title: '已完成', time: '' },
]

const currentStep = ref(0)

// 订单数据
const orderInfo = ref({
  orderNo: 'PF20260130001',
  status: 'pending',
  createdAt: '2026-01-30 09:15',
  appointmentTime: '2026-01-30 10:00',
  package: '温馨告别',
  totalAmount: 2980,
  paidAmount: 500,
  payStatus: 'partial',
})

// 客户信息
const customerInfo = ref({
  name: '李女士',
  phone: '138****8888',
  wechat: '豆豆麻麻',
  address: '北京市朝阳区望京街道xxx小区x号楼x单元xxx室',
  remark: '请轻拿轻放，豆豆怕生，主人情绪激动',
})

// 宠物信息
const petInfo = ref({
  name: '豆豆',
  avatar: '',
  species: '狗',
  breed: '金毛寻回犬',
  gender: '公',
  age: '8岁',
  weight: '32kg',
  deathReason: '器官衰竭',
  deathTime: '2026-01-30 06:30',
})

// 服务明细
const serviceItems = ref([
  { name: '上门接运', price: 200, checked: true },
  { name: '遗容整理', price: 300, checked: true },
  { name: '告别仪式 (1号厅 30分钟)', price: 800, checked: true },
  { name: '单独火化', price: 1200, checked: true },
  { name: '精品骨灰盒', price: 480, checked: true },
])

// 返回列表
const handleBack = () => {
  router.back()
}

// 确认订单
const handleConfirm = () => {
  ElMessageBox.confirm('确定要确认此订单吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(() => {
    currentStep.value = 1
    orderInfo.value.status = 'confirmed'
    ElMessage.success('订单已确认')
  })
}

// 派单
const handleDispatch = () => {
  ElMessage.info('派单功能开发中...')
}

// 取消订单
const handleCancel = () => {
  ElMessageBox.confirm('确定要取消此订单吗？取消后不可恢复。', '警告', {
    confirmButtonText: '确定取消',
    cancelButtonText: '再想想',
    type: 'warning',
  }).then(() => {
    ElMessage.success('订单已取消')
  })
}

// 打印
const handlePrint = () => {
  window.print()
}

// 发送通知
const handleNotify = () => {
  ElMessage.success('通知已发送')
}

// 状态文本
const getStatusText = (status: string) => {
  const map: Record<string, string> = {
    pending: '待确认',
    confirmed: '已确认',
    pickup: '待接运',
    processing: '服务中',
    completed: '已完成',
    cancelled: '已取消',
  }
  return map[status] || status
}

const getStatusType = (status: string) => {
  const map: Record<string, string> = {
    pending: 'warning',
    confirmed: 'primary',
    pickup: 'info',
    processing: 'primary',
    completed: 'success',
    cancelled: 'danger',
  }
  return map[status] || 'info'
}
</script>

<template>
  <div class="order-detail-container">
    <!-- 顶部返回 -->
    <div class="page-header">
      <el-button link @click="handleBack">
        <el-icon><ArrowLeft /></el-icon>
        返回列表
      </el-button>
      <span class="order-title">订单详情 #{{ orderInfo.orderNo }}</span>
      <el-tag :type="getStatusType(orderInfo.status)" effect="light" size="large">
        {{ getStatusText(orderInfo.status) }}
      </el-tag>
    </div>

    <!-- 订单状态流程 -->
    <div class="card">
      <div class="card-title">订单状态</div>
      <el-steps :active="currentStep" align-center>
        <el-step
          v-for="(step, index) in orderSteps"
          :key="index"
          :title="step.title"
          :description="step.time"
        />
      </el-steps>
    </div>

    <!-- 信息卡片 -->
    <el-row :gutter="20">
      <!-- 基本信息 -->
      <el-col :xs="24" :sm="24" :md="12">
        <div class="card">
          <div class="card-title">基本信息</div>
          <el-descriptions :column="1" border>
            <el-descriptions-item label="订单编号">
              <span class="order-no">{{ orderInfo.orderNo }}</span>
            </el-descriptions-item>
            <el-descriptions-item label="下单时间">
              {{ orderInfo.createdAt }}
            </el-descriptions-item>
            <el-descriptions-item label="预约时间">
              <span class="highlight">{{ orderInfo.appointmentTime }}</span>
            </el-descriptions-item>
            <el-descriptions-item label="服务套餐">
              {{ orderInfo.package }}
            </el-descriptions-item>
            <el-descriptions-item label="订单金额">
              <span class="amount">¥{{ orderInfo.totalAmount.toLocaleString() }}</span>
            </el-descriptions-item>
            <el-descriptions-item label="支付状态">
              <el-tag v-if="orderInfo.payStatus === 'partial'" type="warning" effect="light">
                已支付定金 ¥{{ orderInfo.paidAmount }}
              </el-tag>
              <el-tag v-else type="success" effect="light">已支付</el-tag>
            </el-descriptions-item>
          </el-descriptions>
        </div>
      </el-col>

      <!-- 客户信息 -->
      <el-col :xs="24" :sm="24" :md="12">
        <div class="card">
          <div class="card-title">客户信息</div>
          <el-descriptions :column="1" border>
            <el-descriptions-item label="客户姓名">
              {{ customerInfo.name }}
            </el-descriptions-item>
            <el-descriptions-item label="联系电话">
              <el-link type="primary" :underline="false">
                {{ customerInfo.phone }}
              </el-link>
            </el-descriptions-item>
            <el-descriptions-item label="微信昵称">
              {{ customerInfo.wechat }}
            </el-descriptions-item>
            <el-descriptions-item label="接运地址">
              {{ customerInfo.address }}
            </el-descriptions-item>
            <el-descriptions-item label="备注">
              <span class="remark">{{ customerInfo.remark }}</span>
            </el-descriptions-item>
          </el-descriptions>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <!-- 宠物信息 -->
      <el-col :xs="24" :sm="24" :md="12">
        <div class="card">
          <div class="card-title">宠物信息</div>
          <div class="pet-detail">
            <div class="pet-avatar">
              <el-avatar :size="80" :src="petInfo.avatar">
                🐕
              </el-avatar>
              <div class="pet-name">{{ petInfo.name }}</div>
            </div>
            <el-descriptions :column="2" border>
              <el-descriptions-item label="品种">
                {{ petInfo.breed }}
              </el-descriptions-item>
              <el-descriptions-item label="性别">
                {{ petInfo.gender }}
              </el-descriptions-item>
              <el-descriptions-item label="年龄">
                {{ petInfo.age }}
              </el-descriptions-item>
              <el-descriptions-item label="体重">
                {{ petInfo.weight }}
              </el-descriptions-item>
              <el-descriptions-item label="离世原因" :span="2">
                {{ petInfo.deathReason }}
              </el-descriptions-item>
              <el-descriptions-item label="离世时间" :span="2">
                {{ petInfo.deathTime }}
              </el-descriptions-item>
            </el-descriptions>
          </div>
        </div>
      </el-col>

      <!-- 服务明细 -->
      <el-col :xs="24" :sm="24" :md="12">
        <div class="card">
          <div class="card-title">服务明细</div>
          <div class="service-list">
            <div
              v-for="item in serviceItems"
              :key="item.name"
              class="service-item"
            >
              <el-icon v-if="item.checked" color="#A8C686"><CircleCheckFilled /></el-icon>
              <el-icon v-else color="#C0C4CC"><CircleClose /></el-icon>
              <span class="service-name">{{ item.name }}</span>
              <span class="service-price">¥{{ item.price }}</span>
            </div>
            <el-divider />
            <div class="service-total">
              <span>套餐总价</span>
              <span class="total-price">¥{{ orderInfo.totalAmount.toLocaleString() }}</span>
            </div>
            <div class="service-paid">
              <span>已付定金</span>
              <span class="paid-price">-¥{{ orderInfo.paidAmount }}</span>
            </div>
            <el-divider />
            <div class="service-remain">
              <span>待付尾款</span>
              <span class="remain-price">¥{{ (orderInfo.totalAmount - orderInfo.paidAmount).toLocaleString() }}</span>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 底部操作栏 -->
    <div class="action-bar">
      <div class="action-left">
        <el-button @click="handlePrint">
          <el-icon><Printer /></el-icon>
          打印详情
        </el-button>
        <el-button @click="handleNotify">
          <el-icon><Bell /></el-icon>
          发送通知
        </el-button>
      </div>
      <div class="action-right">
        <el-button type="danger" plain @click="handleCancel">取消订单</el-button>
        <el-button type="primary" plain @click="handleDispatch">派单接运</el-button>
        <el-button type="primary" @click="handleConfirm">确认订单</el-button>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.order-detail-container {
  animation: fadeIn 0.3s ease;
}

.page-header {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
  margin-bottom: var(--spacing-lg);

  .order-title {
    font-size: 18px;
    font-weight: 600;
    color: var(--text-color-primary);
  }
}

.card {
  background-color: var(--bg-color-card);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-sm);
  padding: var(--spacing-lg);
  margin-bottom: var(--spacing-lg);
}

.card-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--text-color-primary);
  margin-bottom: var(--spacing-md);
  display: flex;
  align-items: center;

  &::before {
    content: '';
    display: inline-block;
    width: 4px;
    height: 16px;
    background-color: var(--color-primary);
    border-radius: 2px;
    margin-right: var(--spacing-sm);
  }
}

.order-no {
  font-family: 'Monaco', 'Consolas', monospace;
  color: var(--color-primary);
}

.highlight {
  color: var(--color-primary);
  font-weight: 500;
}

.amount {
  font-size: 18px;
  font-weight: 600;
  color: var(--color-warning);
}

.remark {
  color: var(--color-danger);
}

.pet-detail {
  .pet-avatar {
    text-align: center;
    margin-bottom: var(--spacing-md);

    .pet-name {
      margin-top: var(--spacing-sm);
      font-size: 16px;
      font-weight: 600;
    }
  }
}

.service-list {
  .service-item {
    display: flex;
    align-items: center;
    padding: var(--spacing-sm) 0;

    .el-icon {
      margin-right: var(--spacing-sm);
    }

    .service-name {
      flex: 1;
      color: var(--text-color-primary);
    }

    .service-price {
      color: var(--text-color-secondary);
    }
  }

  .service-total,
  .service-paid,
  .service-remain {
    display: flex;
    justify-content: space-between;
    padding: var(--spacing-xs) 0;
  }

  .total-price {
    font-weight: 500;
  }

  .paid-price {
    color: var(--color-success);
  }

  .remain-price {
    font-size: 18px;
    font-weight: 600;
    color: var(--color-warning);
  }
}

.action-bar {
  background-color: var(--bg-color-card);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-sm);
  padding: var(--spacing-md) var(--spacing-lg);
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: sticky;
  bottom: var(--spacing-lg);
}

.action-left,
.action-right {
  display: flex;
  gap: var(--spacing-sm);
}

:deep(.el-descriptions) {
  --el-descriptions-item-bordered-label-background: var(--bg-color-page);
}

:deep(.el-steps) {
  padding: var(--spacing-md) 0;

  .el-step__head.is-finish {
    color: var(--color-primary);
    border-color: var(--color-primary);
  }

  .el-step__title.is-finish {
    color: var(--color-primary);
  }

  .el-step__head.is-process {
    color: var(--color-primary);
    border-color: var(--color-primary);
  }

  .el-step__title.is-process {
    color: var(--color-primary);
    font-weight: 600;
  }
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
