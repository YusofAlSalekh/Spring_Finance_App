package ru.yusof.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yusof.json.request.CreateTransactionRequest;
import ru.yusof.json.response.CreateTransactionResponse;
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
        validatePositiveBigDecimal(request.getAmount());
        BigDecimal amount = request.getAmount();

        validatePositiveInteger(request.getSenderAccountId());
        Integer senderAccountId = request.getSenderAccountId();

        validatePositiveInteger(request.getReceiverAccountId());
        Integer receiverAccountId = request.getReceiverAccountId();

        List<Integer> categoryIds = request.getCategoryIds();
        validateIntegerList(request.getCategoryIds());

        transactionService.performTransaction(senderAccountId, receiverAccountId, userId, amount, categoryIds);
        return new CreateTransactionResponse("Transaction created successfully");
    }

    @Override
    public Class<CreateTransactionRequest> getRequestClass() {
        return CreateTransactionRequest.class;
    }
}
