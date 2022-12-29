package wtchrs.SpringMockShop.controller.form;

import lombok.Getter;
import lombok.Setter;
import wtchrs.SpringMockShop.domain.item.Book;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class BookForm {

    private Long id;

    @NotEmpty
    private String name;
    private int price;
    private int stockQuantity;

    @NotEmpty
    private String author;
    @NotEmpty
    private String isbn;

    public static BookForm of(Book book) {
        BookForm bookForm = new BookForm();
        bookForm.setId(book.getId());
        bookForm.setName(book.getName());
        bookForm.setPrice(book.getPrice());
        bookForm.setStockQuantity(book.getStockQuantity());
        bookForm.setAuthor(book.getAuthor());
        bookForm.setIsbn(book.getIsbn());
        return bookForm;
    }
}
