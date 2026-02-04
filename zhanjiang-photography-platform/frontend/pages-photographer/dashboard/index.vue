<template>
  <view class="page">
    <!-- 数据概览 -->
    <view class="overview-section">
      <view class="section-title">今日数据</view>
      <view class="overview-grid">
        <view class="overview-item">
          <text class="item-value">{{ todayData.views }}</text>
          <text class="item-label">访问量</text>
        </view>
        <view class="overview-item">
          <text class="item-value">{{ todayData.newOrders }}</text>
          <text class="item-label">新订单</text>
        </view>
        <view class="overview-item">
          <text class="item-value">¥{{ todayData.income }}</text>
          <text class="item-label">收入</text>
        </view>
        <view class="overview-item">
          <text class="item-value">{{ todayData.newFans }}</text>
          <text class="item-label">新粉丝</text>
        </view>
      </view>
    </view>
    
    <!-- 快捷入口 -->
    <view class="quick-section">
      <view class="quick-grid">
        <view class="quick-item" @click="goOrderManage">
          <view class="quick-icon order">
            <image src="/static/icons/order-manage.png" mode="aspectFit" />
            <view class="badge" v-if="pendingOrders">{{ pendingOrders }}</view>
          </view>
          <text>订单管理</text>
        </view>
        <view class="quick-item" @click="goWorksManage">
          <view class="quick-icon works">
            <image src="/static/icons/works-manage.png" mode="aspectFit" />
          </view>
          <text>作品管理</text>
        </view>
        <view class="quick-item" @click="goSchedule">
          <view class="quick-icon schedule">
            <image src="/static/icons/schedule.png" mode="aspectFit" />
          </view>
          <text>档期管理</text>
        </view>
        <view class="quick-item" @click="goWallet">
          <view class="quick-icon wallet">
            <image src="/static/icons/wallet.png" mode="aspectFit" />
          </view>
          <text>钱包</text>
        </view>
      </view>
    </view>
    
    <!-- 待处理订单 -->
    <view class="section">
      <view class="section-header">
        <text class="section-title">待处理订单</text>
        <text class="section-more" @click="goOrderManage">查看全部</text>
      </view>
      <view class="order-list">
        <view class="order-item" v-for="order in pendingOrderList" :key="order.id" @click="goOrderDetail(order.id)">
          <view class="order-info">
            <text class="order-title">{{ order.orderTitle }}</text>
            <text class="order-time">{{ order.shootDate }} {{ order.shootTimeSlot }}</text>
          </view>
          <view class="order-status" :class="getStatusClass(order.status)">
            {{ getStatusText(order.status) }}
          </view>
        </view>
        <view class="empty" v-if="!pendingOrderList.length">
          <text>暂无待处理订单</text>
        </view>
      </view>
    </view>
    
    <!-- 收入统计 -->
    <view class="section">
      <view class="section-header">
        <text class="section-title">本月收入</text>
      </view>
      <view class="income-card">
        <view class="income-main">
          <text class="income-label">总收入</text>
          <view class="income-value">
            <text class="symbol">¥</text>
            <text class="amount">{{ monthIncome.total }}</text>
          </view>
        </view>
        <view class="income-detail">
          <view class="detail-item">
            <text class="detail-label">完成订单</text>
            <text class="detail-value">{{ monthIncome.orders }}单</text>
          </view>
          <view class="detail-item">
            <text class="detail-label">平台服务费</text>
            <text class="detail-value">-¥{{ monthIncome.fee }}</text>
          </view>
          <view class="detail-item">
            <text class="detail-label">实际到账</text>
            <text class="detail-value highlight">¥{{ monthIncome.actual }}</text>
          </view>
        </view>
      </view>
    </view>
    
    <!-- 功能菜单 -->
    <view class="section menu-section">
      <view class="menu-item" @click="goPackageManage">
        <image class="menu-icon" src="/static/icons/package.png" mode="aspectFit" />
        <text class="menu-text">套餐管理</text>
        <image class="menu-arrow" src="/static/icons/arrow-right.png" mode="aspectFit" />
      </view>
      <view class="menu-item" @click="goDemandSquare">
        <image class="menu-icon" src="/static/icons/demand-square.png" mode="aspectFit" />
        <text class="menu-text">需求广场</text>
        <view class="menu-badge" v-if="newDemands">{{ newDemands }}条新需求</view>
        <image class="menu-arrow" src="/static/icons/arrow-right.png" mode="aspectFit" />
      </view>
      <view class="menu-item" @click="goProfileEdit">
        <image class="menu-icon" src="/static/icons/profile-edit.png" mode="aspectFit" />
        <text class="menu-text">编辑主页</text>
        <image class="menu-arrow" src="/static/icons/arrow-right.png" mode="aspectFit" />
      </view>
      <view class="menu-item" @click="goDataAnalysis">
        <image class="menu-icon" src="/static/icons/data.png" mode="aspectFit" />
        <text class="menu-text">数据分析</text>
        <image class="menu-arrow" src="/static/icons/arrow-right.png" mode="aspectFit" />
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const todayData = ref({
  views: 58,
  newOrders: 2,
  income: 598,
  newFans: 3
})

