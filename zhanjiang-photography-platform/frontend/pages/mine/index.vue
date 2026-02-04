<template>
  <view class="page">
    <!-- 顶部个人信息区 -->
    <view class="header-section">
      <view class="header-bg"></view>
      <view class="user-card">
        <view class="user-info" @click="goLogin" v-if="!isLoggedIn">
          <image class="avatar" src="/static/images/default-avatar.png" mode="aspectFill" />
          <view class="user-text">
            <text class="nickname">点击登录</text>
            <text class="desc">登录后享受更多服务</text>
          </view>
        </view>
        <view class="user-info" v-else>
          <image class="avatar" :src="userInfo.avatar || '/static/images/default-avatar.png'" mode="aspectFill" />
          <view class="user-text">
            <view class="name-row">
              <text class="nickname">{{ userInfo.nickname }}</text>
              <image class="verified" v-if="userInfo.isVerified" src="/static/icons/verified.png" mode="aspectFit" />
            </view>
            <text class="desc">{{ userInfo.phone || '未绑定手机号' }}</text>
          </view>
          <image class="arrow" src="/static/icons/arrow-right.png" mode="aspectFit" @click="goEditProfile" />
        </view>
        
        <!-- 用户数据统计 -->
        <view class="user-stats" v-if="isLoggedIn">
          <view class="stat-item" @click="goFollows">
            <text class="stat-value">{{ stats.followCount || 0 }}</text>
            <text class="stat-label">关注</text>
          </view>
          <view class="stat-item" @click="goFans">
            <text class="stat-value">{{ stats.fansCount || 0 }}</text>
            <text class="stat-label">粉丝</text>
          </view>
          <view class="stat-item" @click="goFavorites">
            <text class="stat-value">{{ stats.favoriteCount || 0 }}</text>
            <text class="stat-label">收藏</text>
          </view>
          <view class="stat-item" @click="goPosts">
            <text class="stat-value">{{ stats.postCount || 0 }}</text>
            <text class="stat-label">动态</text>
          </view>
        </view>
      </view>
    </view>
    
    <!-- 订单区域 -->
    <view class="section order-section" v-if="isLoggedIn">
      <view class="section-header">
        <text class="section-title">我的订单</text>
        <view class="section-more" @click="goOrderList()">
          <text>全部订单</text>
          <image src="/static/icons/arrow-right.png" mode="aspectFit" />
        </view>
      </view>
      <view class="order-tabs">
        <view class="order-tab" @click="goOrderList(10)">
          <view class="tab-icon-wrap">
            <image src="/static/icons/order-wait.png" mode="aspectFit" />
            <view class="badge" v-if="orderCounts.waitAccept">{{ orderCounts.waitAccept }}</view>
          </view>
          <text>待接单</text>
        </view>
        <view class="order-tab" @click="goOrderList(20)">
          <view class="tab-icon-wrap">
            <image src="/static/icons/order-shoot.png" mode="aspectFit" />
            <view class="badge" v-if="orderCounts.waitShoot">{{ orderCounts.waitShoot }}</view>
          </view>
          <text>待拍摄</text>
        </view>
        <view class="order-tab" @click="goOrderList(50)">
          <view class="tab-icon-wrap">
            <image src="/static/icons/order-delivery.png" mode="aspectFit" />
            <view class="badge" v-if="orderCounts.waitDelivery">{{ orderCounts.waitDelivery }}</view>
          </view>
          <text>待交付</text>
        </view>
        <view class="order-tab" @click="goOrderList(70)">
          <view class="tab-icon-wrap">
            <image src="/static/icons/order-review.png" mode="aspectFit" />
            <view class="badge" v-if="orderCounts.waitReview">{{ orderCounts.waitReview }}</view>
          </view>
          <text>待评价</text>
        </view>
      </view>
    </view>
    
    <!-- 摄影师入口 -->
    <view class="section photographer-entry" v-if="isLoggedIn && !isPhotographer" @click="goApplyPhotographer">
      <view class="entry-content">
        <image class="entry-icon" src="/static/icons/camera-pro.png" mode="aspectFit" />
        <view class="entry-text">
          <text class="entry-title">成为摄影师</text>
          <text class="entry-desc">展示作品，接单赚钱</text>
        </view>
      </view>
      <image class="entry-arrow" src="/static/icons/arrow-right.png" mode="aspectFit" />
    </view>
    
    <!-- 摄影师工作台入口 -->
    <view class="section photographer-entry" v-if="isLoggedIn && isPhotographer" @click="goPhotographerDashboard">
      <view class="entry-content">
        <image class="entry-icon" src="/static/icons/dashboard.png" mode="aspectFit" />
        <view class="entry-text">
          <text class="entry-title">摄影师工作台</text>
          <text class="entry-desc">管理订单、作品、档期</text>
        </view>
      </view>
      <image class="entry-arrow" src="/static/icons/arrow-right.png" mode="aspectFit" />
    </view>
    
    <!-- 功能菜单 -->
    <view class="section menu-section">
      <view class="menu-group">
        <view class="menu-item" @click="goMyDemands">
          <image class="menu-icon" src="/static/icons/demand.png" mode="aspectFit" />
          <text class="menu-text">我的需求</text>
          <image class="menu-arrow" src="/static/icons/arrow-right.png" mode="aspectFit" />
        </view>
        <view class="menu-item" @click="goFavorites">
          <image class="menu-icon" src="/static/icons/favorite.png" mode="aspectFit" />
          <text class="menu-text">我的收藏</text>
          <image class="menu-arrow" src="/static/icons/arrow-right.png" mode="aspectFit" />
        </view>
        <view class="menu-item" @click="goFollows">
          <image class="menu-icon" src="/static/icons/follow.png" mode="aspectFit" />
          <text class="menu-text">我的关注</text>
          <image class="menu-arrow" src="/static/icons/arrow-right.png" mode="aspectFit" />
        </view>
      </view>
      
      <view class="menu-group">
        <view class="menu-item" @click="goCoupons">
          <image class="menu-icon" src="/static/icons/coupon.png" mode="aspectFit" />
          <text class="menu-text">优惠券</text>
          <text class="menu-badge" v-if="couponCount">{{ couponCount }}张可用</text>
          <image class="menu-arrow" src="/static/icons/arrow-right.png" mode="aspectFit" />
        </view>
        <view class="menu-item" @click="goEmergencyContacts">
          <image class="menu-icon" src="/static/icons/emergency.png" mode="aspectFit" />
          <text class="menu-text">紧急联系人</text>
          <image class="menu-arrow" src="/static/icons/arrow-right.png" mode="aspectFit" />
        </view>
      </view>
      
      <view class="menu-group">
        <view class="menu-item" @click="goHelp">
          <image class="menu-icon" src="/static/icons/help.png" mode="aspectFit" />
          <text class="menu-text">帮助与反馈</text>
          <image class="menu-arrow" src="/static/icons/arrow-right.png" mode="aspectFit" />
        </view>
        <view class="menu-item" @click="goSettings">
          <image class="menu-icon" src="/static/icons/settings.png" mode="aspectFit" />
          <text class="menu-text">设置</text>
          <image class="menu-arrow" src="/static/icons/arrow-right.png" mode="aspectFit" />
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useUserStore } from '@/store/user'

