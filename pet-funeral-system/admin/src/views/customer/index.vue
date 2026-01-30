<script setup lang="ts">
import { ref } from 'vue'

const searchKeyword = ref('')
const loading = ref(false)

const customers = ref([
  { id: 1, name: '李女士', phone: '138****8888', pets: 2, orders: 3, totalAmount: 8940, level: '金卡', createdAt: '2024-06-15' },
  { id: 2, name: '王先生', phone: '139****6666', pets: 1, orders: 1, totalAmount: 1280, level: '普通', createdAt: '2025-12-20' },
  { id: 3, name: '张女士', phone: '137****5555', pets: 3, orders: 5, totalAmount: 15800, level: '钻石', createdAt: '2023-03-10' },
  { id: 4, name: '陈先生', phone: '136****4444', pets: 1, orders: 2, totalAmount: 4260, level: '银卡', createdAt: '2024-09-25' },
  { id: 5, name: '刘女士', phone: '135****3333', pets: 2, orders: 2, totalAmount: 3560, level: '银卡', createdAt: '2024-11-08' },
])

const getLevelType = (level: string) => {
  const map: Record<string, string> = {
    '普通': 'info',
    '银卡': '',
    '金卡': 'warning',
    '钻石': 'danger',
  }
  return map[level] || 'info'
}
</script>

<template>
  <div class="customer-container">
    <div class="page-header">
      <h1 class="page-title">客户管理</h1>
      <el-button type="primary">
        <el-icon><Plus /></el-icon>
        新增客户
      </el-button>
    </div>

    <div class="card">
      <div class="search-bar">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索客户姓名/手机号"
          clearable
          style="width: 300px"
          :prefix-icon="Search"
        />
        <el-button type="primary" style="margin-left: 16px">搜索</el-button>
      </div>

      <el-table :data="customers" v-loading="loading" style="width: 100%; margin-top: 20px">
        <el-table-column prop="name" label="客户姓名" min-width="100" />
        <el-table-column prop="phone" label="手机号" min-width="120" />
        <el-table-column prop="pets" label="宠物数量" min-width="100" />
        <el-table-column prop="orders" label="订单数" min-width="100" />
        <el-table-column label="累计消费" min-width="120">
          <template #default="{ row }">
            <span style="color: #E8B89D; font-weight: 500">¥{{ row.totalAmount.toLocaleString() }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="level" label="会员等级" min-width="100">
          <template #default="{ row }">
            <el-tag :type="getLevelType(row.level)" effect="light">{{ row.level }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="注册时间" min-width="120" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default>
            <el-button type="primary" link>详情</el-button>
            <el-button type="primary" link>编辑</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script lang="ts">
import { Search, Plus } from '@element-plus/icons-vue'
export default { data() { return { Search, Plus } } }
</script>

<style lang="scss" scoped>
.customer-container { animation: fadeIn 0.3s ease; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: var(--spacing-lg); }
.page-title { font-size: 24px; font-weight: 600; color: var(--text-color-primary); margin: 0; }
.card { background-color: var(--bg-color-card); border-radius: var(--radius-lg); box-shadow: var(--shadow-sm); padding: var(--spacing-lg); }
.search-bar { display: flex; align-items: center; }
@keyframes fadeIn { from { opacity: 0; transform: translateY(10px); } to { opacity: 1; transform: translateY(0); } }
</style>
