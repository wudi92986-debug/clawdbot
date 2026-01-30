<script setup lang="ts">
import { ref } from 'vue'

const storages = ref([
  { id: 1, pet: '豆豆', cabinetNo: 'A-001', startDate: '2025-12-25', endDate: '2026-12-25', owner: '李女士', status: 'active' },
  { id: 2, pet: '球球', cabinetNo: 'A-015', startDate: '2026-01-16', endDate: '2027-01-16', owner: '张女士', status: 'active' },
  { id: 3, pet: '小绿', cabinetNo: 'B-008', startDate: '2025-08-20', endDate: '2026-02-20', owner: '周先生', status: 'expiring' },
])

const getStatusText = (status: string) => ({ active: '寄存中', expiring: '即将到期', expired: '已到期', retrieved: '已领取' }[status] || status)
const getStatusType = (status: string) => ({ active: 'success', expiring: 'warning', expired: 'danger', retrieved: 'info' }[status] || 'info')
</script>

<template>
  <div class="storage-container">
    <div class="page-header">
      <h1 class="page-title">骨灰寄存</h1>
    </div>
    <div class="card">
      <el-table :data="storages" style="width: 100%">
        <el-table-column prop="pet" label="宠物" min-width="100" />
        <el-table-column prop="cabinetNo" label="柜位编号" min-width="100" />
        <el-table-column prop="owner" label="主人" min-width="100" />
        <el-table-column prop="startDate" label="开始日期" min-width="120" />
        <el-table-column prop="endDate" label="到期日期" min-width="120" />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)" effect="light">{{ getStatusText(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template #default><el-button type="primary" link>续期</el-button><el-button type="primary" link>领取</el-button></template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.storage-container { animation: fadeIn 0.3s ease; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: var(--spacing-lg); }
.page-title { font-size: 24px; font-weight: 600; margin: 0; }
.card { background: var(--bg-color-card); border-radius: var(--radius-lg); box-shadow: var(--shadow-sm); padding: var(--spacing-lg); }
@keyframes fadeIn { from { opacity: 0; } to { opacity: 1; } }
</style>
