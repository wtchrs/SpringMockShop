package wtchrs.SpringMockShop;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import wtchrs.SpringMockShop.domain.*;
import wtchrs.SpringMockShop.domain.item.Book;
import wtchrs.SpringMockShop.service.ItemService;
import wtchrs.SpringMockShop.service.MemberService;
import wtchrs.SpringMockShop.service.OrderService;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

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

        private final MemberService memberService;
        private final ItemService itemService;
        private final OrderService orderService;

        public void dbInit1() {
            Book jpaBook1 = new Book("JPA book 1", 10000, 100, "Author", "1111111111111111");
            Book jpaBook2 = new Book("JPA book 2", 7000, 50, "Author", "1111111122222222");
            Book springBook1 = new Book("Spring book 1", 15000, 100, "Author", "2222222222222222");
            Book springBook2 = new Book("Spring book 2", 12000, 50, "Author", "2222222233333333");

            itemService.addItem(jpaBook1);
            itemService.addItem(jpaBook2);
            itemService.addItem(springBook1);
            itemService.addItem(springBook2);

            Member member1 = new Member("userA", new Address("Seoul", "A street", "12345"));
            Member member2 = new Member("userB", new Address("Seoul", "B street", "54321"));

            memberService.join(member1);
            memberService.join(member2);

            orderService.order(member1.getId(), Map.of(jpaBook1.getId(), 6, jpaBook2.getId(), 5));
            orderService.order(member2.getId(), Map.of(springBook1.getId(), 10, springBook2.getId(), 9));
        }
    }
}
