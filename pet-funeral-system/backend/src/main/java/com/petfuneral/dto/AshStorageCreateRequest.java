package com.petfuneral.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/**
 * 创建骨灰寄存请求
 */
@Data
public class AshStorageCreateRequest {

    /**
     * 订单ID
     */
    private Long orderId;

    @NotNull(message = "宠物ID不能为空")
    private Long petId;

    @NotNull(message = "客户ID不能为空")
    private Long customerId;

    @NotBlank(message = "柜位编号不能为空")
    private String cabinetNo;

    /**
     * 存放位置描述
     */
    private String location;

    @NotNull(message = "开始日期不能为空")
    private LocalDate startDate;

    @NotNull(message = "到期日期不能为空")
    private LocalDate endDate;
}
