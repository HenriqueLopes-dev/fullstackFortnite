package io.github.HenriqueLopes_dev.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table
@Data
@EntityListeners(AuditingEntityListener.class)
public class Cosmetic {

    private String id;

    private String name;

    private String rarity;

    private String added;

    private boolean isNew;

    private int regularPrice;

    private int finalPrice;

}
