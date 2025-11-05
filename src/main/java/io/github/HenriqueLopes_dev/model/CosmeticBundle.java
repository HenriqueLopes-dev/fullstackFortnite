package io.github.HenriqueLopes_dev.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;
import java.util.UUID;

@Entity
@Table
@Data
@EntityListeners(AuditingEntityListener.class)
public class CosmeticBundle {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private String name;

    @Column
    private String imageUrl;

    @Column
    private int regularPrice;

    @Column
    private int finalPrice;

    @Column
    private List<Cosmetic> cosmetics;

}
