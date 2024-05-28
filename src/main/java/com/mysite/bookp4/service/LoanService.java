package com.mysite.bookp4.service;

import com.mysite.bookp4.dto.LoanDTO;
import com.mysite.bookp4.entity.Loan;
import com.mysite.bookp4.entity.User;
import com.mysite.bookp4.repository.LoanRepository;
import com.mysite.bookp4.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import util.DateTimeUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanService {

  private final LoanRepository loanRepository;
  private final ModelMapper modelMapper;


  //변환
  private LoanDTO loanToDto(Loan loan) {
    LoanDTO dto = modelMapper.map(loan, LoanDTO.class);
    if (loan.getLoan_date() != null) dto.setLoan_date(DateTimeUtil.ldtToString(loan.getLoan_date()));
    if (loan.getDue_date() != null) dto.setDue_date(DateTimeUtil.ldtToString(loan.getDue_date()));
    if (loan.getReturn_date() != null) dto.setReturn_date(DateTimeUtil.ldtToString(loan.getReturn_date()));
    System.out.println(dto.toString());
    return dto;
  }

  //리스트 변환
  private List<LoanDTO> loanListToDtoList(List<Loan> loanList) {
    List<LoanDTO> dtoList = new ArrayList<>();
    for (Loan loan : loanList) {
      dtoList.add(loanToDto(loan));
    }
    return dtoList;
  }


  //리스트 read
  public List<LoanDTO> getAllLoans() {
    List<Loan> loanList = loanRepository.findAll();
    return loanListToDtoList(loanList);
  }

  public List<LoanDTO> searchLoans(String type, String text) {
    List<Loan> loans;
    if ("title".equals(type)) {
      loans = loanRepository.findByBookTitleContaining(text);
    } else {
      loans = loanRepository.findByUserNameContaining(text);
    }
    return loanListToDtoList(loans);
  }

  public List<LoanDTO> getLoansByUserId(User user) {
    List<Loan> loans = loanRepository.findByUserId(user);
    return loanListToDtoList(loans);
  }

  public List<LoanDTO> getUnreturnedLoansByUserId(User user) {
    List<Loan> loans = loanRepository.findByUserIdAndIsReturnedFalse(user);
    return loanListToDtoList(loans);
  }



  //개별 read
  public LoanDTO getLoanById(Long id) {
    Loan loan = loanRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid loan Id:" + id));
    return loanToDto(loan);
  }

  //대여
  public void addLoan(LoanDTO dto) {
    Loan loan = modelMapper.map(dto, Loan.class);
    loan.setLoan_date(LocalDateTime.now());
    loan.setDue_date(LocalDateTime.now().plusDays(7));
    loan.setIsReturned(false);
    loanRepository.save(loan);
  }

  //수정(반납)
  public void returnLoan(Long id) {
    Loan loan = loanRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid loan Id:" + id));
    loan.setIsReturned(true);
    loan.setReturn_date(LocalDateTime.now());
    loanRepository.save(loan);
  }

  public void extendDueDate(Long id, int days) {
    Loan loan = loanRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid loan Id:" + id));
    loan.setDue_date(loan.getDue_date().plusDays(days));
    loanRepository.save(loan);
  }

  //삭제 (only admin)
  public void deleteLoan(Long id) {
    loanRepository.deleteById(id);
  }
}
