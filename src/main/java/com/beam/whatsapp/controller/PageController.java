package com.beam.whatsapp.controller;

import com.beam.whatsapp.Service.UserService;
import com.beam.whatsapp.model.User;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.messaging.simp.SimpMessagingTemplate;


import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.beam.whatsapp.Service.UserService.SESSION_KEY;

@RequiredArgsConstructor
@Controller
public class PageController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png");
    private final SimpMessagingTemplate template;


    @GetMapping(value = {"/",})
    public String index(HttpSession session) {

        if (session.getAttribute(SESSION_KEY) == null) {
            return "redirect:/login";
        }
        return "index";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute(SESSION_KEY);
        return "redirect:/login";

    }



    @GetMapping("/register")
    public String register(HttpSession session) {

        if (session.getAttribute(SESSION_KEY) == null) {
            return "register";
        }
        return "redirect:/";

    }

    @GetMapping("/login")
    public String login(HttpSession session) {

        if (session.getAttribute(SESSION_KEY) == null) {
            return "login";
        }
        return "redirect:/";

    }

    @PostMapping("/login")
    public String login(HttpSession session, @RequestParam String username, @RequestParam String password) {

        try {
            User user = userService.findByUsername(username);
            if (user != null && passwordEncoder.matches(password, user.getPassword())) {

                session.setAttribute(SESSION_KEY, user);

                return "redirect:/";
            }else{
                return "redirect:/login?error=invalid_username_password";
            }
        } catch (Exception e) {
            return "redirect:/login?error=" + e.getMessage().toString();
        }

    }


    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String register(@RequestParam String username, @RequestParam String password, @RequestParam(required = false) MultipartFile image) {

        return userService.registerUser(username, password, image);

    }

}
