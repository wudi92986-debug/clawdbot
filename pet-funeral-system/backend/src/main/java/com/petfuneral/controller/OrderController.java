package com.petfuneral.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.petfuneral.common.response.PageResult;
import com.petfuneral.common.response.Result;
import com.petfuneral.dto.OrderCreateRequest;
import com.petfuneral.dto.OrderListDTO;
import com.petfuneral.entity.ServiceOrder;
import com.petfuneral.security.UserPrincipal;
import com.petfuneral.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 订单控制器
 */
@Tag(name = "订单管理")
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "分页查询订单")
    @GetMapping
    public Result<PageResult<OrderListDTO>> getOrderPage(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String keyword) {
        IPage<OrderListDTO> result = orderService.getOrderPage(page, pageSize, status, keyword);
        return Result.success(PageResult.of(result));
    }

    @Operation(summary = "获取订单详情")
    @GetMapping("/{id}")
    public Result<ServiceOrder> getOrderDetail(@PathVariable Long id) {
        return Result.success(orderService.getOrderDetail(id));
    }

    @Operation(summary = "创建订单")
    @PostMapping
    public Result<ServiceOrder> createOrder(
            @AuthenticationPrincipal UserPrincipal user,
            @Valid @RequestBody OrderCreateRequest request) {
        // 这里 customerId 应该从登录客户获取，暂用 userId
        ServiceOrder order = orderService.createOrder(user.getUserId(), request);
        return Result.success("预约成功", order);
    }

    @Operation(summary = "确认订单")
    @PostMapping("/{id}/confirm")
    public Result<Void> confirmOrder(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal user) {
        orderService.confirmOrder(id, user.getUserId());
        return Result.ok("订单已确认");
    }

    @Operation(summary = "确认支付")
    @PostMapping("/{id}/pay")
    public Result<Void> confirmPayment(
            @PathVariable Long id,
            @RequestBody java.util.Map<String, Object> params) {
        Integer payMethod = (Integer) params.get("payMethod");
        Number paidAmountNum = (Number) params.get("paidAmount");
        java.math.BigDecimal paidAmount = paidAmountNum != null 
            ? java.math.BigDecimal.valueOf(paidAmountNum.doubleValue()) 
            : java.math.BigDecimal.ZERO;
        orderService.confirmPayment(id, payMethod, paidAmount);
        return Result.ok("支付确认成功");
    }

    @Operation(summary = "取消订单")
    @PostMapping("/{id}/cancel")
    public Result<Void> cancelOrder(
            @PathVariable Long id,
            @RequestParam(required = false) String reason) {
        orderService.cancelOrder(id, reason);
        return Result.ok("订单已取消");
    }

    @Operation(summary = "完成订单")
    @PostMapping("/{id}/complete")
    public Result<Void> completeOrder(@PathVariable Long id) {
        orderService.completeOrder(id);
        return Result.ok("订单已完成");
    }

    @Operation(summary = "统计订单数量")
    @GetMapping("/count")
    public Result<Long> countOrders(@RequestParam(required = false) Integer status) {
        return Result.success(orderService.countByStatus(status));
    }
}
