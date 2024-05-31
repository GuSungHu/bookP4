package com.mysite.bookp4.controller;

import com.mysite.bookp4.dto.UserDTO;
import com.mysite.bookp4.dto.UserFilterDTO;
import com.mysite.bookp4.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userService.getAllUser());
        model.addAttribute("filter", new UserFilterDTO());
        //model.addAttribute("loggedInUser", userService.getLoggedInUser());//로그인 유저 정보 비활성화
        return "user-list";
    }
    /*
    @GetMapping("/createUser")
    public String createUser(Model model) {
        model.addAttribute("user", new UserDTO());
        return "user-form";
    }*/

    @GetMapping("/createUser")
    public String createUser(Model model) {
        model.addAttribute("user", new UserDTO());
        return "user-register";
    }


    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("user") UserDTO userDTO) throws ParseException {
        System.out.println("입력한 userDTO 객체 : " + userDTO);
        userService.saveUserDetails(userDTO);
        return "redirect:/users";
    }

    //삭제하기
    @GetMapping("/deleteUser")
    public String deleteUser(@RequestParam("userId") Long userId) {
        System.out.println("삭제번호:"+userId);
        userService.deleteUser(userId);
        return "redirect:/users";
    }

    //수정 보이기
    @GetMapping("/updateUser")
    public String updateUser(@RequestParam Long userId, Model model) {
        System.out.println("업데이트 아이템 : " + userId);
        model.addAttribute("user", userService.getUser(userId));
        return "user-register";
    }



}
