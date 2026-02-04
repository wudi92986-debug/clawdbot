package com.zhanjiang.photography.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 作品实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_works")
public class Works extends BaseEntity {
    
    /**
     * 摄影师ID
     */
    private Long photographerId;
    
    /**
     * 作品标题
     */
    private String title;
    
    /**
     * 作品描述
     */
    private String description;
    
    /**
     * 封面图片
     */
    private String coverImage;
    
    /**
     * 分类：1-客片，2-个人创作
     */
    private Integer category;
    
    /**
     * 拍摄日期
     */
    private LocalDate shootDate;
    
    /**
     * 拍摄地点
     */
    private String location;
    
    /**
     * 浏览量
     */
    private Integer viewCount;
    
    /**
     * 点赞数
     */
    private Integer likeCount;
    
    /**
     * 收藏数
     */
    private Integer collectCount;
    
    /**
     * 是否置顶
     */
    private Integer isTop;
    
    /**
     * 排序
     */
    private Integer sortOrder;
    
    /**
     * 状态：0-待审核，1-已发布，2-已下架，3-审核拒绝
     */
    private Integer status;
    
    /**
     * 审核时间
     */
    private LocalDateTime auditTime;
    
    /**
     * 审核人ID
     */
    private Long auditorId;
    
    /**
     * 拒绝原因
     */
    private String rejectReason;
}
