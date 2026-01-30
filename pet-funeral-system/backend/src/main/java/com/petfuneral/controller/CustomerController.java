package com.petfuneral.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.petfuneral.common.response.PageResult;
import com.petfuneral.common.response.Result;
import com.petfuneral.entity.Customer;
import com.petfuneral.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword) {
        IPage<Customer> result = customerService.getCustomerPage(page, pageSize, keyword);
        return Result.success(PageResult.of(result));
    }

    @Operation(summary = "获取客户详情")
    @GetMapping("/{id}")
    public Result<Customer> getCustomerDetail(@PathVariable Long id) {
        return Result.success(customerService.getCustomerDetail(id));
    }

    @Operation(summary = "新增客户")
    @PostMapping
    public Result<Customer> createCustomer(@Valid @RequestBody Customer customer) {
        customerService.save(customer);
        return Result.success(customer);
    }

    @Operation(summary = "更新客户")
    @PutMapping("/{id}")
    public Result<Customer> updateCustomer(@PathVariable Long id, @Valid @RequestBody Customer customer) {
        customer.setId(id);
        customerService.updateById(customer);
        return Result.success(customer);
    }

    @Operation(summary = "删除客户")
    @DeleteMapping("/{id}")
    public Result<Void> deleteCustomer(@PathVariable Long id) {
        customerService.removeById(id);
        return Result.ok("删除成功");
    }

    @Operation(summary = "统计客户数量")
    @GetMapping("/count")
    public Result<Long> countCustomers() {
        return Result.success(customerService.count());
    }
}
