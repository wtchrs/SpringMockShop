package wtchrs.SpringMockShop.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import wtchrs.SpringMockShop.domain.Address;
import wtchrs.SpringMockShop.domain.Member;
import wtchrs.SpringMockShop.domain.OrderStatus;
import wtchrs.SpringMockShop.domain.item.Book;
import wtchrs.SpringMockShop.domain.item.Item;
import wtchrs.SpringMockShop.exception.NotEnoughStockException;
import wtchrs.SpringMockShop.domain.Order;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired private MemberService memberService;
    @Autowired private ItemService itemService;
    @Autowired private OrderService orderService;

    @Test
    void order() {
        Member member = new Member("member", new Address("Seoul", "My-street", "12345"));
        memberService.join(member);

        int initialStock = 100;

        Item book = new Book("My Book", 10000, initialStock, "", "");
        itemService.addItem(book);

        int orderCount = 10;
        Long orderId = orderService.order(member.getId(), Map.of(book.getId(), orderCount));

        Order result = orderService.getOrder(orderId);
        assertThat(result.getMember()).isEqualTo(member);
        assertThat(result.getDelivery().getAddress()).isEqualTo(member.getAddress());
        assertThat(result.getStatus()).isEqualTo(OrderStatus.ORDER);
        assertThat(result.getTotalPrice()).isEqualTo(book.getPrice() * orderCount);
        assertThat(book.getStockQuantity()).isEqualTo(initialStock - orderCount);
    }

    @Test
    void cancelOrder() {
        Member member = new Member("member", new Address("Seoul", "My-street", "12345"));
        memberService.join(member);

        int initialStock = 100;

        Item book = new Book("My Book", 10000, initialStock, "", "");
        itemService.addItem(book);

        int orderCount = 10;
        Long orderId = orderService.order(member.getId(), Map.of(book.getId(), orderCount));

        Order order = orderService.getOrder(orderId);
        orderService.cancelOrder(orderId);

        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCEL);
        assertThat(book.getStockQuantity()).isEqualTo(initialStock);
    }

    @Test
    public void excessItemStockQuantity() {
        Member member = new Member("member", new Address("Seoul", "My-street", "12345"));
        memberService.join(member);

        int initialStock = 100;

        Item book = new Book("My Book", 10000, initialStock, "", "");
        itemService.addItem(book);

        int orderCount = 101;

        assertThrows(NotEnoughStockException.class,
                     () -> orderService.order(member.getId(), Map.of(book.getId(), orderCount)));
    }
}