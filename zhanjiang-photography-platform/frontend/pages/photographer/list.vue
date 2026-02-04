<template>
  <view class="page">
    <!-- 搜索栏 -->
    <view class="search-header">
      <view class="search-box">
        <image class="search-icon" src="/static/icons/search.png" mode="aspectFit" />
        <input class="search-input" 
               v-model="keyword" 
               placeholder="搜索摄影师、风格..."
               placeholder-class="search-placeholder"
               :focus="focusSearch"
               @confirm="onSearch" />
        <image v-if="keyword" 
               class="clear-icon" 
               src="/static/icons/close.png" 
               mode="aspectFit"
               @click="clearKeyword" />
      </view>
      <text class="cancel-btn" @click="goBack">取消</text>
    </view>
    
    <!-- 筛选栏 -->
    <view class="filter-bar">
      <scroll-view class="filter-scroll" scroll-x>
        <view class="filter-list">
          <view class="filter-item" 
                :class="{ active: currentFilter === 'sort' }"
                @click="toggleFilter('sort')">
            <text>{{ sortText }}</text>
            <image class="filter-arrow" src="/static/icons/arrow-down.png" mode="aspectFit" />
          </view>
          <view class="filter-item"
                :class="{ active: currentFilter === 'style' }"
                @click="toggleFilter('style')">
            <text>风格</text>
            <image class="filter-arrow" src="/static/icons/arrow-down.png" mode="aspectFit" />
          </view>
          <view class="filter-item"
                :class="{ active: currentFilter === 'price' }"
                @click="toggleFilter('price')">
            <text>价格</text>
            <image class="filter-arrow" src="/static/icons/arrow-down.png" mode="aspectFit" />
          </view>
          <view class="filter-item"
                :class="{ active: filterParams.minRating }"
                @click="toggleRating">
            <text>好评优先</text>
          </view>
        </view>
      </scroll-view>
    </view>
    
    <!-- 筛选面板 -->
    <view class="filter-panel" v-if="showFilterPanel" @click="closeFilterPanel">
      <view class="filter-content" @click.stop>
        <!-- 排序选项 -->
        <view class="filter-section" v-if="currentFilter === 'sort'">
          <view class="filter-option" 
                v-for="option in sortOptions" 
                :key="option.value"
                :class="{ active: filterParams.sortBy === option.value }"
                @click="selectSort(option)">
            <text>{{ option.label }}</text>
            <image v-if="filterParams.sortBy === option.value" 
                   class="check-icon" 
                   src="/static/icons/check.png" 
                   mode="aspectFit" />
          </view>
        </view>
        
        <!-- 风格选项 -->
        <view class="filter-section" v-if="currentFilter === 'style'">
          <view class="style-tags">
            <view class="style-tag"
                  v-for="style in styleTypes"
                  :key="style.id"
                  :class="{ active: selectedStyles.includes(style.id) }"
                  @click="toggleStyle(style.id)">
              {{ style.tagName }}
            </view>
          </view>
          <view class="filter-actions">
            <view class="btn-reset" @click="resetStyles">重置</view>
            <view class="btn-confirm" @click="confirmStyles">确定</view>
          </view>
        </view>
        
        <!-- 价格选项 -->
        <view class="filter-section" v-if="currentFilter === 'price'">
          <view class="price-range">
            <input class="price-input" 
                   type="number" 
                   v-model="tempMinPrice" 
                   placeholder="最低价" />
            <text class="price-line">-</text>
            <input class="price-input" 
                   type="number" 
                   v-model="tempMaxPrice" 
                   placeholder="最高价" />
          </view>
          <view class="price-presets">
            <view class="preset-item" @click="setPrice(0, 200)">200以下</view>
            <view class="preset-item" @click="setPrice(200, 500)">200-500</view>
            <view class="preset-item" @click="setPrice(500, 1000)">500-1000</view>
            <view class="preset-item" @click="setPrice(1000, null)">1000以上</view>
          </view>
          <view class="filter-actions">
            <view class="btn-reset" @click="resetPrice">重置</view>
            <view class="btn-confirm" @click="confirmPrice">确定</view>
          </view>
        </view>
      </view>
    </view>
    
    <!-- 摄影师列表 -->
    <scroll-view class="photographer-scroll" 
                 scroll-y 
                 @scrolltolower="loadMore"
                 :refresher-enabled="true"
                 :refresher-triggered="refreshing"
                 @refresherrefresh="onRefresh">
      <view class="photographer-list">
        <view class="photographer-item" 
              v-for="photographer in photographers" 
              :key="photographer.id"
              @click="goDetail(photographer.id)">
          <!-- 作品预览 -->
          <view class="works-preview">
            <image class="preview-image" 
                   v-for="(work, index) in (photographer.sampleWorks || []).slice(0, 3)" 
                   :key="index"
                   :src="work"
                   mode="aspectFill" />
            <view class="preview-placeholder" v-if="!photographer.sampleWorks?.length">
              <image src="/static/images/default-cover.jpg" mode="aspectFill" />
            </view>
          </view>
          
          <!-- 摄影师信息 -->
          <view class="photographer-info">
            <view class="info-header">
              <image class="avatar" :src="photographer.avatar || '/static/images/default-avatar.png'" mode="aspectFill" />
              <view class="info-main">
                <view class="name-row">
                  <text class="name">{{ photographer.displayName }}</text>
                  <view class="verified-badge" v-if="photographer.isVerified">
                    <image src="/static/icons/verified.png" mode="aspectFit" />
                  </view>
                </view>
                <view class="stats-row">
                  <view class="rating">
                    <image class="star" src="/static/icons/star.png" mode="aspectFit" />
                    <text>{{ photographer.avgRating }}</text>
                  </view>
                  <text class="divider">|</text>
                  <text class="orders">{{ photographer.completedOrders }}单</text>
                  <text class="divider">|</text>
                  <text class="fans">{{ photographer.totalFans }}粉丝</text>
                </view>
              </view>
            </view>
            
            <view class="tags-row" v-if="photographer.styles?.length">
              <text class="tag" v-for="style in photographer.styles.slice(0, 4)" :key="style.id">
                {{ style.name }}
              </text>
            </view>
            
            <view class="price-row">
              <view class="price">
                <text class="symbol">¥</text>
                <text class="value">{{ photographer.minPrice }}</text>
                <text class="unit">起</text>
              </view>
              <view class="action-btn">查看主页</view>
            </view>
          </view>
        </view>
      </view>
      
      <!-- 加载状态 -->
      <view class="load-status">
        <view class="loading" v-if="loading">
          <text>加载中...</text>
        </view>
        <view class="no-more" v-else-if="!hasMore && photographers.length">
          <text>没有更多了</text>
        </view>
        <view class="empty" v-else-if="!loading && !photographers.length">
          <image class="empty-image" src="/static/images/empty.png" mode="aspectFit" />
          <text>暂无摄影师</text>
        </view>
      </view>
    </scroll-view>
  </view>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { getPhotographers } from '@/api/photographer'
