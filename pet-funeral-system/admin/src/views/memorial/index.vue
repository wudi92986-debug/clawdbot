<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

// 搜索
const searchKeyword = ref('')
const statusFilter = ref('')

// 纪念馆列表
const memorials = ref([
  {
    id: 1,
    petName: '豆豆',
    petAvatar: '',
    petEmoji: '🐕',
    lifespan: '2018 - 2025',
    candleCount: 258,
    flowerCount: 1234,
    visitCount: 3567,
    status: 'active',
    createdAt: '2025-12-25',
  },
  {
    id: 2,
    petName: '咪咪',
    petAvatar: '',
    petEmoji: '🐱',
    lifespan: '2020 - 2026',
    candleCount: 89,
    flowerCount: 567,
    visitCount: 1234,
    status: 'active',
    createdAt: '2026-01-20',
  },
  {
    id: 3,
    petName: '球球',
    petAvatar: '',
    petEmoji: '🐕',
    lifespan: '2015 - 2026',
    candleCount: 456,
    flowerCount: 2345,
    visitCount: 5678,
    status: 'active',
    createdAt: '2026-01-15',
  },
  {
    id: 4,
    petName: '小白',
    petAvatar: '',
    petEmoji: '🐰',
    lifespan: '2019 - 2025',
    candleCount: 123,
    flowerCount: 890,
    visitCount: 2134,
    status: 'active',
    createdAt: '2025-11-10',
  },
  {
    id: 5,
    petName: '旺财',
    petAvatar: '',
    petEmoji: '🐕',
    lifespan: '2017 - 2026',
    candleCount: 67,
    flowerCount: 345,
    visitCount: 876,
    status: 'active',
    createdAt: '2026-01-28',
  },
  {
    id: 6,
    petName: '橘子',
    petAvatar: '',
    petEmoji: '🐱',
    lifespan: '2018 - 2025',
    candleCount: 189,
    flowerCount: 678,
    visitCount: 1567,
    status: 'active',
    createdAt: '2025-10-05',
  },
  {
    id: 7,
    petName: '小绿',
    petAvatar: '',
    petEmoji: '🦜',
    lifespan: '2020 - 2025',
    candleCount: 34,
    flowerCount: 123,
    visitCount: 456,
    status: 'inactive',
    createdAt: '2025-08-20',
  },
  {
    id: 8,
    petName: '大黄',
    petAvatar: '',
    petEmoji: '🐕',
    lifespan: '2014 - 2025',
    candleCount: 567,
    flowerCount: 3456,
    visitCount: 8901,
    status: 'active',
    createdAt: '2025-06-15',
  },
])

// 分页
const pagination = ref({
  page: 1,
  pageSize: 8,
  total: 45,
})

// 格式化数字
const formatCount = (count: number) => {
  if (count >= 1000) {
    return (count / 1000).toFixed(1) + 'k'
  }
  return count.toString()
}

// 查看详情
const handleView = (memorial: any) => {
  router.push(`/memorial/detail/${memorial.id}`)
}

// 编辑
const handleEdit = (memorial: any) => {
  // TODO: 打开编辑弹窗
  ElMessage.info('编辑功能开发中...')
}

// 访问纪念馆
const handleVisit = (memorial: any) => {
  window.open(`/memorial/${memorial.id}`, '_blank')
}
</script>

