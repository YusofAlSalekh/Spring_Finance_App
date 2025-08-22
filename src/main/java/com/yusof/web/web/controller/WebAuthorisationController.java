package com.yusof.web.web.controller;

import com.yusof.web.exceptions.BadCredentialsException;
import com.yusof.web.service.AuthorizationService;
import com.yusof.web.service.ClientDTO;
import com.yusof.web.web.form.AuthorisationForm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class WebAuthorisationController {
    private final AuthorizationService authorizationService;

    @GetMapping("/login")
    public String getAuthorisation(Model model) {
        model.addAttribute("form", new AuthorisationForm());

        return "authorisation";
    }

    @PostMapping("/login")
    public String postAuthorisation(@ModelAttribute("form") @Valid AuthorisationForm form,
                                    BindingResult bindingResult,
                                    HttpServletRequest request) {

        if (!bindingResult.hasErrors()) {
            try {
                ClientDTO clientDTO = authorizationService.authorize(
                        form.getEmail(),
                        form.getPassword());

                HttpSession session = request.getSession();
                session.setAttribute("clientId", clientDTO.getId());
                return "redirect:/menu";
            } catch (BadCredentialsException e) {
                bindingResult.reject("authorisation.failed", "Email or password is invalid");
            }
        }
        return "authorisation";
    }
}
