package com.petfuneral.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 宠物档案
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pet")
public class Pet extends BaseEntity {

    /**
     * 客户ID
     */
    private Long customerId;

    /**
     * 宠物名字
     */
    private String name;

    /**
     * 物种 dog/cat/rabbit/bird/other
     */
    private String species;

    /**
     * 品种
     */
    private String breed;

    /**
     * 性别 1-公 2-母
     */
    private Integer gender;

    /**
     * 生日
     */
    private LocalDate birthday;

    /**
     * 体重(kg)
     */
    private java.math.BigDecimal weight;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 生平简介
     */
    private String bio;

    /**
     * 离世日期
     */
    private LocalDate deathDate;

    /**
     * 离世原因
     */
    private String deathReason;

    /**
     * 状态 1-在世 2-已离世
     */
    private Integer status;
}
