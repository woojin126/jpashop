package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    /**
     * 엔티티를 직접조회시 문제점.
     * 나는 member정보만 얻고싶은데 orders 의 정보까지 가져오게됨. (그럼 Member 에 orders에다 @JsonIgnore 을붙이면 조회 x)
     * 하지만 ENTITY에 저런 어노테이션이 추가된다면 여러스펙의 API를 요구하게되면 같은 Member 엔티티라고해도
     * 어떤 요구 api는 @JsonIgnore이게 필요할 수도 있고 필요 없을 수 도 있기떄문에 사용안하는게 좋다
     * 중요) 엔티티를 절때 직접 반환하면 안된다.
     * 
     * 해결방안) API 응답 스펙에 맞추어 별도의 DTO를 만들자
     * */
    @GetMapping("/api/v1/members")
    public List<Member> memberV1(){
        return memberService.findMembers();
    }

    //해결 V2
    @GetMapping("/api/v2/members")
    public Result memberV2(){
        List<Member> findMembers = memberService.findMembers();
        List<MemberDto> collect = findMembers.stream().map(m -> new MemberDto(m.getName(),m.getAddress()))
                .collect(Collectors.toList());

        return new Result<>(collect.size(),collect);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> //result 껍데기로 감싸줌 배열형태로나가게되면 더이상 새로운 스펙 추가가불가능
    {
        private int count;
        private T data;
    }

    /**
     * DTO를 만들어서 내가 API스펙이 노출할 필드만 기용하여 사용하면됨!
     */
    @Data
    @AllArgsConstructor
    static class MemberDto{
        private String name;
        private Address address;

    }

    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member){
        Long id = memberService.join(member);

        return new CreateMemberResponse(id);
    }

    /**
     * 위 v1처럼 만들면 엔티티와 직접 1:1 매핑을하게되어 위험하게 될 수 있다. (entity 스펙을건들여야함)
     * 아래 v2 처럼 dto 클래스를만들면 마음대로 지지고 볶고 할수있다( entity 기본스펙을 건드리지 안항도됨)
     * 회원등록 API
     */

    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request){

        Member member = new Member();
        member.setName(request.getName());
        Long id = memberService.join(member);

        return new CreateMemberResponse(id);
    }


    /**
     *회원수정 API
     * */
    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(@PathVariable("id") Long id ,
                                               @RequestBody @Valid UpdateMemberRequest request){

        memberService.update(id,request.getName()); //업데이트에서 member를 반환해도되지만 그렇게 되게된다면 조회의목적도 갖을수있기떄문에 형평성에 안맞는다.
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }

    @Data
    static class UpdateMemberRequest{
        private String name;
    }

    @Data
    static class CreateMemberRequest{

        @NotEmpty
        private String name;
    }
    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse{
        private Long id;
        private String name;
    }



    @Data
    static class CreateMemberResponse{
        private Long id;

        public CreateMemberResponse(Long id){
            this.id = id;
        }
    }
}
