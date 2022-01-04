package com.beam.whatsapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true) // If true, generated setters return this instead of void.
@SuperBuilder
@Document(collection = "User")
@TypeAlias("User")
public class User extends Base{

    private String username;
    private String password;
    private String imageUrl;
    private LocalDateTime lastSeen;
    @DBRef
    private List<String> conversations;
}
