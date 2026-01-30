package com.petfuneral.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.petfuneral.entity.UploadFile;
import org.apache.ibatis.annotations.Mapper;

/**
 * 上传文件 Mapper
 */
@Mapper
public interface UploadFileMapper extends BaseMapper<UploadFile> {
}
