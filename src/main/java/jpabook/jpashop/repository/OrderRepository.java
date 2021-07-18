package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order){
        em.persist(order);
    }

    public Order findOne(Long id){
        return em.find(Order.class,id);
    }

    public List<Order> findAllByCriteria(OrderSearch orderSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Order, Member> m = o.join("member", JoinType.INNER); //회원과 조인
        List<Predicate> criteria = new ArrayList<>();
//주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"),
                    orderSearch.getOrderStatus());
            criteria.add(status);
        }
//회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name =
                    cb.like(m.<String>get("name"), "%" +
                            orderSearch.getMemberName() + "%");
            criteria.add(name);
        }
        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000); //최대1000건
        return query.getResultList();
    }

    public List<Order> findAllWithMemberDelivery(int offset, int limit) {
        return em.createQuery(
                " select o from Order o" +
                        " join fetch o.member m " +
                        " join fetch o.delivery d ", Order.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();


    }

    public List<Order> findAllWithItem() {
        /**
         *  아래 쿼리 위부터 3줄은 데이터 뻥튀기가 없어,
         *  그런데 4,5,번줄 은 오더는 2개인데 오더아이템은 4개지? 데이터뻥튀기 느낌오지?
         *  (1 대 다 라그럼),
         *  [nio-8080-exec-1] o.h.h.internal.ast.QueryTranslatorImpl   : HHH000104: firstResult/maxResults specified with collection fetch; applying in memory!
         *  일대다 조인, 페치조인하는순간 우리가원했던 결과가 다틀어짐 order 2 -> 4 된거처럼
         *  이유? 생각해보자
         *  일대다 조인한순간 데이터뻥튀기 , 2개의 데이터가 -> 4개로 조회가됬다.
         *  여기다 페이징을 적용해보자. 난 Order 데이터 2개중 1개만가져 오고싶은데
         *  값은 4개로 뻥튀기가되어있고, 원하지 않는 값을 페이징하게 될 수도 있다는것
         *  (이게다 orderItem 떄문 )
         *  
         *  컬렉션 패치조인은 1개만사용가능, 일대 다도 뻥튀기발생하는데 일대 다대 다를 하면? 망함
         **/
        /*     .setFirstResult(0)
                .setMaxResults(100)*/
        return em.createQuery(
                "select o from Order o " +
                        " join fetch o.member m " +
                        " join fetch o.delivery d " +
                        " join fetch  o.orderItems oi " +
                        " join fetch oi.item i " , Order.class)
                .getResultList();
    }


}
