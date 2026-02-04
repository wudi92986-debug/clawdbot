<template>
  <view class="page">
    <!-- 搜索栏 -->
    <view class="search-bar">
      <view class="search-input" @click="goSearch">
        <image class="search-icon" src="/static/icons/search.png" mode="aspectFit" />
        <text class="search-placeholder">搜索摄影师、风格...</text>
      </view>
      <view class="location" @click="selectLocation">
        <image class="location-icon" src="/static/icons/location.png" mode="aspectFit" />
        <text class="location-text">湛江</text>
      </view>
    </view>
    
    <!-- 轮播图 -->
    <swiper class="banner" 
            :indicator-dots="true" 
            :autoplay="true" 
            :interval="4000" 
            :circular="true"
            indicator-color="rgba(255,255,255,0.5)"
            indicator-active-color="#ffffff">
      <swiper-item v-for="(banner, index) in banners" :key="index" @click="onBannerClick(banner)">
        <image class="banner-image" :src="banner.imageUrl" mode="aspectFill" />
      </swiper-item>
    </swiper>
    
    <!-- 服务分类 -->
    <view class="section">
      <view class="section-header">
        <text class="section-title">服务类型</text>
      </view>
      <view class="service-grid">
        <view class="service-item" 
              v-for="service in serviceTypes" 
              :key="service.id"
              @click="goPhotographerList(service.id)">
          <view class="service-icon-wrap">
            <image class="service-icon" :src="service.icon || '/static/icons/camera.png'" mode="aspectFit" />
          </view>
          <text class="service-name">{{ service.typeName }}</text>
        </view>
      </view>
    </view>
    
    <!-- 风格标签 -->
    <view class="section">
      <view class="section-header">
        <text class="section-title">热门风格</text>
        <text class="section-more" @click="goStyleList">全部</text>
      </view>
      <scroll-view class="style-scroll" scroll-x>
        <view class="style-list">
          <view class="style-tag" 
                v-for="style in styleTypes" 
                :key="style.id"
                :class="{ active: selectedStyleId === style.id }"
                @click="selectStyle(style)">
            {{ style.tagName }}
          </view>
        </view>
      </scroll-view>
    </view>
    
    <!-- 推荐摄影师 -->
    <view class="section">
      <view class="section-header">
        <text class="section-title">推荐摄影师</text>
        <text class="section-more" @click="goPhotographerList()">更多</text>
      </view>
      <view class="photographer-list">
        <view class="photographer-card" 
              v-for="photographer in photographers" 
              :key="photographer.id"
              @click="goPhotographerDetail(photographer.id)">
          <image class="photographer-cover" :src="photographer.coverImage || '/static/images/default-cover.jpg'" mode="aspectFill" />
          <view class="photographer-info">
            <view class="photographer-header">
              <image class="photographer-avatar" :src="photographer.avatar || '/static/images/default-avatar.png'" mode="aspectFill" />
              <view class="photographer-meta">
                <text class="photographer-name">{{ photographer.displayName }}</text>
                <view class="photographer-rating">
                  <image class="star-icon" src="/static/icons/star.png" mode="aspectFit" />
                  <text class="rating-score">{{ photographer.avgRating }}</text>
                  <text class="order-count">{{ photographer.completedOrders }}单</text>
                </view>
              </view>
            </view>
            <view class="photographer-tags">
              <text class="tag" v-for="(tag, i) in (photographer.styles || []).slice(0, 3)" :key="i">
                {{ tag.name }}
              </text>
            </view>
            <view class="photographer-footer">
              <view class="price">
                <text class="price-symbol">¥</text>
                <text class="price-value">{{ photographer.minPrice }}</text>
                <text class="price-unit">起</text>
              </view>
              <view class="btn-book">预约</view>
            </view>
          </view>
        </view>
      </view>
    </view>
    
    <!-- 快捷入口 -->
    <view class="quick-entry" @click="goPublishDemand">
      <view class="entry-content">
        <image class="entry-icon" src="/static/icons/publish.png" mode="aspectFit" />
        <view class="entry-text">
          <text class="entry-title">发布约拍需求</text>
          <text class="entry-desc">让摄影师主动找到你</text>
        </view>
      </view>
      <image class="entry-arrow" src="/static/icons/arrow-right.png" mode="aspectFit" />
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getPhotographers } from '@/api/photographer'
import { getBanners, getStyleTags, getServiceTypes } from '@/api/base'