const pendingOrders = ref(3)
const pendingOrderList = ref([
  {
    id: 1,
    orderTitle: '精选人像套餐',
    shootDate: '2026-02-10',
    shootTimeSlot: '14:00-17:00',
    status: 10
  },
  {
    id: 2,
    orderTitle: '毕业照基础套餐',
    shootDate: '2026-02-12',
    shootTimeSlot: '09:00-12:00',
    status: 20
  }
])

const monthIncome = ref({
  total: 2350,
  orders: 8,
  fee: 235,
  actual: 2115
})

const newDemands = ref(5)

const getStatusClass = (status) => {
  const map = {
    10: 'pending',
    20: 'accepted',
    30: 'shooting',
    40: 'selecting',
    50: 'delivering'
  }
  return map[status] || ''
}

const getStatusText = (status) => {
  const map = {
    10: '待接单',
    20: '待拍摄',
    30: '拍摄中',
    40: '待选片',
    50: '待交付'
  }
  return map[status] || ''
}

// 页面跳转
const goOrderManage = () => {
  uni.navigateTo({ url: '/pages-photographer/orders/index' })
}

const goWorksManage = () => {
  uni.navigateTo({ url: '/pages-photographer/works/manage' })
}

const goSchedule = () => {
  uni.navigateTo({ url: '/pages-photographer/schedule/index' })
}

const goWallet = () => {
  uni.navigateTo({ url: '/pages-photographer/wallet/index' })
}

const goOrderDetail = (id) => {
  uni.navigateTo({ url: `/pages/order/detail?id=${id}` })
}

const goPackageManage = () => {
  uni.navigateTo({ url: '/pages-photographer/packages/index' })
}

const goDemandSquare = () => {
  uni.navigateTo({ url: '/pages/demand/list' })
}

const goProfileEdit = () => {
  uni.navigateTo({ url: '/pages-photographer/profile/edit' })
}

const goDataAnalysis = () => {
  uni.navigateTo({ url: '/pages-photographer/data/index' })
}

onMounted(() => {
  // TODO: 获取真实数据
})
</script>

<style lang="scss" scoped>
@import '@/styles/variables.scss';

.page {
  min-height: 100vh;
  background: $bg-secondary;
  padding-bottom: $spacing-xl;
}

// 数据概览
.overview-section {
  padding: $spacing-lg;
  background: linear-gradient(135deg, $primary-color, $primary-light);
  
  .section-title {
    font-size: $font-md;
    color: rgba(255, 255, 255, 0.8);
    margin-bottom: $spacing-md;
  }
  
  .overview-grid {
    display: flex;
    
    .overview-item {
      flex: 1;
      text-align: center;
      
      .item-value {
        display: block;
        font-size: $font-xxl;
        font-weight: $font-bold;
        color: $text-white;
      }
      
      .item-label {
        display: block;
        margin-top: 8rpx;
        font-size: $font-xs;
        color: rgba(255, 255, 255, 0.7);
      }
    }
  }
}