const userStore = useUserStore()

const isLoggedIn = computed(() => userStore.isLoggedIn)
const userInfo = computed(() => userStore.userInfo || {})
const isPhotographer = computed(() => userStore.isPhotographer)

const stats = ref({
  followCount: 0,
  fansCount: 0,
  favoriteCount: 0,
  postCount: 0
})

const orderCounts = ref({
  waitAccept: 0,
  waitShoot: 0,
  waitDelivery: 0,
  waitReview: 0
})

const couponCount = ref(0)

// 登录
const goLogin = async () => {
  try {
    await userStore.login()
    uni.showToast({ title: '登录成功', icon: 'success' })
  } catch (err) {
    console.error('登录失败', err)
  }
}

// 页面跳转
const goEditProfile = () => {
  uni.navigateTo({ url: '/pages/mine/settings' })
}

const goOrderList = (status) => {
  let url = '/pages/order/list'
  if (status) {
    url += `?status=${status}`
  }
  uni.navigateTo({ url })
}

const goFollows = () => {
  uni.navigateTo({ url: '/pages/mine/follows' })
}

const goFans = () => {
  uni.navigateTo({ url: '/pages/mine/follows?type=fans' })
}

const goFavorites = () => {
  uni.navigateTo({ url: '/pages/mine/favorites' })
}

const goPosts = () => {
  uni.navigateTo({ url: '/pages/community/index?userId=' + userInfo.value.userId })
}

const goMyDemands = () => {
  uni.navigateTo({ url: '/pages/demand/list?mine=1' })
}

const goCoupons = () => {
  uni.navigateTo({ url: '/pages/mine/coupons' })
}

const goEmergencyContacts = () => {
  uni.navigateTo({ url: '/pages/mine/emergency' })
}

const goHelp = () => {
  uni.navigateTo({ url: '/pages/mine/help' })
}

