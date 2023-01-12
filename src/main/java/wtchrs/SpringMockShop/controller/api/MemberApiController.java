package wtchrs.SpringMockShop.controller.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import wtchrs.SpringMockShop.domain.Address;
import wtchrs.SpringMockShop.domain.Member;
import wtchrs.SpringMockShop.repository.LookupResult;
import wtchrs.SpringMockShop.service.MemberService;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    /**
     * When some member has order information, it happens stack overflow with circular dependency of Member and Order.
     */
    @GetMapping("/api/v1/members")
    public List<Member> lookupMembersV1() {
        return memberService.getAllMembers();
    }

    @GetMapping("/api/v2/members")
    public LookupResult<MemberLookup> lookupMembersV2() {
        List<MemberLookup> memberLookups = memberService.getAllMembers().stream().map(MemberLookup::of).toList();
        return new LookupResult<>(memberLookups);
    }

    /**
     * Member entity is used as the request body. When the entity spec is changed, the api spec is also changed. It may
     * cause disability.
     */
    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Validated Member member) {
        Long resultId = memberService.join(member);
        return new CreateMemberResponse(resultId);
    }

    /**
     * Even if the Member entity changes, the api spec does not change.
     */
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Validated CreateMemberRequest request) {
        Long resultId = memberService.join(new Member(request.name, request.address));
        return new CreateMemberResponse(resultId);
    }

    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(@PathVariable("id") Long memberId,
                                               @RequestBody @Validated UpdateMemberRequest request) {
        memberService.update(memberId, request.name, request.address);
        Member member = memberService.getMember(memberId);
        UpdateMemberResponse response = new UpdateMemberResponse(memberId, member.getUsername());
        if (request.address != null) response.setAddress(member.getAddress());
        return response;
    }

    @Data
    @AllArgsConstructor
    private static class MemberLookup {
        private Long id;
        private String name;
        private Address address;

        public static MemberLookup of(Member member) {
            return new MemberLookup(member.getId(), member.getUsername(), member.getAddress());
        }
    }

    @Data
    private static class CreateMemberRequest {
        @NotEmpty
        private String name;
        private Address address;
    }

    @Data
    @AllArgsConstructor
    private static class CreateMemberResponse {
        private Long id;
    }

    @Data
    private static class UpdateMemberRequest {
        @NotEmpty
        private String name;
        private Address address;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private static class UpdateMemberResponse {
        private Long id;
        private String name;
        private Address address;

        public UpdateMemberResponse(Long id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}
