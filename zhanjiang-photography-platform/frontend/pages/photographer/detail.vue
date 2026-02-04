<template>
  <view class="page">
    <!-- 自定义导航栏 -->
    <view class="nav-bar" :class="{ scrolled: isScrolled }">
      <view class="nav-content">
        <view class="nav-back" @click="goBack">
          <image src="/static/icons/back-white.png" mode="aspectFit" />
        </view>
        <text class="nav-title" v-if="isScrolled">{{ photographer.displayName }}</text>
        <view class="nav-actions">
          <view class="nav-btn" @click="onShare">
            <image src="/static/icons/share-white.png" mode="aspectFit" />
          </view>
          <view class="nav-btn" @click="toggleFavorite">
            <image :src="isFavorited ? '/static/icons/heart-fill.png' : '/static/icons/heart-white.png'" mode="aspectFit" />
          </view>
        </view>
      </view>
    </view>
    
    <!-- 封面图 -->
    <view class="cover-section">
      <image class="cover-image" :src="photographer.coverImage || '/static/images/default-cover.jpg'" mode="aspectFill" />
      <view class="cover-gradient"></view>
    </view>
    
    <!-- 摄影师信息卡片 -->
    <view class="profile-card">
      <view class="profile-header">
        <image class="avatar" :src="photographer.avatar || '/static/images/default-avatar.png'" mode="aspectFill" />
        <view class="profile-main">
          <view class="name-row">
            <text class="name">{{ photographer.displayName }}</text>
            <image class="verified" v-if="photographer.isVerified" src="/static/icons/verified.png" mode="aspectFit" />
            <image class="student" v-if="photographer.isStudentVerified" src="/static/icons/student.png" mode="aspectFit" />
          </view>
          <view class="bio">{{ photographer.bio || '暂无简介' }}</view>
        </view>
        <view class="follow-btn" :class="{ followed: isFollowed }" @click="toggleFollow">
          {{ isFollowed ? '已关注' : '+ 关注' }}
        </view>
      </view>
      
      <!-- 统计数据 -->
      <view class="stats-row">
        <view class="stat-item">
          <text class="stat-value">{{ photographer.avgRating }}</text>
          <text class="stat-label">评分</text>
        </view>
        <view class="stat-divider"></view>
        <view class="stat-item">
          <text class="stat-value">{{ photographer.completedOrders }}</text>
          <text class="stat-label">订单</text>
        </view>
        <view class="stat-divider"></view>
        <view class="stat-item">
          <text class="stat-value">{{ photographer.totalFans }}</text>
          <text class="stat-label">粉丝</text>
        </view>
        <view class="stat-divider"></view>
        <view class="stat-item">
          <text class="stat-value">{{ photographer.experienceYears || 0 }}年</text>
          <text class="stat-label">经验</text>
        </view>
      </view>
      
      <!-- 标签 -->
      <view class="tags-row" v-if="photographer.styles?.length">
        <text class="tag" v-for="style in photographer.styles" :key="style.id">
          {{ style.name }}
        </text>
      </view>
    </view>
    
    <!-- Tab栏 -->
    <view class="tab-bar">
      <view class="tab-item" 
            v-for="tab in tabs" 
            :key="tab.key"
            :class="{ active: currentTab === tab.key }"
            @click="switchTab(tab.key)">
        <text>{{ tab.label }}</text>
        <view class="tab-indicator" v-if="currentTab === tab.key"></view>
      </view>
    </view>
    
    <!-- Tab内容 -->
    <view class="tab-content">
      <!-- 作品集 -->
      <view class="works-tab" v-show="currentTab === 'works'">
        <view class="works-grid">
          <view class="works-item" 
                v-for="work in works" 
                :key="work.id"
                @click="goWorksDetail(work.id)">
            <image class="works-cover" :src="work.coverImage" mode="aspectFill" />
            <view class="works-info">
              <text class="works-title">{{ work.title }}</text>
              <view class="works-stats">
                <image src="/static/icons/eye.png" mode="aspectFit" />
                <text>{{ work.viewCount }}</text>
                <image src="/static/icons/heart-small.png" mode="aspectFit" />
                <text>{{ work.likeCount }}</text>
              </view>
            </view>
          </view>
        </view>
        <view class="load-more" v-if="worksHasMore" @click="loadMoreWorks">加载更多</view>
      </view>
      
      <!-- 服务套餐 -->
      <view class="packages-tab" v-show="currentTab === 'packages'">
        <view class="package-card" 
              v-for="pkg in packages" 
              :key="pkg.id"
              @click="selectPackage(pkg)">
          <view class="package-header">
            <text class="package-name">{{ pkg.packageName }}</text>
            <view class="package-price">
              <text class="price-symbol">¥</text>
              <text class="price-value">{{ pkg.price }}</text>
            </view>
          </view>
          <view class="package-desc">{{ pkg.description }}</view>
          <view class="package-details">
            <view class="detail-item">
              <image src="/static/icons/time.png" mode="aspectFit" />
              <text>{{ pkg.duration }}分钟</text>
            </view>
            <view class="detail-item">
              <image src="/static/icons/photo.png" mode="aspectFit" />
              <text>{{ pkg.originalCount }}张原片</text>
            </view>
            <view class="detail-item">
              <image src="/static/icons/edit.png" mode="aspectFit" />
              <text>{{ pkg.refinedCount }}张精修</text>
            </view>
            <view class="detail-item">
              <image src="/static/icons/delivery.png" mode="aspectFit" />
              <text>{{ pkg.deliveryDays }}天交付</text>
            </view>
          </view>
          <view class="package-action">
            <text class="original-price" v-if="pkg.originalPrice">¥{{ pkg.originalPrice }}</text>
            <view class="book-btn">立即预约</view>
          </view>
        </view>
        <view class="empty" v-if="!packages.length">
          <text>暂无服务套餐</text>
        </view>
      </view>
      
      <!-- 评价 -->
      <view class="reviews-tab" v-show="currentTab === 'reviews'">
        <view class="reviews-summary" v-if="reviewsSummary">
          <view class="summary-score">
            <text class="score-value">{{ reviewsSummary.avgRating }}</text>
            <text class="score-label">综合评分</text>
          </view>
          <view class="summary-dimensions">
            <view class="dimension-item">
              <text class="dim-label">拍摄技术</text>
              <view class="dim-bar">
                <view class="dim-fill" :style="{ width: (reviewsSummary.dimensions?.skill / 5 * 100) + '%' }"></view>
              </view>
              <text class="dim-score">{{ reviewsSummary.dimensions?.skill }}</text>
            </view>
            <view class="dimension-item">
              <text class="dim-label">服务态度</text>
              <view class="dim-bar">
                <view class="dim-fill" :style="{ width: (reviewsSummary.dimensions?.attitude / 5 * 100) + '%' }"></view>
              </view>
              <text class="dim-score">{{ reviewsSummary.dimensions?.attitude }}</text>
            </view>
            <view class="dimension-item">
              <text class="dim-label">准时程度</text>
              <view class="dim-bar">
                <view class="dim-fill" :style="{ width: (reviewsSummary.dimensions?.punctuality / 5 * 100) + '%' }"></view>
              </view>
              <text class="dim-score">{{ reviewsSummary.dimensions?.punctuality }}</text>
            </view>
          </view>
        </view>
        
        <view class="reviews-list">
          <view class="review-item" v-for="review in reviews" :key="review.id">
            <view class="review-header">
              <image class="reviewer-avatar" :src="review.user.avatar" mode="aspectFill" />
              <view class="reviewer-info">
                <text class="reviewer-name">{{ review.user.nickname }}</text>
                <view class="review-rating">
                  <image v-for="i in 5" :key="i" 
                         :src="i <= review.overallRating ? '/static/icons/star.png' : '/static/icons/star-gray.png'" 
                         mode="aspectFit" />
                </view>
              </view>
              <text class="review-date">{{ review.createTime }}</text>
            </view>
            <text class="review-content">{{ review.content }}</text>
            <view class="review-images" v-if="review.images?.length">
              <image v-for="(img, i) in review.images" :key="i" :src="img" mode="aspectFill" @click="previewImage(review.images, i)" />
            </view>
            <view class="review-reply" v-if="review.reply">
              <text class="reply-label">摄影师回复：</text>
              <text class="reply-content">{{ review.reply.content }}</text>
            </view>
          </view>
        </view>
        
        <view class="empty" v-if="!reviews.length">
          <text>暂无评价</text>
        </view>
      </view>
    </view>
    
    <!-- 底部操作栏 -->
    <view class="bottom-bar safe-area-bottom">
      <view class="bar-left">
        <view class="bar-item" @click="goChat">
          <image src="/static/icons/chat.png" mode="aspectFit" />
          <text>咨询</text>
        </view>
        <view class="bar-item" @click="toggleFavorite">
          <image :src="isFavorited ? '/static/icons/heart-fill.png' : '/static/icons/heart.png'" mode="aspectFit" />
          <text>收藏</text>
        </view>
      </view>
      <view class="book-button" @click="goCreateOrder">
        <text class="price-label">¥{{ photographer.minPrice }}起</text>
        <text class="book-text">立即预约</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getPhotographerDetail, getPhotographerWorks, getPhotographerPackages, getPhotographerReviews } from '@/api/photographer'

