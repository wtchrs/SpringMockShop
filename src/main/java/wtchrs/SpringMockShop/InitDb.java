package wtchrs.SpringMockShop;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import wtchrs.SpringMockShop.domain.*;
import wtchrs.SpringMockShop.domain.item.Book;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        @PersistenceContext
        private final EntityManager em;

        public void dbInit1() {
            Book jpaBook1 = new Book("JPA book 1", 10000, 100, "Author", "1111111111111111");
            Book jpaBook2 = new Book("JPA book 2", 7000, 50, "Author", "1111111122222222");
            Book springBook1 = new Book("Spring book 1", 15000, 100, "Author", "2222222222222222");
            Book springBook2 = new Book("Spring book 2", 12000, 50, "Author", "2222222233333333");

            em.persist(jpaBook1);
            em.persist(jpaBook2);
            em.persist(springBook1);
            em.persist(springBook2);

            Member member1 = new Member("userA", new Address("Seoul", "A street", "12345"));
            Member member2 = new Member("userB", new Address("Seoul", "B street", "54321"));

            em.persist(member1);
            em.persist(member2);

            OrderItem orderItem1 = OrderItem.createOrderItem(jpaBook1, jpaBook1.getPrice(), 5);
            OrderItem orderItem2 = OrderItem.createOrderItem(jpaBook2, jpaBook2.getPrice(), 5);

            Order order1 = Order.createOrder(member1, new Delivery(member1.getAddress()),
                                             List.of(orderItem1, orderItem2));

            OrderItem orderItem3 = OrderItem.createOrderItem(springBook1, springBook1.getPrice(), 5);
            OrderItem orderItem4 = OrderItem.createOrderItem(springBook2, springBook2.getPrice(), 5);

            Order order2 = Order.createOrder(member2, new Delivery(member2.getAddress()),
                                             List.of(orderItem3, orderItem4));

            em.persist(order1);
            em.persist(order2);
        }
    }
}
