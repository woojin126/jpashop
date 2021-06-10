package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")/*연관 관게의 주인으로 설정*/
    private Member member; /*주문 회원에대한 정보 맵핑*/

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    //Order을 퍼시스트하면 자동으로 그아래 orderItems도 자동 퍼시스트해줌(원래형식이라면 일일히 다해줘야함 퍼시스트)ㄴ
    private List<OrderItem> orderItems = new ArrayList<>();
/*
  cascade가없으면 일일히 다해줘야함
    persist(orderItemA)
    persist(orderItemB)
    persist(orderItemC)
    persist(order)
--------------------------------------------
    cascade가있으면 아래만하면 위에가 알아서다됨
    persist(order)
*/

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)//오더를저장하려할떄 Order를 퍼시스트하면 delivery도 자동으로 저장(퍼시스트)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문상태 [ORDER,CANCEL]


    //==연관관계 편의 메서드(양방향관계),DB는 주인키만 데이터가 보이지만 객체끼리는 둘다보이도록==//
    public void setMember(Member member) {//Order의 입장에서
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery){
        this.delivery=delivery;
        delivery.setOrder(this);
    }

    public static void main(String[] args) {
        Member member = new Member();
        Order order = new Order();

        order.setMember(member);

    }

}


