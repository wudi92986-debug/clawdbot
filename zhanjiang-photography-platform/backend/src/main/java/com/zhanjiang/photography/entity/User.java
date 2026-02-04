package com.zhanjiang.photography.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * 用户实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_user")
public class User extends BaseEntity {
    
    /**
     * 微信openid
     */
    private String openid;
    
    /**
     * 微信union_id
     */
    private String unionId;
    
    /**
     * 昵称
     */
    private String nickname;
    
    /**
     * 头像URL
     */
    private String avatar;
    
    /**
     * 性别：0-未知，1-男，2-女
     */
    private Integer gender;
    
    /**
     * 手机号
     */
    private String phone;
    
    /**
     * 真实姓名
     */
    private String realName;
    
    /**
     * 身份证号（加密存储）
     */
    private String idCard;
    
    /**
     * 用户类型：1-普通用户，2-摄影师，3-管理员
     */
    private Integer userType;
    
    /**
     * 状态：0-禁用，1-正常，2-禁言
     */
    private Integer status;
    
    /**
     * 是否实名认证
     */
    private Integer isVerified;
    
    /**
     * 是否学生认证
     */
    private Integer isStudentVerified;
    
    /**
     * 所属高校ID
     */
    private Long universityId;
    
    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;
    
    /**
     * 最后登录IP
     */
    private String lastLoginIp;
}
