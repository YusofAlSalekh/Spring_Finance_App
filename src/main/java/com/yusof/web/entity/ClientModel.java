package com.yusof.web.entity;

import com.yusof.web.security.ClientRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "client")
public class ClientModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @ElementCollection(targetClass = ClientRole.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "client_role",
            joinColumns = @JoinColumn(name = "client_id"))
    @Column(name = "role")
    private Set<ClientRole> roles = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientModel clientModel = (ClientModel) o;
        return Objects.equals(id, clientModel.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}