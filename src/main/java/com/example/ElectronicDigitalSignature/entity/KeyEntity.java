package com.example.ElectronicDigitalSignature.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "keys")
public class KeyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "public_key", nullable = false, columnDefinition="TEXT", length = 10000)
    private String publicKey;

    @Column(name = "private_key", nullable = false, columnDefinition="TEXT", length = 10000)
    private String privateKey;

    @Column(name = "expire_date", nullable = false, updatable = false)
    private LocalDate expireDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        KeyEntity keyEntity = (KeyEntity) o;
        return getId() != null && Objects.equals(getId(), keyEntity.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
