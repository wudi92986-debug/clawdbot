<script setup lang="ts">
import { ref } from 'vue'

const stats = ref([
  { label: '本月订单', value: 156, trend: '+12%' },
  { label: '本月收入', value: '¥356,800', trend: '+18%' },
  { label: '新增客户', value: 45, trend: '+8%' },
  { label: '纪念馆数', value: 328, trend: '+5%' },
])
</script>

<template>
  <div class="statistics-container">
    <div class="page-header">
      <h1 class="page-title">数据统计</h1>
      <el-date-picker type="monthrange" start-placeholder="开始月份" end-placeholder="结束月份" />
    </div>

    <el-row :gutter="20" style="margin-bottom: 20px">
      <el-col v-for="stat in stats" :key="stat.label" :xs="12" :sm="6">
        <div class="stat-card">
          <div class="stat-value">{{ stat.value }}</div>
          <div class="stat-label">{{ stat.label }}</div>
          <div class="stat-trend">{{ stat.trend }}</div>
        </div>
      </el-col>
    </el-row>

    <div class="card">
      <h3 style="margin: 0 0 20px">业务趋势图表</h3>
      <div style="height: 400px; display: flex; align-items: center; justify-content: center; background: #FAF8F5; border-radius: 8px">
        <span style="color: #909399">图表区域 - ECharts 图表</span>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.statistics-container { animation: fadeIn 0.3s ease; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: var(--spacing-lg); }
.page-title { font-size: 24px; font-weight: 600; margin: 0; }
.stat-card {
  background: linear-gradient(135deg, #C5B49A, #8B7355); border-radius: 12px; padding: 24px; color: #fff; text-align: center;
  .stat-value { font-size: 28px; font-weight: 700; }
  .stat-label { font-size: 14px; opacity: 0.9; margin-top: 8px; }
  .stat-trend { font-size: 12px; opacity: 0.8; margin-top: 4px; }
}
.card { background: var(--bg-color-card); border-radius: var(--radius-lg); box-shadow: var(--shadow-sm); padding: var(--spacing-lg); }
@keyframes fadeIn { from { opacity: 0; } to { opacity: 1; } }
</style>
