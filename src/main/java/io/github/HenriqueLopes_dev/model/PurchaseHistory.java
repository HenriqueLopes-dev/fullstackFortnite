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

    @OneToMany(fetch = FetchType.EAGER)
    private List<Cosmetic> cosmetics;

    @Column
    private boolean isRefound = false;

    @Column
    @CreatedDate
    private LocalDateTime createdAt;
}
