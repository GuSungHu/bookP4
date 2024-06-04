package com.mysite.bookp4.controller;

import com.mysite.bookp4.dto.BookDTO;
import com.mysite.bookp4.entity.Book;
import com.mysite.bookp4.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class BookController {
  private final BookService bookService;

  @GetMapping("/books")
  public String showBookList(@RequestParam(required = false) String type,
                             @RequestParam(required = false) String text,
                             @RequestParam(defaultValue = "0") int page,
                             Model model) {
    Page<BookDTO> books;
    if (type != null && text != null) {
      books = bookService.searchBooks(type, text, page);
    } else {
      books = bookService.getAllBooks(page);
    }
    model.addAttribute("books", books.getContent());
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", books.getTotalPages());
    model.addAttribute("type", type);
    model.addAttribute("text", text);
    return "book-list";
  }

  @GetMapping("books/delete/{bookId}")
  public String deleteBook(@PathVariable Long bookId) {
    bookService.deleteBook(bookId);
    return "redirect:/books";
  }

  @GetMapping("/books/form")
  public String showNewBookForm(Model model) {
    model.addAttribute("book", new Book());
    return "book-form";
  }

  @GetMapping("books/edit/{bookId}")
  public String showEditBookForm(@PathVariable Long bookId, Model model) {
    model.addAttribute("book", bookService.getBookById(bookId));
    return "book-form";
  }
  @GetMapping("books/view/{bookId}")
  public String showViewBookDetail(@PathVariable Long bookId, Model model) {
    model.addAttribute("book", bookService.getBookById(bookId));
    return "book-detail";
  }

  @GetMapping("main/book/{bookId}")
  public String showViewBookDetail2(@PathVariable Long bookId, Model model) {
    model.addAttribute("book", bookService.getBookById(bookId));
    return "book-detail";
  }

  @PostMapping("books/save")
  public String setBook(@ModelAttribute BookDTO book) {
    if(book.getBookId()==null) bookService.addBook(book);
    else bookService.setBook(book);
    return "redirect:/books";
  }
}