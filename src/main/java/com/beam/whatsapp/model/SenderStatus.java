package com.beam.whatsapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true) // If true, generated setters return this instead of void.
@SuperBuilder
public class SenderStatus {

    private String senderName;
    private String status;
    private List<User> userList;

    private String chatId;

}
