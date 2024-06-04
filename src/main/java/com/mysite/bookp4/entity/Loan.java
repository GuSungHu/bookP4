package com.mysite.bookp4.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "Loan")
public class Loan {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long loanId;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User userId;

  @ManyToOne
  @JoinColumn(name = "book_id", nullable = false)
  private Book bookId;

  @Column(nullable = false, name = "is_returned")
  private Boolean isReturned;

  @Column(nullable = false, name = "loan_date")
  private LocalDateTime loanDate;
  @Column(nullable = false, name = "due_date")
  private LocalDateTime dueDate;
  private LocalDateTime return_date;


  // Getters and setters
}
