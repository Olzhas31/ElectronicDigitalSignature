package com.example.ElectronicDigitalSignature.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "otinishter")
public class OtinishEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type")
    private String type;

    @Column(name = "organ")
    private String organ;

    @Column(name = "description")
    private String description;

    @Column(name = "address")
    private String address;

    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private UserEntity owner;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        OtinishEntity otinishEntity = (OtinishEntity) o;
        return getId() != null && Objects.equals(getId(), otinishEntity.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
