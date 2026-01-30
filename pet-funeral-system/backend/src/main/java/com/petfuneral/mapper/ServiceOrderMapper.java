package com.petfuneral.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.petfuneral.dto.OrderListDTO;
import com.petfuneral.entity.ServiceOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 服务订单 Mapper
 */
@Mapper
public interface ServiceOrderMapper extends BaseMapper<ServiceOrder> {

    /**
     * 分页查询订单列表（包含关联信息）
     */
    IPage<OrderListDTO> selectOrderPage(Page<OrderListDTO> page,
                                        @Param("status") Integer status,
                                        @Param("keyword") String keyword);
}
