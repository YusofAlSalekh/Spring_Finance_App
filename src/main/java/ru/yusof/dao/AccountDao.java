package ru.yusof.dao;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yusof.entity.AccountModel;
import ru.yusof.exceptions.AlreadyExistsException;
import ru.yusof.exceptions.DaoException;
import ru.yusof.exceptions.NotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountDao {
    @PersistenceContext
    private EntityManager em;

    public List<AccountModel> findByClientID(int clientId) {
        try {
            return em.createQuery("select a from AccountModel a where a.clientId = :clientId", AccountModel.class)
                    .setParameter("clientId", clientId)
                    .getResultList();
        } catch (PersistenceException e) {
            throw new DaoException("Error occurred while fetching accounts by client ID.", e);
        }
    }

    @Transactional
    public AccountModel createAccount(String accountName, BigDecimal balance, int clientId) {
        try {
            AccountModel account = new AccountModel();
            account.setBalance(balance);
            account.setClientId(clientId);
            account.setName(accountName);
            em.persist(account);

            return account;
        } catch (PersistenceException e) {
            throw new DaoException("Error occurred during account creation", e);
        }
    }

    @Transactional
    public void deleteAccount(int accountId, int clientId) {
        try {
            AccountModel account = em.find(AccountModel.class, accountId);
            accountExistenceValidation(accountId, clientId, account);

            em.remove(account);
        } catch (PersistenceException e) {
            throw new DaoException("Error occurred while deleting account", e);
        }
    }

    @Transactional
    public AccountModel updateAccountName(String accountName, int accountId, int clientId) {
        try {
            Long count = em.createQuery("select count (a) from AccountModel a where a.name = :name and a.id<>:id and a.clientId =:clientId", Long.class)
                    .setParameter("name", accountName)
                    .setParameter("id", accountId)
                    .setParameter("clientId", clientId)
                    .getSingleResult();

            if (count > 0) {
                throw new AlreadyExistsException("The account with this name already exists");
            }

            AccountModel account = em.find(AccountModel.class, accountId);
            accountExistenceValidation(accountId, clientId, account);

            account.setName(accountName);
            return account;
        } catch (PersistenceException e) {
            throw new DaoException("Error occurred while updating account name", e);
        }
    }

    private static void accountExistenceValidation(int accountId, int clientId, AccountModel account) {
        if (account == null || account.getClientId() != clientId) {
            throw new NotFoundException("No account found with Id: " + accountId);
        }
    }
}