const photographerId = ref(null)
const photographer = ref({})
const works = ref([])
const packages = ref([])
const reviews = ref([])
const reviewsSummary = ref(null)

const currentTab = ref('works')
const tabs = [
  { key: 'works', label: '作品' },
  { key: 'packages', label: '套餐' },
  { key: 'reviews', label: '评价' }
]

const isScrolled = ref(false)
const isFollowed = ref(false)
const isFavorited = ref(false)
const worksPage = ref(1)
const worksHasMore = ref(true)

// 获取摄影师详情
const fetchDetail = async () => {
  try {
    const res = await getPhotographerDetail(photographerId.value)
    photographer.value = res
    isFollowed.value = res.isFollowed
    isFavorited.value = res.isFavorited
  } catch (err) {
    console.error('获取摄影师详情失败', err)
  }
}

// 获取作品列表
const fetchWorks = async () => {
  try {
    const res = await getPhotographerWorks(photographerId.value, { 
      page: worksPage.value, 
      pageSize: 10 
    })
    works.value = [...works.value, ...(res?.list || [])]
    worksHasMore.value = (res?.list?.length || 0) === 10
  } catch (err) {
    console.error('获取作品列表失败', err)
  }
}

// 获取服务套餐
const fetchPackages = async () => {
  try {
    const res = await getPhotographerPackages(photographerId.value)
    packages.value = res || []
  } catch (err) {
    console.error('获取服务套餐失败', err)
  }
}

