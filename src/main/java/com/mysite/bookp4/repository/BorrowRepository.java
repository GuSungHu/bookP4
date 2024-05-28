package com.mysite.bookp4.repository;

import com.mysite.bookp4.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowRepository extends JpaRepository<Loan, Long> {
  //  List<Borrow> findById();
//  List<Borrow> findByBook(Book book);
}
