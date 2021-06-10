package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Address;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity/*테이블에 대응하는 하나의 클래스*/
@DiscriminatorValue("A")
@Getter
@Setter
public class Album extends Item {


    private String artist;
    private String etc;


}
