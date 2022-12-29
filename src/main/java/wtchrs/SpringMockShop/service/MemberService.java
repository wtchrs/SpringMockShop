package wtchrs.SpringMockShop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wtchrs.SpringMockShop.domain.Address;
import wtchrs.SpringMockShop.domain.Member;
import wtchrs.SpringMockShop.repository.MemberRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByName(member.getUsername());
        if (findMember != null) throw new IllegalStateException("Duplicate username");
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Member getMember(Long id) {
        return memberRepository.findById(id);
    }

    @Transactional
    public void update(Long memberId, String username, Address address) {
        Member findMember = memberRepository.findById(memberId);
        findMember.setUsername(username);
        if (address != null) findMember.setAddress(address);
    }
}
