package ru.yusof.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.transaction.annotation.Transactional;
import ru.yusof.entity.UserModel;
import ru.yusof.exceptions.AlreadyExistsException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoTest {
    UserDao subj;
    ApplicationContext context;

    @BeforeEach
    public void setUp() {
        System.setProperty("jdbcUrl", "jdbc:h2:mem:test_mem" + UUID.randomUUID().toString());
        System.setProperty("jdbcUser", "postgres");
        System.setProperty("jdbcPassword", "");
        System.setProperty("liquibaseFile", "liquibase_user_dao_test.xml");

        context = new AnnotationConfigApplicationContext("ru.yusof");
        subj = context.getBean(UserDao.class);
    }

    @Test
    @Transactional
    void findByEmailAndHash() {
        Optional<UserModel> user = subj.findByEmailAndHash("yusof@mail.ru", "202cb962ac59075b964b07152d234b70");

        assertTrue(user.isPresent());

        UserModel userModel = user.get();
        assertEquals(1, userModel.getId());
        assertEquals("yusof@mail.ru", userModel.getEmail());
        assertEquals("202cb962ac59075b964b07152d234b70", userModel.getPassword());
    }

    @Test
    void findByEmailAndHash_notFound() {
        Optional<UserModel> user = subj.findByEmailAndHash("yusof@mail.ru", "202cb962ac59075b964b07152d234b71");

        assertFalse(user.isPresent());
    }

    @Test()
    void insertDoesNotWork() {
        assertThrows(AlreadyExistsException.class, () -> {
            subj.addUser("yusof@mail.ru", "202cb962ac59075b964b07152d234b70");
        }, "User with the given email already exists.");
    }

    @Test()
    void insertWorks() {
        UserModel user = subj.addUser("ganik@mail.ru", "202cb962ac59075b964b07152d234b71");

        assertEquals(2, user.getId());
        assertEquals("ganik@mail.ru", user.getEmail());
        assertEquals("202cb962ac59075b964b07152d234b71", user.getPassword());
    }
}