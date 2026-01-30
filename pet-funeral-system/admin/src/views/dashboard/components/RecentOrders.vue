<script setup lang="ts">
import { useRouter } from 'vue-router'

interface Order {
  id: number
  orderNo: string
  pet: string
  petType: string
  package: string
  amount: number
  status: 'pending' | 'processing' | 'completed' | 'cancelled'
}

defineProps<{
  orders: Order[]
}>()

const router = useRouter()

const getStatusText = (status: string) => {
  switch (status) {
    case 'pending':
      return '待确认'
    case 'processing':
      return '服务中'
    case 'completed':
      return '已完成'
    case 'cancelled':
      return '已取消'
    default:
      return status
  }
}

const getStatusType = (status: string) => {
  switch (status) {
    case 'pending':
      return 'warning'
    case 'processing':
      return 'primary'
    case 'completed':
      return 'success'
    case 'cancelled':
      return 'danger'
    default:
      return 'info'
  }
}

const handleView = (order: Order) => {
  router.push(`/order/detail/${order.id}`)
}
</script>

<template>
  <el-table
    :data="orders"
    style="width: 100%"
    :header-cell-style="{ backgroundColor: '#FAF8F5', color: '#4A4A4A' }"
  >
    <el-table-column prop="orderNo" label="订单编号" min-width="140">
      <template #default="{ row }">
        <span class="order-no">{{ row.orderNo }}</span>
      </template>
    </el-table-column>

    <el-table-column label="宠物信息" min-width="120">
      <template #default="{ row }">
        <div class="pet-info">
          <span class="pet-name">{{ row.pet }}</span>
          <span class="pet-type">{{ row.petType }}</span>
        </div>
      </template>
    </el-table-column>

    <el-table-column prop="package" label="套餐" min-width="100" />

    <el-table-column prop="amount" label="金额" min-width="100">
      <template #default="{ row }">
        <span class="amount">¥{{ row.amount.toLocaleString() }}</span>
      </template>
    </el-table-column>

    <el-table-column prop="status" label="状态" min-width="100">
      <template #default="{ row }">
        <el-tag :type="getStatusType(row.status)" effect="light" round>
          {{ getStatusText(row.status) }}
        </el-tag>
      </template>
    </el-table-column>

    <el-table-column label="操作" width="100" fixed="right">
      <template #default="{ row }">
        <el-button type="primary" link @click="handleView(row)">
          详情
        </el-button>
      </template>
    </el-table-column>
  </el-table>
</template>

<style lang="scss" scoped>
.order-no {
  font-family: 'Monaco', 'Consolas', monospace;
  font-size: 13px;
  color: var(--color-primary);
}

.pet-info {
  display: flex;
  flex-direction: column;

  .pet-name {
    font-weight: 500;
    color: var(--text-color-primary);
  }

  .pet-type {
    font-size: 12px;
    color: var(--text-color-secondary);
  }
}

.amount {
  font-weight: 600;
  color: var(--color-warning);
}

:deep(.el-table) {
  --el-table-border-color: var(--border-color-light);
  --el-table-row-hover-bg-color: var(--bg-color-page);
  border-radius: var(--radius-md);
  overflow: hidden;
}
</style>
