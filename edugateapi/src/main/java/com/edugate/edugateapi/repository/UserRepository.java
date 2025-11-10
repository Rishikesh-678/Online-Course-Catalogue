package com.edugate.edugateapi.repository;

import com.edugate.edugateapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // This is the method Spring Security will use to find a user by their email
    Optional<User> findByEmail(String email);
}