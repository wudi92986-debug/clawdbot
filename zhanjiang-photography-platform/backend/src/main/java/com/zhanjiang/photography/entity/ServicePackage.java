package com.zhanjiang.photography.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

/**
 * 服务套餐实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_service_package")
public class ServicePackage extends BaseEntity {
    
    /**
     * 摄影师ID
     */
    private Long photographerId;
    
    /**
     * 套餐名称
     */
    private String packageName;
    
    /**
     * 服务类型ID
     */
    private Long serviceTypeId;
    
    /**
     * 套餐描述
     */
    private String description;
    
    /**
     * 服务时长（分钟）
     */
    private Integer duration;
    
    /**
     * 原片数量
     */
    private Integer originalCount;
    
    /**
     * 精修数量
     */
    private Integer refinedCount;
    
    /**
     * 交付天数
     */
    private Integer deliveryDays;
    
    /**
     * 套餐价格
     */
    private BigDecimal price;
    
    /**
     * 原价
     */
    private BigDecimal originalPrice;
    
    /**
     * 定金比例（%）
     */
    private Integer depositRatio;
    
    /**
     * 排序
     */
    private Integer sortOrder;
    
    /**
     * 状态：0-下架，1-上架
     */
    private Integer status;
}
