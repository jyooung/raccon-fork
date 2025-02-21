package com.sparta.spartaproject.domain.order;

import com.sparta.spartaproject.domain.food.Food;
import com.sparta.spartaproject.domain.food.FoodRepository;
import com.sparta.spartaproject.domain.pay.PayHistory;
import com.sparta.spartaproject.domain.pay.PayHistoryRepository;
import com.sparta.spartaproject.domain.store.Store;
import com.sparta.spartaproject.domain.store.StoreRepository;
import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.domain.user.UserService;
import com.sparta.spartaproject.dto.request.CreateFoodOrderRequestDto;
import com.sparta.spartaproject.dto.request.CreateOrderRequestDto;
import com.sparta.spartaproject.dto.request.UpdateOrderStatusRequestDto;
import com.sparta.spartaproject.dto.response.OrderDetailDto;
import com.sparta.spartaproject.dto.response.OnlyOrderDto;
import com.sparta.spartaproject.dto.response.OrderDto;
import com.sparta.spartaproject.dto.response.OrderStatusDto;
import com.sparta.spartaproject.exception.BusinessException;
import com.sparta.spartaproject.mapper.OrderHistoryMapper;
import com.sparta.spartaproject.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.sparta.spartaproject.domain.order.OrderStatus.*;
import static com.sparta.spartaproject.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final UserService userService;
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;
    private final OrderHistoryMapper orderHistoryMapper;
    private final OrderHistoryRepository orderHistoryRepository;
    private final FoodRepository foodRepository;

    private final Integer size = 10;
    private final PayHistoryRepository payHistoryRepository;

    @Transactional(readOnly = true)
    public OrderStatusDto getStatus(UUID orderId) {
        User user = getUser();

        Order findedOrder = orderRepository.findByIdAndUserAndIsDeletedFalse(orderId, user)
                .orElseThrow(() -> new BusinessException(ORDER_NOT_EXIST));

        return orderMapper.toOrderStatusResponseDto(findedOrder);
    }

    @Transactional
    public void cancelOrder(UUID orderId) {
        User user = getUser();

        Order order = orderRepository.findByIdAndUserAndIsDeletedFalse(orderId, user)
                .orElseThrow(() -> new BusinessException(ORDER_NOT_EXIST));

        if (order.getCreatedAt().isBefore(LocalDateTime.now().minusMinutes(5))) {
            order.changeOrderStatus(PENDING);
            throw new BusinessException(CAN_NOT_CANCEL_ORDER);
        }

        order.changeOrderStatus(CANCEL);
        order.deleteOrder();
        orderRepository.save(order);
    }

    @Transactional
    public void updateStatus(UpdateOrderStatusRequestDto request) {
        Order order = getMyStoreOrder(request.orderId());

        order.changeOrderStatus(request.orderStatus());
    }

    @Transactional
    public void rejectOrder(UUID orderId) {
        Order order = getMyStoreOrder(orderId);

        order.changeOrderStatus(REFUSE);
    }

    @Transactional
    public void acceptOrder(UUID orderId) {
        Order order = getMyStoreOrder(orderId);

        order.changeOrderStatus(ACCEPT);
    }


    @Transactional
    public void createOrder(CreateOrderRequestDto request) {
        int totalPrice = 0;

        User user = getUser();
        Store store = storeRepository.findById(request.storeId())
                .orElseThrow(() -> new BusinessException(STORE_NOT_FOUND));

        Order order = orderMapper.toOrder(request, user, store, WAIT);

        List<OrderHistory> orderHistoryList = new ArrayList<>();

        for (CreateFoodOrderRequestDto food : request.foods()) {
            Food findFood = foodRepository.findById(food.foodId())
                    .orElseThrow(() -> new BusinessException(FOOD_NOT_FOUND));

            OrderHistory orderHistory = orderHistoryMapper.toOrderHistory(
                    order,
                    store,
                    findFood,
                    food.quantity(),
                    food.quantity() * findFood.getPrice()
            );

            orderHistoryList.add(orderHistory);
            totalPrice += findFood.getPrice() * food.quantity();
        }

        PayHistory payHistory = PayHistory.builder()
                .order(order)
                .store(store)
                .user(user)
                .paymentMethod(PaymentMethod.CARD)
                .build();

        order.updateTotalPrice(totalPrice);

        orderRepository.save(order);
        payHistoryRepository.save(payHistory);
        orderHistoryRepository.saveAll(orderHistoryList);

    }

    @Transactional(readOnly = true)
    public OrderDto getAllOrders(int page) {
        Pageable pageable = PageRequest.of(page - 1, size);

        User user = getUser();

        List<Order> orders = orderRepository.findAllByUserAndIsDeletedFalse(pageable, user);

        int totalOrderCount = orders.size();

        List<OnlyOrderDto> orderDtoList = orders.stream()
                .map(
                        order ->
                                orderMapper.toOrderOnlyDto(order,
                                        orderHistoryRepository.findFirstByOrderId(order.getId())
                                                .orElseThrow(() -> new BusinessException(ORDER_NOT_EXIST)).getFood().getName(),
                                        orderHistoryRepository.findByOrderId(order.getId()).stream()
                                                .mapToInt(OrderHistory::getQty)
                                                .sum()
                                ))
                .toList();

        return orderMapper.toOrderDto(orderDtoList, page, (int) Math.ceil((double) totalOrderCount / size), totalOrderCount);
    }

    @Transactional
    public OrderDetailDto getOrderDetail(UUID id) {

        User user = getUser();

        Order order = orderRepository.findByIdAndUserAndIsDeletedFalse(id, user)
                .orElseThrow(() -> new BusinessException(ORDER_NOT_EXIST));

        return orderMapper.toOrderDetailResponseDto(order);
    }


    private User getUser() {
        return userService.loginUser();
    }

    public Order getOrderByIdAndIsDeletedIsFalse(UUID id) {

        User user = getUser();

        return orderRepository.findByIdAndUserAndIsDeletedFalse(id, user)
                .orElseThrow(() -> new BusinessException(ORDER_NOT_EXIST));
    }

    private Order getMyStoreOrder(UUID orderId) {
        User user = getUser();

        return orderRepository.findByIdAndStoreOwnerAndIsDeletedIsFalse(orderId, user)
                .orElseThrow(() -> new BusinessException(ORDER_NOT_EXIST));
    }
}