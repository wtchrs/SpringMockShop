package wtchrs.SpringMockShop.repository.order.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import wtchrs.SpringMockShop.domain.Address;
import wtchrs.SpringMockShop.domain.Order;
import wtchrs.SpringMockShop.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(of = "orderId")
@AllArgsConstructor
public class OrderQueryDto {

    private Long orderId;
    private String username;
    private Address address;
    private OrderStatus status;
    private LocalDateTime orderDateTime;
    private List<OrderItemDto> orderItems;

    public OrderQueryDto(Long orderId, String username, Address address, OrderStatus status,
                         LocalDateTime orderDateTime) {
        this.orderId = orderId;
        this.username = username;
        this.address = address;
        this.status = status;
        this.orderDateTime = orderDateTime;
    }

    public static OrderQueryDto of(Order order) {
        List<OrderItemDto> orderItemDtoList = order.getOrderItems().stream().map(OrderItemDto::of).toList();
        return new OrderQueryDto(order.getId(), order.getMember().getUsername(), order.getDelivery().getAddress(),
                                 order.getStatus(), order.getOrderDateTime(), orderItemDtoList);
    }
}
