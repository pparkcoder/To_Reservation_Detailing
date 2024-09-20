package detailing.reservation.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;
    private String phone;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member") // reserve 테이블에 있는 member 필드에 의해 매핑되었다는
    private List<Reserve> reserves = new ArrayList<>();
}
