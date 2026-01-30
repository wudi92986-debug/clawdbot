package com.petfuneral.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 骨灰寄存 DTO (包含关联信息)
 */
@Data
public class AshStorageDTO {
    private Long id;
    private Long orderId;
    private String orderNo;
    private Long petId;
    private String petName;
    private String petBreed;
    private Long customerId;
    private String customerName;
    private String customerPhone;
    private String cabinetNo;
    private String location;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer status;
    private Long daysRemaining; // 剩余天数
    private LocalDateTime createdAt;
}
