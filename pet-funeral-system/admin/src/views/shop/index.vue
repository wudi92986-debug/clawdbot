<script setup lang="ts">
import { ref } from 'vue'

const products = ref([
  { id: 1, name: '心形骨灰晶石吊坠', category: '骨灰制品', price: 1280, stock: 50, sales: 156, status: 'active' },
  { id: 2, name: 'AI宠物艺术画像', category: '定制画像', price: 399, stock: 999, sales: 234, status: 'active' },
  { id: 3, name: '毛发琥珀纪念吊坠', category: '毛发纪念', price: 880, stock: 30, sales: 89, status: 'active' },
  { id: 4, name: '3D打印宠物摆件', category: '3D打印', price: 599, stock: 20, sales: 67, status: 'active' },
  { id: 5, name: '精装纪念相册', category: '纪念相册', price: 299, stock: 100, sales: 312, status: 'active' },
])
</script>

<template>
  <div class="shop-container">
    <div class="page-header">
      <h1 class="page-title">纪念商城</h1>
      <el-button type="primary"><el-icon><Plus /></el-icon>添加商品</el-button>
    </div>
    <div class="card">
      <el-table :data="products" style="width: 100%">
        <el-table-column prop="name" label="商品名称" min-width="180" />
        <el-table-column prop="category" label="分类" width="120" />
        <el-table-column label="价格" width="100">
          <template #default="{ row }"><span style="color: #E8B89D; font-weight: 500">¥{{ row.price }}</span></template>
        </el-table-column>
        <el-table-column prop="stock" label="库存" width="80" />
        <el-table-column prop="sales" label="销量" width="80" />
        <el-table-column label="状态" width="100">
          <template #default><el-tag type="success" effect="light">上架中</el-tag></template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template #default><el-button type="primary" link>编辑</el-button><el-button type="primary" link>下架</el-button></template>
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
.shop-container { animation: fadeIn 0.3s ease; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: var(--spacing-lg); }
.page-title { font-size: 24px; font-weight: 600; margin: 0; }
.card { background: var(--bg-color-card); border-radius: var(--radius-lg); box-shadow: var(--shadow-sm); padding: var(--spacing-lg); }
@keyframes fadeIn { from { opacity: 0; } to { opacity: 1; } }
</style>
