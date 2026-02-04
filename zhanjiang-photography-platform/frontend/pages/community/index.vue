<template>
  <view class="page">
    <!-- 顶部Tab -->
    <view class="top-bar">
      <view class="tabs">
        <view class="tab-item" 
              :class="{ active: currentTab === 'recommend' }"
              @click="switchTab('recommend')">推荐</view>
        <view class="tab-item"
              :class="{ active: currentTab === 'following' }"
              @click="switchTab('following')">关注</view>
        <view class="tab-item"
              :class="{ active: currentTab === 'topic' }"
              @click="switchTab('topic')">话题</view>
      </view>
      <view class="search-btn" @click="goSearch">
        <image src="/static/icons/search.png" mode="aspectFit" />
      </view>
    </view>
    
    <!-- 话题栏 -->
    <scroll-view class="topic-bar" scroll-x v-if="currentTab !== 'topic'">
      <view class="topic-list">
        <view class="topic-item" 
              v-for="topic in hotTopics" 
              :key="topic.id"
              :class="{ active: selectedTopicId === topic.id }"
              @click="selectTopic(topic)">
          #{{ topic.topicName }}
        </view>
      </view>
    </scroll-view>
    
    <!-- 动态列表 -->
    <scroll-view class="post-scroll" 
                 scroll-y
                 @scrolltolower="loadMore"
                 :refresher-enabled="true"
                 :refresher-triggered="refreshing"
                 @refresherrefresh="onRefresh">
      <!-- 瀑布流布局 -->
      <view class="post-waterfall">
        <view class="waterfall-column">
          <view class="post-card" 
                v-for="post in leftPosts" 
                :key="post.id"
                @click="goPostDetail(post.id)">
            <image class="post-cover" 
                   :src="post.images?.[0]?.url || '/static/images/default-cover.jpg'" 
                   mode="widthFix" />
            <view class="post-content">
              <text class="post-text">{{ post.content }}</text>
              <view class="post-footer">
                <view class="post-user">
                  <image class="user-avatar" :src="post.user.avatar" mode="aspectFill" />
                  <text class="user-name">{{ post.user.nickname }}</text>
                </view>
                <view class="post-likes" @click.stop="toggleLike(post)">
                  <image :src="post.isLiked ? '/static/icons/heart-fill.png' : '/static/icons/heart.png'" mode="aspectFit" />
                  <text>{{ post.likeCount }}</text>
                </view>
              </view>
            </view>
          </view>
        </view>
        <view class="waterfall-column">
          <view class="post-card" 
                v-for="post in rightPosts" 
                :key="post.id"
                @click="goPostDetail(post.id)">
            <image class="post-cover" 
                   :src="post.images?.[0]?.url || '/static/images/default-cover.jpg'" 
                   mode="widthFix" />
            <view class="post-content">
              <text class="post-text">{{ post.content }}</text>
              <view class="post-footer">
                <view class="post-user">
                  <image class="user-avatar" :src="post.user.avatar" mode="aspectFill" />
                  <text class="user-name">{{ post.user.nickname }}</text>
                </view>
                <view class="post-likes" @click.stop="toggleLike(post)">
                  <image :src="post.isLiked ? '/static/icons/heart-fill.png' : '/static/icons/heart.png'" mode="aspectFit" />
                  <text>{{ post.likeCount }}</text>
                </view>
              </view>
            </view>
          </view>
        </view>
      </view>
      
      <!-- 加载状态 -->
      <view class="load-status">
        <text v-if="loading">加载中...</text>
        <text v-else-if="!hasMore && posts.length">没有更多了</text>
      </view>
      
      <!-- 空状态 -->
      <view class="empty" v-if="!loading && !posts.length">
        <image class="empty-image" src="/static/images/empty-post.png" mode="aspectFit" />
        <text>暂无动态</text>
      </view>
    </scroll-view>
    
    <!-- 发布按钮 -->
    <view class="publish-btn" @click="goPublish">
      <image src="/static/icons/plus-white.png" mode="aspectFit" />
    </view>
  </view>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'

const currentTab = ref('recommend')
const selectedTopicId = ref(null)
const hotTopics = ref([
  { id: 1, topicName: '毕业季' },
  { id: 2, topicName: '情侣日常' },
  { id: 3, topicName: '校园生活' },
  { id: 4, topicName: '湛江约拍' },
  { id: 5, topicName: '摄影技巧' }
])

const posts = ref([])
const loading = ref(false)
const refreshing = ref(false)
const hasMore = ref(true)
const page = ref(1)

// 瀑布流分列
const leftPosts = computed(() => posts.value.filter((_, i) => i % 2 === 0))
const rightPosts = computed(() => posts.value.filter((_, i) => i % 2 === 1))

// 模拟数据
const mockPosts = () => {
  return Array.from({ length: 10 }, (_, i) => ({
    id: Date.now() + i,
    content: '今天去海边拍了一组照片，感觉效果还不错~',
    images: [{ url: '/static/images/sample' + ((i % 3) + 1) + '.jpg' }],
    user: {
      id: 1,
      nickname: '摄影师小明',
      avatar: '/static/images/default-avatar.png'
    },
    likeCount: Math.floor(Math.random() * 1000),
    isLiked: false,
    createTime: '2小时前'
  }))
}

