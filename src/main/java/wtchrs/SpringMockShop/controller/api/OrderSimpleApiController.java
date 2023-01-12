package wtchrs.SpringMockShop.controller.api;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import wtchrs.SpringMockShop.domain.Order;
import wtchrs.SpringMockShop.repository.LookupResult;
import wtchrs.SpringMockShop.repository.order.simplequery.SimpleOrderQuery;
import wtchrs.SpringMockShop.service.OrderService;

import java.util.List;

/**
 * Deal with XToOne relationships.
 * Order -> Member (ManyToOne), Order -> Delivery (OneToOne)
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderService orderService;

    /**
     * Enable replacing hibernate proxies in the entity instance to null when it converted to json.
     *
     * @return hibernate5 module setting.
     */
    @Bean
    public Hibernate5Module hibernate5Module() {
        return new Hibernate5Module();
    }

    /**
     * Return all orders. When converting the result to json, it may fail with stack overflow because of the
     * bidirectional relationships. To avoid it, add {@link com.fasterxml.jackson.annotation.JsonIgnore} annotation at
     * the fields of bidirectional relationships in referenced entity classes. It has N+1 problem when lazy loading.
     *
     * @return A list of orders. Do not expose an entity as an api. It is better to return a wrapper class of the list
     * with DTO instances.
     * @see OrderSimpleApiController#hibernate5Module()
     */
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> orders = orderService.getAllOrders();
        for (Order order : orders) {
            order.getMember().getUsername(); // Lazy loading
            order.getDelivery().getAddress();
        }

        return orders;
    }

    /**
     * Return all orders. It has N+1 problem when lazy loading.
     *
     * @return A list of DTO instances({@link SimpleOrderQuery}) of order. It is better to make a wrapper class of the
     * result list.
     */
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderQuery> ordersV2() {
        List<Order> orders = orderService.getAllOrders();
        return orders.stream().map(SimpleOrderQuery::of).toList(); // Lazy loading
    }

    /**
     * Return all orders. It avoids N+1 problem through fetch join JPQL. It obtains the entity instances through the
     * repository and converts them to DTO instances.
     *
     * @return A list of DTO instances({@link SimpleOrderQuery}) of order.
     * @see OrderService
     */
    @GetMapping("/api/v3/simple-orders")
    public LookupResult<SimpleOrderQuery> ordersV3() {
        List<Order> orders = orderService.getAllWithMemberDelivery();
        return new LookupResult<>(orders.stream().map(SimpleOrderQuery::of).toList());
    }

    /**
     * Return all orders. It does not need to map entity instances to DTO instances because using JPQL DTO projection.
     * It can reduce the amount of data transferred over the network, but the repository specialized for the specific
     * DTO is less reusable, and it depends on the API. Therefore, it is better to separate the DTO-specific query
     * methods into the DTO-specific repository class.
     *
     * @return A list of DTO instances({@link SimpleOrderQuery}) of order.
     * @see wtchrs.SpringMockShop.repository.order.simplequery.SimpleOrderQueryRepository
     */
    @GetMapping("/api/v4/simple-orders")
    public LookupResult<SimpleOrderQuery> ordersV4() {
        return new LookupResult<>(orderService.getAllOrderDto());
    }
}
