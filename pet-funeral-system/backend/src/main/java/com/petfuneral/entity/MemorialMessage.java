package com.petfuneral.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 纪念馆留言
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("memorial_message")
public class MemorialMessage extends BaseEntity {

    /**
     * 纪念馆ID
     */
    private Long memorialId;

    /**
     * 留言用户ID (可为空，支持匿名)
     */
    private Long userId;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 留言内容
     */
    private String content;

    /**
     * 点烛数
     */
    private Integer candleCount;

    /**
     * 状态 1-显示 0-隐藏
     */
    private Integer status;
}
