package com.mysite.bookp4.controller;

import com.mysite.bookp4.dto.UserDTO;
import com.mysite.bookp4.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.text.ParseException;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping({"/login","/"})
    public String showLoginPage(Principal principal) {
        System.out.println("로그인 컨트롤러");
        if (principal == null) {
            return "login";
        }
       return "redirect:/users";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new UserDTO());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") UserDTO user, Model model) throws ParseException {
        System.out.println("유저DTO객체 :"+user);
        userService.saveUserDetails(user);
        return "redirect:/login";
    }

    @PostMapping("/registUser")
    public String registUser(@Valid @ModelAttribute("user") UserDTO user, BindingResult result, Model model) throws ParseException {
        System.out.println("유저DTO객체 :"+user);
        if (result.hasErrors()) {
            return "register";
        }
        userService.saveUserDetails(user);
        //redirectAttributes.addFlashAttribute("successMsg", true);
        model.addAttribute("successMsg", true);
        return "/login";
    }
}