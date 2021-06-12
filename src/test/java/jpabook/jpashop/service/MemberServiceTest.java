package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class) //junit 쓸떄 스프링이랑 같이 섞어서하겠다라는것
@SpringBootTest//스프링컨테이너안에서 테스트를 돌리겠다.
@Transactional //기본적으로 테스트가끝나면 롤백을해버리기떄문에 insert문이 안보임(테스트에서만 롤백하는것)
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em; //insert 과정을 보고싶으면 flush 이용

    @Test
    @Rollback(value = false)//눈으로 디비에드가는걸 봐야겠다 싶으면 롤백false
    public void 회원가입() throws Exception{

        //given
        Member member = new Member();
        member.setName("kim");
        //when

        Long join = memberService.join(member);
        //then
        em.flush();
        /*Transaction commit 이 일어날 때 flush가 동작하는데,
        이때 쓰기 지연 저장소에 쌓아 놨던 INSERT, UPDATE, DELETE SQL들이 DB에 날라간다.
        영속성 컨텍스트의 변경 내용을 DB 에 반영하는 것을 말한다. */
        Assert.assertEquals(member,memberRepository.findOne(join));

    }


    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception{

        //given
        Member member1 = new Member();
        member1.setName("김");

        Member member2 = new Member();
        member2.setName("김");
        //when
        memberService.join(member1);
        memberService.join(member2);
   /*    try { 위에 expect에적으면 안적어도됨
             memberService.join(member2);
        } catch (IllegalStateException e){
            return;
        }*/
        //then
        fail("예외가 발생해야 한다.");
    }
}