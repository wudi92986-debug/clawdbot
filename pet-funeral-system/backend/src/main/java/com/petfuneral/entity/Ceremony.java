package com.petfuneral.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 告别仪式
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ceremony")
public class Ceremony extends BaseEntity {

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 宠物ID
     */
    private Long petId;

    /**
     * 客户ID
     */
    private Long customerId;

    /**
     * 仪式类型 1-简约告别 2-温馨告别 3-尊享告别
     */
    private Integer ceremonyType;

    /**
     * 仪式厅编号
     */
    private String hallNo;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 参加人数
     */
    private Integer attendeeCount;

    /**
     * 主持人
     */
    private String host;

    /**
     * 负责员工ID
     */
    private Long staffId;

    /**
     * 特殊要求
     */
    private String specialRequests;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态 0-待安排 1-已安排 2-进行中 3-已完成 4-已取消
     */
    private Integer status;
}
