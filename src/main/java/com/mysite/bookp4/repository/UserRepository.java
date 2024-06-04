package com.mysite.bookp4.repository;


import com.mysite.bookp4.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    //FindByA(B b)일때 SELECT * FROM USER WHERE A = b
    Optional<User> findByEmail(String email);

    Optional<User> findByUserId(Long user_id);

    Page<User> findAll(Pageable pageable);
    
    Page<User> findByNameContaining(String keyword, Pageable pageable);

    Page<User> findByPhoneContaining(String keyword, Pageable pageable);
}
