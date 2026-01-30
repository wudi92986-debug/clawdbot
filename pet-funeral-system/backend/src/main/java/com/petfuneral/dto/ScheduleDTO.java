package com.petfuneral.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 日程 DTO (用于日历展示)
 */
@Data
public class ScheduleDTO {
    private Long id;
    private String type; // ceremony-仪式 cremation-火化 pickup-接运
    private String title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String hallNo;
    private String petName;
    private String customerName;
    private Integer status;
    private String color; // 用于日历显示
}
