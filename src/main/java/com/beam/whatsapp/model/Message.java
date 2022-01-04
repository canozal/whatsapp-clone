package com.beam.whatsapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true) // If true, generated setters return this instead of void.
@SuperBuilder
@Document(collection = "Message")
@TypeAlias("Message")
public class Message extends Base{

    private String message;
    private User sender;
    private LocalDateTime time;

    private List<Status> receiverStatus = new ArrayList<Status>();

    private String chatID;

}
