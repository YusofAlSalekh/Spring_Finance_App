package com.yusof.web.web.controller;

import com.yusof.web.api.controller.TransactionCommandCreation;
import com.yusof.web.exceptions.NotFoundException;
import com.yusof.web.security.CustomUserDetails;
import com.yusof.web.service.TransactionCategoryDTO;
import com.yusof.web.service.TransactionCategoryService;
import com.yusof.web.service.TransactionService;
import com.yusof.web.web.form.TransactionCreationForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/transaction")
public class WebTransactionController {
    private final TransactionService transactionService;
    private final TransactionCategoryService transactionCategoryService;

    @ModelAttribute("categories")
    public List<TransactionCategoryDTO> getCategories(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Integer clientId = customUserDetails.getId();
        return transactionCategoryService.viewTransactionCategory(clientId);
    }

    @GetMapping("/create")
    public String getTransactionCreation(Model model) {
        model.addAttribute("form", new TransactionCreationForm());
        return "transactionCreation";
    }

    @PostMapping("/create")
    public String postTransactionCreation(@ModelAttribute("form") @Valid TransactionCreationForm form,
                                          BindingResult bindingResult,
                                          @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                          RedirectAttributes redirectAttributes) {
        Integer clientId = customUserDetails.getId();
        if (!bindingResult.hasErrors()) {
            try {
                TransactionCommandCreation command = TransactionCommandCreation.builder()
                        .senderAccountId(form.getSenderAccountId())
                        .clientId(clientId)
                        .categoryIds(form.getCategoryIds())
                        .amount(form.getAmount())
                        .receiverAccountId(form.getReceiverAccountId())
                        .build();

                transactionService.performTransaction(command);

                redirectAttributes.addFlashAttribute("success", "Transaction Created");

                return "redirect:/menu";
            } catch (NotFoundException e) {
                bindingResult.rejectValue("categoryIds", "", e.getMessage());
            } catch (IllegalArgumentException e) {
                bindingResult.reject("", e.getMessage());
            }
        }
        return "transactionCreation";
    }
}
