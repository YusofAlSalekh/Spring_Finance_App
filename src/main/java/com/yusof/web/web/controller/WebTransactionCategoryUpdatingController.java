package com.yusof.web.web.controller;

import com.yusof.web.exceptions.AlreadyExistsException;
import com.yusof.web.exceptions.IllegalOwnerException;
import com.yusof.web.exceptions.NotFoundException;
import com.yusof.web.exceptions.UnauthorizedException;
import com.yusof.web.service.TransactionCategoryService;
import com.yusof.web.web.form.TransactionCategoryUpdatingForm;
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
public class WebTransactionCategoryUpdatingController {
    private final TransactionCategoryService transactionCategoryService;

    @GetMapping("/category/update")
    public String getTransactionCategoryUpdating(Model model) {
        model.addAttribute("form", new TransactionCategoryUpdatingForm());
        return "transactionCategoryUpdating";
    }

    @PostMapping("/category/update")
    public String postTransactionCategoryUpdating(
            @ModelAttribute("form") @Valid TransactionCategoryUpdatingForm form,
            BindingResult bindingResult,
            HttpServletRequest request, RedirectAttributes redirectAttributes) {

        if (!bindingResult.hasErrors()) {
            try {
                Integer clientId = getClientId(request);

                transactionCategoryService.updateTransactionCategory(
                        form.getName(),
                        form.getId(),
                        clientId);

                redirectAttributes.addFlashAttribute(
                        "success",
                        "New  category name is \"" + form.getName());

                return "redirect:/menu";
            } catch (UnauthorizedException e) {
                return "redirect:/login";
            } catch (AlreadyExistsException e) {
                bindingResult.reject("",
                        "The category with name \"" + form.getName() + "\" already exists");
            } catch (NotFoundException e) {
                bindingResult.reject("",
                        "No category found with id: " + form.getId());
            } catch (IllegalOwnerException e) {
                bindingResult.reject("",
                        "Category with id: " + form.getId() + " belongs to another client");
            }
        }
        return "transactionCategoryUpdating";
    }
}
