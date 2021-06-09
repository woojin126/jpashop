package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable /*어딘가에 내장이 될수잇다는 뜻*/
@Getter
public class Address {

    private String city;
    private String street;
    private String zipcode;
}
