package com.umanski.eventfinder.user;

import com.umanski.eventfinder.user.model.entity.User;

import java.util.Optional;

public interface UserService {

    Optional<User> findUserByEmail(String username);

}
