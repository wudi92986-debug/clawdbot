package com.zhanjiang.photography.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhanjiang.photography.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    
}
