package com.zhanjiang.photography.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhanjiang.photography.entity.Photographer;

import java.util.Map;

/**
 * 摄影师服务接口
 */
public interface PhotographerService extends IService<Photographer> {
    
    /**
     * 分页查询摄影师列表
     */
    Page<Map<String, Object>> listPhotographers(Integer page, Integer pageSize, 
            String styleIds, Long serviceTypeId, 
            Double minPrice, Double maxPrice, 
            Double minRating, Integer gender,
            Long universityId, String keyword,
            String sortBy, String sortOrder,
            Double longitude, Double latitude);
    
    /**
     * 获取摄影师详情
     */
    Map<String, Object> getPhotographerDetail(Long id, Long currentUserId);
    
    /**
     * 根据用户ID获取摄影师信息
     */
    Photographer getByUserId(Long userId);
    
    /**
     * 申请成为摄影师
     */
    void applyPhotographer(Long userId, Photographer photographer);
    
    /**
     * 更新摄影师信息
     */
    void updatePhotographerProfile(Long userId, Photographer photographer);
    
    /**
     * 增加访问量
     */
    void incrementViews(Long photographerId);
}
