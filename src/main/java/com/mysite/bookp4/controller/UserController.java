package com.mysite.bookp4.controller;

import com.mysite.bookp4.dto.UserDTO;
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
    public String deleteUser(@RequestParam("user_id") Long user_id) {
        System.out.println("삭제번호:"+user_id);
        userService.deleteUser(user_id);
        return "redirect:/users";
    }

    //수정 보이기
    @GetMapping("/updateUser")
    public String updateUser(@RequestParam Long user_id, Model model) {
        System.out.println("업데이트 아이템 : " + user_id);
        model.addAttribute("user", userService.getUser(user_id));
        return "user-form";
    }

}
