package wtchrs.SpringMockShop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import wtchrs.SpringMockShop.controller.form.OrderForm;
import wtchrs.SpringMockShop.domain.OrderStatus;
import wtchrs.SpringMockShop.repository.OrderSearch;
import wtchrs.SpringMockShop.service.ItemService;
import wtchrs.SpringMockShop.service.MemberService;
import wtchrs.SpringMockShop.service.OrderService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping("/order")
    public String orderForm(Model model) {
        model.addAttribute("members", memberService.getAllMembers());
        model.addAttribute("items", itemService.getAllItem());

        model.addAttribute("orderForm", new OrderForm());
        return "orders/order";
    }

    @PostMapping("/order")
    public String orderProcess(@ModelAttribute("orderForm") @Validated OrderForm orderForm, Errors errors) {
        if (errors.hasErrors()) return "orders/order";
        orderService.order(orderForm.getMemberId(), Map.of(orderForm.getItemId(), orderForm.getCount()));

        return "redirect:/orders";
    }

    @GetMapping("/orders")
    public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model) {
        if (orderSearch == null) {
            model.addAttribute("orders", orderService.getAllOrders());
        } else {
            model.addAttribute("orders", orderService.searchOrders(orderSearch));
        }
        model.addAttribute("statusValues", OrderStatus.values());
        return "orders/list";
    }

    @PostMapping("orders/{id}/cancel")
    public String cancelOrder(@PathVariable("id") Long orderId, HttpServletRequest request) {
        orderService.cancelOrder(orderId);
        String referer = request.getHeader("referer");
        if (referer != null){
            return "redirect:" + referer;
        } else {
            return "redirect:/orders";
        }
    }
}
