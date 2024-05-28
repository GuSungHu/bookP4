package com.mysite.bookp4.dto;

import com.mysite.bookp4.entity.Book;
import com.mysite.bookp4.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BorrowDTO {
  private Long id;
  private Long customerId;
  private Long bookId;

  //대여 or 반납
  private String transaction_type;

  private String borrow_date;
  private String return_date;
}
