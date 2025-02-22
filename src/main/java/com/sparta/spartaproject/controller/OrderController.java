package com.sparta.spartaproject.controller;

import com.sparta.spartaproject.domain.order.OrderService;
import com.sparta.spartaproject.dto.request.CreateOrderRequestDto;
import com.sparta.spartaproject.dto.request.UpdateOrderStatusRequestDto;
import com.sparta.spartaproject.dto.response.OrderDetailDto;
import com.sparta.spartaproject.dto.response.OrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Description;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    @Description(
        "주문하기"
    )
    @PostMapping
    public ResponseEntity<Void> createOrder(@RequestBody CreateOrderRequestDto request) {
        orderService.createOrder(request);
        return ResponseEntity.ok().build();
    }

    @Description(
        "manager, master - 주문 내역 전체 조회"
    )
    @GetMapping
    @PreAuthorize("hasAnyAuthority('MASTER', 'MANAGER')")
    public ResponseEntity<Page<OrderDto>> getOrders(
        @RequestParam(required = false, defaultValue = "1") int page,
        @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection
    ) {
        return ResponseEntity.ok(orderService.getOrders(page, sortDirection));
    }

    @Description(
        "owner - 주문 내역 전체 조회"
    )
    @GetMapping("/owner/stores/{storeId}")
    @PreAuthorize("hasAuthority('OWNER')")
    public ResponseEntity<Page<OrderDto>> getOrdersForOwner(
        @PathVariable UUID storeId,
        @RequestParam(required = false, defaultValue = "1") int page,
        @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection
    ) {
        return ResponseEntity.ok(orderService.getOrdersForOwner(storeId, page, sortDirection));
    }

    @Description(
        "주문 내역 상세 조회"
    )
    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailDto> getOrderDetail(@PathVariable UUID id) {
        return ResponseEntity.ok(orderService.getOrderDetail(id));
    }

    @Description(
        "주문 받기"
    )
    @PatchMapping("/{id}/accept")
    @PreAuthorize("hasAnyAuthority('OWNER', 'MASTER', 'MANAGER')")
    public ResponseEntity<Void> acceptOrder(@PathVariable UUID id) {
        orderService.acceptOrder(id);
        return ResponseEntity.ok().build();
    }

    @Description(
        "주문 취소"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelOrder(@PathVariable UUID id) {
        orderService.cancelOrder(id);
        return ResponseEntity.ok().build();
    }

    @Description(
        "주문 거절"
    )
    @PatchMapping("/{id}/reject")
    @PreAuthorize("hasAnyAuthority('OWNER', 'MASTER', 'MANAGER')")
    public ResponseEntity<Void> rejectOrder(@PathVariable UUID id) {
        orderService.rejectOrder(id);
        return ResponseEntity.ok().build();
    }

    @Description(
        "주문 상태 변경"
    )
    @PatchMapping("/status")
    @PreAuthorize("hasAnyAuthority('OWNER', 'MASTER', 'MANAGER')")
    public ResponseEntity<Void> updateStatus(@RequestBody UpdateOrderStatusRequestDto update) {
        orderService.updateStatus(update);
        return ResponseEntity.ok().build();
    }
}