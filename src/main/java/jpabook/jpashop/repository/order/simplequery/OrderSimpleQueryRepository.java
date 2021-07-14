package jpabook.jpashop.repository.order.simplequery;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {

    private final EntityManager em;

    //성능최적화이자, API 스펙이기떄문에 (엔티티전용 REPOSITORY에 함께 묶어두기는 애매해 ==> 그래서 따로 만듬)
    //OrderSimpleQueryDto.class 이건 dto라 매핑이 될수가없음,entity나, embedded 타입 정도만 반환할수잇음
    //dto를 반환하려면 new 오퍼레이션 사용해야함(new 명령어를 사용해서 JPQL의 결과를 DTO로 즉시 변환) orderv4 예제
    //new 오퍼레이션에서는 o <- 같은 엔티티를 바로넘기는건 안됨 (o면 o의 식별자로넘어감 o.orderId)
    public List<OrderSimpleQueryDto> findOrderDtos() {
        return em.createQuery(
                " select new jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address) from Order o" +
                        " join o.member m " +
                        " join o.delivery d ", OrderSimpleQueryDto.class
        ).getResultList();
    }
}
