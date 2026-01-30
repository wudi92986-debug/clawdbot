package com.petfuneral.dto;

import lombok.Builder;
import lombok.Data;

/**
 * 上传结果
 */
@Data
@Builder
public class UploadResult {
    /**
     * 文件ID
     */
    private Long fileId;

    /**
     * 原始文件名
     */
    private String originalName;

    /**
     * 文件URL
     */
    private String url;

    /**
     * 缩略图URL
     */
    private String thumbUrl;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 文件大小
     */
    private Long fileSize;
}
