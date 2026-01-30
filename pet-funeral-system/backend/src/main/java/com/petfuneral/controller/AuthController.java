package com.petfuneral.controller;

import com.petfuneral.common.response.Result;
import com.petfuneral.dto.LoginRequest;
import com.petfuneral.dto.LoginResponse;
import com.petfuneral.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 */
@Tag(name = "认证管理")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "管理员登录")
    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return Result.success(response);
    }

    @Operation(summary = "初始化管理员 (仅用于首次部署)")
    @PostMapping("/init")
    public Result<String> initAdmin() {
        authService.createAdmin("admin", "123456", "系统管理员");
        return Result.success("初始化成功", "用户名: admin，密码: 123456");
    }
}