// 获取评价
const fetchReviews = async () => {
  try {
    const res = await getPhotographerReviews(photographerId.value, { page: 1, pageSize: 10 })
    reviews.value = res?.list || []
    reviewsSummary.value = res?.summary
  } catch (err) {
    console.error('获取评价失败', err)
  }
}

// 切换Tab
const switchTab = (key) => {
  currentTab.value = key
  if (key === 'packages' && !packages.value.length) {
    fetchPackages()
  }
  if (key === 'reviews' && !reviews.value.length) {
    fetchReviews()
  }
}

// 加载更多作品
const loadMoreWorks = () => {
  worksPage.value++
  fetchWorks()
}

// 关注/取消关注
const toggleFollow = () => {
  isFollowed.value = !isFollowed.value
  uni.showToast({
    title: isFollowed.value ? '关注成功' : '已取消关注',
    icon: 'none'
  })
}

// 收藏/取消收藏
const toggleFavorite = () => {
  isFavorited.value = !isFavorited.value
  uni.showToast({
    title: isFavorited.value ? '收藏成功' : '已取消收藏',
    icon: 'none'
  })
}

// 页面跳转
const goBack = () => {
  uni.navigateBack()
}

const goChat = () => {
  uni.navigateTo({ url: `/pages/message/chat?targetId=${photographer.value.userId}` })
}