import { getStyleTags } from '@/api/base'

// 搜索相关
const keyword = ref('')
const focusSearch = ref(false)

// 筛选相关
const showFilterPanel = ref(false)
const currentFilter = ref('')
const styleTypes = ref([])
const selectedStyles = ref([])
const tempMinPrice = ref('')
const tempMaxPrice = ref('')

const filterParams = reactive({
  sortBy: 'rating',
  sortOrder: 'desc',
  styleIds: '',
  minPrice: null,
  maxPrice: null,
  minRating: null
})

const sortOptions = [
  { label: '综合推荐', value: 'rating' },
  { label: '价格从低到高', value: 'price', order: 'asc' },
  { label: '价格从高到低', value: 'price', order: 'desc' },
  { label: '订单数最多', value: 'orders' }
]

const sortText = computed(() => {
  const option = sortOptions.find(o => o.value === filterParams.sortBy)
  return option?.label || '综合推荐'
})

// 列表相关
const photographers = ref([])
const loading = ref(false)
const refreshing = ref(false)
const hasMore = ref(true)
const page = ref(1)
const pageSize = 10

// 获取摄影师列表
const fetchPhotographers = async (isRefresh = false) => {
  if (loading.value) return
  
  if (isRefresh) {
    page.value = 1
    hasMore.value = true
  }
  
  if (!hasMore.value) return
  
  loading.value = true
  
  try {
    const params = {
      page: page.value,
      pageSize,
      keyword: keyword.value,
      ...filterParams
    }
    
    const res = await getPhotographers(params)
    const list = res?.list || []
    
    if (isRefresh) {
      photographers.value = list
    } else {
      photographers.value = [...photographers.value, ...list]
    }
    
    hasMore.value = list.length === pageSize
    if (hasMore.value) {
      page.value++
    }
  } catch (err) {
    console.error('获取摄影师列表失败', err)
  } finally {
    loading.value = false
    refreshing.value = false
  }
}

// 加载更多
const loadMore = () => {
  if (!loading.value && hasMore.value) {
    fetchPhotographers()
  }
}

// 下拉刷新
const onRefresh = () => {
  refreshing.value = true
  fetchPhotographers(true)
}

// 搜索
const onSearch = () => {
  fetchPhotographers(true)
}

const clearKeyword = () => {
  keyword.value = ''
  fetchPhotographers(true)
}

