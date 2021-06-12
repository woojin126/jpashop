package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name; //카테고리명

    @ManyToMany/*실무에서 안쓰지만 보여주기위해 */
    @JoinTable(name = "category_item",
            joinColumns = @JoinColumn(name = "category_id"),//중간테이블인 category_item 테이블의 category_id
            inverseJoinColumns = @JoinColumn(name = "item_id"))//category_item 테이블에 item쪽으로 들어가는 필드들
    /*중간테이블 매핑이 필요 관계형디비는
     1대 다 다대 1로 풀어내는 중간테이블이 필요하기떄문*/
    private List<Item> items = new ArrayList<>();//다대 다 관계 사용할ㅇ리없지만 예제상 보여줌

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();

    //==연관관계 메서드==//
    public void addChildCategory(Category child){
        this.child.add(child);//부모 -> 자식
        child.setParent(this);//자식 -> 부모

    }
}
