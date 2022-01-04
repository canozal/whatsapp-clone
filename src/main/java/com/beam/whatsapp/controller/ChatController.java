package com.beam.whatsapp.controller;

import com.beam.whatsapp.Service.ChatService;
import com.beam.whatsapp.model.*;
import com.beam.whatsapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.json.JSONParser;
import org.bson.json.JsonObject;
import org.bson.json.JsonReader;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.beam.whatsapp.Service.UserService.SESSION_KEY;

@RequiredArgsConstructor
@RestController
@RequestMapping("chat")
public class ChatController {

    public final ChatService chatService;
    public final UserRepository userRepository;
    private final SimpMessagingTemplate template;

    @PostMapping(consumes = "application/json")
    public Chat createChat(@RequestBody Chat chat, HttpSession session) {
        User user = (User) session.getAttribute(SESSION_KEY);
        return chatService.createChat(chat, user);
    }

    @GetMapping(produces = "application/json")
    public List<Chat> getAllChats(HttpSession session) {
        User user = (User) session.getAttribute(SESSION_KEY);
        return chatService.getAllChatsByUserName(user.getUsername());
    }

    @PostMapping(value = "/seen", produces = "application/json")
    public void seen(@RequestBody Chat chat, HttpSession session) {
        User user = (User) session.getAttribute(SESSION_KEY);
        List<User> users = chat.getUsers();
        System.out.println(chat);
        Chat seen = chatService.seen(chat, user);

        for (User u : users) {
            template.convertAndSend("/topic/seen/" + u.getId(), chat);
        }

    }


    @MessageMapping("/status")
    public void status(SenderStatus senderStatus) {
        System.out.println(senderStatus);

        for (User user : senderStatus.getUserList()) {
            template.convertAndSend("/topic/status/" + user.getId(), senderStatus);
        }
    }



    @MessageMapping("/chat")
    public void send(Message message) throws Exception {
        System.out.println("Message received: " + message);

        Chat chatById = chatService.getChatById(message.getChatID());
        message.setId(UUID.randomUUID().toString());


        chatById.getMessages().add(message);


        Chat chat = chatService.createChat(chatById);

        for (User user : chatById.getUsers()) {
            template.convertAndSend("/topic/" + user.getId(), chat);
        }

        // template.convertAndSend("/topic/" + chat.getId(), message);


        // return chatService.getAllChatsByUserName(user.getUsername());
    }
}



