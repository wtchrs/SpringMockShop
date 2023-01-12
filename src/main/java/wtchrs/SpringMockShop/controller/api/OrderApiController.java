package wtchrs.SpringMockShop.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wtchrs.SpringMockShop.domain.Order;
import wtchrs.SpringMockShop.repository.LookupResult;
import wtchrs.SpringMockShop.repository.order.query.OrderFlatDto;
import wtchrs.SpringMockShop.repository.order.query.OrderItemDto;
import wtchrs.SpringMockShop.repository.order.query.OrderQueryDto;
import wtchrs.SpringMockShop.repository.order.query.OrderQueryRepository;
import wtchrs.SpringMockShop.service.OrderService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Deal with the collection lookup(XToMany relationships). Order -> OrderItem (OneToMany)
 */
@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderService orderService;
    private final OrderQueryRepository orderQueryRepository;

    /**
     * It exposes the entity structure directly. It is better to expose a DTO class.
     *
     * @return all orders and related entity instances.
     */
    @GetMapping("/api/v1/orders")
    public LookupResult<Order> ordersV1() {
        List<Order> orders = orderService.getAllOrders();
        for (Order order : orders) {
            order.getMember().getUsername();
            order.getDelivery().getAddress();
            order.getOrderItems().stream().forEach(orderItem -> orderItem.getItem().getName());
        }
        return new LookupResult<>(orders);
    }

    /**
     * Converts the entity instances to DTO instances and returns them. It has the N+1 problem.
     *
     * @return all orders and related entity data as DTO instances.
     */
    @GetMapping("/api/v2/orders")
    public LookupResult<OrderQueryDto> orderV2() {
        List<Order> orders = orderService.getAllOrders();
        return new LookupResult<>(orders.stream().map(OrderQueryDto::of).toList());
    }

    /**
     * Converts the entity instances to DTO instances and returns them. Same code as
     * {@link OrderApiController#orderV2()}, except that it calls {@link OrderService#getAllWithItems()} instead of
     * {@link OrderService#getAllOrders()}. The method {@link OrderService#getAllWithItems()} uses JPQL fetch join to
     * reduce the number of queries sent. Like this, if you separated the controller layers from the service or
     * repository layers, you don't need to make change in controller layers when the logic in the service layer or the
     * repository layer has changed. However, it is hard to apply paging in fetch joined JPQL at collections of
     * entities.
     *
     * @return all orders and related entity data as DTO instances.
     * @see OrderApiController#orderV3Paging(int, int)
     */
    @GetMapping("/api/v3/orders")
    public LookupResult<OrderQueryDto> orderV3() {
        List<Order> orders = orderService.getAllWithItems();
        return new LookupResult<>(orders.stream().map(OrderQueryDto::of).toList());
    }

    /**
     * Converts the entity instances to DTO instances and returns them. It is similar to
     * {@link OrderApiController#orderV2()} except for that it performs a lookup with paging. You can set the
     * {@code spring.jpa.properties.hibernate.default_batch_fetch_size} option at {@code application.yml} or apply
     * {@link org.hibernate.annotations.BatchSize} at the fields of the entity collections to some integer value to
     * improve performance of the additional fetch queries.
     *
     * @param offset Offset of page.
     * @param limit  Limit of page.
     * @return all orders and related entity data as DTO instances.
     */
    @GetMapping("/api/v3.1/orders")
    public LookupResult<OrderQueryDto> orderV3Paging(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                                     @RequestParam(value = "limit", defaultValue = "100") int limit) {
        List<Order> orders = orderService.getAllWithMemberDelivery(offset, limit);
        return new LookupResult<>(orders.stream().map(OrderQueryDto::of).toList());
    }

    /**
     * Get the DTO instances directly by using {@link OrderQueryRepository#findAllOrderAsDto()} and return the lookup
     * result. It has N+1 problem.
     *
     * @return all orders and related entity data as DTO instances.
     */
    @GetMapping("/api/v4/orders")
    public LookupResult<OrderQueryDto> orderV4() {
        return new LookupResult<>(orderQueryRepository.findAllOrderAsDto());
    }

    /**
     * Get the DTO instances directly by using {@link OrderQueryRepository#findAllOrderAsDto()} and return the lookup
     * result. It creates total 2 queries (one for root, other for collection).
     *
     * @return all orders and related entity data as DTO instances.
     */
    @GetMapping("/api/v5/orders")
    public LookupResult<OrderQueryDto> orderV5() {
        return new LookupResult<>(orderQueryRepository.findAllOrderAsDtoOptimized());
    }

    /**
     * Get the {@link OrderFlatDto} instances directly by using {@link OrderQueryRepository#findAllOrderAsDtoFlat()},
     * convert them to {@link OrderQueryDto}, and return the result. It creates the only one query. It uses JPQL join
     * expressions for the collection lookup to reduce the number of queries. Therefore, the rows increase up to the
     * number of OrderItems, and may contain duplicate data. If there are many duplicate data, it may be slower than
     * {@link OrderApiController#orderV5()}. And as it uses the collection join, it is hard to apply paging.
     *
     * @return all orders and related entity data as DTO instances.
     */
    @GetMapping("/api/v6/orders")
    public LookupResult<OrderQueryDto> orderV6() {
        List<OrderFlatDto> flats = orderQueryRepository.findAllOrderAsDtoFlat();
        List<OrderQueryDto> resultList =
            flats.stream()
                 .collect(Collectors.groupingBy(
                     flat -> new OrderQueryDto(flat.getOrderId(), flat.getUsername(), flat.getAddress(),
                                               flat.getStatus(), flat.getOrderDateTime()),
                     Collectors.mapping(flat -> new OrderItemDto(flat.getItemName(), flat.getOrderPrice(),
                                                                 flat.getOrderCount()),
                                        Collectors.toList()))
                 ).entrySet().stream().map(entry -> {
                     OrderQueryDto result = entry.getKey();
                     result.setOrderItems(entry.getValue());
                     return result;
                 }).toList();
        return new LookupResult<>(resultList);
    }
}