const goSettings = () => {
  uni.navigateTo({ url: '/pages/mine/settings' })
}

const goApplyPhotographer = () => {
  uni.navigateTo({ url: '/pages-photographer/apply/index' })
}

const goPhotographerDashboard = () => {
  uni.navigateTo({ url: '/pages-photographer/dashboard/index' })
}

onMounted(() => {
  // 获取用户统计数据
  if (isLoggedIn.value) {
    // TODO: 调用API获取统计数据
  }
})
</script>

<style lang="scss" scoped>
@import '@/styles/variables.scss';

.page {
  min-height: 100vh;
  background: $bg-secondary;
}

// 顶部区域
.header-section {
  position: relative;
  padding-bottom: $spacing-lg;
  
  .header-bg {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 280rpx;
    background: linear-gradient(135deg, $primary-color, $primary-light);
  }
}

.user-card {
  position: relative;
  margin: 120rpx $spacing-lg 0;
  padding: $spacing-lg;
  background: $bg-primary;
  border-radius: $radius-lg;
  box-shadow: $shadow-md;
  
  .user-info {
    display: flex;
    align-items: center;
    
    .avatar {
      width: 120rpx;
      height: 120rpx;
      border-radius: 50%;
      border: 4rpx solid $bg-primary;
      box-shadow: $shadow-sm;
    }
    
    .user-text {
      flex: 1;
      margin-left: $spacing-md;
      
      .name-row {
        display: flex;
        align-items: center;
        
        .verified {
          width: 32rpx;
          height: 32rpx;
          margin-left: 8rpx;
        }
      }
      
      .nickname {
        font-size: $font-xl;
        font-weight: $font-bold;
        color: $text-primary;
      }
      
      .desc {
        display: block;
        margin-top: 8rpx;
        font-size: $font-sm;
        color: $text-secondary;
      }
    }
    
    .arrow {
      width: 32rpx;
      height: 32rpx;
      opacity: 0.3;
    }
  }
  
  .user-stats {
    display: flex;
    margin-top: $spacing-lg;
    padding-top: $spacing-lg;
    border-top: 1rpx solid $border-light;
    
    .stat-item {
      flex: 1;
      text-align: center;
      
      .stat-value {
        display: block;
        font-size: $font-xl;
        font-weight: $font-bold;
        color: $text-primary;
      }
      
      .stat-label {
        display: block;
        margin-top: 4rpx;
        font-size: $font-xs;
        color: $text-tertiary;
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
}

// 订单区域
.order-section {
  padding: $spacing-lg;
  
  .section-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: $spacing-md;
    
    .section-title {
      font-size: $font-lg;
      font-weight: $font-semibold;
      color: $text-primary;
    }
    
    .section-more {
      display: flex;
      align-items: center;
      font-size: $font-sm;
      color: $text-secondary;
      
      image {
        width: 28rpx;
        height: 28rpx;
        margin-left: 4rpx;
        opacity: 0.5;
      }
    }
  }
  
  .order-tabs {
    display: flex;
    
    .order-tab {
      flex: 1;
      display: flex;
      flex-direction: column;
      align-items: center;
      
      .tab-icon-wrap {
        position: relative;
        width: 80rpx;
        height: 80rpx;
        display: flex;
        align-items: center;
        justify-content: center;
        background: $gray-100;
        border-radius: $radius-md;
        
        image {
          width: 44rpx;
          height: 44rpx;
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

// 摄影师入口
.photographer-entry {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: $spacing-lg;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  
  .entry-content {
    display: flex;
    align-items: center;
    
    .entry-icon {
      width: 80rpx;
      height: 80rpx;
    }
    
    .entry-text {
      margin-left: $spacing-md;
      
      .entry-title {
        display: block;
        font-size: $font-lg;
        font-weight: $font-semibold;
        color: $text-white;
      }
      
      .entry-desc {
        display: block;
        margin-top: 4rpx;
        font-size: $font-sm;
        color: rgba(255, 255, 255, 0.8);
      }
    }
  }
  
  .entry-arrow {
    width: 32rpx;
    height: 32rpx;
    opacity: 0.8;
  }
}

// 功能菜单
.menu-section {
  .menu-group {
    & + .menu-group {
      margin-top: $spacing-lg;
      border-top: 16rpx solid $bg-secondary;
    }
  }
  
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
      background: rgba($danger-color, 0.1);
      border-radius: $radius-sm;
      font-size: $font-xs;
      color: $danger-color;
    }
    
    .menu-arrow {
      width: 28rpx;
      height: 28rpx;
      opacity: 0.3;
    }
  }
}
</style>
