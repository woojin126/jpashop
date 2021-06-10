package jpabook.jpashop;

import jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class MemberRepository {

    /*
    * @PersistenceContext
    * 1.논리적인 개념으로, 눈에 보이지 않는다.
    * 2.Entity를 영구 저장하는 환경 이라는뜻
    *  em.persist(member); 동작의 뜻
    * 실제로는 DB에 저장하는 것이 아니라 영속성 컨테스트를 통해서 Entity를 영속화 한다는 뜻.
    * 정확히는 persist() 시점에는 Entity를 영속성 컨테스트에 저장하는 것.
    * 엔티티 매니저를 생성할 때 하나 만들어짐
    * 엔티티 매니저를통해서 영속성 컨텍스트에 접근하고 관리할 수 있다.
    *
    * 영속성 컨텍스트가 엔티티를 관리할시 장점
    * 1.1차 캐시 (영속성 컨텍스트 내부에는 캐시가 있는데 이를 1차캐시, 영속상태 엔티티를 여기에저장,
    * 1차 캐시의 키는 식별자 값 데이터베이스 원본 키 )
    * 2.동일성 보장
    * 3.트랜잭션을 지원하는 쓰기 지연
    * 4.변경 감지
    * 5.지연 로딩
    *  */
    @PersistenceContext
    private EntityManager em;

    public Long save(Member member){
        em.persist(member);
        return member.getId();
    }

    public Member find(Long id){
        return em.find(Member.class,id);//한건조회
    }
}
