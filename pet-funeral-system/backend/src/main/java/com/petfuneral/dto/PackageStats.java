package com.petfuneral.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 套餐销售统计
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PackageStats {
    /**
     * 套餐ID
     */
    private Long packageId;

    /**
     * 套餐名称
     */
    private String packageName;

    /**
     * 订单数
     */
    private Long orderCount;

    /**
     * 销售金额
     */
    private BigDecimal revenue;

    /**
     * 占比
     */
    private Double percentage;
}
