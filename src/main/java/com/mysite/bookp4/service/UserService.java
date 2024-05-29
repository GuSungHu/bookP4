package com.mysite.bookp4.service;

import com.mysite.bookp4.dto.UserDTO;
import com.mysite.bookp4.entity.User;
import com.mysite.bookp4.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepo;
    private final ModelMapper modelMapper;

    public List<User> getAllUser(){
        return userRepo.findAll();
    }

    //엔티티 => DTO 변환
    private UserDTO mapToDTO(User user) {
        UserDTO userDTO = modelMapper.map(user,UserDTO.class);
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

    private User getUserById(Long userId) {
        return userRepo.findById(userId).orElseThrow();
    }
    //userID로 수정할  user를 찾아서 DTO로 리턴
    public UserDTO getUser(Long userId) {
        User user = getUserById(userId);
        UserDTO userDTO = mapToDTO(user);
        return userDTO;
    }

    public List<UserDTO> getFilterUsers(String keyword, String searchBy){
        List<User> list;
        if ("name".equals(searchBy)) {
            list = userRepo.findByNameContaining(keyword);
        } else if ("phone".equals(searchBy)) {
            list = userRepo.findByPhoneContaining(keyword);
        } else {
            // 기본적으로 이름으로 검색
            list = userRepo.findByNameContaining(keyword);
        }
        List<UserDTO> filterlist = list.stream().map(this::mapToDTO).collect(Collectors. toList());
        return filterlist;
    }
}