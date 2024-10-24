package detailing.reservation.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
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

    @NotEmpty
    private String name;
    private String phone;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member") // reserve 테이블에 있는 member 필드에 의해 매핑되었다는 뜻
    private List<Reserve> reserves = new ArrayList<>();
}
