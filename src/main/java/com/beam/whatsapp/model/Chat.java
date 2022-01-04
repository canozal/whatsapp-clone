package com.beam.whatsapp.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true) // If true, generated setters return this instead of void.
@SuperBuilder
@Document(collection = "Chat")
@TypeAlias("Chat")

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = GroupChat.class, name = "GroupChat"),
        @JsonSubTypes.Type(value = PrivateChat.class, name = "PrivateChat")
})
public abstract class Chat extends Base{

    @Indexed
    private Type type;

    private List<User> users;
    private List<Message> messages = new ArrayList<Message>();

    private LocalDateTime lastMessageTime;
}
