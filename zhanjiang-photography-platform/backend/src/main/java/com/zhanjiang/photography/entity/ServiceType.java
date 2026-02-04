package com.zhanjiang.photography.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 服务类型实体
 */
@Data
@TableName("t_service_type")
public class ServiceType implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 类型名称
     */
    private String typeName;
    
    /**
     * 描述
     */
    private String description;
    
    /**
     * 图标
     */
    private String icon;
    
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
