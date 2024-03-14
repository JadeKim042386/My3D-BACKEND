package joo.project.my3dbackend.domain;

import lombok.*;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;

@Getter
@ToString(callSuper = true)
@Table(name = "company")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Company implements Persistable<Long> {
    @Id
    @SequenceGenerator(name = "SEQ_GENERATOR", sequenceName = "seq_company", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GENERATOR")
    @EqualsAndHashCode.Include
    private Long id;

    @Setter
    @Column(nullable = false)
    private String companyName;

    @Setter
    private String homepage;

    @ToString.Exclude
    @OneToOne(mappedBy = "company")
    private UserAccount userAccount;

    private Company(String companyName, String homepage) {
        this.companyName = companyName;
        this.homepage = homepage;
    }

    public static Company of(String companyName, String homepage) {
        return new Company(companyName, homepage);
    }

    @Override
    public boolean isNew() {
        return this.id == null;
    }
}
