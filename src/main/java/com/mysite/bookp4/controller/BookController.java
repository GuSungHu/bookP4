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

//
//  @PostMapping
//  public Book createBook(@RequestBody Book book) {
//    return bookService.save(book);
//  }
//
//  @PostMapping("/{id}")
//  public Book updateBook(@PathVariable Long id, @RequestBody Book bookDetails) {
//    return bookService.findById(id).map(book -> {
//      book.setTitle(bookDetails.getTitle());
//      book.setAuthor(bookDetails.getAuthor());
//      book.setCategory(bookDetails.getCategory());
//      book.setAvailable(bookDetails.isAvailable());
//      return bookService.save(bookDetails);
//    }).orElseThrow(() -> new RuntimeException("Book not found whit id" + id));
//
//  }
//
//  @DeleteMapping("/{id}")
//  public void deleteBook(@PathVariable Long id) {
//    bookService.deleteById(id);
//  }
}