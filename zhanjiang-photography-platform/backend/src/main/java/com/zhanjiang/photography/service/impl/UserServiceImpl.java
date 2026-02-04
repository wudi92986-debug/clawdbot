package com.zhanjiang.photography.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhanjiang.photography.common.exception.BusinessException;
import com.zhanjiang.photography.common.result.ResultCode;
import com.zhanjiang.photography.common.utils.JwtUtils;
import com.zhanjiang.photography.dto.request.WechatLoginRequest;
import com.zhanjiang.photography.dto.response.LoginResponse;
import com.zhanjiang.photography.entity.User;
import com.zhanjiang.photography.mapper.UserMapper;
import com.zhanjiang.photography.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 用户服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    
    private final JwtUtils jwtUtils;
    private final WxMaService wxMaService;
    
    @Override
    @Transactional
    public LoginResponse wechatLogin(WechatLoginRequest request, String ip) {
        // 调用微信接口获取openid
        String openid;
        String unionId = null;
        
        try {
            WxMaJscode2SessionResult session = wxMaService.getUserService().getSessionInfo(request.getCode());
            openid = session.getOpenid();
            unionId = session.getUnionid();
        } catch (WxErrorException e) {
            log.error("微信登录失败: {}", e.getMessage());
            throw new BusinessException(ResultCode.WECHAT_LOGIN_FAILED);
        }
        
        // 查询用户是否存在
        User user = getByOpenid(openid);
        boolean isNewUser = false;
        
        if (user == null) {
            // 新用户，创建账号
            isNewUser = true;
            user = new User();
            user.setOpenid(openid);
            user.setUnionId(unionId);
            user.setNickname("用户" + System.currentTimeMillis() % 100000);
            user.setUserType(1); // 普通用户
            user.setStatus(1);   // 正常状态
            user.setIsVerified(0);
            user.setIsStudentVerified(0);
            user.setLastLoginTime(LocalDateTime.now());
            user.setLastLoginIp(ip);
            save(user);
        } else {
            // 更新登录信息
            user.setLastLoginTime(LocalDateTime.now());
            user.setLastLoginIp(ip);
            updateById(user);
        }
        
        // 检查用户状态
        if (user.getStatus() == 0) {
            throw new BusinessException(ResultCode.ACCOUNT_DISABLED);
        }
        
        // 生成Token
        String accessToken = jwtUtils.generateAccessToken(user.getId(), user.getUserType());
        String refreshToken = jwtUtils.generateRefreshToken(user.getId());
        
        // 构建响应
        LoginResponse response = new LoginResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setExpiresIn(jwtUtils.getExpirationInSeconds());
        
        LoginResponse.UserInfoResponse userInfo = new LoginResponse.UserInfoResponse();
        userInfo.setUserId(user.getId());
        userInfo.setNickname(user.getNickname());
        userInfo.setAvatar(user.getAvatar());
        userInfo.setUserType(user.getUserType());
        userInfo.setIsVerified(user.getIsVerified() == 1);
        userInfo.setIsStudentVerified(user.getIsStudentVerified() == 1);
        userInfo.setIsNewUser(isNewUser);
        response.setUserInfo(userInfo);
        
        return response;
    }
    
    @Override
    public LoginResponse refreshToken(String refreshToken) {
        // 验证刷新Token
        if (!jwtUtils.validateToken(refreshToken) || !jwtUtils.isRefreshToken(refreshToken)) {
            throw new BusinessException(ResultCode.TOKEN_INVALID);
        }
        
        Long userId = jwtUtils.getUserIdFromToken(refreshToken);
        User user = getById(userId);
        
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_EXIST);
        }
        
        if (user.getStatus() == 0) {
            throw new BusinessException(ResultCode.ACCOUNT_DISABLED);
        }
        
        // 生成新Token
        String newAccessToken = jwtUtils.generateAccessToken(user.getId(), user.getUserType());
        String newRefreshToken = jwtUtils.generateRefreshToken(user.getId());
        
        LoginResponse response = new LoginResponse();
        response.setAccessToken(newAccessToken);
        response.setRefreshToken(newRefreshToken);
        response.setExpiresIn(jwtUtils.getExpirationInSeconds());
        
        return response;
    }
    
    @Override
    public User getUserProfile(Long userId) {
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_EXIST);
        }
        // 隐藏敏感信息
        user.setIdCard(null);
        return user;
    }
    
    @Override
    public void updateUserProfile(Long userId, User updateUser) {
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_EXIST);
        }
        
        // 只允许更新部分字段
        if (updateUser.getNickname() != null) {
            user.setNickname(updateUser.getNickname());
        }
        if (updateUser.getAvatar() != null) {
            user.setAvatar(updateUser.getAvatar());
        }
        if (updateUser.getGender() != null) {
            user.setGender(updateUser.getGender());
        }
        
        updateById(user);
    }
    
    @Override
    public User getByOpenid(String openid) {
        return getOne(new LambdaQueryWrapper<User>()
                .eq(User::getOpenid, openid)
                .eq(User::getIsDeleted, 0));
    }
}
