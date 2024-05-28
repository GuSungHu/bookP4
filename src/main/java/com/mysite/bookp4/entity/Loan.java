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
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User userId;

  @ManyToOne
  @JoinColumn(name = "book_id", nullable = false)
  private Book bookId;

  @Column(nullable = false)
  private String transaction_type; //test

  @Column(nullable = false)
  private LocalDateTime borrow_date;
  @Column(nullable = true)
  private LocalDateTime return_date;

  // Getters and setters
}
