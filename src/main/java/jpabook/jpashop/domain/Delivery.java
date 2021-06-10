package jpabook.jpashop.domain;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.DeiliveryStatus;
import jpabook.jpashop.domain.Order;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;
    /*모든 연관 관계는 지연로딩으로 설정해라 꼭  one으로 끝나는것들은 기본이 eager*/
    @OneToOne(mappedBy = "delivery",fetch = FetchType.LAZY)
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)/*이넘타입 ORDINAL은 숫자로들어감 절떄쓰지말아라  순서밀린다 장애남 */
    private DeiliveryStatus status;//READY,COMP
}
