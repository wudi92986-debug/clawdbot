package com.petfuneral.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 客户
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("customer")
public class Customer extends BaseEntity {

    /**
     * 客户姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 地址
     */
    private String address;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 微信 OpenID
     */
    private String openid;

    /**
     * 会员等级 0-普通 1-银卡 2-金卡 3-钻石
     */
    private Integer memberLevel;

    /**
     * 积分
     */
    private Integer points;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态 1-正常 0-禁用
     */
    private Integer status;
}
