package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item){
        if(item.getId() == null){ //아이템은 jpa에 저장하기전까지 값이없다 (새로생성하는 객체라는것)
            em.persist(item); //신규등록
        }else{
            em.merge(item);//어딘가에 db에 등록된것을 가져온다는 것(업데이틑아니어도 비슷한의미)
        }
    }

    public Item findOne(Long id){
        return em.find(Item.class,id);
    }

    public List<Item> findAll(){
        return em.createQuery("select i from Item i",Item.class)
                .getResultList();
    }
}
