package io.github.HenriqueLopes_dev.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table
@Data
@EntityListeners(AuditingEntityListener.class)
public class Cosmetic {

    @Id
    private String id;

    @Column
    private String name;

    @Column
    private String rarity;

    @Column
    private String added;

    @Column
    private boolean isNew;

    @Column
    private int regularPrice;

    @Column
    private int finalPrice;

}
