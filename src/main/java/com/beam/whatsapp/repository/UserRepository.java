package com.beam.whatsapp.repository;

import com.beam.whatsapp.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

    User findByUsername(String username);

    boolean existsByUsername(String username);

    User findByUsernameAndPassword(String username, String password);
}
