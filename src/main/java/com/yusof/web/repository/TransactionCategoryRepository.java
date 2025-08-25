package com.yusof.web.repository;

import com.yusof.web.entity.TransactionCategoryModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionCategoryRepository extends JpaRepository<TransactionCategoryModel, Integer> {
    void deleteByIdAndClientId(int transactionCategoryId, int clientId);

    List<TransactionCategoryModel> findByClientId(int clientId);

    int countByNameAndClientIdAndIdNot(String name, int clientId, int id);

    boolean existsByClientIdAndNameIgnoreCase(Integer clientId, String name);

    Optional<TransactionCategoryModel> findByIdAndClientId(int transactionCategoryId, int clientId);
}
