package com.mysite.bookp4.controller;

import com.mysite.bookp4.dto.LoanDTO;
import com.mysite.bookp4.dto.UserDTO;
import com.mysite.bookp4.entity.Book;
import com.mysite.bookp4.entity.Loan;
import com.mysite.bookp4.entity.User;
import com.mysite.bookp4.repository.BookRepository;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.mysite.bookp4.util.DateTimeUtil.ldtToString;

@Controller
@RequiredArgsConstructor
public class LoanController {

  private final LoanService loanService;
  private final UserService userService;
  private final UserRepository userRepository;
  private final BookRepository bookRepository;

  @GetMapping("/loan")
  public String showLoanList(@RequestParam(value = "type", required = false) String type,
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

  @PostMapping("/main/extend")
  public String extendLoan3(@RequestParam("loanId") Long loanId, @RequestParam("userId") Long userId, Model model) {
    User user = userService.getUserById(userId);

    loanService.extendDueDate(loanId, 3);
    model.addAttribute("user", user);
    return "redirect:/main";
  }

  @GetMapping("/main/loan")
  public String showLoanInput(@RequestParam Long userId, Model model) {
    model.addAttribute("userId", userId);
    model.addAttribute("loan", "loan");
    return "loan-input";
  }

  @GetMapping("/main/return")
  public String showReturnInput(@RequestParam Long userId, Model model) {
    model.addAttribute("userId", userId);
    model.addAttribute("loan", "return");
    return "loan-input";
  }

  @PostMapping("/main/loan/form")
  public String showLoanForm(@RequestParam Long userId, @RequestParam Long bookId, Model model) {
    Book book = bookRepository.findByBookId(bookId).orElseThrow(()-> new IllegalArgumentException("Invalid bookId:" + bookId));
    model.addAttribute("userId", userId);
    model.addAttribute("book", book);
    model.addAttribute("loan", "loan");
    model.addAttribute("loan_date", ldtToString(LocalDateTime.now()));
    model.addAttribute("due_date", ldtToString(LocalDateTime.now().plusDays(7)));
    return "loan-form";
  }

  @PostMapping("/main/return/form")
  public String showReturnForm(@RequestParam Long userId, @RequestParam Long bookId, Model model) {
    Book book = bookRepository.findByBookId(bookId).orElseThrow(()-> new IllegalArgumentException("Invalid bookId:" + bookId));
    Loan loan = loanService.searchFromUserAndBook(userId, bookId);
    model.addAttribute("userId", userId);
    model.addAttribute("book", book);
    model.addAttribute("loan", "return");
    model.addAttribute("loan_date", ldtToString(loan.getLoan_date()));
    model.addAttribute("due_date", ldtToString(loan.getDue_date()));
    return "loan-form";
  }

  @PostMapping("/main/createLoan")
  public String loanBook(@RequestParam Long bookId, @RequestParam Long userId, Model model) {
    try {
      loanService.addLoan(userId, bookId);
      model.addAttribute("message", "Book loaned successfully");
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
    }
    return "redirect:/main";
  }

  @PostMapping("/main/returnLoan")
  public String returnBook(@RequestParam Long bookId, @RequestParam Long userId, Model model) {
    try {
      loanService.returnLoan2(userId, bookId);
      model.addAttribute("message", "Book returned successfully");
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
    }
    return "redirect:/main";
  }
}
