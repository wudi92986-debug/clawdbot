package com.petfuneral.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.petfuneral.common.exception.BusinessException;
import com.petfuneral.dto.UploadResult;
import com.petfuneral.entity.UploadFile;
import com.petfuneral.mapper.UploadFileMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * 文件上传服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileUploadService {

    private final UploadFileMapper uploadFileMapper;

    @Value("${file.upload.path:uploads}")
    private String uploadPath;

    @Value("${file.upload.base-url:/uploads}")
    private String baseUrl;

    @Value("${file.upload.max-size:10485760}")
    private Long maxFileSize; // 默认10MB

    private static final Set<String> IMAGE_TYPES = Set.of("image/jpeg", "image/png", "image/gif", "image/webp");
    private static final Set<String> VIDEO_TYPES = Set.of("video/mp4", "video/mpeg", "video/quicktime", "video/x-msvideo");
    private static final Set<String> DOCUMENT_TYPES = Set.of("application/pdf", "application/msword", 
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document");

    @PostConstruct
    public void init() {
        // 创建上传目录
        try {
            Files.createDirectories(Paths.get(uploadPath));
            log.info("文件上传目录: {}", uploadPath);
        } catch (IOException e) {
            log.error("创建上传目录失败", e);
        }
    }

    /**
     * 上传单个文件
     */
    @Transactional
    public UploadResult uploadFile(MultipartFile file, String module, Long bizId, Long userId) {
        // 验证文件
        validateFile(file);

        String originalName = file.getOriginalFilename();
        String mimeType = file.getContentType();
        String fileType = getFileType(mimeType);
        
        // 生成存储路径
        String dateDir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String storageName = UUID.randomUUID().toString().replace("-", "") + getExtension(originalName);
        String relativePath = module + "/" + dateDir + "/" + storageName;
        Path fullPath = Paths.get(uploadPath, relativePath);

        try {
            // 创建目录
            Files.createDirectories(fullPath.getParent());
            
            // 保存文件
            file.transferTo(fullPath.toFile());
            
            String fileUrl = baseUrl + "/" + relativePath;
            String thumbUrl = null;
            
            // 如果是图片，生成缩略图
            if ("image".equals(fileType)) {
                thumbUrl = generateThumbnail(fullPath, module, dateDir);
            }

            // 保存记录
            UploadFile uploadFile = new UploadFile();
            uploadFile.setOriginalName(originalName);
            uploadFile.setStorageName(storageName);
            uploadFile.setFilePath(relativePath);
            uploadFile.setFileUrl(fileUrl);
            uploadFile.setFileType(fileType);
            uploadFile.setMimeType(mimeType);
            uploadFile.setFileSize(file.getSize());
            uploadFile.setThumbUrl(thumbUrl);
            uploadFile.setModule(module);
            uploadFile.setBizId(bizId);
            uploadFile.setUploadUserId(userId);
            
            uploadFileMapper.insert(uploadFile);

            return UploadResult.builder()
                    .fileId(uploadFile.getId())
                    .originalName(originalName)
                    .url(fileUrl)
                    .thumbUrl(thumbUrl)
                    .fileType(fileType)
                    .fileSize(file.getSize())
                    .build();

        } catch (IOException e) {
            log.error("文件上传失败", e);
            throw new BusinessException("文件上传失败: " + e.getMessage());
        }
    }

    /**
     * 批量上传
     */
    @Transactional
    public List<UploadResult> uploadFiles(MultipartFile[] files, String module, Long bizId, Long userId) {
        return Arrays.stream(files)
                .map(file -> uploadFile(file, module, bizId, userId))
                .toList();
    }

    /**
     * 删除文件
     */
    @Transactional
    public void deleteFile(Long fileId) {
        UploadFile uploadFile = uploadFileMapper.selectById(fileId);
        if (uploadFile == null) {
            return;
        }

        // 删除物理文件
        try {
            Path filePath = Paths.get(uploadPath, uploadFile.getFilePath());
            Files.deleteIfExists(filePath);
            
            // 删除缩略图
            if (uploadFile.getThumbUrl() != null) {
                String thumbPath = uploadFile.getThumbUrl().replace(baseUrl + "/", "");
                Files.deleteIfExists(Paths.get(uploadPath, thumbPath));
            }
        } catch (IOException e) {
            log.warn("删除物理文件失败: {}", e.getMessage());
        }

        // 删除记录
        uploadFileMapper.deleteById(fileId);
    }

    /**
     * 获取业务相关的文件
     */
    public List<UploadFile> getFilesByBiz(String module, Long bizId) {
        return uploadFileMapper.selectList(
            new LambdaQueryWrapper<UploadFile>()
                .eq(UploadFile::getModule, module)
                .eq(UploadFile::getBizId, bizId)
                .orderByDesc(UploadFile::getCreatedAt)
        );
    }

    /**
     * 验证文件
     */
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("文件不能为空");
        }
        
        if (file.getSize() > maxFileSize) {
            throw new BusinessException("文件大小不能超过 " + (maxFileSize / 1024 / 1024) + "MB");
        }

        String mimeType = file.getContentType();
        if (!IMAGE_TYPES.contains(mimeType) && !VIDEO_TYPES.contains(mimeType) && !DOCUMENT_TYPES.contains(mimeType)) {
            throw new BusinessException("不支持的文件类型: " + mimeType);
        }
    }

    /**
     * 获取文件类型
     */
    private String getFileType(String mimeType) {
        if (IMAGE_TYPES.contains(mimeType)) {
            return "image";
        } else if (VIDEO_TYPES.contains(mimeType)) {
            return "video";
        } else if (DOCUMENT_TYPES.contains(mimeType)) {
            return "document";
        }
        return "other";
    }

    /**
     * 获取文件扩展名
     */
    private String getExtension(String filename) {
        if (filename == null) {
            return "";
        }
        int dotIndex = filename.lastIndexOf('.');
        return dotIndex >= 0 ? filename.substring(dotIndex) : "";
    }

    /**
     * 生成缩略图
     */
    private String generateThumbnail(Path originalPath, String module, String dateDir) {
        try {
            BufferedImage originalImage = ImageIO.read(originalPath.toFile());
            if (originalImage == null) {
                return null;
            }

            // 计算缩略图尺寸 (最大200x200)
            int width = originalImage.getWidth();
            int height = originalImage.getHeight();
            int thumbWidth = 200;
            int thumbHeight = 200;
            
            if (width > height) {
                thumbHeight = (int) (height * 200.0 / width);
            } else {
                thumbWidth = (int) (width * 200.0 / height);
            }

            // 创建缩略图
            BufferedImage thumbnail = new BufferedImage(thumbWidth, thumbHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = thumbnail.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(originalImage, 0, 0, thumbWidth, thumbHeight, null);
            g.dispose();

            // 保存缩略图
            String thumbName = "thumb_" + originalPath.getFileName().toString();
            String thumbRelativePath = module + "/" + dateDir + "/" + thumbName;
            Path thumbPath = Paths.get(uploadPath, thumbRelativePath);
            
            String formatName = originalPath.toString().toLowerCase().endsWith(".png") ? "png" : "jpg";
            ImageIO.write(thumbnail, formatName, thumbPath.toFile());

            return baseUrl + "/" + thumbRelativePath;

        } catch (IOException e) {
            log.warn("生成缩略图失败: {}", e.getMessage());
            return null;
        }
    }
}
