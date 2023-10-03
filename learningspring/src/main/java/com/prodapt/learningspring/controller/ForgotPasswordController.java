package com.prodapt.learningspring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.prodapt.learningspring.controller.exception.CustomUserNotFoundException;
import com.prodapt.learningspring.model.EmailDetails;
import com.prodapt.learningspring.model.RandomString;
import com.prodapt.learningspring.service.DomainUserService;
import com.prodapt.learningspring.service.EmailServiceImpl;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/forgotPassword")
public class ForgotPasswordController {
    
    @Autowired
    private DomainUserService domainUserService;

    @GetMapping
    public String forgotPasswordForm(Model model){
        model.addAttribute("title", "Forgot Password");
        return "forgotpasswordform";
    }
    @PostMapping
    public String updatePassword(HttpServletRequest req,String email){
        System.out.println("hello world");
        String token = RandomString.make(45);
        try {
            domainUserService.updateResetPassword(email, token);
            String link = req.getRequestURL().toString().replace(req.getServletPath(),"") + "/resetPassword?token="+token;
            System.out.println(link);

        } catch (CustomUserNotFoundException e) {
            e.printStackTrace();
        }
        return "redirect:/forgotPassword";
    }
}
