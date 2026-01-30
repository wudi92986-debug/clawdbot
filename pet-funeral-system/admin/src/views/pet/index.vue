<script setup lang="ts">
import { ref } from 'vue'

const pets = ref([
  { id: 1, name: '豆豆', species: '狗', breed: '金毛寻回犬', owner: '李女士', status: 'deceased', deathDate: '2025-12-20' },
  { id: 2, name: '咪咪', species: '猫', breed: '英国短毛猫', owner: '王先生', status: 'deceased', deathDate: '2026-01-25' },
  { id: 3, name: '球球', species: '狗', breed: '泰迪', owner: '张女士', status: 'deceased', deathDate: '2026-01-15' },
  { id: 4, name: '小白', species: '兔', breed: '垂耳兔', owner: '刘女士', status: 'deceased', deathDate: '2025-11-10' },
  { id: 5, name: '旺财', species: '狗', breed: '柴犬', owner: '陈先生', status: 'deceased', deathDate: '2026-01-28' },
])
</script>

<template>
  <div class="pet-container">
    <div class="page-header">
      <h1 class="page-title">宠物档案</h1>
    </div>

    <div class="card">
      <el-table :data="pets" style="width: 100%">
        <el-table-column label="宠物" min-width="150">
          <template #default="{ row }">
            <div style="display: flex; align-items: center; gap: 12px">
              <el-avatar :size="40">{{ row.species === '狗' ? '🐕' : row.species === '猫' ? '🐱' : '🐰' }}</el-avatar>
              <div>
                <div style="font-weight: 500">{{ row.name }}</div>
                <div style="font-size: 12px; color: #909399">{{ row.breed }}</div>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="species" label="物种" width="80" />
        <el-table-column prop="owner" label="主人" min-width="100" />
        <el-table-column prop="deathDate" label="离世日期" min-width="120" />
        <el-table-column label="状态" width="100">
          <template #default>
            <el-tag type="info" effect="light">已离世</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default>
            <el-button type="primary" link>查看档案</el-button>
            <el-button type="primary" link>纪念馆</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.pet-container { animation: fadeIn 0.3s ease; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: var(--spacing-lg); }
.page-title { font-size: 24px; font-weight: 600; color: var(--text-color-primary); margin: 0; }
.card { background-color: var(--bg-color-card); border-radius: var(--radius-lg); box-shadow: var(--shadow-sm); padding: var(--spacing-lg); }
@keyframes fadeIn { from { opacity: 0; transform: translateY(10px); } to { opacity: 1; transform: translateY(0); } }
</style>
