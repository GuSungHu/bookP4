package com.mysite.bookp4.repository;

import com.mysite.bookp4.entity.Book;
import com.mysite.bookp4.entity.Loan;
import com.mysite.bookp4.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LoanRepository extends JpaRepository<Loan, Long> {

  Optional<Loan> findByLoanId(Long loan_id);

  @Query("SELECT l FROM Loan l WHERE l.bookId.title LIKE %:title%")
  List<Loan> findByBookTitleContaining(@Param("title") String title);

  @Query("SELECT l FROM Loan l WHERE l.userId.name LIKE %:name%")
  List<Loan> findByUserNameContaining(@Param("name") String name);

  List<Loan> findByUserId(User userId);

  List<Loan> findByUserIdAndIsReturnedFalse(User userId);

  @Query("SELECT l FROM Loan l WHERE l.isReturned = false")
  List<Loan> findUnreturnedLoans();

  long countByBookId(Book bookId);
  long countByUserId(User userId);
}
