package wtchrs.SpringMockShop.domain.item;

import lombok.Getter;
import lombok.NoArgsConstructor;
import wtchrs.SpringMockShop.controller.form.BookForm;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("book")
@NoArgsConstructor
@Getter
public class Book extends Item {

    private String author;
    private String isbn;

    public Book(String name, int price, int stockQuantity, String author, String isbn) {
        super(name, price, stockQuantity);
        this.author = author;
        this.isbn = isbn;
    }

    public void updateValue(String name, int price, int stockQuantity, String author, String isbn) {
        super.updateValue(name, price, stockQuantity);
        this.author = author;
        this.isbn = isbn;
    }

    public static Book of(BookForm bookForm) {
        Book book = new Book(bookForm.getName(), bookForm.getPrice(), bookForm.getStockQuantity(), bookForm.getAuthor(),
                             bookForm.getIsbn());
        book.setId(bookForm.getId());
        return book;
    }
}
