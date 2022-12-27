package wtchrs.jpabook.jpashop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wtchrs.jpabook.jpashop.domain.*;
import wtchrs.jpabook.jpashop.domain.item.Item;
import wtchrs.jpabook.jpashop.repository.ItemRepository;
import wtchrs.jpabook.jpashop.repository.MemberRepository;
import wtchrs.jpabook.jpashop.repository.OrderRepository;
import wtchrs.jpabook.jpashop.repository.OrderSearch;

import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public Long order(Long memberId, Map<Long, Integer> itemMap) {
        Member findMember = memberRepository.findById(memberId);

        List<OrderItem> orderItems = itemMap.keySet().stream().map(itemId -> {
            Item item = itemRepository.findById(itemId);
            return OrderItem.createOrderItem(item, item.getPrice(), itemMap.get(itemId));
        }).toList();

        Delivery delivery = new Delivery();
        delivery.setAddress(findMember.getAddress());
        delivery.setStatus(DeliveryStatus.READY);

        Order order = Order.createOrder(findMember, delivery, orderItems);
        orderRepository.save(order);

        return order.getId();
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        Order findOrder = orderRepository.findById(orderId);
        findOrder.cancel();
    }

    public Order getOrder(Long orderId) {
        return orderRepository.findById(orderId);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> searchOrders(OrderSearch orderSearch) {
        return orderRepository.findAllByString(orderSearch);
    }
}
