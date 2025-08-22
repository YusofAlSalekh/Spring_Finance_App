package com.yusof.web.web.controller;

import com.yusof.web.api.controller.TransactionCategoryDeletionController;
import com.yusof.web.exceptions.IllegalOwnerException;
import com.yusof.web.exceptions.NotFoundException;
import com.yusof.web.exceptions.OperationFailedException;
import com.yusof.web.exceptions.UnauthorizedException;
import com.yusof.web.service.TransactionCategoryService;
import com.yusof.web.web.form.TransactionCategoryDeletionForm;
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
public class WebTransactionCategoryDeletionController {
    private final TransactionCategoryDeletionController transactionCategoryDeletionController;
    private final TransactionCategoryService transactionCategoryService;

    @GetMapping("/category/delete")
    public String getTransactionCategoryDeletion(Model model) {
        model.addAttribute("form", new TransactionCategoryDeletionForm());
        return "transactionCategoryDeletion";
    }

    @PostMapping("/category/delete")
    public String postTransactionCategoryDeletion(
            @ModelAttribute("form") @Valid TransactionCategoryDeletionForm form,
            BindingResult bindingResult,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasErrors()) {
            try {
                Integer clientId = getClientId(request);

                transactionCategoryService.deleteTransactionCategory(form.getId(), clientId);

                redirectAttributes.addFlashAttribute(
                        "success",
                        "Category with id " + form.getId() + " was deleted."
                );
                return "redirect:/menu";

            } catch (UnauthorizedException e) {
                return "redirect:/login";
            } catch (OperationFailedException e) {
                bindingResult.reject(
                        "",
                        "Failed to delete the category."
                );
            } catch (IllegalOwnerException e) {
                bindingResult.reject("",
                        "Category with id " + form.getId() + " belongs to another client");
            } catch (NotFoundException e) {
                bindingResult.reject("",
                        "No category found with id " + form.getId());
            }
        }
        return "transactionCategoryDeletion";
    }
}
