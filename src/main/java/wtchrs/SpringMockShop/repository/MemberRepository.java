package wtchrs.SpringMockShop.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import wtchrs.SpringMockShop.domain.Member;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    @PersistenceContext
    private final EntityManager em;

    public Long save(Member member) {
        em.persist(member);
        return member.getId();
    }

    public Member findById(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    public Member findByName(String name) {
        return em.createQuery("select m from Member m where m.username = :name", Member.class)
                 .setParameter("name", name)
                 .getResultStream().findFirst().orElse(null);
    }
}
