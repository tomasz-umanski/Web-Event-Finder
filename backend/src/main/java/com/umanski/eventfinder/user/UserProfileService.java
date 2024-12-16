package com.umanski.eventfinder.user;


import com.umanski.eventfinder.user.model.dto.ChangePasswordDto;
import com.umanski.eventfinder.user.model.entity.User;

public interface UserProfileService {

    void changePassword(User user, ChangePasswordDto changePasswordDto);

}
