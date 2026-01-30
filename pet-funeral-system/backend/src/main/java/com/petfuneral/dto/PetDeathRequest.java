package com.petfuneral.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/**
 * 宠物离世登记请求
 */
@Data
public class PetDeathRequest {

    @NotNull(message = "离世日期不能为空")
    private LocalDate deathDate;

    /**
     * 离世原因
     */
    private String deathReason;
}
