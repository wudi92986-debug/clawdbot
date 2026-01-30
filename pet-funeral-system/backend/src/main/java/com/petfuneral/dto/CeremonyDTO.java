package com.petfuneral.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 仪式 DTO
 */
@Data
public class CeremonyDTO {
    private Long id;
    private Long orderId;
    private String orderNo;
    private Long petId;
    private String petName;
    private String petBreed;
    private Long customerId;
    private String customerName;
    private String customerPhone;
    private Integer ceremonyType;
    private String ceremonyTypeName;
    private String hallNo;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer attendeeCount;
    private String host;
    private Long staffId;
    private String staffName;
    private String specialRequests;
    private String remark;
    private Integer status;
    private String statusName;
    private LocalDateTime createdAt;
}
