package com.petfuneral.controller;

import com.petfuneral.common.response.Result;
import com.petfuneral.entity.ServicePackage;
import com.petfuneral.service.PackageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 服务套餐控制器
 */
@Tag(name = "服务套餐")
@RestController
@RequestMapping("/api/v1/packages")
@RequiredArgsConstructor
public class PackageController {

    private final PackageService packageService;

    @Operation(summary = "获取上架套餐列表")
    @GetMapping("/active")
    public Result<List<ServicePackage>> getActivePackages() {
        return Result.success(packageService.getActivePackages());
    }

    @Operation(summary = "获取所有套餐 (管理端)")
    @GetMapping
    public Result<List<ServicePackage>> getAllPackages() {
        return Result.success(packageService.getAllPackages());
    }

    @Operation(summary = "获取套餐详情")
    @GetMapping("/{id}")
    public Result<ServicePackage> getPackageDetail(@PathVariable Long id) {
        return Result.success(packageService.getPackageDetail(id));
    }

    @Operation(summary = "创建套餐")
    @PostMapping
    public Result<ServicePackage> createPackage(@RequestBody ServicePackage servicePackage) {
        return Result.success("创建成功", packageService.createPackage(servicePackage));
    }

    @Operation(summary = "更新套餐")
    @PutMapping("/{id}")
    public Result<Void> updatePackage(@PathVariable Long id, @RequestBody ServicePackage servicePackage) {
        servicePackage.setId(id);
        packageService.updatePackage(servicePackage);
        return Result.success("更新成功");
    }

    @Operation(summary = "上架/下架套餐")
    @PostMapping("/{id}/toggle")
    public Result<Void> toggleStatus(@PathVariable Long id) {
        packageService.toggleStatus(id);
        return Result.success("操作成功");
    }
}
