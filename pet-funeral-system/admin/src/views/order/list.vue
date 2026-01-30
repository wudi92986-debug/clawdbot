<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()

// 搜索表单
const searchForm = reactive({
  keyword: '',
  dateRange: [],
  status: '',
  package: '',
  staff: '',
})

// 订单状态选项
const statusOptions = [
  { label: '待确认', value: 'pending' },
  { label: '待接运', value: 'pickup' },
  { label: '服务中', value: 'processing' },
  { label: '已完成', value: 'completed' },
  { label: '已取消', value: 'cancelled' },
]

// 套餐选项
const packageOptions = [
  { label: '基础告别', value: 'basic' },
  { label: '温馨告别', value: 'warm' },
  { label: '尊享告别', value: 'premium' },
]

// 当前 Tab
const activeTab = ref('all')

// 加载状态
const loading = ref(false)

// 分页
const pagination = reactive({
  page: 1,
  pageSize: 10,
  total: 156,
})

// 模拟订单数据
const orders = ref([
  {
    id: 1,
    orderNo: 'PF20260130001',
    customer: '李女士',
    customerPhone: '138****8888',
    pet: '豆豆',
    petType: '金毛寻回犬',
    package: '温馨告别',
    amount: 2980,
    paidAmount: 500,
    status: 'pending',
    appointmentTime: '2026-01-30 10:00',
    createdAt: '2026-01-30 09:15',
  },
  {
    id: 2,
    orderNo: 'PF20260130002',
    customer: '王先生',
    customerPhone: '139****6666',
    pet: '咪咪',
    petType: '英国短毛猫',
    package: '基础告别',
    amount: 1280,
    paidAmount: 1280,
    status: 'processing',
    appointmentTime: '2026-01-30 14:00',
    createdAt: '2026-01-29 20:30',
  },
  {
    id: 3,
    orderNo: 'PF20260129003',
    customer: '张女士',
    customerPhone: '137****5555',
    pet: '球球',
    petType: '泰迪',
    package: '尊享告别',
    amount: 5980,
    paidAmount: 5980,
    status: 'completed',
    appointmentTime: '2026-01-29 09:00',
    createdAt: '2026-01-28 15:20',
  },
  {
    id: 4,
    orderNo: 'PF20260129004',
    customer: '陈先生',
    customerPhone: '136****4444',
    pet: '旺财',
    petType: '柴犬',
    package: '温馨告别',
    amount: 2980,
    paidAmount: 500,
    status: 'pickup',
    appointmentTime: '2026-01-30 15:30',
    createdAt: '2026-01-29 18:45',
  },
  {
    id: 5,
    orderNo: 'PF20260128005',
    customer: '刘女士',
    customerPhone: '135****3333',
    pet: '小白',
    petType: '布偶猫',
    package: '基础告别',
    amount: 1280,
    paidAmount: 1280,
    status: 'completed',
    appointmentTime: '2026-01-28 11:00',
    createdAt: '2026-01-27 22:10',
  },
])

// 选中的订单
const selectedOrders = ref<number[]>([])

// 获取状态文本
const getStatusText = (status: string) => {
  const map: Record<string, string> = {
    pending: '待确认',
    pickup: '待接运',
    processing: '服务中',
    completed: '已完成',
    cancelled: '已取消',
  }
  return map[status] || status
}

// 获取状态类型
const getStatusType = (status: string) => {
  const map: Record<string, string> = {
    pending: 'warning',
    pickup: 'info',
    processing: 'primary',
    completed: 'success',
    cancelled: 'danger',
  }
  return map[status] || 'info'
}

// 搜索
const handleSearch = () => {
  loading.value = true
  setTimeout(() => {
    loading.value = false
  }, 500)
}

// 重置
const handleReset = () => {
  Object.assign(searchForm, {
    keyword: '',
    dateRange: [],
    status: '',
    package: '',
    staff: '',
  })
  handleSearch()
}

// 查看详情
const handleView = (row: any) => {
  router.push(`/order/detail/${row.id}`)
}

