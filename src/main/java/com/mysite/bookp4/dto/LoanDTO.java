package com.mysite.bookp4.dto;

import com.mysite.bookp4.entity.Book;
import com.mysite.bookp4.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanDTO {

  private Long loanId;

  private User userId;
  private Book bookId;

  private String loan_date;
  private String return_date;
  private String due_date;

  private Boolean isReturned;
}
