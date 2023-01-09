package wtchrs.SpringMockShop.repository.order.simplequery;

import lombok.AllArgsConstructor;
import lombok.Data;
import wtchrs.SpringMockShop.domain.Address;
import wtchrs.SpringMockShop.domain.Order;
import wtchrs.SpringMockShop.domain.OrderStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class SimpleOrderQuery {
    private Long orderId;
    private String username;
    private Address address;
    private OrderStatus status;
    private LocalDateTime orderDateTime;

    public static SimpleOrderQuery of(Order order) {
        return new SimpleOrderQuery(order.getId(), order.getMember().getUsername(), order.getDelivery().getAddress(),
                                    order.getStatus(), order.getOrderDateTime());
    }
}
