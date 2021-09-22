package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.query.OrderFlatDto;
import jpabook.jpashop.repository.order.query.OrderQueryDto;
import jpabook.jpashop.repository.order.query.OrderQueryRepository;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByCriteria(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName();
            order.getDelivery().getAddress();
            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.forEach(o -> o.getItem().getName());
        }
        return all;
    }

    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2() {

        List<Order> orders = orderRepository.findAllByCriteria(new OrderSearch());

        return orders.stream().map(OrderDto::new)
                .collect(Collectors.toList());
    }

    /**
     데이터 뻥튀기가됨 OrderItem 떄문에, postman 쿼리확인해보자
     fetch join의 치명적인 단점,,일대 다를 fetch join하는순간 페이징쿼리가 안나감.. 못씀
     ,단점은 중복이 너무많음 일대다를 패치조인하면 db확인하면 알수있다.
     이건 쿼리가 한방만 나가지만 너무많은 중복
     */
    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithItem();

        return orders.stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());

    }

    /**
     * order, orderItem , item 의 관계가 @BatchSize 설정하나로인해
     * 1        다대        다 의관계가
     * 일 대 일대 일 관계가되버림;
     * (이것은 v3 보다 쿼리가좀더 나가지만 정말 필요한 최적의 쿼리만 뽑아낸다는장점)
     * v3보다 이방식이 좋은거같음 (페이징도 가능)
     */
    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> ordersV3_page(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "1") int limit ) {
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset,limit);
        return orders.stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());
    }

    //dto 직접 조회
    //여러테이블에서 데이터를 일부 조회하거나 통계성 데이터를 낼때 dto 직접조회를 많이쓴다.
    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> ordersV4(){
        return orderQueryRepository.findOrderQueryDtos();

    }


    //여러테이블에서 데이터를 일부 조회하거나 통계성 데이터를 낼때 dto 직접조회를 많이쓴다. v4의 N+1 문제 까지해결
    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> ordersV5(){
        return orderQueryRepository.findAllByDto_optimization();
    }

 /* v5는 쿼리 두방이면 이건 1방으로 최적화
  오더와 오더아이템을 join해서 그냥 한방에가져옴
  결국 한방쿼리라 데이터 뻥튀기가일어남.. 페이징이 가능할까   X ..
  */
    @GetMapping("/api/v6/orders")
    public List<OrderFlatDto> ordersV6(){
        return orderQueryRepository.findAllByDto_flat();
    }


    @Data
    static class OrderDto {

        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        //엔티티는아예 사용하면안되 감싸는것도 x
        //이것조차도 dto로 바궈야해
        private List<OrderItemDto> orderItems; //에는 엔티티라 에러가날껏


        protected OrderDto() {

        }

        public OrderDto(Order o) {
            orderId = o.getId();
            name = o.getMember().getName();
            orderDate = o.getOrderDate();
            orderStatus = o.getStatus();
            address = o.getDelivery().getAddress();
            orderItems = o.getOrderItems().stream().map(OrderItemDto::new)
                    .collect(Collectors.toList());
        }
    }

    @Getter //이런식으로 고객이원하는 정보 몇가지만 딲딱 dto로 걸러서 출력
    static class OrderItemDto {

        private String itemName;//상품명
        private int orderPrice;//주문가셧
        private int count;//주문수량


        public OrderItemDto(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }
}
