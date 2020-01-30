package com.organizer.auth.userauth.repositories;

import com.organizer.auth.userauth.model.UserModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepo extends MongoRepository<UserModel, String> {
    List<UserModel> findByEmail(String email);
}