package com.sparta.spartaproject.mapper;

import com.sparta.spartaproject.domain.order.Order;
import com.sparta.spartaproject.domain.order.PayMethod;
import com.sparta.spartaproject.domain.pay.PayHistory;
import com.sparta.spartaproject.domain.store.Store;
import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.dto.request.CreatePayHistoryRequestDto;
import com.sparta.spartaproject.dto.response.OnlyPayHistoryDto;
import com.sparta.spartaproject.dto.response.PayHistoryDetailDto;
import com.sparta.spartaproject.dto.response.PayHistoryDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PayHistoryMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", source = "order")
    @Mapping(target = "store", source = "store")
    @Mapping(target = "payMethod", source = "payMethod")
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    PayHistory toPayHistory(Order order, Store store, User user, PayMethod payMethod);

    @Mapping(target = "orderId", source = "payHistory.order.id")
    @Mapping(target = "shopName", source = "payHistory.order.store.name")
    @Mapping(target = "totalPrice", source = "payHistory.order.totalPrice")
    @Mapping(target = "paymentMethod", source = "payMethodDescription")
    @Mapping(target = "payStatusDescription", source = "description")
    PayHistoryDetailDto toPayHistoryDetailDto(PayHistory payHistory, String description, String payMethodDescription);


    PayHistoryDto toPayHistoryDto(
            List<OnlyPayHistoryDto> onlyPayHistoryDtoList,
            Integer currentPage,
            Integer totalPages,
            Integer totalElements
    );

    @Mapping(target = "orderId", source = "payHistory.order.id")
    @Mapping(target = "shopName", source = "payHistory.store.name")
    @Mapping(target = "totalPrice", source = "payHistory.order.totalPrice")
    @Mapping(target = "status", source = "description")
    @Mapping(target = "paymentMethod", source = "payMethodDescription")
    OnlyPayHistoryDto toOnlyPayHistoryDto(PayHistory payHistory, String description, String payMethodDescription);
}
