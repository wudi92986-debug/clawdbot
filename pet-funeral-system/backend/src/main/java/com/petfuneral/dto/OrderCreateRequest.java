package com.petfuneral.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 创建订单请求
 */
@Data
public class OrderCreateRequest {

    @NotNull(message = "套餐ID不能为空")
    private Long packageId;

    /**
     * 宠物ID (已有宠物)
     */
    private Long petId;

    /**
     * 宠物名字 (新建宠物)
     */
    private String petName;

    /**
     * 宠物类型
     */
    private String petType;

    /**
     * 宠物体重
     */
    private String petWeight;

    @NotNull(message = "预约时间不能为空")
    private LocalDateTime appointmentTime;

    @NotBlank(message = "接运地址不能为空")
    private String pickupAddress;

    private String contactName;

    @NotBlank(message = "联系电话不能为空")
    private String contactPhone;

    private String remark;
}
