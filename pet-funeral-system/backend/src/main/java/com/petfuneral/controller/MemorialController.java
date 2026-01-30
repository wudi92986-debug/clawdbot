package com.petfuneral.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.petfuneral.common.response.PageResult;
import com.petfuneral.common.response.Result;
import com.petfuneral.dto.MemorialCreateRequest;
import com.petfuneral.entity.Memorial;
import com.petfuneral.entity.MemorialAlbum;
import com.petfuneral.entity.MemorialMessage;
import com.petfuneral.security.UserPrincipal;
import com.petfuneral.service.MemorialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 纪念馆控制器
 */
@Tag(name = "纪念馆管理")
@RestController
@RequestMapping("/api/v1/memorials")
@RequiredArgsConstructor
public class MemorialController {

    private final MemorialService memorialService;

    @Operation(summary = "分页查询纪念馆")
    @GetMapping
    public Result<PageResult<Memorial>> getMemorialPage(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer status) {
        IPage<Memorial> result = memorialService.getMemorialPage(page, size, status);
        return Result.success(PageResult.of(result));
    }

    @Operation(summary = "获取纪念馆详情")
    @GetMapping("/{id}")
    public Result<Memorial> getMemorialDetail(@PathVariable Long id) {
        return Result.success(memorialService.getMemorialDetail(id));
    }

    @Operation(summary = "创建纪念馆")
    @PostMapping
    public Result<Memorial> createMemorial(
            @AuthenticationPrincipal UserPrincipal user,
            @Valid @RequestBody MemorialCreateRequest request) {
        Memorial memorial = memorialService.createMemorial(user.getUserId(), request);
        return Result.success("纪念馆创建成功", memorial);
    }

    @Operation(summary = "点烛")
    @PostMapping("/{id}/candle")
    public Result<Void> lightCandle(@PathVariable Long id) {
        memorialService.lightCandle(id);
        return Result.ok("已点亮蜡烛");
    }

    @Operation(summary = "献花")
    @PostMapping("/{id}/flower")
    public Result<Void> offerFlower(@PathVariable Long id) {
        memorialService.offerFlower(id);
        return Result.ok("已献上鲜花");
    }

    @Operation(summary = "添加留言")
    @PostMapping("/{id}/messages")
    public Result<MemorialMessage> addMessage(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal user,
            @RequestParam String nickname,
            @RequestParam String content) {
        Long userId = user != null ? user.getUserId() : null;
        MemorialMessage message = memorialService.addMessage(id, userId, nickname, content);
        return Result.success("留言成功", message);
    }

    @Operation(summary = "获取留言列表")
    @GetMapping("/{id}/messages")
    public Result<List<MemorialMessage>> getMessages(
            @PathVariable Long id,
            @RequestParam(required = false) Integer limit) {
        return Result.success(memorialService.getMessages(id, limit));
    }

    @Operation(summary = "获取相册")
    @GetMapping("/{id}/albums")
    public Result<List<MemorialAlbum>> getAlbums(@PathVariable Long id) {
        return Result.success(memorialService.getAlbums(id));
    }
}
