package wtchrs.SpringMockShop.repository.order.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import wtchrs.SpringMockShop.domain.OrderItem;

@Data
@AllArgsConstructor
public class OrderItemDto {

    private String itemName;
    private int orderPrice;
    private int orderCount;

    @JsonIgnore
    private Long orderId;

    public OrderItemDto(String itemName, int orderPrice, int orderCount) {
        this.itemName = itemName;
        this.orderPrice = orderPrice;
        this.orderCount = orderCount;
    }

    public static OrderItemDto of(OrderItem orderItem) {
        return new OrderItemDto(orderItem.getItem().getName(), orderItem.getOrderPrice(), orderItem.getCount());
    }
}