const goCreateOrder = () => {
  uni.navigateTo({ url: `/pages/order/create?photographerId=${photographerId.value}` })
}

const goWorksDetail = (id) => {
  uni.navigateTo({ url: `/pages/photographer/works?id=${id}` })
}

const selectPackage = (pkg) => {
  uni.navigateTo({ url: `/pages/order/create?photographerId=${photographerId.value}&packageId=${pkg.id}` })
}

const onShare = () => {
  // 分享
}

const previewImage = (images, index) => {
  uni.previewImage({
    urls: images,
    current: index
  })
}

// 监听滚动
onPageScroll((e) => {
  isScrolled.value = e.scrollTop > 200
})

onLoad((options) => {
  photographerId.value = options.id
})

onMounted(() => {
  fetchDetail()
  fetchWorks()
})
</script>

<style lang="scss" scoped>
@import '@/styles/variables.scss';

.page {
  min-height: 100vh;
  background: $bg-secondary;
  padding-bottom: 140rpx;
}

// 导航栏
.nav-bar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: $z-fixed;
  padding-top: var(--status-bar-height);
  transition: background $transition-normal;
  
  &.scrolled {
    background: $bg-primary;
    
    .nav-title {
      opacity: 1;
    }
  }
  
  .nav-content {
    display: flex;
    align-items: center;
    justify-content: space-between;
    height: 88rpx;
    padding: 0 $spacing-lg;
  }
  
  .nav-back, .nav-btn {
    width: 64rpx;
    height: 64rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    background: rgba(0, 0, 0, 0.3);
    border-radius: 50%;
    
    image {
      width: 36rpx;
      height: 36rpx;
    }
  }
  
  .nav-title {
    font-size: $font-lg;
    font-weight: $font-semibold;
    color: $text-primary;
    opacity: 0;
    transition: opacity $transition-normal;
  }
  
  .nav-actions {
    display: flex;
    gap: $spacing-sm;
  }
}

// 封面
.cover-section {
  position: relative;
  height: 500rpx;
  
  .cover-image {
    width: 100%;
    height: 100%;
  }
  
  .cover-gradient {
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    height: 200rpx;
    background: linear-gradient(transparent, rgba(0, 0, 0, 0.5));
  }
}

