package com.spig.spig.domain.user.repository;

import com.spig.spig.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

     Optional<User> findByLoginId(String loginId);
}
