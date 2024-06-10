package ru.yusof.dao;

public class DaoFactory {
    private static UserDao userDao;
    private static AccountDao accountDao;
    private static TransactionTypeDao transactionTypeDao;

    public static UserDao getUserDao() {
        if (userDao == null) {
            userDao = new UserDao();
        }
        return userDao;
    }

    public static AccountDao getAccountDao() {
        if (accountDao == null) {
            accountDao = new AccountDao();
        }
        return accountDao;
    }

    public static TransactionTypeDao getTransactionTypeDao() {
        if (transactionTypeDao == null) {
            transactionTypeDao = new TransactionTypeDao();
        }
        return transactionTypeDao;
    }
}