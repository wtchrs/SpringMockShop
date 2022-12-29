package wtchrs.SpringMockShop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("hello")
    public String hello(Model model) {
        System.out.println("HelloController.hello");
        model.addAttribute("data", "Hello!");
        return "hello";
    }
}
