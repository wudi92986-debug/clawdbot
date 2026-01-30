package com.petfuneral.controller;

import com.petfuneral.common.response.Result;
import com.petfuneral.dto.*;
import com.petfuneral.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 数据统计控制器
 */
@Tag(name = "数据统计")
@RestController
@RequestMapping("/api/v1/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @Operation(summary = "获取仪表盘统计")
    @GetMapping("/dashboard")
    public Result<DashboardStats> getDashboardStats() {
        return Result.success(statisticsService.getDashboardStats());
    }

    @Operation(summary = "获取日收入趋势")
    @GetMapping("/revenue/daily")
    public Result<List<RevenueStats>> getDailyRevenueTrend(
            @RequestParam(required = false, defaultValue = "30") Integer days) {
        return Result.success(statisticsService.getDailyRevenueTrend(days));
    }

    @Operation(summary = "获取月收入趋势")
    @GetMapping("/revenue/monthly")
    public Result<List<RevenueStats>> getMonthlyRevenueTrend(
            @RequestParam(required = false, defaultValue = "12") Integer months) {
        return Result.success(statisticsService.getMonthlyRevenueTrend(months));
    }

    @Operation(summary = "套餐销售统计")
    @GetMapping("/packages")
    public Result<List<PackageStats>> getPackageSalesStats(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return Result.success(statisticsService.getPackageSalesStats(startDate, endDate));
    }

    @Operation(summary = "宠物物种统计")
    @GetMapping("/pets/species")
    public Result<List<PetSpeciesStats>> getPetSpeciesStats() {
        return Result.success(statisticsService.getPetSpeciesStats());
    }

    @Operation(summary = "订单状态统计")
    @GetMapping("/orders/status")
    public Result<Map<String, Long>> getOrderStatusStats() {
        return Result.success(statisticsService.getOrderStatusStats());
    }

    @Operation(summary = "纪念馆互动统计")
    @GetMapping("/memorials/interaction")
    public Result<Map<String, Object>> getMemorialInteractionStats() {
        return Result.success(statisticsService.getMemorialInteractionStats());
    }
}
