package com.mysite.bookp4.controller;

import com.mysite.bookp4.dto.BookDTO;
import com.mysite.bookp4.entity.Book;
import com.mysite.bookp4.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookController {
  private final BookService bookService;

  @GetMapping("/books")
  public String showBookList(@RequestParam(required = false) String type,
                             @RequestParam(required = false) String text, Model model) {
    List<BookDTO> books;
    if (type != null && text != null) {
      books = bookService.searchBooks(type, text);
    } else {
      books = bookService.getAllBooks();
    }
    model.addAttribute("books", books);
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

  @PostMapping("books/save")
  public String setBook(@ModelAttribute BookDTO book) {
    if(book.getBookId()==null) bookService.addBook(book);
    else bookService.setBook(book);
    return "redirect:/books";
  }
}