package com.mysite.bookp4.repository;


import com.mysite.bookp4.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    //FindByA(B b)일때 SELECT * FROM USER WHERE A = b
    Optional<User> findByUserId(Long user_id);

    List<User> findByNameContaining(String keyword);

    List<User> findByPhoneContaining(String keyword);
}
