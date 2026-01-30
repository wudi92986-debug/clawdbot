package com.petfuneral.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 纪念馆
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("memorial")
public class Memorial extends BaseEntity {

    /**
     * 宠物ID
     */
    private Long petId;

    /**
     * 客户ID
     */
    private Long customerId;

    /**
     * 纪念馆URL标识
     */
    private String urlKey;

    /**
     * 主题模板ID
     */
    private Integer templateId;

    /**
     * 背景音乐
     */
    private String backgroundMusic;

    /**
     * 访问次数
     */
    private Integer visitCount;

    /**
     * 点烛次数
     */
    private Integer candleCount;

    /**
     * 献花次数
     */
    private Integer flowerCount;

    /**
     * 隐私设置 1-公开 2-好友可见 3-私密
     */
    private Integer privacy;

    /**
     * 二维码图片
     */
    private String qrCode;

    /**
     * 状态 1-正常 0-关闭
     */
    private Integer status;
}
