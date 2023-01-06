package wtchrs.SpringMockShop.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import wtchrs.SpringMockShop.domain.item.Album;
import wtchrs.SpringMockShop.domain.item.Book;
import wtchrs.SpringMockShop.domain.item.Item;

import java.util.List;

@SpringBootTest
@Transactional
class ItemRepositoryTest {

    @Autowired private ItemRepository itemRepository;

    @Test
    public void addItemAndFind() {
        Item item = new Album("album1", 0, 0, "", "");

        Long saveId = itemRepository.save(item);
        Item findItem = itemRepository.findById(saveId);

        Assertions.assertThat(findItem).isEqualTo(item);
    }

    @Test
    public void findAll() {
        Item album = new Album("album1", 0, 0, "", "");
        Item book = new Book("book1", 0, 0, "", "");

        itemRepository.save(album);
        itemRepository.save(book);
        List<Item> findList = itemRepository.findAll();

//        Assertions.assertThat(findList).hasSize(2); // It fails because of the "InitDb" bean.
        Assertions.assertThat(findList).contains(album);
        Assertions.assertThat(findList).contains(book);
    }
}