// 筛选相关方法
const toggleFilter = (type) => {
  if (currentFilter.value === type) {
    closeFilterPanel()
  } else {
    currentFilter.value = type
    showFilterPanel.value = true
  }
}

const closeFilterPanel = () => {
  showFilterPanel.value = false
  currentFilter.value = ''
}

const selectSort = (option) => {
  filterParams.sortBy = option.value
  filterParams.sortOrder = option.order || 'desc'
  closeFilterPanel()
  fetchPhotographers(true)
}

const toggleStyle = (id) => {
  const index = selectedStyles.value.indexOf(id)
  if (index > -1) {
    selectedStyles.value.splice(index, 1)
  } else {
    selectedStyles.value.push(id)
  }
}

const resetStyles = () => {
  selectedStyles.value = []
}

const confirmStyles = () => {
  filterParams.styleIds = selectedStyles.value.join(',')
  closeFilterPanel()
  fetchPhotographers(true)
}

const setPrice = (min, max) => {
  tempMinPrice.value = min || ''
  tempMaxPrice.value = max || ''
}

const resetPrice = () => {
  tempMinPrice.value = ''
  tempMaxPrice.value = ''
}

const confirmPrice = () => {
  filterParams.minPrice = tempMinPrice.value || null
  filterParams.maxPrice = tempMaxPrice.value || null
  closeFilterPanel()
  fetchPhotographers(true)
}

const toggleRating = () => {
  filterParams.minRating = filterParams.minRating ? null : 4.5
  fetchPhotographers(true)
}

// 页面跳转
const goBack = () => {
  uni.navigateBack()
}

const goDetail = (id) => {
  uni.navigateTo({ url: `/pages/photographer/detail?id=${id}` })
}

// 获取页面参数
onLoad((options) => {
  if (options.focus) {
    focusSearch.value = true
  }
  if (options.styleId) {
    selectedStyles.value = [parseInt(options.styleId)]
    filterParams.styleIds = options.styleId
  }
  if (options.serviceTypeId) {
    filterParams.serviceTypeId = options.serviceTypeId
  }
})

onMounted(async () => {
  // 获取风格标签
  try {
    const styles = await getStyleTags({ type: 1 })
    styleTypes.value = styles || []
  } catch (err) {
    console.error('获取风格标签失败', err)
  }
  
  // 获取摄影师列表
  fetchPhotographers()
})
</script>

<style lang="scss" scoped>
@import '@/styles/variables.scss';

.page {
  min-height: 100vh;
  background: $bg-secondary;
  display: flex;
  flex-direction: column;
}

// 搜索栏
.search-header {
  display: flex;
  align-items: center;
  padding: $spacing-md $spacing-lg;
  background: $bg-primary;
  
  .search-box {
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
    
    .search-input {
      flex: 1;
      margin-left: $spacing-sm;
      font-size: $font-md;
    }
    
    .clear-icon {
      width: 32rpx;
      height: 32rpx;
      opacity: 0.5;
    }
  }
  
  .cancel-btn {
    margin-left: $spacing-md;
    font-size: $font-md;
    color: $primary-color;
  }
}

// 筛选栏
.filter-bar {
  background: $bg-primary;
  border-bottom: 1rpx solid $border-light;
  
  .filter-scroll {
    white-space: nowrap;
  }
  
  .filter-list {
    display: inline-flex;
    padding: $spacing-sm $spacing-lg;
    
    .filter-item {
      display: inline-flex;
      align-items: center;
      padding: 16rpx 24rpx;
      margin-right: $spacing-md;
      background: $gray-100;
      border-radius: $radius-round;
      font-size: $font-sm;
      color: $text-secondary;
      
      &.active {
        background: rgba($primary-color, 0.1);
        color: $primary-color;
      }
      
      .filter-arrow {
        width: 24rpx;
        height: 24rpx;
        margin-left: 8rpx;
        opacity: 0.5;
      }
    }
  }
}

