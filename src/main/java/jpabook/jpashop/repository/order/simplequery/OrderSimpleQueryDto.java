package jpabook.jpashop.repository.order.simplequery;


import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderSimpleQueryDto {
    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;

    public OrderSimpleQueryDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address ){
        this.orderId = orderId;
        this.name =  name;//LAZY 로딩 초기화 영속성컨텍스트가 ID를가지고 직접 찾아와, 없으면 DB에서 가져옴 (MEMBER 쿼리날라감)
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address; //lAZY 초기화 딜리버리 쿼리가나감.
    }

}

