package com.petfuneral.controller;

import com.petfuneral.common.response.Result;
import com.petfuneral.entity.Memorial;
import com.petfuneral.entity.MemorialMessage;
import com.petfuneral.entity.ServicePackage;
import com.petfuneral.service.MemorialService;
import com.petfuneral.service.PackageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 公开接口控制器 (无需登录)
 */
@Tag(name = "公开接口")
@RestController
@RequestMapping("/api/v1/public")
@RequiredArgsConstructor
public class PublicController {

    private final PackageService packageService;
    private final MemorialService memorialService;

    @Operation(summary = "获取上架套餐列表")
    @GetMapping("/packages")
    public Result<List<ServicePackage>> getPackages() {
        return Result.success(packageService.getActivePackages());
    }

    @Operation(summary = "访问纪念馆")
    @GetMapping("/memorial/{urlKey}")
    public Result<Memorial> visitMemorial(@PathVariable String urlKey) {
        return Result.success(memorialService.getMemorialByUrlKey(urlKey));
    }

    @Operation(summary = "获取纪念馆留言")
    @GetMapping("/memorial/{urlKey}/messages")
    public Result<List<MemorialMessage>> getMemorialMessages(
            @PathVariable String urlKey,
            @RequestParam(required = false, defaultValue = "20") Integer limit) {
        Memorial memorial = memorialService.getMemorialByUrlKey(urlKey);
        return Result.success(memorialService.getMessages(memorial.getId(), limit));
    }

    @Operation(summary = "纪念馆点烛 (公开)")
    @PostMapping("/memorial/{urlKey}/candle")
    public Result<Void> lightCandle(@PathVariable String urlKey) {
        Memorial memorial = memorialService.getMemorialByUrlKey(urlKey);
        memorialService.lightCandle(memorial.getId());
        return Result.ok("已点亮蜡烛");
    }

    @Operation(summary = "纪念馆献花 (公开)")
    @PostMapping("/memorial/{urlKey}/flower")
    public Result<Void> offerFlower(@PathVariable String urlKey) {
        Memorial memorial = memorialService.getMemorialByUrlKey(urlKey);
        memorialService.offerFlower(memorial.getId());
        return Result.ok("已献上鲜花");
    }
}
