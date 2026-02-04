package com.zhanjiang.photography.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhanjiang.photography.common.result.Result;
import com.zhanjiang.photography.entity.ServiceType;
import com.zhanjiang.photography.entity.StyleTag;
import com.zhanjiang.photography.entity.University;
import com.zhanjiang.photography.mapper.StyleTagMapper;
import com.zhanjiang.photography.mapper.UniversityMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 基础数据控制器
 */
@Tag(name = "基础数据模块")
@RestController
@RequestMapping("/base")
@RequiredArgsConstructor
public class BaseController {
    
    private final UniversityMapper universityMapper;
    private final StyleTagMapper styleTagMapper;
    
    @Operation(summary = "获取高校列表")
    @GetMapping("/universities")
    public Result<List<University>> getUniversities() {
        List<University> list = universityMapper.selectList(
                new LambdaQueryWrapper<University>()
                        .eq(University::getStatus, 1)
                        .orderByAsc(University::getSortOrder));
        return Result.success(list);
    }
    
    @Operation(summary = "获取风格标签")
    @GetMapping("/style-tags")
    public Result<List<StyleTag>> getStyleTags(
            @Parameter(description = "类型：1-拍摄风格，2-场景标签") 
            @RequestParam(required = false) Integer type) {
        LambdaQueryWrapper<StyleTag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StyleTag::getStatus, 1);
        if (type != null) {
            wrapper.eq(StyleTag::getTagType, type);
        }
        wrapper.orderByAsc(StyleTag::getSortOrder);
        List<StyleTag> list = styleTagMapper.selectList(wrapper);
        return Result.success(list);
    }
}
