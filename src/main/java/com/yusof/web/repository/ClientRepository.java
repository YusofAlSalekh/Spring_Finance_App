package com.yusof.web.repository;

import com.yusof.web.entity.ClientModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<ClientModel, Integer> {
    Optional<ClientModel> findByEmailAndPassword(String email, String password);
}
