package wtchrs.SpringMockShop.repository.order.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    @PersistenceContext
    private final EntityManager em;

    public List<OrderQueryDto> findAllOrderAsDto() {
        List<OrderQueryDto> result = findOrdersAsDto();
        for (OrderQueryDto orderQueryDto : result) {
            List<OrderItemDto> orderItems = findOrderItemAsDto(orderQueryDto.getOrderId());
            orderQueryDto.setOrderItems(orderItems);
        }

        return result;
    }

    public List<OrderQueryDto> findAllOrderAsDtoOptimized() {
        List<OrderQueryDto> result = findOrdersAsDto();

        List<Long> orderIds = result.stream().map(OrderQueryDto::getOrderId).toList();
        List<OrderItemDto> orderItems = findOrderItemsAsDtoByOrderIds(orderIds);

        Map<Long, List<OrderItemDto>> orderItemMap =
            orderItems.stream().collect(Collectors.groupingBy(OrderItemDto::getOrderId));

        for (OrderQueryDto orderQueryDto : result) {
            orderQueryDto.setOrderItems(orderItemMap.get(orderQueryDto.getOrderId()));
        }

        return result;
    }

    public List<OrderFlatDto> findAllOrderAsDtoFlat() {
        return em.createQuery("select new wtchrs.SpringMockShop.repository.order.query.OrderFlatDto("
                              + "o.id, m.username, d.address, o.status, o.orderDateTime, i.name, oi.orderPrice, oi.count"
                              + ") from Order o"
                              + " join o.member m join o.delivery d join o.orderItems oi join oi.item i",
                              OrderFlatDto.class)
                 .getResultList();
    }

    private List<OrderQueryDto> findOrdersAsDto() {
        return em.createQuery("select new wtchrs.SpringMockShop.repository.order.query.OrderQueryDto("
                              + "o.id, m.username, d.address, o.status, o.orderDateTime"
                              + ") from Order o"
                              + " join o.member m join o.delivery d",
                              OrderQueryDto.class)
                 .getResultList();
    }

    private List<OrderItemDto> findOrderItemAsDto(Long orderId) {
        return em.createQuery("select new wtchrs.SpringMockShop.repository.order.query.OrderItemDto("
                              + "   i.name, oi.orderPrice, oi.count, oi.order.id"
                              + ") from OrderItem oi"
                              + " join oi.item i"
                              + " where oi.order.id = :orderId",
                              OrderItemDto.class)
                 .setParameter("orderId", orderId)
                 .getResultList();
    }

    private List<OrderItemDto> findOrderItemsAsDtoByOrderIds(List<Long> orderIds) {
        return em.createQuery("select new wtchrs.SpringMockShop.repository.order.query.OrderItemDto("
                              + "i.name, oi.orderPrice, oi.count, oi.order.id"
                              + ") from OrderItem oi"
                              + " join oi.item i"
                              + " where oi.order.id in :orderIds",
                              OrderItemDto.class)
                 .setParameter("orderIds", orderIds)
                 .getResultList();
    }
}
