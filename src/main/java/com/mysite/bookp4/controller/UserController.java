package com.mysite.bookp4.controller;

import com.mysite.bookp4.dto.LoanDTO;
import com.mysite.bookp4.dto.UserDTO;
import com.mysite.bookp4.dto.UserFilterDTO;
import com.mysite.bookp4.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

//  @GetMapping("/users")
//  public String users(@RequestParam(defaultValue = "0") int page, Model model) {
//    model.addAttribute("users", userService.getAllUser(page));
//    model.addAttribute("filter", new UserFilterDTO());
//    model.addAttribute("loggedInUser", userService.getLoggedInUser());//로그인 유저 정보 비활성화
//    return "user-list";
//  }

//  @GetMapping("/filterUsers")
//  public String filterUsers(@RequestParam(value = "type", required = false) String type,
//                            @RequestParam(value = "text", required = false) String text,
//                            @RequestParam(defaultValue = "0") int page,
//                            Model model) throws Exception {
//    Page<UserDTO> list = userService.searchUsers(type, text, page);
//    model.addAttribute("users", list);
//    for (UserDTO userDTO : list) {
//      System.out.println("여 " + userDTO);
//    }
//    System.out.println("여기");
//    return "user-list";
//  }

  @GetMapping("/users")
  public String showUserList(@RequestParam(value = "type", required = false) String type,
                             @RequestParam(value = "text", required = false) String text,
                             @RequestParam(defaultValue = "0") int page,
                             Model model) {
    Page<UserDTO> users;
    if (type != null && text != null) {
      users = userService.searchUsers(type, text, page);
    } else {
      users = userService.getAllUser(page);
    }
    model.addAttribute("users", users.getContent());
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", users.getTotalPages());
    model.addAttribute("type", type);
    model.addAttribute("text", text);
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
    // 다른 경우에는 기본값으로 users 페이지로 리다이렉트
    return userService.getLoggedInUser().getRole() == 1 ? "redirect:/users" : "redirect:/main";
  }

  //삭제하기
  @GetMapping("/deleteUser")
  public String deleteUser(@RequestParam("userId") Long userId) {
    System.out.println("삭제번호:" + userId);
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

  @GetMapping("/main/updateUser")
  public String updateUser2(Model model) {
    model.addAttribute("user", userService.getLoggedInUser());
    return "user-register";
  }
}
