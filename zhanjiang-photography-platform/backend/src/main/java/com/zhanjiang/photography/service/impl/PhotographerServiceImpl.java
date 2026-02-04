package com.zhanjiang.photography.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhanjiang.photography.common.exception.BusinessException;
import com.zhanjiang.photography.common.result.ResultCode;
import com.zhanjiang.photography.entity.Photographer;
import com.zhanjiang.photography.entity.User;
import com.zhanjiang.photography.mapper.PhotographerMapper;
import com.zhanjiang.photography.service.PhotographerService;
import com.zhanjiang.photography.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 摄影师服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PhotographerServiceImpl extends ServiceImpl<PhotographerMapper, Photographer> 
        implements PhotographerService {
    
    private final UserService userService;
    
    @Override
    public Page<Map<String, Object>> listPhotographers(Integer page, Integer pageSize,
            String styleIds, Long serviceTypeId,
            Double minPrice, Double maxPrice,
            Double minRating, Integer gender,
            Long universityId, String keyword,
            String sortBy, String sortOrder,
            Double longitude, Double latitude) {
        
        Page<Photographer> pageParam = new Page<>(page, pageSize);
        
        LambdaQueryWrapper<Photographer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Photographer::getStatus, 1) // 正常状态
               .eq(Photographer::getAuditStatus, 1) // 已审核通过
               .eq(Photographer::getIsDeleted, 0);
        
        // 价格筛选
        if (minPrice != null) {
            wrapper.ge(Photographer::getMinPrice, minPrice);
        }
        if (maxPrice != null) {
            wrapper.le(Photographer::getMinPrice, maxPrice);
        }
        
        // 评分筛选
        if (minRating != null) {
            wrapper.ge(Photographer::getAvgRating, minRating);
        }
        
        // 关键词搜索
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(Photographer::getDisplayName, keyword)
                   .or()
                   .like(Photographer::getBio, keyword);
        }
        
        // 排序
        if ("rating".equals(sortBy)) {
            wrapper.orderByDesc(Photographer::getAvgRating);
        } else if ("price".equals(sortBy)) {
            if ("desc".equals(sortOrder)) {
                wrapper.orderByDesc(Photographer::getMinPrice);
            } else {
                wrapper.orderByAsc(Photographer::getMinPrice);
            }
        } else if ("orders".equals(sortBy)) {
            wrapper.orderByDesc(Photographer::getCompletedOrders);
        } else {
            // 默认按评分和订单数综合排序
            wrapper.orderByDesc(Photographer::getAvgRating)
                   .orderByDesc(Photographer::getCompletedOrders);
        }
        
        Page<Photographer> resultPage = page(pageParam, wrapper);
        
        // 转换为Map格式返回
        Page<Map<String, Object>> mapPage = new Page<>(page, pageSize, resultPage.getTotal());
        mapPage.setRecords(resultPage.getRecords().stream().map(p -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", p.getId());
            map.put("userId", p.getUserId());
            map.put("displayName", p.getDisplayName());
            map.put("bio", p.getBio());
            map.put("coverImage", p.getCoverImage());
            map.put("avgRating", p.getAvgRating());
            map.put("totalOrders", p.getTotalOrders());
            map.put("completedOrders", p.getCompletedOrders());
            map.put("totalFans", p.getTotalFans());
            map.put("minPrice", p.getMinPrice());
            
            // 获取用户信息补充头像
            User user = userService.getById(p.getUserId());
            if (user != null) {
                map.put("avatar", user.getAvatar());
            }
            
            return map;
        }).toList());
        
        return mapPage;
    }
    
    @Override
    public Map<String, Object> getPhotographerDetail(Long id, Long currentUserId) {
        Photographer photographer = getById(id);
        if (photographer == null) {
            throw new BusinessException(ResultCode.PHOTOGRAPHER_NOT_EXIST);
        }
        
        // 增加访问量
        incrementViews(id);
        
        Map<String, Object> detail = new HashMap<>();
        detail.put("id", photographer.getId());
        detail.put("userId", photographer.getUserId());
        detail.put("displayName", photographer.getDisplayName());
        detail.put("bio", photographer.getBio());
        detail.put("coverImage", photographer.getCoverImage());
        detail.put("experienceYears", photographer.getExperienceYears());
        detail.put("serviceArea", photographer.getServiceArea());
        detail.put("avgRating", photographer.getAvgRating());
        detail.put("totalOrders", photographer.getTotalOrders());
        detail.put("completedOrders", photographer.getCompletedOrders());
        detail.put("totalFans", photographer.getTotalFans());
        detail.put("totalViews", photographer.getTotalViews());
        detail.put("minPrice", photographer.getMinPrice());
        detail.put("depositRatio", photographer.getDepositRatio());
        
        // 获取用户信息
        User user = userService.getById(photographer.getUserId());
        if (user != null) {
            detail.put("avatar", user.getAvatar());
            detail.put("isVerified", user.getIsVerified() == 1);
            detail.put("isStudentVerified", user.getIsStudentVerified() == 1);
        }
        
        // TODO: 获取风格标签、是否关注等信息
        
        return detail;
    }
    
    @Override
    public Photographer getByUserId(Long userId) {
        return getOne(new LambdaQueryWrapper<Photographer>()
                .eq(Photographer::getUserId, userId)
                .eq(Photographer::getIsDeleted, 0));
    }
    
    @Override
    @Transactional
    public void applyPhotographer(Long userId, Photographer photographer) {
        // 检查是否已申请
        Photographer existing = getByUserId(userId);
        if (existing != null) {
            throw new BusinessException("您已提交过申请");
        }
        
        photographer.setUserId(userId);
        photographer.setStatus(0); // 待审核
        photographer.setAuditStatus(0);
        photographer.setAvgRating(new BigDecimal("5.0"));
        photographer.setTotalOrders(0);
        photographer.setCompletedOrders(0);
        photographer.setTotalFans(0);
        photographer.setTotalViews(0);
        
        save(photographer);
    }
    
    @Override
    public void updatePhotographerProfile(Long userId, Photographer updateData) {
        Photographer photographer = getByUserId(userId);
        if (photographer == null) {
            throw new BusinessException(ResultCode.PHOTOGRAPHER_NOT_EXIST);
        }
        
        // 更新允许修改的字段
        if (updateData.getDisplayName() != null) {
            photographer.setDisplayName(updateData.getDisplayName());
        }
        if (updateData.getBio() != null) {
            photographer.setBio(updateData.getBio());
        }
        if (updateData.getCoverImage() != null) {
            photographer.setCoverImage(updateData.getCoverImage());
        }
        if (updateData.getExperienceYears() != null) {
            photographer.setExperienceYears(updateData.getExperienceYears());
        }
        if (updateData.getServiceArea() != null) {
            photographer.setServiceArea(updateData.getServiceArea());
        }
        if (updateData.getEquipment() != null) {
            photographer.setEquipment(updateData.getEquipment());
        }
        if (updateData.getDepositRatio() != null) {
            photographer.setDepositRatio(updateData.getDepositRatio());
        }
        if (updateData.getAutoAccept() != null) {
            photographer.setAutoAccept(updateData.getAutoAccept());
        }
        
        updateById(photographer);
    }
    
    @Override
    public void incrementViews(Long photographerId) {
        Photographer photographer = getById(photographerId);
        if (photographer != null) {
            photographer.setTotalViews(photographer.getTotalViews() + 1);
            updateById(photographer);
        }
    }
}
