package ru.yusof.dao;

import org.springframework.stereotype.Service;
import ru.yusof.entity.AccountModel;
import ru.yusof.entity.CategoryAmountModel;
import ru.yusof.entity.TransactionCategoryModel;
import ru.yusof.entity.TransactionModel;
import ru.yusof.exceptions.DaoException;
import ru.yusof.exceptions.ForbiddenException;
import ru.yusof.exceptions.NotFoundException;
import ru.yusof.exceptions.OperationFailedException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionDao {
    @PersistenceContext
    private EntityManager em;

    public List<CategoryAmountModel> fetchExpenseByCategory(int clientId, LocalDate startDate, LocalDate endDate) {
        try {
            Timestamp start = Timestamp.valueOf(startDate.atStartOfDay());
            Timestamp end = Timestamp.valueOf(endDate.plusDays(1).atStartOfDay());
            return em.createQuery("select new ru.yusof.entity.CategoryAmountModel(c.name, sum(t.amount)) " +
                                    "from TransactionModel t " +
                                    "join t.categories c " +
                                    "join t.sender s " +
                                    "where s.clientId = :clientId and t.createdDate between :start and :end " +
                                    "group by c.name",
                            CategoryAmountModel.class
                    )
                    .setParameter("clientId", clientId)
                    .setParameter("start", start)
                    .setParameter("end", end)
                    .getResultList();
        } catch (PersistenceException e) {
            throw new DaoException("Error during fetching expenses by category", e);
        }
    }

    public List<CategoryAmountModel> fetchIncomeByCategory(int clientId, LocalDate startDate, LocalDate endDate) {
        try {
            Timestamp start = Timestamp.valueOf(startDate.atStartOfDay());
            Timestamp end = Timestamp.valueOf(endDate.plusDays(1).atStartOfDay());
            return em.createQuery("select new ru.yusof.entity.CategoryAmountModel(c.name, sum(t.amount)) " +
                                    "from TransactionModel t " +
                                    "join t.receiver s " +
                                    "join t.categories c " +
                                    "where s.clientId = :clientId and t.createdDate between :start and :end " +
                                    "group by c.name",
                            CategoryAmountModel.class)
                    .setParameter("clientId", clientId)
                    .setParameter("start", start)
                    .setParameter("end", end)
                    .getResultList();
        } catch (PersistenceException e) {
            throw new DaoException("Error during fetching income by category", e);
        }
    }

    @Transactional
    public void addTransaction(int senderAccountId, int receiverAccountId, int clientId, BigDecimal amount, List<Integer> categoryIds) {
        try {
            checkBalanceSufficiency(senderAccountId, clientId, amount);

            subtractMoneyFromSender(senderAccountId, clientId, amount);

            addMoneyToReceiver(receiverAccountId, amount);

            createTransaction(senderAccountId, receiverAccountId, amount, categoryIds);
        } catch (PersistenceException e) {
            throw new OperationFailedException("Error occurred during adding new transaction", e);
        }
    }

    public void createTransaction(int senderAccountId, int receiverAccountId, BigDecimal amount, List<Integer> categoryIds) {
        try {
            AccountModel senderAccount = getSenderAccount(senderAccountId);
            AccountModel receiverAccount = getReceiverAccount(receiverAccountId);
            List<TransactionCategoryModel> categoryModels = getTransactionCategoryModels(categoryIds);
            TransactionModel transactionModel = buildTransaction(amount, receiverAccount, senderAccount, categoryModels);

            em.persist(transactionModel);
        } catch (PersistenceException e) {
            throw new DaoException("Error occurred while creating new transaction", e);
        }
    }

    private AccountModel getReceiverAccount(int receiverAccountId) {
        AccountModel receiverAccount = em.find(AccountModel.class, receiverAccountId);
        if (receiverAccount == null) {
            throw new DaoException("Receiver account with id " + receiverAccountId + " does not exist.");
        }
        return receiverAccount;
    }

    private AccountModel getSenderAccount(int senderAccountId) {
        AccountModel senderAccount = em.find(AccountModel.class, senderAccountId);
        if (senderAccount == null) {
            throw new DaoException("Sender account with id " + senderAccountId + " does not exist.");
        }
        return senderAccount;
    }

    private List<TransactionCategoryModel> getTransactionCategoryModels(List<Integer> categoryIds) {
        List<TransactionCategoryModel> categoryModels = new ArrayList<>();
        for (Integer categoryId : categoryIds) {
            TransactionCategoryModel transactionCategoryModel = em.find(TransactionCategoryModel.class, categoryId);
            if (transactionCategoryModel == null) {
                throw new DaoException("Category with id " + categoryId + " does not exist.");
            }
            categoryModels.add(transactionCategoryModel);
        }
        return categoryModels;
    }

    private static TransactionModel buildTransaction(BigDecimal amount, AccountModel receiverAccount, AccountModel senderAccount, List<TransactionCategoryModel> categoryModels) {
        TransactionModel transactionModel = new TransactionModel();
        transactionModel.setAmount(amount);
        transactionModel.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        transactionModel.setReceiver(receiverAccount);
        transactionModel.setSender(senderAccount);
        transactionModel.setCategories(categoryModels);
        return transactionModel;
    }

    public void addMoneyToReceiver(int receiverAccountId, BigDecimal amount) {
        try {
            AccountModel receiverAccount = em.find(AccountModel.class, receiverAccountId);

            if (receiverAccount == null) {
                throw new NotFoundException("There is no such account with id " + receiverAccountId);
            }

            receiverAccount.setBalance(receiverAccount.getBalance().add(amount));
        } catch (PersistenceException e) {
            throw new DaoException("Error occurred while adding money to the receiver's account with id " + receiverAccountId, e);
        }
    }

    public void subtractMoneyFromSender(int senderAccountId, int clientId, BigDecimal amount) {
        try {
            AccountModel senderAccount = em.find(AccountModel.class, senderAccountId);

            accountInformationValidation(senderAccountId, clientId, senderAccount);

            BigDecimal newBalance = senderAccount.getBalance().subtract(amount);
            senderAccount.setBalance(newBalance);
        } catch (PersistenceException e) {
            throw new DaoException("Error occurred while subtracting money from sender's account with id " + senderAccountId, e);
        }
    }

    private void checkBalanceSufficiency(int senderAccountId, int clientId, BigDecimal amount) {
        try {
            AccountModel senderAccount = em.find(AccountModel.class, senderAccountId);

            accountInformationValidation(senderAccountId, clientId, senderAccount);

            if (senderAccount.getBalance().compareTo(amount) < 0) {
                throw new IllegalArgumentException("Insufficient funds in sender's account with id " + senderAccountId);
            }
        } catch (PersistenceException e) {
            throw new DaoException("Error occurred while checking sender's account balance", e);
        }
    }

    private static void accountInformationValidation(int senderAccountId, int clientId, AccountModel senderAccount) {
        if (senderAccount == null) {
            throw new NotFoundException("There is no account with id " + senderAccountId);
        }

        if (senderAccount.getClientId() != clientId) {
            throw new ForbiddenException("Account does not belong to the user with id " + clientId);
        }
    }
}