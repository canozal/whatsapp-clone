package com.beam.whatsapp;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CryptoTest {



    @Test
    public void testCrypto(){
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode("123456");
        String hashedPassword2 = encoder.encode("123456");
        System.out.println(hashedPassword);

        assertTrue(encoder.matches("123456", hashedPassword));
    }


}
