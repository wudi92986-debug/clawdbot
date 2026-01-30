package com.petfuneral.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单列表 DTO
 */
@Data
public class OrderListDTO {
    private Long id;
    private String orderNo;
    private Long customerId;
    private String customerName;
    private String customerPhone;
    private Long petId;
    private String petName;
    private String petBreed;
    private String packageName;
    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
    private Integer status;
    private Integer payStatus;
    private LocalDateTime appointmentTime;
    private LocalDateTime createdAt;
}
