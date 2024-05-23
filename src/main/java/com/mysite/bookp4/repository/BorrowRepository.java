package com.mysite.bookp4.repository;

import com.mysite.bookp4.entity.Book;
import com.mysite.bookp4.entity.Borrow;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BorrowRepository extends JpaRepository<Borrow, Long> {
  //  List<Borrow> findById();
//  List<Borrow> findByBook(Book book);
}
