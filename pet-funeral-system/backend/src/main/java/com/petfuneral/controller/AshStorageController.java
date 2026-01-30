package com.petfuneral.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.petfuneral.common.response.PageResult;
import com.petfuneral.common.response.Result;
import com.petfuneral.dto.AshStorageCreateRequest;
import com.petfuneral.dto.AshStorageDTO;
import com.petfuneral.entity.AshStorage;
import com.petfuneral.service.AshStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 骨灰寄存控制器
 */
@Tag(name = "骨灰寄存")
@RestController
@RequestMapping("/api/v1/ash-storage")
@RequiredArgsConstructor
public class AshStorageController {

    private final AshStorageService ashStorageService;

    @Operation(summary = "分页查询骨灰寄存")
    @GetMapping
    public Result<PageResult<AshStorageDTO>> getStoragePage(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String keyword) {
        IPage<AshStorageDTO> result = ashStorageService.getStoragePage(page, size, status, keyword);
        return Result.success(PageResult.of(result));
    }

    @Operation(summary = "获取寄存详情")
    @GetMapping("/{id}")
    public Result<AshStorageDTO> getStorageDetail(@PathVariable Long id) {
        return Result.success(ashStorageService.getStorageDetail(id));
    }

    @Operation(summary = "获取客户的寄存记录")
    @GetMapping("/customer/{customerId}")
    public Result<List<AshStorageDTO>> getByCustomerId(@PathVariable Long customerId) {
        return Result.success(ashStorageService.getByCustomerId(customerId));
    }

    @Operation(summary = "创建骨灰寄存")
    @PostMapping
    public Result<AshStorage> createStorage(@Valid @RequestBody AshStorageCreateRequest request) {
        AshStorage storage = ashStorageService.createStorage(request);
        return Result.success("创建成功", storage);
    }

    @Operation(summary = "续期")
    @PostMapping("/{id}/renew")
    public Result<Void> renew(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate newEndDate) {
        ashStorageService.renew(id, newEndDate);
        return Result.ok("续期成功");
    }

    @Operation(summary = "取走骨灰")
    @PostMapping("/{id}/pickup")
    public Result<Void> pickup(@PathVariable Long id) {
        ashStorageService.pickup(id);
        return Result.ok("已登记取走");
    }

    @Operation(summary = "获取即将到期的寄存记录")
    @GetMapping("/expiring")
    public Result<List<AshStorageDTO>> getExpiringStorages(
            @RequestParam(required = false, defaultValue = "7") Integer days) {
        return Result.success(ashStorageService.getExpiringStorages(days));
    }

    @Operation(summary = "获取已过期的寄存记录")
    @GetMapping("/expired")
    public Result<List<AshStorageDTO>> getExpiredStorages() {
        return Result.success(ashStorageService.getExpiredStorages());
    }

    @Operation(summary = "批量更新过期状态")
    @PostMapping("/update-expired")
    public Result<Integer> updateExpiredStatus() {
        int count = ashStorageService.updateExpiredStatus();
        return Result.success("已更新 " + count + " 条记录", count);
    }

    @Operation(summary = "统计寄存数量")
    @GetMapping("/count")
    public Result<Long> countStorages(@RequestParam(required = false) Integer status) {
        return Result.success(ashStorageService.countByStatus(status));
    }
}
