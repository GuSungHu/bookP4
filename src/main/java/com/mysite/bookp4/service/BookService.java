package com.mysite.bookp4.service;

import com.mysite.bookp4.dto.BookDTO;
import com.mysite.bookp4.dto.LoanDTO;
import com.mysite.bookp4.entity.Book;
import com.mysite.bookp4.entity.Loan;
import com.mysite.bookp4.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {

  private final BookRepository bookRepository;
  private final ModelMapper modelMapper;

  private List<BookDTO> booksToDto(List<Book> books) {
    List<BookDTO> dtoList = new ArrayList<>();
    for (Book book : books) {
      dtoList.add(mapToDTO(book));
    }
    return dtoList;
  }

  private BookDTO mapToDTO(Book book) {
    return modelMapper.map(book, BookDTO.class);
  }
  private Book mapToBook(BookDTO dto) {
    return modelMapper.map(dto, Book.class);
  }

  public List<BookDTO> getAllBooks() {
    return booksToDto(bookRepository.findAll());
  }

  public List<BookDTO> searchBooks(String type, String text) {
    List<Book> books;
    if ("title".equals(type)) {
      books = bookRepository.findByTitleContainingIgnoreCase(text);
    } else {
      books = bookRepository.findByAuthorContainingIgnoreCase(text);
    }
    return booksToDto(books);
  }

  public void addBook(BookDTO dto) {
    Book book = mapToBook(dto);
    bookRepository.save(book);
  }

  public void updateBook(BookDTO dto) {
    Book book = bookRepository.findByBookId(dto.getBookId());
    book.setTitle(dto.getTitle());
    book.setAuthor(dto.getAuthor());
    book.setIsbn(dto.getIsbn());
    book.setCategory(dto.getCategory());
    book.setAvailable(dto.isAvailable());
    bookRepository.save(book);
  }

  public void deleteBook(Long id) {
    bookRepository.deleteById(id);
  }
}
