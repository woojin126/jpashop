package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/*
* ToOne 관계에대한것 [ManyToOne , OneToOne]
* Order
* Order -> Member
* Order -> Delievery */
//V3 V4 는 차이점을 잘이용해서 사용하자, API 스펙에 테이블조회가 엄청 많고 크면 V4, 아니면 대부분 V3ㄴ
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    /**
     * 1.엔티티를 이렇게 직접 노출하면 무슨문제가 발생할까? (무한루프)
       2.Member -> orders ->Member -> orders 무한반복 (양방향 연관관계 문제생김 -> 한쪽은 @JsonIgnore 해야함)
       3.그런데 또한번 오류발생.. 조회하려해도 LAZY 전략이라 프록시를 사용해야하는데? 프록시에러
       4. 그냥 이렇게 하지말자 ㅡㅡ..
     **/
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1(){
        List<Order> all = orderRepository.findAllByCriteria(new OrderSearch());
        return all;
    }

    //v2 dto를 이용한방식은 ENTITY를 건들지않아서 좋지만,
    //LAZY 로딩을 사용하기떄문에 쿼리가 너무많이 나간다는 문제점이 있다.
    //order, delievery, member 3개의 테이블을 건드림
    
    //의아한점 : 
    @GetMapping("/api/v2/simple-orders")
    public Result<List<SimpleOrderDto>> ordersV2(){

        //ORDER 2개 조회됬을꺼
        //N + 1  ->  1 + 회원 N + 배송 N  == 문제발생
        List<Order> orders = orderRepository.findAllByCriteria(new OrderSearch());

        //처음돌때 ORDER.MEMBER에 한번찾고,
        List<SimpleOrderDto> collect = orders.stream().map(SimpleOrderDto::new)
                .collect(Collectors.toList());

        return new Result<>(collect);

    }
    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3(){
//fetch join은 MEMBER 객체와, DELIVERY 객체가 한번에 ORDER에담겨서 조회가됨
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        return orders.stream().map(SimpleOrderDto::new)
                .collect(Collectors.toList());

    }

    //v4장점, 딲딱 원하는 필드만 가져올수있음, v3는 전부다 가져옴
    // 에는 dto 를 조회하기때문에 비지니스로직 변경같은게 어려워  dto를 만진다고 db에 값이 변하진 않잖아?
    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4(){
        return orderSimpleQueryRepository.findOrderDtos();
    }


    @Data
    @AllArgsConstructor
    static class Result<T>{

       private T date;
    }

    @Data
    static class SimpleOrderDto{
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order){
            orderId = order.getId();
            name = order.getMember().getName(); //LAZY 로딩 초기화 영속성컨텍스트가 ID를가지고 직접 찾아와, 없으면 DB에서 가져옴 (MEMBER 쿼리날라감)
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress(); //lAZY 초기화 딜리버리 쿼리가나감.
        }

    }





}
