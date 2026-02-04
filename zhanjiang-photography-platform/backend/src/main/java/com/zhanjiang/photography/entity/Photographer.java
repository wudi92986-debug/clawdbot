package com.zhanjiang.photography.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

/**
 * 摄影师实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_photographer")
public class Photographer extends BaseEntity {
    
    /**
     * 关联用户ID
     */
    private Long userId;
    
    /**
     * 展示名称/艺名
     */
    private String displayName;
    
    /**
     * 个人简介
     */
    private String bio;
    
    /**
     * 主页封面图
     */
    private String coverImage;
    
    /**
     * 从业年限
     */
    private Integer experienceYears;
    
    /**
     * 服务区域
     */
    private String serviceArea;
    
    /**
     * 微信号（加密存储）
     */
    private String wechat;
    
    /**
     * 设备信息（JSON格式）
     */
    private String equipment;
    
    /**
     * 平均评分
     */
    private BigDecimal avgRating;
    
    /**
     * 总订单数
     */
    private Integer totalOrders;
    
    /**
     * 完成订单数
     */
    private Integer completedOrders;
    
    /**
     * 粉丝数
     */
    private Integer totalFans;
    
    /**
     * 主页访问量
     */
    private Integer totalViews;
    
    /**
     * 状态：0-待审核，1-正常，2-休息中，3-封禁
     */
    private Integer status;
    
    /**
     * 审核状态：0-待审核，1-通过，2-拒绝
     */
    private Integer auditStatus;
    
    /**
     * 拒绝原因
     */
    private String rejectReason;
    
    /**
     * 起步价
     */
    private BigDecimal minPrice;
    
    /**
     * 定金比例（%）
     */
    private Integer depositRatio;
    
    /**
     * 是否自动接单
     */
    private Integer autoAccept;
}
