package com.petfuneral.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.petfuneral.entity.Ceremony;
import org.apache.ibatis.annotations.Mapper;

/**
 * 告别仪式 Mapper
 */
@Mapper
public interface CeremonyMapper extends BaseMapper<Ceremony> {
}
