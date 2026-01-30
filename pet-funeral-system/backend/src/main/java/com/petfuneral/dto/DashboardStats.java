package com.petfuneral.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 仪表盘统计数据
 */
@Data
@Builder
public class DashboardStats {
    /**
     * 今日订单数
     */
    private Long todayOrders;

    /**
     * 今日收入
     */
    private BigDecimal todayRevenue;

    /**
     * 本月订单数
     */
    private Long monthOrders;

    /**
     * 本月收入
     */
    private BigDecimal monthRevenue;

    /**
     * 客户总数
     */
    private Long totalCustomers;

    /**
     * 宠物总数
     */
    private Long totalPets;

    /**
     * 纪念馆总数
     */
    private Long totalMemorials;

    /**
     * 待处理订单数
     */
    private Long pendingOrders;

    /**
     * 今日仪式数
     */
    private Long todayCeremonies;

    /**
     * 即将到期寄存数
     */
    private Long expiringStorages;
}
