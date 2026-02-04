package com.zhanjiang.photography.common.result;

import lombok.Getter;

/**
 * 响应状态码枚举
 */
@Getter
public enum ResultCode {
    
    // 成功
    SUCCESS(200, "success"),
    
    // 客户端错误 4xxxx
    FAIL(400, "请求失败"),
    PARAM_ERROR(40001, "参数校验失败"),
    DATA_NOT_FOUND(40002, "数据不存在"),
    DATA_EXIST(40003, "数据已存在"),
    
    // 认证授权错误
    UNAUTHORIZED(401, "未登录或Token已过期"),
    TOKEN_INVALID(40101, "Token无效"),
    TOKEN_EXPIRED(40102, "Token已过期"),
    
    // 权限错误
    FORBIDDEN(403, "无权限访问"),
    NO_PERMISSION(40301, "无操作权限"),
    ACCOUNT_DISABLED(40302, "账号已被禁用"),
    
    // 资源不存在
    NOT_FOUND(404, "资源不存在"),
    
    // 服务器错误 5xxxx
    SERVER_ERROR(500, "服务器内部错误"),
    SERVICE_UNAVAILABLE(503, "服务暂不可用"),
    
    // 业务错误 6xxxx
    USER_NOT_EXIST(60001, "用户不存在"),
    USER_DISABLED(60002, "用户已被禁用"),
    PHOTOGRAPHER_NOT_EXIST(60101, "摄影师不存在"),
    PHOTOGRAPHER_NOT_AVAILABLE(60102, "摄影师暂不接单"),
    SCHEDULE_NOT_AVAILABLE(60103, "该时段已被预约"),
    ORDER_NOT_EXIST(60201, "订单不存在"),
    ORDER_STATUS_ERROR(60202, "订单状态异常"),
    ORDER_CANNOT_CANCEL(60203, "订单无法取消"),
    PAYMENT_FAILED(60301, "支付失败"),
    BALANCE_NOT_ENOUGH(60302, "余额不足"),
    COUPON_NOT_AVAILABLE(60401, "优惠券不可用"),
    COUPON_EXPIRED(60402, "优惠券已过期"),
    WECHAT_LOGIN_FAILED(60501, "微信登录失败"),
    FILE_UPLOAD_FAILED(60601, "文件上传失败");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
