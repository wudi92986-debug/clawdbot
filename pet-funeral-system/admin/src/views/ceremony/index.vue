<script setup lang="ts">
import { ref } from 'vue'

const ceremonies = ref([
  { id: 1, pet: '豆豆', owner: '李女士', hall: '1号告别厅', time: '2026-01-30 09:00', duration: '30分钟', status: 'pending' },
  { id: 2, pet: '咪咪', owner: '王先生', hall: '2号告别厅', time: '2026-01-30 10:00', duration: '30分钟', status: 'completed' },
  { id: 3, pet: '球球', owner: '张女士', hall: 'VIP告别厅', time: '2026-01-31 09:00', duration: '60分钟', status: 'scheduled' },
])

const getStatusText = (status: string) => ({ pending: '待开始', completed: '已完成', scheduled: '已预约' }[status] || status)
const getStatusType = (status: string) => ({ pending: 'warning', completed: 'success', scheduled: 'primary' }[status] || 'info')
</script>

<template>
  <div class="ceremony-container">
    <div class="page-header">
      <h1 class="page-title">告别仪式</h1>
      <el-button type="primary"><el-icon><Plus /></el-icon>安排仪式</el-button>
    </div>
    <div class="card">
      <el-table :data="ceremonies" style="width: 100%">
        <el-table-column prop="pet" label="宠物" min-width="100" />
        <el-table-column prop="owner" label="主人" min-width="100" />
        <el-table-column prop="hall" label="场地" min-width="120" />
        <el-table-column prop="time" label="时间" min-width="150" />
        <el-table-column prop="duration" label="时长" width="100" />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)" effect="light">{{ getStatusText(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template #default><el-button type="primary" link>详情</el-button><el-button type="primary" link>直播</el-button></template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script lang="ts">
import { Plus } from '@element-plus/icons-vue'
export default { data() { return { Plus } } }
</script>

<style lang="scss" scoped>
.ceremony-container { animation: fadeIn 0.3s ease; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: var(--spacing-lg); }
.page-title { font-size: 24px; font-weight: 600; margin: 0; }
.card { background: var(--bg-color-card); border-radius: var(--radius-lg); box-shadow: var(--shadow-sm); padding: var(--spacing-lg); }
@keyframes fadeIn { from { opacity: 0; } to { opacity: 1; } }
</style>
