package com.prodapt.learningspring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.prodapt.learningspring.entity.User;
import com.prodapt.learningspring.service.DomainUserService;

@Controller
public class LoginController {


    @GetMapping("/login")
    public String login(){
        return "login";
    }
}
