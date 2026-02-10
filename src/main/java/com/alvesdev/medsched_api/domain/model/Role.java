package com.alvesdev.medsched_api.domain.model;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;

import com.alvesdev.medsched_api.domain.model.enums.RoleType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
public class Role implements GrantedAuthority {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private RoleType name;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();

    @Override
    public String getAuthority() {
        return "ROLE_" + name.name();
    }
}
