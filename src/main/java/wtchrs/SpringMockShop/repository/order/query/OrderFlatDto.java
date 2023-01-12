package wtchrs.SpringMockShop.repository.order.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import wtchrs.SpringMockShop.domain.Address;
import wtchrs.SpringMockShop.domain.OrderStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class OrderFlatDto {

    private Long orderId;
    private String username;
    private Address address;
    private OrderStatus status;
    private LocalDateTime orderDateTime;

    private String itemName;
    private int orderPrice;
    private int orderCount;
}
