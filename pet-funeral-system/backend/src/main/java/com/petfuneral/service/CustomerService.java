package com.petfuneral.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.petfuneral.common.exception.BusinessException;
import com.petfuneral.entity.Customer;
import com.petfuneral.mapper.CustomerMapper;
import org.springframework.stereotype.Service;

/**
 * 客户服务
 */
@Service
public class CustomerService extends ServiceImpl<CustomerMapper, Customer> {

    /**
     * 分页查询客户
     */
    public IPage<Customer> getCustomerPage(Integer page, Integer size, String keyword) {
        Page<Customer> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Customer> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(Customer::getName, keyword)
                   .or()
                   .like(Customer::getPhone, keyword);
        }
        wrapper.orderByDesc(Customer::getCreatedAt);
        return this.page(pageParam, wrapper);
    }

    /**
     * 获取客户详情
     */
    public Customer getCustomerDetail(Long customerId) {
        Customer customer = this.getById(customerId);
        if (customer == null) {
            throw new BusinessException("客户不存在");
        }
        return customer;
    }

    /**
     * 根据手机号获取客户
     */
    public Customer getByPhone(String phone) {
        return this.getOne(
                new LambdaQueryWrapper<Customer>().eq(Customer::getPhone, phone)
        );
    }

    /**
     * 创建或获取客户 (用于H5端登录)
     */
    public Customer getOrCreate(String phone, String nickname) {
        Customer customer = getByPhone(phone);
        if (customer == null) {
            customer = new Customer();
            customer.setPhone(phone);
            customer.setName(nickname != null ? nickname : "用户" + phone.substring(7));
            this.save(customer);
        }
        return customer;
    }
}
