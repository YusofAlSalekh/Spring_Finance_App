package com.yusof.web.web.controller;

import com.yusof.web.exceptions.AlreadyExistsException;
import com.yusof.web.exceptions.NotFoundException;
import com.yusof.web.exceptions.UnauthorizedException;
import com.yusof.web.service.AccountDTO;
import com.yusof.web.service.AccountService;
import com.yusof.web.web.form.AccountCreationForm;
import com.yusof.web.web.form.AccountDeletionForm;
import com.yusof.web.web.form.AccountNameChangingForm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/account")
public class WebAccountController extends AbstractWebController {
    private final AccountService accountService;

    @GetMapping("/create")
    public String getAccountCreation(Model model) {
        model.addAttribute("form", new AccountCreationForm());

        return "accountCreation";
    }

    @PostMapping("/create")
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

    @GetMapping("/delete")
    public String getAccountDeletion(Model model) {
        model.addAttribute("form", new AccountDeletionForm());

        return "accountDeletion";
    }

    @PostMapping("/delete")
    public String postAccountDeletion(@ModelAttribute("form") @Valid AccountDeletionForm form,
                                      BindingResult bindingResult,
                                      RedirectAttributes redirectAttributes,
                                      HttpServletRequest request) {
        if (!bindingResult.hasErrors()) {
            try {
                Integer clientId = getClientId(request);
                accountService.deleteAccount(
                        form.getAccountId(),
                        clientId
                );
                redirectAttributes.addFlashAttribute(
                        "success",
                        "Account with id " + form.getAccountId() + " was deleted."
                );
                return "redirect:/menu";
            } catch (UnauthorizedException e) {
                return "redirect:/login";
            } catch (NotFoundException e) {
                bindingResult.reject("",
                        "No account found with id " + form.getAccountId());
            }
        }
        return "accountDeletion";
    }

    @GetMapping("/update")
    public String getAccountNameChanging(Model model) {
        model.addAttribute("form", new AccountNameChangingForm());

        return "accountNameChanging";
    }

    @PostMapping("/update")
    public String postAccountNameChanging(@ModelAttribute("form") @Valid AccountNameChangingForm form,
                                          BindingResult bindingResult,
                                          RedirectAttributes redirectAttributes,
                                          HttpServletRequest request) {
        if (!bindingResult.hasErrors()) {
            try {
                Integer clientId = getClientId(request);
                AccountDTO updatedAccountName = accountService.updateAccountName(
                        form.getName(),
                        form.getAccountId(),
                        clientId);

                redirectAttributes.addFlashAttribute("success",
                        "Account's name was updates, new name is " + updatedAccountName.getName());

                return "redirect:/menu";
            } catch (UnauthorizedException e) {
                return "redirect:/login";
            } catch (AlreadyExistsException e) {
                bindingResult.reject("",
                        "The account with name \"" + form.getName() + "\" already exists");
            } catch (NotFoundException e) {
                bindingResult.reject("",
                        "No account found with id: " + form.getAccountId());
            }
        }
        return "accountNameChanging";
    }
}
