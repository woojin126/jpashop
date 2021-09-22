package jpabook.jpashop.repository.order.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 엔티티 조회x dto 조회를 위한 전용
 */
@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager em;

    public List<OrderQueryDto> findOrderQueryDtos() {
        List<OrderQueryDto> result = findOrders(); //1번이면 아래 N번  (N+1 발생)

        result.forEach(o -> {
            System.out.println("o.getOrderId() = " + o.getOrderId());
           List<OrderItemQueryDto> orderItems =  findOrderItems(o.getOrderId()); //쿼리 N번
           o.setOrderItems(orderItems);
        });
        return result;
    }


    public List<OrderQueryDto> findAllByDto_optimization() {
        List<OrderQueryDto> result = findOrders();

        List<Long> orderIds = result.stream()
                .map(OrderQueryDto::getOrderId)
                .collect(Collectors.toList());

        List<OrderItemQueryDto> orderItems = em.createQuery(
                "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto " +
                        " ( oi.order.id, i.name, oi.orderPrice, oi.count  )" +
                        " from OrderItem oi " +
                        " join oi.item i " +
                        " where oi.order.id in :orderIds ", OrderItemQueryDto.class)
                .setParameter("orderIds", orderIds)
                .getResultList();

        Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItems.stream()
                .collect(Collectors.groupingBy(OrderItemQueryDto::getOrderId));//맵으로바꿔줌 성능최적화를위해


        System.out.println("맵"+orderItemMap.get(orderIds.get(0)));

        result.forEach(o ->o.setOrderItems(orderItemMap.get(o.getOrderId())));

        return result;
    }

    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery(
                "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto " +
                        " ( oi.order.id, i.name, oi.orderPrice, oi.count  )" +
                        "from OrderItem oi " +
                        " join oi.item i " +
                        " where oi.order.id = :orderId ",OrderItemQueryDto.class)
                .setParameter("orderId", orderId)
                .getResultList();

    }

    /**
     *  dto(필요한 필드만) 직접조회에서  fetch join을 여기서 사용하지 않는이유
     *  fetch join인 객체그래프 모두를 가져오는 목적이자, 그렇게 사용하는것,
     *  우리는 필요한 필드만 가져져오려고 하는것 .
     * */
    private List<OrderQueryDto> findOrders() {
        return em.createQuery(
                "select new jpabook.jpashop.repository.order.query.OrderQueryDto" +
                        " (o.id,m.name,o.orderDate, o.status, d.address) " +
                        "from Order o " +
                        " join o.member m " +
                        " join o.delivery d ", OrderQueryDto.class)
                .setFirstResult(0)
                .setMaxResults(3)
                .getResultList();
    }

    public List<OrderFlatDto> findAllByDto_flat() {

        return em.createQuery(" select new " +
                " jpabook.jpashop.repository.order.query.OrderFlatDto" +
                " ( o.id , m.name ,o.orderDate, o.status, d.address, i.name, oi.orderPrice ,oi.count) " +
                " from Order o " +
                " join o.member m " +
                " join o.delivery d " +
                " join o.orderItems oi " +
                " join oi.item i " , OrderFlatDto.class)
                .getResultList();
    }
}


