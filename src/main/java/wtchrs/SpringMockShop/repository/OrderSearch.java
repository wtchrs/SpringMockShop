package wtchrs.SpringMockShop.repository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import wtchrs.SpringMockShop.domain.OrderStatus;

@RequiredArgsConstructor
@Getter
public class OrderSearch {

    private final String username;
    private final OrderStatus status;
}
