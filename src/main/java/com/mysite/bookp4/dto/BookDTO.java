package com.mysite.bookp4.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {

  private Long bookId;
  private String title;
  private String author;
  private String isbn;
  private String category;
  private boolean isAvailable;

}
