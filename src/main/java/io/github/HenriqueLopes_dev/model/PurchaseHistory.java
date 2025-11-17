package io.github.HenriqueLopes_dev.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table
@Data
@EntityListeners(AuditingEntityListener.class)
public class PurchaseHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private Integer price;

    @Column
    private String bundleName;

    @Column
    private String bundleImage;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "purchase_history_cosmetics",
            joinColumns = @JoinColumn(name = "purchase_history_id"),
            inverseJoinColumns = @JoinColumn(name = "cosmetic_id")
    )
    private List<Cosmetic> cosmetics = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userr_id")
    private Userr user;

    @Column
    private boolean refund = false;

    @Column
    @CreatedDate
    private LocalDateTime createdAt;
}
