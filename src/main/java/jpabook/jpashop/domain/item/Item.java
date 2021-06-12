package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) /*SINGLE_TABLE은 한테이블에 다떄려박음 , JOINED는 가장 정규화스타일, */
@DiscriminatorColumn(name = "dtype") /*만약 book B 이면어떻게할래? 저장할떄 db 구분을위해*/
@Getter @Setter
public class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    //==비지니스로직== 해당 클래스안에 있는 로직을 처리해야한다면 해당클래스안에서 처리한는것이 객체지향적인것//

    /**
     * 재고 증가로직
     * 세터를 쓰지말아라 변경할일 있으면 메서드를 만들어라
     * */
    public void addStock(int quantity){
        this.stockQuantity += quantity;
    }

    /**
     * 재고 감소 로직
     */
    public void removeStock(int quantity){
        int resStock = this.stockQuantity - quantity;
        if(resStock < 0){
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = resStock;
    }
}
