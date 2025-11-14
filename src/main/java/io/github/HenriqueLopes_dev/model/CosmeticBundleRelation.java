package io.github.HenriqueLopes_dev.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table
public class CosmeticBundleRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cosmetic_id")
    private Cosmetic cosmetic;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bundle_id")
    private CosmeticBundle bundle;
}

