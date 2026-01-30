package com.petfuneral.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.petfuneral.common.response.PageResult;
import com.petfuneral.common.response.Result;
import com.petfuneral.entity.Customer;
import com.petfuneral.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 客户控制器
 */
@Tag(name = "客户管理")
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @Operation(summary = "分页查询客户")
    @GetMapping
    public Result<PageResult<Customer>> getCustomerPage(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword) {
        IPage<Customer> result = customerService.getCustomerPage(page, size, keyword);
        return Result.success(PageResult.of(result));
    }

    @Operation(summary = "获取客户详情")
    @GetMapping("/{id}")
    public Result<Customer> getCustomerDetail(@PathVariable Long id) {
        return Result.success(customerService.getCustomerDetail(id));
    }

    @Operation(summary = "统计客户数量")
    @GetMapping("/count")
    public Result<Long> countCustomers() {
        return Result.success(customerService.count());
    }
}
