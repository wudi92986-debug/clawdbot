package com.petfuneral.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 服务订单
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("service_order")
public class ServiceOrder extends BaseEntity {

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 客户ID
     */
    private Long customerId;

    /**
     * 宠物ID
     */
    private Long petId;

    /**
     * 套餐ID
     */
    private Long packageId;

    /**
     * 订单总金额
     */
    private BigDecimal totalAmount;

    /**
     * 已支付金额
     */
    private BigDecimal paidAmount;

    /**
     * 订单状态 0-待确认 1-已确认 2-待接运 3-服务中 4-已完成 5-已取消
     */
    private Integer status;

    /**
     * 支付状态 0-未支付 1-部分支付 2-已支付
     */
    private Integer payStatus;

    /**
     * 支付方式 1-微信 2-支付宝 3-银行卡 4-现金
     */
    private Integer payMethod;

    /**
     * 支付时间
     */
    private LocalDateTime payTime;

    /**
     * 预约时间
     */
    private LocalDateTime appointmentTime;

    /**
     * 接运地址
     */
    private String pickupAddress;

    /**
     * 联系人
     */
    private String contactName;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 备注
     */
    private String remark;

    /**
     * 处理员工ID
     */
    private Long staffId;

    /**
     * 确认时间
     */
    private LocalDateTime confirmedAt;

    /**
     * 完成时间
     */
    private LocalDateTime completedAt;

    /**
     * 取消原因
     */
    private String cancelReason;
}
