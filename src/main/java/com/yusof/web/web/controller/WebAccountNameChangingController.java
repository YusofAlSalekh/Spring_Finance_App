package com.yusof.web.web.controller;

import com.yusof.web.exceptions.AlreadyExistsException;
import com.yusof.web.exceptions.IllegalOwnerException;
import com.yusof.web.exceptions.NotFoundException;
import com.yusof.web.exceptions.UnauthorizedException;
import com.yusof.web.service.AccountDTO;
import com.yusof.web.service.AccountService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.yusof.web.api.controller.SessionUtils.getClientId;

@Controller
@RequiredArgsConstructor
public class WebAccountNameChangingController {
    private final AccountService accountService;

    @GetMapping("/account/update")
    public String getAccountNameChanging(Model model) {
        model.addAttribute("form", new AccountNameChangingForm());

        return "accountNameChanging";
    }

    @PostMapping("/account/update")
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
            } catch (IllegalOwnerException e) {
                bindingResult.reject("",
                        "Account with id: " + form.getAccountId() + " belongs to another client");
            }
        }
        return "accountNameChanging";
    }
}

