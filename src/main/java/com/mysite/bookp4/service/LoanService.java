package com.mysite.bookp4.service;

import com.mysite.bookp4.dto.LoanDTO;
import com.mysite.bookp4.entity.Book;
import com.mysite.bookp4.entity.Loan;
import com.mysite.bookp4.entity.User;
import com.mysite.bookp4.repository.BookRepository;
import com.mysite.bookp4.repository.LoanRepository;
import com.mysite.bookp4.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import com.mysite.bookp4.util.DateTimeUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanService {

  private final LoanRepository loanRepository;
  private final BookRepository bookRepository;
  private final UserRepository userRepository;
  private final ModelMapper modelMapper;


  //변환
  private LoanDTO loanToDto(Loan loan) {
    LoanDTO dto = modelMapper.map(loan, LoanDTO.class);
    if (loan.getLoanDate() != null) dto.setLoan_date(DateTimeUtil.ldtToString(loan.getLoanDate()));
    if (loan.getDueDate() != null) dto.setDue_date(DateTimeUtil.ldtToString(loan.getDueDate()));
    if (loan.getReturn_date() != null) dto.setReturn_date(DateTimeUtil.ldtToString(loan.getReturn_date()));
    return dto;
  }

  //리스트 변환
  private List<LoanDTO> loanPageToDtoList(Page<Loan> loanList) {
    List<LoanDTO> dtoList = new ArrayList<>();
    for (Loan loan : loanList) {
      dtoList.add(loanToDto(loan));
    }
    return dtoList;
  }


  //리스트 read
  public Page<LoanDTO> getAllLoans(int page) {
    Pageable pageable = PageRequest.of(page, 15, Sort.by("loanDate").descending());
    Page<Loan> loans = loanRepository.findAll(pageable);
    return new PageImpl<>(loanPageToDtoList(loans), pageable, loans.getTotalElements());
  }

  public Page<LoanDTO> searchLoans(String type, String text, int page) {

    Pageable pageable = PageRequest.of(page, 15, Sort.by("loanDate").descending());
    Page<Loan> loans;

    if ("title".equals(type)) {
      loans = loanRepository.findByBookTitleContaining(text, pageable);
    } else {
      loans = loanRepository.findByUserNameContaining(text, pageable);
    }

    return new PageImpl<>(loanPageToDtoList(loans), pageable, loans.getTotalElements());
  }

  public List<LoanDTO> getUnreturnedLoansByUserId(User user) {
    List<LoanDTO> dtoList = new ArrayList<>();
    List<Loan> list = loanRepository.findByUserIdAndIsReturnedFalse(user);
    for (Loan loan : list) dtoList.add(loanToDto(loan));
    return dtoList;
  }

  public Page<LoanDTO> getUnreturnedLoans(int page) {
    Pageable pageable = PageRequest.of(page, 15, Sort.by("dueDate").ascending());
    Page<Loan> loans = loanRepository.findUnreturnedLoans(pageable);
    return new PageImpl<>(loanPageToDtoList(loans), pageable, loans.getTotalElements());
  }

  //개별 read
  public LoanDTO getLoanById(Long id) {
    Loan loan = loanRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid loan Id:" + id));
    return loanToDto(loan);
  }

  //대여
  public void addLoan(Long userId, Long bookId) {

    Book book = bookRepository.findById(bookId).orElseThrow(() -> new IllegalArgumentException("Invalid book Id:" + bookId));
    if (book.isAvailable()) {
      Loan loan = new Loan();
      User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + userId));
      loan.setBookId(book);
      loan.setUserId(user);
      loan.setLoanDate(LocalDateTime.now());
      loan.setDueDate(LocalDateTime.now().plusDays(7));
      loan.setIsReturned(false);
      book.setAvailable(false);
      loanRepository.save(loan);
    }
  }

  //수정(반납)
  public void returnLoan(Long id) {
    Loan loan = loanRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid loan Id:" + id));
    loan.setIsReturned(true);
    loan.setReturn_date(LocalDateTime.now());
    loanRepository.save(loan);

    Book book = loan.getBookId();
    book.setAvailable(true);
    bookRepository.save(book);
  }

  public void returnLoan2(Long userId, Long bookId) {
    User user = getUser(userId);
    Book book = getBook(bookId);
    Loan loan = getLoan(user, book);
    book.setAvailable(true);
    loan.setIsReturned(true);
    loan.setReturn_date(LocalDateTime.now());
    bookRepository.save(book);
    loanRepository.save(loan);
  }

  public Loan searchFromUserAndBook(Long userId, Long bookId) {
    User user = getUser(userId);
    Book book = getBook(bookId);

    return getLoan(user, book);
  }

  private Book getBook(Long bookId) {
    return bookRepository.findByBookId(bookId).orElseThrow(() -> new IllegalArgumentException("Invalid book Id:" + bookId));
  }

  private User getUser(Long userId) {
    return userRepository.findByUserId(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + userId));
  }

  private Loan getLoan(User user, Book book) {
    return loanRepository.findByUserIdAndBookIdAndIsReturnedFalse(user, book).orElseThrow(() -> new IllegalArgumentException("Invalid loan Id:"));
  }

  //기간 연장
  public void extendDueDate(Long id, int days) {
    Loan loan = loanRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid loan Id:" + id));
    if (!loan.getIsReturned()) {
      loan.setDueDate(loan.getDueDate().plusDays(days));
      loanRepository.save(loan);
    }
  }

  //삭제 (only admin)
  public void deleteLoan(Long id) {
    loanRepository.deleteById(id);
  }
}
