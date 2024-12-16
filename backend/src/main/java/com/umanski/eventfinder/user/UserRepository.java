package com.umanski.eventfinder.user;

import com.umanski.eventfinder.user.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findUserByEmail(String email);

    boolean existsByEmailIgnoreCase(String email);

}
