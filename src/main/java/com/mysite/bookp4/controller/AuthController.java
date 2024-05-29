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

import java.text.ParseException;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping({"/login","/"})
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new UserDTO());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") UserDTO user) {
        System.out.println("유저DTO객체 :"+user);
        return "redirect:/login";
    }

    @PostMapping("/registUser")
    public String registUser(@ModelAttribute("user") UserDTO userDTO) throws ParseException {
        System.out.println("입력한 userDTO 객체 : " + userDTO);
        userService.saveUserDetails(userDTO);
        return "redirect:/login";
    }
}