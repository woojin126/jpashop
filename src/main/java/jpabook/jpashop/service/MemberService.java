package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor //final이있는 필드에대한 생성자를 만드러줌
@Transactional(readOnly = true)//데이터변경에는 항상 트랜잭션이잇어야함 lazy 이런게 인식됨,아래메서드에 다 적용됨
public class MemberService {

    private final MemberRepository memberRepository;

/*    @Autowired 생략가능 @require~~덕에
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }*/

    /**
     * 회원가입
     * */
    @Transactional //전체가 readOnly= true니  기본값인 삽입이일어나는곳만 false로 설정
    public Long join(Member member){
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        //EXCEPTION
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }
    //회원 전체 조회
    //@Transactional(readOnly = true) //이렇ㄱ ㅔ직접설정해도됨 조회같은곳에 readOnly를 붙이면 성능 최적화됨
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }

    @Transactional
    public void update(Long id, String name) {
        Member member = memberRepository.findOne(id);
        member.setName(name);
    }
}
