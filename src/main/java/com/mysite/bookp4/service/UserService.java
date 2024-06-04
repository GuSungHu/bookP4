package com.mysite.bookp4.service;

import com.mysite.bookp4.dto.UserDTO;
import com.mysite.bookp4.entity.Loan;
import com.mysite.bookp4.entity.User;
import com.mysite.bookp4.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepo;
  private final ModelMapper modelMapper;
  private final PasswordEncoder passwordEncoder;

  public Page<UserDTO> getAllUser(int page) {
    Pageable pageable = PageRequest.of(page, 15, Sort.by("userId").descending());
    Page<User> users = userRepo.findAll(pageable);
    return new PageImpl<>(userPageToDtoList(users), pageable, users.getTotalElements());
  }

  private List<UserDTO> userPageToDtoList(Page<User> users) {
    List<UserDTO> list = new ArrayList<>();
    for (User user : users) list.add(mapToDTO(user));
    return list;
  }

  //엔티티 => DTO 변환
  private UserDTO mapToDTO(User user) {
    UserDTO userDTO = modelMapper.map(user, UserDTO.class);
    return userDTO;
        /*UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhone(user.getPhone());
        return userDTO;*/
  }
/*
    public List<UserDTO> getAllUser(){
        List<User> list = userRepo.findAll();
        List<UserDTO> listDTO = list.stream().map(this::mapToDTO).collect(Collectors.toList());
        return listDTO;
    }*/

  public UserDTO saveUserDetails(UserDTO userDTO) throws ParseException {
    System.out.println("세이브 유저");
    userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
    //1. DTO => Entity
    User user = mapToEntity(userDTO);
    //2. DB에 저장 id가 있는경우 업데이트
    userRepo.save(user);
    //3. Entity => DTO
    return mapToDTO(user);
  }

  private User mapToEntity(UserDTO userDTO) throws ParseException {
    User user = modelMapper.map(userDTO, User.class);
    return user;
  }

  public void deleteUser(Long userId) {
    User user = getUserById(userId);
    userRepo.delete(user);
  }

  public User getUserById(Long userId) {
    return userRepo.findById(userId).orElseThrow();
  }
  //userID로 수정할  user를 찾아서 DTO로 리턴

  public UserDTO getUser(Long userId) {
    User user = getUserById(userId);
    UserDTO userDTO = mapToDTO(user);
    return userDTO;
  }

  public Page<UserDTO> searchUsers(String type, String text, int page) {
    Page<User> user;
    Pageable pageable = PageRequest.of(page, 15, Sort.by("name"));
    if ("name".equals(type)) {
      user = userRepo.findByNameContaining(text, pageable);
    } else {
      user = userRepo.findByPhoneContaining(text, pageable);
    }
    System.out.println(user.getTotalElements());
    return new PageImpl<>(userPageToDtoList(user), pageable, user.getTotalElements());
  }

  //로그인 된 유저정보 가져오기 , 비활성화시

  public User getLoggedInUser() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String loginUserEmail = auth.getName();
    return userRepo.findByEmail(loginUserEmail).orElseThrow(() ->
            new UsernameNotFoundException("이메일을 찾을수 없습니다"));
  }
}