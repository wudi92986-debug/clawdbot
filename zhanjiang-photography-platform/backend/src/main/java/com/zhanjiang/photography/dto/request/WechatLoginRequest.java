package com.zhanjiang.photography.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 微信登录请求
 */
@Data
public class WechatLoginRequest {
    
    @NotBlank(message = "code不能为空")
    private String code;
    
    private String encryptedData;
    
    private String iv;
}
