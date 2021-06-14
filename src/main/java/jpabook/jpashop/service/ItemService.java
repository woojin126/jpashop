package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional//이걸안쓰면 위에 전체값이 리드온리라 저장이안됨 == false 라는것
    public void saveItem(Item item){
        itemRepository.save(item);
    }

    /*Long itemId,*/
    @Transactional
    public void updateItem(Book book){
        Item findItem = itemRepository.findOne(book.getId());
        findItem.setName(book.getName());
        findItem.setPrice(book.getPrice());
        findItem.setStockQuantity(book.getStockQuantity());
        /*이런식으로 set으로 하기보다는 의미있는 메서드를 만들어서 사용하자*/
    }

    //조회
    public List<Item> findItems(){
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId){
        return itemRepository.findOne(itemId);
    }
}
