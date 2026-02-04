package com.zhanjiang.photography.dto.response;

import lombok.Data;

/**
 * 登录响应
 */
@Data
public class LoginResponse {
    
    private String accessToken;
    private String refreshToken;
    private Long expiresIn;
    private UserInfoResponse userInfo;
    
    @Data
    public static class UserInfoResponse {
        private Long userId;
        private String nickname;
        private String avatar;
        private Integer userType;
        private Boolean isVerified;
        private Boolean isStudentVerified;
        private Boolean isNewUser;
    }
}
