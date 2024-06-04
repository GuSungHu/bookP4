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
import org.springframework.stereotype.Service;
import com.mysite.bookp4.util.DateTimeUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
    if (loan.getLoan_date() != null) dto.setLoan_date(DateTimeUtil.ldtToString(loan.getLoan_date()));
    if (loan.getDue_date() != null) dto.setDue_date(DateTimeUtil.ldtToString(loan.getDue_date()));
    if (loan.getReturn_date() != null) dto.setReturn_date(DateTimeUtil.ldtToString(loan.getReturn_date()));
    return dto;
  }

  //리스트 변환
  private List<LoanDTO> loanListToDtoList(List<Loan> loanList) {
    List<LoanDTO> dtoList = new ArrayList<>();
    for (Loan loan : loanList) {
      dtoList.add(loanToDto(loan));
    }
    return dtoList;
  }


  //리스트 read
  public List<LoanDTO> getAllLoans() {
    List<Loan> loanList = loanRepository.findAll();
    return loanListToDtoList(loanList);
  }

  public List<LoanDTO> searchLoans(String type, String text) {
    List<Loan> loans;
    if ("title".equals(type)) {
      loans = loanRepository.findByBookTitleContaining(text);
    } else {
      loans = loanRepository.findByUserNameContaining(text);
    }
    return loanListToDtoList(loans);
  }

  public List<LoanDTO> getLoansByUserId(User user) {
    List<Loan> loans = loanRepository.findByUserId(user);
    return loanListToDtoList(loans);
  }

  public List<LoanDTO> getUnreturnedLoansByUserId(User user) {
    List<Loan> loans = loanRepository.findByUserIdAndIsReturnedFalse(user);
    return loanListToDtoList(loans);
  }

  public List<LoanDTO> getUnreturnedLoansOrderedByDueDate() {
    List<Loan> loans = loanRepository.findUnreturnedLoans();
    return loanListToDtoList(loans.stream()
            .sorted(Comparator.comparing(Loan::getDue_date))
            .collect(Collectors.toList()));
  }



  //개별 read
  public LoanDTO getLoanById(Long id) {
    Loan loan = loanRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid loan Id:" + id));
    return loanToDto(loan);
  }

  //대여
  public void addLoan(Long userId, Long bookId) {

    Book book = bookRepository.findById(bookId).orElseThrow(() -> new IllegalArgumentException("Invalid book Id:" + bookId));
    if(book.isAvailable()) {
      Loan loan = new Loan();
      User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + userId));
      loan.setBookId(book);
      loan.setUserId(user);
      loan.setLoan_date(LocalDateTime.now());
      loan.setDue_date(LocalDateTime.now().plusDays(7));
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
    if(!loan.getIsReturned()) {
      loan.setDue_date(loan.getDue_date().plusDays(days));
      loanRepository.save(loan);
    }
  }

  //삭제 (only admin)
  public void deleteLoan(Long id) {
    loanRepository.deleteById(id);
  }
}
