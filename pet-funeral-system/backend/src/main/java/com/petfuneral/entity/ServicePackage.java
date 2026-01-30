package com.petfuneral.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 服务套餐
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("service_package")
public class ServicePackage extends BaseEntity {

    /**
     * 套餐名称
     */
    private String name;

    /**
     * 套餐描述
     */
    private String description;

    /**
     * 基础价格
     */
    private BigDecimal basePrice;

    /**
     * 适用体重范围
     */
    private String weightRange;

    /**
     * 包含的服务项 (JSON)
     */
    private String includes;

    /**
     * 套餐图标
     */
    private String icon;

    /**
     * 排序
     */
    private Integer sortOrder;

    /**
     * 状态 1-上架 0-下架
     */
    private Integer status;
}
