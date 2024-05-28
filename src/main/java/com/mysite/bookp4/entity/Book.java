package com.mysite.bookp4.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Book")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long bookId;

  @Column(nullable = false)
  private String title;

  private String author;

  private String isbn;

  private String category;

  @Column(nullable = false)
  private boolean isAvailable;

  // Getters and setters
}