// 确认订单
const handleConfirm = (row: any) => {
  ElMessageBox.confirm(`确定要确认订单 ${row.orderNo} 吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(() => {
    ElMessage.success('订单已确认')
  })
}

// 批量确认
const handleBatchConfirm = () => {
  if (selectedOrders.value.length === 0) {
    ElMessage.warning('请先选择订单')
    return
  }
  ElMessageBox.confirm(`确定要批量确认 ${selectedOrders.value.length} 个订单吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(() => {
    ElMessage.success('订单已批量确认')
  })
}

// 导出
const handleExport = () => {
  ElMessage.success('正在导出...')
}

// 选择变化
const handleSelectionChange = (rows: any[]) => {
  selectedOrders.value = rows.map((r) => r.id)
}

// 分页变化
const handlePageChange = (page: number) => {
  pagination.page = page
  handleSearch()
}

const handleSizeChange = (size: number) => {
  pagination.pageSize = size
  pagination.page = 1
  handleSearch()
}
</script>

<template>
  <div class="order-list-container">
    <!-- 搜索区域 -->
    <div class="card search-card">
      <el-form :model="searchForm" inline>
        <el-form-item label="">
          <el-input
            v-model="searchForm.keyword"
            placeholder="订单号/宠物名/客户名"
            clearable
            style="width: 200px"
            :prefix-icon="Search"
          />
        </el-form-item>
        <el-form-item label="">
          <el-date-picker
            v-model="searchForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            style="width: 240px"
          />
        </el-form-item>
        <el-form-item label="">
          <el-select v-model="searchForm.status" placeholder="订单状态" clearable style="width: 120px">
            <el-option
              v-for="item in statusOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="">
          <el-select v-model="searchForm.package" placeholder="服务套餐" clearable style="width: 120px">
            <el-option
              v-for="item in packageOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 列表区域 -->
    <div class="card">
      <!-- Tab 切换 -->
      <el-tabs v-model="activeTab" class="order-tabs">
        <el-tab-pane label="全部 (156)" name="all" />
        <el-tab-pane label="待确认 (12)" name="pending" />
        <el-tab-pane label="待接运 (5)" name="pickup" />
        <el-tab-pane label="服务中 (8)" name="processing" />
        <el-tab-pane label="已完成 (131)" name="completed" />
      </el-tabs>

      <!-- 表格 -->
      <el-table
        v-loading="loading"
        :data="orders"
        style="width: 100%"
        :header-cell-style="{ backgroundColor: '#FAF8F5', color: '#4A4A4A' }"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="50" />
        
        <el-table-column prop="orderNo" label="订单编号" min-width="150">
          <template #default="{ row }">
            <span class="order-no">{{ row.orderNo }}</span>
          </template>
        </el-table-column>

        <el-table-column label="客户" min-width="120">
          <template #default="{ row }">
            <div class="customer-info">
              <div class="customer-name">{{ row.customer }}</div>
              <div class="customer-phone">{{ row.customerPhone }}</div>
            </div>
          </template>
        </el-table-column>

        <el-table-column label="宠物" min-width="140">
          <template #default="{ row }">
            <div class="pet-info">
              <span class="pet-name">{{ row.pet }}</span>
              <span class="pet-type">{{ row.petType }}</span>
            </div>
          </template>
        </el-table-column>

        <el-table-column prop="package" label="套餐" min-width="100" />

        <el-table-column label="金额" min-width="120">
          <template #default="{ row }">
            <div class="amount-info">
              <div class="amount">¥{{ row.amount.toLocaleString() }}</div>
              <div v-if="row.paidAmount < row.amount" class="paid">
                已付 ¥{{ row.paidAmount }}
              </div>
            </div>
          </template>
        </el-table-column>

        <el-table-column label="预约时间" min-width="150">
          <template #default="{ row }">
            {{ row.appointmentTime }}
          </template>
        </el-table-column>

        <el-table-column prop="status" label="状态" min-width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)" effect="light" round>
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleView(row)">
              详情
            </el-button>
            <el-button
              v-if="row.status === 'pending'"
              type="success"
              link
              @click="handleConfirm(row)"
            >
              确认
            </el-button>
            <el-dropdown trigger="click">
              <el-button type="info" link>
                <el-icon><More /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item>派单</el-dropdown-item>
                  <el-dropdown-item>打印</el-dropdown-item>
                  <el-dropdown-item divided>取消订单</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <!-- 底部操作栏 -->
      <div class="table-footer">
        <div class="batch-actions">
          <el-checkbox
            :indeterminate="selectedOrders.length > 0 && selectedOrders.length < orders.length"
            :model-value="selectedOrders.length === orders.length && orders.length > 0"
          >
            全选
          </el-checkbox>
          <el-button size="small" :disabled="selectedOrders.length === 0" @click="handleBatchConfirm">
            批量确认
          </el-button>
          <el-button size="small" :disabled="selectedOrders.length === 0">
            批量派单
          </el-button>
          <el-button size="small" @click="handleExport">
            <el-icon><Download /></el-icon>
            导出Excel
          </el-button>
        </div>

        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handlePageChange"
        />
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Search, More, Download } from '@element-plus/icons-vue'
export default {
  data() {
    return { Search, More, Download }
  }
}
</script>

<style lang="scss" scoped>
.order-list-container {
  animation: fadeIn 0.3s ease;
}

.search-card {
  margin-bottom: var(--spacing-lg);
  
  :deep(.el-form-item) {
    margin-bottom: 0;
    margin-right: var(--spacing-md);
  }
}

.card {
  background-color: var(--bg-color-card);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-sm);
  padding: var(--spacing-lg);
}

.order-tabs {
  :deep(.el-tabs__header) {
    margin-bottom: var(--spacing-md);
  }

  :deep(.el-tabs__item.is-active) {
    color: var(--color-primary);
  }

  :deep(.el-tabs__active-bar) {
    background-color: var(--color-primary);
  }
}

.order-no {
  font-family: 'Monaco', 'Consolas', monospace;
  font-size: 13px;
  color: var(--color-primary);
  cursor: pointer;

  &:hover {
    text-decoration: underline;
  }
}

.customer-info,
.pet-info {
  display: flex;
  flex-direction: column;

  .customer-name,
  .pet-name {
    font-weight: 500;
    color: var(--text-color-primary);
  }

  .customer-phone,
  .pet-type {
    font-size: 12px;
    color: var(--text-color-secondary);
  }
}

.amount-info {
  .amount {
    font-weight: 600;
    color: var(--text-color-primary);
  }

  .paid {
    font-size: 12px;
    color: var(--color-success);
  }
}

.table-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: var(--spacing-lg);
  padding-top: var(--spacing-md);
  border-top: 1px solid var(--border-color-light);
}

.batch-actions {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
}

:deep(.el-table) {
  --el-table-border-color: var(--border-color-light);
  --el-table-row-hover-bg-color: var(--bg-color-page);
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
