package com.zhanjiang.photography.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 高校实体
 */
@Data
@TableName("t_university")
public class University implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 高校名称
     */
    private String universityName;
    
    /**
     * 简称
     */
    private String shortName;
    
    /**
     * 省份
     */
    private String province;
    
    /**
     * 城市
     */
    private String city;
    
    /**
     * 地址
     */
    private String address;
    
    /**
     * Logo图片
     */
    private String logo;
    
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
