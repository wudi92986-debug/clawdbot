package com.petfuneral.service;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.petfuneral.common.exception.BusinessException;
import com.petfuneral.dto.OrderCreateRequest;
import com.petfuneral.dto.OrderListDTO;
import com.petfuneral.entity.Pet;
import com.petfuneral.entity.ServiceOrder;
import com.petfuneral.entity.ServicePackage;
import com.petfuneral.mapper.PetMapper;
import com.petfuneral.mapper.ServiceOrderMapper;
import com.petfuneral.mapper.ServicePackageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 订单服务
 */
@Service
@RequiredArgsConstructor
public class OrderService {

    private final ServiceOrderMapper orderMapper;
    private final ServicePackageMapper packageMapper;
    private final PetMapper petMapper;

    /**
     * 分页查询订单
     */
    public IPage<OrderListDTO> getOrderPage(Integer page, Integer size, Integer status, String keyword) {
        Page<OrderListDTO> pageParam = new Page<>(page, size);
        return orderMapper.selectOrderPage(pageParam, status, keyword);
    }

    /**
     * 获取订单详情
     */
    public ServiceOrder getOrderDetail(Long orderId) {
        ServiceOrder order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        return order;
    }

    /**
     * 创建订单
     */
    @Transactional
    public ServiceOrder createOrder(Long customerId, OrderCreateRequest request) {
        // 查询套餐
        ServicePackage servicePackage = packageMapper.selectById(request.getPackageId());
        if (servicePackage == null) {
            throw new BusinessException("套餐不存在");
        }

        // 处理宠物信息
        Long petId = request.getPetId();
        if (petId == null && request.getPetName() != null) {
            // 创建新宠物
            Pet pet = new Pet();
            pet.setCustomerId(customerId);
            pet.setName(request.getPetName());
            pet.setSpecies(request.getPetType());
            pet.setStatus(1);
            petMapper.insert(pet);
            petId = pet.getId();
        }

        // 生成订单号
        String orderNo = "PF" + DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now())
                + IdUtil.fastSimpleUUID().substring(0, 4).toUpperCase();

        // 创建订单
        ServiceOrder order = new ServiceOrder();
        order.setOrderNo(orderNo);
        order.setCustomerId(customerId);
        order.setPetId(petId);
        order.setPackageId(request.getPackageId());
        order.setTotalAmount(servicePackage.getBasePrice());
        order.setPaidAmount(BigDecimal.ZERO);
        order.setStatus(0); // 待确认
        order.setPayStatus(0); // 未支付
        order.setAppointmentTime(request.getAppointmentTime());
        order.setPickupAddress(request.getPickupAddress());
        order.setContactName(request.getContactName());
        order.setContactPhone(request.getContactPhone());
        order.setRemark(request.getRemark());

        orderMapper.insert(order);
        return order;
    }

    /**
     * 确认订单
     */
    @Transactional
    public void confirmOrder(Long orderId, Long staffId) {
        ServiceOrder order = getOrderDetail(orderId);
        if (order.getStatus() != 0) {
            throw new BusinessException("订单状态不正确");
        }

        order.setStatus(1); // 已确认
        order.setStaffId(staffId);
        order.setConfirmedAt(LocalDateTime.now());
        orderMapper.updateById(order);
    }

    /**
     * 取消订单
     */
    @Transactional
    public void cancelOrder(Long orderId, String reason) {
        ServiceOrder order = getOrderDetail(orderId);
        if (order.getStatus() >= 4) {
            throw new BusinessException("订单无法取消");
        }

        order.setStatus(5); // 已取消
        order.setCancelReason(reason);
        orderMapper.updateById(order);
    }

    /**
     * 完成订单
     */
    @Transactional
    public void completeOrder(Long orderId) {
        ServiceOrder order = getOrderDetail(orderId);
        // 允许从已支付(1)或服务中(2)状态完成订单
        if (order.getStatus() < 1 || order.getStatus() > 2) {
            throw new BusinessException("订单状态不正确");
        }

        order.setStatus(3); // 已完成
        order.setCompletedAt(LocalDateTime.now());
        orderMapper.updateById(order);
    }

    /**
     * 确认支付
     */
    @Transactional
    public void confirmPayment(Long orderId, Integer payMethod, BigDecimal paidAmount) {
        ServiceOrder order = getOrderDetail(orderId);
        if (order.getPayStatus() != 0) {
            throw new BusinessException("订单已支付");
        }

        order.setPayStatus(1); // 已支付
        order.setPayMethod(payMethod);
        order.setPaidAmount(paidAmount);
        order.setPayTime(LocalDateTime.now());
        order.setStatus(1); // 已支付状态
        orderMapper.updateById(order);
    }

    /**
     * 统计订单数量
     */
    public Long countByStatus(Integer status) {
        LambdaQueryWrapper<ServiceOrder> wrapper = new LambdaQueryWrapper<>();
        if (status != null) {
            wrapper.eq(ServiceOrder::getStatus, status);
        }
        return orderMapper.selectCount(wrapper);
    }
}
