<script setup lang="ts">
import { ref } from 'vue'

const roles = ref([
  { id: 1, name: '超级管理员', description: '拥有所有权限', users: 1 },
  { id: 2, name: '管理员', description: '管理订单、客户、员工', users: 2 },
  { id: 3, name: '前台', description: '接待客户、处理订单', users: 3 },
  { id: 4, name: '火化师', description: '火化管理', users: 2 },
  { id: 5, name: '司机', description: '接运服务', users: 4 },
])
</script>

<template>
  <div class="role-container">
    <div class="page-header">
      <h1 class="page-title">角色权限</h1>
      <el-button type="primary"><el-icon><Plus /></el-icon>添加角色</el-button>
    </div>
    <div class="card">
      <el-table :data="roles" style="width: 100%">
        <el-table-column prop="name" label="角色名称" min-width="120" />
        <el-table-column prop="description" label="描述" min-width="200" />
        <el-table-column prop="users" label="用户数" width="100" />
        <el-table-column label="操作" width="200">
          <template #default><el-button type="primary" link>编辑</el-button><el-button type="primary" link>权限配置</el-button><el-button type="danger" link>删除</el-button></template>
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
.role-container { animation: fadeIn 0.3s ease; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: var(--spacing-lg); }
.page-title { font-size: 24px; font-weight: 600; margin: 0; }
.card { background: var(--bg-color-card); border-radius: var(--radius-lg); box-shadow: var(--shadow-sm); padding: var(--spacing-lg); }
@keyframes fadeIn { from { opacity: 0; } to { opacity: 1; } }
</style>
