package com.mysite.bookp4.repository;

import com.mysite.bookp4.entity.Book;
import com.mysite.bookp4.entity.Loan;
import com.mysite.bookp4.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LoanRepository extends JpaRepository<Loan, Long> {

  Optional<Loan> findByLoanId(Long loan_id);

  @Query("SELECT l FROM Loan l")
  Page<Loan> findAll(Pageable pageable);

  @Query("SELECT l FROM Loan l WHERE l.bookId.title LIKE %:title%")
  Page<Loan> findByBookTitleContaining(@Param("title") String title, Pageable pageable);

  @Query("SELECT l FROM Loan l WHERE l.userId.name LIKE %:name%")
  Page<Loan> findByUserNameContaining(@Param("name") String name, Pageable pageable);

  Page<Loan> findByUserId(User userId, Pageable pageable);

  Page<Loan> findByUserIdAndIsReturnedFalse(User userId, Pageable pageable);
  List<Loan> findByUserIdAndIsReturnedFalse(User userId);

  Optional<Loan> findByUserIdAndBookIdAndIsReturnedFalse(User user, Book book);

  @Query("SELECT l FROM Loan l WHERE l.isReturned = false")
  Page<Loan> findUnreturnedLoans(Pageable pageable);

  long countByBookId(Book bookId);
}
