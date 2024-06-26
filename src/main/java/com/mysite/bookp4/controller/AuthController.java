package com.mysite.bookp4.controller;

import com.mysite.bookp4.dto.BookDTO;
import com.mysite.bookp4.dto.LoanDTO;
import com.mysite.bookp4.dto.UserDTO;
import com.mysite.bookp4.entity.Book;
import com.mysite.bookp4.entity.Loan;
import com.mysite.bookp4.entity.User;
import com.mysite.bookp4.repository.LoanRepository;
import com.mysite.bookp4.repository.UserRepository;
import com.mysite.bookp4.service.BookService;
import com.mysite.bookp4.service.LoanService;
import com.mysite.bookp4.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.text.ParseException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final BookService bookService;
    private final LoanService loanService;

    @GetMapping({"/login","/"})
    public String showLoginPage(Principal principal) {
        System.out.println("로그인 컨트롤러");
        if (principal == null) {
            return "login";
        }
       return "redirect:/main";
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

    @GetMapping("/main")
    public String getUserMain(@RequestParam(value = "type", required = false) String type,
                              @RequestParam(value = "text", required = false) String text,
                              @RequestParam(defaultValue = "0") int page,
                              Model model) {
        User user = userService.getLoggedInUser();

        List<LoanDTO> loans = loanService.getUnreturnedLoansByUserId(user);
        Page<BookDTO> books;
        if (type == null){
            books=null;
            model.addAttribute("books", books);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", 0);
        }
        else{
            books = bookService.searchBooks(type, text, page);
            model.addAttribute("books", books.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", books.getTotalPages());
        }
        model.addAttribute("user", user);
        model.addAttribute("loans",loans);

        model.addAttribute("type", type);
        model.addAttribute("text", text);
        return "user-main";
    }
}