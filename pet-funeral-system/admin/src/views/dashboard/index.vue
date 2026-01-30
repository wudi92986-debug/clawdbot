<script setup lang="ts">
import { ref, onMounted } from 'vue'
import StatCard from './components/StatCard.vue'
import TodoList from './components/TodoList.vue'
import ScheduleList from './components/ScheduleList.vue'
import RecentOrders from './components/RecentOrders.vue'
import TrendChart from './components/TrendChart.vue'

// 统计数据
const stats = ref([
  { 
    title: '新增订单', 
    value: 12, 
    trend: '+20%', 
    trendUp: true,
    icon: 'Document',
    type: 'primary' 
  },
  { 
    title: '服务中', 
    value: 8, 
    trend: '+5%', 
    trendUp: true,
    icon: 'Clock',
    type: 'success' 
  },
  { 
    title: '今日收入', 
    value: '¥28,800', 
    trend: '+15%', 
    trendUp: true,
    icon: 'Wallet',
    type: 'warning' 
  },
  { 
    title: '本月服务', 
    value: 156, 
    trend: '+8%', 
    trendUp: true,
    icon: 'Finished',
    type: 'info' 
  },
])

// 待办事项
const todoItems = ref([
  { id: 1, type: 'danger', text: '3 个订单待确认', link: '/order/pending' },
  { id: 2, type: 'warning', text: '2 个骨灰即将到期', link: '/storage' },
  { id: 3, type: 'warning', text: '1 个仪式待安排', link: '/ceremony' },
  { id: 4, type: 'info', text: '5 个客户待回访', link: '/customer' },
])

// 今日排期
const scheduleItems = ref([
  { id: 1, time: '09:00', title: '豆豆 - 告别仪式', location: '1号告别厅', status: 'pending' },
  { id: 2, time: '10:30', title: '咪咪 - 火化服务', location: '火化室A', status: 'processing' },
  { id: 3, time: '14:00', title: '球球 - 骨灰领取', location: '前台', status: 'pending' },
  { id: 4, time: '15:30', title: '旺财 - 接运出发', location: '朝阳区望京街道', status: 'pending' },
])

// 最新订单
const recentOrders = ref([
  { id: 1, orderNo: 'PF20260130001', pet: '豆豆', petType: '金毛', package: '温馨告别', amount: 2980, status: 'pending' },
  { id: 2, orderNo: 'PF20260130002', pet: '咪咪', petType: '英短', package: '基础告别', amount: 1280, status: 'processing' },
  { id: 3, orderNo: 'PF20260129003', pet: '球球', petType: '泰迪', package: '尊享告别', amount: 5980, status: 'completed' },
  { id: 4, orderNo: 'PF20260129004', pet: '旺财', petType: '柴犬', package: '温馨告别', amount: 2980, status: 'pending' },
  { id: 5, orderNo: 'PF20260128005', pet: '小白', petType: '布偶', package: '基础告别', amount: 1280, status: 'completed' },
])

// 日期范围
const dateRange = ref('today')

const handleDateChange = (value: string) => {
  dateRange.value = value
  // TODO: 根据日期范围刷新数据
}
</script>

<template>
  <div class="dashboard-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1 class="page-title">工作台</h1>
      <div class="date-filter">
        <el-radio-group v-model="dateRange" size="small" @change="handleDateChange">
          <el-radio-button value="today">今天</el-radio-button>
          <el-radio-button value="week">本周</el-radio-button>
          <el-radio-button value="month">本月</el-radio-button>
        </el-radio-group>
      </div>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stat-row">
      <el-col v-for="stat in stats" :key="stat.title" :xs="12" :sm="12" :md="6">
        <StatCard
          :title="stat.title"
          :value="stat.value"
          :trend="stat.trend"
          :trend-up="stat.trendUp"
          :icon="stat.icon"
          :type="stat.type"
        />
      </el-col>
    </el-row>

    <!-- 中间区域 -->
    <el-row :gutter="20">
      <!-- 待处理事项 -->
      <el-col :xs="24" :sm="24" :md="12">
        <div class="card">
          <div class="card-title">待处理事项</div>
          <TodoList :items="todoItems" />
        </div>
      </el-col>

      <!-- 今日排期 -->
      <el-col :xs="24" :sm="24" :md="12">
        <div class="card">
          <div class="card-title">今日排期</div>
          <ScheduleList :items="scheduleItems" />
        </div>
      </el-col>
    </el-row>

    <!-- 趋势图表 -->
    <div class="card">
      <div class="card-title">业务趋势</div>
      <TrendChart />
    </div>

    <!-- 最新订单 -->
    <div class="card">
      <div class="card-header">
        <div class="card-title">最新订单</div>
        <el-button type="primary" link @click="$router.push('/order/list')">
          查看全部 <el-icon><ArrowRight /></el-icon>
        </el-button>
      </div>
      <RecentOrders :orders="recentOrders" />
    </div>
  </div>
</template>

<style lang="scss" scoped>
.dashboard-container {
  animation: fadeIn 0.3s ease;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--spacing-lg);
}

.page-title {
  font-size: 24px;
  font-weight: 600;
  color: var(--text-color-primary);
  margin: 0;
}

.stat-row {
  margin-bottom: var(--spacing-lg);
}

.card {
  background-color: var(--bg-color-card);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-sm);
  padding: var(--spacing-lg);
  margin-bottom: var(--spacing-lg);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
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
