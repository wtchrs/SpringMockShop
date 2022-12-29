package wtchrs.SpringMockShop.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import wtchrs.SpringMockShop.domain.Address;
import wtchrs.SpringMockShop.domain.Member;
import wtchrs.SpringMockShop.repository.MemberRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired MemberRepository memberRepository;
    @Autowired MemberService memberService;

    @Test
    public void memberJoin() {
        Member member = new Member("member", new Address("Seoul-si", "xxx street", "12345"));

        Long id = memberService.join(member);
        Member findMember = memberService.getMember(id);

        Assertions.assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void duplicateMemberJoin() {
        Member member1 = new Member("member", null);
        Member member2 = new Member("member", null);

        memberService.join(member1);
        assertThrows(IllegalStateException.class, () -> memberService.join(member2));
    }
}