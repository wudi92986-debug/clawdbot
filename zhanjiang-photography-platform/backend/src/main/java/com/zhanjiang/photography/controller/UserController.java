package com.zhanjiang.photography.controller;

import com.zhanjiang.photography.common.result.Result;
import com.zhanjiang.photography.dto.request.WechatLoginRequest;
import com.zhanjiang.photography.dto.response.LoginResponse;
import com.zhanjiang.photography.entity.User;
import com.zhanjiang.photography.security.UserContext;
import com.zhanjiang.photography.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 用户控制器
 */
@Tag(name = "用户模块")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    @Operation(summary = "微信登录")
    @PostMapping("/login/wechat")
    public Result<LoginResponse> wechatLogin(@Valid @RequestBody WechatLoginRequest request,
                                              HttpServletRequest httpRequest) {
        String ip = getClientIp(httpRequest);
        LoginResponse response = userService.wechatLogin(request, ip);
        return Result.success(response);
    }
    
    @Operation(summary = "刷新Token")
    @PostMapping("/token/refresh")
    public Result<LoginResponse> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        LoginResponse response = userService.refreshToken(refreshToken);
        return Result.success(response);
    }
    
    @Operation(summary = "获取用户信息")
    @GetMapping("/profile")
    public Result<User> getProfile() {
        Long userId = UserContext.getUserId();
        User user = userService.getUserProfile(userId);
        return Result.success(user);
    }
    
    @Operation(summary = "更新用户信息")
    @PutMapping("/profile")
    public Result<Void> updateProfile(@RequestBody User user) {
        Long userId = UserContext.getUserId();
        userService.updateUserProfile(userId, user);
        return Result.success();
    }
    
    /**
     * 获取客户端IP
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 多个代理的情况，取第一个IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