// 筛选面板
.filter-panel {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  z-index: $z-modal-backdrop;
  
  .filter-content {
    position: absolute;
    top: 200rpx;
    left: 0;
    right: 0;
    background: $bg-primary;
    border-radius: 0 0 $radius-lg $radius-lg;
    padding: $spacing-lg;
    
    .filter-option {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: $spacing-md 0;
      font-size: $font-md;
      color: $text-primary;
      
      &.active {
        color: $primary-color;
      }
      
      .check-icon {
        width: 36rpx;
        height: 36rpx;
      }
    }
    
    .style-tags {
      display: flex;
      flex-wrap: wrap;
      gap: $spacing-sm;
      
      .style-tag {
        padding: 16rpx 28rpx;
        background: $gray-100;
        border-radius: $radius-round;
        font-size: $font-sm;
        color: $text-secondary;
        
        &.active {
          background: $primary-color;
          color: $text-white;
        }
      }
    }
    
    .price-range {
      display: flex;
      align-items: center;
      margin-bottom: $spacing-md;
      
      .price-input {
        flex: 1;
        height: 80rpx;
        padding: 0 $spacing-md;
        background: $gray-100;
        border-radius: $radius-md;
        text-align: center;
      }
      
      .price-line {
        margin: 0 $spacing-md;
        color: $text-tertiary;
      }
    }
    
    .price-presets {
      display: flex;
      flex-wrap: wrap;
      gap: $spacing-sm;
      
      .preset-item {
        padding: 16rpx 24rpx;
        background: $gray-100;
        border-radius: $radius-round;
        font-size: $font-sm;
        color: $text-secondary;
      }
    }
    
    .filter-actions {
      display: flex;
      gap: $spacing-md;
      margin-top: $spacing-lg;
      
      .btn-reset {
        flex: 1;
        height: 80rpx;
        display: flex;
        align-items: center;
        justify-content: center;
        background: $gray-100;
        border-radius: $radius-md;
        font-size: $font-md;
        color: $text-secondary;
      }
      
      .btn-confirm {
        flex: 2;
        height: 80rpx;
        display: flex;
        align-items: center;
        justify-content: center;
        background: $primary-color;
        border-radius: $radius-md;
        font-size: $font-md;
        color: $text-white;
      }
    }
  }
}

// 摄影师列表
.photographer-scroll {
  flex: 1;
  height: 0;
}

.photographer-list {
  padding: $spacing-lg;
}

.photographer-item {
  background: $bg-primary;
  border-radius: $radius-lg;
  overflow: hidden;
  margin-bottom: $spacing-lg;
  box-shadow: $shadow-sm;
  
  .works-preview {
    display: flex;
    height: 200rpx;
    
    .preview-image {
      flex: 1;
      height: 100%;
      
      & + .preview-image {
        margin-left: 4rpx;
      }
    }
    
    .preview-placeholder {
      flex: 1;
      
      image {
        width: 100%;
        height: 100%;
      }
    }
  }
  
  .photographer-info {
    padding: $spacing-lg;
    
    .info-header {
      display: flex;
      
      .avatar {
        width: 88rpx;
        height: 88rpx;
        border-radius: 50%;
      }
      
      .info-main {
        flex: 1;
        margin-left: $spacing-md;
        
        .name-row {
          display: flex;
          align-items: center;
          
          .name {
            font-size: $font-lg;
            font-weight: $font-semibold;
            color: $text-primary;
          }
          
          .verified-badge {
            margin-left: 8rpx;
            
            image {
              width: 32rpx;
              height: 32rpx;
            }
          }
        }
        
        .stats-row {
          display: flex;
          align-items: center;
          margin-top: 8rpx;
          font-size: $font-sm;
          color: $text-secondary;
          
          .rating {
            display: flex;
            align-items: center;
            color: $warning-color;
            
            .star {
              width: 24rpx;
              height: 24rpx;
              margin-right: 4rpx;
            }
          }
          
          .divider {
            margin: 0 $spacing-sm;
            color: $border-color;
          }
        }
      }
    }
    
    .tags-row {
      display: flex;
      flex-wrap: wrap;
      gap: $spacing-sm;
      margin-top: $spacing-md;
      
      .tag {
        padding: 8rpx 16rpx;
        background: $gray-100;
        border-radius: $radius-sm;
        font-size: $font-xs;
        color: $text-secondary;
      }
    }
    
    .price-row {
      display: flex;
      align-items: center;
      justify-content: space-between;
      margin-top: $spacing-lg;
      
      .price {
        display: flex;
        align-items: baseline;
        
        .symbol {
          font-size: $font-sm;
          color: $danger-color;
        }
        
        .value {
          font-size: $font-xxl;
          font-weight: $font-bold;
          color: $danger-color;
        }
        
        .unit {
          margin-left: 4rpx;
          font-size: $font-xs;
          color: $text-secondary;
        }
      }
      
      .action-btn {
        padding: 16rpx 32rpx;
        background: $primary-color;
        border-radius: $radius-round;
        font-size: $font-sm;
        color: $text-white;
      }
    }
  }
}

// 加载状态
.load-status {
  padding: $spacing-lg;
  text-align: center;
  
  .loading, .no-more {
    font-size: $font-sm;
    color: $text-tertiary;
  }
  
  .empty {
    padding: 100rpx 0;
    
    .empty-image {
      width: 200rpx;
      height: 200rpx;
      opacity: 0.5;
    }
    
    text {
      display: block;
      margin-top: $spacing-md;
      font-size: $font-md;
      color: $text-tertiary;
    }
  }
}
</style>
