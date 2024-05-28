package com.mysite.bookp4.repository;


import com.mysite.bookp4.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    //SELECT * FROM tbl_users WHERE email = ?
    Optional<User> findById(Long user_id);
}
