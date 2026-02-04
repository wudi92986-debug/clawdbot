package com.zhanjiang.photography.security;

import lombok.Data;

/**
 * 用户上下文，存储当前登录用户信息
 */
public class UserContext {
    
    private static final ThreadLocal<UserInfo> USER_HOLDER = new ThreadLocal<>();
    
    public static void setUser(UserInfo userInfo) {
        USER_HOLDER.set(userInfo);
    }
    
    public static UserInfo getUser() {
        return USER_HOLDER.get();
    }
    
    public static Long getUserId() {
        UserInfo userInfo = USER_HOLDER.get();
        return userInfo != null ? userInfo.getUserId() : null;
    }
    
    public static Integer getUserType() {
        UserInfo userInfo = USER_HOLDER.get();
        return userInfo != null ? userInfo.getUserType() : null;
    }
    
    public static void clear() {
        USER_HOLDER.remove();
    }
    
    @Data
    public static class UserInfo {
        private Long userId;
        private Integer userType;
        
        public UserInfo(Long userId, Integer userType) {
            this.userId = userId;
            this.userType = userType;
        }
    }
}
