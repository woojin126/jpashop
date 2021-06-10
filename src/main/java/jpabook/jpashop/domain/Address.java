package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable /*어딘가에 내장이 될수잇다는 뜻*/
@Getter
public class Address {  /*값 타입은 setter를 제공안하고 생성할때만 */
    private String city;
    private String street;
    private String zipcode;


    protected Address() {

    }
    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
