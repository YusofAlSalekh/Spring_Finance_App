package com.yusof.web.repository;

import com.yusof.web.entity.TransactionModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository
        extends JpaRepository<TransactionModel, Integer>, TransactionRepositoryCustom {
}
