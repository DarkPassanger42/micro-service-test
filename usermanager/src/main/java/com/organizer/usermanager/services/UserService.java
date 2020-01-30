package com.organizer.usermanager.services;

import com.organizer.usermanager.model.UserModel;
import com.organizer.usermanager.model.UserDTO;
import com.organizer.usermanager.model.UserModelMapper;
import com.organizer.usermanager.repositories.UsersRepo;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private UsersRepo usersRepo;
    private UserModelMapper userModelMapper;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UsersRepo usersRepo, UserModelMapper userModelMapper,
                       BCryptPasswordEncoder bCryptPasswordEncoder){
        this.usersRepo = usersRepo;
        this.userModelMapper = userModelMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    public void deleteUser(String userId){
        usersRepo.deleteById(userId);
    }

    public Optional<UserModel> findUserById(String id){
        Optional<UserModel> user = usersRepo.findById(id);
        return user;
    }

    public List<UserModel> findUserByEmail(String email){
        List<UserModel> users = usersRepo.findByEmail(email);
        return users;
    }

    public List<UserModel> getAllUsers(){
        List<UserModel> allUsers = usersRepo.findAll();
        return allUsers;
    }

    public UserDTO createUser(String userName, String password){
        String encryptedPassword = bCryptPasswordEncoder.encode(password);
        UserModel userModel = new UserModel();
        userModel.setEmail(userName);
        userModel.setPassword(encryptedPassword);
        UserModel saveResp = usersRepo.save(userModel);
        UserDTO dtoResp = userModelMapper.toUserDTO(saveResp);
        return dtoResp;
    }

}
