package com.organizer.usermanager.model;

import org.springframework.stereotype.Component;

@Component
public class UserModelMapper {

    public UserDTO toUserDTO(UserModel userModel){
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(userModel.getEmail());
        userDTO.setId(userModel.getId());
        return userDTO;
    }

}
