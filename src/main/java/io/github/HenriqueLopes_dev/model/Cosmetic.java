package io.github.HenriqueLopes_dev.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
    private String added;

    @Column(nullable = false)
    private Boolean isNew = false;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bundle_id")
    @JsonManagedReference
    private CosmeticBundle bundle;
}
