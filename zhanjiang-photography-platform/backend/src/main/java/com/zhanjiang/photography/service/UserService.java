package com.zhanjiang.photography.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhanjiang.photography.dto.request.WechatLoginRequest;
import com.zhanjiang.photography.dto.response.LoginResponse;
import com.zhanjiang.photography.entity.User;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {
    
    /**
     * 微信登录
     */
    LoginResponse wechatLogin(WechatLoginRequest request, String ip);
    
    /**
     * 刷新Token
     */
    LoginResponse refreshToken(String refreshToken);
    
    /**
     * 获取用户信息
     */
    User getUserProfile(Long userId);
    
    /**
     * 更新用户信息
     */
    void updateUserProfile(Long userId, User user);
    
    /**
     * 根据openid查询用户
     */
    User getByOpenid(String openid);
}
