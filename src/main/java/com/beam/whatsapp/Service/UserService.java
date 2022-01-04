package com.beam.whatsapp.Service;

import com.beam.whatsapp.model.User;
import com.beam.whatsapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.data.domain.Example;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {

    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png");
    private final PasswordEncoder passwordEncoder;
    private final SimpMessagingTemplate template;


    private final UserRepository userRepository;
    // private final PasswordEncoder passwordEncoder;

    public static final String SESSION_KEY = "ususer";

    public User getUserByName(String name) {
        return userRepository.findByUsername(name);
    }

    public User saveUser(User user) {

        boolean exists = userRepository.existsByUsername(user.getUsername());

        if (exists) {
            return null;
        }

        // hash password
        //user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public String registerUser(@RequestParam String username, @RequestParam String password, @RequestParam(required = false) MultipartFile image) {

        try {
            byte[] bytes;
            String fileName = null;
            String ext;

            if (!(username.length() > 0) || !(password.length() > 0)) {
                return "redirect:/register?error=enter_username_password";
            }

            if (image.getOriginalFilename().length() > 0) {
                ext = FilenameUtils.getExtension(image.getOriginalFilename()).toLowerCase();

                if (!ALLOWED_EXTENSIONS.contains(ext)) {
                    throw new IllegalArgumentException("invalid_extension");
                }

                bytes = image.getBytes();
                fileName = UUID.randomUUID().toString() + "." + ext;
                Path path = Paths.get("C:\\Users\\can.ozal\\Desktop\\wa_images\\person_avatars\\" + fileName);
                try {
                    Files.write(path, bytes);
                } catch (Exception e) {
                    return "redirect:/register" + "?error=image_upload_failed";
                }

            }

            User user = User.builder().username(username).password(passwordEncoder.encode(password)).imageUrl(fileName).id(UUID.randomUUID().toString()).build();


            User saveUser = saveUser(user);

            if (saveUser == null) {
                return "redirect:/register" + "?error=user_already_exists";
            }

            template.convertAndSend("/topic/newuser/", "123");

            return "redirect:/login";


        } catch (Exception e) {
            return "redirect:/register" + "?error=" + e.getMessage().toString();
        }

    }

    public User deleteUser(String name) {
        User user = userRepository.findByUsername(name);
        if (user != null) {
            userRepository.delete(user);
            return user;
        }
        return null;

    }

    public User findByUsernameAndPassword(String username, String password) {
        User user = User.builder().username(username).password(password).build();
        return userRepository.findByUsernameAndPassword(username, password);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
