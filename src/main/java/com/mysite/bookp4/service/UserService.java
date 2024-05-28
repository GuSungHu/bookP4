package com.mysite.bookp4.service;

import com.mysite.bookp4.dto.UserDTO;
import com.mysite.bookp4.entity.User;
import com.mysite.bookp4.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

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
        /*UserDTO useromerDTO = new UserDTO();
        useromerDTO.setId(user.getId());
        useromerDTO.setName(user.getName());
        useromerDTO.setEmail(user.getEmail());
        useromerDTO.setPhone(user.getPhone());
        return useromerDTO;*/
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
        user = userRepo.save(user);
        //3. Entity => DTO
        return mapToDTO(user);
    }
    private User mapToEntity(UserDTO userDTO) throws ParseException {
        User user = modelMapper.map(userDTO, User.class);
        return user;
    }

    public void deleteUser(Long user_id) {
        User user = getUserById(user_id);
        userRepo.delete(user);
    }

    private User getUserById(Long user_id) {
        return userRepo.findById(user_id).orElse(null);
    }
    //useromerID로 수정할  useromer를 찾아서 DTO로 리턴
    public UserDTO getUser(Long user_id) {
        User user = getUserById(user_id);
        UserDTO useromerDTO = mapToDTO(user);
        return useromerDTO;
    }
}