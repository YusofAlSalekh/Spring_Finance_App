package ru.yusof.dao;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Service;
import ru.yusof.entity.UserModel;
import ru.yusof.exceptions.AlreadyExistsException;
import ru.yusof.exceptions.DaoException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserDao {
    @PersistenceContext
    private EntityManager em;

    public Optional<UserModel> findByEmailAndHash(String email, String hash) {
        try {
            return Optional.of(em.createQuery("select u from UserModel u where u.email = :email and u.password = :hash", UserModel.class)
                    .setParameter("email", email)
                    .setParameter("hash", hash)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (PersistenceException e) {
            throw new DaoException("Error occurred during finding user by email and hash", e);
        }
    }

    @Transactional
    public UserModel addUser(String email, String hash) {
        try {
            UserModel userModel = new UserModel();
            userModel.setEmail(email);
            userModel.setPassword(hash);
            em.persist(userModel);

            return userModel;
        } catch (PersistenceException e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw new AlreadyExistsException("User with the given email already exists.");
            }
            throw new DaoException("Error occurred during adding new user", e);
        }
    }

    public Optional<UserModel> findById(Integer userId) {
        try {
            UserModel userModel = em.find(UserModel.class, userId);
            return Optional.ofNullable(userModel);
        } catch (PersistenceException e) {
            throw new DaoException("Error occurred during finding user", e);
        }
    }
}