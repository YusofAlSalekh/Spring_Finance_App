package com.yusof.web.web.controller;

import com.yusof.web.exceptions.AlreadyExistsException;
import com.yusof.web.service.AuthenticationService;
import com.yusof.web.service.ClientDTO;
import com.yusof.web.web.form.RegistrationForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class WebAuthenticationController {
    private final AuthenticationService authenticationService;

    @GetMapping("/login")
    public String getAuthorisation() {
        return "authorisation";
    }

    /*@PostMapping("/login")
    public String postAuthorisation(@ModelAttribute("form") @Valid AuthorisationForm form,
                                    BindingResult bindingResult,
                                    HttpServletRequest request) {

        if (!bindingResult.hasErrors()) {
            try {
                ClientDTO clientDTO = authenticationService.authorize(
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
    }*/

    @GetMapping("/register")
    public String getRegistration(Model model) {
        model.addAttribute("form", new RegistrationForm());
        return "registration";
    }

    @PostMapping("/register")
    public String postRegistration(@ModelAttribute("form") @Valid RegistrationForm form,
                                   BindingResult bindingResult,
                                   RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasErrors()) {
            try {
                ClientDTO clientDTO = authenticationService.register(
                        form.getLogin(),
                        form.getPassword());

                redirectAttributes.addFlashAttribute("success", "User with login \"" + clientDTO.getEmail() + "\" successfully registered!");
                return "redirect:/login";
            } catch (AlreadyExistsException e) {
                bindingResult.rejectValue("login", "", e.getMessage());
            }
        }
        return "registration";
    }
}
