package com.petfuneral.security;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户主体信息
 */
@Data
@AllArgsConstructor
public class UserPrincipal implements Serializable {
    private Long userId;
    private String username;
}
