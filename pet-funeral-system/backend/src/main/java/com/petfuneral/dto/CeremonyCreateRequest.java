package com.petfuneral.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 创建仪式请求
 */
@Data
public class CeremonyCreateRequest {

    @NotNull(message = "订单ID不能为空")
    private Long orderId;

    @NotNull(message = "宠物ID不能为空")
    private Long petId;

    @NotNull(message = "客户ID不能为空")
    private Long customerId;

    /**
     * 仪式类型 1-简约告别 2-温馨告别 3-尊享告别
     */
    private Integer ceremonyType = 1;

    /**
     * 仪式厅编号
     */
    private String hallNo;

    @NotNull(message = "开始时间不能为空")
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
}
