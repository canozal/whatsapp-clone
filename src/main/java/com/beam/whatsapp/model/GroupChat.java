package com.beam.whatsapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor

@Accessors(chain = true) // If true, generated setters return this instead of void.
@SuperBuilder
@Document(collection = "GroupChat")
@TypeAlias("GroupChat")
public class GroupChat extends Chat {

    public GroupChat(){
        setType(Type.GROUP);
    }


    private String name;
    private String imageUrl;
}
