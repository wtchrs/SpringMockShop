package wtchrs.jpabook.jpashop.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import wtchrs.jpabook.jpashop.domain.item.Album;
import wtchrs.jpabook.jpashop.domain.item.Book;
import wtchrs.jpabook.jpashop.domain.item.Item;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

        Assertions.assertThat(findList).hasSize(2);
        Assertions.assertThat(findList).contains(album);
        Assertions.assertThat(findList).contains(book);
    }
}