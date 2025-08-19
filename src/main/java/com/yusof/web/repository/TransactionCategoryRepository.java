package com.yusof.web.repository;

import com.yusof.web.entity.TransactionCategoryModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionCategoryRepository extends JpaRepository<TransactionCategoryModel, Integer> {
    int deleteByIdAndClientId(int accountId, int clientId);

    List<TransactionCategoryModel> findByClientId(int clientId);

    int countByNameAndClientIdAndIdNot(String name, int clientId, int id);

    boolean existsByClientIdAndNameIgnoreCase(Integer clientId, String name);
}
