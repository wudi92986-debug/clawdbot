package com.petfuneral.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 纪念馆相册
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("memorial_album")
public class MemorialAlbum extends BaseEntity {

    /**
     * 纪念馆ID
     */
    private Long memorialId;

    /**
     * 媒体类型 1-图片 2-视频
     */
    private Integer mediaType;

    /**
     * 文件URL
     */
    private String url;

    /**
     * 缩略图URL
     */
    private String thumbUrl;

    /**
     * 描述
     */
    private String description;

    /**
     * 拍摄日期
     */
    private LocalDate takenAt;

    /**
     * 排序
     */
    private Integer sortOrder;
}
