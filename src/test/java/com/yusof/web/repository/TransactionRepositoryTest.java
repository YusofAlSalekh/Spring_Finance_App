package com.yusof.web.repository;

import com.yusof.web.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionCategoryRepository categoryRepository;

    @Test
    void fetchIncomeByCategory() {
        ClientModel client = new ClientModel();
        client.setEmail("test@example.com");
        client.setPassword("password");
        client = clientRepository.save(client);

        AccountModel receiver = new AccountModel();
        receiver.setName("Receiver Account");
        receiver.setClientId(client.getId());
        receiver.setBalance(new BigDecimal("100.00"));
        receiver = accountRepository.save(receiver);

        AccountModel sender = new AccountModel();
        sender.setName("Sender Account");
        sender.setClientId(client.getId());
        sender.setBalance(new BigDecimal("200.00"));
        sender = accountRepository.save(sender);

        TransactionCategoryModel catFood = new TransactionCategoryModel();
        catFood.setName("Food");
        catFood.setClientId(client.getId());
        categoryRepository.save(catFood);

        TransactionCategoryModel catCleaning = new TransactionCategoryModel();
        catCleaning.setName("Cleaning");
        catCleaning.setClientId(client.getId());
        categoryRepository.save(catCleaning);

        TransactionModel t1 = new TransactionModel();
        t1.setReceiver(receiver);
        t1.setSender(sender);
        t1.setAmount(new BigDecimal("10.00"));
        t1.setCreatedDate(LocalDateTime.now().minusDays(1));
        t1.setCategories((List.of(catCleaning)));
        transactionRepository.save(t1);

        TransactionModel t2 = new TransactionModel();
        t2.setReceiver(receiver);
        t2.setSender(sender);
        t2.setAmount(new BigDecimal("20.00"));
        t2.setCreatedDate(LocalDateTime.now().minusHours(5));
        t2.setCategories((List.of(catFood)));
        transactionRepository.save(t2);

        List<CategoryReportModel> reports =
                transactionRepository.fetchIncomeByCategory(
                        client.getId(),
                        LocalDateTime.now().minusDays(2),
                        LocalDateTime.now()
                );

        assertThat(reports).hasSize(2);
        assertThat(reports.get(0).getCategoryName()).isEqualTo("Cleaning");
        assertThat(reports.get(0).getTotalAmount()).isEqualByComparingTo("10.00");
        assertThat(reports.get(1).getCategoryName()).isEqualTo("Food");
        assertThat(reports.get(1).getTotalAmount()).isEqualByComparingTo("20.00");
    }

    @Test
    void fetchExpenseByCategory() {
        ClientModel client = new ClientModel();
        client.setEmail("test@example.com");
        client.setPassword("password");
        client = clientRepository.save(client);

        AccountModel receiver = new AccountModel();
        receiver.setName("Receiver Account");
        receiver.setClientId(client.getId());
        receiver.setBalance(new BigDecimal("100.00"));
        receiver = accountRepository.save(receiver);

        AccountModel sender = new AccountModel();
        sender.setName("Sender Account");
        sender.setClientId(client.getId());
        sender.setBalance(new BigDecimal("200.00"));
        sender = accountRepository.save(sender);

        TransactionCategoryModel catFood = new TransactionCategoryModel();
        catFood.setName("Food");
        catFood.setClientId(client.getId());
        categoryRepository.save(catFood);

        TransactionModel t1 = new TransactionModel();
        t1.setReceiver(receiver);
        t1.setSender(sender);
        t1.setAmount(new BigDecimal("10.00"));
        t1.setCreatedDate(LocalDateTime.now().minusDays(2));
        t1.setCategories((List.of(catFood)));
        transactionRepository.save(t1);

        TransactionModel t2 = new TransactionModel();
        t2.setReceiver(receiver);
        t2.setSender(sender);
        t2.setAmount(new BigDecimal("20.00"));
        t2.setCreatedDate(LocalDateTime.now().minusHours(5));
        t2.setCategories((List.of(catFood)));
        transactionRepository.save(t2);

        List<CategoryReportModel> reports =
                transactionRepository.fetchExpenseByCategory(
                        client.getId(),
                        LocalDateTime.now().minusDays(1),
                        LocalDateTime.now()
                );

        assertThat(reports).hasSize(1);
        CategoryReportModel report = reports.get(0);
        assertThat(report.getCategoryName()).isEqualTo("Food");
        assertThat(report.getTotalAmount()).isEqualByComparingTo("20.00");
    }
}