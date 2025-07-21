package ru.yusof.dao;

import org.springframework.stereotype.Service;
import ru.yusof.entity.TransactionCategoryModel;
import ru.yusof.exceptions.AlreadyExistsException;
import ru.yusof.exceptions.DaoException;
import ru.yusof.exceptions.NotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class TransactionCategoryDao {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public TransactionCategoryModel createTransactionCategory(String name, int clientId) {
        try {
            TransactionCategoryModel transactionCategoryModel = new TransactionCategoryModel();
            transactionCategoryModel.setName(name);
            transactionCategoryModel.setClientId(clientId);
            em.persist(transactionCategoryModel);

            return transactionCategoryModel;
        } catch (PersistenceException e) {
            throw new DaoException("Error occurred during transaction type creation", e);
        }
    }

    @Transactional
    public boolean deleteTransactionCategory(int transactionCategoryId, int clientId) {
        try {
            TransactionCategoryModel transactionCategoryModel = em.find(TransactionCategoryModel.class, transactionCategoryId);
            transactionCategoryExistenceValidation(transactionCategoryId, clientId, transactionCategoryModel);

            em.remove(transactionCategoryModel);
        } catch (PersistenceException e) {
            throw new DaoException("Error occurred while deleting transaction type", e);
        }
        return true;
    }

    @Transactional
    public boolean updateTransactionCategoryName(String name, int transactionCategoryId, int clientId) {
        try {
            Long count = em.createQuery("select count(tc) from TransactionCategoryModel tc where tc.name = :name and tc.id <> :transactionCategoryId and tc.clientId = :clientId", Long.class)
                    .setParameter("name", name)
                    .setParameter("transactionCategoryId", transactionCategoryId)
                    .setParameter("clientId", clientId)
                    .getSingleResult();

            if (count > 0) {
                throw new AlreadyExistsException("The category with this name already exists");
            }

            TransactionCategoryModel transactionCategoryModel = em.find(TransactionCategoryModel.class, transactionCategoryId);

            transactionCategoryExistenceValidation(transactionCategoryId, clientId, transactionCategoryModel);

            transactionCategoryModel.setName(name);
            return true;
        } catch (PersistenceException e) {
            throw new DaoException("Error occurred during transaction name updating", e);
        }
    }

    public List<TransactionCategoryModel> findByClientID(int clientId) {
        try {
            return em.createQuery("select tc from TransactionCategoryModel tc where tc.clientId = :clientId", TransactionCategoryModel.class)
                    .setParameter("clientId", clientId)
                    .getResultList();
        } catch (PersistenceException e) {
            throw new DaoException("Database error occurred while fetching category by client Id.", e);
        }
    }

    private static void transactionCategoryExistenceValidation(int transactionCategoryId, int clientId, TransactionCategoryModel transactionCategoryModel) {
        if (transactionCategoryModel == null || transactionCategoryModel.getClientId() != clientId) {
            throw new NotFoundException("No transaction category found with Id: " + transactionCategoryId);
        }
    }
}
