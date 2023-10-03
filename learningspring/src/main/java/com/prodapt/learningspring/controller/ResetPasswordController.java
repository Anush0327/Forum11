package com.prodapt.learningspring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.prodapt.learningspring.controller.binding.RegistrationForm;
import com.prodapt.learningspring.entity.User;
import com.prodapt.learningspring.service.DomainUserService;

@Controller
@RequestMapping("/resetPassword")
public class ResetPasswordController {
    
    @Autowired
    private DomainUserService domainUserService;

    @GetMapping
    public String resetPassword(Model model,@RequestParam String token){
        model.addAttribute("registrationForm", new RegistrationForm());
        model.addAttribute("token", token);
        return "resetpasswordform";
    }
    @PostMapping
    public String afterReset(@ModelAttribute("registrationForm") RegistrationForm registrationForm,String token, BindingResult bindingResult,RedirectAttributes attr){
        if (bindingResult.hasErrors()) {
            attr.addFlashAttribute("org.springframework.validation.BindingResult.registrationForm", bindingResult);
            attr.addFlashAttribute("registrationForm", registrationForm);
            return "redirect:/resetPassword";
        }
        if (!registrationForm.isValid()) {
            attr.addFlashAttribute("message", "Passwords must match");
            attr.addFlashAttribute("registrationForm", registrationForm);
            return "redirect:/resetPassword";
        }
        System.out.println(token);
        domainUserService.updatePassword(registrationForm.getPassword(), token);
        return "redirect:/forum/post/form";
    }
}
