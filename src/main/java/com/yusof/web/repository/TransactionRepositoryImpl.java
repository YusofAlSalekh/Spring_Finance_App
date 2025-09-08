package com.yusof.web.repository;

import com.yusof.web.entity.CategoryReportModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class TransactionRepositoryImpl implements TransactionRepositoryCustom {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<CategoryReportModel> fetchIncomeByCategory(int clientId, LocalDateTime start, LocalDateTime end) {
        return em.createQuery(
                        "select new com.yusof.web.entity.CategoryReportModel(c.name, sum(t.amount)) " +
                                "from TransactionModel t " +
                                "join t.receiver r " +
                                "join t.categories c " +
                                "where r.clientId = :clientId and t.createdDate between :start and :end " +
                                "group by c.name ",
                        CategoryReportModel.class)
                .setParameter("clientId", clientId)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();
    }

    @Override
    public List<CategoryReportModel> fetchExpenseByCategory(int clientId, LocalDateTime start, LocalDateTime end) {
        return em.createQuery(
                        "select new com.yusof.web.entity.CategoryReportModel(c.name, sum(t.amount)) " +
                                "from TransactionModel t " +
                                "join t.sender s " +
                                "join t.categories c " +
                                "where s.clientId = :clientId and t.createdDate between :start and :end " +
                                "group by c.name ",
                        CategoryReportModel.class)
                .setParameter("clientId", clientId)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();
    }
}
