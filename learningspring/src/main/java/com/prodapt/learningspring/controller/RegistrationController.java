package com.prodapt.learningspring.controller;


import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.prodapt.learningspring.controller.binding.RegistrationForm;
import com.prodapt.learningspring.controller.exception.CustomUserNotFoundException;
import com.prodapt.learningspring.entity.User;
import com.prodapt.learningspring.model.RandomString;
import com.prodapt.learningspring.service.DomainUserService;


@Controller
@RequestMapping("/register")
public class RegistrationController {
    
    @Autowired
    private DomainUserService domainUserService;

    @GetMapping
    public String getRegistrationForm(Model model) {
        System.out.println("hello world");
        if (!model.containsAttribute("registrationForm")) {
            model.addAttribute("registrationForm", new RegistrationForm());
          }
        return "register";
    }

    @PostMapping
    public String register(@ModelAttribute("registrationForm") RegistrationForm registrationForm, 
    BindingResult bindingResult, 
    RedirectAttributes attr) {
        if (bindingResult.hasErrors()) {
        attr.addFlashAttribute("org.springframework.validation.BindingResult.registrationForm", bindingResult);
        attr.addFlashAttribute("registrationForm", registrationForm);
        return "redirect:/register";
        }
        if (!registrationForm.isValid()) {
        attr.addFlashAttribute("message", "Passwords must match");
        attr.addFlashAttribute("registrationForm", registrationForm);
        return "redirect:/register";
        }
        String token = RandomString.make(6);
        try {
		BufferedImage bufferedImage = generateQRCodeImage(token);
		File outputfile = new File("/home/abbagownianush/Downloads/image_"+registrationForm.getName()+".jpg");
		ImageIO.write(bufferedImage, "jpg", outputfile);
		}catch (Exception e) {
			e.getMessage();
		}
        attr.addFlashAttribute("result", "Registration success!");
        domainUserService.save(registrationForm.getName(), registrationForm.getPassword(), registrationForm.getEmail(),registrationForm.getDateOfBirth(),token);
        return "redirect:/register/success?email="+registrationForm.getEmail();
    }

    @GetMapping("/success")
    public String getSuccess(Model model,String email){
        model.addAttribute("email", email);
        return "success";
    }

    @PostMapping("/success")
    public String onSuccess(String Otp,Model model,String email) throws CustomUserNotFoundException
    {
        User user = domainUserService.findUserByEmail(email);
        if(Otp.equals(user.getToken())){
            domainUserService.updateResetPassword(email, null);
            return "redirect:/login";
        }
        else{
            model.addAttribute("success", "wrong OTP");
            return "redirect:/register/success";
        }
    }

    public static BufferedImage generateQRCodeImage(String token) throws Exception {
		StringBuilder str = new StringBuilder();
		str = str.append(token);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        BitMatrix bitMatrix = 
	      qrCodeWriter.encode(str.toString(), BarcodeFormat.QR_CODE, 200, 200);

	    return MatrixToImageWriter.toBufferedImage(bitMatrix);
	}


}
