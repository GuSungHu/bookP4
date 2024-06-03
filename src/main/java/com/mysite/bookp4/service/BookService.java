package com.mysite.bookp4.service;

import com.mysite.bookp4.dto.BookDTO;
import com.mysite.bookp4.dto.LoanDTO;
import com.mysite.bookp4.entity.Book;
import com.mysite.bookp4.entity.Loan;
import com.mysite.bookp4.repository.BookRepository;
import com.mysite.bookp4.repository.LoanRepository;
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
  private final LoanRepository loanRepository;

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

  public BookDTO getBookById(Long bookId) {
    Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new IllegalArgumentException("BookId가 없습니다 :" + bookId));
    return mapToDTO(book);
  }

  public void addBook(BookDTO dto) {
    Book book = mapToBook(dto);
    book.setAvailable(true);
    bookRepository.save(book);
  }

  public void setBook(BookDTO dto) {
    Optional<Book> _book = bookRepository.findByBookId(dto.getBookId());
    Book book;
    if(_book.isPresent()) {
      book = _book.get();
    } else {
      book = new Book();
    }
    book.setTitle(dto.getTitle());
    book.setAuthor(dto.getAuthor());
    book.setIsbn(dto.getIsbn());
    book.setCategory(dto.getCategory());
    book.setAvailable(dto.isAvailable());
    bookRepository.save(book);
  }

  public void deleteBook(Long bookId) {
    // 특정 Book 엔티티와 관련된 Loan 엔티티의 수를 확인
    long loanCount = loanRepository.countByBookId(bookRepository.findByBookId(bookId).orElseThrow(() -> new IllegalArgumentException("BookId가 없습니다 :" + bookId)));

    if (loanCount == 0) {
      // 관련된 Loan 엔티티가 없으면 Book 엔티티 삭제
      bookRepository.deleteById(bookId);
    } else {
      // 관련된 Loan 엔티티가 있으면 예외를 던지거나 다른 처리 수행
      throw new IllegalStateException("Cannot delete book with active loans");
    }
  }
}
