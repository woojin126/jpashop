package jpabook.jpashop.domain;


import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;//주문가격
    private int count;//주문수량

    /*protected OrderItem(){ //다른데서객체생성 하지말아라 메서드형식으로 만들어라
    ===NoArgsConstructor 어노테이션 대체가능
    }*/
    //==생성메서드==//
    public static OrderItem createOrderItem(Item item,int orderPrice,int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count); //주문을햇으니 재고가 빠져야겟지?
        return orderItem;
    }
    //==비지니스 로직==//
    public void cancel() {
        getItem().addStock(count);//주문 취소한만큼 재고수량을 원복
    }

    /**
     * 주문 상품 전체 가격 조회
     * */
    //==조회 로직==//
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}
