package com.petfuneral.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 骨灰寄存
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ash_storage")
public class AshStorage extends BaseEntity {

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 宠物ID
     */
    private Long petId;

    /**
     * 客户ID
     */
    private Long customerId;

    /**
     * 柜位编号
     */
    private String cabinetNo;

    /**
     * 存放位置描述
     */
    private String location;

    /**
     * 开始日期
     */
    private LocalDate startDate;

    /**
     * 到期日期
     */
    private LocalDate endDate;

    /**
     * 状态 1-寄存中 2-已取走 3-已到期
     */
    private Integer status;
}
