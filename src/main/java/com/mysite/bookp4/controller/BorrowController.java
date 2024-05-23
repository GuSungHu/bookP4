package com.mysite.bookp4.controller;

import com.mysite.bookp4.dto.BorrowDTO;
import com.mysite.bookp4.entity.Borrow;
import com.mysite.bookp4.service.BorrowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BorrowController {
  private final BorrowService brwService;

  @GetMapping("/borrow")
  public String showBorrowList(Model model) {
    List<BorrowDTO> dtoList = brwService.getAllBorrows();
    model.addAttribute("dtoList", dtoList);
    return "borrow-list";
  }


}
