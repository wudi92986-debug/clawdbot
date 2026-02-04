package com.zhanjiang.photography.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhanjiang.photography.common.result.PageResult;
import com.zhanjiang.photography.common.result.Result;
import com.zhanjiang.photography.entity.Photographer;
import com.zhanjiang.photography.security.UserContext;
import com.zhanjiang.photography.service.PhotographerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 摄影师控制器
 */
@Tag(name = "摄影师模块")
@RestController
@RequestMapping("/photographers")
@RequiredArgsConstructor
public class PhotographerController {
    
    private final PhotographerService photographerService;
    
    @Operation(summary = "获取摄影师列表")
    @GetMapping
    public Result<PageResult<Map<String, Object>>> listPhotographers(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "风格ID，多个逗号分隔") @RequestParam(required = false) String styleIds,
            @Parameter(description = "服务类型ID") @RequestParam(required = false) Long serviceTypeId,
            @Parameter(description = "最低价格") @RequestParam(required = false) Double minPrice,
            @Parameter(description = "最高价格") @RequestParam(required = false) Double maxPrice,
            @Parameter(description = "最低评分") @RequestParam(required = false) Double minRating,
            @Parameter(description = "性别") @RequestParam(required = false) Integer gender,
            @Parameter(description = "高校ID") @RequestParam(required = false) Long universityId,
            @Parameter(description = "搜索关键词") @RequestParam(required = false) String keyword,
            @Parameter(description = "排序字段") @RequestParam(defaultValue = "rating") String sortBy,
            @Parameter(description = "排序方式") @RequestParam(defaultValue = "desc") String sortOrder,
            @Parameter(description = "经度") @RequestParam(required = false) Double longitude,
            @Parameter(description = "纬度") @RequestParam(required = false) Double latitude) {
        
        Page<Map<String, Object>> result = photographerService.listPhotographers(
                page, pageSize, styleIds, serviceTypeId, 
                minPrice, maxPrice, minRating, gender,
                universityId, keyword, sortBy, sortOrder,
                longitude, latitude);
        
        return Result.success(PageResult.of(result.getRecords(), 
                result.getCurrent(), result.getSize(), result.getTotal()));
    }
    
    @Operation(summary = "获取摄影师详情")
    @GetMapping("/{id}")
    public Result<Map<String, Object>> getPhotographerDetail(@PathVariable Long id) {
        Long currentUserId = UserContext.getUserId();
        Map<String, Object> detail = photographerService.getPhotographerDetail(id, currentUserId);
        return Result.success(detail);
    }
    
    @Operation(summary = "申请成为摄影师")
    @PostMapping("/apply")
    public Result<Void> applyPhotographer(@RequestBody Photographer photographer) {
        Long userId = UserContext.getUserId();
        photographerService.applyPhotographer(userId, photographer);
        return Result.success();
    }
    
    @Operation(summary = "更新摄影师信息")
    @PutMapping("/profile")
    public Result<Void> updateProfile(@RequestBody Photographer photographer) {
        Long userId = UserContext.getUserId();
        photographerService.updatePhotographerProfile(userId, photographer);
        return Result.success();
    }
    
    @Operation(summary = "获取我的摄影师信息")
    @GetMapping("/mine")
    public Result<Photographer> getMyPhotographerInfo() {
        Long userId = UserContext.getUserId();
        Photographer photographer = photographerService.getByUserId(userId);
        return Result.success(photographer);
    }
}
