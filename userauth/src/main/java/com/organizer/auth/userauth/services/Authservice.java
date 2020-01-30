package com.organizer.auth.userauth.services;

import com.organizer.auth.userauth.model.UserModel;
import com.organizer.auth.userauth.repositories.UsersRepo;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Authservice implements UserDetailsService {

    private UsersRepo usersRepo;

    public Authservice(UsersRepo usersRepo){
        this.usersRepo = usersRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        List<UserModel> users = usersRepo.findByEmail(username);
        if (users.size() == 0){
            throw new UsernameNotFoundException("User name not found");
        }

        UserModel userFromDb = users.get(0);

        User user = new User(userFromDb.getEmail(), userFromDb.getPassword(),
                true, true, true,
                true, new ArrayList<>());

        return user;
    }

    public UserModel getUserDetails(String username){
        List<UserModel> users = usersRepo.findByEmail(username);
        if (users.size() == 0){
            return null;
        }
        else{
            return users.get(0);
        }
    }
}
