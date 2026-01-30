package com.petfuneral.controller;

import com.petfuneral.common.response.Result;
import com.petfuneral.dto.UploadResult;
import com.petfuneral.entity.UploadFile;
import com.petfuneral.security.UserPrincipal;
import com.petfuneral.service.FileUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文件上传控制器
 */
@Tag(name = "文件上传")
@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileUploadController {

    private final FileUploadService fileUploadService;

    @Operation(summary = "上传单个文件")
    @PostMapping("/upload")
    public Result<UploadResult> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(defaultValue = "common") String module,
            @RequestParam(required = false) Long bizId,
            @AuthenticationPrincipal UserPrincipal user) {
        Long userId = user != null ? user.getUserId() : null;
        UploadResult result = fileUploadService.uploadFile(file, module, bizId, userId);
        return Result.success("上传成功", result);
    }

    @Operation(summary = "批量上传文件")
    @PostMapping("/upload/batch")
    public Result<List<UploadResult>> uploadFiles(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam(defaultValue = "common") String module,
            @RequestParam(required = false) Long bizId,
            @AuthenticationPrincipal UserPrincipal user) {
        Long userId = user != null ? user.getUserId() : null;
        List<UploadResult> results = fileUploadService.uploadFiles(files, module, bizId, userId);
        return Result.success("上传成功", results);
    }

    @Operation(summary = "上传宠物头像")
    @PostMapping("/upload/pet-avatar")
    public Result<UploadResult> uploadPetAvatar(
            @RequestParam("file") MultipartFile file,
            @RequestParam Long petId,
            @AuthenticationPrincipal UserPrincipal user) {
        Long userId = user != null ? user.getUserId() : null;
        UploadResult result = fileUploadService.uploadFile(file, "pet-avatar", petId, userId);
        return Result.success("上传成功", result);
    }

    @Operation(summary = "上传纪念馆照片")
    @PostMapping("/upload/memorial")
    public Result<UploadResult> uploadMemorialPhoto(
            @RequestParam("file") MultipartFile file,
            @RequestParam Long memorialId,
            @AuthenticationPrincipal UserPrincipal user) {
        Long userId = user != null ? user.getUserId() : null;
        UploadResult result = fileUploadService.uploadFile(file, "memorial", memorialId, userId);
        return Result.success("上传成功", result);
    }

    @Operation(summary = "删除文件")
    @DeleteMapping("/{fileId}")
    public Result<Void> deleteFile(@PathVariable Long fileId) {
        fileUploadService.deleteFile(fileId);
        return Result.success("删除成功");
    }

    @Operation(summary = "获取业务相关的文件")
    @GetMapping("/biz")
    public Result<List<UploadFile>> getFilesByBiz(
            @RequestParam String module,
            @RequestParam Long bizId) {
        return Result.success(fileUploadService.getFilesByBiz(module, bizId));
    }
}
