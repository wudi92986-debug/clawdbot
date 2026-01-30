package com.petfuneral.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 创建纪念馆请求
 */
@Data
public class MemorialCreateRequest {

    @NotNull(message = "宠物ID不能为空")
    private Long petId;

    private Integer templateId = 1;

    private String backgroundMusic;

    private Integer privacy = 1;
}