// 个人信息卡片
.profile-card {
  margin: -80rpx $spacing-lg 0;
  padding: $spacing-lg;
  background: $bg-primary;
  border-radius: $radius-lg;
  box-shadow: $shadow-md;
  position: relative;
  z-index: 1;
  
  .profile-header {
    display: flex;
    align-items: flex-start;
    
    .avatar {
      width: 120rpx;
      height: 120rpx;
      border-radius: 50%;
      border: 4rpx solid $bg-primary;
      box-shadow: $shadow-sm;
    }
    
    .profile-main {
      flex: 1;
      margin-left: $spacing-md;
      
      .name-row {
        display: flex;
        align-items: center;
        
        .name {
          font-size: $font-xl;
          font-weight: $font-bold;
          color: $text-primary;
        }
        
        .verified, .student {
          width: 36rpx;
          height: 36rpx;
          margin-left: 8rpx;
        }
      }
      
      .bio {
        margin-top: 8rpx;
        font-size: $font-sm;
        color: $text-secondary;
        display: -webkit-box;
        -webkit-box-orient: vertical;
        -webkit-line-clamp: 2;
        overflow: hidden;
      }
    }
    
    .follow-btn {
      padding: 12rpx 28rpx;
      background: $primary-color;
      border-radius: $radius-round;
      font-size: $font-sm;
      color: $text-white;
      
      &.followed {
        background: $gray-200;
        color: $text-secondary;
      }
    }
  }
  
  .stats-row {
    display: flex;
    align-items: center;
    justify-content: space-around;
    margin-top: $spacing-lg;
    padding-top: $spacing-lg;
    border-top: 1rpx solid $border-light;
    
    .stat-item {
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
    
    .stat-divider {
      width: 1rpx;
      height: 60rpx;
      background: $border-light;
    }
  }
  
  .tags-row {
    display: flex;
    flex-wrap: wrap;
    gap: $spacing-sm;
    margin-top: $spacing-lg;
    
    .tag {
      padding: 8rpx 20rpx;
      background: rgba($primary-color, 0.08);
      border-radius: $radius-round;
      font-size: $font-xs;
      color: $primary-color;
    }
  }
}

// Tab栏
.tab-bar {
  display: flex;
  background: $bg-primary;
  margin-top: $spacing-lg;
  padding: 0 $spacing-lg;
  
  .tab-item {
    flex: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: $spacing-md 0;
    font-size: $font-md;
    color: $text-secondary;
    position: relative;
    
    &.active {
      color: $primary-color;
      font-weight: $font-semibold;
    }
    
    .tab-indicator {
      position: absolute;
      bottom: 0;
      width: 48rpx;
      height: 6rpx;
      background: $primary-color;
      border-radius: 3rpx;
    }
  }
}

// Tab内容
.tab-content {
  padding: $spacing-lg;
}

// 作品网格
.works-grid {
  display: flex;
  flex-wrap: wrap;
  gap: $spacing-sm;
  
  .works-item {
    width: calc(50% - 8rpx);
    border-radius: $radius-md;
    overflow: hidden;
    background: $bg-primary;
    
    .works-cover {
      width: 100%;
      height: 240rpx;
    }
    
    .works-info {
      padding: $spacing-sm;
      
      .works-title {
        font-size: $font-sm;
        color: $text-primary;
        display: -webkit-box;
        -webkit-box-orient: vertical;
        -webkit-line-clamp: 1;
        overflow: hidden;
      }
      
      .works-stats {
        display: flex;
        align-items: center;
        margin-top: 8rpx;
        font-size: $font-xs;
        color: $text-tertiary;
        
        image {
          width: 24rpx;
          height: 24rpx;
          margin-right: 4rpx;
          
          &:not(:first-child) {
            margin-left: $spacing-sm;
          }
        }
      }
    }
  }
}

.load-more {
  text-align: center;
  padding: $spacing-md;
  font-size: $font-sm;
  color: $primary-color;
}

// 套餐卡片
.package-card {
  background: $bg-primary;
  border-radius: $radius-lg;
  padding: $spacing-lg;
  margin-bottom: $spacing-md;
  
  .package-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    
    .package-name {
      font-size: $font-lg;
      font-weight: $font-semibold;
      color: $text-primary;
    }
    
    .package-price {
      .price-symbol {
        font-size: $font-sm;
        color: $danger-color;
      }
      
      .price-value {
        font-size: $font-xxl;
        font-weight: $font-bold;
        color: $danger-color;
      }
    }
  }
  
  .package-desc {
    margin-top: $spacing-sm;
    font-size: $font-sm;
    color: $text-secondary;
  }
  
  .package-details {
    display: flex;
    flex-wrap: wrap;
    gap: $spacing-md;
    margin-top: $spacing-md;
    padding-top: $spacing-md;
    border-top: 1rpx solid $border-light;
    
    .detail-item {
      display: flex;
      align-items: center;
      font-size: $font-sm;
      color: $text-secondary;
      
      image {
        width: 32rpx;
        height: 32rpx;
        margin-right: 8rpx;
        opacity: 0.6;
      }
    }
  }
  
  .package-action {
    display: flex;
    align-items: center;
    justify-content: flex-end;
    margin-top: $spacing-md;
    
    .original-price {
      margin-right: $spacing-md;
      font-size: $font-sm;
      color: $text-tertiary;
      text-decoration: line-through;
    }
    
    .book-btn {
      padding: 16rpx 32rpx;
      background: $primary-color;
      border-radius: $radius-round;
      font-size: $font-sm;
      color: $text-white;
    }
  }
}

