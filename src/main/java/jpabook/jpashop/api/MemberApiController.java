package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

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
