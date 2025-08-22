package com.yusof.web.web.controller;

import com.yusof.web.exceptions.UnauthorizedException;
import com.yusof.web.service.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static com.yusof.web.api.controller.SessionUtils.getClientId;

@RequiredArgsConstructor
@Controller
public class WebMenu {
    private final AuthorizationService authorizationService;
    private final AccountService accountService;
    private final TransactionCategoryService transactionCategoryService;

    @GetMapping("/menu")
    public String getMenu(Model model,
                          HttpServletRequest request,
                          @RequestParam(name = "show", required = false) String show) {
        try {
            Integer clientId = getClientId(request);

            ClientDTO client = authorizationService.getClientById(clientId);

            model.addAttribute("id", client.getId())
                    .addAttribute("name", client.getEmail());


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
        } catch (UnauthorizedException e) {
            return "redirect:/login";
        }
    }
}
