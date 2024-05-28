package com.mysite.bookp4.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "Book")
public class Book {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String author;

  private String category;

  @Column(nullable = false)
  private boolean available;

  // Getters and setters
}
