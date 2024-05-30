package com.mysite.bookp4.service;

import com.mysite.bookp4.entity.User;
import com.mysite.bookp4.repository.UserRepository;
import com.mysite.bookp4.entity.User;
import com.mysite.bookp4.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepo;
    //시큐리티가 로그인 하기 위한 메서드 원성시켜야 함
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("커스텀 서비스");
        System.out.println("입력된 이메일: " + email);
        User user = userRepo.findByEmail(email).orElseThrow(() ->//옵셔널 타입 찾았을때 못찾았을때 동시처리
                new UsernameNotFoundException(email + "해당 이메일의 유저가 없습니다 "));
        //시큐리티 유저는 권한도 필요
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),user.getPassword(), new ArrayList<>()//권한 롤
        ); //시큐리티 유저객체
    }

}