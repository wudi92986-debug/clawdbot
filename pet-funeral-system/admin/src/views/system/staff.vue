<script setup lang="ts">
import { ref } from 'vue'

const staffList = ref([
  { id: 1, name: '张管理员', phone: '138****0001', role: '管理员', status: 'active', createdAt: '2024-01-01' },
  { id: 2, name: '李接待', phone: '138****0002', role: '前台', status: 'active', createdAt: '2024-03-15' },
  { id: 3, name: '王师傅', phone: '138****0003', role: '火化师', status: 'active', createdAt: '2024-02-20' },
  { id: 4, name: '赵司机', phone: '138****0004', role: '司机', status: 'active', createdAt: '2024-06-10' },
])
</script>

<template>
  <div class="staff-container">
    <div class="page-header">
      <h1 class="page-title">员工管理</h1>
      <el-button type="primary"><el-icon><Plus /></el-icon>添加员工</el-button>
    </div>
    <div class="card">
      <el-table :data="staffList" style="width: 100%">
        <el-table-column prop="name" label="姓名" min-width="100" />
        <el-table-column prop="phone" label="手机号" min-width="120" />
        <el-table-column prop="role" label="角色" min-width="100" />
        <el-table-column label="状态" width="100">
          <template #default><el-tag type="success" effect="light">在职</el-tag></template>
        </el-table-column>
        <el-table-column prop="createdAt" label="入职时间" min-width="120" />
        <el-table-column label="操作" width="150">
          <template #default><el-button type="primary" link>编辑</el-button><el-button type="danger" link>禁用</el-button></template>
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
.staff-container { animation: fadeIn 0.3s ease; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: var(--spacing-lg); }
.page-title { font-size: 24px; font-weight: 600; margin: 0; }
.card { background: var(--bg-color-card); border-radius: var(--radius-lg); box-shadow: var(--shadow-sm); padding: var(--spacing-lg); }
@keyframes fadeIn { from { opacity: 0; } to { opacity: 1; } }
</style>
