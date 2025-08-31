package com.yusof.web.web.controller;

import com.yusof.web.security.CustomUserDetails;
import com.yusof.web.service.AccountDTO;
import com.yusof.web.service.AccountService;
import com.yusof.web.service.TransactionCategoryDTO;
import com.yusof.web.service.TransactionCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class WebMenu {
    private final AccountService accountService;
    private final TransactionCategoryService transactionCategoryService;

    @GetMapping("/menu")
    public String getMenu(Model model,
                          @AuthenticationPrincipal CustomUserDetails customUserDetails,
                          @RequestParam(name = "show", required = false) String show) {

        Integer clientId = customUserDetails.getId();

        model.addAttribute("id", clientId)
                .addAttribute("name", customUserDetails.getUsername());

        if ("accounts".equals(show)) {
            List<AccountDTO> accounts = accountService.viewAccount(clientId);
            model.addAttribute("accounts", accounts);
        }

        if ("categories".equals(show)) {
            List<TransactionCategoryDTO> transactionCategory =
                    transactionCategoryService.viewTransactionCategory(clientId);
            model.addAttribute("categories", transactionCategory);
        }
        return "menu";
    }
}
