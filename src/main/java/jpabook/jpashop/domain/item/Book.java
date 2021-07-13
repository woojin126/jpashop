package jpabook.jpashop.domain.item;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


@Entity
@DiscriminatorValue("B")
@Getter
@Setter
public class Book extends Item{

    private String author;
    private String isbn;



    public static Book createBookInfo(Long id,String name,int price,int stockQuantity,String author,String isbn){
        Book book = new Book();
        book.setId(id);
        book.setName(name);
        book.setPrice(price);
        book.setAuthor(author);
        book.setStockQuantity(stockQuantity);
        book.setIsbn(isbn);

        return book;

    }
}
