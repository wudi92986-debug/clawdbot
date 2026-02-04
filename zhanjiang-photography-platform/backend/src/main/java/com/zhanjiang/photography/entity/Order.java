package com.zhanjiang.photography.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 订单实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_order")
public class Order extends BaseEntity {
    
    /**
     * 订单编号
     */
    private String orderNo;
    
    /**
     * 客户用户ID
     */
    private Long userId;
    
    /**
     * 摄影师ID
     */
    private Long photographerId;
    
    /**
     * 关联需求ID
     */
    private Long demandId;
    
    /**
     * 套餐ID
     */
    private Long packageId;
    
    /**
     * 服务类型ID
     */
    private Long serviceTypeId;
    
    /**
     * 订单标题
     */
    private String orderTitle;
    
    /**
     * 拍摄日期
     */
    private LocalDate shootDate;
    
    /**
     * 拍摄时段
     */
    private String shootTimeSlot;
    
    /**
     * 预计时长（分钟）
     */
    private Integer duration;
    
    /**
     * 拍摄地点
     */
    private String location;
    
    /**
     * 经度
     */
    private BigDecimal longitude;
    
    /**
     * 纬度
     */
    private BigDecimal latitude;
    
    /**
     * 原片数量
     */
    private Integer originalCount;
    
    /**
     * 精修数量
     */
    private Integer refinedCount;
    
    /**
     * 订单总金额
     */
    private BigDecimal totalAmount;
    
    /**
     * 定金金额
     */
    private BigDecimal depositAmount;
    
    /**
     * 尾款金额
     */
    private BigDecimal finalAmount;
    
    /**
     * 实付金额
     */
    private BigDecimal actualAmount;
    
    /**
     * 优惠金额
     */
    private BigDecimal discountAmount;
    
    /**
     * 优惠券ID
     */
    private Long couponId;
    
    /**
     * 平台服务费
     */
    private BigDecimal platformFee;
    
    /**
     * 摄影师收入
     */
    private BigDecimal photographerIncome;
    
    /**
     * 订单状态
     */
    private Integer status;
    
    /**
     * 支付状态：0-未支付，1-已付定金，2-已付全款
     */
    private Integer payStatus;
    
    /**
     * 订单备注
     */
    private String remark;
    
    /**
     * 取消原因
     */
    private String cancelReason;
    
    /**
     * 取消时间
     */
    private LocalDateTime cancelTime;
    
    /**
     * 取消方：1-客户，2-摄影师，3-系统
     */
    private Integer cancelBy;
    
    /**
     * 接单时间
     */
    private LocalDateTime acceptTime;
    
    /**
     * 实际拍摄时间
     */
    private LocalDateTime shootTime;
    
    /**
     * 完成时间
     */
    private LocalDateTime completeTime;
    
    /**
     * 交付截止时间
     */
    private LocalDateTime deliveryDeadline;
}
