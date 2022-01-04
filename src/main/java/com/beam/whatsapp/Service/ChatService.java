package com.beam.whatsapp.Service;

import com.beam.whatsapp.model.*;
import com.beam.whatsapp.repository.ChatRepository;
import com.beam.whatsapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.beam.whatsapp.Service.UserService.SESSION_KEY;

@RequiredArgsConstructor
@Service
public class ChatService {

    public final ChatRepository chatRepository;
    public final UserRepository userRepository;
    private final SimpMessagingTemplate template;


    public Chat createChat(Chat chat, User user) {

        try {

            user = User.builder().username(user.getUsername()).imageUrl(user.getImageUrl()).id(user.getId()).build();

            List<User> userList = chat.getUsers();

            chat.setUsers(new java.util.ArrayList<>());

            User finalUser = user;
            userList.stream().forEach(u -> {
                User username = userRepository.findByUsername(u.getUsername());
                if (username != null && !Objects.equals(username.getUsername(), finalUser.getUsername())) {
                    User user1 = User.builder().username(username.getUsername()).imageUrl(username.getImageUrl()).id(username.getId()).build();
                    chat.getUsers().add(user1);
                }
            });
            List<User> oldUsers = chat.getUsers();

            int i = 0;
            if (chat.getType() == Type.PRIVATE) {
                List<Chat> allChatsByUserName = chatRepository.findByUsersUsernameGroupByType(chat.getUsers().get(0).getUsername());


                for (Chat c : allChatsByUserName) {
                    i = 0;
                    for (User u : c.getUsers()) {
                        if (u.getUsername().equals(user.getUsername()) ||
                                u.getUsername().equals(chat.getUsers().get(0).getUsername())) {
                           i++;
                        }
                    }
                    if (i == 2) {
                        return null;
                    }
                }
            }

            chat.getUsers().add(user);
            chat.setId(UUID.randomUUID().toString());

            if (!(chat.getUsers().size() > 1)) {
                throw new Exception("Chat must have at least two users");
            }

            for (User u : oldUsers) {
                template.convertAndSend("/topic/new/" + u.getId(), chat);
            }

            return createChat(chat);
        } catch (Exception e) {
            return null;
        }

    }

    public Chat createChat(Chat chat) {
        return chatRepository.save(chat);
    }


    public List<Chat> getAllChatsByUserName(String username) {
        return chatRepository.findByUsersUsername(username);
    }

    public Chat getChatById(String chatID) {
        return chatRepository.findById(chatID).orElse(null);
    }

    public Chat seen(Chat chat, User user) {
        List<Message> messages = chat.getMessages();
        for(Message m : messages) {
            if(!contains(user, m.getReceiverStatus()) && !(m.getSender().getUsername().equals(user.getUsername()))) {
                m.getReceiverStatus().add(Status.builder().user(user).status(StatusCode.READ).build());
            }
        }

        chat.setMessages(messages);
        chat.getUsers().add(User.builder().id(user.getId()).username(user.getUsername()).build());
        return chatRepository.save(chat);
    }

    private boolean contains(User user, List<Status> status) {
        for(Status s : status) {
            if(s.getUser().getUsername().equals(user.getUsername())) {
                return true;
            }
        }
        return false;
    }


}
