package wtchrs.SpringMockShop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true)
    private String username;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    public Member(String username, Address address) {
        setUsername(username);
        this.address = address;
    }

    public void setUsername(String username) {
        if (!StringUtils.hasText(username)) {
            throw new IllegalArgumentException("Member.username must not be empty string.");
        }
        this.username = username;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
