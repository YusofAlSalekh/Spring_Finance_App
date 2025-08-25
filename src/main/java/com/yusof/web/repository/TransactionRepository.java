package com.yusof.web.repository;

import com.yusof.web.entity.CategoryReportModel;
import com.yusof.web.entity.TransactionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionModel, Integer> {
    @Query("select new com.yusof.web.entity.CategoryReportModel(c.name, sum(t.amount)) " +
            "from TransactionModel t " +
            "join t.receiver s " +
            "join t.categories c " +
            "where s.clientId = :clientId and t.createdDate between :start and :end " +
            "group by c.name")
    List<CategoryReportModel> fetchIncomeByCategory(
            @Param("clientId") int clientId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("select new com.yusof.web.entity.CategoryReportModel(c.name, sum(t.amount)) " +
            "from TransactionModel t " +
            "join t.categories c " +
            "join t.sender s " +
            "where s.clientId = :clientId and t.createdDate between :start and :end " +
            "group by c.name")
    List<CategoryReportModel> fetchExpenseByCategory(
            @Param("clientId") int clientId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
}
