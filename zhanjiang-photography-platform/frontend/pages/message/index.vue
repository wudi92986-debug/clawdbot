<template>
  <view class="page">
    <!-- 消息Tab -->
    <view class="tab-bar">
      <view class="tab-item" 
            :class="{ active: currentTab === 'chat' }"
            @click="switchTab('chat')">
        <text>聊天</text>
        <view class="badge" v-if="chatUnread">{{ chatUnread > 99 ? '99+' : chatUnread }}</view>
      </view>
      <view class="tab-item"
            :class="{ active: currentTab === 'notification' }"
            @click="switchTab('notification')">
        <text>通知</text>
        <view class="badge" v-if="notifyUnread">{{ notifyUnread > 99 ? '99+' : notifyUnread }}</view>
      </view>
    </view>
    
    <!-- 聊天列表 -->
    <view class="chat-list" v-show="currentTab === 'chat'">
      <view class="chat-item" 
            v-for="item in conversations" 
            :key="item.id"
            @click="goChat(item)">
        <view class="avatar-wrap">
          <image class="avatar" :src="item.targetUser.avatar || '/static/images/default-avatar.png'" mode="aspectFill" />
          <view class="online-dot" v-if="item.isOnline"></view>
        </view>
        <view class="chat-content">
          <view class="chat-header">
            <text class="chat-name">{{ item.targetUser.nickname }}</text>
            <text class="chat-time">{{ formatTime(item.lastMessage.time) }}</text>
          </view>
          <view class="chat-message">
            <text class="message-text">{{ item.lastMessage.content }}</text>
            <view class="unread-badge" v-if="item.unreadCount">{{ item.unreadCount }}</view>
          </view>
        </view>
      </view>
      
      <view class="empty" v-if="!conversations.length">
        <image class="empty-image" src="/static/images/empty-message.png" mode="aspectFit" />
        <text>暂无消息</text>
      </view>
    </view>
    
    <!-- 通知列表 -->
    <view class="notification-list" v-show="currentTab === 'notification'">
      <view class="notification-group" v-for="group in notificationGroups" :key="group.type">
        <view class="notification-item" @click="goNotificationDetail(group.type)">
          <view class="notify-icon-wrap" :class="group.iconClass">
            <image :src="group.icon" mode="aspectFit" />
          </view>
          <view class="notify-content">
            <view class="notify-header">
              <text class="notify-title">{{ group.title }}</text>
              <text class="notify-time">{{ group.lastTime }}</text>
            </view>
            <text class="notify-desc">{{ group.lastContent }}</text>
          </view>
          <view class="notify-badge" v-if="group.unreadCount">{{ group.unreadCount }}</view>
        </view>
      </view>
      
      <view class="empty" v-if="!notificationGroups.length">
        <image class="empty-image" src="/static/images/empty-notification.png" mode="aspectFit" />
        <text>暂无通知</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import dayjs from 'dayjs'

const currentTab = ref('chat')
const chatUnread = ref(0)
const notifyUnread = ref(0)

const conversations = ref([])
const notificationGroups = ref([
  {
    type: 'order',
    title: '订单通知',
    icon: '/static/icons/notify-order.png',
    iconClass: 'order',
    lastContent: '您有一个新订单待处理',
    lastTime: '刚刚',
    unreadCount: 2
  },
  {
    type: 'system',
    title: '系统消息',
    icon: '/static/icons/notify-system.png',
    iconClass: 'system',
    lastContent: '欢迎使用湛江约拍平台',
    lastTime: '昨天',
    unreadCount: 0
  },
  {
    type: 'activity',
    title: '活动通知',
    icon: '/static/icons/notify-activity.png',
    iconClass: 'activity',
    lastContent: '毕业季特惠活动开始啦',
    lastTime: '3天前',
    unreadCount: 1
  }
])

// 切换Tab
const switchTab = (tab) => {
  currentTab.value = tab
}

// 格式化时间
const formatTime = (time) => {
  if (!time) return ''
  const date = dayjs(time)
  const now = dayjs()
  
  if (date.isSame(now, 'day')) {
    return date.format('HH:mm')
  } else if (date.isSame(now.subtract(1, 'day'), 'day')) {
    return '昨天'
  } else if (date.isSame(now, 'year')) {
    return date.format('MM-DD')
  } else {
    return date.format('YYYY-MM-DD')
  }
}

