<script setup lang="ts">
import { useRouter } from 'vue-router'

interface TodoItem {
  id: number
  type: 'danger' | 'warning' | 'info'
  text: string
  link: string
}

defineProps<{
  items: TodoItem[]
}>()

const router = useRouter()

const getTypeIcon = (type: string) => {
  switch (type) {
    case 'danger':
      return 'WarningFilled'
    case 'warning':
      return 'Clock'
    default:
      return 'InfoFilled'
  }
}
</script>

<template>
  <div class="todo-list">
    <div
      v-for="item in items"
      :key="item.id"
      class="todo-item"
      :class="item.type"
      @click="router.push(item.link)"
    >
      <div class="todo-icon">
        <el-icon>
          <component :is="getTypeIcon(item.type)" />
        </el-icon>
      </div>
      <span class="todo-text">{{ item.text }}</span>
      <el-icon class="todo-arrow"><ArrowRight /></el-icon>
    </div>

    <div v-if="items.length === 0" class="todo-empty">
      <el-icon :size="48"><CircleCheck /></el-icon>
      <p>暂无待办事项</p>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.todo-list {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-sm);
}

.todo-item {
  display: flex;
  align-items: center;
  padding: var(--spacing-md);
  background-color: var(--bg-color-page);
  border-radius: var(--radius-md);
  cursor: pointer;
  transition: all 0.3s ease;

  &:hover {
    transform: translateX(4px);
    
    .todo-arrow {
      opacity: 1;
    }
  }

  &.danger {
    border-left: 3px solid var(--color-danger);

    .todo-icon {
      color: var(--color-danger);
    }
  }

  &.warning {
    border-left: 3px solid var(--color-warning);

    .todo-icon {
      color: var(--color-warning);
    }
  }

  &.info {
    border-left: 3px solid var(--color-info);

    .todo-icon {
      color: var(--color-info);
    }
  }
}

.todo-icon {
  font-size: 18px;
  margin-right: var(--spacing-sm);
}

.todo-text {
  flex: 1;
  font-size: 14px;
  color: var(--text-color-primary);
}

.todo-arrow {
  opacity: 0;
  color: var(--text-color-secondary);
  transition: opacity 0.3s ease;
}

.todo-empty {
  text-align: center;
  padding: var(--spacing-xl);
  color: var(--text-color-secondary);

  p {
    margin-top: var(--spacing-sm);
  }
}
</style>
