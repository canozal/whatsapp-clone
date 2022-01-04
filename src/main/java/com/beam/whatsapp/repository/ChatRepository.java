package com.beam.whatsapp.repository;

import com.beam.whatsapp.model.Chat;
import com.beam.whatsapp.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ChatRepository extends MongoRepository<Chat, String> {

    @Query(value = "{'type': 'PRIVATE'}")
    List<Chat> findByUsersUsernameGroupByType(String username);

    List<Chat> findByUsersUsername(String username);
}
