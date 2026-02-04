package com.zhanjiang.photography.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhanjiang.photography.entity.Photographer;
import org.apache.ibatis.annotations.Mapper;

/**
 * 摄影师Mapper
 */
@Mapper
public interface PhotographerMapper extends BaseMapper<Photographer> {
    
}
