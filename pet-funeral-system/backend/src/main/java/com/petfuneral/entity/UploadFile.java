package com.petfuneral.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 上传文件记录
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("upload_file")
public class UploadFile extends BaseEntity {

    /**
     * 原始文件名
     */
    private String originalName;

    /**
     * 存储文件名
     */
    private String storageName;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 文件URL
     */
    private String fileUrl;

    /**
     * 文件类型 image/video/document
     */
    private String fileType;

    /**
     * MIME类型
     */
    private String mimeType;

    /**
     * 文件大小(字节)
     */
    private Long fileSize;

    /**
     * 缩略图URL (图片/视频)
     */
    private String thumbUrl;

    /**
     * 所属模块
     */
    private String module;

    /**
     * 关联业务ID
     */
    private Long bizId;

    /**
     * 上传用户ID
     */
    private Long uploadUserId;
}