// 快捷入口
.quick-section {
  margin: -40rpx $spacing-lg 0;
  padding: $spacing-lg;
  background: $bg-primary;
  border-radius: $radius-lg;
  box-shadow: $shadow-md;
  
  .quick-grid {
    display: flex;
    
    .quick-item {
      flex: 1;
      display: flex;
      flex-direction: column;
      align-items: center;
      
      .quick-icon {
        position: relative;
        width: 96rpx;
        height: 96rpx;
        display: flex;
        align-items: center;
        justify-content: center;
        border-radius: $radius-lg;
        
        &.order { background: rgba($primary-color, 0.1); }
        &.works { background: rgba($success-color, 0.1); }
        &.schedule { background: rgba($warning-color, 0.1); }
        &.wallet { background: rgba($info-color, 0.1); }
        
        image {
          width: 48rpx;
          height: 48rpx;
        }
        
        .badge {
          position: absolute;
          top: -8rpx;
          right: -8rpx;
          min-width: 32rpx;
          height: 32rpx;
          padding: 0 8rpx;
          background: $danger-color;
          border-radius: 16rpx;
          font-size: $font-xs;
          color: $text-white;
          text-align: center;
          line-height: 32rpx;
        }
      }
      
      text {
        margin-top: $spacing-sm;
        font-size: $font-sm;
        color: $text-secondary;
      }
    }
  }
}

// 区块
.section {
  margin: $spacing-lg;
  background: $bg-primary;
  border-radius: $radius-lg;
  overflow: hidden;
  
  .section-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: $spacing-lg;
    border-bottom: 1rpx solid $border-light;
    
    .section-title {
      font-size: $font-lg;
      font-weight: $font-semibold;
      color: $text-primary;
    }
    
    .section-more {
      font-size: $font-sm;
      color: $primary-color;
    }
  }
}

// 订单列表
.order-list {
  .order-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: $spacing-lg;
    
    & + .order-item {
      border-top: 1rpx solid $border-light;
    }
    
    .order-info {
      .order-title {
        display: block;
        font-size: $font-md;
        color: $text-primary;
      }
      
      .order-time {
        display: block;
        margin-top: 8rpx;
        font-size: $font-sm;
        color: $text-secondary;
      }
    }
    
    .order-status {
      padding: 8rpx 16rpx;
      border-radius: $radius-sm;
      font-size: $font-sm;
      
      &.pending {
        background: rgba($warning-color, 0.1);
        color: $warning-color;
      }
      
      &.accepted {
        background: rgba($primary-color, 0.1);
        color: $primary-color;
      }
      
      &.shooting {
        background: rgba($success-color, 0.1);
        color: $success-color;
      }
    }
  }
  
  .empty {
    padding: $spacing-xl;
    text-align: center;
    font-size: $font-sm;
    color: $text-tertiary;
  }
}

// 收入卡片
.income-card {
  padding: $spacing-lg;
  
  .income-main {
    text-align: center;
    padding-bottom: $spacing-lg;
    border-bottom: 1rpx solid $border-light;
    
    .income-label {
      font-size: $font-sm;
      color: $text-secondary;
    }
    
    .income-value {
      margin-top: $spacing-sm;
      
      .symbol {
        font-size: $font-lg;
        color: $text-primary;
      }
      
      .amount {
        font-size: 64rpx;
        font-weight: $font-bold;
        color: $text-primary;
      }
    }
  }
  
  .income-detail {
    padding-top: $spacing-lg;
    
    .detail-item {
      display: flex;
      align-items: center;
      justify-content: space-between;
      
      & + .detail-item {
        margin-top: $spacing-sm;
      }
      
      .detail-label {
        font-size: $font-sm;
        color: $text-secondary;
      }
      
      .detail-value {
        font-size: $font-sm;
        color: $text-primary;
        
        &.highlight {
          color: $success-color;
          font-weight: $font-semibold;
        }
      }
    }
  }
}

// 菜单
.menu-section {
  .menu-item {
    display: flex;
    align-items: center;
    padding: $spacing-lg;
    
    & + .menu-item {
      border-top: 1rpx solid $border-light;
    }
    
    .menu-icon {
      width: 44rpx;
      height: 44rpx;
    }
    
    .menu-text {
      flex: 1;
      margin-left: $spacing-md;
      font-size: $font-md;
      color: $text-primary;
    }
    
    .menu-badge {
      margin-right: $spacing-sm;
      padding: 4rpx 12rpx;
      background: rgba($primary-color, 0.1);
      border-radius: $radius-sm;
      font-size: $font-xs;
      color: $primary-color;
    }
    
    .menu-arrow {
      width: 28rpx;
      height: 28rpx;
      opacity: 0.3;
    }
  }
}
</style>