<template>
  <div class="memorial-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1 class="page-title">纪念馆管理</h1>
      <el-button type="primary">
        <el-icon><Plus /></el-icon>
        创建纪念馆
      </el-button>
    </div>

    <!-- 搜索区域 -->
    <div class="card search-card">
      <el-input
        v-model="searchKeyword"
        placeholder="搜索纪念馆/宠物名"
        clearable
        style="width: 300px"
        :prefix-icon="Search"
      />
      <el-select v-model="statusFilter" placeholder="状态" clearable style="width: 120px; margin-left: 16px">
        <el-option label="全部" value="" />
        <el-option label="已上线" value="active" />
        <el-option label="已下线" value="inactive" />
      </el-select>
    </div>

    <!-- 纪念馆卡片列表 -->
    <div class="memorial-grid">
      <div
        v-for="memorial in memorials"
        :key="memorial.id"
        class="memorial-card"
        @click="handleView(memorial)"
      >
        <!-- 头像区域 -->
        <div class="memorial-avatar">
          <div class="avatar-wrapper">
            <span class="pet-emoji">{{ memorial.petEmoji }}</span>
          </div>
          <div class="status-badge" :class="memorial.status">
            {{ memorial.status === 'active' ? '已上线' : '已下线' }}
          </div>
        </div>

        <!-- 信息区域 -->
        <div class="memorial-info">
          <h3 class="pet-name">{{ memorial.petName }}</h3>
          <p class="lifespan">{{ memorial.lifespan }}</p>
        </div>

        <!-- 统计区域 -->
        <div class="memorial-stats">
          <div class="stat-item">
            <span class="stat-icon">🕯️</span>
            <span class="stat-value">{{ formatCount(memorial.candleCount) }}</span>
          </div>
          <div class="stat-item">
            <span class="stat-icon">🌸</span>
            <span class="stat-value">{{ formatCount(memorial.flowerCount) }}</span>
          </div>
          <div class="stat-item">
            <span class="stat-icon">👁️</span>
            <span class="stat-value">{{ formatCount(memorial.visitCount) }}</span>
          </div>
        </div>

        <!-- 操作区域 -->
        <div class="memorial-actions">
          <el-button type="primary" link size="small" @click.stop="handleView(memorial)">
            详情
          </el-button>
          <el-button type="primary" link size="small" @click.stop="handleEdit(memorial)">
            编辑
          </el-button>
          <el-button type="primary" link size="small" @click.stop="handleVisit(memorial)">
            访问
          </el-button>
        </div>
      </div>
    </div>

    <!-- 分页 -->
    <div class="pagination-wrapper">
      <el-pagination
        v-model:current-page="pagination.page"
        v-model:page-size="pagination.pageSize"
        :total="pagination.total"
        layout="prev, pager, next"
        background
      />
      <span class="total-text">共 {{ pagination.total }} 个纪念馆</span>
    </div>
  </div>
</template>

<script lang="ts">
import { Search, Plus } from '@element-plus/icons-vue'
export default {
  data() {
    return { Search, Plus }
  }
}
</script>

<style lang="scss" scoped>
.memorial-container {
  animation: fadeIn 0.3s ease;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--spacing-lg);
}

.page-title {
  font-size: 24px;
  font-weight: 600;
  color: var(--text-color-primary);
  margin: 0;
}

.card {
  background-color: var(--bg-color-card);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-sm);
  padding: var(--spacing-lg);
}

.search-card {
  margin-bottom: var(--spacing-lg);
  display: flex;
  align-items: center;
}

.memorial-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
  gap: var(--spacing-lg);
  margin-bottom: var(--spacing-lg);
}

.memorial-card {
  background-color: var(--bg-color-card);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-sm);
  padding: var(--spacing-lg);
  cursor: pointer;
  transition: all 0.3s ease;

  &:hover {
    transform: translateY(-4px);
    box-shadow: var(--shadow-lg);
  }
}

.memorial-avatar {
  position: relative;
  display: flex;
  justify-content: center;
  margin-bottom: var(--spacing-md);

  .avatar-wrapper {
    width: 80px;
    height: 80px;
    border-radius: 50%;
    background: linear-gradient(135deg, var(--color-primary-lighter), var(--color-primary-light));
    display: flex;
    align-items: center;
    justify-content: center;

    .pet-emoji {
      font-size: 40px;
    }
  }

  .status-badge {
    position: absolute;
    top: 0;
    right: 20%;
    padding: 2px 8px;
    border-radius: 10px;
    font-size: 10px;
    color: #fff;

    &.active {
      background-color: var(--color-success);
    }

    &.inactive {
      background-color: var(--color-info);
    }
  }
}

.memorial-info {
  text-align: center;
  margin-bottom: var(--spacing-md);

  .pet-name {
    font-size: 18px;
    font-weight: 600;
    color: var(--text-color-primary);
    margin: 0 0 4px;
  }

  .lifespan {
    font-size: 13px;
    color: var(--text-color-secondary);
    margin: 0;
  }
}

.memorial-stats {
  display: flex;
  justify-content: center;
  gap: var(--spacing-lg);
  padding: var(--spacing-md) 0;
  border-top: 1px solid var(--border-color-light);
  border-bottom: 1px solid var(--border-color-light);

  .stat-item {
    display: flex;
    flex-direction: column;
    align-items: center;

    .stat-icon {
      font-size: 18px;
      margin-bottom: 4px;
    }

    .stat-value {
      font-size: 14px;
      font-weight: 500;
      color: var(--text-color-primary);
    }
  }
}

.memorial-actions {
  display: flex;
  justify-content: center;
  gap: var(--spacing-sm);
  margin-top: var(--spacing-md);
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: var(--spacing-md);

  .total-text {
    color: var(--text-color-secondary);
    font-size: 14px;
  }
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
