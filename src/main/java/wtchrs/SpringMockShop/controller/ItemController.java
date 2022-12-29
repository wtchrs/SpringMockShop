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
import wtchrs.SpringMockShop.controller.form.BookForm;
import wtchrs.SpringMockShop.domain.item.Book;
import wtchrs.SpringMockShop.service.ItemService;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/items/new")
    public String registerForm(Model model) {
        model.addAttribute("bookForm", new BookForm());
        return "items/register";
    }

    @PostMapping("/items/new")
    public String registerProcess(@ModelAttribute("bookForm") @Validated BookForm bookForm, Errors errors) {
        if (errors.hasErrors()) return "items/register";

        itemService.addItem(Book.of(bookForm));
        return "redirect:/items";
    }

    @GetMapping("/items")
    public String itemList(Model model) {
        model.addAttribute("items", itemService.getAllItem());
        return "items/list";
    }

    @GetMapping("/items/{itemId}/edit")
    public String editItemForm(@PathVariable("itemId") Long itemId, Model model) {
        Book book = (Book) itemService.getItem(itemId);
        model.addAttribute("bookForm", BookForm.of(book));
        return "/items/editItemForm";
    }

    @PostMapping("/items/{itemId}/edit")
    public String editItemProcess(@PathVariable("itemId") Long itemId,
                                  @ModelAttribute("bookForm") @Validated BookForm bookForm, Errors errors) {
        if (errors.hasErrors()) return "items/editItemForm";
        if (!itemId.equals(bookForm.getId())) {
            throw new IllegalStateException("Path variable's id is not equal to form data's id.");
        }

        itemService.updateBook(bookForm.getId(), bookForm);
        return "redirect:/items";
    }
}
