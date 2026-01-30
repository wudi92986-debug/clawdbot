<script setup lang="ts">
defineProps<{
  title: string
  value: string | number
  trend: string
  trendUp: boolean
  icon: string
  type: string
}>()
</script>

<template>
  <div class="stat-card" :class="type">
    <div class="stat-icon">
      <el-icon :size="32">
        <component :is="icon" />
      </el-icon>
    </div>
    <div class="stat-content">
      <div class="stat-value">{{ value }}</div>
      <div class="stat-title">{{ title }}</div>
      <div class="stat-trend" :class="{ up: trendUp, down: !trendUp }">
        <el-icon v-if="trendUp"><Top /></el-icon>
        <el-icon v-else><Bottom /></el-icon>
        {{ trend }}
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.stat-card {
  background: linear-gradient(135deg, var(--color-primary-lighter), var(--color-primary));
  border-radius: var(--radius-lg);
  padding: var(--spacing-lg);
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
  color: #fff;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  cursor: pointer;

  &:hover {
    transform: translateY(-4px);
    box-shadow: var(--shadow-lg);
  }

  &.success {
    background: linear-gradient(135deg, #C4D9A8, #A8C686);
  }

  &.warning {
    background: linear-gradient(135deg, #F2D4C2, #E8B89D);
  }

  &.info {
    background: linear-gradient(135deg, #C8C9CC, #909399);
  }
}

.stat-icon {
  width: 64px;
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: rgba(255, 255, 255, 0.2);
  border-radius: var(--radius-md);
}

.stat-content {
  flex: 1;
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  line-height: 1.2;
}

.stat-title {
  font-size: 14px;
  opacity: 0.9;
  margin-top: 4px;
}

.stat-trend {
  font-size: 12px;
  margin-top: 8px;
  display: flex;
  align-items: center;
  gap: 4px;
  opacity: 0.9;

  &.up {
    color: #C4D9A8;
  }

  &.down {
    color: #F2C4C4;
  }
}
</style>
