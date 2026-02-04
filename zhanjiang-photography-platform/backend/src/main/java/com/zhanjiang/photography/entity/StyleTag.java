package com.zhanjiang.photography.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 风格标签实体
 */
@Data
@TableName("t_style_tag")
public class StyleTag implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 标签名称
     */
    private String tagName;
    
    /**
     * 类型：1-拍摄风格，2-场景标签
     */
    private Integer tagType;
    
    /**
     * 图标
     */
    private String icon;
    
    /**
     * 颜色值
     */
    private String color;
    
    /**
     * 排序
     */
    private Integer sortOrder;
    
    /**
     * 状态
     */
    private Integer status;
    
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
