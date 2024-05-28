package com.mysite.bookp4.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "Borrow")
public class Borrow {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "customer_id", nullable = false)
  private User user;

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
