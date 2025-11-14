package io.github.HenriqueLopes_dev.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table
@Data
@EntityListeners(AuditingEntityListener.class)
public class CosmeticBundle{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column
    private String imageUrl;

    @Column
    private Integer regularPrice;

    @Column
    private Integer finalPrice;

    @Column
    private Boolean isOnSale = false;

    @OneToMany(mappedBy = "bundle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CosmeticBundleRelation> cosmetics = new ArrayList<>();
}
