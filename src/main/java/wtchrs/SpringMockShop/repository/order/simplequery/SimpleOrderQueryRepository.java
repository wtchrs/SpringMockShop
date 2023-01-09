package wtchrs.SpringMockShop.repository.order.simplequery;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SimpleOrderQueryRepository {

    @PersistenceContext
    private final EntityManager em;

    public List<SimpleOrderQuery> findAllAsDto() {
        return em.createQuery(
                     "select new wtchrs.SpringMockShop.repository.order.simplequery.SimpleOrderQuery("
                     + "    o.id, m.username, d.address, o.status, o.orderDateTime"
                     + ") from Order o join o.member m join o.delivery d",
                     SimpleOrderQuery.class)
                 .getResultList();
    }
}