// 数据
const banners = ref([
  { imageUrl: '/static/images/banner1.jpg', linkType: 1, linkUrl: '' },
  { imageUrl: '/static/images/banner2.jpg', linkType: 1, linkUrl: '' }
])
const serviceTypes = ref([])
const styleTypes = ref([])
const photographers = ref([])
const selectedStyleId = ref(null)

// 获取数据
const loadData = async () => {
  try {
    // 获取轮播图
    // const bannerRes = await getBanners({ position: 'home' })
    // banners.value = bannerRes || []
    
    // 获取服务类型
    const serviceRes = await getServiceTypes()
    serviceTypes.value = serviceRes || []
    
    // 获取风格标签
    const styleRes = await getStyleTags({ type: 1 })
    styleTypes.value = styleRes || []
    
    // 获取推荐摄影师
    const photographerRes = await getPhotographers({ page: 1, pageSize: 6 })
    photographers.value = photographerRes?.list || []
  } catch (err) {
    console.error('加载数据失败', err)
  }
}

// 页面跳转
const goSearch = () => {
  uni.navigateTo({ url: '/pages/photographer/list?focus=1' })
}

const selectLocation = () => {
  uni.showToast({ title: '定位功能开发中', icon: 'none' })
}

const goPhotographerList = (serviceTypeId) => {
  let url = '/pages/photographer/list'
  if (serviceTypeId) {
    url += `?serviceTypeId=${serviceTypeId}`
  }
  uni.navigateTo({ url })
}

const goPhotographerDetail = (id) => {
  uni.navigateTo({ url: `/pages/photographer/detail?id=${id}` })
}

const goStyleList = () => {
  uni.navigateTo({ url: '/pages/photographer/list' })
}

const selectStyle = (style) => {
  selectedStyleId.value = style.id
  uni.navigateTo({ url: `/pages/photographer/list?styleId=${style.id}` })
}

const goPublishDemand = () => {
  uni.navigateTo({ url: '/pages/demand/publish' })
}

const onBannerClick = (banner) => {
  // 处理轮播图点击
}

// 下拉刷新
onPullDownRefresh(async () => {
  await loadData()
  uni.stopPullDownRefresh()
})

onMounted(() => {
  loadData()
})
</script>

<style lang="scss" scoped>
@import '@/styles/variables.scss';

.page {
  min-height: 100vh;
  background: $bg-secondary;
  padding-bottom: 120rpx;
}

// 搜索栏
.search-bar {
  display: flex;
  align-items: center;
  padding: $spacing-md $spacing-lg;
  background: $bg-primary;
  
  .search-input {
    flex: 1;
    display: flex;
    align-items: center;
    height: 72rpx;
    padding: 0 $spacing-md;
    background: $gray-100;
    border-radius: $radius-round;
    
    .search-icon {
      width: 36rpx;
      height: 36rpx;
      opacity: 0.5;
    }
    
    .search-placeholder {
      margin-left: $spacing-sm;
      font-size: $font-md;
      color: $text-tertiary;
    }
  }
  
  .location {
    display: flex;
    align-items: center;
    margin-left: $spacing-md;
    
    .location-icon {
      width: 32rpx;
      height: 32rpx;
    }
    
    .location-text {
      margin-left: 8rpx;
      font-size: $font-md;
      color: $text-primary;
    }
  }
}

// 轮播图
.banner {
  width: 100%;
  height: 320rpx;
  
  .banner-image {
    width: 100%;
    height: 100%;
  }
}

// 区块
.section {
  margin-top: $spacing-lg;
  padding: 0 $spacing-lg;
  
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
      font-size: $font-sm;
      color: $primary-color;
    }
  }
}

