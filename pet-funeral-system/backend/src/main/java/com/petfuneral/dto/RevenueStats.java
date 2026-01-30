package com.petfuneral.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 收入统计
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RevenueStats {
    /**
     * 日期/月份标签
     */
    private String label;

    /**
     * 订单数
     */
    private Long orderCount;

    /**
     * 收入金额
     */
    private BigDecimal revenue;
}
