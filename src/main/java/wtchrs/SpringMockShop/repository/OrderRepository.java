package wtchrs.SpringMockShop.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import wtchrs.SpringMockShop.domain.Member;
import wtchrs.SpringMockShop.domain.Order;
import wtchrs.SpringMockShop.domain.OrderStatus;
import wtchrs.SpringMockShop.repository.order.simplequery.SimpleOrderQuery;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    @PersistenceContext
    private final EntityManager em;

    public Long save(Order order) {
        em.persist(order);
        return order.getId();
    }

    public Order findById(Long id) {
        return em.find(Order.class, id);
    }

    public List<Order> findByMember(Member member) {
        return em.createQuery("select o from Order o where o.member = :member", Order.class)
                 .setParameter("member", member).getResultList();
    }

    public List<Order> findByStatus(OrderStatus status) {
        return em.createQuery("select o from Order o where o.status = :status", Order.class)
                 .setParameter("status", status).getResultList();
    }

    public List<Order> findAll() {
        return em.createQuery("select o from Order o", Order.class).getResultList();
    }

    public List<Order> findAllByString(OrderSearch orderSearch) {
        String qlString = "select o from Order o join o.member m";
        boolean firstCondition = true;

        if (StringUtils.hasText(orderSearch.getUsername())) {
            firstCondition = false;
            qlString += " where m.username like :username";
        }

        if (orderSearch.getStatus() != null) {
            if (firstCondition) {
                qlString += " where";
            } else {
                qlString += " and";
            }
            qlString += " o.status = :status";
        }

        TypedQuery<Order> query = em.createQuery(qlString, Order.class);

        if (StringUtils.hasText(orderSearch.getUsername())) {
            query.setParameter("username", orderSearch.getUsername());
        }

        if (orderSearch.getStatus() != null) {
            query.setParameter("status", orderSearch.getStatus());
        }

        return query.setMaxResults(1000).getResultList();
    }

    public List<Order> findAllByCriteria(OrderSearch orderSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cbQuery = cb.createQuery(Order.class);
        Root<Order> order = cbQuery.from(Order.class);
        Join<Object, Object> member = order.join("member", JoinType.INNER);

        List<Predicate> criteria = new ArrayList<>();

        if (orderSearch.getStatus() != null) {
            criteria.add(cb.equal(order.get("status"), orderSearch.getStatus()));
        }
        if (StringUtils.hasText(orderSearch.getUsername())) {
            criteria.add(cb.like(member.get("username"), "%" + orderSearch.getUsername() + "%"));
        }

        cbQuery.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cbQuery).setMaxResults(1000);

        return query.getResultList();
    }

    public List<Order> findAllWithMemberDelivery() {
        return em.createQuery("select o from Order o join fetch o.member m join fetch o.delivery d", Order.class)
                 .getResultList();
    }

    public List<Order> findAllWithMemberDelivery(int offset, int limit) {
        return em.createQuery("select o from Order o join fetch o.member m join fetch o.delivery d", Order.class)
                 .setFirstResult(offset).setMaxResults(limit)
                 .getResultList();
    }

    public List<Order> findAllWithItems() {
        return em.createQuery("select distinct o from Order o join fetch o.member join fetch o.delivery"
                              + " join fetch o.orderItems oi join fetch oi.item",
                              Order.class)
                 .getResultList();
    }
}