// 获取动态列表
const fetchPosts = async (isRefresh = false) => {
  if (loading.value) return
  
  if (isRefresh) {
    page.value = 1
    hasMore.value = true
  }
  
  if (!hasMore.value) return
  
  loading.value = true
  
  // 模拟API请求
  setTimeout(() => {
    const newPosts = mockPosts()
    
    if (isRefresh) {
      posts.value = newPosts
    } else {
      posts.value = [...posts.value, ...newPosts]
    }
    
    hasMore.value = page.value < 3
    if (hasMore.value) {
      page.value++
    }
    
    loading.value = false
    refreshing.value = false
  }, 1000)
}

// 切换Tab
const switchTab = (tab) => {
  currentTab.value = tab
  fetchPosts(true)
}

// 选择话题
const selectTopic = (topic) => {
  if (selectedTopicId.value === topic.id) {
    selectedTopicId.value = null
  } else {
    selectedTopicId.value = topic.id
  }
  fetchPosts(true)
}

// 点赞
const toggleLike = (post) => {
  post.isLiked = !post.isLiked
  post.likeCount += post.isLiked ? 1 : -1
}

// 加载更多
const loadMore = () => {
  if (!loading.value && hasMore.value) {
    fetchPosts()
  }
}

// 下拉刷新
const onRefresh = () => {
  refreshing.value = true
  fetchPosts(true)
}

// 页面跳转
const goSearch = () => {
  uni.navigateTo({ url: '/pages/community/search' })
}

const goPostDetail = (id) => {
  uni.navigateTo({ url: `/pages/community/detail?id=${id}` })
}

const goPublish = () => {
  uni.navigateTo({ url: '/pages/community/publish' })
}

onMounted(() => {
  fetchPosts()
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

// 顶部栏
.top-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: $spacing-md $spacing-lg;
  background: $bg-primary;
  
  .tabs {
    display: flex;
    gap: $spacing-lg;
    
    .tab-item {
      font-size: $font-lg;
      color: $text-secondary;
      
      &.active {
        color: $text-primary;
        font-weight: $font-bold;
      }
    }
  }
  
  .search-btn {
    width: 64rpx;
    height: 64rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    
    image {
      width: 40rpx;
      height: 40rpx;
      opacity: 0.6;
    }
  }
}

// 话题栏
.topic-bar {
  white-space: nowrap;
  background: $bg-primary;
  border-bottom: 1rpx solid $border-light;
  
  .topic-list {
    display: inline-flex;
    padding: $spacing-sm $spacing-lg;
    gap: $spacing-sm;
    
    .topic-item {
      display: inline-flex;
      padding: 12rpx 24rpx;
      background: $gray-100;
      border-radius: $radius-round;
      font-size: $font-sm;
      color: $text-secondary;
      
      &.active {
        background: rgba($primary-color, 0.1);
        color: $primary-color;
      }
    }
  }
}

// 动态列表
.post-scroll {
  flex: 1;
  height: 0;
}

.post-waterfall {
  display: flex;
  padding: $spacing-sm;
  gap: $spacing-sm;
  
  .waterfall-column {
    flex: 1;
    display: flex;
    flex-direction: column;
    gap: $spacing-sm;
  }
}

.post-card {
  background: $bg-primary;
  border-radius: $radius-lg;
  overflow: hidden;
  
  .post-cover {
    width: 100%;
    display: block;
  }
  
  .post-content {
    padding: $spacing-sm;
    
    .post-text {
      font-size: $font-sm;
      color: $text-primary;
      display: -webkit-box;
      -webkit-box-orient: vertical;
      -webkit-line-clamp: 2;
      overflow: hidden;
    }
    
    .post-footer {
      display: flex;
      align-items: center;
      justify-content: space-between;
      margin-top: $spacing-sm;
      
      .post-user {
        display: flex;
        align-items: center;
        
        .user-avatar {
          width: 40rpx;
          height: 40rpx;
          border-radius: 50%;
        }
        
        .user-name {
          margin-left: 8rpx;
          font-size: $font-xs;
          color: $text-secondary;
          max-width: 120rpx;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }
      }
      
      .post-likes {
        display: flex;
        align-items: center;
        
        image {
          width: 32rpx;
          height: 32rpx;
        }
        
        text {
          margin-left: 4rpx;
          font-size: $font-xs;
          color: $text-tertiary;
        }
      }
    }
  }
}

// 加载状态
.load-status {
  padding: $spacing-lg;
  text-align: center;
  font-size: $font-sm;
  color: $text-tertiary;
}

// 空状态
.empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 120rpx $spacing-lg;
  
  .empty-image {
    width: 200rpx;
    height: 200rpx;
    opacity: 0.5;
  }
  
  text {
    margin-top: $spacing-md;
    font-size: $font-md;
    color: $text-tertiary;
  }
}

// 发布按钮
.publish-btn {
  position: fixed;
  right: 40rpx;
  bottom: 160rpx;
  width: 100rpx;
  height: 100rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  background: $primary-color;
  border-radius: 50%;
  box-shadow: 0 8rpx 24rpx rgba($primary-color, 0.4);
  
  image {
    width: 48rpx;
    height: 48rpx;
  }
}
</style>
