package wtchrs.SpringMockShop.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import wtchrs.SpringMockShop.domain.item.Item;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    @PersistenceContext
    private final EntityManager em;

    public Long save(Item item) {
        if (item.getId() == null) {
            em.persist(item);
        } else {
            // it is not good using em::merge. Dirty checking is better.
            // There is a risk of merging with null values.
            em.merge(item);
        }
        return item.getId();
    }

    public Item findById(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class).getResultList();
    }
}
