package com.yusof.web.repository;

import com.yusof.web.entity.AccountModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountModel, Integer> {
    List<AccountModel> findByClientId(int clientId);

    boolean existsByClientIdAndNameIgnoreCase(int clientId, String name);

    int deleteByIdAndClientId(int accountId, int clientId);

    int countByNameAndClientIdAndIdNot(String name, int clientId, int accountId);

    Optional<AccountModel> findAccountByIdAndClientId(int accountId, int clientId);
}


