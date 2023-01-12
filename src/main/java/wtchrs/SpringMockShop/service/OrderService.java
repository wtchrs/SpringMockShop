package wtchrs.SpringMockShop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wtchrs.SpringMockShop.domain.*;
import wtchrs.SpringMockShop.domain.item.Item;
import wtchrs.SpringMockShop.repository.ItemRepository;
import wtchrs.SpringMockShop.repository.MemberRepository;
import wtchrs.SpringMockShop.repository.OrderRepository;
import wtchrs.SpringMockShop.repository.OrderSearch;
import wtchrs.SpringMockShop.repository.order.simplequery.SimpleOrderQuery;
import wtchrs.SpringMockShop.repository.order.simplequery.SimpleOrderQueryRepository;

import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final SimpleOrderQueryRepository simpleOrderQueryRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public Long order(Long memberId, Map<Long, Integer> itemMap) {
        Member findMember = memberRepository.findById(memberId);

        List<OrderItem> orderItems = itemMap.keySet().stream().map(itemId -> {
            Item item = itemRepository.findById(itemId);
            return OrderItem.createOrderItem(item, item.getPrice(), itemMap.get(itemId));
        }).toList();

        Delivery delivery = new Delivery(findMember.getAddress());
        delivery.initStatus();

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

    public List<Order> getAllWithMemberDelivery() {
        return orderRepository.findAllWithMemberDelivery();
    }

    public List<Order> getAllWithMemberDelivery(int offset, int limit) {
        return orderRepository.findAllWithMemberDelivery(offset, limit);
    }

    public List<Order> searchOrders(OrderSearch orderSearch) {
        return orderRepository.findAllByString(orderSearch);
    }

    public List<SimpleOrderQuery> getAllOrderDto() {
        return simpleOrderQueryRepository.findAllAsDto();
    }

    public List<Order> getAllWithItems() {
        return orderRepository.findAllWithItems();
    }
}
