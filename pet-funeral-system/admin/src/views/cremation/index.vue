<script setup lang="ts">
import { ref } from 'vue'

const cremations = ref([
  { id: 1, pet: '咪咪', type: '单独火化', time: '2026-01-30 10:30', room: '火化室A', operator: '张师傅', status: 'processing' },
  { id: 2, pet: '小白', type: '单独火化', time: '2026-01-30 14:00', room: '火化室B', operator: '李师傅', status: 'pending' },
  { id: 3, pet: '球球', type: '单独火化', time: '2026-01-29 11:00', room: '火化室A', operator: '张师傅', status: 'completed' },
])

const getStatusText = (status: string) => ({ pending: '待火化', processing: '火化中', completed: '已完成' }[status] || status)
const getStatusType = (status: string) => ({ pending: 'warning', processing: 'primary', completed: 'success' }[status] || 'info')
</script>

<template>
  <div class="cremation-container">
    <div class="page-header">
      <h1 class="page-title">火化管理</h1>
    </div>
    <div class="card">
      <el-table :data="cremations" style="width: 100%">
        <el-table-column prop="pet" label="宠物" min-width="100" />
        <el-table-column prop="type" label="火化类型" min-width="100" />
        <el-table-column prop="time" label="时间" min-width="150" />
        <el-table-column prop="room" label="火化室" min-width="100" />
        <el-table-column prop="operator" label="操作员" min-width="100" />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)" effect="light">{{ getStatusText(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120">
          <template #default><el-button type="primary" link>详情</el-button></template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.cremation-container { animation: fadeIn 0.3s ease; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: var(--spacing-lg); }
.page-title { font-size: 24px; font-weight: 600; margin: 0; }
.card { background: var(--bg-color-card); border-radius: var(--radius-lg); box-shadow: var(--shadow-sm); padding: var(--spacing-lg); }
@keyframes fadeIn { from { opacity: 0; } to { opacity: 1; } }
</style>
