package com.petfuneral.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.petfuneral.entity.Pet;
import org.apache.ibatis.annotations.Mapper;

/**
 * 宠物 Mapper
 */
@Mapper
public interface PetMapper extends BaseMapper<Pet> {
}
