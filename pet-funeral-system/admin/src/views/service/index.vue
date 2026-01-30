<script setup lang="ts">
import { ref } from 'vue'

const packages = ref([
  { id: 1, name: '基础告别', price: 1280, description: '集体火化，不保留骨灰', includes: ['遗容整理', '集体火化'], status: 'active' },
  { id: 2, name: '温馨告别', price: 2980, description: '含告别仪式+单独火化', includes: ['上门接运', '遗容整理', '告别仪式', '单独火化', '骨灰盒'], status: 'active' },
  { id: 3, name: '尊享告别', price: 5980, description: 'VIP全套服务+纪念相册', includes: ['专车接运', '高级美容', 'VIP告别厅', '单独火化', '高端骨灰盒', '纪念相册', 'AI纪念视频'], status: 'active' },
])
</script>

<template>
  <div class="service-container">
    <div class="page-header">
      <h1 class="page-title">服务套餐</h1>
      <el-button type="primary">
        <el-icon><Plus /></el-icon>
        新增套餐
      </el-button>
    </div>

    <div class="package-grid">
      <div v-for="pkg in packages" :key="pkg.id" class="package-card">
        <div class="package-header">
          <h3>{{ pkg.name }}</h3>
          <el-tag type="success" effect="light">上架中</el-tag>
        </div>
        <div class="package-price">
          <span class="currency">¥</span>
          <span class="amount">{{ pkg.price.toLocaleString() }}</span>
          <span class="unit">起</span>
        </div>
        <p class="package-desc">{{ pkg.description }}</p>
        <div class="package-includes">
          <div v-for="item in pkg.includes" :key="item" class="include-item">
            <el-icon color="#A8C686"><CircleCheckFilled /></el-icon>
            {{ item }}
          </div>
        </div>
        <div class="package-actions">
          <el-button type="primary" plain>编辑</el-button>
          <el-button>下架</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Plus } from '@element-plus/icons-vue'
export default { data() { return { Plus } } }
</script>

<style lang="scss" scoped>
.service-container { animation: fadeIn 0.3s ease; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: var(--spacing-lg); }
.page-title { font-size: 24px; font-weight: 600; color: var(--text-color-primary); margin: 0; }
.package-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(320px, 1fr)); gap: var(--spacing-lg); }
.package-card {
  background-color: var(--bg-color-card); border-radius: var(--radius-lg); box-shadow: var(--shadow-sm); padding: var(--spacing-xl);
  .package-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: var(--spacing-md);
    h3 { margin: 0; font-size: 20px; color: var(--text-color-primary); }
  }
  .package-price { margin-bottom: var(--spacing-md);
    .currency { font-size: 16px; color: var(--color-warning); }
    .amount { font-size: 32px; font-weight: 700; color: var(--color-warning); }
    .unit { font-size: 14px; color: var(--text-color-secondary); }
  }
  .package-desc { color: var(--text-color-secondary); margin-bottom: var(--spacing-md); }
  .package-includes { margin-bottom: var(--spacing-lg);
    .include-item { display: flex; align-items: center; gap: 8px; padding: 6px 0; font-size: 14px; color: var(--text-color-primary); }
  }
  .package-actions { display: flex; gap: var(--spacing-sm); }
}
@keyframes fadeIn { from { opacity: 0; transform: translateY(10px); } to { opacity: 1; transform: translateY(0); } }
</style>
