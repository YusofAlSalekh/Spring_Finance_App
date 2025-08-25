package com.yusof.web.web.controller;

import com.yusof.web.entity.CategoryReportModel;
import com.yusof.web.exceptions.AlreadyExistsException;
import com.yusof.web.exceptions.NotFoundException;
import com.yusof.web.exceptions.UnauthorizedException;
import com.yusof.web.service.TransactionCategoryDTO;
import com.yusof.web.service.TransactionCategoryService;
import com.yusof.web.service.TransactionService;
import com.yusof.web.web.form.*;
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

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/category")
public class WebTransactionCategoryController extends AbstractWebController {
    private final TransactionCategoryService transactionCategoryService;
    private final TransactionService transactionService;

    @GetMapping("/create")
    public String getTransactionCategoryCreation(Model model) {
        model.addAttribute("form", new TransactionCategoryCreationForm());

        return "transactionCategoryCreation";
    }

    @PostMapping("/create")
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

    @GetMapping("/delete")
    public String getTransactionCategoryDeletion(Model model) {
        model.addAttribute("form", new TransactionCategoryDeletionForm());
        return "transactionCategoryDeletion";
    }

    @PostMapping("/delete")
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
            } catch (NotFoundException e) {
                bindingResult.reject("",
                        "No category found with id " + form.getId());
            }
        }
        return "transactionCategoryDeletion";
    }

    @GetMapping("/update")
    public String getTransactionCategoryUpdating(Model model) {
        model.addAttribute("form", new TransactionCategoryUpdatingForm());
        return "transactionCategoryUpdating";
    }

    @PostMapping("/update")
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
            }
        }
        return "transactionCategoryUpdating";
    }

    @GetMapping("/report/expense")
    public String getExpenseReport(Model model) {
        model.addAttribute("form", new ExpenseReportForm());
        return "expenseReport";
    }

    @PostMapping("/report/expense")
    public String postExpenseReport(@ModelAttribute("form") @Valid ExpenseReportForm form,
                                    BindingResult bindingResult,
                                    HttpServletRequest request,
                                    Model model) {
        if (!bindingResult.hasErrors()) {
            Integer clientId = getClientId(request);

            List<CategoryReportModel> transactions = transactionService.getExpenseReportByCategory(clientId,
                    form.getStartDate(),
                    form.getEndDate());

            model.addAttribute("transactions", transactions);
            return "expenseReport";
        }
        return "expenseReport";
    }

    @GetMapping("/report/income")
    public String getIncomeReport(Model model) {
        model.addAttribute("form", new IncomeReportForm());
        return "incomeReport";
    }

    @PostMapping("/report/income")
    public String postIncomeReport(@ModelAttribute("form") @Valid IncomeReportForm form,
                                   BindingResult bindingResult,
                                   HttpServletRequest request,
                                   Model model) {
        if (!bindingResult.hasErrors()) {
            Integer clientId = getClientId(request);

            List<CategoryReportModel> transactions = transactionService.getIncomeReportByCategory(
                    clientId,
                    form.getStartDate(),
                    form.getEndDate());

            model.addAttribute("transactions", transactions);
            return "incomeReport";
        }
        return "incomeReport";
    }
}
