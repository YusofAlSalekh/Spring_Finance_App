package com.yusof.web.web.controller;

import com.yusof.web.exceptions.AlreadyExistsException;
import com.yusof.web.exceptions.UnauthorizedException;
import com.yusof.web.service.TransactionCategoryDTO;
import com.yusof.web.service.TransactionCategoryService;
import com.yusof.web.web.form.TransactionCategoryCreationForm;
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
public class WebTransactionCategoryCreationController {
    private final TransactionCategoryService transactionCategoryService;

    @GetMapping("/category/create")
    public String getTransactionCategoryCreation(Model model) {
        model.addAttribute("form", new TransactionCategoryCreationForm());

        return "transactionCategoryCreation";
    }

    @PostMapping("/category/create")
    public String postTransactionCategoryCreation(@ModelAttribute("form") @Valid TransactionCategoryCreationForm form,
                                                  BindingResult bindingResult,
                                                  HttpServletRequest request, RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasErrors()) {
            try {
                Integer clientId = getClientId(request);

                TransactionCategoryDTO transactionCategory = transactionCategoryService.createCategory(
                        form.getName(),
                        clientId);

                redirectAttributes.addFlashAttribute(
                        "success",
                        "Category \"" + transactionCategory.getName() + "\" created.");

                return "redirect:/menu";
            } catch (UnauthorizedException e) {
                return "redirect:/login";
            } catch (AlreadyExistsException e) {
                bindingResult.rejectValue("name", "", e.getMessage());
            }
        }
        return "transactionCategoryCreation";
    }
}
