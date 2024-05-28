package com.mysite.bookp4.service;

import com.mysite.bookp4.dto.BorrowDTO;
import com.mysite.bookp4.entity.Loan;
import com.mysite.bookp4.repository.BorrowRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BorrowService {

  private final BorrowRepository brwRepo;
  private final ModelMapper modelMapper;


  public List<BorrowDTO> getAllBorrows() {
    List<Loan> loanList = brwRepo.findAll();
    List<BorrowDTO> dtoList = new ArrayList<>();
    for(Loan loan : loanList) {
      dtoList.add(borrowToDto(loan));
    }
    return dtoList;
  }

  private BorrowDTO borrowToDto(Loan loan){
    BorrowDTO dto = modelMapper.map(loan, BorrowDTO.class);
    dto.setBorrow_date(dto.getBorrow_date().substring(2,10));
    dto.setReturn_date(dto.getReturn_date().substring(2,10));
    return dto;
  }
}
