package com.mysite.bookp4.controller;

import com.mysite.bookp4.dto.LoanDTO;
import com.mysite.bookp4.dto.UserDTO;
import com.mysite.bookp4.entity.Book;
import com.mysite.bookp4.entity.Loan;
import com.mysite.bookp4.entity.User;
import com.mysite.bookp4.repository.BookRepository;
import com.mysite.bookp4.repository.LoanRepository;
import com.mysite.bookp4.repository.UserRepository;
import com.mysite.bookp4.service.BookService;
import com.mysite.bookp4.service.LoanService;
import com.mysite.bookp4.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.mysite.bookp4.util.DateTimeUtil.ldtToString;

@Controller
@RequiredArgsConstructor
public class LoanController {

  private final LoanService loanService;
  private final UserService userService;
  private final BookService bookService;
  private final UserRepository userRepository;
  private final BookRepository bookRepository;
  private final LoanRepository loanRepository;

  @GetMapping("/loan")
  public String showLoanList(@RequestParam(value = "type", required = false) String type,
                             @RequestParam(value = "text", required = false) String text,
                             @RequestParam(defaultValue = "0") int page,
                             Model model) {
    Page<LoanDTO> dtoList;
    if (type != null && text != null) {
      dtoList = loanService.searchLoans(type, text, page);
    } else {
      dtoList = loanService.getAllLoans(page);
    }
    model.addAttribute("dtoList", dtoList.getContent());
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", dtoList.getTotalPages());
    model.addAttribute("type", type);
    model.addAttribute("text", text);
    return "loan-list";
  }

  @GetMapping("/loan/{id}")
  public String showLoanDetail(@PathVariable("id") Long id,
                               @RequestParam(defaultValue = "0") int page,
                               Model model) {
    LoanDTO loan = loanService.getLoanById(id);
    List<LoanDTO> userLoans = loanService.getUnreturnedLoansByUserId(loan.getUserId());
    model.addAttribute("loan", loan);
    model.addAttribute("userLoans", userLoans);
    return "loan-detail";
  }

  @GetMapping("/loan/user/{userId}")
  public String showUserDetail(@PathVariable("userId") Long userId,
                               @RequestParam(defaultValue = "0") int page,
                               Model model) {
    UserDTO user = userService.getUser(userId);
    List<LoanDTO> userLoans = loanService.getUnreturnedLoansByUserId(userRepository.findByUserId(userId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid loan Id:" + userId)));
    model.addAttribute("user", user); //UserDTO user
    model.addAttribute("userLoans", userLoans);
    return "user-detail";
  }

  @PostMapping("/loan/{id}/extend")
  public String extendLoan(@PathVariable("id") Long id) {
    loanService.extendDueDate(id, 1);
    return "redirect:/loan/" + id;
  }

  @PostMapping("/loan/{userId}/return")
  public String returnLoan(@PathVariable("userId") Long id) {
    loanService.returnLoan(id);
    return "redirect:/loan/" + id;
  }

  @PostMapping("/loan/user/{userId}/extend")
  public String extendLoan2(@RequestParam("loanId") Long loanId, @PathVariable("userId") Long userId) {
    loanService.extendDueDate(loanId, 1);
    return "redirect:/loan/user/" + userId;
  }

  @PostMapping("/loan/user/{userId}/return")
  public String returnLoan2(@RequestParam("loanId") Long loanId, @PathVariable("userId") Long userId) {
    loanService.returnLoan(loanId);
    return "redirect:/loan/user/" + userId;
  }

  @GetMapping("/admin")
  public String showBorrowList(@RequestParam(defaultValue = "0") int page, Model model) {
    Page<LoanDTO> dtoList = loanService.getUnreturnedLoans(page);
    model.addAttribute("dtoList", dtoList.getContent());
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", dtoList.getTotalPages());
    return "admin-main";
  }

  @PostMapping("/main/extend")
  public String extendLoan3(@RequestParam("loanId") Long loanId,Model model) {
    User user = userService.getLoggedInUser();

    loanService.extendDueDate(loanId, 1);
    model.addAttribute("user", user);
    return "redirect:/main";
  }

  @GetMapping("/main/loan")
  public String showLoanInput(Model model) {
    model.addAttribute("userId", userService.getLoggedInUser().getUserId());
    model.addAttribute("loan", "loan");
    return "loan-input";
  }

  @GetMapping("/main/return")
  public String showReturnInput(Model model) {
    model.addAttribute("userId", userService.getLoggedInUser().getUserId());
    model.addAttribute("loan", "return");
    return "loan-input";
  }

  @PostMapping("/main/check")
  public String check(@RequestParam Long bookId, @RequestParam String loan, Model model) {
    Optional<Book> _book = bookRepository.findByBookId(bookId);
    User user = userService.getLoggedInUser();

    model.addAttribute("userId", user.getUserId());
    model.addAttribute("loan", loan);

    if(_book.isPresent()){
      if(loan.equals("loan")&&_book.get().isAvailable()) return "redirect:/main/loan/form?bookId=" + _book.get().getBookId();
      if(loan.equals("return")&&loanRepository.findByUserIdAndBookIdAndIsReturnedFalse(user,_book.get()).isPresent()) return "redirect:/main/return/form?bookId=" + _book.get().getBookId();
    }
    return "redirect:/main/"+loan;
  }

  @GetMapping("/main/loan/form")
  public String showLoanForm(@RequestParam Long bookId, Model model) {
    Book book = bookRepository.findByBookId(bookId).orElseThrow(() -> new IllegalArgumentException("Invalid bookId:" + bookId));
    model.addAttribute("userId", userService.getLoggedInUser().getUserId());
    model.addAttribute("book", book);
    model.addAttribute("loan", "loan");
    model.addAttribute("loan_date", ldtToString(LocalDateTime.now()));
    model.addAttribute("due_date", ldtToString(LocalDateTime.now().plusDays(7)));
    return "loan-form";
  }

  @GetMapping("/main/return/form")
  public String showReturnForm(@RequestParam Long bookId, Model model) {
    Book book = bookRepository.findByBookId(bookId).orElseThrow(() -> new IllegalArgumentException("Invalid bookId:" + bookId));
    Loan loan = loanService.searchFromUserAndBook(userService.getLoggedInUser().getUserId(), bookId);
    model.addAttribute("userId", userService.getLoggedInUser().getUserId());
    model.addAttribute("book", book);
    model.addAttribute("loan", "return");
    model.addAttribute("loan_date", ldtToString(loan.getLoanDate()));
    model.addAttribute("due_date", ldtToString(loan.getDueDate()));
    return "loan-form";
  }

  @PostMapping("/main/createLoan")
  public String loanBook(@RequestParam Long bookId, Model model) {
    try {
      loanService.addLoan(userService.getLoggedInUser().getUserId(), bookId);
      model.addAttribute("message", "Book loaned successfully");
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
    }
    return "redirect:/main";
  }

  @PostMapping("/main/returnLoan")
  public String returnBook(@RequestParam Long bookId, Model model) {
    try {
      loanService.returnLoan2(userService.getLoggedInUser().getUserId(), bookId);
      model.addAttribute("message", "Book returned successfully");
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
    }
    return "redirect:/main";
  }


}
