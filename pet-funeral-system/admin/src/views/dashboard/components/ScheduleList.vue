<script setup lang="ts">
interface ScheduleItem {
  id: number
  time: string
  title: string
  location: string
  status: 'pending' | 'processing' | 'completed'
}

defineProps<{
  items: ScheduleItem[]
}>()

const getStatusText = (status: string) => {
  switch (status) {
    case 'pending':
      return '待处理'
    case 'processing':
      return '进行中'
    case 'completed':
      return '已完成'
    default:
      return status
  }
}
</script>

<template>
  <div class="schedule-list">
    <div
      v-for="item in items"
      :key="item.id"
      class="schedule-item"
    >
      <div class="schedule-time">
        <span class="time">{{ item.time }}</span>
      </div>
      <div class="schedule-line">
        <div class="dot" :class="item.status"></div>
        <div class="line"></div>
      </div>
      <div class="schedule-content">
        <div class="schedule-title">{{ item.title }}</div>
        <div class="schedule-location">
          <el-icon><Location /></el-icon>
          {{ item.location }}
        </div>
        <el-tag
          size="small"
          :type="item.status === 'processing' ? 'primary' : item.status === 'completed' ? 'success' : 'warning'"
          effect="light"
        >
          {{ getStatusText(item.status) }}
        </el-tag>
      </div>
    </div>

    <div v-if="items.length === 0" class="schedule-empty">
      <el-icon :size="48"><Calendar /></el-icon>
      <p>今日暂无排期</p>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.schedule-list {
  display: flex;
  flex-direction: column;
}

.schedule-item {
  display: flex;
  padding: var(--spacing-sm) 0;

  &:last-child {
    .schedule-line .line {
      display: none;
    }
  }
}

.schedule-time {
  width: 60px;
  flex-shrink: 0;

  .time {
    font-size: 14px;
    font-weight: 500;
    color: var(--text-color-primary);
  }
}

.schedule-line {
  width: 24px;
  display: flex;
  flex-direction: column;
  align-items: center;
  flex-shrink: 0;

  .dot {
    width: 10px;
    height: 10px;
    border-radius: 50%;
    background-color: var(--color-warning);

    &.processing {
      background-color: var(--color-primary);
      box-shadow: 0 0 0 4px rgba(139, 115, 85, 0.2);
    }

    &.completed {
      background-color: var(--color-success);
    }
  }

  .line {
    flex: 1;
    width: 2px;
    background-color: var(--border-color);
    margin-top: 4px;
  }
}

.schedule-content {
  flex: 1;
  padding-bottom: var(--spacing-md);
}

.schedule-title {
  font-size: 14px;
  font-weight: 500;
  color: var(--text-color-primary);
  margin-bottom: 4px;
}

.schedule-location {
  font-size: 12px;
  color: var(--text-color-secondary);
  display: flex;
  align-items: center;
  gap: 4px;
  margin-bottom: 8px;
}

.schedule-empty {
  text-align: center;
  padding: var(--spacing-xl);
  color: var(--text-color-secondary);

  p {
    margin-top: var(--spacing-sm);
  }
}
</style>
