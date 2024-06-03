package com.mysite.bookp4.controller;

import com.mysite.bookp4.dto.UserDTO;
import com.mysite.bookp4.dto.UserFilterDTO;
import com.mysite.bookp4.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserFilterController {

    private final UserService userService;


    @GetMapping("/filterUsers")
    public String filterUsers(@ModelAttribute("filter") UserFilterDTO userFilterDTO, Model model) throws Exception {
        System.out.println(userFilterDTO);
        List<UserDTO> list = userService.getFilterUsers(userFilterDTO.getKeyword(),userFilterDTO.getSearchBy());
        model.addAttribute("users", list);
        for (UserDTO userDTO : list) {
            System.out.println("여 "+userDTO);
        }
        System.out.println("여기");
        return "user-list";
    }
}