package com.petfuneral.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.petfuneral.common.response.PageResult;
import com.petfuneral.common.response.Result;
import com.petfuneral.dto.CeremonyCreateRequest;
import com.petfuneral.dto.CeremonyDTO;
import com.petfuneral.dto.ScheduleDTO;
import com.petfuneral.entity.Ceremony;
import com.petfuneral.service.CeremonyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 仪式管理控制器
 */
@Tag(name = "仪式管理")
@RestController
@RequestMapping("/api/v1/ceremonies")
@RequiredArgsConstructor
public class CeremonyController {

    private final CeremonyService ceremonyService;

    @Operation(summary = "分页查询仪式")
    @GetMapping
    public Result<PageResult<CeremonyDTO>> getCeremonyPage(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        IPage<CeremonyDTO> result = ceremonyService.getCeremonyPage(page, size, status, date);
        return Result.success(PageResult.of(result));
    }

    @Operation(summary = "获取仪式详情")
    @GetMapping("/{id}")
    public Result<CeremonyDTO> getCeremonyDetail(@PathVariable Long id) {
        return Result.success(ceremonyService.getCeremonyDetail(id));
    }

    @Operation(summary = "创建仪式")
    @PostMapping
    public Result<Ceremony> createCeremony(@Valid @RequestBody CeremonyCreateRequest request) {
        Ceremony ceremony = ceremonyService.createCeremony(request);
        return Result.success("创建成功", ceremony);
    }

    @Operation(summary = "更新仪式")
    @PutMapping("/{id}")
    public Result<Void> updateCeremony(@PathVariable Long id, @Valid @RequestBody CeremonyCreateRequest request) {
        ceremonyService.updateCeremony(id, request);
        return Result.ok("更新成功");
    }

    @Operation(summary = "开始仪式")
    @PostMapping("/{id}/start")
    public Result<Void> startCeremony(@PathVariable Long id) {
        ceremonyService.startCeremony(id);
        return Result.ok("仪式已开始");
    }

    @Operation(summary = "完成仪式")
    @PostMapping("/{id}/complete")
    public Result<Void> completeCeremony(@PathVariable Long id) {
        ceremonyService.completeCeremony(id);
        return Result.ok("仪式已完成");
    }

    @Operation(summary = "取消仪式")
    @PostMapping("/{id}/cancel")
    public Result<Void> cancelCeremony(
            @PathVariable Long id,
            @RequestParam(required = false) String reason) {
        ceremonyService.cancelCeremony(id, reason);
        return Result.ok("仪式已取消");
    }

    @Operation(summary = "获取某日日程")
    @GetMapping("/schedule/day")
    public Result<List<ScheduleDTO>> getDaySchedule(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return Result.success(ceremonyService.getDaySchedule(date));
    }

    @Operation(summary = "获取某周日程")
    @GetMapping("/schedule/week")
    public Result<List<ScheduleDTO>> getWeekSchedule(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {
        return Result.success(ceremonyService.getWeekSchedule(startDate));
    }

    @Operation(summary = "今日待办仪式")
    @GetMapping("/today")
    public Result<List<CeremonyDTO>> getTodayCeremonies() {
        return Result.success(ceremonyService.getTodayCeremonies());
    }

    @Operation(summary = "统计仪式数量")
    @GetMapping("/count")
    public Result<Long> countCeremonies(@RequestParam(required = false) Integer status) {
        return Result.success(ceremonyService.countByStatus(status));
    }
}
