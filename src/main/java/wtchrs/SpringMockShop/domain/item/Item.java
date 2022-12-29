package wtchrs.SpringMockShop.domain.item;

import lombok.Getter;
import lombok.NoArgsConstructor;
import wtchrs.SpringMockShop.domain.Category;
import wtchrs.SpringMockShop.exception.NotEnoughStockException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@NoArgsConstructor
@Getter
public abstract class Item {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany
    @JoinTable(name = "item_category",
        joinColumns = @JoinColumn(name = "item_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categories = new ArrayList<>();

    public Item(String name, int price, int stockQuantity) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    protected void setId(Long id) {
        if (this.id != null) throw new IllegalStateException("Id is already set.");
        this.id = id;
    }

    /**
     * Increase stockQuantity.
     */
    public void increaseStock(int inc) {
        this.stockQuantity += inc;
    }

    /**
     * Decrease stockQuantity.
     */
    public void decreaseStock(int dec) {
        if (this.stockQuantity < dec) throw new NotEnoughStockException("Need more stock.");
        this.stockQuantity -= dec;
    }

    public void updateValue(String name, int price, int stockQuantity) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }
}