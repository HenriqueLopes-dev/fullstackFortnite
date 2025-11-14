package io.github.HenriqueLopes_dev.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table
@Data
@EntityListeners(AuditingEntityListener.class)
public class Cosmetic {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String externalId;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column
    private String imageUrl;

    @Column
    private String type;

    @Column
    private String rarity;

    @Column
    private LocalDateTime added;

    @Column(nullable = false)
    private Boolean isNew = false;

    @OneToMany(mappedBy = "cosmetic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CosmeticBundleRelation> bundles = new ArrayList<>();
}
