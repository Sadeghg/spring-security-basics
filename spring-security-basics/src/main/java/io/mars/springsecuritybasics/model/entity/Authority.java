package io.mars.springsecuritybasics.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "role_id", foreignKey = @ForeignKey(name = "authority_role_fkey"), referencedColumnName = "id")
    private Role role;
}
