package wtchrs.jpabook.jpashop.repository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import wtchrs.jpabook.jpashop.domain.OrderStatus;

@RequiredArgsConstructor
@Getter
public class OrderSearch {

    private final String username;
    private final OrderStatus status;
}
