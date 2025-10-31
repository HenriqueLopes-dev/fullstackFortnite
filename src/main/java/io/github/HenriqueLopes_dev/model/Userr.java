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
public class Userr {

    private static final int INITIAL_BALANCE = 10000;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private String name;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private int balance = INITIAL_BALANCE;

    @Column
    private List<Cosmetic> adquiredCosmetics;

    @Column
    private List<Cosmetic> purchaseHistory;

}
