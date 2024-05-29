package com.mysite.bookp4.controller;

import com.mysite.bookp4.dto.LoanDTO;
import com.mysite.bookp4.dto.UserDTO;
import com.mysite.bookp4.repository.UserRepository;
import com.mysite.bookp4.service.LoanService;
import com.mysite.bookp4.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class LoanController {

  private final LoanService loanService;
  private final UserService userService;
  private final UserRepository userRepository;

  @GetMapping("/loan")
  public String showBorrowList(@RequestParam(value = "type", required = false) String type,
                               @RequestParam(value = "text", required = false) String text,
                               Model model) {
    List<LoanDTO> dtoList;
    if (type != null && text != null) {
      dtoList = loanService.searchLoans(type, text);
    } else {
      dtoList = loanService.getAllLoans();
    }
    model.addAttribute("dtoList", dtoList);
    return "loan-list";
  }

  @GetMapping("/loan/{id}")
  public String showLoanDetail(@PathVariable("id") Long id, Model model) {
    LoanDTO loan = loanService.getLoanById(id);
    List<LoanDTO> userLoans = loanService.getUnreturnedLoansByUserId(loan.getUserId());
    model.addAttribute("loan", loan);
    model.addAttribute("userLoans", userLoans);
    return "loan-detail";
  }
  
  @GetMapping("/loan/user/{userId}")
  public String showUserDetail(@PathVariable("userId") Long userId, Model model) {
    UserDTO user = userService.getUser(userId);
    List<LoanDTO> userLoans = loanService.getUnreturnedLoansByUserId(userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid loan Id:" + userId)));
    model.addAttribute("user", user); //UserDTO user
    model.addAttribute("userLoans", userLoans);
    return "user-detail";
  }

  @PostMapping("/loan/{id}/extend")
  public String extendLoan(@PathVariable("id") Long id) {
    loanService.extendDueDate(id, 3);
    return "redirect:/loan/" + id;
  }

  @PostMapping("/loan/{userId}/return")
  public String returnLoan(@PathVariable("userId") Long id) {
    loanService.returnLoan(id);
    return "redirect:/loan/" + id;
  }

  @PostMapping("/loan/user/{userId}/extend")
  public String extendLoan2(@RequestParam("loanId") Long loanId, @PathVariable("userId") Long userId) {
    loanService.extendDueDate(loanId, 3);
    return "redirect:/loan/user/" + userId;
  }

  @PostMapping("/loan/user/{userId}/return")
  public String returnLoan2(@RequestParam("loanId") Long loanId, @PathVariable("userId") Long userId) {
    loanService.returnLoan(loanId);
    return "redirect:/loan/user/" + userId;
  }

  @GetMapping("/admin")
  public String showBorrowList(Model model) {
    List<LoanDTO> dtoList = loanService.getUnreturnedLoansOrderedByDueDate();
    model.addAttribute("dtoList", dtoList);
    return "admin-main";
  }
}