// 跳转聊天
const goChat = (item) => {
  uni.navigateTo({
    url: `/pages/message/chat?targetId=${item.targetUser.id}&name=${item.targetUser.nickname}`
  })
}

// 跳转通知详情
const goNotificationDetail = (type) => {
  uni.navigateTo({
    url: `/pages/message/notification?type=${type}`
  })
}

onMounted(() => {
  // TODO: 获取会话列表和通知
})
</script>

<style lang="scss" scoped>
@import '@/styles/variables.scss';

.page {
  min-height: 100vh;
  background: $bg-secondary;
}

// Tab栏
.tab-bar {
  display: flex;
  background: $bg-primary;
  padding: $spacing-sm $spacing-lg;
  
  .tab-item {
    position: relative;
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: $spacing-md 0;
    font-size: $font-lg;
    color: $text-secondary;
    
    &.active {
      color: $text-primary;
      font-weight: $font-semibold;
    }
    
    .badge {
      position: absolute;
      top: 8rpx;
      right: calc(50% - 60rpx);
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
}

// 聊天列表
.chat-list {
  padding: $spacing-sm 0;
  
  .chat-item {
    display: flex;
    align-items: center;
    padding: $spacing-md $spacing-lg;
    background: $bg-primary;
    
    & + .chat-item {
      border-top: 1rpx solid $border-light;
    }
    
    .avatar-wrap {
      position: relative;
      
      .avatar {
        width: 96rpx;
        height: 96rpx;
        border-radius: 50%;
      }
      
      .online-dot {
        position: absolute;
        bottom: 4rpx;
        right: 4rpx;
        width: 20rpx;
        height: 20rpx;
        background: $success-color;
        border: 4rpx solid $bg-primary;
        border-radius: 50%;
      }
    }
    
    .chat-content {
      flex: 1;
      margin-left: $spacing-md;
      overflow: hidden;
      
      .chat-header {
        display: flex;
        align-items: center;
        justify-content: space-between;
        
        .chat-name {
          font-size: $font-md;
          font-weight: $font-medium;
          color: $text-primary;
        }
        
        .chat-time {
          font-size: $font-xs;
          color: $text-tertiary;
        }
      }
      
      .chat-message {
        display: flex;
        align-items: center;
        margin-top: 8rpx;
        
        .message-text {
          flex: 1;
          font-size: $font-sm;
          color: $text-secondary;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }
        
        .unread-badge {
          min-width: 36rpx;
          height: 36rpx;
          padding: 0 10rpx;
          margin-left: $spacing-sm;
          background: $danger-color;
          border-radius: 18rpx;
          font-size: $font-xs;
          color: $text-white;
          text-align: center;
          line-height: 36rpx;
        }
      }
    }
  }
}

// 通知列表
.notification-list {
  padding: $spacing-sm 0;
  
  .notification-item {
    display: flex;
    align-items: center;
    padding: $spacing-lg;
    background: $bg-primary;
    
    & + .notification-item {
      margin-top: $spacing-sm;
    }
    
    .notify-icon-wrap {
      width: 88rpx;
      height: 88rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      border-radius: $radius-md;
      
      &.order {
        background: rgba($primary-color, 0.1);
      }
      
      &.system {
        background: rgba($info-color, 0.1);
      }
      
      &.activity {
        background: rgba($warning-color, 0.1);
      }
      
      image {
        width: 48rpx;
        height: 48rpx;
      }
    }
    
    .notify-content {
      flex: 1;
      margin-left: $spacing-md;
      overflow: hidden;
      
      .notify-header {
        display: flex;
        align-items: center;
        justify-content: space-between;
        
        .notify-title {
          font-size: $font-md;
          font-weight: $font-medium;
          color: $text-primary;
        }
        
        .notify-time {
          font-size: $font-xs;
          color: $text-tertiary;
        }
      }
      
      .notify-desc {
        display: block;
        margin-top: 8rpx;
        font-size: $font-sm;
        color: $text-secondary;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
    }
    
    .notify-badge {
      min-width: 36rpx;
      height: 36rpx;
      padding: 0 10rpx;
      margin-left: $spacing-sm;
      background: $danger-color;
      border-radius: 18rpx;
      font-size: $font-xs;
      color: $text-white;
      text-align: center;
      line-height: 36rpx;
    }
  }
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
</style>
