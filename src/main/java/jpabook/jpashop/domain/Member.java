package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member{
    /*식별자 아이디에 맵핑(primary기능), primary 주키의 값을위한 자동생성*/
    /*제너레이트 속성
    * 1. AUTO : (persistence provider가) 특정 DB에 맞게 자동 선택
      2. IDENTITY : DB의 identity 컬럼을 이용
      3. SEQUENCE : DB의 시퀀스 컬럼을 이용
      4. TABLE : 유일성이 보장된 데이터베이스 테이블을 이용
*/
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded
    private Address address;


    /*거울이기때문에 여기값을 바꾼다고 order에 member 값이 바뀌지않음*/
    @OneToMany(mappedBy = "member") /*오더테이블에 member 필드에의해 맵핑 거울일뿐 */
    private List<Order> orders = new ArrayList<>(); //초기화한것은 가급적이면 건들이지말아라 set이나 그런걸로 바꾸면
                                                    //하이버네이트가이미 감싸놓은 값이 엉망이됨
}
