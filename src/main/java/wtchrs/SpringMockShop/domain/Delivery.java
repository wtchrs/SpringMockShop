package wtchrs.SpringMockShop.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @Embedded
    private Address address;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "delivery")
    private Order order;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    public Delivery(Address address) {
        this.address = address;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void initStatus() {
        if (status != null) throw new IllegalStateException("Delivery status already set");
        status = DeliveryStatus.READY;
    }

    public void finishDelivery() {
        if (status == null) throw new IllegalStateException("Delivery status not set");
        if (status == DeliveryStatus.COMP) throw new IllegalStateException("Delivery already finished");
        status = DeliveryStatus.COMP;
    }
}
