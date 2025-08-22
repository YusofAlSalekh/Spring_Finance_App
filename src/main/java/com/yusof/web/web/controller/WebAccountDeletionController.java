package com.yusof.web.web.controller;

import com.yusof.web.exceptions.IllegalOwnerException;
import com.yusof.web.exceptions.NotFoundException;
import com.yusof.web.exceptions.OperationFailedException;
import com.yusof.web.exceptions.UnauthorizedException;
import com.yusof.web.service.AccountService;
import com.yusof.web.web.form.AccountDeletionForm;
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
public class WebAccountDeletionController {
    private final AccountService accountService;

    @GetMapping("/account/delete")
    public String getAccountDeletion(Model model) {
        model.addAttribute("form", new AccountDeletionForm());

        return "accountDeletion";
    }

    @PostMapping("/account/delete")
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
            } catch (OperationFailedException e) {
                bindingResult.reject(
                        "",
                        "Failed to delete the account."
                );
            } catch (IllegalOwnerException e) {
                bindingResult.reject("",
                        "Account with id " + form.getAccountId() + " belongs to another client");
            } catch (NotFoundException e) {
                bindingResult.reject("",
                        "No account found with id " + form.getAccountId());
            }
        }
        return "accountDeletion";
    }
}


