package ru.yusof.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yusof.json.CreateTransactionRequest;
import ru.yusof.json.CreateTransactionResponse;
import ru.yusof.service.TransactionService;

import java.math.BigDecimal;
import java.util.List;

import static ru.yusof.controller.ValidationUtils.*;

@Service("/transaction/create")
@AllArgsConstructor
public class CreateTransactionController implements SecureController<CreateTransactionRequest, CreateTransactionResponse> {
    private final TransactionService transactionService;

    @Override
    public CreateTransactionResponse handle(CreateTransactionRequest request, Integer userId) {
        BigDecimal amount = extractPositiveBigDecimal(request.getAmount());
        int senderAccountId = extractPositiveInteger(request.getSenderAccountId());
        int receiverAccountId = extractPositiveInteger(request.getReceiverAccountId());
        List<Integer> categoryIds = extractIntegerList(request.getCategoryIds());

        transactionService.performTransaction(senderAccountId, receiverAccountId, userId, amount, categoryIds);
        return new CreateTransactionResponse("Transaction created successfully");
    }

    @Override
    public Class<CreateTransactionRequest> getRequestClass() {
        return CreateTransactionRequest.class;
    }
}
