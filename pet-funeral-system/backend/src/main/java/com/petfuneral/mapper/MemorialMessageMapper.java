package com.petfuneral.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.petfuneral.entity.MemorialMessage;
import org.apache.ibatis.annotations.Mapper;

/**
 * 纪念馆留言 Mapper
 */
@Mapper
public interface MemorialMessageMapper extends BaseMapper<MemorialMessage> {
}