// 服务分类
.service-grid {
  display: flex;
  flex-wrap: wrap;
  background: $bg-primary;
  border-radius: $radius-lg;
  padding: $spacing-md;
  
  .service-item {
    width: 25%;
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: $spacing-md 0;
    
    .service-icon-wrap {
      width: 88rpx;
      height: 88rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      background: $gray-100;
      border-radius: $radius-lg;
      margin-bottom: $spacing-sm;
    }
    
    .service-icon {
      width: 48rpx;
      height: 48rpx;
    }
    
    .service-name {
      font-size: $font-sm;
      color: $text-primary;
    }
  }
}

// 风格标签
.style-scroll {
  white-space: nowrap;
  
  .style-list {
    display: inline-flex;
    gap: $spacing-sm;
    
    .style-tag {
      display: inline-flex;
      padding: 16rpx 28rpx;
      background: $bg-primary;
      border-radius: $radius-round;
      font-size: $font-sm;
      color: $text-secondary;
      box-shadow: $shadow-sm;
      
      &.active {
        background: $primary-color;
        color: $text-white;
      }
    }
  }
}

// 摄影师卡片
.photographer-list {
  display: flex;
  flex-direction: column;
  gap: $spacing-lg;
}

.photographer-card {
  background: $bg-primary;
  border-radius: $radius-lg;
  overflow: hidden;
  box-shadow: $shadow-sm;
  
  .photographer-cover {
    width: 100%;
    height: 300rpx;
  }
  
  .photographer-info {
    padding: $spacing-lg;
    
    .photographer-header {
      display: flex;
      align-items: center;
      
      .photographer-avatar {
        width: 80rpx;
        height: 80rpx;
        border-radius: 50%;
        border: 4rpx solid $bg-primary;
        margin-top: -60rpx;
        box-shadow: $shadow-md;
      }
      
      .photographer-meta {
        flex: 1;
        margin-left: $spacing-md;
        
        .photographer-name {
          font-size: $font-lg;
          font-weight: $font-semibold;
          color: $text-primary;
        }
        
        .photographer-rating {
          display: flex;
          align-items: center;
          margin-top: 8rpx;
          
          .star-icon {
            width: 28rpx;
            height: 28rpx;
          }
          
          .rating-score {
            margin-left: 8rpx;
            font-size: $font-md;
            font-weight: $font-semibold;
            color: $warning-color;
          }
          
          .order-count {
            margin-left: $spacing-sm;
            font-size: $font-sm;
            color: $text-secondary;
          }
        }
      }
    }
    
    .photographer-tags {
      display: flex;
      flex-wrap: wrap;
      gap: $spacing-sm;
      margin-top: $spacing-md;
      
      .tag {
        padding: 8rpx 16rpx;
        background: rgba($primary-color, 0.08);
        border-radius: $radius-sm;
        font-size: $font-xs;
        color: $primary-color;
      }
    }
    
    .photographer-footer {
      display: flex;
      align-items: center;
      justify-content: space-between;
      margin-top: $spacing-lg;
      
      .price {
        display: flex;
        align-items: baseline;
        
        .price-symbol {
          font-size: $font-sm;
          color: $danger-color;
        }
        
        .price-value {
          font-size: $font-xxl;
          font-weight: $font-bold;
          color: $danger-color;
        }
        
        .price-unit {
          margin-left: 4rpx;
          font-size: $font-xs;
          color: $text-secondary;
        }
      }
      
      .btn-book {
        padding: 16rpx 40rpx;
        background: $primary-color;
        border-radius: $radius-round;
        font-size: $font-md;
        font-weight: $font-medium;
        color: $text-white;
      }
    }
  }
}

// 快捷入口
.quick-entry {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin: $spacing-lg;
  padding: $spacing-lg;
  background: linear-gradient(135deg, $primary-color, $primary-light);
  border-radius: $radius-lg;
  
  .entry-content {
    display: flex;
    align-items: center;
    
    .entry-icon {
      width: 72rpx;
      height: 72rpx;
    }
    
    .entry-text {
      margin-left: $spacing-md;
      
      .entry-title {
        font-size: $font-lg;
        font-weight: $font-semibold;
        color: $text-white;
      }
      
      .entry-desc {
        margin-top: 8rpx;
        font-size: $font-sm;
        color: rgba(255, 255, 255, 0.8);
      }
    }
  }
  
  .entry-arrow {
    width: 40rpx;
    height: 40rpx;
    opacity: 0.8;
  }
}
</style>
