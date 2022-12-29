package wtchrs.SpringMockShop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wtchrs.SpringMockShop.controller.form.BookForm;
import wtchrs.SpringMockShop.domain.item.Book;
import wtchrs.SpringMockShop.domain.item.Item;
import wtchrs.SpringMockShop.repository.ItemRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void addItem(Item item) {
        itemRepository.save(item);
    }

    @Transactional
    public void updateBook(Long itemId, BookForm bookForm) {
        Item findItem = itemRepository.findById(itemId);
        if (!(findItem instanceof Book)) {
            throw new IllegalStateException("Not matching types: Type of repository's object is not Book.");
        }
        ((Book) findItem).updateValue(bookForm.getName(), bookForm.getPrice(), bookForm.getStockQuantity(),
                                      bookForm.getAuthor(), bookForm.getIsbn());
    }

    public Item getItem(Long id) {
        return itemRepository.findById(id);
    }

    public List<Item> getAllItem() {
        return itemRepository.findAll();
    }
}