// 评价
.reviews-summary {
  display: flex;
  background: $bg-primary;
  border-radius: $radius-lg;
  padding: $spacing-lg;
  margin-bottom: $spacing-lg;
  
  .summary-score {
    width: 160rpx;
    text-align: center;
    border-right: 1rpx solid $border-light;
    
    .score-value {
      display: block;
      font-size: 72rpx;
      font-weight: $font-bold;
      color: $warning-color;
    }
    
    .score-label {
      font-size: $font-sm;
      color: $text-tertiary;
    }
  }
  
  .summary-dimensions {
    flex: 1;
    padding-left: $spacing-lg;
    
    .dimension-item {
      display: flex;
      align-items: center;
      margin-bottom: 12rpx;
      
      &:last-child {
        margin-bottom: 0;
      }
      
      .dim-label {
        width: 120rpx;
        font-size: $font-sm;
        color: $text-secondary;
      }
      
      .dim-bar {
        flex: 1;
        height: 12rpx;
        background: $gray-200;
        border-radius: 6rpx;
        overflow: hidden;
        
        .dim-fill {
          height: 100%;
          background: $warning-color;
          border-radius: 6rpx;
        }
      }
      
      .dim-score {
        width: 60rpx;
        text-align: right;
        font-size: $font-sm;
        color: $text-primary;
      }
    }
  }
}

.reviews-list {
  .review-item {
    background: $bg-primary;
    border-radius: $radius-lg;
    padding: $spacing-lg;
    margin-bottom: $spacing-md;
    
    .review-header {
      display: flex;
      align-items: center;
      
      .reviewer-avatar {
        width: 72rpx;
        height: 72rpx;
        border-radius: 50%;
      }
      
      .reviewer-info {
        flex: 1;
        margin-left: $spacing-md;
        
        .reviewer-name {
          font-size: $font-md;
          color: $text-primary;
        }
        
        .review-rating {
          display: flex;
          margin-top: 8rpx;
          
          image {
            width: 24rpx;
            height: 24rpx;
          }
        }
      }
      
      .review-date {
        font-size: $font-xs;
        color: $text-tertiary;
      }
    }
    
    .review-content {
      margin-top: $spacing-md;
      font-size: $font-md;
      color: $text-primary;
      line-height: 1.6;
    }
    
    .review-images {
      display: flex;
      flex-wrap: wrap;
      gap: $spacing-sm;
      margin-top: $spacing-md;
      
      image {
        width: 160rpx;
        height: 160rpx;
        border-radius: $radius-sm;
      }
    }
    
    .review-reply {
      margin-top: $spacing-md;
      padding: $spacing-md;
      background: $gray-100;
      border-radius: $radius-sm;
      
      .reply-label {
        font-size: $font-sm;
        color: $primary-color;
      }
      
      .reply-content {
        margin-top: 8rpx;
        font-size: $font-sm;
        color: $text-secondary;
      }
    }
  }
}

.empty {
  text-align: center;
  padding: 100rpx 0;
  font-size: $font-md;
  color: $text-tertiary;
}

// 底部操作栏
.bottom-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  align-items: center;
  padding: $spacing-md $spacing-lg;
  background: $bg-primary;
  box-shadow: 0 -4rpx 20rpx rgba(0, 0, 0, 0.05);
  
  .bar-left {
    display: flex;
    
    .bar-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      padding: 0 $spacing-lg;
      
      image {
        width: 48rpx;
        height: 48rpx;
      }
      
      text {
        margin-top: 4rpx;
        font-size: $font-xs;
        color: $text-secondary;
      }
    }
  }
  
  .book-button {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: center;
    height: 88rpx;
    margin-left: $spacing-lg;
    background: $primary-color;
    border-radius: $radius-round;
    
    .price-label {
      font-size: $font-sm;
      color: rgba(255, 255, 255, 0.8);
    }
    
    .book-text {
      margin-left: $spacing-sm;
      font-size: $font-lg;
      font-weight: $font-semibold;
      color: $text-white;
    }
  }
}
</style>
