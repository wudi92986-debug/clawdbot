package com.petfuneral.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 创建宠物请求
 */
@Data
public class PetCreateRequest {

    @NotNull(message = "客户ID不能为空")
    private Long customerId;

    @NotBlank(message = "宠物名字不能为空")
    private String name;

    /**
     * 物种 dog/cat/rabbit/bird/other
     */
    private String species;

    /**
     * 品种
     */
    private String breed;

    /**
     * 性别 1-公 2-母
     */
    private Integer gender;

    /**
     * 生日
     */
    private LocalDate birthday;

    /**
     * 体重(kg)
     */
    private BigDecimal weight;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 生平简介
     */
    private String bio;
}
