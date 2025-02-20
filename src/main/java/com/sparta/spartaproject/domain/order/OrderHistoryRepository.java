package com.sparta.spartaproject.domain.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderHistoryRepository extends JpaRepository<OrderHistory, UUID> {
    List<OrderHistory> findAllByOrder(Order order);

    Optional<OrderHistory> findLatestFoodByOrderId(UUID id);

    @Query("SELECT COUNT(oh) FROM OrderHistory oh WHERE oh.order = :order")
    Integer countByOrder(@Param("order") Order order);
}

