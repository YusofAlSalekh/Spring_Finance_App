package com.yusof.web.web.controller;

import com.yusof.web.exceptions.AlreadyExistsException;
import com.yusof.web.exceptions.UnauthorizedException;
import com.yusof.web.service.AccountDTO;
import com.yusof.web.service.AccountService;
import com.yusof.web.web.form.AccountCreationForm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.yusof.web.api.controller.SessionUtils.getClientId;

@Controller
@RequiredArgsConstructor
public class WebAccountCreationController {
    private final AccountService accountService;

    @GetMapping("/account/create")
    public String getAccountCreation(Model model) {
        model.addAttribute("form", new AccountCreationForm());

        return "accountCreation";
    }

    @PostMapping("/account/create")
    public String postAccountCreation(@ModelAttribute("form") @Valid AccountCreationForm form,
                                      BindingResult bindingResult,
                                      RedirectAttributes redirectAttributes,
                                      HttpServletRequest request) {

        if (!bindingResult.hasErrors()) {
            try {
                Integer clientId = getClientId(request);
                AccountDTO accountDTO = accountService.createAccount(
                        form.getName(),
                        form.getBalance(),
                        clientId);

                redirectAttributes.addFlashAttribute(
                        "success",
                        "Account \"" + accountDTO.getName() + "\" created."
                );

                return "redirect:/menu";
            } catch (UnauthorizedException e) {
                return "redirect:/login";
            } catch (AlreadyExistsException e) {
                bindingResult.rejectValue("name", "", e.getMessage());
            }
        }
        return "accountCreation";
    }
}
